package com.vision_digital.model.continueWatchingCourseItem;

import static com.vision_digital.activities.DashboardActivity.sid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.github.mikephil.charting.components.YAxis;
import com.vision_digital.activities.CourseDetailsActivity;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.activities.MileStoneVideoPlayerActivity;
import com.vision_digital.R;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.chapters.ItemChapter;
import com.vision_digital.model.milestone.ItemMileStone;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.exoplayer2.MediaItem;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<CourseItem> courseItemList;
    int course_pos = 0;


    public CourseAdapter(List<CourseItem> courseItemList) {
        this.courseItemList = courseItemList;
    }

    Context context;

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
    String chap_id = "";
    FirebaseUser user;
    String lastMilestoneId = "";
    private String courseLogo = "";
    //Play Button


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        courseDetailsUrl = context.getString(R.string.apiURL) + "getCourseDetails";
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_continue_watching, parent, false);
        return new CourseAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        course_pos = position;

        holder.milestoneName.setText(courseItemList.get(position).getMilestoneName());
        holder.courseName.setText(courseItemList.get(position).getCourseTitle());
        lastMilestoneId = courseItemList.get(position).getLastPlayedMilestoneId();
//        holder.percenatge.setText((int) courseItemList.get(position).getPercentageWatched()+"%\nCompleted");


        if (courseItemList.get(position).getAccuracy() == 0) {
            holder.progress_bar_accuracy.setProgress(1);
            holder.accuracyPercentage.setText(String.valueOf(1));

            Log.e("ZeroAccuracy", String.valueOf(courseItemList.get(position).getAccuracy()));

        } else {
            holder.accuracyPercentage.setText(String.valueOf((int) courseItemList.get(position).getAccuracy()));
            holder.progress_bar_accuracy.setProgress((int) courseItemList.get(position).getAccuracy());

        }

        if (position == 0) {
            if (courseItemList.get(position).getAccuracy() == 0) {
                holder.progress_bar_accuracy.setProgress(1);
                holder.accuracyPercentage.setText("01");

                Log.e("ZeroAccuracy", String.valueOf(courseItemList.get(position).getAccuracy()));

            } else {
                if (courseItemList.get(position).getAccuracy() < 10) {
                    holder.accuracyPercentage.setText("0" + String.valueOf((int) courseItemList.get(position).getAccuracy()));

                } else {
                    holder.accuracyPercentage.setText(String.valueOf((int) courseItemList.get(position).getAccuracy()));

                }
                holder.progress_bar_accuracy.setProgress((int) courseItemList.get(position).getAccuracy());

            }
        } else {
            if (courseItemList.get(position).getAccuracy() < 10) {
                holder.accuracyPercentage.setText("0" + String.valueOf((int) courseItemList.get(position).getAccuracy()));

            } else {
                holder.accuracyPercentage.setText(String.valueOf((int) courseItemList.get(position).getAccuracy()));

            }
            holder.progress_bar_accuracy.setProgress((int) courseItemList.get(position).getAccuracy());
        }


        if (courseItemList.get(position).getPercentageWatched() == 0) {
            holder.progress_bar_completion.setProgress(1);
            Log.e("ZeroCompletion", String.valueOf(courseItemList.get(position).getPercentageWatched()));
        } else {
            holder.progress_bar_completion.setProgress((int) courseItemList.get(position).getPercentageWatched());

        }
        if (courseItemList.get(position).getPercentageWatched() < 10) {
            holder.completedPercentage.setText("0" + String.valueOf((int) courseItemList.get(position).getPercentageWatched()));

        } else {
            holder.completedPercentage.setText(String.valueOf((int) courseItemList.get(position).getPercentageWatched()));

        }
//        holder.accuracyPercentage.setText(String.valueOf((int) courseItemList.get(position).getAccuracy()));
        holder.progress_bar_accuracy.setMax(100);
        holder.progress_bar_completion.setMax(100);

//        double second = courseItemList.get(position).getActualPlayDuration();
//
//        int remainder = (int) second%60;
//        int var  = (int) second-remainder;
//        int mins = var / 60;
//
//
//        if (mins <= 1) {
//            holder.spentTime.setText(mins + " min");
//        } else {
//            holder.spentTime.setText(mins + " mins");
//        }


        double second = courseItemList.get(position).getActualPlayDuration();
        int hours = (int) second / 3600;
        int remainder = (int) second - hours * 3600;
        int mins = remainder / 60;


        holder.spentTime.setVisibility(View.VISIBLE);

        if (hours < 10 && mins < 10) {
            holder.spentTime.setText("0" + hours + ":0" + mins + " Hrs");
        } else if (hours == 0 && mins < 10) {
            holder.spentTime.setText("00" + ":0" + mins + " Hrs");

        } else if (hours == 0 && mins > 10) {
            holder.spentTime.setText("00" + ":" + mins + " Hrs");

        } else if (hours > 10 && mins < 10) {
            holder.spentTime.setText(hours + ":0" + mins + " Hrs");

        } else if (hours < 10 && mins > 10) {
            holder.spentTime.setText("0" + hours + ":" + mins + " Hrs");

        } else {
            holder.spentTime.setText("" + hours + ":" + mins + " Hrs");
        }

        Log.e("ActualPlayDuration", String.valueOf(mins));


//        double secondT = courseItemList.get(position).getCourseDuration();
//        int remainderT = (int) secondT%60;
//        int varT  = (int) secondT-remainderT;
//        int minsT  = varT/ 60;
//
//
//        if (minsT <= 1) {
//            holder.requiredTime.setText(minsT + " min");
//        } else {
//            holder.requiredTime.setText(minsT + " mins");
//        }
//


        double secondT = courseItemList.get(position).getCourseDuration();
        int hoursT = (int) secondT / 3600;
        int remainderT = (int) secondT - hoursT * 3600;
        int minsT = remainderT / 60;


        if (hoursT < 10 && minsT < 10) {
            holder.requiredTime.setText("0" + hoursT + ":0" + minsT + " Hrs");
        } else if (hoursT == 0 && minsT < 10) {
            holder.spentTime.setText("00" + ":0" + minsT + " Hrs");

        } else if (hoursT == 0 && minsT > 10) {
            holder.requiredTime.setText("00" + ":" + minsT + " Hrs");

        } else if (hoursT > 10 && minsT < 10) {
            holder.requiredTime.setText(hoursT + ":0" + minsT + " Hrs");

        } else if (hoursT < 10 && minsT > 10) {
            holder.requiredTime.setText("0" + hoursT + ":" + minsT + " Hrs");

        } else {
            holder.requiredTime.setText("" + hoursT + ":" + minsT + " Hrs");
        }

        Log.e("courseDuration", String.valueOf(minsT));


//        int playPercentage = mins / minsT * 100;
        holder.percentageProgressBar.setProgress((int) courseItemList.get(position).getPercentageWatched());
//        Log.e("playPercentage", String.valueOf(playPercentage));

        holder.home_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < courseItemList.size(); i++) {
                    if (i == position) {
                        courseId = courseItemList.get(position).getCourseId();

                    }

                }
                new GetCourseDetailsDash().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent courseIntent = new Intent(context, CourseDetailsActivity.class);
//                courseIntent.putExtra("id", courseItemList.get(position).getCourseId());
//                context.startActivity(courseIntent);
//            }
//        });

        //graph

        holder.mchart.setDescription(null);
        holder.mchart.getAxisRight().setEnabled(false);
        holder.mchart.getAxisLeft().setEnabled(true);
        holder.mchart.getAxisLeft().setAxisLineWidth(2f);
        holder.mchart.getAxisLeft().setAxisMinValue(0);
        holder.mchart.getAxisLeft().setGridLineWidth(0.25f);
        holder.mchart.getAxisLeft().setDrawGridLinesBehindData(true);
        holder.mchart.setClickable(false);
        holder.mchart.setDrawMarkers(false);
        holder.mchart.getAxisLeft().setTextColor(Color.parseColor("#ffffff"));
        holder.mchart.getAxisLeft().setAxisLineColor(Color.parseColor("#FFF212"));

        final ArrayList<String> axiss = new ArrayList<>();
        for (int a = 0; a < courseItemList.get(position).getActualTimeList().size(); a++) {
            axiss.add("v-" + a);
        }


        XAxis xAxis = holder.mchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);
        xAxis.setAxisLineWidth(2f);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.parseColor("#FFF212"));
        xAxis.setLabelRotationAngle(0);
        xAxis.setTextColor(Color.parseColor("#ffffff"));


        YAxis leftYAxis = holder.mchart.getAxisLeft();
        leftYAxis.setTextColor(Color.parseColor("#ffffff")); // Set label text color

        YAxis rightYAxis = holder.mchart.getAxisRight();
        rightYAxis.setTextColor(Color.parseColor("#ffffff")); // Set label text color

//        xAxis.setValueFormatter(new IndexAxisValueFormatter(axiss));

        Log.e("12345", String.valueOf(courseItemList.get(position).getSpentTimeList()));


        LineDataSet set1, set2;

        set1 = new LineDataSet(courseItemList.get(position).getActualTimeList(), "Required time");
        set1.setColor(Color.parseColor("#ffffff"));
        set1.setDrawCircleHole(false);
        set1.setLineWidth(1.8f);
        set1.setDrawValues(false);
        set1.setDrawCircles(false);

//        set1.setCircleRadius(0);
//        set1.setCircleColor(Color.parseColor("#ffffff"));
        //to make the smooth line as the graph is adrapt change so smooth curve
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //to enable the cubic density : if 1 then it will be sharp curve
        set1.setCubicIntensity(0.2f);

        set1.enableDashedLine(20, 18, 0);


        set2 = new LineDataSet(courseItemList.get(position).getSpentTimeList(), "Spent time");
        set2.setColor(Color.parseColor("#FFF212"));
        set2.setDrawCircleHole(false);
        set2.setLineWidth(2.5f);
//        set1.setCircleRadius(0);
        set2.setDrawCircles(false);

        set2.setDrawValues(false);
//        set2.setCircleColor(Color.parseColor("#F58E5A"));
        //to make the smooth line as the graph is adrapt change so smooth curve
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //to enable the cubic density : if 1 then it will be sharp curve
        set2.setCubicIntensity(0.2f);


        LineData data = new LineData(set1, set2);
        holder.mchart.setData(data);
        holder.mchart.animateX(1200);
    }

    @Override
    public int getItemCount() {
        return courseItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView courseName, milestoneName, completedPercentage, accuracyPercentage, spentTime, requiredTime;
        ProgressBar percentageProgressBar, progress_bar_completion, progress_bar_accuracy;
        LineChart mchart;
        ImageView home_play_button;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseName);
            milestoneName = itemView.findViewById(R.id.mileStoneName);
//            percenatge = itemView.findViewById(R.id.percentageCompleted);
            percentageProgressBar = itemView.findViewById(R.id.progressDone);
            progress_bar_completion = itemView.findViewById(R.id.progress_bar_completion);
            progress_bar_accuracy = itemView.findViewById(R.id.progress_bar_accuracy);
            mchart = itemView.findViewById(R.id.graph_home);
            completedPercentage = itemView.findViewById(R.id.completedPercentage);
            accuracyPercentage = itemView.findViewById(R.id.accuracyPercentage);
            spentTime = itemView.findViewById(R.id.spentTime);
            requiredTime = itemView.findViewById(R.id.requiredTime);
            home_play_button = itemView.findViewById(R.id.home_play_button);


        }
    }


    class GetCourseDetailsDash extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(context);
            int versionCode;


            PackageInfo pInfo = null;
            try {
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            versionCode = pInfo.versionCode;
            Log.e("versionCode", String.valueOf(versionCode));



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
                            courseDuration = dataObj.getString("duration");
                            courseOwnerName = dataObj.getJSONObject("owner_details").getString("owned_name");
                            courseOwnerQuali = dataObj.getJSONObject("owner_details").getString("owner_qualification");
                            courseLogo = dataObj.getString("image");

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
                                MediaItem mediaItem = MediaItem.fromUri(previewVideoURI);


                                //Setting content--------------------
                                JSONArray courseContent = dataObj.getJSONArray("course_content");
                                JSONArray monthContent = dataObj.getJSONArray("course_content");

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
                                        Log.e("Milestone Id", lastMilestoneId);
                                        if (mileStoneObj.getString("id").equals(lastMilestoneId)) {
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

                                Intent mileStonePlayer = new Intent(context, MileStoneVideoPlayerActivity.class);
                                mileStonePlayer.putExtra("videoPosition", videoPos);
                                mileStonePlayer.putExtra("mileStonesList", mileStonesArrayList);
                                mileStonePlayer.putExtra("id", courseId);
                                mileStonePlayer.putExtra("logo", courseLogo);
                                mileStonePlayer.putExtra("subscriptionStatus","subscribed");
                                Log.e("putextracourseId", courseId);
                                mileStonePlayer.putExtra("mileUpdateType","chap_mile_click");
                                mileStonePlayer.putExtra("chapter_id", chapter_id);
                                context.startActivity(mileStonePlayer);


                            } else if (subscriptionStatus.equals("unsubscribed")) {
                                Toast.makeText(context, "Subscribe this Course Again!..", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context, CourseDetailsActivity.class);
                                intent.putExtra("id", courseId);
                                intent.putExtra("image",courseLogo);
                                intent.putExtra("fromActivity","homePage");
                                context.startActivity(intent);


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

