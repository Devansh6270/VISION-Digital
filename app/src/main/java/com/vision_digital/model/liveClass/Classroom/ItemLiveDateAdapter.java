package com.vision_digital.model.liveClass.Classroom;
import static com.vision_digital.activities.DashboardActivity.sid;
import static com.vision_digital.liveClass.LiveClassRoomActivity.currentDate;
import static com.vision_digital.liveClass.LiveClassRoomActivity.liveCourseID;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.vision_digital.R;
import com.vision_digital.helperClasses.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemLiveDateAdapter extends RecyclerView.Adapter<ItemLiveDateAdapter.ItemLiveDateViewHolder>{

    List<ItemLiveClassDate> liveClassDatesList;
    Context context;
    int selectedPosition = -1;
     private ProgressDialog progressDialog;

    private Handler handler = new Handler();


  public  static  List<ItemLiveClassesDateWise> liveClassesListsDateWise = new ArrayList<>();


    //  For Date wise Data


    String selectedDate;

    public ItemLiveDateAdapter(List<ItemLiveClassDate> liveClassDatesList, Context context) {
        this.liveClassDatesList = liveClassDatesList;
        this.context = context;
    }



    @NonNull
    @Override
    public ItemLiveDateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemLiveDateViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_live_class_date, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemLiveDateViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {

            Log.e("TAG","IN ITEMLIVE DATE ADAPTER");

            holder.date.setText(liveClassDatesList.get(position).getDate());

            if (currentDate.equals(liveClassDatesList.get(position).getDate())){
                holder.onTap.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red));
                holder.date.setTextColor(ContextCompat.getColor(context, R.color.white));
            }

//            if (position == selectedPosition) {
//                // Set the selected card color to red and text color to white
//                holder.onTap.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red));
//                holder.date.setTextColor(ContextCompat.getColor(context, R.color.white));
//            } else {
//                // Set the rest of the cards to their default colors
//                holder.onTap.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
//                holder.date.setTextColor(ContextCompat.getColor(context, R.color.grey));
//            }


            selectedDate=currentDate;
            new GetClassRoomData().execute();

            holder.onTap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("click","click");


                    selectedDate=liveClassDatesList.get(position).getDate();
                    showProgressDialog();
                    new GetClassRoomData().execute();


                    selectedPosition = position;





//                    holder.onTap.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red));
//                    holder.date.setTextColor(ContextCompat.getColor(context,R.color.white));
//                    holder.month.setTextColor(ContextCompat.getColor(context,R.color.white));


                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return  liveClassDatesList != null ? liveClassDatesList.size() : 0;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }



    public class ItemLiveDateViewHolder extends RecyclerView.ViewHolder{

        TextView date;
        TextView month;

        CardView onTap;

        public ItemLiveDateViewHolder(@NonNull View itemView) {
            super(itemView);

            onTap =itemView.findViewById(R.id.dateMonthLayout);
            date = itemView.findViewById(R.id.date);


        }
    }

    class GetClassRoomData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(context);




            String param = "student_id="+sid + "&live_course_id="+liveCourseID;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest("", "POST", param);
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
                    dismissProgressDialog();
                    String status = jsonObject.getString("status");
                    switch (status) {
                        case "success":

                            JSONObject dataObj = jsonObject.getJSONObject("data");

                           // currentDate=dataObj.getString("current_date");
                            try{
                                JSONObject recentObj = dataObj.getJSONObject("recent");
                                Log.e("TAG", "Selected Date: "+ selectedDate );
                                JSONArray liveClassArray =recentObj.getJSONArray(selectedDate);

                                if (liveClassArray.length() == 0) {

                                } else {
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

                                  //  itemLiveClassesDateWiseAdapter.notifyDataSetChanged();



                                }
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
                            Toast.makeText(context, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(context, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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
