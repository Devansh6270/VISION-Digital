package com.vision_digital.signUp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.chaos.view.BuildConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.R;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.oboardingScreen.IntroActivity;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

public class OTPActivity extends AppCompatActivity {

    private static final String CREDENTIAL = "CNB";
    //Layout--------------------------------------------
    TextView btnOTPVerification;
    ImageView backBtn;
    TextView sendingOTPOn;
    TextView resendCode;

    //HTTP--------------------------
    String checkLoginUrl = "";
//    --------------------------------------------------

    PinView otpView;
    String phoneNumber;
    String countryCode = "+91";
    public static String mobileNum;
    FirebaseAuth mAuth;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PhoneAuthCredential phoneAuthCredentialUser;
    private String tokenId = "";

    ProgressDialog dialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String deepLink = "";

    String sendOtpUrl ="";
    String resendOtpUrl ="";
    String verifyOtpUrl ="";
    boolean intro = false;
    private int sid ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        checkLoginUrl = getApplicationContext().getString(R.string.apiURL) + "isRegisteredStudent";
        sendOtpUrl=getApplicationContext().getString(R.string.apiURL)+"sendOTPcnb";
        resendOtpUrl=getApplicationContext().getString(R.string.apiURL)+"resendOTPcnb";
        verifyOtpUrl=getApplicationContext().getString(R.string.apiURL)+"verifyOTPcnb";


        mAuth = FirebaseAuth.getInstance();
        otpView = findViewById(R.id.otpView);
        mobileNum = getIntent().getStringExtra("MobileNumber");
//        countryCode = getIntent().getStringExtra("countryCode");
//        deepLink = getIntent().getStringExtra("deeplinkFirebase");
        phoneNumber = mobileNum;

        SharedPreferences userIsRegisteredSuccessful = this.getSharedPreferences("CNB", MODE_PRIVATE);
        intro = userIsRegisteredSuccessful.getBoolean("intro", false);
        btnOTPVerification = findViewById(R.id.btnOTPVerification);
        btnOTPVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOTPVerification.setEnabled(false);
                dialog = new ProgressDialog(OTPActivity.this);
                dialog.setMessage("Verifying");
                dialog.show();
                new VerifyOtp().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OTPActivity.this, LogInViaPhoneActivity.class));
                finish();
            }
        });

        sendingOTPOn = findViewById(R.id.sendingOTPOn);
        sendingOTPOn.setText("+91"+" "+mobileNum);

        resendCode = findViewById(R.id.resendVerificationCode);
        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ResendOtp().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
               // resendVerificationCode(phoneNumber, mResendToken);
            }
        });



        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                }
                tokenId = task.getResult();
                Log.e(TAG, "tokenId:========================================================== "+tokenId);

            }
        });



    }



    class  VerifyOtp extends AsyncTask<String,Void ,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            JSONParser jsonParser = new JSONParser(OTPActivity.this);

            String otp = otpView.getText().toString().trim();

            //  String phoneNumber ="91"+phoneNo;
            String param = "phone=" + phoneNumber  +"&otp="+otp;

            // FirebaseInstanceId.getInstance().getToken();
            Log.e("test-param", param + verifyOtpUrl);

            JSONObject jsonObject = jsonParser.makeHttpRequest(verifyOtpUrl, "POST", param);
            if (jsonObject != null) {
                Log.e("test-response", jsonObject.toString());
                return jsonObject.toString();

            }else {

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
                        Toast.makeText(OTPActivity.this, message, Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = getSharedPreferences("CNBMOBILE", MODE_PRIVATE).edit();
                        editor.putBoolean("mobileNoRegistered", true);
                        editor.putString("mobileNo", mobileNum);
                        editor.apply();


                        SharedPreferences studDetails = getSharedPreferences("CNBMOBILE", MODE_PRIVATE);
                        String mobString = studDetails.getString("mobileNo", "NO_NAME");
                        Log.e("mobString",mobString);


//                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                        Log.e("TAG", "onComplete: userId"+uid);
//                        SharedPreferences.Editor shareUid = getSharedPreferences("CNBUID", MODE_PRIVATE).edit();
//                        shareUid.putBoolean("isUidSaved", true);
//                        shareUid.putString("uid", uid);
//                        shareUid.apply();
                        new IsRegisteredStudent().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
                    } else {
                        // Failure case

                        Toast.makeText(OTPActivity.this, "==================================================================="+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(OTPActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(OTPActivity.this, "Invalid OTP!", Toast.LENGTH_SHORT).show();
                otpView.setText("");
                dialog.dismiss();
                btnOTPVerification.setEnabled(true);
            }
        }



    }

    class  ResendOtp extends AsyncTask<String,Void ,String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(OTPActivity.this);
            //  String phoneNumber ="91"+phoneNo;
            String param = "phone=" + mobileNum;

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
                        Toast.makeText(OTPActivity.this, "OTP Resend Successfully!", Toast.LENGTH_LONG).show();
                    } else if (status.equals("false")) {
                        // Failure case
                        Toast.makeText(OTPActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("maintainance")) {
                        // Under maintenance case
                        // ... Code for displaying maintenance dialog
                    } else {
                        // Unknown status case
                        Toast.makeText(OTPActivity.this, "Something went wrong. Contact support over the website", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(OTPActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(OTPActivity.this, "Empty response", Toast.LENGTH_SHORT).show();
            }
        }


    }


    class IsRegisteredStudent extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            SharedPreferences userIsRegisteredSuccessful = OTPActivity.this.getSharedPreferences("CNB", MODE_PRIVATE);
            String current_login = userIsRegisteredSuccessful.getString("current_login_id","");
            Log.e(TAG, "doInBackground: " + "do in background");

            JSONParser jsonParser = new JSONParser(OTPActivity.this);
            int versionCode = BuildConfig.VERSION_CODE;
            Log.e(TAG, "doInBackground: token id =========================================="+tokenId);
            String param = "mobile=" + mobileNum + "&app_version=" + versionCode + "&token_id=" +tokenId+ "&current_login_id="+current_login;

            // FirebaseInstanceId.getInstance().getToken();
            Log.e("test-param", param + checkLoginUrl);

            JSONObject jsonObject = jsonParser.makeHttpRequest(checkLoginUrl, "POST", param);
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
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    Log.e("Result : ", s);

                    //Do work-----------------------------
                    String status = jsonObject.getString("status");
                    final JSONObject dataObj = jsonObject.getJSONObject("data");
                    switch (status) {
                        case "success":
                            //Running Fine
                            //Setting sid in phone---------------------------------------


                            String userStatus = dataObj.getString("user_status");
                            if (userStatus.equals("registered")) {
                                SharedPreferences.Editor editor = getSharedPreferences("CNB", MODE_PRIVATE).edit();
                                editor.putInt("sid", dataObj.getInt("id"));
                                editor.putString("enrollno", dataObj.getString("enrollno"));
                                editor.putString("profileName",dataObj.getString("name"));
                                editor.putString("isLogin","Yes");
                                editor.putString("mobileNo",dataObj.getString("mobile"));
                                editor.putString("current_login_id", dataObj.getString("current_login_id"));
                                sid=dataObj.getInt("id");
                                editor.apply();
                                editor.commit();
                                Log.e(TAG, "login save hoga hai: "+sid );

                                    if (intro){
                                        Intent intent = new Intent(OTPActivity.this, DashboardActivity.class);
                                        intent.putExtra("mobileNo",mobileNum);
                                        intent.putExtra("deeplinkFirebase",deepLink);
                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                         finishAffinity();
                                    }else{
                                        Intent intent = new Intent(OTPActivity.this, IntroActivity.class);
                                        intent.putExtra("mobileNo",mobileNum);
                                        intent.putExtra("deeplinkFirebase",deepLink);
                                        startActivity(intent);
                                        finishAffinity();
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    }

                            } else if (userStatus.equals("banned")) {
                                SharedPreferences.Editor editor = getSharedPreferences("CNB", MODE_PRIVATE).edit();
                                editor.putInt("sid", dataObj.getInt("id"));
                                editor.putString("isLogin","Yes");
                                sid=dataObj.getInt("id");
                                editor.apply();
                                editor.commit();
                                Log.e(TAG, "login save hoga hai: "+sid );

                                //Student is banned--------------------------
                                String message = dataObj.getString("user_status_banned_message");
                                Toast.makeText(OTPActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else if (userStatus.equals("not_registered")) {
                                SharedPreferences.Editor editor = getSharedPreferences("CNB", MODE_PRIVATE).edit();
                                editor.putInt("sid", dataObj.getInt("id"));
                                editor.putString("isLogin","Yes");
                                editor.putString("mobileNo",mobileNum);
                                sid=dataObj.getInt("id");
                                editor.apply();
                                editor.commit();
                                Log.e(TAG, "onPostExecute: "+ "login registered nhi hia"  );
                                startActivity(new Intent(OTPActivity.this, VisionStudentActivity.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }

                            break;
                        case "maintainance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OTPActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = OTPActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
                            String msgContent = dataObj.getString("message");
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
                            Toast.makeText(OTPActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:

                            Toast.makeText(OTPActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Response reached---------------value in 's' variable
        }
    }


}
