package com.vision_classes.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.smarteist.autoimageslider.SliderView;
import com.vision_classes.TestSeries.TestSeriesDashboardActivity;
import com.vision_classes.R;
import com.vision_classes.TestSeries.model.dashboardTestSeries.DashTestSeriesModel;
import com.vision_classes.helperClasses.JSONParser;
import com.vision_classes.internetConnectivity.ConnectivityReciever;
import com.vision_classes.internetConnectivity.MyApplication;
import com.vision_classes.model.CoursePackage.ItemPackageAdapter;
import com.vision_classes.model.CoursePackage.ItemPackageList;
import com.vision_classes.model.ImageSlider.SliderAdapter;
import com.vision_classes.model.ImageSlider.SliderModel;
import com.vision_classes.model.PopularCourses.ItemPopularCoursesAdapter;
import com.vision_classes.model.analytics.ItemAnalytics;
import com.vision_classes.model.analytics.ItemAnalyticsAdapter;
import com.vision_classes.model.continueWatchingCourseItem.CourseAdapter;
import com.vision_classes.model.continueWatchingCourseItem.CourseItem;
import com.vision_classes.model.liveClass.GoingOnLive.GoingOnLiveAdapter;
import com.vision_classes.model.liveClass.GoingOnLive.GoingOnLiveModel;
import com.vision_classes.model.myCourses.ItemMyCourse;
import com.vision_classes.model.myCourses.ItemMyCourseAdapter;
import com.vision_classes.model.offlineResult.ItemOfflineResultAdapter;
import com.vision_classes.model.offlineResult.ItemOfflineResultList;
import com.vision_classes.model.popularTeachers.ItemTeacher;
import com.vision_classes.model.popularTeachers.ItemTeacherAdapter;
import com.vision_classes.profile.ProfileActivity;
import com.vision_classes.signUp.LogInViaPhoneActivity;
import com.vision_classes.transaction.TransactionHistory;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardActivity extends AppCompatActivity implements ConnectivityReciever.ConnectivityRecieverListener {

    ImageView menuBtn, searchBtn, live_ViewAllBtn, popularInstitute_viewAllBtn, analytics_viewAllBtn;
    ImageView notificationBtn;

    LinearLayout  popularCousre_viewAllBtn, popularTeachers_viewAllBtn, offLineResultLayout ;

    CardView myCourse_viewAllBtn , packagesViewALLBtn , testSeriesViewAllBtn , liveClassViewAllBtn;

    public DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    public static String mobileNo = "";

    TextView  dashboardUserName;
    ImageView userImage;
    String studName;
    String studClass;
    String studSchool;

    String UserDashName = "";


    public static int sid;
    RelativeLayout myCourseLayout, myAnalyticsLayout;

    ArrayList<Entry> actualList = new ArrayList<>();
    ArrayList<Entry> spentList = new ArrayList<>();

    FirebaseUser user;


    public static String uid;

    String getDashboardData = "";
    String getOfflineResultData = "";
    String getPackageListApi = "";

    public static ArrayList<ItemMyCourse> myCoursesList = new ArrayList<>();
    public static ArrayList<ItemMyCourse> popularCoursesList = new ArrayList<>();
    public static ArrayList<ItemTeacher> popularTeacherList = new ArrayList<>();
    public static ArrayList<ItemTeacher> popularInstitutesList = new ArrayList<>();
    public static ArrayList<String> instituteForProfile = new ArrayList<>();

    public static ArrayList<ItemAnalytics> courseAnalytics = new ArrayList<>();



    ProgressDialog dialog;

    RecyclerView liveClassesList;
    int totalLiveClasses = 0;
    TextView noLiveClasses;

    //  Package Course
    ItemPackageAdapter itemPackageAdapter;
    RecyclerView packageRecyclerView;

    public static List<ItemPackageList> packageLists = new ArrayList<>();

    //My Course work-------------------------------------------
    ItemMyCourseAdapter itemMyCourseAdapter;
    RecyclerView myCourseslistView;


    ItemPopularCoursesAdapter popularCoursesAdapter;
    RecyclerView popularCourseslistView;

    ItemTeacherAdapter itemTeacherAdapter;
    RecyclerView popularTeacherslistView;
    List<ItemTeacher> popularTeachersList = new ArrayList<>();

    ItemTeacherAdapter itemInstitueAdapter;
    RecyclerView popularInstitutelistView;
    List<ItemTeacher> popularInstituteList = new ArrayList<>();

    //Continue Watching------------------------------------------------------
    private DiscreteScrollView itemPicker;
    private CourseAdapter courseAdapter;
    List<CourseItem> courseItemList = new ArrayList<>();

    LinearLayout continueWatchingContainer;



    String profileImageUrlString = "";

    String deeplinkFirebase = "chalksnboard";
    LinearLayout llOfflineResult;

    String getProfileDataUrl = "";
    CardView no_data_card;
    LinearLayout packageLayout;

    private String mImageUri = "";
    static  ArrayList<ItemOfflineResultList> offlineResultListArrayList = new ArrayList<>();
    static  ArrayList<GoingOnLiveModel> liveClassesLists = new ArrayList<>();
    GoingOnLiveAdapter goingOnLiveAdapter;
    ItemOfflineResultAdapter offlineResultAdapter;
    RecyclerView rvOfflineResult;

    String current_login_id="";

    SliderView sliderView;

    static String enrollno;

    ArrayList<SliderModel> sliderModelList = new ArrayList<>();


    RecyclerView recyclerLiveClassList;

   static ArrayList<DashTestSeriesModel> testSeriesList = new ArrayList<>();

    static ArrayList<DashTestSeriesModel> liveClassList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        myAnalyticsLayout = findViewById(R.id.analytics_layout);

        rvOfflineResult = findViewById(R.id.rvOfflineResult);
        offLineResultLayout = findViewById(R.id.offlineResultLayout);
        sliderView=findViewById(R.id.slider);
         recyclerLiveClassList = findViewById(R.id.recyclerLiveClassesList);
        testSeriesViewAllBtn = findViewById(R.id.testSeriesViewAllBtn);

        no_data_card = findViewById(R.id.no_data_card);

        getProfileDataUrl = getApplicationContext().getString(R.string.apiURL) + "getProfile";
        getPackageListApi = getApplicationContext().getString(R.string.apiURL) + "getProductPackageList";


        dialog = new ProgressDialog(DashboardActivity.this);
        dialog.setMessage("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        menuBtn = findViewById(R.id.profile_back_btn);


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String deviceToken = task.getResult();
                Log.e("Token",deviceToken);

            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("all_users");

        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);


        SharedPreferences userIsRegisteredSuccessful = getSharedPreferences("CNB", MODE_PRIVATE);
        boolean registered = userIsRegisteredSuccessful.getBoolean("registered", true);
        sid = userIsRegisteredSuccessful.getInt("sid", 0);
        current_login_id = userIsRegisteredSuccessful.getString("current_login_id","");
        enrollno = userIsRegisteredSuccessful.getString("enrollno","");
        Log.e("aaaaaaa", "sid" + sid);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.e("sid", String.valueOf(sid));
            if (registered) {
                uid = user.getUid();
                Log.e("uid", uid);
            }
        }
        mobileNo = getIntent().getStringExtra("mobileNo");



        deeplinkFirebase = getIntent().getStringExtra("deeplinkFirebase");


        // Continue Watching-------------------------

        continueWatchingContainer = findViewById(R.id.continueWatchingContainer);
        continueWatchingContainer.setVisibility(View.GONE);
        itemPicker = findViewById(R.id.continueWatchingList);
        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.setSlideOnFling(true);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());


        //Checking internet Connectivity
        filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        reciever = new ConnectivityReciever();

        //Retriving user details stored in Local---------------------------------------
        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        studName = studDetails.getString("profileName", "NO_NAME");
        studClass = studDetails.getString("qualification", "NO_CLASS");
        studSchool = studDetails.getString("schoolId", "NO_SCHOOL");
        sid = studDetails.getInt("sid", 0);
        String sidString = String.valueOf(sid);
        getDashboardData = getApplicationContext().getString(R.string.apiURL) + "getUserDashboardData";
        getOfflineResultData = getApplicationContext().getString(R.string.apiURL) +"students/"+ sidString + "/offlineresultall";
        Log.e("offlineResult", getOfflineResultData);


        //Putting name on dasboard--------------------Hello User--------------
        dashboardUserName = findViewById(R.id.userName);
        String arr[] = studName.split(" ", 2);
        String firstName = arr[0];
        Log.e(TAG, "onCreate: " + UserDashName);//quick brown fox
        if (UserDashName.isEmpty() || UserDashName.equals("") || UserDashName.equals(null) || UserDashName.equals(" ")) {
            dashboardUserName.setText("Hello " + firstName);
        } else {
            dashboardUserName.setText(UserDashName);
        }

        //Search Button---------------------------------------------------
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(DashboardActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        //viewAll
        popularCousre_viewAllBtn = findViewById(R.id.popularCoursesViewAllBtn);
        myCourse_viewAllBtn = findViewById(R.id.MyCoursesViewAllBtn);
        liveClassViewAllBtn=findViewById(R.id.liveClassViewAllBtn);
        llOfflineResult = findViewById(R.id.llOfflineResult);
        popularCousre_viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, PopularCoursesActivity.class));
            }
        });

        testSeriesViewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AllTestSeriesListActivity.class));

            }
        });
        liveClassViewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AllLiveClassActivity.class));

            }
        });
        myCourse_viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, MyCoursesActivity.class));

            }
        });
        popularTeachers_viewAllBtn = findViewById(R.id.popularTeachersViewAllBtn);
        popularInstitute_viewAllBtn = findViewById(R.id.popularInstituteViewAllBtn);

        popularTeachers_viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AllPopularTeachers.class));
            }
        });

        popularInstitute_viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AllPopularInstitute.class));
            }
        });

        packagesViewALLBtn = findViewById(R.id.packageFullViewAllBtn);
        packagesViewALLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, AllPackageActivity.class));
            }
        });

        analytics_viewAllBtn = findViewById(R.id.analyticsFullPage);
        analytics_viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AllAnalyticsPage.class));
            }
        });

        llOfflineResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, OfflineResultListActivity.class));
            }
        });


        //Live Classes
        noLiveClasses = findViewById(R.id.noLiveClasses);
        //Live class view all button----------------------------------------------------
        live_ViewAllBtn = findViewById(R.id.LiveViewAllBtn);
        live_ViewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liveClassesList.smoothScrollToPosition(totalLiveClasses);
                Toast.makeText(DashboardActivity.this, "Your are at the last.", Toast.LENGTH_SHORT).show();
            }
        });

        //Notification Button--------------------------------------------------
        notificationBtn = findViewById(R.id.notificationBtn);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent notificationIntent = new Intent(DashboardActivity.this, NotificationActivity.class);
                startActivity(notificationIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        //Live Classes Layout and work------------------------------------------
        liveClassesList = findViewById(R.id.liveClassesList);

//        fetchLiveClasses();
        //Drawer Layout--------------------------------------------
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        nvDrawer.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

        userImage = nvDrawer.getHeaderView(0).findViewById(R.id.userImage);
//        profileName.setText(studName);



        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);



        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(Gravity.LEFT);
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent profile = new Intent(DashboardActivity.this, ProfileActivity.class);
//                profile.putExtra("instituteForProfile", instituteForProfile);
//                profile.putExtra("fromActivity", "homePage");
//                startActivity(profile);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        new getProfileData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new GetOfflineResult().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);




        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mImageUri = preferences.getString("image", null);

        if (mImageUri != null) {
            userImage.setImageURI(Uri.parse(mImageUri));
        } else {

        }

        View decorView = getWindow().getDecorView();
        decorView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    // Handle the back button event here

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                    // ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = DashboardActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.exit_popup, null);
                    dialogBuilder.setView(dialogView);

                    //Alert Dialog Layout work
                    final AlertDialog alertDialog = dialogBuilder.create();
//                TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button cancelBtn = dialogView.findViewById(R.id.cancelButton);
                    Button yesBtn = dialogView.findViewById(R.id.yesButton);


                    cancelBtn.setText("No");

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finishAffinity();
                            Intent a = new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);

                        }
                    });


                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(true);

                    return true; // Consume the event
                }
                return false;
            }
        });


    }







    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        // Class fragmentClass = HomeFragment.class;
        switch (menuItem.getItemId()) {
//            case R.id.calender:
//                Snackbar.make(mDrawer, "Coming soon", Snackbar.LENGTH_LONG)
//                        .setAction("CLOSE", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                            }
//                        })
//                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
//                        .show();
//
//                break;

            case R.id.testSeries:
                Intent testSeriesIntent = new Intent(DashboardActivity.this, TestSeriesDashboardActivity.class);
                startActivity(testSeriesIntent);
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.certificates:
                Intent certificate = new Intent(DashboardActivity.this, InstallmentPaymentActivity.class);
                certificate.putExtra("instituteForProfile", instituteForProfile);
                certificate.putExtra("fromActivity", "homePage");
                certificate.putExtra("sid", String.valueOf(sid));
                certificate.putExtra("enrollNo", enrollno);
                String stuId2 = String.valueOf(sid);

                certificate.putExtra("student_id", stuId2);
                startActivity(certificate);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.visit_us:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://chalksnboard.com/home"));
                startActivity(browserIntent);
                break;


            case R.id.transaction:
                Intent topperIntent = new Intent(DashboardActivity.this, TransactionHistory.class);
                String stuId = String.valueOf(sid);
                topperIntent.putExtra("student_id", stuId);
                startActivity(topperIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;


            case R.id.profile:
                Intent profile = new Intent(DashboardActivity.this, ProfileActivity.class);
                profile.putExtra("instituteForProfile", instituteForProfile);
                profile.putExtra("fromActivity", "homePage");
                startActivity(profile);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.offlineResult:
                Intent offlineResult = new Intent(DashboardActivity.this, OfflineResultListActivity.class);
                startActivity(offlineResult);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.myCourses:
                Intent myCourses = new Intent(DashboardActivity.this, MyCoursesActivity.class);
                startActivity(myCourses);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;


            case R.id.report_bug:
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setType("message/rfc822");
                i.setData(Uri.parse("mailto:"));
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@chalksnboard.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Reporting Bug for ChalksNBoard App");
                i.putExtra(Intent.EXTRA_TEXT, "Hi Developer,\n\n");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DashboardActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Download the android app :\n" +
                        " " +
                        "https://play.google.com/store/apps/details?id=com.vision_digital";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.logout:
                SharedPreferences.Editor editor = getSharedPreferences("CNB", MODE_PRIVATE).edit();
                editor.putString("isLogin", "No");
                editor.apply();


                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DashboardActivity.this, LogInViaPhoneActivity.class));
                finishAffinity();
                finish();
                break;
            case R.id.rate_us:
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                break;
            case R.id.needHelp:
                Intent needHelpIntent = new Intent(DashboardActivity.this, NeedHelpActivity.class);
                startActivity(needHelpIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;


            default:
                break;
        }

        mDrawer.closeDrawers();
    }

    private ConnectivityReciever reciever;
    IntentFilter filter;

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        ConnectivityReciever connectivityReciever = new ConnectivityReciever();
        connectivityReciever.showSnackbar(isConnected, findViewById(R.id.drawer_layout), getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
//        liveClassesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        liveClassesAdapter.stopListening();
        ConnectivityReciever.snackbar = null;

    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(reciever);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new GetDashboardData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new getProfileData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        MyApplication.getInstance().setConnectivityListener(this);
        registerReceiver(reciever, filter);
    }

    class GetOfflineResult extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(DashboardActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            this.dialog.dismiss();

            JSONParser jsonParser = new JSONParser(DashboardActivity.this);

            String param = "uid=" + uid + "&sid=" + sid;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(getOfflineResultData, "GET", param);
            if (jsonObject != null) {
                return jsonObject.toString();
            } else {
                Log.e("DATA OFFLINE", "NOT CONNECTED WITH API");
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("json offline", s);

            if (!s.equals("")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    Log.e("Result Offline : ", s);

                    //Do work-----------------------------
                    this.dialog.dismiss();
                    String status = jsonObject.getString("success");
                    final JSONArray dataArr = jsonObject.getJSONArray("data");

                    Log.e("Status", status.toString());
                    if (status.equals("true")) {
                        if(dataArr.length()==0){
                            offLineResultLayout.setVisibility(View.GONE);
                        }else{
                            offLineResultLayout.setVisibility(View.VISIBLE);
                            Log.e("DATA OFFLINE", dataArr.toString());
                            offlineResultListArrayList.clear();
                            for (int i = 0; i < dataArr.length(); i++) {
                                ItemOfflineResultList itemOfflineResult = new ItemOfflineResultList();
                                JSONObject dataObj = dataArr.getJSONObject(i);
                                String id = String.valueOf(dataObj.getInt("id"));
//                                String student_rollno = dataObj.getString("student_rollno");
//                                String sid = String.valueOf(dataObj.getInt("sid"));
                                String student_name = dataObj.getString("student_name");
                                String batch_no = dataObj.getString("batch_no");
                                String physics = dataObj.getString("physics");
                                String chemistry = dataObj.getString("chemistry");
                                String maths = dataObj.getString("maths");
                                String biology = dataObj.getString("biology");
                                String total = dataObj.getString("total");
                                String percentage = dataObj.getString("percentage");
                                String rank = dataObj.getString("rank");
                                String test_date = dataObj.getString("test_date");
                                String created_at = dataObj.getString("created_at");
                                String updated_at = dataObj.getString("updated_at");
                                itemOfflineResult.setBatchNumber(batch_no);
                                itemOfflineResult.setTestDate(test_date);
                                itemOfflineResult.setRank(rank);
                                itemOfflineResult.setPhysics(physics);
                                itemOfflineResult.setChemistry(chemistry);
                                itemOfflineResult.setBiology(biology);
                                itemOfflineResult.setMaths(maths);
                                itemOfflineResult.setPercentage(percentage);
                                itemOfflineResult.setTotalMarks(total);

                                offlineResultListArrayList.add(itemOfflineResult);
                            }

                            LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.this, RecyclerView.HORIZONTAL, false);
                            rvOfflineResult.setLayoutManager(layoutManager);
                            offlineResultAdapter = new ItemOfflineResultAdapter(DashboardActivity.this, offlineResultListArrayList);
                            rvOfflineResult.setAdapter(offlineResultAdapter);


                            Log.e("DATA", dataArr.toString());

                        }



                    } else if (status.equals("false")) {
                        Toast.makeText(DashboardActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(DashboardActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
        }
    }

    class GetDashboardData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(DashboardActivity.this);


            int versoncodes;


            PackageInfo pInfo = null;
            try {
                pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            versoncodes = pInfo.versionCode;
            Log.e("versionCode", String.valueOf(versoncodes));



            String param = "uid=" + uid + "&app_version=" + versoncodes + "&sid=" + sid + "&current_login_id=" + current_login_id;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(getDashboardData, "POST", param);
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
                        case "success":
                            //Running Fine
                            //Checking App update-------------------------------
                            int appUpdate = dataObj.getInt("app_update");
                            switch (appUpdate) {
                                case 0:
                                    //No Updates
                                    //Checking User Status------------------------------
                                    String userStatus = dataObj.getString("user_status");
                                    if (userStatus.equals("")) {

                                        // Live Class

                                        JSONArray currentLiveClass = dataObj.getJSONArray("current_live_class");
                                        liveClassesLists.clear();
                                        if (currentLiveClass.length()==0){
                                            sliderView.setVisibility(View.GONE);
                                        }else {
                                            for (int i = 0; i < currentLiveClass.length(); i++) {
                                                GoingOnLiveModel goingOnLiveModel = new GoingOnLiveModel();
                                                JSONObject packageJSONObject = currentLiveClass.getJSONObject(i);
                                                goingOnLiveModel.setId(String.valueOf(packageJSONObject.getInt("id")));
                                                goingOnLiveModel.setCourseId(String.valueOf(packageJSONObject.getInt("live_course_id")));
                                                goingOnLiveModel.setLiveDate(packageJSONObject.getString("live_date"));
                                                goingOnLiveModel.setLiveTime(packageJSONObject.getString("live_time"));
                                                goingOnLiveModel.setUrl(packageJSONObject.getString("live_url"));
                                                goingOnLiveModel.setTitle(packageJSONObject.getString("live_title"));
                                                goingOnLiveModel.setCourseTitle(packageJSONObject.getString("course_title"));
                                                goingOnLiveModel.setDescription(packageJSONObject.getString("live_desc"));
                                                goingOnLiveModel.setLiveStatus(packageJSONObject.getString("live_status"));
                                                goingOnLiveModel.setLiveCurrentStatus(packageJSONObject.getString("live_current_status"));


                                                liveClassesLists.add(goingOnLiveModel);
                                            }
                                            Log.e("LOG", "IN IF LOOP");


                                            goingOnLiveAdapter = new GoingOnLiveAdapter(liveClassesLists, DashboardActivity.this);


                                            LinearLayoutManager layoutManagerPopularCourses = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                            recyclerLiveClassList.setLayoutManager(layoutManagerPopularCourses);
                                            recyclerLiveClassList.setAdapter(goingOnLiveAdapter);

                                        }

                                        // Slider

                                        JSONArray sliderArrayObj = dataObj.getJSONArray("top_banner");
                                        sliderModelList.clear();
                                        if (sliderArrayObj.length()==0){
                                            sliderView.setVisibility(View.GONE);
                                        }else {
                                            sliderView.setVisibility(View.VISIBLE);
                                            for (int i = 0; i < sliderArrayObj.length(); i++) {
                                                SliderModel itemSliderList = new SliderModel();
                                                JSONObject packageJSONObject = sliderArrayObj.getJSONObject(i);
                                                itemSliderList.setId(String.valueOf(packageJSONObject.getInt("id")));
                                                itemSliderList.setTitle(packageJSONObject.getString("title"));
                                                itemSliderList.setCourseId(String.valueOf(packageJSONObject.getInt("course_id")));
                                                itemSliderList.setImage(packageJSONObject.getString("image"));
                                                itemSliderList.setCourseType(packageJSONObject.getString("course_type"));
                                                sliderModelList.add(itemSliderList);
                                            }
                                            Log.e("LOG", "IN IF LOOP  Banner");


                                            SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList, DashboardActivity.this);
                                            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                                            sliderView.setSliderAdapter(sliderAdapter);
                                            sliderView.setScrollTimeInSec(3);
                                            sliderView.setAutoCycle(true);
                                            sliderView.startAutoCycle();

                                        }


                                        // Popular Course


                                        JSONArray popularCourses = dataObj.getJSONArray("popular_courses");
                                        popularCoursesList.clear();
                                        for (int i = 0; i < popularCourses.length(); i++) {
                                            ItemMyCourse itemMyCourse = new ItemMyCourse();
                                            JSONObject popularCoursesJSONObject = popularCourses.getJSONObject(i);
                                            itemMyCourse.setId(popularCoursesJSONObject.getString("id"));
                                            itemMyCourse.setDescription(popularCoursesJSONObject.getString("description"));
                                            itemMyCourse.setDuration(popularCoursesJSONObject.getString("duration"));
                                            itemMyCourse.setOwner_name(popularCoursesJSONObject.getString("owner_name"));
                                            itemMyCourse.setTitle(popularCoursesJSONObject.getString("title"));
                                            itemMyCourse.setImage(popularCoursesJSONObject.getString("image"));
                                            itemMyCourse.setPrice(popularCoursesJSONObject.getString("monthly_rate"));
                                            itemMyCourse.setOwner_qualification(popularCoursesJSONObject.getString("owner_qualification"));
                                            popularCoursesList.add(itemMyCourse);
                                        }



                                        //Popular courses-------------
                                        popularCourseslistView = findViewById(R.id.popularCourseList);
                                        LinearLayoutManager layoutManagerPopularCourses = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                        popularCourseslistView.setLayoutManager(layoutManagerPopularCourses);
                                        popularCoursesAdapter = new ItemPopularCoursesAdapter(popularCoursesList);
                                        popularCourseslistView.setAdapter(popularCoursesAdapter);

                                        // Packages


                                        JSONArray popularPackageArr = dataObj.getJSONArray("popular_package");
                                        packageLists.clear();
                                        if (popularPackageArr.length() == 0) {

                                        } else {
                                            for (int i = 0; i < popularPackageArr.length(); i++) {
                                                ItemPackageList itemPackageList = new ItemPackageList();
                                                JSONObject packageJSONObject = popularPackageArr.getJSONObject(i);
                                                itemPackageList.setId(String.valueOf(packageJSONObject.getInt("id")));
                                                itemPackageList.setTitle(packageJSONObject.getString("title"));
                                                itemPackageList.setOwnerName(packageJSONObject.getString("owner_name"));
                                                itemPackageList.setImage(packageJSONObject.getString("image"));
                                                itemPackageList.setPrice(String.valueOf(packageJSONObject.getInt("price")));
                                                packageLists.add(itemPackageList);
                                            }
                                            Log.e("LOG", "IN IF LOOP");

                                        }

                                        // TestSeries


                                        JSONArray testSeriesArr = dataObj.getJSONArray("popular_testseries_courses");
                                        testSeriesList.clear();
                                        if (popularPackageArr.length() == 0) {

                                        } else {
                                            for (int i = 0; i < testSeriesArr.length(); i++) {
                                                DashTestSeriesModel dashTestSeriesModel = new DashTestSeriesModel();
                                                JSONObject packageJSONObject = testSeriesArr.getJSONObject(i);
                                                dashTestSeriesModel.setId(String.valueOf(packageJSONObject.getInt("id")));
                                                dashTestSeriesModel.setTitle(packageJSONObject.getString("title"));
                                                dashTestSeriesModel.setImage(packageJSONObject.getString("image"));
                                                dashTestSeriesModel.setActualPrice(String.valueOf(packageJSONObject.getInt("actual_price")));
                                                dashTestSeriesModel.setSellingPrice(String.valueOf(packageJSONObject.getInt("selling_price")));
                                                testSeriesList.add(dashTestSeriesModel);
                                            }
                                            Log.e("LOG", "IN IF LOOP");

                                        }

                                        JSONArray liveClassesArr = dataObj.getJSONArray("popular_live_courses");
                                        liveClassList.clear();
                                        if (liveClassesArr.length() == 0) {

                                        } else {
                                            for (int i = 0; i < liveClassesArr.length(); i++) {
                                                DashTestSeriesModel dashTestSeriesModel = new DashTestSeriesModel();
                                                JSONObject packageJSONObject = liveClassesArr.getJSONObject(i);
                                                dashTestSeriesModel.setId(String.valueOf(packageJSONObject.getInt("id")));
                                                dashTestSeriesModel.setTitle(packageJSONObject.getString("title"));
                                                dashTestSeriesModel.setImage(packageJSONObject.getString("image"));
                                                dashTestSeriesModel.setActualPrice(String.valueOf(packageJSONObject.getInt("actual_price")));
                                                dashTestSeriesModel.setSellingPrice(String.valueOf(packageJSONObject.getInt("selling_price")));
                                                liveClassList.add(dashTestSeriesModel);
                                            }
                                            Log.e("LOG", "IN IF LOOP");

                                        }

//                                        Continue Watching------------------------
                                        String continueWatching1 = (dataObj.getString("continue_watching_graph"));
                                        if (!continueWatching1.equals("")) {
                                            JSONArray continueWatching = dataObj.getJSONArray("continue_watching_graph");

                                            courseItemList.clear();
                                            spentList.clear();
                                            actualList.clear();
                                            for (int i = 0; i < continueWatching.length(); i++) {
                                                CourseItem courseItem = new CourseItem();
                                                JSONObject courseObject = continueWatching.getJSONObject(i);
                                                courseItem.setCourseId(courseObject.getString("course_id"));
//                                                courseId= courseObject.getString("course_id");
//                                                Log.e("cid",courseId);
                                                courseItem.setCourseTitle(courseObject.getString("course_name"));
                                                courseItem.setLastPlayedMilestoneId(courseObject.getString("last_mile_id"));
                                                courseItem.setMilestoneName(courseObject.getString("milestone_name"));
                                                courseItem.setCourseDuration(courseObject.getDouble("course_duration"));
//
                                                courseItem.setAccuracy(courseObject.getDouble("accuracy"));
//
                                                courseItem.setPercentageWatched(courseObject.getDouble("percentage"));
//
//
                                                courseItem.setActualPlayDuration(courseObject.getDouble("actual_play_duration"));
                                                courseItem.setActualMilestoneDuration(courseObject.getDouble("actual_milestone_duration"));


                                                JSONArray requiredTime = courseObject.getJSONArray("actual_video_time");
                                                for (int a = 0; a < requiredTime.length(); a++) {


                                                    double secondT = requiredTime.getDouble(a);
                                                    int remainderT = (int) secondT % 60;
                                                    int varT = (int) secondT - remainderT;
                                                    int minsT = varT / 60;

                                                    courseItem.getActualTimeList().add(new Entry(a, minsT));

                                                }


                                                JSONArray spentTime = courseObject.getJSONArray("spent_video_time");
                                                for (int b = 0; b < spentTime.length(); b++) {


                                                    double secondT = spentTime.getDouble(b);
                                                    int remainderT = (int) secondT % 60;
                                                    int varT = (int) secondT - remainderT;
                                                    int minsT = varT / 60;


                                                    courseItem.getSpentTimeList().add(new Entry(b, minsT));

                                                }


                                                courseItemList.add(courseItem);
                                            }
                                            if (courseItemList.isEmpty()) {
                                                continueWatchingContainer.setVisibility(View.GONE);
                                                no_data_card.setVisibility(View.GONE);
                                            } else {
                                                continueWatchingContainer.setVisibility(View.VISIBLE);
                                                no_data_card.setVisibility(View.GONE);
                                            }
                                            courseAdapter = new CourseAdapter(courseItemList);
                                            itemPicker.setAdapter(courseAdapter);
                                        }


//                                        Fetching Popular Teachers--------------------
                                        JSONArray popular_teachers = dataObj.getJSONArray("popular_teachers");
                                        popularTeachersList.clear();
                                        popularTeacherList.clear();
                                        for (int i = 0; i < popular_teachers.length(); i++) {
                                            ItemTeacher itemTeacher = new ItemTeacher();
                                            JSONObject popular_teachersJSONObject = popular_teachers.getJSONObject(i);
                                            itemTeacher.setId(popular_teachersJSONObject.getLong("id"));
                                            itemTeacher.setName(popular_teachersJSONObject.getString("name"));
                                            itemTeacher.setCity(popular_teachersJSONObject.getString("city"));
                                            itemTeacher.setSubscriber(popular_teachersJSONObject.getString("subscriber"));
                                            itemTeacher.setCountry(popular_teachersJSONObject.getString("country"));
                                            itemTeacher.setMobile(popular_teachersJSONObject.getString("mobile"));
                                            itemTeacher.setPopularity(popular_teachersJSONObject.getLong("popularity"));
                                            itemTeacher.setImage(popular_teachersJSONObject.getString("image"));
                                            itemTeacher.setQualification(popular_teachersJSONObject.getString("qualification"));
                                            itemTeacher.setType("teacher");
                                            popularTeachersList.add(itemTeacher);
                                            popularTeacherList.add(itemTeacher);
                                        }

//Popular Teachers
                                        popularTeacherslistView = findViewById(R.id.popularTeachersList);
                                        LinearLayoutManager layoutManagerPopularTeachers = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                        popularTeacherslistView.setLayoutManager(layoutManagerPopularTeachers);
                                        itemTeacherAdapter = new ItemTeacherAdapter(popularTeachersList);
                                        popularTeacherslistView.setAdapter(itemTeacherAdapter);





//

                                    } else if (userStatus.equals("banned")) {
                                        //Student is banned--------------------------
                                        String message = dataObj.getString("user_status_banned_message");
                                        Toast.makeText(DashboardActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else {



                                        JSONArray popularCourses = dataObj.getJSONArray("popular_courses");
                                        popularCoursesList.clear();
                                        for (int i = 0; i < popularCourses.length(); i++) {
                                            ItemMyCourse itemMyCourse = new ItemMyCourse();
                                            JSONObject popularCoursesJSONObject = popularCourses.getJSONObject(i);
                                            itemMyCourse.setId(popularCoursesJSONObject.getString("id"));
                                            itemMyCourse.setDescription(popularCoursesJSONObject.getString("description"));
                                            itemMyCourse.setDuration(popularCoursesJSONObject.getString("duration"));
                                            itemMyCourse.setOwner_name(popularCoursesJSONObject.getString("owner_name"));
                                            itemMyCourse.setTitle(popularCoursesJSONObject.getString("title"));
                                            itemMyCourse.setPrice(popularCoursesJSONObject.getString("monthly_rate"));
                                            itemMyCourse.setImage(popularCoursesJSONObject.getString("image"));
                                            itemMyCourse.setOwner_qualification(popularCoursesJSONObject.getString("owner_qualification"));
                                            popularCoursesList.add(itemMyCourse);
                                        }

//                                        fetchLiveClasses();

                                        //Popular courses-------------
                                        //Popular courses-------------
                                        popularCourseslistView = findViewById(R.id.popularCourseList);
                                        LinearLayoutManager layoutManagerPopularCourses = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                        popularCourseslistView.setLayoutManager(layoutManagerPopularCourses);
                                        popularCoursesAdapter = new ItemPopularCoursesAdapter(popularCoursesList);
                                        popularCourseslistView.setAdapter(popularCoursesAdapter);


//                                        Continue Watching------------------------
                                        String continueWatching1 = (dataObj.getString("continue_watching_graph"));
                                        if (!continueWatching1.equals("")) {
                                            JSONArray continueWatching = dataObj.getJSONArray("continue_watching_graph");

                                            courseItemList.clear();
                                            spentList.clear();
                                            actualList.clear();
                                            for (int i = 0; i < continueWatching.length(); i++) {
                                                CourseItem courseItem = new CourseItem();
                                                JSONObject courseObject = continueWatching.getJSONObject(i);
                                                courseItem.setCourseId(courseObject.getString("course_id"));
//                                                courseId= courseObject.getString("course_id");
//                                                Log.e("cid",courseId);
                                                courseItem.setCourseTitle(courseObject.getString("course_name"));
                                                courseItem.setLastPlayedMilestoneId(courseObject.getString("last_mile_id"));
                                                courseItem.setMilestoneName(courseObject.getString("milestone_name"));
                                                courseItem.setCourseDuration(courseObject.getDouble("course_duration"));
//                                                courseItem.setTotalPlayDuration(courseObject.getDouble("total_play_duration"));
                                                courseItem.setAccuracy(courseObject.getDouble("accuracy"));

                                                courseItem.setPercentageWatched(courseObject.getDouble("percentage"));


                                                courseItem.setActualPlayDuration(courseObject.getDouble("actual_play_duration"));
                                                courseItem.setActualMilestoneDuration(courseObject.getDouble("actual_milestone_duration"));


                                                JSONArray requiredTime = courseObject.getJSONArray("actual_video_time");
                                                for (int a = 0; a < requiredTime.length(); a++) {


                                                    double secondT = requiredTime.getDouble(a);
                                                    int remainderT = (int) secondT % 60;
                                                    int varT = (int) secondT - remainderT;
                                                    int minsT = varT / 60;

                                                    courseItem.getActualTimeList().add(new Entry(a, minsT));

                                                }


                                                JSONArray spentTime = courseObject.getJSONArray("spent_video_time");
                                                for (int b = 0; b < spentTime.length(); b++) {



                                                    double secondT = spentTime.getDouble(b);
                                                    int remainderT = (int) secondT % 60;
                                                    int varT = (int) secondT - remainderT;
                                                    int minsT = varT / 60;


                                                    courseItem.getSpentTimeList().add(new Entry(b, minsT));

                                                }


                                                courseItemList.add(courseItem);
                                            }
                                            if (courseItemList.isEmpty()) {
                                                continueWatchingContainer.setVisibility(View.GONE);
                                                no_data_card.setVisibility(View.GONE);
                                            } else {
                                                continueWatchingContainer.setVisibility(View.VISIBLE);
                                                no_data_card.setVisibility(View.GONE);
                                            }
                                            courseAdapter = new CourseAdapter(courseItemList);
                                            itemPicker.setAdapter(courseAdapter);
                                        }


//                                        Fetching Popular Teachers--------------------
                                        JSONArray popular_teachers = dataObj.getJSONArray("popular_teachers");
                                        popularTeachersList.clear();
                                        popularTeacherList.clear();
                                        for (int i = 0; i < popular_teachers.length(); i++) {
                                            ItemTeacher itemTeacher = new ItemTeacher();
                                            JSONObject popular_teachersJSONObject = popular_teachers.getJSONObject(i);
                                            itemTeacher.setId(popular_teachersJSONObject.getLong("id"));
                                            itemTeacher.setName(popular_teachersJSONObject.getString("name"));
                                            itemTeacher.setCity(popular_teachersJSONObject.getString("city"));
                                            itemTeacher.setSubscriber(popular_teachersJSONObject.getString("subscriber"));
                                            itemTeacher.setCountry(popular_teachersJSONObject.getString("country"));
                                            itemTeacher.setMobile(popular_teachersJSONObject.getString("mobile"));
                                            itemTeacher.setPopularity(popular_teachersJSONObject.getLong("popularity"));
                                            itemTeacher.setImage(popular_teachersJSONObject.getString("image"));
                                            itemTeacher.setQualification(popular_teachersJSONObject.getString("qualification"));
                                            itemTeacher.setType("teacher");
                                            popularTeachersList.add(itemTeacher);
                                            popularTeacherList.add(itemTeacher);
                                        }

//Popular Teachers
                                        popularTeacherslistView = findViewById(R.id.popularTeachersList);
                                        LinearLayoutManager layoutManagerPopularTeachers = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                        popularTeacherslistView.setLayoutManager(layoutManagerPopularTeachers);
                                        itemTeacherAdapter = new ItemTeacherAdapter(popularTeachersList);
                                        popularTeacherslistView.setAdapter(itemTeacherAdapter);

                                        //Fetching Popular Institute--------------------
                                        JSONArray popular_institute = dataObj.getJSONArray("popular_schools");
                                        popularInstituteList.clear();
                                        popularInstitutesList.clear();
                                        instituteForProfile.clear();
                                        for (int i = 0; i < popular_institute.length(); i++) {
                                            ItemTeacher itemInstitute = new ItemTeacher();
                                            JSONObject popular_instituteJSONObjec = popular_institute.getJSONObject(i);
                                            itemInstitute.setId(popular_instituteJSONObjec.getLong("id"));
                                            itemInstitute.setName(popular_instituteJSONObjec.getString("name"));
                                            itemInstitute.setCity(popular_instituteJSONObjec.getString("city"));
                                            itemInstitute.setSubscriber(popular_instituteJSONObjec.getString("subscriber"));
                                            itemInstitute.setCountry(popular_instituteJSONObjec.getString("country"));
                                            itemInstitute.setMobile(popular_instituteJSONObjec.getString("mobile"));
                                            itemInstitute.setPopularity(popular_instituteJSONObjec.getLong("popularity"));
                                            itemInstitute.setImage(popular_instituteJSONObjec.getString("image"));
                                            itemInstitute.setQualification(popular_instituteJSONObjec.getString("qualification"));
                                            itemInstitute.setType("institute");
                                            popularInstituteList.add(itemInstitute);
                                            popularInstitutesList.add(itemInstitute);
                                            instituteForProfile.add(popular_instituteJSONObjec.getString("name"));
                                        }

                                        instituteForProfile.add("other");
                                        popularInstitutelistView = findViewById(R.id.popularInstituteList);
                                        LinearLayoutManager layoutManagerPopularInstitute = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                        popularInstitutelistView.setLayoutManager(layoutManagerPopularInstitute);
                                        itemInstitueAdapter = new ItemTeacherAdapter(popularInstituteList);
                                        popularInstitutelistView.setAdapter(itemInstitueAdapter);

                                        // Course Analytics
                                        JSONArray analytics = dataObj.getJSONArray("my_courses_analytics");
                                        if (analytics.length() == 0) {
                                            myAnalyticsLayout.setVisibility(View.GONE);

                                        } else {
                                            myAnalyticsLayout.setVisibility(View.VISIBLE);

                                            courseAnalytics.clear();
                                            for (int i = 0; i < analytics.length(); i++) {
                                                ItemAnalytics itemMyCourse = new ItemAnalytics();
                                                JSONObject coursesAnalyticsJSONObject = analytics.getJSONObject(i);
                                                itemMyCourse.setId(coursesAnalyticsJSONObject.getLong("id"));
                                                //itemMyCourse.setDescription(popularCoursesJSONObject.getString("description"));
                                                // itemMyCourse.setDuration(popularCoursesJSONObject.getString("duration"));
                                                itemMyCourse.setCourseOwner(coursesAnalyticsJSONObject.getString("owner_name"));
                                                itemMyCourse.setCourseTitle(coursesAnalyticsJSONObject.getString("title"));
                                                itemMyCourse.setImage(coursesAnalyticsJSONObject.getString("image"));
                                                //  itemMyCourse.setOwner_qualification(popularCoursesJSONObject.getString("owner_qualification"));
                                                courseAnalytics.add(itemMyCourse);
                                            }

                                            RecyclerView courseAnalyticRecyclerView = findViewById(R.id.analyticRecyclarView);
                                            LinearLayoutManager linearLayoutManagerAnalytic = new LinearLayoutManager(DashboardActivity.this,
                                                    LinearLayoutManager.HORIZONTAL, false);
                                            courseAnalyticRecyclerView.setLayoutManager(linearLayoutManagerAnalytic);

                                            ItemAnalyticsAdapter itemAnalyticsAdapter = new ItemAnalyticsAdapter(DashboardActivity.this
                                                    , courseAnalytics);
                                            courseAnalyticRecyclerView.setAdapter(itemAnalyticsAdapter);

                                        }


                                    }

                                    break;
                                case 1:
                                    //Minor Update------------------------------------------------
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                                    // ...Irrelevant code for customizing the buttons and title
                                    LayoutInflater inflater = DashboardActivity.this.getLayoutInflater();
                                    View dialogView = inflater.inflate(R.layout.update_available_dialog, null);
                                    dialogBuilder.setView(dialogView);
                                    //Alert Dialog Layout work
                                    final AlertDialog alertDialog = dialogBuilder.create();

                                    //Layout-----------------------------------------------
                                    TextView updateNow = dialogView.findViewById(R.id.updateBtn);
                                    updateNow.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Updating App--------------------------
                                            Uri uri = Uri.parse("market://details?id=" + DashboardActivity.this.getPackageName());
                                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                            // To count with Play market backstack, After pressing back button,
                                            // to taken back to our application, we need to add following flags to intent.
                                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                            try {
                                                startActivity(goToMarket);
                                            } catch (ActivityNotFoundException e) {
                                                startActivity(new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse("http://play.google.com/store/apps/details?id=" + DashboardActivity.this.getPackageName())));
                                            }
                                        }
                                    });
                                    TextView updateLater = dialogView.findViewById(R.id.updateLater);
                                    updateLater.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Posponding Update App--------------------------
                                            alertDialog.dismiss();
                                            //Checking User Status------------------------------
                                            String userStatus = null;
                                            try {
                                                userStatus = dataObj.getString("user_status");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            if (userStatus.equals("allowed")) {
                                                //Already Logged in-----------------------
                                                startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                                                finish();
                                            } else if (userStatus.equals("banned")) {
                                                //Student is banned--------------------------
                                                String message = null;
                                                try {
                                                    message = dataObj.getString("user_status_banned_message");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Toast.makeText(DashboardActivity.this, message, Toast.LENGTH_SHORT).show();
                                            } else {
//                                                startActivity(new Intent(DashboardActivity.this, LogInViaPhoneActivity.class));
//                                                finish();
                                            }

                                        }
                                    });
                                    alertDialog.show();
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    break;
                                case 2:
                                    //Major Update------------------------------------------------
                                    AlertDialog.Builder mdialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                                    // ...Irrelevant code for customizing the buttons and title
                                    LayoutInflater minflater = DashboardActivity.this.getLayoutInflater();
                                    View mdialogView = minflater.inflate(R.layout.update_available_dialog, null);
                                    mdialogBuilder.setView(mdialogView);
                                    //Alert Dialog Layout work
                                    // String mmsgContent = dataObj.getString("message");
                                    // mmaintainanceContent.setText(Html.fromHtml(mmsgContent));

                                    //Layout-----------------------------------------------
                                    TextView mupdateNow = mdialogView.findViewById(R.id.updateBtn);
                                    TextView updateLaterBtn = mdialogView.findViewById(R.id.updateLater);
                                    updateLaterBtn.setVisibility(View.GONE);
                                    mupdateNow.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Updating App--------------------------
                                            Uri uri = Uri.parse("market://details?id=" + DashboardActivity.this.getPackageName());
                                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                            // To count with Play market backstack, After pressing back button,
                                            // to taken back to our application, we need to add following flags to intent.
                                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                            try {
                                                startActivity(goToMarket);
                                            } catch (ActivityNotFoundException e) {
                                                startActivity(new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse("http://play.google.com/store/apps/details?id=" + DashboardActivity.this.getPackageName())));
                                            }
                                        }
                                    });

                                    AlertDialog malertDialog = mdialogBuilder.create();
                                    malertDialog.show();
                                    malertDialog.setCanceledOnTouchOutside(false);
                                    break;
                                default:
                                    Toast.makeText(DashboardActivity.this, "Something went wrong. Contact to support over web", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = DashboardActivity.this.getLayoutInflater();
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
                            Toast.makeText(DashboardActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(DashboardActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = DashboardActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exit_popup, null);
        dialogBuilder.setView(dialogView);

        //Alert Dialog Layout work
        final AlertDialog alertDialog = dialogBuilder.create();
//                TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button cancelBtn = dialogView.findViewById(R.id.cancelButton);
        Button yesBtn = dialogView.findViewById(R.id.yesButton);
        TextView message = dialogView.findViewById(R.id.message);
        TextView title = dialogView.findViewById(R.id.title);


        cancelBtn.setText("No");

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
//                finish();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);

//                DashboardActivity.super.onBackPressed();

            }
        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);

    }





    class getProfileData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(DashboardActivity.this);
            // int versionCode = BuildConfig.VERSION_CODE;


            String param = "sid=" + sid;
            Log.e(TAG, "param: " + param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(getProfileDataUrl, "POST", param);
            if (jsonObject != null) {
                Log.e("test-response", jsonObject.toString());
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
                    dialog.dismiss();
                    switch (status) {
                        case "success":
                            //Running Fine
                            JSONObject dataObj = jsonObject.getJSONObject("data");

                            UserDashName = dataObj.getString("name");
                            profileImageUrlString = dataObj.getString("image");
                            String cleanImageUrl = cleanImageUrl(profileImageUrlString);

                            if (cleanImageUrl.equals("")) {
                                Glide.with(getApplicationContext()).load(R.drawable.user_profile).into(userImage);
                            } else {
                                Glide.with(getApplicationContext()).load(cleanImageUrl).into(userImage);
                            }

                            String arr[] = studName.split(" ", 2);
                            String firstName = arr[0];
                            Log.e(TAG, "onCreate: " + UserDashName);//quick brown fox
                            if (UserDashName.isEmpty() || UserDashName.equals("") || UserDashName.equals(null) || UserDashName.equals(" ")) {
                                dashboardUserName.setText("Hello " + firstName);
                            } else {
                                String arrN[] = UserDashName.split(" ", 2);
                                String firstN = arrN[0];
                                dashboardUserName.setText("Hello " + firstN);
                            }

                            break;
                        case "maintainance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = DashboardActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);

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
//
                            Toast.makeText(DashboardActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(DashboardActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    public static String cleanImageUrl(String imageUrl) {
        Pattern pattern = Pattern.compile("\\\\/");
        Matcher matcher = pattern.matcher(imageUrl);
        return matcher.replaceAll("/");
    }


}