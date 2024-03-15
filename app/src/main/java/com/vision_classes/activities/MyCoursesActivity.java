package com.vision_classes.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vision_classes.TestSeries.model.TestSeriesAvailabilityBundle.ItemTestSeriesAvailabilityAdapter;
import com.vision_classes.TestSeries.model.TestSeriesAvailabilityBundle.ItemTestSeriesAvailabilityList;

import com.vision_classes.helperClasses.JSONParser;
import com.vision_classes.model.myCourses.ItemSubscribedCourse;
import com.vision_classes.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.vision_classes.model.myCourses.ItemMyAllCourseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyCoursesActivity extends AppCompatActivity {


    TextView  allBtn , recordedBtn , liveBtn , testSeriesBtn, packageBtn;
    ItemMyAllCourseAdapter itemMyAllCourseAdapter;
    RecyclerView myCoursesLiveListView;
    RecyclerView myCoursesRecordedListView;
    RecyclerView myCoursesAllListView;
    RecyclerView myCoursesTestSeriesListView;
    RecyclerView packageListView;

    ImageView backBtn;
    ProgressDialog dialog;

    int sid;

    String getSubscribeCoursesUrl="";

    ConstraintLayout emptyCourseListLayout;
    LinearLayout llTabMenu;

    CardView exploreCourseBtn;

    List<ItemSubscribedCourse> itemSubscribedLiveCourseList = new ArrayList<>();
    List<ItemSubscribedCourse> itemSubscribedRecordedCourseList = new ArrayList<>();
    List<ItemSubscribedCourse> itemSubscribedAllCourseList = new ArrayList<>();
    List<ItemSubscribedCourse> itemPackageCourseList = new ArrayList<>();

    List<ItemSubscribedCourse> itemTestSeriesAvailabilityLists = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);
        //        To Stop user from video recording or taking screen shot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        getSubscribeCoursesUrl= getApplicationContext().getString(R.string.apiURL)+"getMyCoursesList";

        SharedPreferences userIsRegisteredSuccessful = getSharedPreferences("CNB", MODE_PRIVATE);
        sid = userIsRegisteredSuccessful.getInt("sid", 0);

        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        backBtn = findViewById(R.id.backBtn);
        emptyCourseListLayout=findViewById(R.id.emptyCourseListLayout);
        exploreCourseBtn=findViewById(R.id.exploreCoursesBtn);
        llTabMenu=findViewById(R.id.llTabMenu);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


//        exploreCourseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MyCoursesActivity.this, AllVideoCoursesActivity.class));
//            }
//        });

        exploreCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCoursesActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        dialog = new ProgressDialog(MyCoursesActivity.this);
        dialog.setMessage("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();














        myCoursesLiveListView = findViewById(R.id.myCoursesLiveList);
        myCoursesRecordedListView = findViewById(R.id.myCoursesRecordedList);
        myCoursesAllListView = findViewById(R.id.myCoursesAllList);
        myCoursesTestSeriesListView = findViewById(R.id.myCourseTestSeriesList);
        packageListView = findViewById(R.id.packageCourseList);

        allBtn=findViewById(R.id.allCourseBtn);
        liveBtn=findViewById(R.id.liveCourseBtn);
        recordedBtn=findViewById(R.id.recordedCourseBtn);
        testSeriesBtn=findViewById(R.id.testSeriesBtn);
        packageBtn=findViewById(R.id.packageBtn);

        myCoursesLiveListView.setVisibility(View.GONE);
        myCoursesRecordedListView.setVisibility(View.GONE);
        myCoursesTestSeriesListView.setVisibility(View.GONE);
        packageListView.setVisibility(View.GONE);

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCoursesLiveListView.setVisibility(View.GONE);
                myCoursesRecordedListView.setVisibility(View.GONE);
//                myCoursesAllListView.setVisibility(View.VISIBLE);
                if (itemSubscribedAllCourseList.size() == 0){
                    emptyCourseListLayout.setVisibility(View.VISIBLE);
                    myCoursesAllListView.setVisibility(View.GONE);
                } else {
                    myCoursesAllListView.setVisibility(View.VISIBLE);
                    emptyCourseListLayout.setVisibility(View.GONE);
                }
                myCoursesTestSeriesListView.setVisibility(View.GONE);
                packageListView.setVisibility(View.GONE);

                allBtn.setBackgroundResource(R.drawable.bg_red_round_button);
                allBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                liveBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                liveBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                recordedBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                recordedBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                testSeriesBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                testSeriesBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                packageBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                packageBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));



            }
        });

        liveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                myCoursesLiveListView.setVisibility(View.VISIBLE);
                if (itemSubscribedLiveCourseList.size() == 0){
                    emptyCourseListLayout.setVisibility(View.VISIBLE);
                    myCoursesLiveListView.setVisibility(View.GONE);
                } else {
                    myCoursesLiveListView.setVisibility(View.VISIBLE);
                    emptyCourseListLayout.setVisibility(View.GONE);
                }
                myCoursesRecordedListView.setVisibility(View.GONE);
                myCoursesAllListView.setVisibility(View.GONE);
                myCoursesTestSeriesListView.setVisibility(View.GONE);
                packageListView.setVisibility(View.GONE);

                allBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                allBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                liveBtn.setBackgroundResource(R.drawable.bg_red_round_button);
                liveBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                recordedBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                recordedBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                testSeriesBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                testSeriesBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                packageBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                packageBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));



            }
        });

        recordedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCoursesLiveListView.setVisibility(View.GONE);
//                myCoursesRecordedListView.setVisibility(View.VISIBLE);
                if (itemSubscribedRecordedCourseList.size() == 0){
                    emptyCourseListLayout.setVisibility(View.VISIBLE);
                    myCoursesRecordedListView.setVisibility(View.GONE);
                } else {
                    myCoursesRecordedListView.setVisibility(View.VISIBLE);
                    emptyCourseListLayout.setVisibility(View.GONE);
                }
                myCoursesAllListView.setVisibility(View.GONE);
                myCoursesTestSeriesListView.setVisibility(View.GONE);
                packageListView.setVisibility(View.GONE);

                allBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                allBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                liveBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                liveBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                recordedBtn.setBackgroundResource(R.drawable.bg_red_round_button);
                recordedBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                testSeriesBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                testSeriesBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                packageBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                packageBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

            }
        });

        testSeriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCoursesLiveListView.setVisibility(View.GONE);
                myCoursesRecordedListView.setVisibility(View.GONE);
                myCoursesAllListView.setVisibility(View.GONE);
//                myCoursesTestSeriesListView.setVisibility(View.VISIBLE);
                if (itemTestSeriesAvailabilityLists.size() == 0){
                    emptyCourseListLayout.setVisibility(View.VISIBLE);
                    myCoursesTestSeriesListView.setVisibility(View.GONE);
                } else {
                    myCoursesTestSeriesListView.setVisibility(View.VISIBLE);
                    emptyCourseListLayout.setVisibility(View.GONE);
                }
                packageListView.setVisibility(View.GONE);

                allBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                allBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                liveBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                liveBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                recordedBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                recordedBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                testSeriesBtn.setBackgroundResource(R.drawable.bg_red_round_button);
                testSeriesBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                packageBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                packageBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));


            }
        });


        packageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                packageListView.setVisibility(View.VISIBLE);
                if (itemPackageCourseList.size() == 0){
                    emptyCourseListLayout.setVisibility(View.VISIBLE);
                    packageListView.setVisibility(View.GONE);
                } else {
                    packageListView.setVisibility(View.VISIBLE);
                    emptyCourseListLayout.setVisibility(View.GONE);
                }
                myCoursesLiveListView.setVisibility(View.GONE);
                myCoursesRecordedListView.setVisibility(View.GONE);
                myCoursesAllListView.setVisibility(View.GONE);
                myCoursesTestSeriesListView.setVisibility(View.GONE);

                allBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                allBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                liveBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                liveBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                recordedBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                recordedBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                testSeriesBtn.setBackgroundResource(R.drawable.bg_category_my_course);
                testSeriesBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));

                packageBtn.setBackgroundResource(R.drawable.bg_red_round_button);
                packageBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));


            }
        });


        new GetSubscribedCourseDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    class GetSubscribedCourseDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(MyCoursesActivity.this);


            String param = "sid="+sid;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(getSubscribeCoursesUrl, "POST", param);
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
                    dialog.dismiss();
                    String status = jsonObject.getString("status");
                    switch (status) {
                        case "true":

                            JSONObject dataObj =jsonObject.getJSONObject("data");

//                            if (dataObj.length()==0){
//                                emptyCourseListLayout.setVisibility(View.VISIBLE);
//                                llTabMenu.setVisibility(View.GONE);
//                            } else {
//                                emptyCourseListLayout.setVisibility(View.GONE);
//                                llTabMenu.setVisibility(View.VISIBLE);


                                // Live

                                JSONArray liveCourses = dataObj.getJSONArray("live");

                                if (liveCourses.length() == 0) {
                                    emptyCourseListLayout.setVisibility(View.VISIBLE);
                                    liveBtn.setVisibility(View.GONE);

                                } else {
                                    emptyCourseListLayout.setVisibility(View.GONE);
                                    itemSubscribedLiveCourseList.clear();
                                    itemSubscribedAllCourseList.clear();
                                    for (int i = 0; i < liveCourses.length(); i++) {
                                        ItemSubscribedCourse itemLiveCoursesList = new ItemSubscribedCourse();
                                        JSONObject packageJSONObject = liveCourses.getJSONObject(i);
                                        itemLiveCoursesList.setId(String.valueOf(packageJSONObject.getInt("id")));
                                        itemLiveCoursesList.setName(packageJSONObject.getString("name"));
                                        itemLiveCoursesList.setImage(packageJSONObject.getString("image"));
                                        itemLiveCoursesList.setType("live");
                                        itemSubscribedLiveCourseList.add(itemLiveCoursesList);
                                        itemSubscribedAllCourseList.add(itemLiveCoursesList);


                                    }

                                    LinearLayoutManager layoutManager = new LinearLayoutManager(MyCoursesActivity.this, LinearLayoutManager.VERTICAL, false);
                                    // LinearLayoutManager layoutManager = new GridLayoutManager(AllVideoCoursesActivity.this, 2);
                                    myCoursesLiveListView.setLayoutManager(layoutManager);
                                    itemMyAllCourseAdapter = new ItemMyAllCourseAdapter(itemSubscribedLiveCourseList, MyCoursesActivity.this);
                                    myCoursesLiveListView.setAdapter(itemMyAllCourseAdapter);

                                }


                                //  Recorded
                                JSONArray recordedCourses = dataObj.getJSONArray("course");

                                if (recordedCourses.length() == 0) {
                                    emptyCourseListLayout.setVisibility(View.VISIBLE);
                                    recordedBtn.setVisibility(View.GONE);
                                } else {
                                    emptyCourseListLayout.setVisibility(View.GONE);
                                    itemSubscribedRecordedCourseList.clear();
                                    for (int i = 0; i < recordedCourses.length(); i++) {
                                        ItemSubscribedCourse itemRecordedCoursesList = new ItemSubscribedCourse();
                                        JSONObject packageJSONObject = recordedCourses.getJSONObject(i);
                                        itemRecordedCoursesList.setId(String.valueOf(packageJSONObject.getInt("id")));
                                        itemRecordedCoursesList.setName(packageJSONObject.getString("name"));
                                        itemRecordedCoursesList.setImage(packageJSONObject.getString("image"));
                                        itemRecordedCoursesList.setType("course");
                                        itemSubscribedRecordedCourseList.add(itemRecordedCoursesList);
                                        itemSubscribedAllCourseList.add(itemRecordedCoursesList);

                                    }
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(MyCoursesActivity.this, LinearLayoutManager.VERTICAL, false);
                                    myCoursesRecordedListView.setLayoutManager(layoutManager);
                                    itemMyAllCourseAdapter = new ItemMyAllCourseAdapter(itemSubscribedRecordedCourseList, MyCoursesActivity.this);
                                    myCoursesRecordedListView.setAdapter(itemMyAllCourseAdapter);
                                }


                                // Package

                                JSONArray packageCourses = dataObj.getJSONArray("package");

                                if (packageCourses.length() == 0) {
                                    emptyCourseListLayout.setVisibility(View.VISIBLE);
                                    packageBtn.setVisibility(View.GONE);
                                } else {
                                    emptyCourseListLayout.setVisibility(View.GONE);
                                    itemPackageCourseList.clear();
                                    for (int i = 0; i < packageCourses.length(); i++) {
                                        ItemSubscribedCourse itemPackageCoursesList = new ItemSubscribedCourse();
                                        JSONObject packageJSONObject = packageCourses.getJSONObject(i);
                                        itemPackageCoursesList.setId(String.valueOf(packageJSONObject.getInt("id")));
                                        itemPackageCoursesList.setName(packageJSONObject.getString("name"));
                                        itemPackageCoursesList.setImage(packageJSONObject.getString("image"));
                                        itemPackageCoursesList.setType("package");
                                        itemSubscribedAllCourseList.add(itemPackageCoursesList);
                                        itemPackageCourseList.add(itemPackageCoursesList);

                                    }
                                    Log.e("Package", "HERE is adding package data");
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(MyCoursesActivity.this, LinearLayoutManager.VERTICAL, false);
                                    packageListView.setLayoutManager(layoutManager);
                                    itemMyAllCourseAdapter = new ItemMyAllCourseAdapter(itemPackageCourseList, MyCoursesActivity.this);
                                    packageListView.setAdapter(itemMyAllCourseAdapter);
                                }


                                // All  Courses

                                if (itemSubscribedAllCourseList.size() == 0) {
                                    emptyCourseListLayout.setVisibility(View.VISIBLE);
                                } else {
                                    emptyCourseListLayout.setVisibility(View.GONE);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(MyCoursesActivity.this, LinearLayoutManager.VERTICAL, false);
                                    myCoursesAllListView.setLayoutManager(layoutManager);
                                    itemMyAllCourseAdapter = new ItemMyAllCourseAdapter(itemSubscribedAllCourseList, MyCoursesActivity.this);
                                    myCoursesAllListView.setAdapter(itemMyAllCourseAdapter);
                                }

                                //  Test Series
                                JSONArray testSeries = dataObj.getJSONArray("testseries");
                                if (testSeries.length() == 0) {
                                    //emptyCourseListLayout.setVisibility(View.VISIBLE);
                                    testSeriesBtn.setVisibility(View.GONE);
                                } else {
                                    emptyCourseListLayout.setVisibility(View.GONE);
                                    itemTestSeriesAvailabilityLists.clear();
                                    for (int i = 0; i < testSeries.length(); i++) {
                                        ItemSubscribedCourse itemSubscribedTestSeries = new ItemSubscribedCourse();
                                        JSONObject testObj = testSeries.getJSONObject(i);
                                        itemSubscribedTestSeries.setId(String.valueOf(testObj.getInt("id")));
                                        itemSubscribedTestSeries.setName(testObj.getString("name"));
                                        itemSubscribedTestSeries.setImage(testObj.getString("image"));
                                        itemSubscribedTestSeries.setType("testseries");
                                        itemTestSeriesAvailabilityLists.add(itemSubscribedTestSeries);


                                    }
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(MyCoursesActivity.this, LinearLayoutManager.VERTICAL, false);
                                    myCoursesTestSeriesListView.setLayoutManager(layoutManager);
//                                    ItemTestSeriesAvailabilityAdapter itemTestSeriesAvailabilityAdapter = new ItemTestSeriesAvailabilityAdapter(MyCoursesActivity.this, itemTestSeriesAvailabilityLists);
//                                    myCoursesTestSeriesListView.setAdapter(itemTestSeriesAvailabilityAdapter);
                                    itemMyAllCourseAdapter = new ItemMyAllCourseAdapter(itemTestSeriesAvailabilityLists, MyCoursesActivity.this);
                                    myCoursesTestSeriesListView.setAdapter(itemMyAllCourseAdapter);
                                }
//                            }




                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyCoursesActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = MyCoursesActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
                            String msgContent = jsonObject.getString("message");
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
                            Toast.makeText(MyCoursesActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(MyCoursesActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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
