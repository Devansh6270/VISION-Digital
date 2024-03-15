package com.vision_classes.model.searchResults;

import static com.vision_classes.activities.DashboardActivity.sid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_classes.activities.CourseDetailsActivity;
import com.vision_classes.activities.DashboardActivity;
import com.vision_classes.activities.MileStoneVideoPlayerActivity;
import com.vision_classes.R;
import com.vision_classes.activities.TeachersDetailsActivity;
import com.vision_classes.helperClasses.JSONParser;
import com.vision_classes.model.chapters.ItemChapter;
import com.vision_classes.model.milestone.ItemMileStone;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.grpc.android.BuildConfig;

public class ItemSearchAdapter extends RecyclerView.Adapter<ItemSearchViewHolder> {

    List<ItemSearch> searchList;
    Context context;

    public ItemSearchAdapter(List<ItemSearch> searchList) {
        this.searchList = searchList;
    }

    String courseDetailsUrl = "";
//            "https://chalksnboard.com/api/v2/getCourseDetails";
    String courseId = "";
    String subscriptionStatus = "";
    String courseDuration = "";
    String courseOwnerName = "", courseOwnerQuali = "";
    List<Long> forMonths = new ArrayList<>();
    Uri previewVideoURI;
    ArrayList<Uri> courseMilestoneList = new ArrayList<>();
    ArrayList<ItemMileStone> mileStonesArrayList = new ArrayList<>();
    ArrayList<ItemChapter> chaptersList = new ArrayList<>();
    int chapter_id;
    String courseDescription;
    int videoPos = 0;
    int videoPosTemp = 0;
    //sss
    String chap_id = "";
    FirebaseUser user;
    String milestoneId = "";

    @NonNull
    @Override
    public ItemSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        courseDetailsUrl = context.getString(R.string.apiURL) + "getCourseDetails";
        return new ItemSearchViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemSearchViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.title.setText(searchList.get(position).getTitle());
        courseId = searchList.get(position).getCid();
        milestoneId = searchList.get(position).getId();
        Log.e("milestoneId",milestoneId);
        Log.e("courseId",courseId);
        if (searchList.get(position).getTitle().length()>=21){
            holder.title_dots.setVisibility(View.VISIBLE);
        }
        holder.title_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.title.setMaxLines(3);
                holder.title_dots.setVisibility(View.GONE);

            }
        });

        if (searchList.get(position).getDescription().length()>=20){
            holder.title_dots_desc.setVisibility(View.VISIBLE);
        }
        holder.title_dots_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.description.setMaxLines(3);
                holder.title_dots_desc.setVisibility(View.GONE);

            }
        });
        if(!searchList.get(position).getDuration().equals("")){
            double second = Double.parseDouble(searchList.get(position).getDuration());

            int remainder = (int) second%60;
            int var  = (int) second-remainder;
            int mins = var / 60;



            if (mins <= 1) {
                holder.duration_text.setText(mins + " min");
            } else {
                holder.duration_text.setText(mins + " mins");
            }
        }

//        holder.duration_text.setText(searchList.get(position).getDuration());
        holder.description.setText(searchList.get(position).getDescription());
        Glide.with(context).load(searchList.get(position).getImageUrl()).into(holder.image);
        holder.ownedBy.setText(searchList.get(position).getOwnedBy());

        Log.e("title",searchList.get(position).getTitle());
        Log.e("Description",searchList.get(position).getDescription());
        Log.e("Duration",searchList.get(position).getDuration());
        Log.e("owner",searchList.get(position).getOwnedBy());
        Log.e("image",searchList.get(position).getImageUrl());


        if (searchList.get(position).getTitle().equals("")){
            holder.title.setVisibility(View.GONE);
        }if (searchList.get(position).getDuration().equals("")){
            holder.duration_linear
                    .setVisibility(View.GONE);
        }if (searchList.get(position).getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }if (searchList.get(position).getOwnedBy().equals("")){
            holder.ownedBy.setVisibility(View.GONE);
        }if (searchList.get(position).getImageUrl().equals("")){
            holder.image.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(searchList.get(position).getType().equals("course")){
                    Intent courseIntent = new Intent(context,CourseDetailsActivity.class);
                    courseIntent.putExtra("id",searchList.get(position).getId());
                    courseIntent.putExtra("image","");
                    courseIntent.putExtra("fromActivity","searchPage");
                    context.startActivity(courseIntent);
                }
                if(searchList.get(position).getType().equals("teacher")){
                    Intent teacherIntent = new Intent(context, TeachersDetailsActivity.class);
                    teacherIntent.putExtra("id",searchList.get(position).getId());
                    teacherIntent.putExtra("fromActivity","searchPage");
                    context.startActivity(teacherIntent);
                }
                if(searchList.get(position).getType().equals("milestone")){
                    courseId = searchList.get(position).getCid();
                    milestoneId = searchList.get(position).getId();
                    new GetCourseDetailsSearch().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                else {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList != null ? searchList.size() : 0;
    }

    class GetCourseDetailsSearch extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(context);
            int versionCode = BuildConfig.VERSION_CODE;
            String param = "uid=" + DashboardActivity.uid + "&app_version=" + versionCode + "&student_id=" + sid + "&course_id=" + courseId;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(courseDetailsUrl, "POST", param);
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

                    String available_subscription_months, subscribed_months;
                    switch (status) {
                        case "success":
                            //Running Fine
                            int positionNumber = 0;

                            //Setting course Details for subscribed course--------------------------
                            subscriptionStatus = dataObj.getString("subscription_status");

                            String courseName = dataObj.getString("title");
                            String courseDesc = dataObj.getString("description");
                            String imageCourse = dataObj.getString("image");
                            courseDuration = dataObj.getString("duration");
                            courseOwnerName = dataObj.getJSONObject("owner_details").getString("owned_name");
                            courseOwnerQuali = dataObj.getJSONObject("owner_details").getString("owner_qualification");

                            if (subscriptionStatus.equals("subscribed")) {

                                int totalMonths = dataObj.getInt("available_subscription_months");
                                int currentSubsMonths = dataObj.getInt("subscribed_months");
                                if (totalMonths <= currentSubsMonths) {
                                } else {
                                    JSONArray subscriptionJsonArray = dataObj.getJSONArray("available_subscriptions");
                                    forMonths.clear();
                                }
                                courseDescription = courseDesc;
                                previewVideoURI = Uri.parse(dataObj.getString("promo_video_link"));

                                //Setting content--------------------
                                JSONArray courseContent = dataObj.getJSONArray("course_content");

                                chaptersList.clear();
                                courseMilestoneList.clear();
                                mileStonesArrayList.clear();

                                ProgressDialog dialogForIntent = new ProgressDialog(context);
                                dialogForIntent.setMessage("Please wait");
                                dialogForIntent.show();


                                for (int i = 0; i < courseContent.length(); i++) {
                                    ItemChapter chapter = new ItemChapter();
                                    JSONObject chapterObj = courseContent.getJSONObject(i);

                                    chapter.setId(chapterObj.getString("id"));
                                    chapter.setTitle("Chapter " + (i + 1) + ": " + chapterObj.getString("title"));
                                    chapter.setSort_order(chapterObj.getString("sort_order"));
                                    chapter.setMin_month("Subscription Month: " + chapterObj.getString("min_month"));

                                    int subMonths = Integer.parseInt(chapterObj.getString("min_month"));

                                    JSONArray notestones = chapterObj.getJSONArray("notes");

                                    JSONArray milestones = chapterObj.getJSONArray("milestones");
                                    for (int j = 0; j < milestones.length(); j++) {
                                        dialogForIntent.show();
                                        ItemMileStone mileStone = new ItemMileStone();
                                        JSONObject mileStoneObj = milestones.getJSONObject(j);
                                        mileStone.setId(mileStoneObj.getString("id"));
//                                        mileStone.setVideoPosition(positionNumber++);
                                        videoPosTemp = positionNumber++;
                                        mileStone.setVideoPosition(videoPosTemp);
                                        Log.e("Topic name", "" + mileStoneObj.getString("title"));
                                        mileStone.setTitle(mileStoneObj.getString("title"));
                                        mileStone.setDuration(mileStoneObj.getString("duration"));
                                        mileStone.setSort_order(mileStoneObj.getString("sort_order"));
                                        mileStone.setVideoUrl(mileStoneObj.getString("video_link"));
                                        mileStone.setActivityType("courseDetails");

                                        mileStone.setSelected(false);

//                                        String video = mileStoneObj.getString("video_link");
//                                        String lastThree = video.substring(video.length() - 3, video.length());

                                        if (!mileStoneObj.getString("video_link").equals("")) {
                                            courseMilestoneList.add(Uri.parse(mileStoneObj.getString("video_link")));
                                        }
                                        if (!mileStoneObj.getString("duration").equals("1")) {
                                            mileStonesArrayList.add(mileStone);

                                        }
                                        Log.e("Milestone Id", milestoneId);
                                        if (mileStoneObj.getString("id").equals(milestoneId)) {
                                            videoPos = videoPosTemp;
                                            Log.e("videoPos", String.valueOf(videoPos));

                                            chap_id = chapterObj.getString("id");
                                            Log.e("chap_id", String.valueOf(chap_id));

                                            chapter_id = Integer.parseInt(chapterObj.getString("id"));
                                            Log.e("chapter_id", String.valueOf(chapter_id));

                                        } else {
                                            Log.e("No video Pos", "no video pos");
                                        }
                                        mileStone.setMilestoneType("chap_mile_click");
                                        mileStone.setChapterId(chapter_id);

                                        chapter.mileStonesList.add(mileStone);
                                        chapter.setItemMileStone(mileStone);
                                    }

                                    chaptersList.add(chapter);

                                }
                                dialogForIntent.dismiss();
                                dialog.dismiss();

//

//
                                Intent mileStonePlayer = new Intent(context, MileStoneVideoPlayerActivity.class);
                                mileStonePlayer.putExtra("id",courseId);
                                mileStonePlayer.putExtra("videoPosition", videoPos);
                                mileStonePlayer.putExtra("mileStonesList", mileStonesArrayList);
                                mileStonePlayer.putExtra("chapter_id", chap_id);
                                mileStonePlayer.putExtra("logo", imageCourse);
                                mileStonePlayer.putExtra("subscriptionStatus","subscribed");
                                mileStonePlayer.putExtra("name", courseName);
                                mileStonePlayer.putExtra("mileUpdateType","chap_mile_click");
                                mileStonePlayer.putExtra("logo", CourseDetailsActivity.courseLogo);

                                context.startActivity(mileStonePlayer);


                            } else if (subscriptionStatus.equals("unsubscribed")) {
                                Intent courseIntent = new Intent(context, CourseDetailsActivity.class);
                                courseIntent.putExtra("id", courseId);
                                courseIntent.putExtra("image","");
                                courseIntent.putExtra("fromActivity","searchPage");
                                context.startActivity(courseIntent);
//
                            }

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
                                    ((Activity) context).finish();
                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
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
            dialog.dismiss();
        }
    }
}