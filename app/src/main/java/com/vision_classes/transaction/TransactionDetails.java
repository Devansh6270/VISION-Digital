package com.vision_classes.transaction;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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
import com.vision_classes.transaction.detailsModel.ItemDetails;
import com.vision_classes.transaction.detailsModel.ItemDetailsAdapter;
import com.vision_classes.transaction.testTransaction.ItemTestTransaction;
import com.vision_classes.transaction.testTransaction.ItemTestTransactionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TransactionDetails extends AppCompatActivity {

    ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
    ItemDetailsAdapter itemDetailsAdapter;
    ArrayList<ItemTestTransaction> itemTestTransactionArrayList = new ArrayList<>();
    ItemTestTransactionAdapter itemTestTransactionAdapter;


    RecyclerView transaction_details_recycler, test_transaction_recycler;
    TextView txtOrderId, txtStatus, txtTranDate, txtCouponValue, txtPaymentMode, txtPrice, txtGst, txtTotal;
    public static String transactionDetailsUrl, sid, orderId;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtOrderId = findViewById(R.id.txtOrderId);
        txtStatus = findViewById(R.id.txtStatus);
        txtTranDate = findViewById(R.id.txtTranDate);
        txtCouponValue = findViewById(R.id.txtCouponValue);
        txtPaymentMode = findViewById(R.id.txtPaymentMode);
        txtPrice = findViewById(R.id.txtPrice);
        txtGst = findViewById(R.id.txtGst);
        txtTotal = findViewById(R.id.txtTotal);
        backBtn = findViewById(R.id.backBtn);

        transaction_details_recycler = findViewById(R.id.transaction_details_recycler);
        test_transaction_recycler = findViewById(R.id.test_transaction_recycler);

        transactionDetailsUrl = getApplicationContext().getString(R.string.apiURL) + "order_details_ipay";
        sid = getIntent().getStringExtra("sid");
        orderId = getIntent().getStringExtra("order_id");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public class GetTransactionDetails extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(TransactionDetails.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(TransactionDetails.this);
            int versionCode = BuildConfig.VERSION_CODE;
//            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
            String param = "student_id=" + sid + "&order_id=" + orderId;


            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(transactionDetailsUrl, "POST", param);
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
                    JSONObject dataObj = jsonObject.getJSONObject("data");

                    switch (status) {
                        case "success":
                            //Running Fine

                            itemDetailsArrayList.clear();
                            itemTestTransactionArrayList.clear();
                            JSONArray transactiondetailsContent = dataObj.getJSONArray("course_subscription_details");
                            for (int i = 0; i < transactiondetailsContent.length(); i++) {
//                                JSONObject contentSubscribe = courseContent.getJSONObject(i);
                                ItemDetails itemDetails = new ItemDetails();
                                JSONObject transactionDetailsObject = transactiondetailsContent.getJSONObject(i);

                                itemDetails.setCourseId(transactionDetailsObject.getString("course_id"));
                                itemDetails.setCourseStatus(transactionDetailsObject.getString("status"));
                                itemDetails.setAccessibleDate(transactionDetailsObject.getString("expiration_date"));
                                itemDetails.setImageUrl(transactionDetailsObject.getString("course_image"));
                                itemDetails.setSubscriptionDate(transactionDetailsObject.getString("subscription_date"));
                                itemDetails.setSubscribedMonth(transactionDetailsObject.getString("month_subscribed"));
                                itemDetails.setLastRenewedDate(transactionDetailsObject.getString("last_renewed_date"));
                                itemDetails.setTitle(transactionDetailsObject.getString("title"));

                                itemDetailsArrayList.add(itemDetails);

                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(TransactionDetails.this, LinearLayoutManager.VERTICAL, false);
                            transaction_details_recycler.setLayoutManager(layoutManager);
                            itemDetailsAdapter = new ItemDetailsAdapter(itemDetailsArrayList);
                            transaction_details_recycler.setAdapter(itemDetailsAdapter);

                            JSONArray transactionTesdetailsContent = dataObj.getJSONArray("test_subscription_details");
                            for (int i = 0; i < transactionTesdetailsContent.length(); i++) {
//                                JSONObject contentSubscribe = courseContent.getJSONObject(i);
                                ItemTestTransaction itemTestTransaction = new ItemTestTransaction();
                                JSONObject transactionTestDetailsObject = transactionTesdetailsContent.getJSONObject(i);

                                itemTestTransaction.setTestId(transactionTestDetailsObject.getString("test_id"));
                                itemTestTransaction.setTestSubsDate(transactionTestDetailsObject.getString("test_subscription_date"));
                                itemTestTransaction.setStatus(transactionTestDetailsObject.getString("status"));
                                itemTestTransaction.setTitle(transactionTestDetailsObject.getString("title"));
                                itemTestTransaction.setImage(transactionTestDetailsObject.getString("image"));
                                itemTestTransaction.setStartAt(transactionTestDetailsObject.getString("start_at"));

                                Log.e("test_name", transactionTestDetailsObject.getString("title"));

                                itemTestTransactionArrayList.add(itemTestTransaction);

                            }
                            LinearLayoutManager layoutManagerTwo = new LinearLayoutManager(TransactionDetails.this, LinearLayoutManager.VERTICAL, false);
                            test_transaction_recycler.setLayoutManager(layoutManagerTwo);
                            itemTestTransactionAdapter = new ItemTestTransactionAdapter(itemTestTransactionArrayList);
                            test_transaction_recycler.setAdapter(itemTestTransactionAdapter);


                            JSONObject transdetailsContent = dataObj.getJSONObject("orders_details");

                            txtOrderId.setText(transdetailsContent.getString("order_id"));
                            txtPrice.setText("Rs." + transdetailsContent.getString("actual_amount"));
                            txtCouponValue.setText("Rs." + transdetailsContent.getString("coupon_value"));
                            txtGst.setText("Rs." + transdetailsContent.getString("gst_amount"));
                            txtStatus.setText(transdetailsContent.getString("response_message"));
                            txtTotal.setText("Rs." + transdetailsContent.getString("paid_amount"));
                            txtPaymentMode.setText(transdetailsContent.getString("payment_type"));
                            if (transdetailsContent.getString("response_message").equals("SUCCESS")) {
                                txtStatus.setTextColor(Color.parseColor("#03980A"));
                            } else {
                                txtStatus.setTextColor(Color.parseColor("#D30404"));
                            }


                            String txn_date = transdetailsContent.getString("indian_datetime");   //created_at

                            SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat outputFormatter = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa");
                            try {
                                Date txnDated = inputFormatter.parse(txn_date);
                                String txnTime = outputFormatter.format(txnDated);
                                txtTranDate.setText(txnTime);
                                //txtTranDate.setText(txnDated);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TransactionDetails.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = TransactionDetails.this.getLayoutInflater();
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
                            Toast.makeText(TransactionDetails.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(TransactionDetails.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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
        new GetTransactionDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}