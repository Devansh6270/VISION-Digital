package com.vision_digital.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.vision_digital.TestSeries.TestSeriesDashboardActivity;
import com.vision_digital.R;
import com.vision_digital.TestSeries.TestDetailsActivity;
import com.vision_digital.community.CommunityChatPageActivity;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.internetConnectivity.ConnectivityReciever;
import com.vision_digital.internetConnectivity.MyApplication;
import com.vision_digital.model.CoursePackage.ItemPackageAdapter;
import com.vision_digital.model.CoursePackage.ItemPackageList;
import com.vision_digital.model.PopularCourses.ItemPopularCoursesAdapter;
import com.vision_digital.model.analytics.ItemAnalytics;
import com.vision_digital.model.analytics.ItemAnalyticsAdapter;
import com.vision_digital.model.continueWatchingCourseItem.CourseAdapter;
import com.vision_digital.model.continueWatchingCourseItem.CourseItem;
import com.vision_digital.model.liveClass.ItemLiveClass;
import com.vision_digital.model.liveClass.ItemLiveClassViewHolder;
import com.vision_digital.model.myCourses.ItemMyCourse;
import com.vision_digital.model.myCourses.ItemMyCourseAdapter;
import com.vision_digital.model.offlineResult.ItemOfflineResultAdapter;
import com.vision_digital.model.offlineResult.ItemOfflineResultList;
import com.vision_digital.model.otherSubject.OtherSubjectModel;
import com.vision_digital.model.popularTeachers.ItemTeacher;
import com.vision_digital.model.popularTeachers.ItemTeacherAdapter;
import com.vision_digital.profile.EditProfileActivity;
import com.vision_digital.profile.ProfileActivity;
import com.vision_digital.signUp.LogInViaPhoneActivity;
import com.vision_digital.transaction.TransactionHistory;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardActivity extends AppCompatActivity implements ConnectivityReciever.ConnectivityRecieverListener {

    private static int MICROPHONE_PERMISSION_CODE = 200;

    ImageView menuBtn, searchBtn, live_ViewAllBtn, popularInstitute_viewAllBtn, analytics_viewAllBtn;
    ImageView notificationBtn;

    LinearLayout myCourse_viewAllBtn, popularCousre_viewAllBtn, popularTeachers_viewAllBtn, packagesViewALLBtn;

    Button logOut;

    public DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    public static String mobileNo = "";

    TextView profileName, greetingText, dashboardUserName;
    ImageView userImage, notification;
    String studName;
    String studClass;
    String studSchool;

    String UserDashName = "";


    public static int sid;
    RelativeLayout myCourseLayout, myAnalyticsLayout;

    ArrayList<Entry> actualList = new ArrayList<>();
    ArrayList<Entry> spentList = new ArrayList<>();

    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;


    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<String> myCourseListForCommunity;

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

    public static ArrayList<OtherSubjectModel> otherSubjectModelArrayList = new ArrayList<>();


    ProgressDialog dialog;

    //Live RecylerView work------------------------------------------------
    RecyclerView liveClassesList;
    int totalLiveClasses = 0;
    FirestoreRecyclerAdapter liveClassesAdapter;
    TextView noLiveClasses;

    //  Package Course
    ItemPackageAdapter itemPackageAdapter;
    RecyclerView packageRecyclerView;

    public static List<ItemPackageList> packageLists = new ArrayList<>();

    //My Course work-------------------------------------------
    ItemMyCourseAdapter itemMyCourseAdapter;
    RecyclerView myCourseslistView;

    //
    ItemMyCourseAdapter itemPopularCoursesAdapter;

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
    List<String> myCoursesIdList = new ArrayList<>(8);


    String profileImageUrlString = "";

    String deeplinkFirebase = "chalksnboard";

    String getProfileDataUrl = "";
    CardView no_data_card;
    LinearLayout packageLayout;

    private String mImageUri = "";
    ArrayList<ItemOfflineResultList> offlineResultListArrayList = new ArrayList<>();
    ItemOfflineResultAdapter offlineResultAdapter;
    RecyclerView rvOfflineResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        myCourseLayout = findViewById(R.id.myCourseLayout);
        myAnalyticsLayout = findViewById(R.id.analytics_layout);
        myCourseslistView = findViewById(R.id.myCourseList);
        packageLayout = findViewById(R.id.packageLayout);
        packageRecyclerView = findViewById(R.id.recyclerPackageList);
        rvOfflineResult = findViewById(R.id.rvOfflineResult);

        no_data_card = findViewById(R.id.no_data_card);

//        logOut=findViewById(R.id.logout);
        getProfileDataUrl = getApplicationContext().getString(R.string.apiURL1) + "getProfile";
        getPackageListApi = getApplicationContext().getString(R.string.apiURL) + "getProductPackageList";


        dialog = new ProgressDialog(DashboardActivity.this);
        dialog.setMessage("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        menuBtn = findViewById(R.id.profile_back_btn);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);


        SharedPreferences userIsRegisteredSuccessful = getSharedPreferences("CNB", MODE_PRIVATE);
        boolean registered = userIsRegisteredSuccessful.getBoolean("registered", true);
        sid = userIsRegisteredSuccessful.getInt("sid", 0);
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


//        --DEEP LINK HANDLING--

        //www.example.com/course/9
        getDynamicLinkFromFirebase();

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
        getDashboardData = getApplicationContext().getString(R.string.apiURL) + "getDashboardData";
        getOfflineResultData = "http://v.chalksnboard.com/api/v4/students/" + sidString + "/offlineresultall";


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
        popularCousre_viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, PopularCoursesActivity.class));
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
        //working on Navigation View-----------------------profileName, GreetingText---------------------------
//        profileName = nvDrawer.getHeaderView(0).findViewById(R.id.profileName);
        userImage = nvDrawer.getHeaderView(0).findViewById(R.id.userImage);
//        profileName.setText(studName);


//        greetingText = nvDrawer.getHeaderView(0).findViewById(R.id.greetingText);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

//        if (timeOfDay >= 0 && timeOfDay < 12) {
//            greetingText.setText("Good Morning,");
//        } else if (timeOfDay >= 12 && timeOfDay < 16) {
//            greetingText.setText("Good Afternoon,");
//        } else if (timeOfDay >= 16 && timeOfDay < 21) {
//            greetingText.setText("Good Evening,");
//        } else if (timeOfDay >= 21 && timeOfDay < 24) {
//            greetingText.setText("Good Night,");
//        }

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(Gravity.LEFT);
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(DashboardActivity.this, ProfileActivity.class);
                profile.putExtra("instituteForProfile", instituteForProfile);
                profile.putExtra("fromActivity", "homePage");
                startActivity(profile);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


//        CollectionReference liveClassRef = db.collection("liveClasses");
//        liveClassRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                if (value.size() <= 0) {
//                    noLiveClasses.setVisibility(View.VISIBLE);
//                    liveClassesList.setVisibility(View.GONE);
//                } else {
//                    noLiveClasses.setVisibility(View.GONE);
//                    liveClassesList.setVisibility(View.VISIBLE);
//                    liveClassesList.getLayoutParams().height = 600;
//                    liveClassesList.requestLayout();
//                    //Toast.makeText(DashboardActivity.this, "listworking", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
        new getProfileData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new GetOfflineResult().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


//        if (isMicrophonePresent()) {
//            getMicrophonePermission();
//
//        }

//        logOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(DashboardActivity.this, LogInViaPhoneActivity.class));
//                finish();
////                SharedPreferences.Editor editor = getSharedPreferences(CREDENTIALS, MODE_PRIVATE).edit();
////                editor.clear();
////                editor.apply();
////                startActivity(new Intent(NewResultActivity.this, LogInActivity.class));
////                finish();
//            }
//        });


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

    private void getDynamicLinkFromFirebase() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)

                        Log.i("link", "we have dynamic link");
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            Log.i("link", String.valueOf(pendingDynamicLinkData));
                            deepLink = pendingDynamicLinkData.getLink();
                        }

                        // Handle the deep link. For example, open the linked content,
                        // or apply promotional credit to the user's account.
                        // ...
                        if (deepLink != null) {
                            Log.i("link", String.valueOf(deepLink));
                            String value = deepLink.getLastPathSegment();

                            List<String> param = deepLink.getPathSegments();
                            Log.e("ParamList", String.valueOf(param));
                            String paramZero = param.get(0);

                            if (paramZero.equals("courseProfile")) {
                                Log.e("param_one", paramZero);
                                String courseId = param.get(1);
                                Intent i = new Intent(DashboardActivity.this, CourseDetailsActivity.class);
                                i.putExtra("id", courseId);
                                i.putExtra("image", "");
                                i.putExtra("fromActivity", "homePage");
                                startActivity(i);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            } else if (paramZero.equals("mytestProfile")) {
                                String id = param.get(1);
                                Intent k = new Intent(DashboardActivity.this, TestDetailsActivity.class);
                                k.putExtra("id", id);
                                k.putExtra("desc", "");
                                k.putExtra("price", "");
                                k.putExtra("testType", "");
                                startActivity(k);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            } else if (paramZero.equals("teacherProfile")) {
                                String id = param.get(1);
                                Intent k = new Intent(DashboardActivity.this, TeachersDetailsActivity.class);
                                k.putExtra("id", id);
                                startActivity(k);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            } else if (paramZero.equals("schoolProfile")) {
                                String id = param.get(1);
                                Intent k = new Intent(DashboardActivity.this, InstituteDetailsActivity.class);
                                k.putExtra("id", id);
                                startActivity(k);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            } else if (paramZero.equals("community")) {
                                String id = param.get(1);
                                String image = deepLink.getQueryParameter("link");
                                String name = param.get(2);
                                Intent k = new Intent(DashboardActivity.this, CommunityChatPageActivity.class);
                                k.putExtra("community_name", name);
                                k.putExtra("community_id", id);
                                k.putExtra("community_logo", image);
                                k.putExtra("activity", "community_list");
                                startActivity(k);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "getDynamicLink:onFailure", e);
                    }
                });
    }

    private String getDate(long time) {

        SimpleDateFormat formatter, dateFormate;
        long currentTime = new Date().getTime();
//        long diff = currentTime-time;
//        int diffHrs = (int) TimeUnit.MILLISECONDS.toHours(diff);

        dateFormate = new SimpleDateFormat("dd");
        int currentDate = Integer.parseInt(dateFormate.format(new Date(currentTime)));
        int msgDate = Integer.parseInt(dateFormate.format(new Date(time)));

        Log.e("timeAgo", "" + currentDate);
        if (currentDate == msgDate) {
            formatter = new SimpleDateFormat("hh:mm aa");
        } else {
            formatter = new SimpleDateFormat("dd MMM hh:mm aa");
        }
        String dateString = formatter.format(new Date(time));
//        Calendar cal = Calendar.getInstance(new Locale("IN"));
//        cal.setTimeInMillis(time * 1000);
//        String date = DateFormat.format("hh:mm aa", cal).toString();
        return dateString;
    }


    //Animation-------------------------
    public void layoutAnimation(RecyclerView recycler) {
        Context context = recycler.getContext();
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);
        recycler.setLayoutAnimation(animationController);
        recycler.getAdapter().notifyDataSetChanged();
        recycler.scheduleLayoutAnimation();

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


            // for Adda
//            case R.id.communities:
//
//                for (int i = 0; i < myCoursesList.size(); i++) {
//                    String communityId = myCoursesList.get(i).getId();
//                    String communityName = myCoursesList.get(i).getTitle();
//                    String communityImage = myCoursesList.get(i).getImage();
//                    CreateNewGroup(communityName, communityId, communityImage);
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                }
//                if (!userNameString.equals("")) {
//                    Intent community = new Intent(DashboardActivity.this, CommunitiesListActivity.class);
//                    community.putExtra("id", "id");
//                    community.putExtra("fromActivity", "dashboard");
//
//                    startActivity(community);
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                } else {
//                    popupForCompleteProfile();
//                }
//
//                break;


            case R.id.profile:
                Intent profile = new Intent(DashboardActivity.this, ProfileActivity.class);
                profile.putExtra("instituteForProfile", instituteForProfile);
                profile.putExtra("fromActivity", "homePage");
                startActivity(profile);
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
                        "https://play.google.com/store/apps/details?id=com.chalksnboard";
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
//                SharedPreferences.Editor editor = getSharedPreferences(CREDENTIALS, MODE_PRIVATE).edit();
//                editor.clear();
//                editor.apply();
//                startActivity(new Intent(NewResultActivity.this, LogInActivity.class));
//                finish();
                break;
            case R.id.rate_us:
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
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
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                break;
            case R.id.needHelp:
                Intent needHelpIntent = new Intent(DashboardActivity.this, NeedHelpActivity.class);
                startActivity(needHelpIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

//            case R.id.policy:
//                Intent policyIntent = new Intent(DashboardActivity.this, PrivacyPolicyActivity.class);
//                startActivity(policyIntent);
//                break;
//

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

    public void fetchLiveClasses() {
        Log.e("LiveClass", "Live class");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        myCoursesIdList.add("9");
        Query query = db.collection("liveClasses").whereIn("courseId", myCoursesIdList);
        FirestoreRecyclerOptions<ItemLiveClass> options = new FirestoreRecyclerOptions.Builder<ItemLiveClass>().setQuery(query, ItemLiveClass.class).build();

        liveClassesAdapter = new FirestoreRecyclerAdapter<ItemLiveClass, ItemLiveClassViewHolder>(options) {
            @NonNull
            @Override
            public ItemLiveClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_class, parent, false);

                return new ItemLiveClassViewHolder(mView);
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemLiveClassViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull final ItemLiveClass model) {

                holder.teacherNameTV.setText(model.getTeacherName());
                holder.teacherQualification.setText(model.getTeacherQualification());
                holder.classTitle.setText(model.getClassTitle());
                Log.e("Added", "added");
                //Toast.makeText(DashboardActivity.this, "Added", Toast.LENGTH_SHORT).show();
                holder.startedAt.setText("Started At: " + getDate(Long.parseLong(model.getClassStartTimestamp()) * 1000));
                if (!model.getTeacherImage().equals(""))
                    Glide.with(DashboardActivity.this).load(model.getTeacherImage()).into(holder.teacherImage);
                holder.itemLiveClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent goTOLiveIntent = new Intent(DashboardActivity.this, LiveVideoPlayerActivity.class);
                        goTOLiveIntent.putExtra("ApplicationID", model.getApplicationId());
                        goTOLiveIntent.putExtra("broadcastID", model.getBroadcastId());
                        startActivity(goTOLiveIntent);
                    }
                });
                //temp--------------------------------
                totalLiveClasses = position;//---------------------------------
                //-----------------------------------------
                if (position % 2 == 0) {
                    holder.blueCard.setVisibility(View.GONE);
                }
            }
        };
//
//        liveClassesList.setHasFixedSize(true);
        liveClassesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        liveClassesList.setAdapter(liveClassesAdapter);
        onStart();

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
        new getPackagesList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //register connection status listener

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
                    dialog.dismiss();
                    String status = jsonObject.getString("success");
                    final JSONArray dataArr = jsonObject.getJSONArray("data");

                    Log.e("Status", status.toString());
                    if (status.equals("true")) {

                        Log.e("DATA OFFLINE", dataArr.toString());
                        offlineResultListArrayList.clear();
                        for (int i = 0; i < dataArr.length(); i++) {
                            ItemOfflineResultList itemOfflineResult = new ItemOfflineResultList();
                            JSONObject dataObj = dataArr.getJSONObject(i);
                            String id = String.valueOf(dataObj.getInt("id"));
                            String student_rollno = dataObj.getString("student_rollno");
                            String sid = String.valueOf(dataObj.getInt("sid"));
                            String student_name = dataObj.getString("student_name");
                            String batch_no = dataObj.getString("batch_no");
                            String physics = dataObj.getString("physics");
                            String chemistry = dataObj.getString("chemistry");
                            String maths = dataObj.getString("maths");
                            String biology = dataObj.getString("biology");
                            String total = dataObj.getString("total");
                            String percentage = dataObj.getString("percentage");
                            String rank = dataObj.getString("rank") + "\nRank";
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
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
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


            String param = "uid=" + uid + "&app_version=" + versoncodes + "&sid=" + sid;

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
                                    if (userStatus.equals("allowed")) {
                                        //Already L
                                        // ogged in-----------------------
                                        JSONArray mycourses = dataObj.getJSONArray("my_courses");
                                        if (mycourses.length() == 0) {
                                            myCourseLayout.setVisibility(View.GONE);
                                            myCourseslistView.setVisibility(View.GONE);
                                            no_data_card.setVisibility(View.GONE);
                                        } else {
                                            myCourseslistView.setVisibility(View.VISIBLE);
                                            myCourseLayout.setVisibility(View.VISIBLE);
                                            no_data_card.setVisibility(View.GONE);
                                            myCoursesList.clear();
                                            myCoursesIdList.clear();
                                            for (int i = 0; i < mycourses.length(); i++) {
                                                ItemMyCourse itemMyCourse = new ItemMyCourse();
                                                JSONObject courseObj = mycourses.getJSONObject(i);
                                                itemMyCourse.setId(courseObj.getString("id"));
                                                Log.e("Id", courseObj.getString("id"));
                                                if (courseObj.getString("id").equals("8") || courseObj.getString("id").equals("15")) {
                                                    Log.e("89 Id", courseObj.getString("id"));

                                                } else {
                                                    myCoursesIdList.add(courseObj.getString("id"));
                                                    Log.e("LiveClassId", courseObj.getString("id"));

                                                }
                                                itemMyCourse.setDescription(courseObj.getString("description"));
                                                itemMyCourse.setDuration(courseObj.getString("duration"));
                                                itemMyCourse.setOwner_name(courseObj.getString("owner_name"));
                                                itemMyCourse.setTitle(courseObj.getString("title"));
                                                itemMyCourse.setImage(courseObj.getString("image"));
                                                itemMyCourse.setOwner_qualification(courseObj.getString("owner_qualification"));
                                                myCoursesList.add(itemMyCourse);
//                                                myCourseListForCommunity.add(courseObj.getString("title"));
                                            }

                                        }

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

                                        //My courses-------------
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                        myCourseslistView.setLayoutManager(layoutManager);
                                        itemMyCourseAdapter = new ItemMyCourseAdapter(myCoursesList);
                                        myCourseslistView.setAdapter(itemMyCourseAdapter);

//                                        fetchLiveClasses();

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
//                                                double acc = courseObject.getDouble("accuracy");
//                                                if (acc == 0) {
//                                                    courseItem.setAccuracy(0.1d);
//                                                    Log.e("acc", String.valueOf(0.1));
//                                                } else {
//                                                    courseItem.setAccuracy(acc);
//                                                }
                                                courseItem.setPercentageWatched(courseObject.getDouble("percentage"));
//                                                double per = courseObject.getDouble("percentage");
//                                                if (per == 0) {
//                                                    courseItem.setPercentageWatched(0.1d);
//                                                    Log.e("per", String.valueOf(0.1));
//
//                                                } else {
//                                                    courseItem.setPercentageWatched(per);
//
//                                                }
//                                                courseItem.setPercentageWatched(per);
                                                courseItem.setActualPlayDuration(courseObject.getDouble("actual_play_duration"));
                                                courseItem.setActualMilestoneDuration(courseObject.getDouble("actual_milestone_duration"));


                                                JSONArray requiredTime = courseObject.getJSONArray("actual_video_time");
                                                for (int a = 0; a < requiredTime.length(); a++) {
//
//                                                    Long secondT = requiredTime.getLong(a);
//                                                    long hoursT = (long) secondT / 3600;
//                                                    long remainderT = (long) secondT - hoursT * 3600;
//                                                    long minsT = remainderT / 60;
////                                                    yValue1.add(new Entry(a, spentTime.getInt(a)));
////                                                    actualList.add(new Entry(a,spentTime.getLong(a)));
////                                                    courseItem.setActualTimeList(actualList);
//                                                    courseItem.getActualTimeList().add(new Entry(a, minsT));

                                                    double secondT = requiredTime.getDouble(a);
                                                    int remainderT = (int) secondT % 60;
                                                    int varT = (int) secondT - remainderT;
                                                    int minsT = varT / 60;

                                                    courseItem.getActualTimeList().add(new Entry(a, minsT));

                                                }


                                                JSONArray spentTime = courseObject.getJSONArray("spent_video_time");
                                                for (int b = 0; b < spentTime.length(); b++) {

//                                                    Log.e("mint", String.valueOf(minsT));
////                                                    yValue2.add(new Entry(b,minsT));
////                                                    spentList.add(new Entry(b,requiredTime.getLong(b)));
//                                                    courseItem.getSpentTimeList().add(new Entry(b, minsT));

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


//                                        fetchLiveClasses();

                                    } else if (userStatus.equals("banned")) {
                                        //Student is banned--------------------------
                                        String message = dataObj.getString("user_status_banned_message");
                                        Toast.makeText(DashboardActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        //Already L
                                        // ogged in-----------------------
                                        JSONArray mycourses = dataObj.getJSONArray("my_courses");
                                        if (mycourses.length() == 0) {
                                            myCourseLayout.setVisibility(View.GONE);
                                            myCourseslistView.setVisibility(View.GONE);
                                            no_data_card.setVisibility(View.GONE);
                                        } else {
                                            myCourseslistView.setVisibility(View.VISIBLE);
                                            myCourseLayout.setVisibility(View.VISIBLE);
                                            no_data_card.setVisibility(View.GONE);
                                            myCoursesList.clear();
                                            myCoursesIdList.clear();
                                            for (int i = 0; i < mycourses.length(); i++) {
                                                ItemMyCourse itemMyCourse = new ItemMyCourse();
                                                JSONObject courseObj = mycourses.getJSONObject(i);
                                                itemMyCourse.setId(courseObj.getString("id"));
                                                Log.e("Id", courseObj.getString("id"));
                                                if (courseObj.getString("id").equals("8") || courseObj.getString("id").equals("15")) {
                                                    Log.e("89 Id", courseObj.getString("id"));

                                                } else {
                                                    myCoursesIdList.add(courseObj.getString("id"));
                                                    Log.e("LiveClassId", courseObj.getString("id"));

                                                }
                                                itemMyCourse.setDescription(courseObj.getString("description"));
                                                itemMyCourse.setDuration(courseObj.getString("duration"));
                                                itemMyCourse.setOwner_name(courseObj.getString("owner_name"));
                                                itemMyCourse.setTitle(courseObj.getString("title"));
                                                itemMyCourse.setImage(courseObj.getString("image"));
                                                itemMyCourse.setOwner_qualification(courseObj.getString("owner_qualification"));
                                                myCoursesList.add(itemMyCourse);
//                                                myCourseListForCommunity.add(courseObj.getString("title"));
                                            }

                                        }

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

                                        //My courses-------------
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                        myCourseslistView.setLayoutManager(layoutManager);
                                        itemMyCourseAdapter = new ItemMyCourseAdapter(myCoursesList);
                                        myCourseslistView.setAdapter(itemMyCourseAdapter);

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
//                                                double acc = courseObject.getDouble("accuracy");
//                                                if (acc == 0) {
//                                                    courseItem.setAccuracy(0.1d);
//                                                    Log.e("acc", String.valueOf(0.1));
//                                                } else {
//                                                    courseItem.setAccuracy(acc);
//                                                }
                                                courseItem.setPercentageWatched(courseObject.getDouble("percentage"));
//                                                double per = courseObject.getDouble("percentage");
//                                                if (per == 0) {
//                                                    courseItem.setPercentageWatched(0.1d);
//                                                    Log.e("per", String.valueOf(0.1));
//
//                                                } else {
//                                                    courseItem.setPercentageWatched(per);
//
//                                                }
//                                                courseItem.setPercentageWatched(per);
                                                courseItem.setActualPlayDuration(courseObject.getDouble("actual_play_duration"));
                                                courseItem.setActualMilestoneDuration(courseObject.getDouble("actual_milestone_duration"));


                                                JSONArray requiredTime = courseObject.getJSONArray("actual_video_time");
                                                for (int a = 0; a < requiredTime.length(); a++) {
//
//                                                    Long secondT = requiredTime.getLong(a);
//                                                    long hoursT = (long) secondT / 3600;
//                                                    long remainderT = (long) secondT - hoursT * 3600;
//                                                    long minsT = remainderT / 60;
////                                                    yValue1.add(new Entry(a, spentTime.getInt(a)));
////                                                    actualList.add(new Entry(a,spentTime.getLong(a)));
////                                                    courseItem.setActualTimeList(actualList);
//                                                    courseItem.getActualTimeList().add(new Entry(a, minsT));

                                                    double secondT = requiredTime.getDouble(a);
                                                    int remainderT = (int) secondT % 60;
                                                    int varT = (int) secondT - remainderT;
                                                    int minsT = varT / 60;

                                                    courseItem.getActualTimeList().add(new Entry(a, minsT));

                                                }


                                                JSONArray spentTime = courseObject.getJSONArray("spent_video_time");
                                                for (int b = 0; b < spentTime.length(); b++) {

//                                                    Log.e("mint", String.valueOf(minsT));
////                                                    yValue2.add(new Entry(b,minsT));
////                                                    spentList.add(new Entry(b,requiredTime.getLong(b)));
//                                                    courseItem.getSpentTimeList().add(new Entry(b, minsT));

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


    private void popupForCompleteProfile() {
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


        title.setText("Choose your username");
        message.setText("And more details about you");
        yesBtn.setText("Yes");
        cancelBtn.setText("Not now");
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent community = new Intent(DashboardActivity.this, EditProfileActivity.class);
                community.putExtra("ForCommunity", "yes");
                community.putExtra("instituteForProfile", instituteForProfile);
                community.putExtra("fromActivity", "homePage");
                startActivity(community);
                alertDialog.dismiss();


            }
        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);

    }

    private void CreateNewGroup(String communityName, String communityId, String
            communityImage) {
        HashMap<String, Object> community = new HashMap<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FieldValue msgTime = FieldValue.serverTimestamp();
        DocumentReference documentReference = db.collection("Communities").document(communityId);


//        ItemCommunityModel community_details = new ItemCommunityModel(communityId, communityName, communityImage);
//
//        documentReference.set(community_details).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//
//
//                Log.e("community Success", "Success");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("Community Failed", e.toString());
//            }
//        });

        DocumentReference docRef = db.collection("Communities").document(communityId).collection("LastChat").document("last_chat");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {

                    } else {
//                        community.put("community_id", communityId);
//                        commun ity.put("community_name", communityName);
//                        community.put("community_image_url", communityImage);
//                        community.put("lastChat", "");
//                        community.put("lastChatTime", msgTime);
//
//                        documentReference.set(community).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//
//                                Log.e(TAG, "onSuccess: Communities Created");
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                            }
//                        });

                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
//        community.put("community_id", communityId);
//        community.put("community_name", communityName);
//        community.put("community_image_url", communityImage);
//        community.put("lastChat", "");
//        community.put("lastChatTime", msgTime);
//
//        documentReference.set(community).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//
//                Log.e(TAG, "onSuccess: Communities Created");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });

    }


    private boolean isMicrophonePresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        } else {
            return false;
        }
    }

    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
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


    class getPackagesList extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(DashboardActivity.this);
            // int versionCode = BuildConfig.VERSION_CODE;


            String param = "sid=" + sid;
            Log.e(TAG, "param: " + param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(getPackageListApi, "POST", param);
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
                            packageLayout.setVisibility(View.VISIBLE);
                            JSONArray dataArrayObj = jsonObject.getJSONArray("data");
                            packageLists.clear();
                            if (dataArrayObj.length() == 0) {
                                packageLayout.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < dataArrayObj.length(); i++) {
                                    ItemPackageList itemPackageList = new ItemPackageList();
                                    JSONObject packageJSONObject = dataArrayObj.getJSONObject(i);
                                    itemPackageList.setId(String.valueOf(packageJSONObject.getInt("id")));
                                    itemPackageList.setTitle(packageJSONObject.getString("title"));
                                    itemPackageList.setOwnerName(packageJSONObject.getString("owner_name"));
                                    itemPackageList.setImage(packageJSONObject.getString("image"));
                                    itemPackageList.setPrice(String.valueOf(packageJSONObject.getInt("price")));
                                    packageLists.add(itemPackageList);
                                }
                                Log.e("LOG", "IN IF LOOP");


                                LinearLayoutManager linearLayoutManagerPackage = new LinearLayoutManager(DashboardActivity.this,
                                        LinearLayoutManager.HORIZONTAL, false);
                                packageRecyclerView.setLayoutManager(linearLayoutManagerPackage);

                                itemPackageAdapter = new ItemPackageAdapter(packageLists, DashboardActivity.this);
                                packageRecyclerView.setAdapter(itemPackageAdapter);
                            }

                            break;
                        case "maintainance":

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