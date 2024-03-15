package com.vision_classes.transaction;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vision_classes.BuildConfig;
import com.vision_classes.R;
import com.vision_classes.helperClasses.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransactionHistory extends AppCompatActivity {
    String transactionUrl="";
    public static String studId;
    ArrayList<ItemTansaction> tansactionArrayList = new ArrayList<>();
    ItemTransactionAdapter itemTransactionAdapter;
    RecyclerView transactionRecycler;
    ImageView backBtn;
    TextView tvNoTransactionHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        transactionUrl = getApplicationContext().getString(R.string.apiURL) + "order_details_list_ipay";
        studId  = getIntent().getStringExtra("student_id");
        transactionRecycler = findViewById(R.id.transactionRecycler);
        backBtn = findViewById(R.id.backBtn);
        tvNoTransactionHistory = findViewById(R.id.tvNoTransactionHistory);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class GetTransactionList extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(TransactionHistory.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(TransactionHistory.this);
            int versionCode = BuildConfig.VERSION_CODE;
//            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
            String param = "student_id=" + studId;


            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(transactionUrl, "POST", param);
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
//                    final JSONObject dataObj = jsonObject.getJSONObject("data");

                    switch (status) {
                        case "success":
                            //Running Fine

                            tansactionArrayList.clear();
                            JSONArray transactionContent = jsonObject.getJSONArray("data");

                            if (transactionContent.length()==0){
                                tvNoTransactionHistory.setVisibility(View.VISIBLE);
                                transactionRecycler.setVisibility(View.GONE);
                            }else {
                                tvNoTransactionHistory.setVisibility(View.GONE);
                                transactionRecycler.setVisibility(View.VISIBLE);

                                for (int i = 0; i < transactionContent.length(); i++) {
//                                JSONObject contentSubscribe = courseContent.getJSONObject(i);
                                    ItemTansaction transactionHistory = new ItemTansaction();
                                    JSONObject transactionObject = transactionContent.getJSONObject(i);

                                    transactionHistory.setOrderId(transactionObject.getString("order_id"));
                                    // transactionHistory.setStatus(transactionObject.getString("cos_status"));
                                    transactionHistory.setStatus(transactionObject.getString("response_message"));
                                    transactionHistory.setAmount(transactionObject.getString("paid_amount"));
                                    transactionHistory.setId(transactionObject.getString("id"));
                                    transactionHistory.setDate(transactionObject.getString("created_at"));
                                    tansactionArrayList.add(transactionHistory);

                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(TransactionHistory.this, LinearLayoutManager.VERTICAL, false);
                                transactionRecycler.setLayoutManager(layoutManager);
                                itemTransactionAdapter = new ItemTransactionAdapter(tansactionArrayList);
                                transactionRecycler.setAdapter(itemTransactionAdapter);
                            }

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TransactionHistory.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = TransactionHistory.this.getLayoutInflater();
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
                            Toast.makeText(TransactionHistory.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(TransactionHistory.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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
    protected void onPostResume() {
        super.onPostResume();
        new GetTransactionList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}