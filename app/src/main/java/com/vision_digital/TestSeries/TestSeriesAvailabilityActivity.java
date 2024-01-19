package com.vision_digital.TestSeries;

import static android.content.ContentValues.TAG;
import static com.vision_digital.activities.DashboardActivity.sid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;
import com.vision_digital.TestSeries.model.TestSeriesAvailabilityBundle.ItemTestSeriesAvailabilityAdapter;
import com.vision_digital.TestSeries.model.TestSeriesAvailabilityBundle.ItemTestSeriesAvailabilityList;
import com.vision_digital.helperClasses.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TestSeriesAvailabilityActivity extends AppCompatActivity {

    ImageView btnBack;
    RecyclerView rvTestSeriesAvailability;

    String coursePackageId="";
    String url="";
    ProgressDialog dialog;

    ArrayList<ItemTestSeriesAvailabilityList> itemTestSeriesAvailabilityLists = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_series_availability);

        url=getApplicationContext().getString(R.string.apiURL)+"getPackageCourseTestseriesList";

        coursePackageId=getIntent().getStringExtra("id");

        dialog = new ProgressDialog(TestSeriesAvailabilityActivity.this);
        dialog.setMessage("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        btnBack = findViewById(R.id.btnBack);
        rvTestSeriesAvailability = findViewById(R.id.rvTestSeriesAvailability);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        new  GetCourseDetailsPackage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    class GetCourseDetailsPackage extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(TestSeriesAvailabilityActivity.this);



            Log.e(TAG, "Sub CategoryCourseActivity-----: " + "do in background");

            String param =  "course_package_id="+coursePackageId +"&sid="+sid;

            Log.e(TAG, "param:------ " + param);


            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
            if (jsonObject != null) {
                Log.e("SubCategory------", jsonObject.toString());
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


                            JSONArray jsonArray = jsonObject.getJSONArray("data");


                            if (jsonArray.length() == 0) {

                            } else {

                                itemTestSeriesAvailabilityLists.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ItemTestSeriesAvailabilityList itemSubscribedTestSeries = new ItemTestSeriesAvailabilityList();
                                    JSONObject courseObj = jsonArray.getJSONObject(i);
                                    itemSubscribedTestSeries.setId(String.valueOf(courseObj.getInt("id")));
                                    itemSubscribedTestSeries.setTitle(courseObj.getString("title"));
                                    itemSubscribedTestSeries.setCourseType(courseObj.getString("course_type"));
                                    itemSubscribedTestSeries.setTestSchedulePDFURl(courseObj.getString("testseries_schedule_pdf"));
                                    itemSubscribedTestSeries.setStartTime(courseObj.getString("start_date"));
                                    itemSubscribedTestSeries.setEndTime(courseObj.getString("end_date"));
                                    itemSubscribedTestSeries.setSubscriptionValidity(String.valueOf(courseObj.getInt("subscription_validity")));
                                    itemSubscribedTestSeries.setCategoryName(courseObj.getString("category_name"));
                                    itemSubscribedTestSeries.setCategoryImage(courseObj.getString("category_image"));
                                    itemSubscribedTestSeries.setSub_category_name(courseObj.getString("sub_category_name"));
                                    itemSubscribedTestSeries.setTotal_test_count(String.valueOf(courseObj.getInt("total_test_count")));
                                    // itemSubscribedTestSeries.setShortDesc(courseObj.getString("short_desc"));

                                    itemTestSeriesAvailabilityLists.add(itemSubscribedTestSeries);

                                }

                                //My courses-------------

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TestSeriesAvailabilityActivity.this, RecyclerView.VERTICAL, false);
                                rvTestSeriesAvailability.setLayoutManager(linearLayoutManager);
                                ItemTestSeriesAvailabilityAdapter itemTestSeriesAvailabilityAdapter = new ItemTestSeriesAvailabilityAdapter(TestSeriesAvailabilityActivity.this, itemTestSeriesAvailabilityLists);
                                rvTestSeriesAvailability.setAdapter(itemTestSeriesAvailabilityAdapter);

                            }

                                break;
                                case "maintainance":
                                    //Undermaintance
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TestSeriesAvailabilityActivity.this);
                                    // ...Irrelevant code for customizing the buttons and title
                                    LayoutInflater inflater = TestSeriesAvailabilityActivity.this.getLayoutInflater();
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
                                    Toast.makeText(TestSeriesAvailabilityActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(TestSeriesAvailabilityActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                                    break;

                            }



                }
            catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

            //Response reached---------------value in 's' variable


}