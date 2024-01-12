package com.vision_digital.community.invitation;

import static com.vision_digital.model.milestone.ItemMileStoneAdapter.isHaveMilestoneId;
import static com.vision_digital.model.milestone.ItemMileStoneAdapter.selectedMilestoneID;
import static com.vision_digital.model.milestone.ItemMileStoneAdapter.selectedMilestoneName;
import static com.vision_digital.model.milestone.ItemMileStoneAdapter.videoPos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vision_digital.R;
import com.vision_digital.coupons.ItemCoupon;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.chapters.ItemChapter;
import com.vision_digital.model.chapters.ItemChapterAdapter;
import com.vision_digital.model.milestone.ItemMileStone;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectMilestoneInvitationActivity extends AppCompatActivity {
    RecyclerView chaptersListRV;


    String courseDetailsUrl = "", coursePlanUrl = "";

    String courseName = "";
    private String userNameString = "";

    //CourseContent Subscription

    int studId = 0;
    String uid = "";
    String courseId = "";
    //    public String courseId = "";
    String subscriptionStatus = "";
    String courseDuration = "";
    String courseOwnerName = "", courseOwnerQuali = "";
    public static String fromActivityInSelectMile = "";

    //Layout-----------------------
    ProgressDialog dialog;
    TextView watchNowBtn;
    ConstraintLayout subscribeBtnLayout;
    List<Long> forMonths = new ArrayList<>();
    public static int subsPrice = 0;
    int chapter_id;
    Map<Integer, Long> subscriptionMap = new HashMap<>();


    //Milestone resources---------
    ArrayList<Uri> courseMilestoneList = new ArrayList<>();
    ArrayList<ItemMileStone> mileStonesArrayList = new ArrayList<>();

    //CourseContent-------------------
    ArrayList<ItemChapter> chaptersList = new ArrayList<>();
    ArrayList<ItemCoupon> couponList = new ArrayList<>();


    ArrayList<Integer> months = new ArrayList<Integer>();


    ItemChapterAdapter itemChapterAdapter;

    public static String community_id_select_milestone = "", community_name_select_mile = "", community_logo_select_mile = "";
    public static Button skipButton, next_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_milestone_invitation);
        courseDetailsUrl = getApplicationContext().getString(R.string.apiURL) + "getCourseDetails";
        coursePlanUrl = getApplicationContext().getString(R.string.apiURL) + "availableChapterInMonth";

        chaptersListRV = findViewById(R.id.chaptersList);
        next_Button = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);


        fromActivityInSelectMile = getIntent().getStringExtra("fromActivity");
        community_logo_select_mile = getIntent().getStringExtra("community_logo");
        community_name_select_mile = getIntent().getStringExtra("community_name");
        userNameString = getIntent().getStringExtra("userNameString");

//        selectedMilestoneID = getIntent().getStringExtra("id");
        next_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("isHaveMilestoneId0", "isHaveMilestoneId false");

                if (isHaveMilestoneId) {
                    Intent i = new Intent(SelectMilestoneInvitationActivity.this, SelectStudentActivity.class);
                    i.putExtra("community_id", courseId);
                    i.putExtra("milestone_id", selectedMilestoneID);
                    i.putExtra("videoPos", videoPos);
                    i.putExtra("milestone_name", selectedMilestoneName);
                    i.putExtra("community_name", community_name_select_mile);
                    i.putExtra("community_logo", community_logo_select_mile);
                    i.putExtra("userNameString", userNameString);
                    i.putExtra("fromActivity", fromActivityInSelectMile);
                    Log.e("community_logo", community_logo_select_mile);

                    startActivity(i);
                    Log.e("isHaveMilestoneId0", "isHaveMilestoneId true 2");

                } else {
                    Log.e("isHaveMilestoneId0", "isHaveMilestoneId false2");

                    Toast.makeText(SelectMilestoneInvitationActivity.this, "Please select any milestone!....", Toast.LENGTH_SHORT).show();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SelectStudentActivity.class));
            }
        });


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Retriving user details stored in Local---------------------------------------
        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        studId = studDetails.getInt("sid", 0);
        courseId = getIntent().getStringExtra("community_id");
        new GetCourseDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class GetCourseDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(SelectMilestoneInvitationActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(SelectMilestoneInvitationActivity.this);
            int versionCode = com.vision_digital.BuildConfig.VERSION_CODE;

            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;

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

                    switch (status) {
                        case "success":
                            //Running Fine

                            int positionNumber = 0;

                            //Setting course Details for subscribed course--------------------------
                            subscriptionStatus = dataObj.getString("subscription_status");


                            courseName = dataObj.getString("title");
                            String courseDesc = dataObj.getString("description");
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

                                    int a = 1;
                                    int counterMonth = 1;
                                    for (int i = 0; i < subscriptionJsonArray.length(); i++) {
                                        JSONObject subscriptionMonth = subscriptionJsonArray.getJSONObject(i);

                                        try {
                                            subsPrice = Integer.parseInt(subscriptionMonth.getString(String.valueOf(counterMonth)));
                                            forMonths.add(Long.valueOf(counterMonth));
                                            subscriptionMap.put(counterMonth, Long.valueOf(subscriptionMonth.getString("" + (counterMonth))));


                                        } catch (Exception e) {
                                            i--;
                                        }
                                        counterMonth++;

                                    }
                                }


                                //Setting content--------------------
                                JSONArray courseContent = dataObj.getJSONArray("course_content");
                                JSONArray monthContent = dataObj.getJSONArray("course_content");

                                chaptersList.clear();
                                courseMilestoneList.clear();
                                mileStonesArrayList.clear();

                                for (int i = 0; i < courseContent.length(); i++) {
                                    ItemChapter chapter = new ItemChapter();
                                    JSONObject chapterObj = courseContent.getJSONObject(i);

                                    chapter.setId(chapterObj.getString("id"));
                                    chapter_id = Integer.parseInt(chapterObj.getString("id"));
                                    chapter.setTitle("Chapter " + (i + 1) + ": " + chapterObj.getString("title"));
                                    chapter.setSort_order(chapterObj.getString("sort_order"));
                                    chapter.setMin_month("Subscription Month: " + chapterObj.getString("min_month"));

                                    int subMonths = Integer.parseInt(chapterObj.getString("min_month"));

                                    JSONArray notestones = chapterObj.getJSONArray("notes");

                                    JSONArray milestones = chapterObj.getJSONArray("milestones");
                                    for (int j = 0; j < milestones.length(); j++) {
                                        ItemMileStone mileStone = new ItemMileStone();
                                        JSONObject mileStoneObj = milestones.getJSONObject(j);
                                        mileStone.setId(mileStoneObj.getString("id"));
                                        Log.e("Topic name", "" + mileStoneObj.getString("title"));
                                        mileStone.setTitle(mileStoneObj.getString("title"));
                                        mileStone.setDuration(mileStoneObj.getString("duration"));
                                        mileStone.setSort_order(mileStoneObj.getString("sort_order"));
                                        mileStone.setVideoUrl(mileStoneObj.getString("video_link"));
//                                        String video = mileStoneObj.getString("video_link");
                                        mileStone.setActivityType("selectMilestone");
                                        mileStone.setSelected(false);

//                                        String lastThree = video.substring(video.length() - 3, video.length());
                                        mileStone.setVideoPosition(positionNumber++);
                                        if (!mileStoneObj.getString("video_link").equals("")) {
                                            courseMilestoneList.add(Uri.parse(mileStoneObj.getString("video_link")));
                                        }
                                        if (!mileStoneObj.getString("duration").equals("1")) {
                                            mileStonesArrayList.add(mileStone);

                                        }
//                                        if (!lastThree.equals("pdf")) {
//                                            mileStonesArrayList.add(mileStone);
//
//                                        }
                                        chapter.mileStonesList.add(mileStone);
                                        chapter.setItemMileStone(mileStone);
                                    }
                                    for (int k = 0; k < notestones.length(); k++) {
                                        ItemMileStone mileStone = new ItemMileStone();
                                        JSONObject noteStoneObject = notestones.getJSONObject(k);
                                        mileStone.setId(noteStoneObject.getString("id"));
                                        mileStone.setTitle(noteStoneObject.getString("title"));
                                        mileStone.setSort_order(noteStoneObject.getString("sort_order"));
                                        mileStone.setVideoUrl("not_available");
                                        mileStone.setDuration("0");
                                        mileStone.setActivityType("selectMilestone");
                                        mileStone.setSelected(false);

//

                                        mileStone.setVideoPosition(positionNumber++);
                                        if (!noteStoneObject.getString("pdf_link").equals("")) {
                                            courseMilestoneList.add(Uri.parse(noteStoneObject.getString("pdf_link")));
                                            mileStone.setVideoUrl(noteStoneObject.getString("pdf_link"));

                                        }

//                                        chapter.mileStonesList.add(mileStone);
                                        Log.e("milestoneList", String.valueOf(mileStone));
//                                        chapter.setItemMileStone(mileStone);
//
                                    }
                                    chaptersList.add(chapter);
                                }


                                LinearLayoutManager layoutManager = new LinearLayoutManager(SelectMilestoneInvitationActivity.this, LinearLayoutManager.VERTICAL, false);
                                chaptersListRV.setLayoutManager(layoutManager);
                                itemChapterAdapter = new ItemChapterAdapter(chaptersList);
                                chaptersListRV.setAdapter(itemChapterAdapter);

                                JSONArray couponContent = dataObj.getJSONArray("available_coupons");
                                int length = couponContent.length();
                                for (int i = 0; i < couponContent.length(); i++) {
                                    ItemCoupon itemCoupon = new ItemCoupon();
                                    JSONObject couponObject = couponContent.getJSONObject(i);

                                    itemCoupon.setId(couponObject.getString("id"));
                                    itemCoupon.setCouponCode(couponObject.getString("code"));
                                    itemCoupon.setCouponTitle(couponObject.getString("title"));
                                    itemCoupon.setDescription(couponObject.getString("description"));


//                                    if (i==couponContent.length()){
//                                        itemCoupon.setCouponCode("Other");
//                                        itemCoupon.setCouponTitle("By user Input");
////                                        itemCoupon.setDescription(couponObject.getString("description"));
//                                    }
                                    couponList.add(itemCoupon);
//                                    Log.e("couonList", couponObject.getString("code"));

                                }


                            } else if (subscriptionStatus.equals("unsubscribed")) {
//                                watchNowBtn.setText("Subscribe");
                                int totalMonths = dataObj.getInt("available_subscription_months");
                                int currentSubsMonths = dataObj.getInt("subscribed_months");
                                if (totalMonths == currentSubsMonths) {
                                } else {
                                    JSONArray subscriptionJsonArray = dataObj.getJSONArray("available_subscriptions");
                                    forMonths.clear();

                                    int counterMonth = 1;
                                    for (int i = 0; i < subscriptionJsonArray.length(); i++) {
                                        JSONObject subscriptionMonth = subscriptionJsonArray.getJSONObject(i);

//                                        forMonths.add(i)
//                                        subscriptionMap.put(i + 1, Long.valueOf(subscriptionMonth.getString("" + (i + 1))));
                                        try {
                                            subsPrice = Integer.parseInt(subscriptionMonth.getString(String.valueOf(counterMonth)));
                                            forMonths.add(Long.valueOf(counterMonth));
                                            subscriptionMap.put(counterMonth, Long.valueOf(subscriptionMonth.getString("" + (counterMonth))));


                                        } catch (Exception e) {
                                            i--;
                                        }
                                        counterMonth++;
//                                        String value = subscriptionJsonArray.getString(i);
//                                        Log.e("value",value);
//
////                                        subsPrice = Integer.parseInt(subscriptionMonth.getString(subscriptionJsonArray.getString(i)));

                                    }
                                }


                                //Setting content--------------------
                                JSONArray courseContent = dataObj.getJSONArray("course_content");
                                chaptersList.clear();
                                courseMilestoneList.clear();
                                mileStonesArrayList.clear();

                                for (int i = 0; i < courseContent.length(); i++) {
                                    ItemChapter chapter = new ItemChapter();
                                    JSONObject chapterObj = courseContent.getJSONObject(i);

                                    chapter.setId(chapterObj.getString("id"));
                                    chapter.setTitle("Chapter " + (i + 1) + ": " + chapterObj.getString("title"));
                                    chapter.setSort_order(chapterObj.getString("sort_order"));
                                    chapter.setMin_month("Subscription Month: " + chapterObj.getString("min_month"));

                                    JSONArray notestones = chapterObj.getJSONArray("notes");

                                    JSONArray milestones = chapterObj.getJSONArray("milestones");
                                    for (int j = 0; j < milestones.length(); j++) {
                                        ItemMileStone mileStone = new ItemMileStone();
                                        JSONObject mileStoneObj = milestones.getJSONObject(j);
                                        mileStone.setId(mileStoneObj.getString("id"));
                                        mileStone.setTitle(mileStoneObj.getString("title"));
                                        mileStone.setDuration(mileStoneObj.getString("duration"));
                                        mileStone.setSort_order(mileStoneObj.getString("sort_order"));
                                        mileStone.setVideoUrl("not_available");
                                        mileStone.setActivityType("selectMilestone");
                                        mileStone.setSelected(false);

                                        mileStone.setVideoPosition(positionNumber++);
                                        if (!mileStoneObj.getString("video_link").equals("")) {
                                            courseMilestoneList.add(Uri.parse(mileStoneObj.getString("video_link")));
                                        }
//
                                        if (!mileStoneObj.getString("duration").equals("1")) {
                                            mileStonesArrayList.add(mileStone);

                                        }


                                        chapter.mileStonesList.add(mileStone);
                                        chapter.setItemMileStone(mileStone);
                                    }
                                    for (int k = 0; k < notestones.length(); k++) {
                                        ItemMileStone mileStone = new ItemMileStone();
                                        JSONObject noteStoneObject = notestones.getJSONObject(k);
                                        mileStone.setId(noteStoneObject.getString("id"));
                                        mileStone.setTitle(noteStoneObject.getString("title"));
                                        mileStone.setSort_order(noteStoneObject.getString("sort_order"));
                                        mileStone.setVideoUrl("not_available");
                                        mileStone.setDuration("0");
                                        mileStone.setActivityType("selectMilestone");
                                        mileStone.setSelected(false);


                                        mileStone.setVideoPosition(positionNumber++);
                                        if (!noteStoneObject.getString("pdf_link").equals("")) {
                                            courseMilestoneList.add(Uri.parse(noteStoneObject.getString("pdf_link")));
                                        }

                                        chapter.mileStonesList.add(mileStone);
                                        chapter.setItemMileStone(mileStone);
//
                                    }
                                    chaptersList.add(chapter);
                                }

                                LinearLayoutManager layoutManager = new LinearLayoutManager(SelectMilestoneInvitationActivity.this, LinearLayoutManager.VERTICAL, false);
                                chaptersListRV.setLayoutManager(layoutManager);
                                itemChapterAdapter = new ItemChapterAdapter(chaptersList);
                                chaptersListRV.setAdapter(itemChapterAdapter);

                                JSONArray couponContent = dataObj.getJSONArray("available_coupons");
                                for (int i = 0; i < couponContent.length() + 1; i++) {
                                    ItemCoupon itemCoupon = new ItemCoupon();
                                    JSONObject couponObject = couponContent.getJSONObject(i);
                                    itemCoupon.setCouponCode(couponObject.getString("id"));
                                    itemCoupon.setCouponCode(couponObject.getString("code"));
                                    itemCoupon.setCouponTitle(couponObject.getString("title"));
                                    itemCoupon.setDescription(couponObject.getString("description"));

//                                    if (i==couponContent.length()+1){
//                                        itemCoupon.setCouponCode("Other");
//                                        itemCoupon.setCouponTitle("By user Input");
////                                        itemCoupon.setDescription(couponObject.getString("description"));
//                                    }

                                }
                            }

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectMilestoneInvitationActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = getLayoutInflater();
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
                            Toast.makeText(SelectMilestoneInvitationActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(SelectMilestoneInvitationActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
//        new GetCourseDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}