package com.vision_digital.liveClass;


import static com.vision_digital.activities.DashboardActivity.sid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.chaos.view.BuildConfig;
import com.vision_digital.R;
import com.vision_digital.activities.MyCoursesActivity;
import com.vision_digital.activities.videoPlayer.YouTubeVideoPlayerActivity;
import com.vision_digital.databinding.ActivityLiveDetailsBinding;
import com.vision_digital.helperClasses.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LiveDetailsActivity extends AppCompatActivity  {

    ActivityLiveDetailsBinding binding;

    String liveDetailsUrl = "";
    String id, sub_category_id, category_id, title, registration_start_date, registration_end_date, short_desc, description, image, subscriptionValidity;
    String language, sellingPrice, ActualPrice;
    Boolean subscriptionStatus = false;
    int studId = 0;
    String courseId;

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
    String price = "";

    String totalPrice = "0";

    TextView emptyTextBox, totalPriceText, buyNowBtn;
    // Paytm------------------
    String mid = "IxDAFe91483847846332"; //Rajit

    String marchentKey = "1BGSS5qX5OuGWITA"; //Rajit


    JSONObject jsonRawData = new JSONObject();
    String orderId = "", razorPaymentId = "", status = "";

    String couponCode = "";

    String packageId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_live_details);
        //        To Stop user from video recording or taking screen shot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        courseId = getIntent().getStringExtra("id");
        packageId = getIntent().getStringExtra("packageId");

        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        studId = studDetails.getInt("sid", 0);

        liveDetailsUrl = getApplicationContext().getString(R.string.apiURL) + "getCourseDetailsLive";

        binding.classRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LiveDetailsActivity.this, LiveClassRoomActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        binding.buyLiveCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                total_price = sellingPrice;


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LiveDetailsActivity.this);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = LiveDetailsActivity.this.getLayoutInflater();
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
                duration.setVisibility(View.GONE);

                duration_layout = dialogView.findViewById(R.id.duration_layout);
                duration_layout.setVisibility(View.GONE);

                couponMsgTest = dialogView.findViewById(R.id.couponMsgTest);
                couponMsgTest.setVisibility(View.GONE);
                gst_linear.setVisibility(View.GONE);
                sub_total_linear_test.setVisibility(View.GONE);
                coupon_value_layout_linear_test.setVisibility(View.GONE);
                btnApplyCoupon.setVisibility(View.GONE);

                Long tsLong = System.currentTimeMillis() / 1000;
                String timeStamp = tsLong.toString();
                orderId = "OD" + sid + "S" + timeStamp;

                initial_price_test.setText("\u20b9 " + sellingPrice + "/-");
                totalPriceTest.setText("\u20b9 " + sellingPrice + "/-");

                coupon_code_edt_test.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        coupon_code_test = s.toString();
                        couponCode = coupon_code_test;
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

                        new GetSubscriptionDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
                            new MakeLiveClassSubscription().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        } else {
                          //  startPayment();
                            // new SendUserDetailTOServer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }

                        alertDialog.dismiss();
                    }
                });


                alertDialog.show();
                binding.buyLiveCourseBtn.setEnabled(true);
                alertDialog.setCanceledOnTouchOutside(false);
            }
        });

        new GetLiveClassesDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    //======= To open pdf after clicking on Download Schedule button id is btnDownloadPdfLiveClass ==========
    private void openPdfInBrowser(String pdfUrl) {
        // Create an Intent to open the PDF in a browser
        Intent intentPdf = new Intent(Intent.ACTION_VIEW);
        intentPdf.setData(Uri.parse(pdfUrl));

        try {
            startActivity(intentPdf);
        } catch (Exception e) {
            // Handle exceptions, such as the browser not being available or the URL being invalid
            Toast.makeText(LiveDetailsActivity.this, "Try again or document not available", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //    ==================================================================================================
//======= To extract the id from the video url ========================
    public String extractVideoIdFromEmbedUrl(String embedUrl) {
        String videoId = null;
        if (embedUrl != null && !embedUrl.isEmpty()) {
            int index = embedUrl.lastIndexOf('/');
            if (index != -1) {
                videoId = embedUrl.substring(index + 1);
            }
        }
        return videoId;
    }
//    =======================================================================

//    public void startPayment() {
//
//        Long tsLong = System.currentTimeMillis() / 1000;
//        String timeStamp = tsLong.toString();
//        orderId = "OD" + sid + "S" + timeStamp;
//
//
//        final Checkout co = new Checkout();
//        co.setKeyID("rzp_live_1SWl87h7M6UCNY");
//        co.setImage(R.drawable.ic_dikshant_logo);
//
//        try {
//
//            SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
//            String sid = String.valueOf(studDetails.getInt("sid", 0));
//            String userName = studDetails.getString("userName", "NoNAME");
//            String userEmail = studDetails.getString("userEmail", "");
//            String userMobile = studDetails.getString("userMobile", "+910000000000");
//            String profileName = studDetails.getString("profileName", "NoNAME");
//
//
//            int amount = Integer.parseInt(total_price);
//            // amount to be in paisa only
//            int finalAmount = amount * 100; // converting 12p rupees into paisa
//
//            JSONObject options = new JSONObject();
//            options.put("name", "Dikshant IAS/PCS");
//            options.put("description", "Student name is -" + userName + "Student Id is--" + sid);
//            //You can omit the image option to fetch the image from dashboard
//            //   options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("currency", "INR");
//            options.put("amount", String.valueOf(finalAmount));
//            // to set theme color
//            options.put("theme.color", "#FF0000");
//
//
//            //      options.put("order_id", order_DBJOWzybf0sJbb);//from response of step 3.
//
//            JSONObject preFill = new JSONObject();
//            preFill.put("email", "" + userEmail);
//            preFill.put("contact", "" + userMobile);
//
//            JSONObject notes = new JSONObject();
//            notes.put("Product_details", "Here are the notes , Course Title is--" + title);
//
//            options.put("prefill", preFill);
//            options.put("notes", notes);
//
//            co.open(LiveDetailsActivity.this, options);
//        } catch (Exception e) {
//            Toast.makeText(LiveDetailsActivity.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
//                    .show();
//            e.printStackTrace();
//
//        }
//    }
//
//
//    @Override
//    public void onPaymentSuccess(String s, PaymentData paymentData) {
//        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
//        Log.e("TAG", "onPaymentSuccess: " + s + "-----Payment Data------" + "PaymentID--" + paymentData.getPaymentId());
//        razorPaymentId = paymentData.getPaymentId().toString();
//        jsonRawData = paymentData.getData();
//        status = "Success";
//        new MakeLiveClassSubscription().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPaymentError(int i, String s, PaymentData paymentData) {
//        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
//        status = "Failure";
//        razorPaymentId = paymentData.getPaymentId() + "NOID";
//        jsonRawData = paymentData.getData();
//        new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        Log.e("TAG", "onPaymentError: " + s + "------Payment Data-----" + paymentData.getOrderId() + paymentData.getPaymentId());
//    }

    class GetLiveClassesDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(LiveDetailsActivity.this);


            int versoncodes;


            PackageInfo pInfo = null;
            try {
                pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            versoncodes = pInfo.versionCode;
            Log.e("versionCode", String.valueOf(versoncodes));


            String param = "student_id=" + sid + "&live_course_id=" + courseId+"&package_id="+packageId;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(liveDetailsUrl, "POST", param);
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
                    //  dialog.dismiss();
                    String status = jsonObject.getString("status");
                    switch (status) {
                        case "success":

                            JSONObject dataObj = jsonObject.getJSONObject("data");
                            id = String.valueOf(dataObj.getInt("id"));
                            title = dataObj.getString("title");
                            description = dataObj.getString("description");
//                          description api data is in html format Spanned is converting it to HTML format result
//                            Spanned spannedHtml = Html.fromHtml(description); ---IT IS ALSO WORKING BUT CANNOT CONVERT HTML TABLE DATA INTO TEXT FORMAT
                            image = dataObj.getString("image");
                            sellingPrice = String.valueOf(dataObj.getInt("selling_price"));
                            ActualPrice = String.valueOf(dataObj.getInt("actual_price"));
                            subscriptionStatus = dataObj.getBoolean("is_subscribed");
                            registration_start_date = dataObj.getString("session_start_date");
                            registration_end_date = dataObj.getString("session_end_date");
                            subscriptionValidity = dataObj.getString("subscription_validity");

//  =====================   Converting date format in dd month name yyyy ===================================
                            String inputStartDate = registration_start_date;
                            String inputEndDate = registration_end_date;
                            // Parse the input date string into a LocalDate object
                            LocalDate inputDateForStart = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                inputDateForStart = LocalDate.parse(inputStartDate);
                            }
                            LocalDate inputDateForEnd = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                inputDateForEnd = LocalDate.parse(inputEndDate);
                            }
                            // Specify the desired output date format
                            DateTimeFormatter outputDateFormatStart = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                outputDateFormatStart = DateTimeFormatter.ofPattern("d MMMM yyyy");
                            }
                            DateTimeFormatter outputDateFormatEnd = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                outputDateFormatEnd = DateTimeFormatter.ofPattern("d MMMM yyyy");
                            }
                            // Format the date according to the desired output format
                            String outputStartDate = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                outputStartDate = inputDateForStart.format(outputDateFormatStart);
                            }
                            String outputEndDate = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                outputEndDate = inputDateForEnd.format(outputDateFormatEnd);
                            }
//  =============================================================================================================
                            binding.liveStartDateEndDate.setText(outputStartDate + " to " + outputEndDate);
                            binding.liveClassTitle.setText(title);
//                            Testing the HTML format document
//                            binding.liveDescription.setText(description);
                            binding.liveDescription.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null);
                            binding.liveClassActualPrice.setText("\u20B9" + ActualPrice);
                            binding.liveClassSellingPrice.setText("\u20B9" + sellingPrice);

                            if (subscriptionStatus) {
                                binding.buyLiveCourseBtn.setVisibility(View.GONE);
                                binding.liveClassActualPrice.setVisibility(View.GONE);
                                binding.liveClassSellingPrice.setVisibility(View.GONE);
                            } else {
                                binding.buyLiveCourseBtn.setVisibility(View.VISIBLE);
                                binding.liveClassActualPrice.setVisibility(View.VISIBLE);
                                binding.liveClassSellingPrice.setVisibility(View.VISIBLE);
                            }

                            Glide.with(LiveDetailsActivity.this).load(image).into(binding.liveCourseImage);

                            JSONObject syllabusObj = dataObj.getJSONObject("syllubas");


                            binding.scheduleClassesName.setText(syllabusObj.getString("title"));
                            // Monday
                            if (syllabusObj.getString("mon_datetime").isEmpty() || syllabusObj.getString("mon_datetime").equals(" ")) {
                                binding.mondayScheduleTime.setText("No Classes on Monday");
                            } else {
                                binding.mondayScheduleTime.setText(syllabusObj.getString("mon_datetime"));
                            }
                            // TuesDay
                            if (syllabusObj.getString("tue_datetime").isEmpty() || syllabusObj.getString("tue_datetime").equals(" ")) {
                                binding.tuesdayScheduleTime.setText("No Classes on Tuesday");

                            } else {
                                binding.tuesdayScheduleTime.setText(syllabusObj.getString("tue_datetime"));
                            }

                            // Wednesday
                            if (syllabusObj.getString("wed_datetime").isEmpty() || syllabusObj.getString("wed_datetime").equals(" ")) {
                                binding.wednesdayScheduleTime.setText("No Classes on Wednesday");
                            } else {
                                binding.wednesdayScheduleTime.setText(syllabusObj.getString("wed_datetime"));
                            }

                            // Thursday
                            if (syllabusObj.getString("thu_datetime").isEmpty() || syllabusObj.getString("thu_datetime").equals(" ")) {
                                binding.thursdayScheduleTime.setText("No Classes on Thursday");
                            } else {
                                binding.thursdayScheduleTime.setText(syllabusObj.getString("thu_datetime"));
                            }


                            // Friday
                            if (syllabusObj.getString("fri_datetime").isEmpty() || syllabusObj.getString("fri_datetime").equals(" ")) {
                                binding.fridayScheduleTime.setText("No Classes on Friday");
                            } else {
                                binding.fridayScheduleTime.setText(syllabusObj.getString("fri_datetime"));
                            }

                            // Saturday
                            if (syllabusObj.getString("sat_datetime").isEmpty() || syllabusObj.getString("sat_datetime").equals(" ")) {
                                binding.saturdayScheduleTime.setText("No Classes on Saturday");
                            } else {
                                binding.saturdayScheduleTime.setText(syllabusObj.getString("sat_datetime"));
                            }

                            // Sunday
                            if (syllabusObj.getString("sun_datetime").isEmpty() || syllabusObj.getString("sun_datetime").equals(" ")) {
                                binding.sundayScheduleTime.setText("No Classes on Sunday");
                            } else {
                                binding.sundayScheduleTime.setText(syllabusObj.getString("sun_datetime"));
                            }

// ========= Button for Demo and Download Schedule if the url is empty then button will not be visible=================
                            String urlVideo = dataObj.getString("video");
                            String videoTitle = dataObj.getString("title");
                            String videoDescription = dataObj.getString("description");
                            //String urlVideo = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4";
                            if (urlVideo == null || urlVideo.isEmpty())
                                binding.btnDemoLiveClass.setVisibility(View.GONE);
                            else {
                                binding.btnDemoLiveClass.setVisibility(View.VISIBLE);
                                binding.btnDemoLiveClass.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        String videoId = extractVideoIdFromEmbedUrl(urlVideo);


                                        Log.e("Click", "Clicked on Demo Button");
                                        try {
                                            Intent i = new Intent(LiveDetailsActivity.this, YouTubeVideoPlayerActivity.class);
                                            i.putExtra("videoId", videoId);
                                            i.putExtra("videoTitle", videoTitle);
                                            i.putExtra("videoDescription", videoDescription);
                                            startActivity(i);
                                        } catch (Exception e) {
                                            Toast.makeText(LiveDetailsActivity.this, "Not worked", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                            String urlOfPdf = dataObj.getString("pdf_schedule");
//                            Toast.makeText(LiveDetailsActivity.this, urlOfPdf, Toast.LENGTH_SHORT).show();
//  ========================= Issue in checking null. If urlOfPdf is null then download button should be gone.
//                            String urlOfPdf = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
                            if (urlOfPdf.isEmpty() || urlOfPdf == "NA") {
                                binding.btnDownloadPdfLiveClass.setVisibility(View.GONE);
                            } else {
                                binding.btnDownloadPdfLiveClass.setVisibility(View.VISIBLE);
                                binding.btnDownloadPdfLiveClass.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        openPdfInBrowser(urlOfPdf);
                                    }
                                });

                            }
//==========================================================================================================================


                            //     itemLiveClassArrayList.clear();

//                                for (int i = 0; i < liveCourses.length(); i++) {
//                                    ItemLiveClass itemLiveCourse = new ItemLiveClass();
//                                    JSONObject courseObj = liveCourses.getJSONObject(i);
//                                    itemLiveCourse.setTitle(courseObj.getString("text"));
//                                    itemLiveClassArrayList.add(itemLiveCourse);
//
//                                }
//
//
//
//
//                            // LinearLayoutManager layoutManager = new LinearLayoutManager(AllLiveClassesAcitivity.this, LinearLayoutManager.HORIZONTAL, false);
//                            LinearLayoutManager layoutManager = new GridLayoutManager(AllLiveClassesAcitivity.this, 2);
//                            binding.liveClassesRecyclarView.setLayoutManager(layoutManager);
//                            itemLiveClassesAdapter = new ItemAllLiveClassesAdapter(itemLiveClassArrayList);
//                            binding.liveClassesRecyclarView.setAdapter(itemLiveClassesAdapter);


                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LiveDetailsActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = LiveDetailsActivity.this.getLayoutInflater();
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
                            Toast.makeText(LiveDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LiveDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
        }
    }

    public class GetSubscriptionDetails extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(LiveDetailsActivity.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(LiveDetailsActivity.this);
            int versionCode = BuildConfig.VERSION_CODE;

//            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
            String param = "sid=" + sid + "&course_id=" + id + "&coupon_code=" + coupon_code_test + "&price=" + sellingPrice + "&course_type=" + "live";


            Log.e("param", param);

            String couponUrl = getApplicationContext().getString(R.string.apiURL) + "checkCouponApplyEligibility";

            JSONObject jsonObject = jsonParser.makeHttpRequest(couponUrl, "POST", param);
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
                            coupon_status = status;
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
                                initial_price_test.setText("\u20b9 " + sellingPrice + "/-");
                                total_gst_test.setText("\u20b9 " + gst + "/-");
                                sub_total_test.setText("\u20b9 " + iniPrice + "/-");
                                coupon_price_test.setText("\u20b9 " + coupon_code_values + "/-");


                            }
//


                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LiveDetailsActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = LiveDetailsActivity.this.getLayoutInflater();
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
                            Log.e("IN FAILURE", status);
                            String message = jsonObject.getString("message");
                            coupon_status = status;
                            coupon_msg = message;


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
                            Toast.makeText(LiveDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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

    class MakeLiveClassSubscription extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);
        String url = getApplicationContext().getString(R.string.apiURL) + "makeCourseSubscription";


        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(LiveDetailsActivity.this);
            String param = "student_id=" + studId + "&course_id=" + id + "&course_type=" + "live" + "&subscription_validity=" + subscriptionValidity + "&order_id=" + orderId + "&coupon_code=" +
                    coupon_code_test + couponCode + "&coupon_value=" + coupon_code_values + "&amount_paid=" + total_price + "&amount_course=" + sellingPrice;

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

                    // String data = jsonObject.getString("data");
//                    Toast.makeText(CourseDetailsActivity.this, "" + data, Toast.LENGTH_SHORT).show();
                    if (true) {
                        //Undermaintance
                        AlertDialog.Builder qdialogBuilder = new AlertDialog.Builder(LiveDetailsActivity.this);
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater qinflater = LiveDetailsActivity.this.getLayoutInflater();
                        View qdialogView = qinflater.inflate(R.layout.congratulations, null);
                        qdialogBuilder.setView(qdialogView);
                        //Alert Dialog Layout work

                        Log.e("Working", "Working");
                        TextView qbtnOK = qdialogView.findViewById(R.id.btnOK);
                        TextView duration_txt = qdialogView.findViewById(R.id.duration_txt);
//
                        TextView underMaintananceContent = qdialogView.findViewById(R.id.underMaintananceContent);

                        underMaintananceContent.setText("Subscribed successfully.");


                        qbtnOK.setText("OK");

                        final AlertDialog qalertDialog = qdialogBuilder.create();
                        qalertDialog.show();
                        qalertDialog.setCanceledOnTouchOutside(false);
                        qbtnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(LiveDetailsActivity.this, MyCoursesActivity.class));

                                qalertDialog.dismiss();
                            }
                        });

                    } else {
                        startActivity(new Intent(LiveDetailsActivity.this, LiveDetailsActivity.class));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Response reached---------------value in 's' variable
        }
    }


    class LogRawData extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);
        String url = getApplicationContext().getString(R.string.apiURL) + "saveTransactionRPay";


        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(LiveDetailsActivity.this);
            String param = "student_id=" + sid + "&order_id=" + orderId + "&amount=" + total_price + "&razorpay_payment_id=" + razorPaymentId + "&status=" + status + "&response=" + jsonRawData;
            Log.e("logParam", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);

            if (jsonObject == null) {
                return "";
            }
            Log.e("Log result >>", jsonObject.toString());
            if (jsonObject != null) {
                Log.e("Log result >>", jsonObject.toString());
                return jsonObject.toString();
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Response reached---------------value in 's' variable
        }
    }


}