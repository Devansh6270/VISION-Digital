package com.vision_digital.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vision_digital.R;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.InstallmentOrderList.ItemInstallmentOrderAdapter;
import com.vision_digital.model.InstallmentOrderList.ItemInstallmentOrderList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class InstallmentOrderListActivity extends AppCompatActivity {

    RecyclerView rvOrderItemList;

    String getInstallmentList;
    String feeId;
    String installmentId;
    TextView tvNoInstallmentText;
    ArrayList<ItemInstallmentOrderList> arrayListOrder = new ArrayList<>();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installment_order_list);

        dialog = new ProgressDialog(InstallmentOrderListActivity.this);
        feeId = getIntent().getStringExtra("feeId");
        installmentId = getIntent().getStringExtra("installmentId");

        getInstallmentList = "http://v.chalksnboard.com/api/v4/students/" + feeId + "/" + installmentId + "/installmentList";

        rvOrderItemList = findViewById(R.id.rvInstallmentOrderList);
        tvNoInstallmentText = findViewById(R.id.tvNoInstallmentText);



        Log.e("arrayListOrder", arrayListOrder.toString());

        new GetOrderListData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    class GetOrderListData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser(InstallmentOrderListActivity.this);

            String param = "&sid=" + "sid";

            JSONObject jsonObject = jsonParser.makeHttpRequest(getInstallmentList, "GET", param);
            if (jsonObject != null) {
                return jsonObject.toString();
            } else {
                Log.e("ORDER LIST", "NOT CONNECTED WITH API");
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("json order installment", s);

            if (!s.equals("")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    dialog.dismiss();
                    String status = jsonObject.getString("success");
                    final JSONArray payment_history = jsonObject.getJSONArray("data");

                    Log.e("Status", status.toString());

                    if (status.equals("true")) {
                        if (payment_history.length() == 0) {

                            rvOrderItemList.setVisibility(View.GONE);
                            tvNoInstallmentText.setVisibility(View.VISIBLE);
                        } else {

                            rvOrderItemList.setVisibility(View.VISIBLE);
                            tvNoInstallmentText.setVisibility(View.GONE);
                            arrayListOrder.clear();
                            for (int j = 0; j < payment_history.length(); j++) {
                                ItemInstallmentOrderList orderList = new ItemInstallmentOrderList();
                                JSONObject orderDataObj = payment_history.getJSONObject(j);
                                orderList.setId(String.valueOf(orderDataObj.getInt("id")));
                                orderList.setAmount(String.valueOf(orderDataObj.getInt("amount")));
                                orderList.setInstallment(String.valueOf(orderDataObj.getInt("installment")));
                                orderList.setOrder_id(orderDataObj.getString("order_id"));
                                orderList.setPaid_date(orderDataObj.getString("paid_date"));
                                orderList.setPayment_type(orderDataObj.getString("payment_type"));
                                orderList.setStatus(orderDataObj.getString("status"));

                                arrayListOrder.add(orderList);
                            }

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InstallmentOrderListActivity.this, RecyclerView.VERTICAL, false);
                            rvOrderItemList.setLayoutManager(linearLayoutManager);

                            ItemInstallmentOrderAdapter adapter = new ItemInstallmentOrderAdapter(InstallmentOrderListActivity.this, arrayListOrder);
                            rvOrderItemList.setAdapter(adapter);
                            Log.e("ORDER DATA", payment_history.toString());
                        }
                    } else if (status.equals("false")) {
                        Toast.makeText(InstallmentOrderListActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(InstallmentOrderListActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}