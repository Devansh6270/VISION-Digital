package com.vision_digital.signUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vision_digital.R;

import com.google.android.material.textfield.TextInputLayout;
import com.vision_digital.helperClasses.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInViaPhoneActivity extends AppCompatActivity {

    TextInputLayout eTphoneNo;
    TextInputEditText phone;
    TextView btnSendOTP;
    String tokenId;
    Spinner countryCodeSpinner;
    String countryCode ="+91";

    ImageView backBtnLog;
 //   ActivityLogInViaPhoneBinding binding;
    String deepLink = "";
    String phoneNo="";

    String sendOtpUrl="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityLogInViaPhoneBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_log_in_via_phone);

        sendOtpUrl=getApplicationContext().getString(R.string.apiURL)+"sendOTPcnb";

       // countryCodeSpinner = findViewById(R.id.countryCode);
//        countryCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                countryCode = (String) countryCodeSpinner.getItemAtPosition(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//countryCode = "+91";
//            }
//        });

        eTphoneNo = findViewById(R.id.mobNo);
        phone=findViewById(R.id.phoneNo);
        btnSendOTP = findViewById(R.id.btnSendOTP);
        //backBtnLog = findViewById(R.id.backBtnLog);
        deepLink = getIntent().getStringExtra("deeplinkFirebase");

         phoneNo = eTphoneNo.getEditText().getText().toString().trim();

        FirebaseApp.initializeApp(this);

        // Get the FCM registration token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        tokenId = task.getResult();
                        Log.d("FCM Token", "Token: " + tokenId.toString());
                        // You can now use 'token' to send FCM messages to this device
                    } else {
                        Log.e("FCM Token", "Failed to get token");
                    }
                });



        if (checkValidation()){
            btnSendOTP.setBackgroundResource(R.drawable.topper_button);
        }else {
            btnSendOTP.setBackgroundResource(R.drawable.disable_btn);
            btnSendOTP.setEnabled(true);
            btnSendOTP.setTextColor(Color.parseColor("#ffffff"));
        }

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnSendOTP.setBackgroundResource(R.drawable.topper_button);
            }
        });

//        backBtnLog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finishAffinity();
//                Intent a = new Intent(Intent.ACTION_MAIN);
//                a.addCategory(Intent.CATEGORY_HOME);
//                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(a);
//
//            }
//        });

//        btnSendOTP.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SendOtp().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            }
//        });
        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // new SendOtp().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                String phoneNo = eTphoneNo.getEditText().getText().toString().trim();
                SharedPreferences.Editor editor = getSharedPreferences("CNBMOBILE", MODE_PRIVATE).edit();
                editor.putBoolean("mobileNoRegistered", true);
                editor.putString("mobileNo", "+91-"+phoneNo);
                editor.apply();

                if (phoneNo.length() == 10) {
//                    Intent otpIntent = new Intent(LogInViaPhoneActivity.this,OTPActivity.class);
//                    otpIntent.putExtra("MobileNumber",phoneNo);
//                    otpIntent.putExtra("countryCode",countryCode);
//                    otpIntent.putExtra("deeplinkFirebase",deepLink);
//                    startActivity(otpIntent);

                    new SendOtp().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                  //  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                   // finish();
                }else{
                    Toast.makeText(LogInViaPhoneActivity.this, "Enter valid Mobile number ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean checkValidation() {
        if (phoneNo.isEmpty() || phoneNo.equals("")){
            return false;
        }
        return true;

    }

    class  SendOtp extends AsyncTask<String,Void ,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(LogInViaPhoneActivity.this);
          String  phoneNo = eTphoneNo.getEditText().getText().toString().trim();
          //  String phoneNumber ="91"+phoneNo;
            String param = "phone=" + phoneNo;

            // FirebaseInstanceId.getInstance().getToken();
            Log.e("test-param", param + sendOtpUrl);

            JSONObject jsonObject = jsonParser.makeHttpRequest(sendOtpUrl, "POST", param);
            if (jsonObject != null) {
                Log.e("test-response", jsonObject.toString());
                return jsonObject.toString();

            } else {
                Log.e("test-response-else", jsonObject.toString());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("json", s);

            if (!s.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    Log.e("Result: ", s);

                    // Extract status and message
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("true")) {
                        // Success case
                        Toast.makeText(LogInViaPhoneActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent otpIntent = new Intent(LogInViaPhoneActivity.this, OTPActivity.class);
                        String  phoneNo = eTphoneNo.getEditText().getText().toString().trim();
                        otpIntent.putExtra("MobileNumber", phoneNo);
//                        otpIntent.putExtra("countryCode", countryCode);
//                        otpIntent.putExtra("deeplinkFirebase", deepLink);
                        startActivity(otpIntent);
                    } else if (status.equals("false")) {
                        // Failure case
                        Toast.makeText(LogInViaPhoneActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("maintainance")) {
                        // Under maintenance case
                        // ... Code for displaying maintenance dialog
                    } else {
                        // Unknown status case
                        Toast.makeText(LogInViaPhoneActivity.this, "Something went wrong. Contact support over the website", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LogInViaPhoneActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LogInViaPhoneActivity.this, "Empty response", Toast.LENGTH_SHORT).show();
            }
        }

//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            Log.i("json", s);
//
//            if (!s.equals("")) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(s);
//
//                    Log.e("Result : ", s);
//
//                    //Do work-----------------------------
//                    String status = jsonObject.getString("status");
//                   // dialog.dismiss();
//                    switch (status) {
//                        case "true":
//                            //Running Fine
//                            String message = jsonObject.getString("message");
//                            Toast.makeText(LogInViaPhoneActivity.this, message, Toast.LENGTH_SHORT).show();
//                            Intent otpIntent = new Intent(LogInViaPhoneActivity.this,OTPActivity.class);
//                            otpIntent.putExtra("MobileNumber",phoneNo);
//                            otpIntent.putExtra("countryCode",countryCode);
//                            otpIntent.putExtra("deeplinkFirebase",deepLink);
//                            startActivity(otpIntent);
//                              //  startActivity(new Intent(LogInViaPhoneActivity.this, OTPActivity.class));
//                                //finishAffinity();
//                            break;
//
//                        case "false":
//                            Toast.makeText(LogInViaPhoneActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
//                            break;
//                        case "maintainance":
//                            //Undermaintance
//                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LogInViaPhoneActivity.this);
//                            // ...Irrelevant code for customizing the buttons and title
//                            LayoutInflater inflater = LogInViaPhoneActivity.this.getLayoutInflater();
//                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
//                            dialogBuilder.setView(dialogView);
//                            //Alert Dialog Layout work
//                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
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
//
//                        default:
//                            Toast.makeText(LogInViaPhoneActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
//
//                            break;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
