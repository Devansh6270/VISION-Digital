package com.vision_digital.TestSeries;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.vision_digital.BuildConfig;
import com.vision_digital.R;
import com.vision_digital.TestSeries.model.banner.ItemTestBanner;
import com.vision_digital.TestSeries.model.banner.TestBannerViewPagerAdapter;
import com.vision_digital.TestSeries.model.bundle.ItemTestBundle;
import com.vision_digital.TestSeries.model.bundle.ItemTestBundleAdapter;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.helperClasses.JSONParser;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestSeriesDashboardActivity extends AppCompatActivity {

    int sid;

    String uid;

    String getDashboardData = "getTestList";
    String duration;


    ArrayList<ItemTestBundle> myBundleList = new ArrayList<>();
    ArrayList<ItemTestBundle> subscribedTestsList = new ArrayList<>();
    ArrayList<ItemTestBundle> unSubTestList = new ArrayList<>();
    ArrayList<ItemTestBundle> postTestSeriesList = new ArrayList<>();

    ProgressDialog dialog;

    //My  Test Series bundle work-------------------------------------------
    ItemTestBundleAdapter itemBundleTestSeriesAdapter;
    RecyclerView testSeriesBundleList;


    //My Subscribed Test Series work-------------------------------------------
    ItemTestBundleAdapter itemSubscribedTestSeriesAdapter;
    RecyclerView itemSubscribedTestSeriesListView;

    //
    ItemTestBundleAdapter itemUnsubscribedTestSeriesCoursesAdapter;
    RecyclerView itemUnsubscribedTestSeriesListView;

    //My Subscribed Test Series work-------------------------------------------
    ItemTestBundleAdapter itemPostTestSeriesAdapter;
    RecyclerView itemPostTestSeriesListView;

    ImageView backBtn;
    LinearLayout postTxt, subsTxt, unsubscribedTxt, bundleTxt;


    List<String> myCoursesIdList = new ArrayList<>();

    //for banner
    CardView banner_card;
    private ViewPager viewPagerBanner;
    TestBannerViewPagerAdapter testBannerViewPagerAdapter;
    private TabLayout tabIndicatorBanner;
    int position = 0;
    Timer timer;
    List<ItemTestBanner> itemTestBannerList = new ArrayList<>();

    final private long BANNER_DELAY_TIME = 2000;
    final private long BANNER_PERIOD_TIME = 2000;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_series_dashboard);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        dialog = new ProgressDialog(TestSeriesDashboardActivity.this);
        dialog.setMessage("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        backBtn = findViewById(R.id.backBtn);
        postTxt = findViewById(R.id.postTxt);
        postTxt.setVisibility(View.GONE);
        tabIndicatorBanner = findViewById(R.id.tabLayoutTest);
        subsTxt = findViewById(R.id.subsTxt);
        banner_card = findViewById(R.id.banner_card);
        unsubscribedTxt = findViewById(R.id.unsubscribedTxt);
        bundleTxt = findViewById(R.id.bundleTxt);
//        subsTxt.setVisibility(View.GONE);

        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        testSeriesBundleList = findViewById(R.id.testSeriesBundleList);
        itemSubscribedTestSeriesListView = findViewById(R.id.subscribedTestSeriesList);
        itemUnsubscribedTestSeriesListView = findViewById(R.id.unsubscribedTestSeriesList);
        itemPostTestSeriesListView = findViewById(R.id.postTestSeriesList);

        viewPagerBanner = findViewById(R.id.viewPagerTest);



//        List<ItemTestBanner> mList = new ArrayList<>();
//        mList.add(new ItemTestBanner(1,R.drawable.banner_testing_image));
//        mList.add(new ItemTestBanner(2,R.drawable.banner_testing_image));
//        mList.add(new ItemTestBanner(3,R.drawable.banner_testing_image));
//
//
//        testBannerViewPagerAdapter = new TestBannerViewPagerAdapter(this,mList);
//        viewPagerBanner.setAdapter(testBannerViewPagerAdapter);
//        tabIndicatorBanner.setupWithViewPager(viewPagerBanner);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {

                position = i;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
//                    pageLooper();
                }

            }
        };

        viewPagerBanner.addOnPageChangeListener(onPageChangeListener);

        tabIndicatorBanner.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (position == itemTestBannerList.size()) {
                } else {
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPagerBanner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                pageLooper();
                stopBannerSlidShow();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    startBannerSlideShow();
                }
                return false;
            }
        });

        startBannerSlideShow();


        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        sid = studDetails.getInt("sid", 0);

        new GetTestSeriesDashboardData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void startBannerSlideShow() {
        Handler handler = new Handler();
        Runnable update = new Runnable() {
            @Override
            public void run() {

                if (position >= itemTestBannerList.size()) {
                    position = 0;
                }

                viewPagerBanner.setCurrentItem(position++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, BANNER_DELAY_TIME, BANNER_PERIOD_TIME);
    }

    private void stopBannerSlidShow() {
        timer.cancel();
    }

    class GetTestSeriesDashboardData extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(TestSeriesDashboardActivity.this);
            int versionCode = BuildConfig.VERSION_CODE;

            SharedPreferences studDetails = getSharedPreferences("CNBUID", MODE_PRIVATE);
            uid = studDetails.getString("uid", "NO_NAME");
//=================Previous code for api hit=======================================
            String param = "uid=" + uid + "&app_version=" + versionCode + "&sid=" + sid;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest("https://v.chalksnboard.com/api/v3/" + getDashboardData, "POST", param);
            Log.e("link", "https://v.chalksnboard.com/api/v3/" + getDashboardData);

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
                    final JSONObject dataObj = jsonObject.getJSONObject("data");
                    switch (status) {
                        case "true":

                            //Already Logged in-----------------------

                                banner_card.setVisibility(View.VISIBLE);
                                itemTestBannerList.clear();
                                for (int i = 0; i < 3; i++) {
                                    ItemTestBanner itemTestBanner = new ItemTestBanner();
                                    itemTestBanner.setIdBanner("1");
                                    itemTestBanner.setImageBanner("https://chalksnboard-universe.s3.ap-south-1.amazonaws.com/Dejawoo%20School%20of%20Innovation/Profile%20Images/dejawoo%20school.png");

                                    itemTestBannerList.add(itemTestBanner);

//                                }
                                //My courses-------------

                                testBannerViewPagerAdapter = new TestBannerViewPagerAdapter(TestSeriesDashboardActivity.this, itemTestBannerList);
                                viewPagerBanner.setAdapter(testBannerViewPagerAdapter);
                                tabIndicatorBanner.setupWithViewPager(viewPagerBanner);

                                banner_card.setVisibility(View.GONE);
                                tabIndicatorBanner.setVisibility(View.GONE);
                            }



                            JSONArray mycourses = dataObj.getJSONArray("subscribedTestSeries");

                            if (mycourses.length() == 0) {
                                subsTxt.setVisibility(View.GONE);
                            } else {
                                subsTxt.setVisibility(View.VISIBLE);
                                subscribedTestsList.clear();
                                myCoursesIdList.clear();
                                for (int i = 0; i < mycourses.length(); i++) {
                                    ItemTestBundle itemSubscribedTestSeries = new ItemTestBundle();
                                    JSONObject courseObj = mycourses.getJSONObject(i);
                                    itemSubscribedTestSeries.setId(courseObj.getString("id"));
                                    myCoursesIdList.add(courseObj.getString("id"));
                                    itemSubscribedTestSeries.setDescription(courseObj.getString("short_desc"));
                                    itemSubscribedTestSeries.setDuration(courseObj.getString("duration"));
                                    itemSubscribedTestSeries.setOwner_name(courseObj.getString("owner"));
                                    itemSubscribedTestSeries.setTitle(courseObj.getString("title"));
                                    itemSubscribedTestSeries.setImage(courseObj.getString("image"));
                                    itemSubscribedTestSeries.setOwner_qualification("");
                                    itemSubscribedTestSeries.setType("Test");
                                    itemSubscribedTestSeries.setStatus("Start");
                                    itemSubscribedTestSeries.setTestType("single");
                                    subscribedTestsList.add(itemSubscribedTestSeries);

                                }

                                //My courses-------------

                                LinearLayoutManager layoutManager = new LinearLayoutManager(TestSeriesDashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                itemSubscribedTestSeriesListView.setLayoutManager(layoutManager);
                                itemSubscribedTestSeriesAdapter = new ItemTestBundleAdapter(subscribedTestsList);
                                itemSubscribedTestSeriesListView.setAdapter(itemSubscribedTestSeriesAdapter);
                            }


                            JSONArray bundleTestSeries = dataObj.getJSONArray("unsubscribedTestSeriesBundle");
                            if (bundleTestSeries.length() == 0) {
                                bundleTxt.setVisibility(View.GONE);
                            } else {
                                bundleTxt.setVisibility(View.VISIBLE);
                                myBundleList.clear();
                                for (int i = 0; i < bundleTestSeries.length(); i++) {
                                    ItemTestBundle itemBundleTestSeries = new ItemTestBundle();
                                    JSONObject bundleTestSeriesJSONObject = bundleTestSeries.getJSONObject(i);
                                    itemBundleTestSeries.setId(bundleTestSeriesJSONObject.getString("id"));
                                    itemBundleTestSeries.setOwner_name(bundleTestSeriesJSONObject.getString("owner"));
                                    itemBundleTestSeries.setTitle(bundleTestSeriesJSONObject.getString("title"));
                                    itemBundleTestSeries.setImage(bundleTestSeriesJSONObject.getString("image"));
                                    itemBundleTestSeries.setDuration("");
                                    itemBundleTestSeries.setPrice(bundleTestSeriesJSONObject.getString("price"));
                                    itemBundleTestSeries.setDescription(bundleTestSeriesJSONObject.getString("description"));
                                    itemBundleTestSeries.setStatus(bundleTestSeriesJSONObject.getString("status"));
                                    itemBundleTestSeries.setOwner_qualification("");
                                    itemBundleTestSeries.setType("TestSeries");
                                    itemBundleTestSeries.setTestType(bundleTestSeriesJSONObject.getString("test_type"));
                                    myBundleList.add(itemBundleTestSeries);


                                }

                                LinearLayoutManager layoutManagerBundleTestSeries = new LinearLayoutManager(TestSeriesDashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                testSeriesBundleList.setLayoutManager(layoutManagerBundleTestSeries);
                                itemBundleTestSeriesAdapter = new ItemTestBundleAdapter(myBundleList);
                                testSeriesBundleList.setAdapter(itemBundleTestSeriesAdapter);

                            }


                            JSONArray unSubsJsonArray = dataObj.getJSONArray("unsubscribedTestSeries");
                            if (unSubsJsonArray.length() == 0) {
                                unsubscribedTxt.setVisibility(View.GONE);
                            } else {
                                unsubscribedTxt.setVisibility(View.VISIBLE);
                                unSubTestList.clear();
                                for (int i = 0; i < unSubsJsonArray.length(); i++) {
                                    ItemTestBundle itemUnSubscribedTestSeries = new ItemTestBundle();
                                    JSONObject unSubTestJSONObject = unSubsJsonArray.getJSONObject(i);
                                    itemUnSubscribedTestSeries.setId(unSubTestJSONObject.getString("id"));
                                    itemUnSubscribedTestSeries.setDescription(unSubTestJSONObject.getString("short_desc"));
                                    itemUnSubscribedTestSeries.setDuration(unSubTestJSONObject.getString("duration"));
                                    itemUnSubscribedTestSeries.setOwner_name(unSubTestJSONObject.getString("owner"));
                                    itemUnSubscribedTestSeries.setTitle(unSubTestJSONObject.getString("title"));
                                    itemUnSubscribedTestSeries.setImage(unSubTestJSONObject.getString("image"));
                                    itemUnSubscribedTestSeries.setOwner_qualification("");
                                    itemUnSubscribedTestSeries.setStatus("Subscribe");
                                    itemUnSubscribedTestSeries.setTestType(unSubTestJSONObject.getString("test_type"));
                                    itemUnSubscribedTestSeries.setType("Test");
                                    unSubTestList.add(itemUnSubscribedTestSeries);
                                }


                                //Popular courses-------------

                                LinearLayoutManager layoutManagerPopularCourses = new LinearLayoutManager(TestSeriesDashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                itemUnsubscribedTestSeriesListView.setLayoutManager(layoutManagerPopularCourses);
                                itemUnsubscribedTestSeriesCoursesAdapter = new ItemTestBundleAdapter(unSubTestList);
                                itemUnsubscribedTestSeriesListView.setAdapter(itemUnsubscribedTestSeriesCoursesAdapter);

                            }


                            JSONArray postTestSeriesArray = dataObj.getJSONArray("postTestSeries");

                            if (postTestSeriesArray.length() == 0) {
                                postTxt.setVisibility(View.GONE);
                            } else {
                                postTxt.setVisibility(View.VISIBLE);
                                postTestSeriesList.clear();

                                for (int i = 0; i < postTestSeriesArray.length(); i++) {
                                    ItemTestBundle itemPostTestSeries = new ItemTestBundle();
                                    JSONObject postTestSeriesJSONObject = postTestSeriesArray.getJSONObject(i);
                                    itemPostTestSeries.setId(postTestSeriesJSONObject.getString("id"));
                                    itemPostTestSeries.setDescription(postTestSeriesJSONObject.getString("short_desc"));
                                    itemPostTestSeries.setDuration(postTestSeriesJSONObject.getString("duration"));
                                    itemPostTestSeries.setOwner_name(postTestSeriesJSONObject.getString("owner"));
                                    itemPostTestSeries.setTitle(postTestSeriesJSONObject.getString("title"));
                                    itemPostTestSeries.setStatus("See Result");
                                    itemPostTestSeries.setImage(postTestSeriesJSONObject.getString("image"));
                                    itemPostTestSeries.setOwner_qualification("");
                                    itemPostTestSeries.setType("Test");
                                    itemPostTestSeries.setTestType("single");
                                    postTestSeriesList.add(itemPostTestSeries);
                                }

                                LinearLayoutManager layoutManagerPostTestSeries = new LinearLayoutManager(TestSeriesDashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                itemPostTestSeriesListView.setLayoutManager(layoutManagerPostTestSeries);
                                itemPostTestSeriesAdapter = new ItemTestBundleAdapter(postTestSeriesList);
                                itemPostTestSeriesListView.setAdapter(itemPostTestSeriesAdapter);


                            }


                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TestSeriesDashboardActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = TestSeriesDashboardActivity.this.getLayoutInflater();
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
                            Toast.makeText(TestSeriesDashboardActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(TestSeriesDashboardActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        sid = studDetails.getInt("sid", 0);
        new GetTestSeriesDashboardData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TestSeriesDashboardActivity.this, DashboardActivity.class);
        startActivity(intent);

    }
}
