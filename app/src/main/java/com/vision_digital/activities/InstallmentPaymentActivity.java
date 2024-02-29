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
import com.vision_digital.model.InstallmentOrderList.ItemInstallmentOrderList;
import com.vision_digital.model.installment.ItemInstallmentAdapter;
import com.vision_digital.model.installment.ItemInstallmentList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InstallmentPaymentActivity extends AppCompatActivity {

    ActivityInstallmentPaymentBinding binding;
    ArrayList<ItemInstallmentList> arrayListInstallment = new ArrayList<>();
   static ArrayList<ItemInstallmentOrderList> arrayListOrder = new ArrayList<>();
    ItemInstallmentAdapter itemInstallmentAdapter;
    String sid;
    String getInstallment, enrollNo;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_installment_payment);
        dialog = new ProgressDialog(InstallmentPaymentActivity.this);

        //setContentView(R.layout.activity_installment_payment);
        sid = getIntent().getStringExtra("sid");

        enrollNo = getIntent().getStringExtra("enrollNo");
        Log.e("enrollno", enrollNo);
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

                        if (dataObj.length()==0){
                            binding.feesLayout.setVisibility(View.GONE);
                            binding.installmentMonths.setVisibility(View.GONE);
                            binding.noInstallmentText.setVisibility(View.VISIBLE);
                            binding.viewBorder.setVisibility(View.GONE);
                            binding.feeStructureText.setVisibility(View.GONE);
                            binding.rvInstallment.setVisibility(View.GONE);
                        }else {

                            JSONArray installmentArray = dataObj.getJSONArray("installment");
                            binding.feesLayout.setVisibility(View.VISIBLE);
                            binding.installmentMonths.setVisibility(View.VISIBLE);
                            binding.noInstallmentText.setVisibility(View.GONE);
                            binding.rvInstallment.setVisibility(View.VISIBLE);
                            binding.viewBorder.setVisibility(View.VISIBLE);
                            binding.feeStructureText.setVisibility(View.VISIBLE);

                            binding.tvTotalFees.setText(dataObj.getString("total_fee"));
                            binding.tvPendingAmount.setText(dataObj.getString("pending_fee"));
                            String id = String.valueOf(dataObj.getInt("id"));
                            String batch = dataObj.getString("batch");
//                            String admno = dataObj.getString("admno");
                            String total_installment = dataObj.getString("total_installment");
                            binding.edtMonth.setText(total_installment);


                            arrayListInstallment.clear();
                            arrayListOrder.clear();

                            for (int i = 0; i<installmentArray.length(); i++){
                                ItemInstallmentList itemInstallmentList = new ItemInstallmentList();
                                JSONObject installDataObj = installmentArray.getJSONObject(i);
                                itemInstallmentList.setInstallmentNumber(String.valueOf(installDataObj.getInt("installment"))+" Installment");
// ==========================WE HAVE TO FORWARD INSTALLMENT ID ONLY IN NEXT ACTIVITY THAT IS WHY STORING IN NEW GETTER SETTER===============
                                itemInstallmentList.setOnlyInstallmentId(String.valueOf(installDataObj.getInt("installment")));
                                itemInstallmentList.setDueDate("Pay by "+installDataObj.getString("due_date"));
// ==========================WE HAVE TO SHOW DUE DATE ONLY IN ADAPTER THAT IS WHY STORING IN NEW GETTER SETTER===============
                                itemInstallmentList.setOnlyDueDate(installDataObj.getString("due_date"));
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
//                                JSONArray payment_history= installDataObj.getJSONArray("payment_history");
//
//
//                                for (int j=0; j<payment_history.length(); j++){
//                                    ItemInstallmentOrderList orderList = new ItemInstallmentOrderList();
//                                    JSONObject orderDataObj = payment_history.getJSONObject(j);
//                                    orderList.setId(String.valueOf(orderDataObj.getInt("id")));
//                                    orderList.setAmount(String.valueOf(orderDataObj.getInt("amount")));
//                                    orderList.setInstallment(String.valueOf(orderDataObj.getInt("installment")));
//                                    orderList.setOrder_id(orderDataObj.getString("order_id"));
//                                    orderList.setPaid_date(orderDataObj.getString("paid_date"));
//                                    orderList.setPayment_type(orderDataObj.getString("payment_type"));
//                                    orderList.setStatus(orderDataObj.getString("status"));
//
//                                    arrayListOrder.add(orderList);
//                                }


                                arrayListInstallment.add(itemInstallmentList);
                            }

                            LinearLayoutManager layoutManager = new LinearLayoutManager(InstallmentPaymentActivity.this, RecyclerView.VERTICAL, false);
                            binding.rvInstallment.setLayoutManager(layoutManager);

                            itemInstallmentAdapter = new ItemInstallmentAdapter(InstallmentPaymentActivity.this, arrayListInstallment);
                            binding.rvInstallment.setAdapter(itemInstallmentAdapter);




                            Log.e("INSTALLMENT DATA", installmentArray.toString());

                        }


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