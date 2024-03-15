package com.vision_classes.TestSeries;



import static com.vision_classes.activities.DashboardActivity.sid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_classes.R;
import com.vision_classes.TestSeries.model.SubjectivePaper.ItemSubjectivePaperList;
import com.vision_classes.TestSeries.model.allTestSeries.ItemAllTestPageAdapter;
import com.vision_classes.TestSeries.model.allTestSeries.ItemAllTestPageList;
//import com.vision_digital.activities.payment.PaymentActivity;
import com.vision_classes.helperClasses.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllTestPageActivity extends AppCompatActivity {


    TextView tvEntranceExamName, tvNoOFTest, tvCoursePrice, tvStartDate, tvEndDate, tvStart;

    WebView tvDescription;
    TextView tvTestName, tvTotalQuestions, tvTotalTime, tvTotalMarks, tvResult;
    ImageView ivBannerImage, ivLogo;
    RecyclerView rvTestSeriesList, rvSubjectivePaper;
    ImageView btnBack;
    ProgressDialog dialog;
    NestedScrollView nsvObjectivePaper, nsvSubjectivePaper;
    RadioButton rbObjectivePaper, rbSubjectivePaper;

    String subscriptionValidity="";
    public static String testSeriesId = "";
    ArrayList<ItemAllTestPageList> itemAllTestPageLists = new ArrayList<>();
    ArrayList<ItemSubjectivePaperList> itemSubjectivePaperLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_test_page);

        dialog = new ProgressDialog(AllTestPageActivity.this);
        dialog.setMessage("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        testSeriesId = getIntent().getStringExtra("id");
        subscriptionValidity = getIntent().getStringExtra("subscriptionValidity");

        btnBack = findViewById(R.id.btnBack);
        rvTestSeriesList = findViewById(R.id.rvTestSeriesList);

        tvEntranceExamName = findViewById(R.id.tvEntranceExamName);
        tvNoOFTest = findViewById(R.id.tvNumberOfTest);
        tvCoursePrice = findViewById(R.id.tvCoursePrice);
        ivBannerImage = findViewById(R.id.ivBannerImage);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvDescription = findViewById(R.id.tvDescription);
        tvResult = findViewById(R.id.tvBuyNow);
        nsvObjectivePaper = findViewById(R.id.nsvObjectivePaper);

//   ======= Item All Test Page Id's are below =============================
        tvTestName = rvTestSeriesList.findViewById(R.id.tvTestName);
        tvTotalQuestions = rvTestSeriesList.findViewById(R.id.tvTotalQuestion);
        tvTotalTime = rvTestSeriesList.findViewById(R.id.tvTotalTime);
        tvTotalMarks = rvTestSeriesList.findViewById(R.id.tvTotalMarks);
        tvStart = rvTestSeriesList.findViewById(R.id.tvStart);


        new GetDataForAllTestSeries().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });



    }

    private String convertSecondsToMinutes(int seconds) {
        int minutes = seconds / 60;

        return minutes + " min | ";
    }

    class GetDataForAllTestSeries extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser(AllTestPageActivity.this);

            // studId = DashboardActivity.sid;
//            int mobile = Integer.parseInt(binding.etEnterMobileNumber.getText().toString());
            String id = getIntent().getStringExtra("id");
            String param = "sid=" + sid + "&testseries_id=" + testSeriesId;
            Log.e("TAG", "Parameters-------" + param);
            String url = getApplicationContext().getString(R.string.apiURL) + "getTestSeriesDetails";

            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
            if (jsonObject != null) {
                //  Toast.makeText(ForgetPassword.this, "API is working", Toast.LENGTH_SHORT).show();
                return jsonObject.toString();
            } else {
                // Toast.makeText(ForgetPassword.this, "Problem is API hitting", Toast.LENGTH_SHORT).show();
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

                    Log.e("Result of Test : ", s);

                    //Do work-----------------------------
                    Boolean status = jsonObject.getBoolean("status");
//
                    if (status) {
                        dialog.dismiss();

                        JSONObject dataObject = jsonObject.getJSONObject("data");

                        String idMain = String.valueOf(dataObject.getInt("id"));

                        String title = dataObject.getString("title");
                        String NoOFTest = String.valueOf(dataObject.getInt("test_count"));
                        String imageUrl = dataObject.getString("image");
                        String sellingPrice = String.valueOf(dataObject.getInt("selling_price"));
                        String startDate = dataObject.getString("start_date");
                        String endDate = dataObject.getString("end_date");
                        String description = dataObject.getString("description");
                        String isSubscribed = dataObject.getString("buy_status");

                        Log.e("BUY STATUS", isSubscribed);
                        if (isSubscribed.equals("1")){
                            tvResult.setVisibility(View.GONE);
                            tvCoursePrice.setVisibility(View.GONE);
                        } else if (isSubscribed.equals("0")){
                            tvResult.setVisibility(View.VISIBLE);
                            tvCoursePrice.setVisibility(View.VISIBLE);
                        }

                        tvResult.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Intent i = new Intent(AllTestPageActivity.this, PaymentActivity.class);
//                                i.putExtra("id", idMain);
//                                i.putExtra("price", sellingPrice);
//                                i.putExtra("title",title);
//                                i.putExtra("subscriptionValidity",subscriptionValidity);
//                                i.putExtra("courseType","testseries");
//                                startActivity(i);
                            }
                        });

                        tvEntranceExamName.setText(title);
                        tvNoOFTest.setText(NoOFTest);
                        tvCoursePrice.setText("\u20B9" + sellingPrice);
                        Glide.with(AllTestPageActivity.this).load(imageUrl).into(ivBannerImage);
                        tvStartDate.setText(startDate);
                        tvEndDate.setText(endDate);
                        tvDescription.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null);
                        //tvDescription.setText(description);

//===================== OBJECTIVE PAPER SECTION OF TEST SERIES LIST =============================================================

                        JSONArray testDetails = dataObject.getJSONArray("testList");

                        for (int i = 0; i < testDetails.length(); i++) {
                            ItemAllTestPageList itemPageList = new ItemAllTestPageList();
                            JSONObject testObj = testDetails.getJSONObject(i);
                            itemPageList.setTestName(testObj.getString("title"));
                            itemPageList.setTotalQuestions(testObj.getInt("total_question") + " Questions | ");

                            String timeInMinute = convertSecondsToMinutes(testObj.getInt("duration"));
//                            itemPageList.setTotalTime(testObj.getString("duration") + " Time | ");
                            itemPageList.setTotalTime(timeInMinute);
                            itemPageList.setTotalMarks(testObj.getString("count_ques_marks") + " Marks");
                            itemPageList.setResult(testObj.getString("buy_text"));
                            itemPageList.setTestListId(testObj.getString("id"));
                            itemPageList.setLock(testObj.getString("lock"));
                            itemPageList.setCategory_id(testObj.getString("category_id"));
                            itemPageList.setBuy_button(testObj.getString("buy_button"));
                            itemPageList.setBuy_status(String.valueOf(testObj.getInt("buy_status")));
                            itemPageList.setStartTimeTestList(testObj.getString("start_at"));
                            itemPageList.setEndTimeTestList(testObj.getString("end_at"));

                            itemAllTestPageLists.add(itemPageList);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllTestPageActivity.this, RecyclerView.VERTICAL, false);
                        rvTestSeriesList.setLayoutManager(linearLayoutManager);


                        ItemAllTestPageAdapter itemAllTestPageAdapter = new ItemAllTestPageAdapter(AllTestPageActivity.this, itemAllTestPageLists);
                        rvTestSeriesList.setAdapter(itemAllTestPageAdapter);



                        //
                    } else if (!status) {
                        Toast.makeText(AllTestPageActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AllTestPageActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}