package com.vision_digital.TestSeries;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.BuildConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.vision_digital.R;
import com.vision_digital.TestSeries.model.objectiveQuestion.ItemObjectiveQuestion;
import com.vision_digital.TestSeries.model.objectiveQuestion.options.ItemOption;
import com.vision_digital.TestSeries.model.section.ItemSection;
import com.vision_digital.helperClasses.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TestDetailsActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    TextView startDateTxt, endDateTxt;
    LinearLayout startDateLayout, endDateLayout;
    ProgressDialog dialog;
    Button proceedForTestBtn;
    CheckBox tAndConditionTest;
    TextView descriptionTV;
    static String titleTestSeries;
    //    String currentTime;
    String textStartTime, textEndTime;
    Date startDate, endDate, currentTime, startTimeFormate, endTimeFormate;
    public static ArrayList<ItemObjectiveQuestion> questionArrayList = null;

    //Shared preference option
    public static String answer;

    boolean isBundle = false;


    static long timeLimit = 60 * 30;
    String timeDuration;
    String testId = "";
    String desc = "";
    String subscription_status = "";

    //Payment Work-----------------------------------
    int sid;
    String uid = "";
    String orderID;
    String testType = "";
    String logData;
    String gateway_name = "paytm";
    String timeStamp;
    JSONObject jsonRawData = new JSONObject();
    int activeOrderCount = 0;
    String price = "";
    String startTime, endTime;


    public static ArrayList<ItemSection> sectionArrayList = new ArrayList<>();

    //Payment-------------------
    // Paytm------------------
    String mid = "IxDAFe91483847846332"; //Rajit
    //    String mid = "TLvNGP42879274127615";
//                                String marchentKey = "G&%UEPNTnz6&4cti";
    String marchentKey = "1BGSS5qX5OuGWITA"; //Rajit
// 10th charector is not zero


    // subscription popup widgets
    TextView totalPriceTest, total_gst_test, sub_total_test, initial_price_test, btnApplyCoupon, couponMsgTest, coupon_price_test, duration;
    LinearLayout duration_layout;
    EditText coupon_code_edt_test;
    String coupon_code_test = "";
    LinearLayout priceLayout, coupon_value_layout_linear_test, sub_total_linear_test, gst_linear;
    String coursePlanUrlTest = "";
    String total_price = "";
    TextView qbtnOK;
    String coupon_code_values = "";
    String coupon_status = "";
    String gst = "";
    String iniPrice = "";
    String coupon_msg = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        testId = getIntent().getStringExtra("id");
        testType = getIntent().getStringExtra("testType");
        price = getIntent().getStringExtra("price");
        desc = getIntent().getStringExtra("desc");


        Log.e("testType", testType);
        Log.e("testId", testId);

       // coursePlanUrlTest = "https://chalksnboard.com/api/v3/availableChapterInMonth";
        coursePlanUrlTest = getApplicationContext().getString(R.string.apiURL4)+"availableChapterInMonth";

//        currentTime = (Date) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());
        currentTime = Calendar.getInstance().getTime();
//        Toast.makeText(this, "Current Date is"+currentTime, Toast.LENGTH_LONG).show();


        SharedPreferences uidDetails = getSharedPreferences("CNBUID", MODE_PRIVATE);
        uid = uidDetails.getString("uid", "NO_NAME");


//        timeLimit = 60 * 30;
        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        sid = studDetails.getInt("sid", 0);
        startDateTxt = findViewById(R.id.startDate);
        endDateTxt = findViewById(R.id.endDate);
        startDateLayout = findViewById(R.id.startDateLayout);
        endDateLayout = findViewById(R.id.endDateLayout);


        if (testId == null) {
            Uri data = getIntent().getData();
            List<String> param = data.getPathSegments();
            String first = param.get(0);
            testId = first;
        }

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        descriptionTV = findViewById(R.id.description);


        tAndConditionTest = findViewById(R.id.tAndConditionTest);
        proceedForTestBtn = findViewById(R.id.proceedForTestBtn);

        tAndConditionTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (tAndConditionTest.isChecked()) {
                    proceedForTestBtn.setEnabled(true);
                } else {
                    proceedForTestBtn.setEnabled(false);
                }
            }
        });

        proceedForTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (testType.equals("bundle")) {

                    isBundle = true;
                    startDateLayout.setVisibility(View.GONE);
                    endDateLayout.setVisibility(View.GONE);
                    subscription_status = "unsubscribed";
                    descriptionTV.setText(desc);
                    price = getIntent().getStringExtra("price");
                    total_price = price;


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TestDetailsActivity.this);
                    // ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = TestDetailsActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.test_subscription_popup, null);
                    dialogBuilder.setView(dialogView);
                    //Alert Dialog Layout work
                    final AlertDialog alertDialog = dialogBuilder.create();
                    TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
                    priceDetails.setVisibility(View.GONE);
                    total_gst_test = dialogView.findViewById(R.id.total_gst_test);
                    totalPriceTest = dialogView.findViewById(R.id.totalPriceTest);
                    coupon_code_edt_test = dialogView.findViewById(R.id.coupon_code_edt_test);
                    initial_price_test = dialogView.findViewById(R.id.initial_price_test);
                    sub_total_test = dialogView.findViewById(R.id.sub_total_test);
                    priceLayout = dialogView.findViewById(R.id.priceLayout);
                    btnApplyCoupon = dialogView.findViewById(R.id.btnApplyCoupon);
                    coupon_price_test = dialogView.findViewById(R.id.coupon_price_test);
                    coupon_value_layout_linear_test = dialogView.findViewById(R.id.coupon_value_layout_linear_test);
                    sub_total_linear_test = dialogView.findViewById(R.id.sub_total_linear_test);
                    gst_linear = dialogView.findViewById(R.id.gst_linear);
                    duration = dialogView.findViewById(R.id.duration);
                    duration_layout = dialogView.findViewById(R.id.duration_layout);
                    duration_layout.setVisibility(View.GONE);

                    couponMsgTest = dialogView.findViewById(R.id.couponMsgTest);
                    couponMsgTest.setVisibility(View.GONE);
                    gst_linear.setVisibility(View.GONE);
                    sub_total_linear_test.setVisibility(View.GONE);
                    coupon_value_layout_linear_test.setVisibility(View.GONE);
                    btnApplyCoupon.setVisibility(View.GONE);


                    duration.setText(timeDuration);
                    initial_price_test.setText("\u20b9 " + price + "/-");
                    totalPriceTest.setText("\u20b9 " + price + "/-");

                    coupon_code_edt_test.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            coupon_code_test = s.toString();
                            btnApplyCoupon.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            coupon_code_test = s.toString();
                            btnApplyCoupon.setVisibility(View.VISIBLE);
                        }
                    });


                    btnApplyCoupon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            new GetSubscriptionTestDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        }
                    });
                    qbtnOK = dialogView.findViewById(R.id.btnOK);
                    qbtnOK.setText("Pay & Subscribe");
                    qbtnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (total_price.equals("0")) {

                                try {
                                    jsonRawData.put("free", "couponCodeApplied");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                new MakeTestSubscription().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            } else {
                                new SendUserDetailTOServerdd().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }

                            alertDialog.dismiss();
                        }
                    });


                    alertDialog.show();
                    proceedForTestBtn.setEnabled(true);
                    alertDialog.setCanceledOnTouchOutside(false);
                } else {

                    startDateLayout.setVisibility(View.VISIBLE);
                    endDateLayout.setVisibility(View.VISIBLE);
//                    dialog = new ProgressDialog(TestDetailsActivity.this);
//                    dialog.setCanceledOnTouchOutside(false);
//                    dialog.setMessage("Loading");
//                    dialog.show();
                    new GetTestDetailsForProceedBtn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

            }
        });

        //Paytm Payment-----------------------------------------
        String alpha = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        Long tsLong = System.currentTimeMillis() / 1000;
        timeStamp = tsLong.toString();
//        orderID = alpha.charAt(random.nextInt(26)) + uid + alpha.charAt(random.nextInt(26)) + timeStamp;
        orderID = "OD" + sid + "S" + timeStamp;

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


    }


    class GetTestDetails extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
            //            this.dialog.setMessage("Please wait");
            //            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(TestDetailsActivity.this);



//            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
            String param = "test_id=" + testId + "&sid=" + sid;
            Log.e("param", param);

           // https://chalksnboard.com/api/v3/getTestSeries"

            JSONObject jsonObject = jsonParser.makeHttpRequest(getApplicationContext().getString(R.string.apiURL4)+"getTestSeries", "POST", param);
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
                            //Subscription Status check
                            subscription_status = dataObj.getString("subscription_status");
                            if (subscription_status.equals("unsubscribed")) {
                                price = dataObj.getString("price");
                                total_price = price;

                                Log.e("totalprice price", total_price);
                                proceedForTestBtn.setText("Subscribe");
                            } else {
                                proceedForTestBtn.setText("Start Test");

                            }

                            //Running Fine

                            String description = dataObj.getString("description");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                descriptionTV.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                descriptionTV.setText(Html.fromHtml(description));
                            }
                            SharedPreferences saveTimeData = getSharedPreferences(testId, MODE_PRIVATE);

                            Long time_saved = saveTimeData.getLong("timeSave", 0);
                            if (time_saved == 0) {
                                timeLimit = dataObj.getLong("duration");

                                long hours = timeLimit / 3600;
                                long remainder = timeLimit - hours * 3600;
                                long min = remainder / 60;
                                long sec = timeLimit % 60;
                                String hour = String.format("%02d", hours);
                                String minut = String.format("%02d", min);

                                if (hours == 0) {
                                    timeDuration = minut + " minutes";
                                } else {
                                    timeDuration = hour + ":" + minut + " hours";
                                }

                            } else {
                                timeLimit = time_saved;

                            }
                            Log.e("timesaved", String.valueOf(time_saved));

                            titleTestSeries = dataObj.getString("title");
                            startTime = dataObj.getString("start");
                            Log.e("start", startTime);
                            endTime = dataObj.getString("end");
                            Log.e("end", endTime);


                            SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat outputFormatter = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa");

                            try {
                                startTimeFormate = inputFormatter.parse(startTime);
                                textStartTime = outputFormatter.format(startTimeFormate);
                                startDateTxt.setText(textStartTime);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            try {
                                endTimeFormate = inputFormatter.parse(endTime);
                                textEndTime = outputFormatter.format(endTimeFormate);
                                endDateTxt.setText(textEndTime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            try {
                                startDate = format.parse(startTime);


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            try {
                                endDate = format2.parse(endTime);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            //fetching sections--------------
                            JSONArray sectionsArray = dataObj.getJSONArray("sections");
                            sectionArrayList.clear();
                            for (int i = 0; i < sectionsArray.length(); i++) {
                                JSONObject sectionObject = sectionsArray.getJSONObject(i);
                                ItemSection section = new ItemSection();

                                section.setId(sectionObject.getString("id"));
//                                section.setName("Section" + sectionObject.getString("id"));
                                section.setName(sectionObject.getString("name"));
                                section.setType(sectionObject.getString("type"));

                                JSONArray questionsArray = sectionObject.getJSONArray("questions");
//                                ArrayList<ItemObjectiveQuestion> questionArrayList = null;
                                switch (section.getType()) {
                                    case "Objective":
                                        questionArrayList = new ArrayList<ItemObjectiveQuestion>();
                                        for (int j = 0; j < questionsArray.length(); j++) {
                                            ItemObjectiveQuestion objectiveQuestion = new ItemObjectiveQuestion();
                                            JSONObject questionObject = questionsArray.getJSONObject(j);

                                            SharedPreferences saveTestData = getSharedPreferences("cnb" + testId, MODE_PRIVATE);

                                            answer = saveTestData.getString(questionObject.getString("id"), "NO_VALUE");
                                            Log.e("answer", answer);

                                            if (answer.equals("NO_VALUE")) {
                                                objectiveQuestion.setStatus("");

                                            } else if (answer.equals("")) {
                                                objectiveQuestion.setStatus("");
                                            } else {
                                                objectiveQuestion.setStatus("Answered");
                                                objectiveQuestion.setAnsweredAnswer(answer);


                                            }
                                            objectiveQuestion.setId(questionObject.getString("id"));

                                            objectiveQuestion.setQuestion(questionObject.getString("question"));
                                            if (!questionObject.getString("image").equals(null)) {
                                                objectiveQuestion.setQuestionImageURL(questionObject.getString("image"));

                                            }
//                                            JSONArray optionsArray = questionObject.getJSONArray("options");
//                                            ArrayList<ItemOption> optionsList = new ArrayList<>();
//                                            for (int k = 0; k < optionsArray.length(); k++) {
//                                                ItemOption itemOption = new ItemOption();
//                                                itemOption.setOption(optionsArray.getString(k));
//                                                if (optionsArray.getString(k).trim().equals(answer.trim())) {
//                                                    itemOption.setSelected(true);
//                                                }
//                                                optionsList.add(itemOption);
//                                            }

                                            JSONObject optionObject = questionObject.getJSONObject("options");
                                            ArrayList<ItemOption> optionsList = new ArrayList<>();
                                            int a = 1;
                                            for (int k = 0; k < optionObject.length(); k++) {
                                                ItemOption itemOption = new ItemOption();

                                                JSONObject optionObjectvar = optionObject.getJSONObject("option" + a);
                                                Log.e("objectoption", String.valueOf(optionObject.getJSONObject("option1")));
                                                itemOption.setOptNo("option" + a);
                                                String optionNumber = "option" + a;
                                                itemOption.setOption(optionObjectvar.getString("title"));
                                                Log.e("title", (optionObjectvar.getString("title")));
                                                itemOption.setOptionImageUrl(optionObjectvar.getString("image"));
                                                if (optionNumber.trim().equals(answer.trim())) {
                                                    itemOption.setSelected(true);
                                                }

                                                optionsList.add(itemOption);
                                                a++;

                                            }


                                            Collections.shuffle(optionsList);
                                            objectiveQuestion.setOptions(optionsList);
                                            questionArrayList.add(objectiveQuestion);
                                        }
                                        break;
                                    default:

                                        break;

                                }
                                Collections.shuffle(questionArrayList);
                                Object questionsList = questionArrayList;
                                section.setQuestionsList((ArrayList<Object>) questionsList);
                                sectionArrayList.add(section);
                                Log.e("Test", sectionArrayList.toString());
                            }

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TestDetailsActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = TestDetailsActivity.this.getLayoutInflater();
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
                            Toast.makeText(TestDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(TestDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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

    class GetTestDetailsForProceedBtn extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
            //            this.dialog.setMessage("Please wait");
            //            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(TestDetailsActivity.this);

//            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
            String param = "test_id=" + testId + "&sid=" + sid;
            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(getApplicationContext().getString(R.string.apiURL4)+"getTestSeries", "POST", param);
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
                            //Subscription Status check
                            subscription_status = dataObj.getString("subscription_status");
                            if (subscription_status.equals("unsubscribed")) {
                                price = dataObj.getString("price");
                                total_price = price;
                                if (subscription_status.equals("unsubscribed")) {

                                    Log.e("action", "going to payment");

                                    //Undermaintance
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TestDetailsActivity.this);
                                    // ...Irrelevant code for customizing the buttons and title
                                    LayoutInflater inflater = TestDetailsActivity.this.getLayoutInflater();
                                    View dialogView = inflater.inflate(R.layout.test_subscription_popup, null);
                                    dialogBuilder.setView(dialogView);
                                    //Alert Dialog Layout work
                                    final AlertDialog alertDialog = dialogBuilder.create();
                                    TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
                                    priceDetails.setVisibility(View.GONE);
                                    total_gst_test = dialogView.findViewById(R.id.total_gst_test);
                                    totalPriceTest = dialogView.findViewById(R.id.totalPriceTest);
                                    coupon_code_edt_test = dialogView.findViewById(R.id.coupon_code_edt_test);
                                    initial_price_test = dialogView.findViewById(R.id.initial_price_test);
                                    sub_total_test = dialogView.findViewById(R.id.sub_total_test);
                                    priceLayout = dialogView.findViewById(R.id.priceLayout);
                                    btnApplyCoupon = dialogView.findViewById(R.id.btnApplyCoupon);
                                    coupon_price_test = dialogView.findViewById(R.id.coupon_price_test);
                                    coupon_value_layout_linear_test = dialogView.findViewById(R.id.coupon_value_layout_linear_test);
                                    sub_total_linear_test = dialogView.findViewById(R.id.sub_total_linear_test);
                                    gst_linear = dialogView.findViewById(R.id.gst_linear);
                                    duration = dialogView.findViewById(R.id.duration);
                                    duration_layout = dialogView.findViewById(R.id.duration_layout);
                                    duration_layout.setVisibility(View.VISIBLE);

                                    couponMsgTest = dialogView.findViewById(R.id.couponMsgTest);
                                    couponMsgTest.setVisibility(View.GONE);
                                    gst_linear.setVisibility(View.GONE);
                                    sub_total_linear_test.setVisibility(View.GONE);
                                    coupon_value_layout_linear_test.setVisibility(View.GONE);
                                    btnApplyCoupon.setVisibility(View.GONE);


                                    duration.setText(timeDuration);
                                    initial_price_test.setText("\u20b9 " + price + "/-");
                                    totalPriceTest.setText("\u20b9 " + price + "/-");

                                    coupon_code_edt_test.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                                            coupon_code_test = s.toString();
                                            btnApplyCoupon.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            coupon_code_test = s.toString();
                                            btnApplyCoupon.setVisibility(View.VISIBLE);
                                        }
                                    });


                                    btnApplyCoupon.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            new GetSubscriptionTestDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                        }
                                    });


//                    if (isBundle) {
//                        priceDetails.setText("\nCost: \u20b9 " + price);
//
//                    } else {
//                        priceDetails.setText(timeDuration + "\nCost: \u20b9 " + price);
//
//                    }
                                    qbtnOK = dialogView.findViewById(R.id.btnOK);
                                    qbtnOK.setText("Pay & Subscribe");
                                    qbtnOK.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (total_price.equals("0")) {

                                                try {
                                                    jsonRawData.put("free", "couponCodeApplied");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                new MakeTestSubscription().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                            } else {
                                                new SendUserDetailTOServerdd().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                            }

                                            alertDialog.dismiss();
                                        }
                                    });


                                    alertDialog.show();
                                    proceedForTestBtn.setEnabled(true);
                                    alertDialog.setCanceledOnTouchOutside(false);
                                } else if (currentTime.after(startDate) || currentTime.equals(startDate)) {
                                    Intent startTestIntent = new Intent(TestDetailsActivity.this, OngoingTestActivity.class);
                                    startTestIntent.putExtra("id", testId);
                                    startActivity(startTestIntent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                } else {
                                    Toast.makeText(TestDetailsActivity.this, "Test will start on Time!", Toast.LENGTH_SHORT).show();
                                }
                                Log.e("totalprice price", total_price);
                                proceedForTestBtn.setText("Subscribe");
                            } else {
                                proceedForTestBtn.setText("Start Test");
                                if (currentTime.after(startDate) || currentTime.equals(startDate)) {
                                    Intent startTestIntent = new Intent(TestDetailsActivity.this, OngoingTestActivity.class);
                                    startTestIntent.putExtra("id", testId);
                                    startActivity(startTestIntent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                } else {
                                    Toast.makeText(TestDetailsActivity.this, "Test will start on Time!", Toast.LENGTH_SHORT).show();
                                }

                            }


                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TestDetailsActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = TestDetailsActivity.this.getLayoutInflater();
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
                            Toast.makeText(TestDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(TestDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable

        }
    }

    class SendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(TestDetailsActivity.this);
        //private String orderId , mid, custid, amt;
        String host = "https://securegw.paytm.in/";
        String varifyurl = host + "theia/paytmCallback?ORDER_ID=" + orderID;
        String url = getApplicationContext().getString(R.string.apiURL) + "generateCheckSum";

//        String varifyurlOld = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";


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
            JSONParser jsonParser = new JSONParser(TestDetailsActivity.this);
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
                            "&CALLBACK_URL=" + varifyurl + "&INDUSTRY_TYPE_ID=Retail";
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
            paramMap.put("CALLBACK_URL", varifyurl);
            //paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
            // paramMap.put( "MOBILE_NO" , "9144040888");  // no need
            paramMap.put("CHECKSUMHASH", CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param " + paramMap.toString());
            Service.initialize(Order, null);
            // start payment service call here
            Service.startPaymentTransaction(TestDetailsActivity.this, true, true,
                    TestDetailsActivity.this);
        }
    }


//    public class GetSubscriptionCourseDetails extends AsyncTask<String, Void, String> {
//
//        private ProgressDialog dialog = new ProgressDialog(TestDetailsActivity.this);
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
//            JSONParser jsonParser = new JSONParser(TestDetailsActivity.this);
//            int versionCode = BuildConfig.VERSION_CODE;
//
////            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
//            String param = "sid=" + sid + "&course_id=" + testId + "&subscription_month=" + "1" + "&coupon_code=" + coupon_code_test + "&selected_price=" + price;
//
//
//            Log.e("param", param);
//
//            JSONObject jsonObject = jsonParser.makeHttpRequest(coursePlanUrlTest, "POST", param);
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
//            if (!s.equals("")) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(s);
//
//                    Log.e("Result : ", s);
//
//                    //Do work-----------------------------
//                    String status = jsonObject.getString("status");
//                    final JSONObject dataObj = jsonObject.getJSONObject("data");
//
//                    switch (status) {
//                        case "success":
//                            //Running Fine
//
//
//                            JSONObject couponDetails = dataObj.getJSONObject("coupon");
//                            String totalprice = couponDetails.getString("total_amount");
//                            coupon_msg = couponDetails.getString("msg");
//                            iniPrice = couponDetails.getString("sub_total");
//                            gst = couponDetails.getString("gst_amount");
//                            coupon_status = couponDetails.getString("status");
//                            coupon_code_values = couponDetails.getString("coupon_code_value");
//                            total_price = totalprice;
//                            Log.e("totalprice totalprice", total_price);
//
//                            if (total_price.equals("0")) {
//                                qbtnOK.setText("Redeem now");
//                            } else {
//                                qbtnOK.setText("Proceed to pay");
//                            }
//
//
//                            this.dialog.dismiss();
//
//                            if (coupon_status.equals("failure")) {
//                                couponMsgTest.setVisibility(View.VISIBLE);
//                                if (!coupon_msg.equals("")) {
//                                    couponMsgTest.setText(coupon_msg + "..");
//                                    couponMsgTest.setTextColor(Color.parseColor("#D30404"));
//                                }
//                                gst_linear.setVisibility(View.GONE);
//                                sub_total_linear_test.setVisibility(View.GONE);
//                                coupon_value_layout_linear_test.setVisibility(View.GONE);
//
//                            } else {
//                                btnApplyCoupon.setVisibility(View.GONE);
//                                couponMsgTest.setVisibility(View.VISIBLE);
//                                if (!coupon_msg.equals("")) {
//                                    couponMsgTest.setText(coupon_msg + "..");
//                                    couponMsgTest.setTextColor(Color.parseColor("#FF007AFF"));
//                                }
//                                gst_linear.setVisibility(View.VISIBLE);
//                                sub_total_linear_test.setVisibility(View.VISIBLE);
//                                coupon_value_layout_linear_test.setVisibility(View.VISIBLE);
//                                totalPriceTest.setText("\u20b9 " + total_price + "/-");
//                                priceLayout.setVisibility(View.VISIBLE);
//                                initial_price_test.setText("\u20b9 " + price + "/-");
//                                total_gst_test.setText("\u20b9 " + gst + "/-");
//                                sub_total_test.setText("\u20b9 " + iniPrice + "/-");
//                                coupon_price_test.setText("\u20b9 " + coupon_code_values + "/-");
//
//
//                            }
////
//
//
//                            break;
//                        case "maintenance":
//                            //Undermaintance
//                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TestDetailsActivity.this);
//                            // ...Irrelevant code for customizing the buttons and title
//                            LayoutInflater inflater = TestDetailsActivity.this.getLayoutInflater();
//                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
//                            dialogBuilder.setView(dialogView);
//                            //Alert Dialog Layout work
//                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
//                            String msgContent = dataObj.getString("message");
//                            maintainanceContent.setText(Html.fromHtml(msgContent));
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
//                            Toast.makeText(TestDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
//                            break;
//                        default:
//                            Toast.makeText(TestDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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


    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network gone", Toast.LENGTH_SHORT).show();
//        payViaPaytmBtn.setEnabled(true);
        proceedForTestBtn.setEnabled(true);
    }

    @Override
    public void onErrorProceed(String s) {

    }

    @Override
    public void clientAuthenticationFailed(String s) {
        proceedForTestBtn.setEnabled(true);
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  " + s);
//        payViaPaytmBtn.setEnabled(true);
        proceedForTestBtn.setEnabled(true);
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true " + s + "  s1 " + s1);
//        payViaPaytmBtn.setEnabled(true);
        proceedForTestBtn.setEnabled(true);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        proceedForTestBtn.setEnabled(true);
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        proceedForTestBtn.setEnabled(true);
    }

    @Override
    public void onTransactionResponse(Bundle bundle) {
//        Log.e("checksum ", " respon true " + bundle.toString());
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
//        new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        if (txn_status.equals("TXN_SUCCESS")) {

            new MakeTestSubscription().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }

//        payViaPaytmBtn.setEnabled(true);
        proceedForTestBtn.setEnabled(true);

    }


    class LogRawData extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);
        String url = getApplicationContext().getString(R.string.apiURL4)+"logTransactions"; //"https://chalksnboard.com/api/v3/logTransactions";


        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(TestDetailsActivity.this);
            String param = "student_id=" + sid + "&course_id=" + testId + "&uid=" + uid + "&gateway_response=" + jsonRawData + "&gateway_name=" + gateway_name;


            Log.e(TAG, "doInBackground: " + param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
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

    class MakeTestSubscription extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);
       // String url ="https://chalksnboard.com/api/v3/makeTestSubscription";
        String url = getApplicationContext().getString(R.string.apiURL4)+"makeTestSubscription";


        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(TestDetailsActivity.this);
            String param = "sid=" + sid + "&test_id=" + testId + "&subscription_month=" + 1 + "&order_id=" + orderID + "&test_type=" + testType
                    + "&coupon_code=" +
                    coupon_code_test + "&coupon_value=" + coupon_code_values + "&amount_paid=" + total_price + "&amount_course=" + price;
            ;


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
                        AlertDialog.Builder qdialogBuilder = new AlertDialog.Builder(TestDetailsActivity.this);
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater qinflater = TestDetailsActivity.this.getLayoutInflater();
                        View qdialogView = qinflater.inflate(R.layout.congratulations, null);

                        qdialogBuilder.setView(qdialogView);
                        //Alert Dialog Layout work

                        Log.e("Working", "Working");
                        TextView btnOK = qdialogView.findViewById(R.id.btnOK);
                        TextView duration_txt = qdialogView.findViewById(R.id.duration_txt);


                        duration_txt.setText(timeDuration);
                        btnOK.setText("OK");

                        final AlertDialog qalertDialog = qdialogBuilder.create();
                        qalertDialog.show();
                        qalertDialog.setCanceledOnTouchOutside(false);
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isBundle) {
                                    qalertDialog.dismiss();
                                    finish();
                                } else {
                                    qalertDialog.dismiss();
                                    finish();

                                }
                            }
                        });

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Response reached---------------value in 's' variable
        }
    }


    public class GetSubscriptionTestDetails extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(TestDetailsActivity.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(TestDetailsActivity.this);
            int versionCode = BuildConfig.VERSION_CODE;

//            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
            String param = "sid=" + sid + "&course_id=" + testId + "&subscription_month=" + "1" + "&coupon_code=" + coupon_code_test + "&selected_price=" + price;


            Log.e("param", param);

            String testCouponUrl= getApplicationContext().getString(R.string.apiURL)+"apply_coupon_code_check";

            JSONObject jsonObject = jsonParser.makeHttpRequest(testCouponUrl, "POST", param);
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


                    switch (status) {
                        case "success":
                            //Running Fine

                            final JSONObject dataObj = jsonObject.getJSONObject("data");


                           // JSONObject couponDetails = dataObj.getJSONObject("coupon");
                            String totalprice = dataObj.getString("total_amount");
                            coupon_msg = dataObj.getString("message");
                            iniPrice = dataObj.getString("sub_total");
                            gst = dataObj.getString("gst_amount");
                            coupon_status=status;
                          //  coupon_status = couponDetails.getString("status");
                            coupon_code_values = dataObj.getString("coupon_code_value");
                            total_price = totalprice;
                            Log.e("totalprice totalprice", total_price);

                            if (total_price.equals("0")) {
                                qbtnOK.setText("Redeem now");
                            } else {
                                qbtnOK.setText("Proceed to pay");
                            }


                            this.dialog.dismiss();

                            if (coupon_status.equals("failure")) {
                                couponMsgTest.setVisibility(View.VISIBLE);
                                if (!coupon_msg.equals("")) {
                                    couponMsgTest.setText(coupon_msg + "..");
                                    couponMsgTest.setTextColor(Color.parseColor("#D30404"));
                                }
                                gst_linear.setVisibility(View.GONE);
                                sub_total_linear_test.setVisibility(View.GONE);
                                coupon_value_layout_linear_test.setVisibility(View.GONE);

                            } else {
                                btnApplyCoupon.setVisibility(View.GONE);
                                couponMsgTest.setVisibility(View.VISIBLE);
                                if (!coupon_msg.equals("")) {
                                    couponMsgTest.setText(coupon_msg + "..");
                                    couponMsgTest.setTextColor(Color.parseColor("#FF007AFF"));
                                }
                                gst_linear.setVisibility(View.VISIBLE);
                                sub_total_linear_test.setVisibility(View.VISIBLE);
                                coupon_value_layout_linear_test.setVisibility(View.VISIBLE);
                                totalPriceTest.setText("\u20b9 " + total_price + "/-");
                                priceLayout.setVisibility(View.VISIBLE);
                                initial_price_test.setText("\u20b9 " + price + "/-");
                                total_gst_test.setText("\u20b9 " + gst + "/-");
                                sub_total_test.setText("\u20b9 " + iniPrice + "/-");
                                coupon_price_test.setText("\u20b9 " + coupon_code_values + "/-");


                            }
//


                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TestDetailsActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = TestDetailsActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
                           // String msgContent = dataObj.getString("message");
                           // maintainanceContent.setText(Html.fromHtml(msgContent));

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
                            Log.e("IN FAILURE",status);
                            String message = jsonObject.getString("message");
                            coupon_status=status;
                            coupon_msg=message;


                            couponMsgTest.setVisibility(View.VISIBLE);
                            if (!coupon_msg.equals("")) {
                                couponMsgTest.setText(coupon_msg + "..");
                                couponMsgTest.setTextColor(Color.parseColor("#D30404"));
                            }
                            gst_linear.setVisibility(View.GONE);
                            sub_total_linear_test.setVisibility(View.GONE);
                            coupon_value_layout_linear_test.setVisibility(View.GONE);
                            break;
                        default:
                            Toast.makeText(TestDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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
        if (testType.equals("bundle")) {

            isBundle = true;
            startDateLayout.setVisibility(View.GONE);
            endDateLayout.setVisibility(View.GONE);
            subscription_status = "unsubscribed";
            proceedForTestBtn.setText("Subscribe");
            descriptionTV.setText(desc);
            price = getIntent().getStringExtra("price");
        } else {
            isBundle = false;
            startDateLayout.setVisibility(View.VISIBLE);
            endDateLayout.setVisibility(View.VISIBLE);
            dialog = new ProgressDialog(TestDetailsActivity.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading");
            dialog.show();
            new GetTestDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }


}

