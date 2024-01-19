package com.vision_digital.liveClass;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vision_digital.R;
import com.vision_digital.databinding.ActivityLiveClassRoomBinding;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.liveClass.Classroom.ItemClassroomContentAdapter;
import com.vision_digital.model.liveClass.Classroom.ItemClassroomContentModel;
import com.vision_digital.model.liveClass.Classroom.ItemLiveClassDate;
import com.vision_digital.model.liveClass.Classroom.ItemLiveClassesDateWise;
import com.vision_digital.model.liveClass.Classroom.ItemLiveClassesDateWiseAdapter;
import com.vision_digital.model.liveClass.Classroom.ItemLiveDateAdapter;
import com.vision_digital.model.liveClass.NotesClassroomContent.ItemNotesAdapter;
import com.vision_digital.model.liveClass.NotesClassroomContent.ItemNotesList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LiveClassRoomActivity extends AppCompatActivity {

    ActivityLiveClassRoomBinding binding;
    List<ItemClassroomContentModel> itemClassroomContentModelList = new ArrayList<>();

    public static String classRoomDataUrl = "";
    Boolean isSubscribed = false;
    Boolean isLiveClassAvailable=false;
    Boolean isContentAvailable=false;
    public static String currentDate;

    public static String liveCourseID;

    public static int sid;

    List<ItemLiveClassDate> datesList = new ArrayList<>();
    List<ItemLiveClassesDateWise> liveClassesListsDateWise = new ArrayList<>();
    ItemLiveDateAdapter liveDateAdapter;
    ArrayList<ItemNotesList> arrayListNotes = new ArrayList<>();
    private ProgressDialog progressDialog;

    public static ItemLiveClassesDateWiseAdapter itemLiveClassesDateWiseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_live_class_room);
        binding.tvVideos.setPaintFlags(binding.tvVideos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //        To Stop user from video recording or taking screen shot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        classRoomDataUrl = getApplicationContext().getString(R.string.apiURL) + "liveSessionClassroomContent";

        liveCourseID = getIntent().getStringExtra("id");

        progressDialog = new ProgressDialog(LiveClassRoomActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        sid = studDetails.getInt("sid", 0);


        new GetClassRoomDatas().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        LinearLayoutManager layoutManager = new LinearLayoutManager(LiveClassRoomActivity.this, LinearLayoutManager.VERTICAL, false);
        // LinearLayoutManager layoutManager = new GridLayoutManager(LiveClassRoomActivity.this, 2);
        binding.liveClassCardRecyclarView.setLayoutManager(layoutManager);
        itemLiveClassesDateWiseAdapter = new ItemLiveClassesDateWiseAdapter(liveClassesListsDateWise, LiveClassRoomActivity.this);
        binding.liveClassCardRecyclarView.setAdapter(itemLiveClassesDateWiseAdapter);
        binding.recentClassLayout.setVisibility(View.VISIBLE);
        binding.classroomContentLayout.setVisibility(View.GONE);
        binding.classRoomContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(LiveClassRoomActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
                binding.recentClassLayout.setVisibility(View.GONE);
                binding.classroomContentLayout.setVisibility(View.VISIBLE);
                binding.liveClassCardRecyclarView.setVisibility(View.GONE);
                binding.liveClassroomContentRecyclerView.setVisibility(View.VISIBLE);
                binding.loginButtonLog.setBackgroundColor(Color.WHITE);
                binding.loginButtonLog.setTextColor(Color.GRAY);
                binding.classRoomContentBtn.setBackgroundResource(R.drawable.bg_red_round_button);
                binding.classRoomContentBtn.setTextColor(Color.WHITE);

//==================BELOW CODE IS FOR VIDEOS AND NOTES SECTION INSIDE CLASSROOM CONTENT ==========================================
                binding.tvNotes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.liveClassroomContentRecyclerView.setVisibility(View.GONE);
                        binding.tvNotes.setTextColor(Color.RED);
                        binding.tvVideos.setTextColor(Color.LTGRAY);
                        binding.tvNotes.setPaintFlags(binding.tvNotes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        binding.tvVideos.setPaintFlags(binding.tvVideos.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                        binding.notesLiveClassroomContentRecyclerView.setVisibility(View.VISIBLE);
                        binding.liveClassroomContentRecyclerView.setVisibility(View.GONE);
                    }
                });

                binding.tvVideos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.liveClassroomContentRecyclerView.setVisibility(View.VISIBLE);
                        binding.tvNotes.setTextColor(Color.LTGRAY);
                        binding.tvVideos.setTextColor(Color.RED);
                        binding.tvVideos.setPaintFlags(binding.tvVideos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        binding.tvNotes.setPaintFlags(binding.tvNotes.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                        binding.notesLiveClassroomContentRecyclerView.setVisibility(View.GONE);
                        binding.liveClassroomContentRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }
//==================ABOVE CODE IS FOR VIDEOS AND NOTES SECTION INSIDE CLASSROOM CONTENT ==========================================
        });

        binding.loginButtonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.recentClassLayout.setVisibility(View.VISIBLE);
                binding.classroomContentLayout.setVisibility(View.GONE);
                binding.liveClassroomContentRecyclerView.setVisibility(View.GONE);
                binding.liveClassCardRecyclarView.setVisibility(View.VISIBLE);
                binding.loginButtonLog.setBackgroundResource(R.drawable.bg_red_round_button);
                binding.loginButtonLog.setTextColor(Color.WHITE);
                binding.classRoomContentBtn.setBackgroundColor(Color.WHITE);
                binding.classRoomContentBtn.setTextColor(Color.GRAY);
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }



    class GetClassRoomDatas extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(LiveClassRoomActivity.this);


            String param = "student_id=" + sid + "&live_course_id=" + liveCourseID;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(classRoomDataUrl, "POST", param);
            if (jsonObject != null) {
                return jsonObject.toString();
            } else {

            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("json", s);

            if (!s.equals("")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    Log.e("Result : ", s);

                    //Do work-----------------------------
                    progressDialog.dismiss();
                    String status = jsonObject.getString("status");
                    switch (status) {
                        case "success":

                            JSONObject dataObj = jsonObject.getJSONObject("data");

                            isSubscribed = dataObj.getBoolean("is_subscribed");
                            try {
                                JSONArray liveClassArray = dataObj.getJSONArray("now");
                                if (liveClassArray.length()==0){
                                   binding.noDateAvailable.setVisibility(View.VISIBLE);
                                   binding.liveClassCardRecyclarView.setVisibility(View.GONE);
                                }else{
                                    binding.noDateAvailable.setVisibility(View.GONE);
                                    binding.liveClassCardRecyclarView.setVisibility(View.VISIBLE);
                                    liveClassesListsDateWise.clear();
                                    for (int i = 0; i < liveClassArray.length(); i++) {
                                        ItemLiveClassesDateWise itemLiveDate = new ItemLiveClassesDateWise();
                                        JSONObject dateObj = liveClassArray.getJSONObject(i);
                                        itemLiveDate.setTitle(dateObj.getString("title"));
                                        itemLiveDate.setId(String.valueOf(dateObj.getInt("id")));
                                        itemLiveDate.setLiveDate(dateObj.getString("live_date"));
                                        itemLiveDate.setLiveTime(dateObj.getString("live_time"));
                                        itemLiveDate.setShortDesc(dateObj.getString("short_desc"));
                                        itemLiveDate.setDescription(dateObj.getString("description"));
                                        itemLiveDate.setStatus(dateObj.getString("status"));
                                        itemLiveDate.setAccessType(dateObj.getString("access_type"));
                                        itemLiveDate.setVideoUrl(dateObj.getString("video_url"));
                                        itemLiveDate.setLocked(Boolean.valueOf(dateObj.getBoolean("is_locked")));

                                        liveClassesListsDateWise.add(itemLiveDate);

                                    }

                                    itemLiveClassesDateWiseAdapter.notifyDataSetChanged();
                                }





                                JSONObject contentDataObj = dataObj.getJSONObject("content");
                                JSONArray videContentDataObjArray = contentDataObj.getJSONArray("video");
                                if (videContentDataObjArray.length() == 0){
                                    Toast.makeText(LiveClassRoomActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                                    binding.noContentAvailable.setVisibility(View.VISIBLE);
                                    binding.liveClassroomContentRecyclerView.setVisibility(View.GONE);

                                } else {
                                    binding.noContentAvailable.setVisibility(View.GONE);
                                    binding.liveClassroomContentRecyclerView.setVisibility(View.VISIBLE);
                                    itemClassroomContentModelList.clear();
                                    for (int i = 0; i < videContentDataObjArray.length(); i++) {

                                        ItemClassroomContentModel itemClassroomContentModel = new ItemClassroomContentModel();
                                        JSONObject iVideoObj = videContentDataObjArray.getJSONObject(i);
                                        Log.e("TAG","BEFORE SETTING VIDEO DATA LIST ______");
                                        itemClassroomContentModel.setId(iVideoObj.getInt("id"));
                                        itemClassroomContentModel.setLive_course_id(iVideoObj.getInt("live_course_id"));
                                        itemClassroomContentModel.setSort(iVideoObj.getInt("sort"));
                                        itemClassroomContentModel.setTitle(iVideoObj.getString("title"));
                                        itemClassroomContentModel.setDescription(iVideoObj.getString("description"));
                                        itemClassroomContentModel.setStatus(iVideoObj.getString("status"));
                                        itemClassroomContentModel.setAccess_type(iVideoObj.getString("access_type"));
                                        itemClassroomContentModel.setShort_desc(iVideoObj.getString("short_desc"));
                                        itemClassroomContentModel.setIs_locked(iVideoObj.getBoolean("is_locked"));
                                        itemClassroomContentModel.setCreated_at(iVideoObj.getString("created_at"));
                                        itemClassroomContentModel.setUpdated_at(iVideoObj.getString("updated_at"));
                                        itemClassroomContentModel.setLive_date(iVideoObj.getString("live_date"));
                                        itemClassroomContentModel.setLive_time(iVideoObj.getString("live_time"));
                                        itemClassroomContentModel.setVideo_url(iVideoObj.getString("video_url"));
                                        itemClassroomContentModel.setVideo_type(iVideoObj.getString("video_type"));
                                        itemClassroomContentModelList.add(itemClassroomContentModel);
                                    }
                                }
                                Log.e("TAG","Before Setting video adapter");
                                LinearLayoutManager layoutManager = new LinearLayoutManager(LiveClassRoomActivity.this, LinearLayoutManager.VERTICAL, false);
                                binding.liveClassroomContentRecyclerView.setLayoutManager(layoutManager);
                                Log.e("TAG", " Content onPostExecute: "+"Before setting video Adapter");
                                ItemClassroomContentAdapter itemClassroomContentAdapter = new ItemClassroomContentAdapter( LiveClassRoomActivity.this, itemClassroomContentModelList);
                                binding.liveClassroomContentRecyclerView.setAdapter(itemClassroomContentAdapter);

                                JSONArray notesContentDataObjArray = contentDataObj.getJSONArray("notes");
                                Log.e("Notes live", notesContentDataObjArray.toString());
                                if (notesContentDataObjArray.length() == 0){
                                    binding.notesLiveClassroomContentRecyclerView.setVisibility(View.GONE);
                                } else {
                                    binding.notesLiveClassroomContentRecyclerView.setVisibility(View.VISIBLE);
                                    arrayListNotes.clear();
                                    for (int i = 0; i < notesContentDataObjArray.length(); i++){
                                        ItemNotesList itemNotesList = new ItemNotesList();
                                        JSONObject iNotesObj = notesContentDataObjArray.getJSONObject(i);
                                        Log.e("TAG", "Before setting notes data list");
                                        itemNotesList.setNotesTitle(iNotesObj.getString("title"));
                                        itemNotesList.setNotesId(iNotesObj.getInt("id"));
                                        itemNotesList.setAccess_type(iNotesObj.getString("access_type"));
                                        itemNotesList.setLive_course_id(iNotesObj.getInt("live_course_id"));
                                        itemNotesList.setIs_locked(iNotesObj.getBoolean("is_locked"));
                                        itemNotesList.setCreated_at(iNotesObj.getString("created_at"));
                                        itemNotesList.setUpdated_at(iNotesObj.getString("updated_at"));
                                        itemNotesList.setStatus(iNotesObj.getString("status"));
                                        itemNotesList.setUrl(iNotesObj.getString("url"));
                                        arrayListNotes.add(itemNotesList);

                                    }
                                }

                                Log.e("TAG", "Before setting notes adapter");
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LiveClassRoomActivity.this, LinearLayoutManager.VERTICAL, false);
                                binding.notesLiveClassroomContentRecyclerView.setLayoutManager(linearLayoutManager);
                                Log.e("TAG", " Content onPostExecute: "+"Before setting notes Adapter");
                                ItemNotesAdapter itemNotesAdapter = new ItemNotesAdapter(LiveClassRoomActivity.this, arrayListNotes);
                                binding.notesLiveClassroomContentRecyclerView.setAdapter(itemNotesAdapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


//                            LinearLayoutManager layoutManager = new LinearLayoutManager(LiveClassRoomActivity.this, LinearLayoutManager.HORIZONTAL, false);
//                            // LinearLayoutManager layoutManager = new GridLayoutManager(LiveClassRoomActivity.this, 2);
//                            binding.datesRecyclarView.setLayoutManager(layoutManager);
//                            Log.e("TAG", "onPostExecute: "+"Before Setting Adapter" );
//                            liveDateAdapter = new ItemLiveDateAdapter(datesList,LiveClassRoomActivity.this);
//                            binding.datesRecyclarView.setAdapter(liveDateAdapter);


                            break;
                        case "maintenance":
                            //Undermaintance

                            break;
                        case "failure":
                            Toast.makeText(LiveClassRoomActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LiveClassRoomActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
        }
    }

}