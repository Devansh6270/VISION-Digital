package com.vision_digital.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vision_digital.R;
import com.vision_digital.databinding.ActivityInstallmentPaymentBinding;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.installment.ItemInstallmentAdapter;
import com.vision_digital.model.installment.ItemInstallmentList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InstallmentPaymentActivity extends AppCompatActivity {

    ActivityInstallmentPaymentBinding binding;
    ArrayList<ItemInstallmentList> arrayListInstallment = new ArrayList<>();
    ItemInstallmentAdapter itemInstallmentAdapter;
    String sid;
    String getInstallment;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_installment_payment);
        dialog = new ProgressDialog(InstallmentPaymentActivity.this);

        //setContentView(R.layout.activity_installment_payment);
        sid = getIntent().getStringExtra("sid");
        getInstallment = "http://v.chalksnboard.com/api/v4/students/"+sid+"/fee";


        binding.profileBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new GetInstallmentData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class GetInstallmentData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(InstallmentPaymentActivity.this);

            String param = "&sid=" + sid;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(getInstallment, "GET",param);
            if (jsonObject != null) {
                return jsonObject.toString();
            } else {
                Log.e("INSTALLMENT DATA", "NOT CONNECTED WITH API");
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("json installment", s);

            if (!s.equals("")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);


                    //Do work-----------------------------
                    dialog.dismiss();
                    String status = jsonObject.getString("success");
                    final JSONObject dataObj = jsonObject.getJSONObject("data");

                    Log.e("Status", status.toString());
                    if (status.equals("true")){

                        binding.tvTotalFees.setText(dataObj.getString("total_fee"));
                        binding.tvPendingAmount.setText(dataObj.getString("pending_fee"));
                        String id = String.valueOf(dataObj.getInt("id"));
                        String batch = dataObj.getString("batch");
                        String admno = dataObj.getString("admno");
                        String total_installment = dataObj.getString("total_installment");
                        JSONArray installmentArray = dataObj.getJSONArray("installment");

                        JSONArray paymentHistoryArr;
                        arrayListInstallment.clear();

                        for (int i = 0; i<installmentArray.length(); i++){
                            ItemInstallmentList itemInstallmentList = new ItemInstallmentList();
                            JSONObject installDataObj = installmentArray.getJSONObject(i);
                            itemInstallmentList.setInstallmentNumber(String.valueOf(installDataObj.getInt("installment"))+" Installment");
                            itemInstallmentList.setDueDate("Pay by "+installDataObj.getString("due_date"));
//                            itemInstallmentList.setDate(installDataObj.getString(""));
                            itemInstallmentList.setAmount(String.valueOf(installDataObj.getInt("amount")));
                            itemInstallmentList.setPendingAmount(String.valueOf(installDataObj.getInt("pending")));

                            String idCard = String.valueOf(installDataObj.getInt("id"));
                            itemInstallmentList.setId(idCard);
                            String fee_id = String.valueOf(installDataObj.getInt("fee_id"));
                            itemInstallmentList.setFee_id(fee_id);
                            String admnoCard = installDataObj.getString("admno");
                            itemInstallmentList.setAdmno(admnoCard);
                            String statusCard = installDataObj.getString("status");
                            itemInstallmentList.setStatus(statusCard);
                            String payment_alert = installDataObj.getString("payment_alert");
                            itemInstallmentList.setPayment_alert(payment_alert);
                            String payment_status= installDataObj.getString("payment_status");
                            itemInstallmentList.setPayment_status(payment_status);
                            paymentHistoryArr= installDataObj.getJSONArray("payment_history");
                            for (int j = 0; i<paymentHistoryArr.length(); j++){
                                JSONObject paymentHistoryObj = paymentHistoryArr.getJSONObject(j);
                                itemInstallmentList.setOrderId(paymentHistoryObj.getString("order_id"));
                                itemInstallmentList.setPaidDate(paymentHistoryObj.getString("paid_date"));
                                itemInstallmentList.setPaymentType(paymentHistoryObj.getString("payment_type"));
                                itemInstallmentList.setAmountOrder(paymentHistoryObj.getString("amount"));
                            }


                            arrayListInstallment.add(itemInstallmentList);
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(InstallmentPaymentActivity.this, RecyclerView.VERTICAL, false);
                        binding.rvInstallment.setLayoutManager(layoutManager);

                        itemInstallmentAdapter = new ItemInstallmentAdapter(InstallmentPaymentActivity.this, arrayListInstallment);
                        binding.rvInstallment.setAdapter(itemInstallmentAdapter);


                        Log.e("INSTALLMENT DATA", installmentArray.toString());


                    } else if (status.equals("false")) {
                        Toast.makeText(InstallmentPaymentActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(InstallmentPaymentActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}