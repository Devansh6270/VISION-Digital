package com.vision_classes.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vision_classes.BuildConfig;
import com.vision_classes.R;
import com.vision_classes.helperClasses.JSONParser;
import com.vision_classes.model.myCourses.ItemMyCourse;
import com.vision_classes.model.myCourses.ItemMyCourseAdapter;
import com.vision_classes.model.videoItem.VideoAdapter;
import com.vision_classes.model.videoItem.VideoItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeachersDetailsActivity extends AppCompatActivity {

    String teacherId = "";
    String getTeacherUrl = "";
    ProgressDialog dialog;
    SimpleExoPlayer prevPlayer, currentPlayer;

    private DiscreteScrollView itemPicker;
    //    private InfiniteScrollAdapter<?> infiniteAdapter;
    private VideoAdapter videoAdapter;
    List<VideoItem> videoItemList = new ArrayList<>();

    ItemMyCourseAdapter itemCoursesAdapter;
    RecyclerView CourseslistView;

    ArrayList<ItemMyCourse> coursesList = new ArrayList<>();

    //Layout Setup--------------------------
    TextView teachersName, teachersQualification, teachersDesc,popularCoursesBy,know_more;
    ImageView teachersImg;
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_teachers_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dialog = new ProgressDialog(TeachersDetailsActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading");
        dialog.show();

        //Layout-------------------------
        teachersName = findViewById(R.id.teachersName);
        teachersQualification = findViewById(R.id.teacherQualification);
        teachersImg = findViewById(R.id.teachersImage);
        teachersDesc = findViewById(R.id.teachDesc);
        popularCoursesBy = findViewById(R.id.popularCoursesBy);
        know_more = findViewById(R.id.know_more);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        teacherId = getIntent().getStringExtra("id");
        if (teacherId==null){
            Uri data =getIntent().getData();
            List<String> param = data.getPathSegments();
            String first = param.get(0);
            teacherId = first;
        }

//        final String url = "https://chalksnboard.com/Home/teacherProfileDetails/"+teacherId+teachersName;


        //courses-------------
        CourseslistView = findViewById(R.id.CourseList);


        itemPicker = findViewById(R.id.videosList);
        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.setSlideOnFling(true);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        itemPicker.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
//                int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);
                onItemChanged(videoItemList.get(adapterPosition));
            }
        });



        getTeacherUrl = getApplicationContext().getString(R.string.apiURL) + "getTeacherDetails";



    }

    class GetTeacherDetails extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
            //            this.dialog.setMessage("Please wait");
            //            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(TeachersDetailsActivity.this);
            int versionCode = BuildConfig.VERSION_CODE;
            String param = "teacher_id=" + teacherId;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(getTeacherUrl, "POST", param);
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
                    String status = jsonObject.getString("status");
                    final JSONObject dataObj = jsonObject.getJSONObject("data");
                    switch (status) {
                        case "success":
                            //Running Fine

                            JSONArray promoVideoList = dataObj.getJSONArray("teacher_promo_link");

                            //Profile setup
                            String teacherName = dataObj.getString("name");
                            String qualification = dataObj.getString("qualification");
                            String imgurl = dataObj.getString("image");
                            String description = dataObj.getString("long_intro");
                            String ownedBy = dataObj.getString("owned_by");

                            Glide.with(TeachersDetailsActivity.this).load(imgurl).into(teachersImg);
                            teachersDesc.setText(description);
                            teachersName.setText(teacherName);
                            popularCoursesBy.setText("Popular Courses by "+ownedBy);
                            teachersQualification.setText(qualification);

                            final String url = "https://chalksnboard.com/teacherProfile/"+teacherId+"/"+teacherName.trim();
//                            final String url = "https://chalksnboard.com/Home/teacherProfileDetails/"+teacherId;
                            Log.e("url",url);

                            know_more.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(browserIntent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                            });

                            videoItemList.clear();
                            for (int i = 0; i < promoVideoList.length(); i++) {
                                VideoItem videoItem = new VideoItem();
                                videoItem.setVideoUrl((String) promoVideoList.get(i));
//                                videoItem.setVideoUrl("https://www.dropbox.com/s/0x2ke57h7wv49ll/Sample_512x288.mp4");
                                Log.e("link", (String) promoVideoList.get(i));
                                videoItemList.add(videoItem);
                            }
//                            infiniteAdapter = InfiniteScrollAdapter.wrap();
                            videoAdapter = new VideoAdapter(videoItemList);
                            itemPicker.setAdapter(videoAdapter);
                            onItemChanged(videoItemList.get(0));

                            coursesList.clear();
                            JSONArray coursesListArray = dataObj.getJSONArray("course");
                            for (int i = 0; i < coursesListArray.length(); i++) {
                                ItemMyCourse itemMyCourse = new ItemMyCourse();
                                JSONObject coursesJSONObject = coursesListArray.getJSONObject(i);
                                itemMyCourse.setId(coursesJSONObject.getString("id"));
//                                itemMyCourse.setId("1");
                                itemMyCourse.setImage(coursesJSONObject.getString("image"));
                                itemMyCourse.setDescription(coursesJSONObject.getString("description"));
                                itemMyCourse.setDuration(coursesJSONObject.getString("duration"));
                                itemMyCourse.setOwner_name(coursesJSONObject.getString("owner_name"));
                                itemMyCourse.setTitle(coursesJSONObject.getString("title"));
                                itemMyCourse.setOwner_qualification(coursesJSONObject.getString("owner_qualification"));
//                                itemMyCourse.setOwner_qualification("Phd");

                                coursesList.add(itemMyCourse);
                                Log.e("Added", "1");

                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(TeachersDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            CourseslistView.setLayoutManager(layoutManager);
                            itemCoursesAdapter = new ItemMyCourseAdapter(coursesList);
                            CourseslistView.setAdapter(itemCoursesAdapter);
                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TeachersDetailsActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = TeachersDetailsActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
                            String msgContent = dataObj.getString("message");
                            maintainanceContent.setText(Html.fromHtml(msgContent));

                            TextView btnOK = dialogView.findViewById(R.id.btnOK);
                            btnOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finishAndRemoveTask();
                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            break;
                        case "failure":
                            Toast.makeText(TeachersDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(TeachersDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
            dialog.dismiss();
        }
    }

    private void onItemChanged(VideoItem item) {
        try {
            prevPlayer = currentPlayer;
            Log.e("Player", "" + currentPlayer + item);

            currentPlayer = item.getPlayer();
            currentPlayer.prepare();
            prevPlayer.pause();
//            prevPlayer.release();

        } catch (NullPointerException e) {
            Log.e("Player", "NoPlayer");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetTeacherDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            currentPlayer.pause();
            currentPlayer.release();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            currentPlayer.pause();
            currentPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            currentPlayer.pause();
            currentPlayer.release();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
