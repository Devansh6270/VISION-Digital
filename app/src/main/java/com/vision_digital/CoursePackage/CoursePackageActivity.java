package com.vision_digital.CoursePackage;
import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chaos.view.BuildConfig;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.vision_digital.R;
import com.vision_digital.databinding.ActivityCoursePackageBinding;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.CoursePackage.ItemTypesPackage;
import com.vision_digital.model.CoursePackage.ItemTypesPackageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class CoursePackageActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    ActivityCoursePackageBinding binding;
    ProgressDialog dialog;
    String getPackageDetailsApi="";
    String packageId;
    String packageLogo;
    String packageName;

    String image="";
    String price ="";
    String ownerName="";
    String ownerImage="";
    String description="";
    String buyButton="";

    ArrayList<ItemTypesPackage>  coursesTypeList= new ArrayList<>();
    ArrayList<ItemTypesPackage>  notesTypeList= new ArrayList<>();
    ArrayList<ItemTypesPackage>  testTypeList= new ArrayList<>();
    ArrayList<ItemTypesPackage>  testSeriesTypeList= new ArrayList<>();
    ArrayList<ItemTypesPackage>  liveTypeList= new ArrayList<>();



    int sid = 0;

    // subscription popup widgets
    TextView totalPriceTest, total_gst_test, sub_total_test, initial_price_test, btnApplyCoupon, couponMsgTest, coupon_price_test, duration;
    EditText coupon_code_edt_test;
    String coupon_code_test = "";
    LinearLayout priceLayout, coupon_value_layout_linear_test, sub_total_linear_test, gst_linear;
    String total_price = "";
    TextView qbtnOK;
    String coupon_code_values = "";
    String coupon_status = "";
    String gst = "";
    String iniPrice = "";
    String coupon_msg = "";

    //Payment-------------------
    // Paytm------------------
    String mid = "IxDAFe91483847846332"; //Rajit
    //    String mid = "TLvNGP42879274127615";
//                                String marchentKey = "G&%UEPNTnz6&4cti";
    String marchentKey = "1BGSS5qX5OuGWITA"; //Rajit

    String timeDuration;
    String testId = "";
    String desc = "";
    String subscription_status = "";

    //Payment Work-----------------------------------

    String uid = "";
    String orderID;
    String testType = "";
    String logData;
    String gateway_name = "paytm";
    String timeStamp;
    JSONObject jsonRawData = new JSONObject();
    int activeOrderCount = 0;

    String startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=DataBindingUtil.setContentView(this,R.layout.activity_course_package);
        //setContentView(R.layout.activity_course_package);

        dialog = new ProgressDialog(CoursePackageActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading");
        dialog.show();

        packageId = getIntent().getStringExtra("id");
        packageLogo = getIntent().getStringExtra("image");
       // fromActivity = getIntent().getStringExtra("fromActivity");
        Log.e("Package ID", packageId);

        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        sid = studDetails.getInt("sid", 0);
        SharedPreferences uidDetails = getSharedPreferences("CNBUID", MODE_PRIVATE);
        uid = uidDetails.getString("uid", "NO_NAME");

        getPackageDetailsApi=getApplicationContext().getString(R.string.apiURL)+"getProductPackageDetails";

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   onBackPressed();
                                               }
                                           });

        binding.sharButtonCourseDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Click to learn: " + packageName + " \n" +

                        "\nhttps://dlink.chalksnboard.com/course" + packageId;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Zeal");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

           binding.tabCourseLayout.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   binding.onTabCourses.setVisibility(View.VISIBLE);
                   binding.onTabTestSeries.setVisibility(View.GONE);
                   binding.onTabTest.setVisibility(View.GONE);
                   binding.onTabLive.setVisibility(View.GONE);
                   binding.onTabNote.setVisibility(View.GONE);

                   // Recycler View
                   binding.recyclarViewCourse.setVisibility(View.VISIBLE);
                   binding.recyclarViewTest.setVisibility(View.GONE);
                   binding.recyclarViewTestSeries.setVisibility(View.GONE);
                   binding.recyclarViewLive.setVisibility(View.GONE);
                   binding.recyclarViewNotes.setVisibility(View.GONE);

                      // TAB COLOR MANAGE
                   binding.colorTabText.setTextColor(Color.parseColor("#393E59"));
                   binding.colorTabTest.setTextColor(Color.parseColor("#6E6E6E"));
                   binding.colorTabTestSeries.setTextColor(Color.parseColor("#6E6E6E"));
                   binding.colorTabLive.setTextColor(Color.parseColor("#6E6E6E"));
                   binding.colorTabNotes.setTextColor(Color.parseColor("#6E6E6E"));


               }
           });
        binding.tabTestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.onTabTest.setVisibility(View.VISIBLE);
                binding.onTabTestSeries.setVisibility(View.GONE);
                binding.onTabCourses.setVisibility(View.GONE);
                binding.onTabLive.setVisibility(View.GONE);
                binding.onTabNote.setVisibility(View.GONE);

                // Recycler View
                binding.recyclarViewTest.setVisibility(View.VISIBLE);
                binding.recyclarViewCourse.setVisibility(View.GONE);
                binding.recyclarViewTestSeries.setVisibility(View.GONE);
                binding.recyclarViewLive.setVisibility(View.GONE);
                binding.recyclarViewNotes.setVisibility(View.GONE);

                // TAB COLOR MANAGE
                binding.colorTabText.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabTest.setTextColor(Color.parseColor("#393E59"));
                binding.colorTabTestSeries.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabLive.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabNotes.setTextColor(Color.parseColor("#6E6E6E"));
            }
        });

        binding.tabTestSeriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.onTabTestSeries.setVisibility(View.VISIBLE);
                binding.onTabTest.setVisibility(View.GONE);
                binding.onTabCourses.setVisibility(View.GONE);
                binding.onTabLive.setVisibility(View.GONE);
                binding.onTabNote.setVisibility(View.GONE);

                // Recycler View
                binding.recyclarViewTestSeries.setVisibility(View.VISIBLE);
                binding.recyclarViewCourse.setVisibility(View.GONE);
                binding.recyclarViewTest.setVisibility(View.GONE);
                binding.recyclarViewLive.setVisibility(View.GONE);
                binding.recyclarViewNotes.setVisibility(View.GONE);

                // TAB COLOR MANAGE
                binding.colorTabText.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabTest.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabTestSeries.setTextColor(Color.parseColor("#393E59"));
                binding.colorTabLive.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabNotes.setTextColor(Color.parseColor("#6E6E6E"));
            }
        });

        binding.tabLiveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.onTabLive.setVisibility(View.VISIBLE);
                binding.onTabTest.setVisibility(View.GONE);
                binding.onTabCourses.setVisibility(View.GONE);
                binding.onTabTestSeries.setVisibility(View.GONE);
                binding.onTabNote.setVisibility(View.GONE);

                // Recycler View
                binding.recyclarViewLive.setVisibility(View.VISIBLE);
                binding.recyclarViewCourse.setVisibility(View.GONE);
                binding.recyclarViewTest.setVisibility(View.GONE);
                binding.recyclarViewTestSeries.setVisibility(View.GONE);
                binding.recyclarViewNotes.setVisibility(View.GONE);

                // TAB COLOR MANAGE
                binding.colorTabText.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabTest.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabTestSeries.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabLive.setTextColor(Color.parseColor("#393E59"));
                binding.colorTabNotes.setTextColor(Color.parseColor("#6E6E6E"));
            }
        });

        binding.tabNotesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.onTabNote.setVisibility(View.VISIBLE);
                binding.onTabTest.setVisibility(View.GONE);
                binding.onTabCourses.setVisibility(View.GONE);
                binding.onTabTestSeries.setVisibility(View.GONE);
                binding.onTabLive.setVisibility(View.GONE);

                // Recycler View
                binding.recyclarViewNotes.setVisibility(View.VISIBLE);
                binding.recyclarViewCourse.setVisibility(View.GONE);
                binding.recyclarViewTest.setVisibility(View.GONE);
                binding.recyclarViewTestSeries.setVisibility(View.GONE);
                binding.recyclarViewLive.setVisibility(View.GONE);

                // TAB COLOR MANAGE
                binding.colorTabText.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabTest.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabTestSeries.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabLive.setTextColor(Color.parseColor("#6E6E6E"));
                binding.colorTabNotes.setTextColor(Color.parseColor("#393E59"));
            }
        });

        binding.readMoreLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CoursePackageActivity.this);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = CoursePackageActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.pop_up_description, null);
                dialogBuilder.setView(dialogView);
                //Alert Dialog Layout work
                TextView descriptionText = dialogView.findViewById(R.id.description);
                descriptionText.setText(Html.fromHtml(description));

                Button btnOK = dialogView.findViewById(R.id.okButton);

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
                dialog.dismiss();
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                          alertDialog.dismiss();
                    }
                });
            }
        });

        binding.subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    total_price = price;


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CoursePackageActivity.this);
                    // ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = CoursePackageActivity.this.getLayoutInflater();
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
//                    duration_layout = dialogView.findViewById(R.id.duration_layout);
//                    duration_layout.setVisibility(View.GONE);

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

                            new GetCouponCodeDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
                                new MakePackageSubscription().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            } else {
                                new SendUserDetailTOServerdd().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }

                            alertDialog.dismiss();
                        }
                    });


                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(false);
                }



        });


        //Paytm Payment-----------------------------------------
        String alpha = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        Long tsLong = System.currentTimeMillis() / 1000;
        timeStamp = tsLong.toString();
//        orderID = alpha.charAt(random.nextInt(26)) + uid + alpha.charAt(random.nextInt(26)) + timeStamp;
        orderID = "OD" + sid + "S" + timeStamp;

      new getPackagesDetails().execute();
    }





    class SendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(CoursePackageActivity.this);
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
            JSONParser jsonParser = new JSONParser(CoursePackageActivity.this);
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
            Service.startPaymentTransaction(CoursePackageActivity.this, true, true,
                    CoursePackageActivity.this);
        }
    }


    class getPackagesDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(CoursePackageActivity.this);
            // int versionCode = BuildConfig.VERSION_CODE;


            String param = "sid=" + sid +"&package_id="+packageId;
            Log.e(TAG, "param: " + param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(getPackageDetailsApi, "POST", param);
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

                            packageName=dataObj.getString("title");
                            image=dataObj.getString("image");
                            ownerImage=dataObj.getString("owner_image");
                            ownerName=dataObj.getString("owner_name");
                            description=dataObj.getString("description");
                            price= String.valueOf(dataObj.getInt("price"));
                            buyButton= String.valueOf(dataObj.getInt("buy_button"));

                            binding.courseName.setText(packageName);
                            binding.coursePrice.setText("\u20B9"+price+" /-");
                            binding.courseDes.setText(Html.fromHtml(description));
                            Glide.with(CoursePackageActivity.this).load(image)
                                    .into(binding.bannerImage);

                            Glide.with(CoursePackageActivity.this).load(ownerImage)
                                    .into(binding.profileImage);

                            JSONArray productList =dataObj.getJSONArray("package_list");
                            if (buyButton.equals("0")){
                                binding.subscribeBtn.setVisibility(View.GONE);
                                binding.coursePrice.setVisibility(View.GONE);
                            }else {
                                binding.subscribeBtn.setVisibility(View.VISIBLE);
                                binding.coursePrice.setVisibility(View.VISIBLE);
                            }
                            if (productList.length()==0){

                            }else{

                                coursesTypeList.clear();
                                testTypeList.clear();
                                testSeriesTypeList.clear();
                                notesTypeList.clear();
                                liveTypeList.clear();

                                for (int i=0;i<productList.length();i++){
                                    ItemTypesPackage itemPackageList = new ItemTypesPackage();
                                    JSONObject packageJSONObject = productList.getJSONObject(i);

                                    itemPackageList.setProductId(String.valueOf(packageJSONObject.getInt("product_id")));
                                    itemPackageList.setProductType(packageJSONObject.getString("product_type"));
                                    itemPackageList.setProductName(packageJSONObject.getString("product_name"));
                                    itemPackageList.setProductImage(packageJSONObject.getString("product_image"));

                                    String productType=packageJSONObject.getString("product_type");
                                    Log.e("PRODUCT TYPE",productType);
                                   if(productType.equals("course")){
                                       Log.e("LOG","IN COURSE");
                                       coursesTypeList.add(itemPackageList);
                                   } else if (productType.equals("test")) {
                                       Log.e("LOG","IN TEST");
                                       testTypeList.add(itemPackageList);
                                   } else if (productType.equals("testseries")) {
                                       Log.e("LOG","IN TEST SERIES");
                                       testSeriesTypeList.add(itemPackageList);
                                   } else if (productType.equals("notes")) {
                                       Log.e("LOG","IN NOTES");
                                      notesTypeList.add(itemPackageList);
                                   }else if (productType.equals("live")){
                                       liveTypeList.add(itemPackageList);
                                   }

                                    if (buyButton.equals("0")){
                                        itemPackageList.setStatus("unlock");
                                    }else {
                                        itemPackageList.setStatus("lock");
                                    }

                                }
                                    // Course
                                if(coursesTypeList.size()==0){
                                    binding.tabCourseLayout.setVisibility(View.GONE);
                                    binding.recyclarViewCourse.setVisibility(View.GONE);
                                }else{
                                    // Default Tab
                                    binding.tabCourseLayout.setVisibility(View.VISIBLE);
                                    binding.recyclarViewCourse.setVisibility(View.VISIBLE);
                                }
                                // Test
                                if(testTypeList.size()==0){
                                    Log.e("LOG","IN TEST LAYOUT GONE");
                                    binding.tabTestLayout.setVisibility(View.GONE);
                                }else{
                                    Log.e("LOG","IN TEST LAYOUT VISIBLE");
                                    binding.tabTestLayout.setVisibility(View.VISIBLE);
                                }

                                // Test Series
                                if(testSeriesTypeList.size()==0){
                                    binding.tabTestSeriesLayout.setVisibility(View.GONE);
                                }else{
                                    binding.tabTestSeriesLayout.setVisibility(View.VISIBLE);
                                }

                                // Live
                                if(liveTypeList.size()==0){
                                    binding.tabLiveLayout.setVisibility(View.GONE);
                                    binding.recyclarViewLive.setVisibility(View.GONE);
                                }else{
                                    binding.tabLiveLayout.setVisibility(View.VISIBLE);
                                }
                                // NOTES
                                if(notesTypeList.size()==0){
                                    binding.tabNotesLayout.setVisibility(View.GONE);
                                }else{
                                    binding.tabNotesLayout.setVisibility(View.VISIBLE);
                                }



                                LinearLayoutManager linearLayoutManagerPackage = new LinearLayoutManager(CoursePackageActivity.this,
                                        LinearLayoutManager.VERTICAL, false);
                                LinearLayoutManager linearLayoutManagerTest = new LinearLayoutManager(CoursePackageActivity.this,
                                        LinearLayoutManager.VERTICAL, false);
                                LinearLayoutManager linearLayoutManagerTestSeries = new LinearLayoutManager(CoursePackageActivity.this,
                                        LinearLayoutManager.VERTICAL, false);
                                LinearLayoutManager linearLayoutManagerLive = new LinearLayoutManager(CoursePackageActivity.this,
                                        LinearLayoutManager.VERTICAL, false);
                                LinearLayoutManager linearLayoutManagerNotes = new LinearLayoutManager(CoursePackageActivity.this,
                                        LinearLayoutManager.VERTICAL, false);

                                binding.recyclarViewCourse.setLayoutManager(linearLayoutManagerPackage);
                                binding.recyclarViewTest.setLayoutManager(linearLayoutManagerTest);
                                binding.recyclarViewTestSeries.setLayoutManager(linearLayoutManagerTestSeries);
                                binding.recyclarViewLive.setLayoutManager(linearLayoutManagerLive);
                                binding.recyclarViewNotes.setLayoutManager(linearLayoutManagerNotes);

                                // Course
                                ItemTypesPackageAdapter itemPackageAdapter = new ItemTypesPackageAdapter(coursesTypeList,CoursePackageActivity.this);
                                binding.recyclarViewCourse.setAdapter(itemPackageAdapter);


                                    // Test
                                ItemTypesPackageAdapter itemTestPackageAdapter = new ItemTypesPackageAdapter(testTypeList,CoursePackageActivity.this);
                                binding.recyclarViewTest.setAdapter(itemTestPackageAdapter);

                                // Test Series
                                ItemTypesPackageAdapter itemTestSeriesPackageAdapter = new ItemTypesPackageAdapter(testSeriesTypeList,CoursePackageActivity.this);
                                binding.recyclarViewTestSeries.setAdapter(itemTestSeriesPackageAdapter);

                                // Live
                                ItemTypesPackageAdapter itemLivePackageAdapter = new ItemTypesPackageAdapter(liveTypeList,CoursePackageActivity.this);
                                binding.recyclarViewLive.setAdapter(itemLivePackageAdapter);
                            }

                            break;
                        case "maintainance":

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CoursePackageActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = CoursePackageActivity.this.getLayoutInflater();
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
                            Toast.makeText(CoursePackageActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(CoursePackageActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MakePackageSubscription extends AsyncTask<String, Void, String> {

        String url = getApplicationContext().getString(R.string.apiURL)+"makePackageSubscription";


        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(CoursePackageActivity.this);
            String param = "sid=" + sid + "&package_id=" + packageId + "&subscription_month=" + 1 + "&order_id=" + orderID +
                    "&coupon_code=" +
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

                    String status = jsonObject.getString("status");
                    String message =jsonObject.getString("message");
//                    Toast.makeText(CourseDetailsActivity.this, "" + data, Toast.LENGTH_SHORT).show();
                    if (status.equals("success")) {
                        //Undermaintance
                        AlertDialog.Builder qdialogBuilder = new AlertDialog.Builder(CoursePackageActivity.this);
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater qinflater = CoursePackageActivity.this.getLayoutInflater();
                        View qdialogView = qinflater.inflate(R.layout.congratulations, null);

                        qdialogBuilder.setView(qdialogView);
                        //Alert Dialog Layout work

                        Log.e("Working", "Working");
                        TextView btnOK = qdialogView.findViewById(R.id.btnOK);
                        TextView messageContent = qdialogView.findViewById(R.id.underMaintananceContent);
                        TextView duration_txt = qdialogView.findViewById(R.id.duration_txt);

                         messageContent.setText("Subscribed Successfully");
                        duration_txt.setText(timeDuration);
                        btnOK.setText("OK");

                        final AlertDialog qalertDialog = qdialogBuilder.create();
                        qalertDialog.show();
                        qalertDialog.setCanceledOnTouchOutside(false);
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                    qalertDialog.dismiss();
                                    finish();
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

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network gone", Toast.LENGTH_SHORT).show();
//        payViaPaytmBtn.setEnabled(true);

    }

    @Override
    public void onErrorProceed(String s) {

    }

    @Override
    public void clientAuthenticationFailed(String s) {


    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  " + s);
//        payViaPaytmBtn.setEnabled(true);

    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true " + s + "  s1 " + s1);
//        payViaPaytmBtn.setEnabled(true);

    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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

            new MakePackageSubscription().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new getPackagesDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            new LogRawData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }

    }


    class LogRawData extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);
        String url = getApplicationContext().getString(R.string.apiURL)+"logTransactions"; //"https://chalksnboard.com/api/v3/logTransactions";


        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(CoursePackageActivity.this);
            String param = "student_id=" + sid + "&course_id=" + packageId + "&uid=" + "" + "&gateway_response=" + jsonRawData + "&gateway_name=" + gateway_name;


            Log.e("TAG LOG DATA", "doInBackground: " + param);
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

    public class GetCouponCodeDetails extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(CoursePackageActivity.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(CoursePackageActivity.this);
            int versionCode = BuildConfig.VERSION_CODE;

//            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
            String param = "sid=" + sid + "&course_id=" + packageId + "&course_type="+"package"+ "&subscription_month=" + "1" + "&coupon_code=" + coupon_code_test + "&price=" + price;


            Log.e("param", param);
            String  applyCouponCodeCheck= getApplicationContext().getString(R.string.apiURL)+"apply_coupon_code_check";

            JSONObject jsonObject = jsonParser.makeHttpRequest(applyCouponCodeCheck, "POST", param);
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
                            String totalprice = dataObj.getString("total_amount");
                            coupon_msg = dataObj.getString("message");
                            iniPrice = dataObj.getString("sub_total");
                            gst = dataObj.getString("gst_amount");
                            coupon_status = status;
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
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CoursePackageActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = CoursePackageActivity.this.getLayoutInflater();
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
                            Toast.makeText(CoursePackageActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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