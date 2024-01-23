package com.vision_digital.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.BuildConfig;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.vision_digital.R;
import com.vision_digital.coupons.ItemCoupon;
import com.vision_digital.coupons.ItemCouponAdapter;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.chapters.ItemChapter;
import com.vision_digital.model.chapters.ItemChapterAdapter;
import com.vision_digital.model.milestone.ItemMileStone;
import com.vision_digital.model.myCourses.ItemMyCourse;
import com.vision_digital.model.subscription.ItemSubscription;
import com.vision_digital.model.subscription.ItemSubscriptionAdapter;
import com.vision_digital.profile.EditProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.vision_digital.activities.DashboardActivity.myCoursesList;

public class CourseDetailsActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback {
    private Integer ActivityRequestCode = 2;
    String courseDetailsUrl = "";
    String coursePlanUrl = "";
    public static EditText couponCode, couponCodeEdt;
    public static RecyclerView coupons_available_spinner;
    AlertDialog alertDialog;
    public static String courseName = "";
    public static String courseLogo = "";
    String fromActivity = "";

    Boolean isCouponOpen = false;


    String expiry;
    String upcoming;
    public static String coupon_code, total_price, coupon_msg, monthPrice, gst, coupon_status, coupon_code_values, sub_total_string;

    int studId = 0;
    String uid = "";
    public static String courseId = "";
    public static String subscriptionStatus = "";
    String courseDuration = "";
    String courseOwnerName = "", courseOwnerQuali = "";
    public static ArrayAdapter<Long> subscriptionAdapter;
    public static ArrayAdapter<Long> subscriptionSpinnerAdapter;
    String[] contents;

    //Payment Work-----------------------------------
    String orderID;
    String logData;
    String gateway_name = "paytm";
    String timeStamp;
    JSONObject jsonRawData = new JSONObject();
    int activeOrderCount = 0;
    public static String price = "";

    boolean isActive = true;


    //Layout-----------------------
    TextView courseNameTv, courseDescriptionTv , readMoreLessBtn;

    FloatingActionButton analyticsBtn;
    String courseDescription;
    RecyclerView chaptersListRV;
    ImageView backBtn, playBtn, pauseBtn, fullScreenbtn, shareBtn;
    ProgressDialog dialog;
    PlayerView previewVideo;
    SimpleExoPlayer player;
    TextView watchNowBtn;
    public static TextView subscribeBtn;
    ConstraintLayout subscribeBtnLayout;
    List<Long> forMonths = new ArrayList<>();
    ArrayList<String> content = new ArrayList<>();
    public static int subsPrice = 0;

    Map<Integer, Long> subscriptionMap = new HashMap<>();

    public static int subscriptionMonths = 1;


    //Milestone resources---------
    public static ArrayList<Uri> courseMilestoneList = new ArrayList<>();
    public static ArrayList<ItemMileStone> mileStonesArrayList = new ArrayList<>();

    //CourseContent-------------------
    ArrayList<ItemChapter> chaptersList = new ArrayList<>();
    ArrayList<ItemCoupon> couponList = new ArrayList<>();
    ArrayList<ItemSubscription> contentList = new ArrayList<>();
    ArrayList<Integer> months = new ArrayList<Integer>();
    ItemChapterAdapter itemChapterAdapter;
    ItemSubscriptionAdapter itemSubscriptionAdapter;
    public static ItemCouponAdapter itemCouponAdapter;
    Uri previewVideoURI;

    // Paytm------------------
    String mid = "IxDAFe91483847846332"; //Rajit

    String marchentKey = "1BGSS5qX5OuGWITA"; //Rajit


    // 10th charector is not zero
    public static int chapter_id;

    TextView couponMsg;

    // for go to community and create invitation
    TextView communityBtn;
    ConstraintLayout inviteBtnLayout;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String forTask="";
    private String packageId="";

    //get profile
    private String userNameString = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        courseId = getIntent().getStringExtra("id");
        courseLogo = getIntent().getStringExtra("image");
        fromActivity = getIntent().getStringExtra("fromActivity");
        forTask=getIntent().getStringExtra("forTask");
        forTask=getIntent().getStringExtra("packageId");

        Log.e("courseId", courseId);


        dialog = new ProgressDialog(CourseDetailsActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading");
        dialog.show();

        //Layout---------------------------

        subscribeBtn = findViewById(R.id.subscribeBtn);
        shareBtn = findViewById(R.id.sharButtonCourseDetail);
        communityBtn = findViewById(R.id.communityBtn);
        inviteBtnLayout = findViewById(R.id.inviteBtnLayout);
        analyticsBtn = findViewById(R.id.analytics_btn);

       // analyticsBtn.setVisibility(View.GONE);
        analyticsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subscriptionStatus.equals("subscribed")) {
                    Intent intent = new Intent(getApplicationContext(), AnalyticsActivity.class);
                    intent.putExtra("id", courseId);
                    startActivity(intent);
                } else {
                    Toast.makeText(CourseDetailsActivity.this, "You have to subscribe this course!..", Toast.LENGTH_SHORT).show();
                }

            }
        });

        readMoreLessBtn=findViewById(R.id.readMoreLess);
        readMoreLessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (readMoreLessBtn.getText().equals("...Read More")){
                    courseDescriptionTv.setMaxLines(200);
                    readMoreLessBtn.setText("Show Less");
                }else {
                    courseDescriptionTv.setMaxLines(3);
                    readMoreLessBtn.setText("...Read More");
                }
            }
        });



        communityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!userNameString.equals("")) {

                } else {
                    popupForCompleteProfile();
                }


            }
        });


        inviteBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!userNameString.equals("")){
//                    if (subscriptionStatus.equals("subscribed")){
//                        Intent invitationIntent = new Intent(CourseDetailsActivity.this, SelectMilestoneInvitationActivity.class);
//                        invitationIntent.putExtra("community_id", courseId);
//                        invitationIntent.putExtra("community_name", courseName);
//                        invitationIntent.putExtra("community_logo", courseLogo);
//                        invitationIntent.putExtra("fromActivity","courseDetails");
//                        invitationIntent.putExtra("userNameString",userNameString);
//                        startActivity(invitationIntent);
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                    }else{
//                        Toast.makeText(CourseDetailsActivity.this, "Subscribe this course for the sending invitation! ", Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    popupForCompleteProfile();
//                }
                Intent intent = new Intent(getApplicationContext(), AnalyticsActivity.class);
                intent.putExtra("course_id", courseId);
                startActivity(intent);

            }
        });








        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                coupon_code = "";
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CourseDetailsActivity.this);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = CourseDetailsActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.subscription_popup, null);
                dialogBuilder.setView(dialogView);


                //Alert Dialog Layout work
                alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final Spinner subscriptionsMonthsSpinner = dialogView.findViewById(R.id.subscriptionsMonthsSpinner);
                coupons_available_spinner = dialogView.findViewById(R.id.coupons_available_spinner);

                final EditText edtMonth = dialogView.findViewById(R.id.edtMonth);
                couponCode = dialogView.findViewById(R.id.couponCode);
                couponCodeEdt = dialogView.findViewById(R.id.couponCodeEdt);
                couponMsg = dialogView.findViewById(R.id.couponMsg);
                coupon_msg = "";


//                coupon_code = couponCode.getText().toString().trim();
                edtMonth.setFocusable(false);


                LinearLayoutManager layoutManagerCoupon = new LinearLayoutManager(CourseDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                coupons_available_spinner.setLayoutManager(layoutManagerCoupon);
                itemCouponAdapter = new ItemCouponAdapter(couponList);
                coupons_available_spinner.setAdapter(itemCouponAdapter);
//                itemCouponAdapter.notifyDataSetChanged();


                Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
                subscriptionsMonthsSpinner.setAdapter(subscriptionAdapter);
                subscriptionAdapter.notifyDataSetChanged();


                edtMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subscriptionsMonthsSpinner.setVisibility(View.VISIBLE);
                        subscriptionsMonthsSpinner.performClick();

                        coupon_code = couponCode.getText().toString();

                    }
                });


                subscriptionsMonthsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        new GetSubscriptionCourseDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                        price = "0";
                        for (int j = i; j >= 0; j--) {

                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            subscriptionMonths = Math.toIntExact(forMonths.get(i));
                        }
                        price = String.valueOf(subscriptionMap.get(subscriptionMonths));


                        if (subscriptionMonths == 1) {
                            edtMonth.setText(subscriptionMonths + " Month Subscription");


                        } else {
                            edtMonth.setText(subscriptionMonths + " Months Subscription");

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                couponCodeEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        couponCode.setText(couponCodeEdt.getText().toString().trim());
                        coupon_code = couponCodeEdt.getText().toString().trim();
                        Log.e("couponCodeEdt", couponCodeEdt.getText().toString());

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        coupon_code = couponCodeEdt.getText().toString().trim();
                        Log.e("couponCodeEdt2", couponCodeEdt.getText().toString());

                    }
                });
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtMonth.getText().toString().trim().equals("")) {
                            Toast.makeText(getApplicationContext(), "Please select Month!..", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.show();
                            new GetSubscriptionCourseDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            dialog.dismiss();


                            Log.e("action", "going to payment");


                        }


                    }
                });

                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);

            }

        });


        subscribeBtnLayout = findViewById(R.id.subscribeBtnLayout);

        watchNowBtn = findViewById(R.id.watchNowBtn);
        watchNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchNowBtn.setEnabled(false);
                String txtAction = watchNowBtn.getText().toString().trim();
                if (txtAction.equals("Watch Now")) {

                    SharedPreferences courseDetails = getSharedPreferences("courseId_" + CourseDetailsActivity.courseId, MODE_PRIVATE);

                    Intent mileStonePlayer = new Intent(CourseDetailsActivity.this, MileStoneVideoPlayerActivity.class);
                    mileStonePlayer.putExtra("videoPosition", courseDetails.getInt("videoPosition", 0));
                    mileStonePlayer.putExtra("id", courseId);
                    mileStonePlayer.putExtra("mileStonesList", CourseDetailsActivity.mileStonesArrayList);
                    mileStonePlayer.putExtra("chapter_id", chapter_id);
                    mileStonePlayer.putExtra("name", CourseDetailsActivity.courseName);
                    mileStonePlayer.putExtra("logo", CourseDetailsActivity.courseLogo);
                    mileStonePlayer.putExtra("subscriptionStatus", subscriptionStatus);
                    mileStonePlayer.putExtra("mileUpdateType", "chap_mile_click");

                    startActivity(mileStonePlayer);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    watchNowBtn.setEnabled(true);

                    Log.e("resuming", "resuming");
                }

            }
        });

        courseNameTv = findViewById(R.id.courseName);
        courseDescriptionTv = findViewById(R.id.course_des);
        chaptersListRV = findViewById(R.id.chaptersList);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        previewVideo = findViewById(R.id.previewVideoView);

        fullScreenbtn = findViewById(R.id.fullScreenBtn);


        playBtn = findViewById(R.id.playBtn);
        pauseBtn = findViewById(R.id.pauseBtn);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playBtn.setVisibility(View.GONE);
                player.play();
                pauseBtn.setVisibility(View.VISIBLE);
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseBtn.setVisibility(View.GONE);
                player.pause();
                playBtn.setVisibility(View.VISIBLE);
            }
        });

        fullScreenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fullScreenIntent = new Intent(CourseDetailsActivity.this, CoursePreviewVideoPlayerActivity.class);
                startActivity(fullScreenIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        courseDetailsUrl = getApplicationContext().getString(R.string.apiURL) + "getCourseDetails";
        coursePlanUrl = getApplicationContext().getString(R.string.apiURL) + "availableChapterInMonth";

        SharedPreferences uidDetails = getSharedPreferences("CNBUID", MODE_PRIVATE);
        uid = uidDetails.getString("uid", "NO_NAME");

        //Retriving user details stored in Local---------------------------------------
        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        studId = studDetails.getInt("sid", 0);
        //HardCode----
        //Catching Course Details from previous activity

        if (courseId == null) {
            Uri data = getIntent().getData();
            List<String> param = data.getPathSegments();
            String first = param.get(0);
            courseId = first;
        }

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Click to learn: " + courseName + " \n" +

                        "\nhttps://dlink.chalksnboard.com/course" + courseId;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Zeal");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
//Hardcode---


        //Paytm Payment-----------------------------------------
        String alpha = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        Long tsLong = System.currentTimeMillis() / 1000;
        timeStamp = tsLong.toString();
//        orderID = alpha.charAt(random.nextInt(26)) + uid + alpha.charAt(random.nextInt(26)) + timeStamp;
        orderID = "OD" + studId + "S" + timeStamp;

//        //Marchent check--------------------
        DocumentReference flagValues = FirebaseFirestore.getInstance().collection("FlagValues").document("7Mf6LOHmxLmhLrbBpB8E");

        flagValues.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        int flag = Integer.parseInt(String.valueOf(document.get("flagValue")));
                        if (flag == 1) {
                            mid = "TLvNGP42879274127615";
                            marchentKey = "G&%UEPNTnz6&4cti";
                            Log.e("Details", "Fetched");
                        }
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());

                }
            }
        });
//        subscriptionAdapter = new ArrayAdapter<Long>(CourseDetailsActivity.this,
//                android.R.layout.simple_spinner_item, forMonths);
//        subscriptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        new GetCourseDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if (forTask.equals("subscription")) {

            popupForSubscribeButton();
        }

    }
    public void popupForSubscribeButton(){


        coupon_code = "";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CourseDetailsActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = CourseDetailsActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.subscription_popup, null);
        dialogBuilder.setView(dialogView);


        //Alert Dialog Layout work
       alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

         Spinner subscriptionsMonthsSpinner = dialogView.findViewById(R.id.subscriptionsMonthsSpinner);
       RecyclerView coupons_available_spinner = dialogView.findViewById(R.id.coupons_available_spinner);

         EditText edtMonth = dialogView.findViewById(R.id.edtMonth);
      EditText  couponCode = dialogView.findViewById(R.id.couponCode);
      EditText  couponCodeEdt = dialogView.findViewById(R.id.couponCodeEdt);
      TextView  couponMsg = dialogView.findViewById(R.id.couponMsg);
        coupon_msg = "";


//                coupon_code = couponCode.getText().toString().trim();
        edtMonth.setFocusable(false);


        LinearLayoutManager layoutManagerCoupon = new LinearLayoutManager(CourseDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        coupons_available_spinner.setLayoutManager(layoutManagerCoupon);
        itemCouponAdapter = new ItemCouponAdapter(couponList);
        coupons_available_spinner.setAdapter(itemCouponAdapter);
//                itemCouponAdapter.notifyDataSetChanged();


        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        subscriptionsMonthsSpinner.setAdapter(subscriptionAdapter);


        edtMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscriptionsMonthsSpinner.setVisibility(View.VISIBLE);
                subscriptionsMonthsSpinner.performClick();

                coupon_code = couponCode.getText().toString();

            }
        });

        subscriptionsMonthsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        new GetSubscriptionCourseDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                price = "0";
                for (int j = i; j >= 0; j--) {

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    subscriptionMonths = Math.toIntExact(forMonths.get(i));
                }
                price = String.valueOf(subscriptionMap.get(subscriptionMonths));


                if (subscriptionMonths == 1) {
                    edtMonth.setText(subscriptionMonths + " Month Subscription");


                } else {
                    edtMonth.setText(subscriptionMonths + " Months Subscription");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        couponCodeEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                couponCode.setText(couponCodeEdt.getText().toString().trim());
                coupon_code = couponCodeEdt.getText().toString().trim();
                Log.e("couponCodeEdt", couponCodeEdt.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

                coupon_code = couponCodeEdt.getText().toString().trim();
                Log.e("couponCodeEdt2", couponCodeEdt.getText().toString());

            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtMonth.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please select Month!..", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    alertDialog.dismiss();
                    new GetSubscriptionCourseDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    dialog.dismiss();
                   // alertDialog.dismiss();



                    Log.e("action", "going to payment");

                }


            }
        });

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);




    }







    private void popupForCompleteProfile() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CourseDetailsActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = CourseDetailsActivity.this.getLayoutInflater();
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
                Intent community = new Intent(CourseDetailsActivity.this, EditProfileActivity.class);
                community.putExtra("ForCommunity", "yes");
                startActivity(community);
                alertDialog.dismiss();


            }
        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);

    }


    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network gone", Toast.LENGTH_SHORT).show();
//        payViaPaytmBtn.setEnabled(true);
        watchNowBtn.setEnabled(true);
    }

    @Override
    public void onErrorProceed(String s) {
        watchNowBtn.setEnabled(true);

    }

    @Override
    public void clientAuthenticationFailed(String s) {

    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  " + s);
//        payViaPaytmBtn.setEnabled(true);
        watchNowBtn.setEnabled(true);
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true " + s + "  s1 " + s1);
//        payViaPaytmBtn.setEnabled(true);
        watchNowBtn.setEnabled(true);
    }

    @Override
    public void onBackPressedCancelTransaction() {

        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        watchNowBtn.setEnabled(true);
        new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        watchNowBtn.setEnabled(true);
        //Put RawData-----------------------------------
        new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onTransactionResponse(@Nullable Bundle bundle) {


//        Log.e("checksum ", " response true " + bundle.toString());
        String txn_status = bundle.getString("STATUS");


        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                // json.put(key, bundle.get(key)); see edit below
                jsonRawData.put(key, JSONObject.wrap(bundle.get(key)));
            } catch (JSONException e) {
                //Handle exception here
            }
        }
        Log.e("json", jsonRawData.toString());
        //Put RawData-----------------------------------

        if (txn_status.equals("TXN_SUCCESS")) {

            new MakeCourseSubscription().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }


//        payViaPaytmBtn.setEnabled(true);
        watchNowBtn.setEnabled(true);
    }


    class SendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(CourseDetailsActivity.this);
        //private String orderId , mid, custid, amt;
        String host = "https://securegw.paytm.in/";


        String callBackUrl = host + "theia/paytmCallback?ORDER_ID=" + orderID;

        String url = getApplicationContext().getString(R.string.apiURL) + "generateCheckSum";
        String varifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        //"https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+orderId;
        //https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp
        // "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;
        String CHECKSUMHASH = "";

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            JSONParser jsonParser = new JSONParser(CourseDetailsActivity.this);
            String newMKey = null;
//            if(marchentKey.equals("G&%UEPNTnz6&4cti")){
//                newMKey = "G+%UEPNTnz6+4cti";
//            }else{
//                newMKey = marchentKey;
//            }

            try {
                newMKey = URLEncoder.encode(marchentKey, String.valueOf(StandardCharsets.UTF_8));
                Log.e("New Key", newMKey);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String param =
                    "MID=" + mid +
                            "&MERCHANT_KEY=" + newMKey +
                            "&ORDER_ID=" + orderID +
                            "&CUST_ID=" + uid +
                            "&CHANNEL_ID=WAP&TXN_AMOUNT=" + Double.parseDouble(total_price) + "&WEBSITE=DEFAULT" +
                            "&CALLBACK_URL=" + callBackUrl + "&INDUSTRY_TYPE_ID=Retail";
            Log.e("params", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
            // yaha per checksum ke saht order id or status receive hoga..
            Log.e("CheckSum result >>", jsonObject.toString());
            if (jsonObject != null) {
                Log.e("CheckSum result >>", jsonObject.toString());
                try {
                    JSONObject dataObj = jsonObject.getJSONObject("data");
                    CHECKSUMHASH = dataObj.has("CHECKSUMHASH") ? dataObj.getString("CHECKSUMHASH") : "";
                    Log.e("CheckSum result >>", CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ", "  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            //PaytmPGService Service = PaytmPGService.getStagingService("");
            // when app is ready to publish use production service
            PaytmPGService Service = PaytmPGService.getProductionService();
            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("MERCHANT_KEY", marchentKey);
            paramMap.put("ORDER_ID", orderID);
            paramMap.put("CUST_ID", uid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", "" + Double.parseDouble(total_price));
            paramMap.put("WEBSITE", "DEFAULT");
            paramMap.put("CALLBACK_URL", callBackUrl);
            //paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
            // paramMap.put( "MOBILE_NO" , "9144040888");  // no need
            paramMap.put("CHECKSUMHASH", CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param " + paramMap.toString());
            Service.initialize(Order, null);
            // start payment service call here
            Service.startPaymentTransaction(CourseDetailsActivity.this, true, true,
                    CourseDetailsActivity.this);


        }
    }


     class GetCourseDetails extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
            //            this.dialog.setMessage("Please wait");
            //            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(CourseDetailsActivity.this);
//            int versionCode = com.chalksnboard.BuildConfig.VERSION_CODE;

            int versionCode;


            PackageInfo pInfo = null;
            try {
                pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            versionCode = pInfo.versionCode;
            Log.e("versionCode", String.valueOf(versionCode));


            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId +"&package_id="+packageId;

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

                    dialog.show();
                    String available_subscription_months, subscribed_months;
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
                                    subscribeBtnLayout.setVisibility(View.GONE);
                                } else {
                                    subscribeBtnLayout.setVisibility(View.VISIBLE);
                                    JSONArray subscriptionJsonArray = dataObj.getJSONArray("available_subscriptions");
                                    forMonths.clear();
//                                    for (int i = 0; i < subscriptionJsonArray.length(); i++) {
//                                        JSONObject subscriptionMonth = subscriptionJsonArray.getJSONObject(i);
//                                        forMonths.add(Long.valueOf(i));
//                                        subscriptionMap.put(i, Long.valueOf(subscriptionMonth.getString("" + i)));
//                                    }
                                    int a = 1;
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
                                    subscriptionAdapter = new ArrayAdapter<Long>(CourseDetailsActivity.this,
                                            android.R.layout.simple_spinner_item, forMonths);
                                    subscriptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    subscriptionAdapter.notifyDataSetChanged();

                                    subscriptionSpinnerAdapter = new ArrayAdapter<Long>(CourseDetailsActivity.this,
                                            android.R.layout.simple_spinner_item, forMonths);
                                    subscriptionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                }
                                watchNowBtn.setVisibility(View.VISIBLE);
                                watchNowBtn.setText("Watch Now");
                                subscribeBtn.setText("Renew course");
                                courseNameTv.setText(courseName);
                                courseDescription = courseDesc;
                                courseDescriptionTv.setText(courseDesc);
                                previewVideoURI = Uri.parse(dataObj.getString("promo_video_link"));
                                MediaItem mediaItem = MediaItem.fromUri(previewVideoURI);
                                player = new SimpleExoPlayer.Builder(CourseDetailsActivity.this).build();
                                previewVideo.setPlayer(player);
                                player.setMediaItem(mediaItem);
                                player.prepare();
//                                player.play();
//                                pauseBtn.setVisibility(View.VISIBLE);

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
                                        mileStone.setActivityType("courseDetails");
                                        mileStone.setChapterId(Integer.parseInt(chapterObj.getString("id")));
                                        mileStone.setMilestoneType("chap_mile_click");
                                        mileStone.setSelected(false);


//                                        String video = mileStoneObj.getString("video_link");
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
                                        mileStone.setActivityType("courseDetails");
                                        mileStone.setSelected(false);
                                        mileStone.setMilestoneType("chap_mile_click");
                                        mileStone.setChapterId(chapter_id);
                                        mileStone.setDuration("0");
//

//                                        mileStone.setVideoPosition(positionNumber++);
                                        if (!noteStoneObject.getString("pdf_link").equals("")) {
                                            courseMilestoneList.add(Uri.parse(noteStoneObject.getString("pdf_link")));
                                            mileStone.setVideoUrl(noteStoneObject.getString("pdf_link"));

                                        }

                                        chapter.mileStonesList.add(mileStone);
                                        Log.e("milestoneList", String.valueOf(mileStone));
                                        chapter.setItemMileStone(mileStone);
//
                                    }
                                    chaptersList.add(chapter);
                                    months.add(subMonths);
                                }


                                LinearLayoutManager layoutManager = new LinearLayoutManager(CourseDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                                chaptersListRV.setLayoutManager(layoutManager);
                                itemChapterAdapter = new ItemChapterAdapter(chaptersList);
                                chaptersListRV.setAdapter(itemChapterAdapter);

                                JSONArray couponContent = dataObj.getJSONArray("available_coupons");
                                couponList.clear();
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
                                watchNowBtn.setVisibility(View.GONE);
                                int totalMonths = dataObj.getInt("available_subscription_months");
                                int currentSubsMonths = dataObj.getInt("subscribed_months");
                                if (totalMonths == currentSubsMonths) {
                                    subscribeBtnLayout.setVisibility(View.GONE);
                                } else {
                                    subscribeBtnLayout.setVisibility(View.VISIBLE);
                                    JSONArray subscriptionJsonArray = dataObj.getJSONArray("available_subscriptions");
                                    forMonths.clear();
//                                    for (int i = 0; i < subscriptionJsonArray.length(); i++) {
//                                        JSONObject subscriptionMonth = subscriptionJsonArray.getJSONObject(i);
//                                        Log.e("1", String.valueOf(i));
//                                        forMonths.add(Long.valueOf(i + 1));
//////                                        subscriptionMap.put(i + 1, Long.valueOf(subscriptionMonth.getString("" + (i + 1))));
////                                        subsPrice = Integer.parseInt(subscriptionMonth.getString(String.valueOf((i + 1))));
////                                        subsPrice = Integer.parseInt(subscriptionMonth.getString(subscriptionJsonArray.getString(i)));
//                                        String value = subscriptionJsonArray.getString(i);
//                                        Log.e("value", value);
//
//                                    }
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
                                    subscriptionAdapter = new ArrayAdapter<Long>(CourseDetailsActivity.this,
                                            android.R.layout.simple_spinner_item, forMonths);
                                    subscriptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                }

                                price = dataObj.getString("rate");
                                courseNameTv.setText(courseName);
                                courseDescriptionTv.setText(courseDesc);
                                previewVideoURI = Uri.parse(dataObj.getString("promo_video_link"));
                                MediaItem mediaItem = MediaItem.fromUri(previewVideoURI);
                                player = new SimpleExoPlayer.Builder(CourseDetailsActivity.this).build();
                                previewVideo.setPlayer(player);
                                player.setMediaItem(mediaItem);
                                player.prepare();
//                                player.play();
//                                pauseBtn.setVisibility(View.VISIBLE);

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
                                        mileStone.setActivityType("courseDetails");
                                        mileStone.setMilestoneType("chap_mile_click");
                                        mileStone.setChapterId(Integer.parseInt(chapterObj.getString("id")));
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
                                        mileStone.setActivityType("courseDetails");
                                        mileStone.setMilestoneType("chap_mile_click");
                                        mileStone.setChapterId(chapter_id);
                                        mileStone.setSelected(false);

//                                        mileStone.setVideoPosition(positionNumber++);
                                        if (!noteStoneObject.getString("pdf_link").equals("")) {
                                            courseMilestoneList.add(Uri.parse(noteStoneObject.getString("pdf_link")));
                                        }

                                        chapter.mileStonesList.add(mileStone);
                                        chapter.setItemMileStone(mileStone);
//
                                    }
                                    chaptersList.add(chapter);
                                }

                                LinearLayoutManager layoutManager = new LinearLayoutManager(CourseDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                                chaptersListRV.setLayoutManager(layoutManager);
                                itemChapterAdapter = new ItemChapterAdapter(chaptersList);
                                chaptersListRV.setAdapter(itemChapterAdapter);

                                JSONArray couponContent = dataObj.getJSONArray("available_coupons");
                                couponList.clear();
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
                                    couponList.add(itemCoupon);
                                    Log.e("couonList", couponObject.getString("code"));

                                }
                            }
                            dialog.dismiss();

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CourseDetailsActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = CourseDetailsActivity.this.getLayoutInflater();
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
                            dialog.dismiss();
                            break;
                        case "failure":
                            Toast.makeText(CourseDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            break;

                        default:
                            Toast.makeText(CourseDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("TAG", " result code " + resultCode);
        // -1 means successful  // 0 means failed
        // one error is - nativeSdkForMerchantMessage : networkError
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Log.e("TAG", key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                }
            }
            Log.e("TAG", " data " + data.getStringExtra("nativeSdkForMerchantMessage"));
            Log.e("TAG", " data response - " + data.getStringExtra("response"));
/*
 data response - {"BANKNAME":"WALLET","BANKTXNID":"1395841115",
 "CHECKSUMHASH":"7jRCFIk6eRmrep+IhnmQrlrL43KSCSXrmMP5pH0hekXaaxjt3MEgd1N9mLtWyu4VwpWexHOILCTAhybOo5EVDmAEV33rg2VAS/p0PXdk\u003d",
 "CURRENCY":"INR","GATEWAYNAME":"WALLET","MID":"EAcR4116","ORDERID":"100620202152",
 "PAYMENTMODE":"PPI","RESPCODE":"01","RESPMSG":"Txn Success","STATUS":"TXN_SUCCESS",
 "TXNAMOUNT":"2.00","TXNDATE":"2020-06-10 16:57:45.0","TXNID":"202006101112128001101683631290118"}
  */
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage")
                    + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        } else {
            Log.e("TAG", " payment failed");
        }
    }

    public class GetSubscriptionCourseDetails extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(CourseDetailsActivity.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(CourseDetailsActivity.this);
            int versionCode = BuildConfig.VERSION_CODE;

//            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
            String param = "sid=" + studId + "&course_id=" + courseId + "&subscription_month=" + subscriptionMonths + "&coupon_code=" + coupon_code + "&selected_price=" + price;


            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(coursePlanUrl, "POST", param);
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

                            int positionNumberS = 0;
                            contentList.clear();
                            JSONArray courseContent = dataObj.getJSONArray("chapters");
                            for (int i = 0; i < courseContent.length(); i++) {
//                                JSONObject contentSubscribe = courseContent.getJSONObject(i);
                                ItemSubscription contents = new ItemSubscription();
                                contents.setTitle(courseContent.getString(i));

                                content.add(courseContent.getString(i));
                                contentList.add(contents);
                                contents.setNumber(i + 1);

                            }

                            JSONObject couponDetails = dataObj.getJSONObject("coupon");
                            total_price = couponDetails.getString("total_amount");
                            coupon_msg = couponDetails.getString("msg");
                            monthPrice = couponDetails.getString("sub_total");
                            gst = couponDetails.getString("gst_amount");
                            coupon_status = couponDetails.getString("status");
                            coupon_code_values = couponDetails.getString("coupon_code_value");


                            Log.e("Content", contentList.toString());

                            String expiryTime = dataObj.getString("expiry");
                            upcoming = dataObj.getString("upcoming");
//                             Log.e("upcoming",upcoming);
//                             Log.e("expiry",expiry);

                            SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat outputFormatter = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa");
//
                            try {
                                Date startTimeFormate = inputFormatter.parse(expiryTime);
                                expiry = outputFormatter.format(startTimeFormate);


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            this.dialog.dismiss();

                            if (coupon_status.equals("failure")) {
                                couponMsg.setVisibility(View.VISIBLE);
                                if (!coupon_msg.equals("")) {
                                    couponMsg.setText(coupon_msg + "..");
                                    couponMsg.setTextColor(Color.parseColor("#D30404"));
                                }


                            } else {
                                alertDialog.dismiss();
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CourseDetailsActivity.this);
                                // ...Irrelevant code for customizing the buttons and title
                                LayoutInflater inflater = CourseDetailsActivity.this.getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.activity_subscription_plan, null);
                                dialogBuilder.setView(dialogView);


                                //Alert Dialog Layout work
                                final AlertDialog alertDialogTwo = dialogBuilder.create();
                                alertDialogTwo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                final TextView totalPrice = dialogView.findViewById(R.id.totalPrice);
                                final TextView note = dialogView.findViewById(R.id.note);
                                final TextView msg = dialogView.findViewById(R.id.msg);
                                final TextView coupon_price = dialogView.findViewById(R.id.coupon_price);
                                final TextView expiryDateTxt = dialogView.findViewById(R.id.expiryDate);
                                final TextView month_price = dialogView.findViewById(R.id.month_price);
                                final TextView total_gst = dialogView.findViewById(R.id.total_gst);
                                final TextView sub_total = dialogView.findViewById(R.id.sub_total);
                                final LinearLayout coupon_value_layout_linear = dialogView.findViewById(R.id.coupon_value_layout_linear);
                                final LinearLayout sub_total_linear = dialogView.findViewById(R.id.sub_total_linear);


                                RecyclerView chaptersListR = dialogView.findViewById(R.id.chaptersList);
                                final CheckBox tAndCondition = dialogView.findViewById(R.id.tAndCondition);
                                final Button proceedToPay = dialogView.findViewById(R.id.proceedToPay);


                                note.setText(upcoming);
                                totalPrice.setText("Rs." + total_price + "/-");
                                expiryDateTxt.setText(expiry);

                                if (total_price.equals("0")) {
                                    proceedToPay.setText("Redeem now");
                                } else {
                                    proceedToPay.setText("Proceed to pay");
                                }

                                if (coupon_msg.equals("")) {
                                    msg.setVisibility(View.GONE);
                                } else {
                                    msg.setVisibility(View.VISIBLE);
                                    msg.setText(coupon_msg + "..");
                                }

                                month_price.setText("Rs." + price + "/-");
                                total_gst.setText("Rs." + gst + "/-");
                                sub_total.setText(sub_total_string);
                                if (!coupon_code_values.equals("0")) {
                                    coupon_value_layout_linear.setVisibility(View.VISIBLE);
                                    coupon_price.setText("Rs." + coupon_code_values + "/-");
                                    sub_total_linear.setVisibility(View.VISIBLE);
                                    sub_total.setText("Rs." + monthPrice + "/-");

                                } else {
                                    coupon_value_layout_linear.setVisibility(View.GONE);
                                    sub_total_linear.setVisibility(View.GONE);
                                }


                                LinearLayoutManager layoutManager = new LinearLayoutManager(CourseDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                                chaptersListR.setLayoutManager(layoutManager);
                                itemSubscriptionAdapter = new ItemSubscriptionAdapter(contentList);
                                chaptersListR.setAdapter(itemSubscriptionAdapter);


                                tAndCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (tAndCondition.isChecked()) {
                                            proceedToPay.setEnabled(true);
                                        } else {
                                            proceedToPay.setEnabled(false);
                                        }
                                    }
                                });

                                proceedToPay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (total_price.equals("0")) {
                                            new MakeCourseSubscription().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        } else {
                                            new SendUserDetailTOServerdd().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        }

                                        alertDialogTwo.dismiss();
                                    }
                                });

                                alertDialogTwo.show();
                                alertDialogTwo.setCanceledOnTouchOutside(false);

                            }
//


                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CourseDetailsActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = CourseDetailsActivity.this.getLayoutInflater();
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
                            Toast.makeText(CourseDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(CourseDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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



//    public class GetSubscriptionCourseCouponDetails extends AsyncTask<String, Void, String> {
//
//        private ProgressDialog dialog = new ProgressDialog(CourseDetailsActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            JSONParser jsonParser = new JSONParser(CourseDetailsActivity.this);
//            int versionCode = BuildConfig.VERSION_CODE;
//
////            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
//            String param = "sid=" + studId + "&course_id=" + courseId + "&subscription_month=" + subscriptionMonths + "&coupon_code=" + coupon_code + "&selected_price=" + price;
//
//
//            Log.e("param", param);
//
//            String courseCouponUrl =getApplicationContext().getString(R.string.apiURL)+"apply_coupon_code_check";
//
//            JSONObject jsonObject = jsonParser.makeHttpRequest(courseCouponUrl, "POST", param);
//            if (jsonObject != null) {
//                return jsonObject.toString();
//            } else {
//
//            }
//            return "";
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            Log.i("json", s);
//
//
//            if (!s.equals("")) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(s);
//
//                    Log.e("Result : ", s);
//
//                    //Do work-----------------------------
//                    String status = jsonObject.getString("status");
//
//
//                    switch (status) {
//                        case "success":
//                            //Running Fine
//
//                            final JSONObject dataObj = jsonObject.getJSONObject("data");
//                            int positionNumberS = 0;
//
//
//                            //JSONObject couponDetails = dataObj.getJSONObject("coupon");
//                            total_price = dataObj.getString("total_amount");
//                            coupon_msg = dataObj.getString("message");
//                            monthPrice = dataObj.getString("sub_total");
//                            gst = dataObj.getString("gst_amount");
//                           // coupon_status = dataObj.getString("status");
//                            coupon_status = status;
//                            coupon_code_values = dataObj.getString("coupon_code_value");
//
//
//                            Log.e("Content", contentList.toString());
//
//                            this.dialog.dismiss();
//
//                            if (coupon_status.equals("failure")) {
//                                couponMsg.setVisibility(View.VISIBLE);
//                                if (!coupon_msg.equals("")) {
//                                    couponMsg.setText(coupon_msg + "..");
//                                    couponMsg.setTextColor(Color.parseColor("#D30404"));
//                                }
//
//
//                            } else {
//                                alertDialog.dismiss();
//                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CourseDetailsActivity.this);
//                                // ...Irrelevant code for customizing the buttons and title
//                                LayoutInflater inflater = CourseDetailsActivity.this.getLayoutInflater();
//                                View dialogView = inflater.inflate(R.layout.activity_subscription_plan, null);
//                                dialogBuilder.setView(dialogView);
//
//
//                                //Alert Dialog Layout work
//                                final AlertDialog alertDialogTwo = dialogBuilder.create();
//                                alertDialogTwo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                                final TextView totalPrice = dialogView.findViewById(R.id.totalPrice);
//                                final TextView note = dialogView.findViewById(R.id.note);
//                                final TextView msg = dialogView.findViewById(R.id.msg);
//                                final TextView coupon_price = dialogView.findViewById(R.id.coupon_price);
//                                final TextView expiryDateTxt = dialogView.findViewById(R.id.expiryDate);
//                                final TextView month_price = dialogView.findViewById(R.id.month_price);
//                                final TextView total_gst = dialogView.findViewById(R.id.total_gst);
//                                final TextView sub_total = dialogView.findViewById(R.id.sub_total);
//                                final LinearLayout coupon_value_layout_linear = dialogView.findViewById(R.id.coupon_value_layout_linear);
//                                final LinearLayout sub_total_linear = dialogView.findViewById(R.id.sub_total_linear);
//
//
//                                RecyclerView chaptersListR = dialogView.findViewById(R.id.chaptersList);
//                                final CheckBox tAndCondition = dialogView.findViewById(R.id.tAndCondition);
//                                final Button proceedToPay = dialogView.findViewById(R.id.proceedToPay);
//
//
//                                note.setText(upcoming);
//                                totalPrice.setText("Rs." + total_price + "/-");
//                                expiryDateTxt.setText(expiry);
//
//                                if (total_price.equals("0")) {
//                                    proceedToPay.setText("Redeem now");
//                                } else {
//                                    proceedToPay.setText("Proceed to pay");
//                                }
//
//                                if (coupon_msg.equals("")) {
//                                    msg.setVisibility(View.GONE);
//                                } else {
//                                    msg.setVisibility(View.VISIBLE);
//                                    msg.setText(coupon_msg + "..");
//                                }
//
//                                month_price.setText("Rs." + price + "/-");
//                                total_gst.setText("Rs." + gst + "/-");
//                                sub_total.setText(sub_total_string);
//                                if (!coupon_code_values.equals("0")) {
//                                    coupon_value_layout_linear.setVisibility(View.VISIBLE);
//                                    coupon_price.setText("Rs." + coupon_code_values + "/-");
//                                    sub_total_linear.setVisibility(View.VISIBLE);
//                                    sub_total.setText("Rs." + monthPrice + "/-");
//
//                                } else {
//                                    coupon_value_layout_linear.setVisibility(View.GONE);
//                                    sub_total_linear.setVisibility(View.GONE);
//                                }
//
//
//                                LinearLayoutManager layoutManager = new LinearLayoutManager(CourseDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
//                                chaptersListR.setLayoutManager(layoutManager);
//                                itemSubscriptionAdapter = new ItemSubscriptionAdapter(contentList);
//                                chaptersListR.setAdapter(itemSubscriptionAdapter);
//
//
//                                tAndCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                    @Override
//                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                        if (tAndCondition.isChecked()) {
//                                            proceedToPay.setEnabled(true);
//                                        } else {
//                                            proceedToPay.setEnabled(false);
//                                        }
//                                    }
//                                });
//
//                                proceedToPay.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        if (total_price.equals("0")) {
//                                            new MakeCourseSubscription().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                        } else {
//                                            new SendUserDetailTOServerdd().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                        }
//
//                                        alertDialogTwo.dismiss();
//                                    }
//                                });
//
//                                alertDialogTwo.show();
//                                alertDialogTwo.setCanceledOnTouchOutside(false);
//
//                            }
////
//
//
//                            break;
//                        case "maintenance":
//                            //Undermaintance
//                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CourseDetailsActivity.this);
//                            // ...Irrelevant code for customizing the buttons and title
//                            LayoutInflater inflater = CourseDetailsActivity.this.getLayoutInflater();
//                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
//                            dialogBuilder.setView(dialogView);
//                            //Alert Dialog Layout work
//                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
//                            //String msgContent = dataObj.getString("message");
//                            //maintainanceContent.setText(Html.fromHtml(msgContent));
//
//                            TextView btnOK = dialogView.findViewById(R.id.btnOK);
//                            btnOK.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    finishAndRemoveTask();
//                                }
//                            });
//                            AlertDialog alertDialog = dialogBuilder.create();
//                            alertDialog.show();
//                            alertDialog.setCanceledOnTouchOutside(false);
//                            break;
//                        case "failure":
//                            Log.e("IN FAILURE",status);
//                            String message = jsonObject.getString("message");
//                            coupon_status=status;
//                            coupon_msg=message;
//                                couponMsg.setVisibility(View.VISIBLE);
//                                if (!coupon_msg.equals("")) {
//                                    couponMsg.setText(coupon_msg + "..");
//                                    couponMsg.setTextColor(Color.parseColor("#D30404"));
//
//                                }
//                                break;
//                        default:
//                            Toast.makeText(CourseDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            //Response reached---------------value in 's' variable
//            dialog.dismiss();
//        }
//    }




    class LogRawData extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);
        String url = getApplicationContext().getString(R.string.apiURL) + "logTransactions";


        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(CourseDetailsActivity.this);
            String param = "student_id=" + studId + "&course_id=" + courseId + "&uid=" + uid + "&gateway_response=" + jsonRawData + "&gateway_name=" + gateway_name + "&coupon_value=" + coupon_code_values + "&coupon_code=" + coupon_code;
            Log.e("logParam", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
//            sid=41&course_id=8&subscription_month=1&coupon_code=&selected_price=12
//            if(jsonObject==null){
//                return "";
//            }
//            Log.e("Log result >>", jsonObject.toString());
//            if (jsonObject != null) {
//                Log.e("Log result >>", jsonObject.toString());
//                return jsonObject.toString();
//            }
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Response reached---------------value in 's' variable
        }
    }

    class MakeCourseSubscription extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);
        String url = getApplicationContext().getString(R.string.apiURL) + "makeCourseSubscription";


        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(CourseDetailsActivity.this);
            String param = "student_id=" + studId + "&course_id=" + courseId + "&subscription_month=" + subscriptionMonths + "&order_id=" + orderID + "&coupon_code=" +
                    coupon_code + "&coupon_value=" + coupon_code_values + "&amount_paid=" + total_price + "&amount_course=" + price +  "&course_type=" + "course";

            Log.e("param", param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
            if (jsonObject == null) {
                return "";
            }
            Log.e("Log result >>", jsonObject.toString());

            Log.e("Log result >>", jsonObject.toString());
            return jsonObject.toString();


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

                    String data = jsonObject.getString("data");
//                    Toast.makeText(CourseDetailsActivity.this, "" + data, Toast.LENGTH_SHORT).show();
                    if (true) {
                        //Undermaintance
                        AlertDialog.Builder qdialogBuilder = new AlertDialog.Builder(CourseDetailsActivity.this);
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater qinflater = CourseDetailsActivity.this.getLayoutInflater();
                        View qdialogView = qinflater.inflate(R.layout.congratulations, null);
                        qdialogBuilder.setView(qdialogView);
                        //Alert Dialog Layout work

                        Log.e("Working", "Working");
                        TextView qbtnOK = qdialogView.findViewById(R.id.btnOK);
                        TextView duration_txt = qdialogView.findViewById(R.id.duration_txt);
//
                        TextView underMaintananceContent = qdialogView.findViewById(R.id.underMaintananceContent);

                        underMaintananceContent.setText("Course subscribed successfully.");

                        if (subscriptionMonths == 1) {
                            duration_txt.setText("Duration: " + subscriptionMonths + " month");

                        } else {
                            duration_txt.setText("Duration: " + subscriptionMonths + " months");

                        }
                        new GetCourseDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        qbtnOK.setText("OK");

                        final AlertDialog qalertDialog = qdialogBuilder.create();
                        qalertDialog.show();
                        qalertDialog.setCanceledOnTouchOutside(false);
                        qbtnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ItemMyCourse itemMyCourse = new ItemMyCourse();
                                itemMyCourse.setId(courseId);
                                itemMyCourse.setDescription(courseDescriptionTv.getText().toString());
                                itemMyCourse.setDuration(courseDuration);
                                itemMyCourse.setOwner_name(courseOwnerName);
                                itemMyCourse.setTitle(courseNameTv.getText().toString());
                                itemMyCourse.setOwner_qualification(courseOwnerQuali);
                                myCoursesList.add(itemMyCourse);


                                qalertDialog.dismiss();
                            }
                        });

                    } else {
                        new GetCourseDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Response reached---------------value in 's' variable
        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        new GetCourseDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetCourseDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onBackPressed() {
        try {
            player.pause();
            player.release();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fromActivity.equals("communityChatPage") || fromActivity.equals("homePage")) {
            startActivity(new Intent(CourseDetailsActivity.this, DashboardActivity.class));
        } else {
            super.onBackPressed();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            pauseBtn.setVisibility(View.GONE);
            player.pause();
            playBtn.setVisibility(View.VISIBLE);
            player.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            player.pause();
            player.release();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
