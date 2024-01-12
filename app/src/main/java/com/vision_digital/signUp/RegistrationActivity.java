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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vision_digital.BuildConfig;
import com.vision_digital.activities.NeedHelpActivity;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.R;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.oboardingScreen.IntroActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;



public class RegistrationActivity extends AppCompatActivity {

    //Layout-----------------------------------------------------
    TextView registerBtn, needHelpBtn;
    TextInputLayout studentNameTV;

    //Details------------------------------------------------------
    String studName = "";
    FirebaseUser user;
    int sid;


    //HTTP--------------------------
    String checkLoginUrl = "";

    ProgressDialog dialog;
    private String mobileNumber = "";
    ImageView backBtnReg;

    String deepLink = "";
    boolean introReg = false;

    String  tokenId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        checkLoginUrl = getApplicationContext().getString(R.string.apiURL) + "registerStudent";
        user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences sharedPreferences = getSharedPreferences("CNB", MODE_PRIVATE);
        sid = sharedPreferences.getInt("sid", 0);

        dialog = new ProgressDialog(RegistrationActivity.this);

        studentNameTV = findViewById(R.id.studentName);

        mobileNumber = getIntent().getStringExtra("mobileNo");
        deepLink = getIntent().getStringExtra("deeplinkFirebase");
        backBtnReg = findViewById(R.id.backBtnReg);
        SharedPreferences userIsRegisteredSuccessful = this.getSharedPreferences("CNBINTRO", MODE_PRIVATE);
        introReg = userIsRegisteredSuccessful.getBoolean("intro", false);

        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                studName = studentNameTV.getEditText().getText().toString().trim();
//                studEmail = studentEmailTV.getEditText().getText().toString().trim();

                if (studName.equals("")) {
                    Toast.makeText(RegistrationActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.setMessage("Please wait.");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    new RegisterStudent().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }


            }
        });

        backBtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        needHelpBtn = findViewById(R.id.needHelpBtn);
        needHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, NeedHelpActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                }
                tokenId = task.getResult();

            }
        });

    }

    class RegisterStudent extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(RegistrationActivity.this);

            int versionCode = BuildConfig.VERSION_CODE;
            String param = "sid=" + sid + "&uid=" + user.getUid() + "&name=" + studName +"&token_id=" + tokenId + "&app_version=" + versionCode;

//            String param = "sid=" + sid + "&uid=" + user.getUid() + "&name=" + studName + "&email=" + studEmail + "&qualification=" + studQuali + "&country=" + studCountry + "&other_school_name=" + otherSchoolName + "&city=" + studState + "&school=" + studInstitute + "&token_id=" + FirebaseInstanceId.getInstance().getToken() + "&app_version=" + versionCode;
            Log.e("test-param", param + checkLoginUrl);

            JSONObject jsonObject = jsonParser.makeHttpRequest(checkLoginUrl, "POST", param);
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
                    final JSONObject dataObj = jsonObject.getJSONObject("data");
                    dialog.dismiss();
                    switch (status) {
                        case "success":
                            //Running Fine
                            String inputValidation = dataObj.getString("input_validation");
                            if (inputValidation.equals("pass")) {
                                SharedPreferences.Editor editor = getSharedPreferences("CNB", MODE_PRIVATE).edit();
                                editor.putBoolean("registered", true);
                                editor.putString("profileName", studName);
                                editor.apply();

//                                startActivity(new Intent(RegistrationActivity.this, DashboardActivity.class));

                                if (introReg){
                                    Intent intent = new Intent(RegistrationActivity.this, DashboardActivity.class);
                                    intent.putExtra("mobileNo",mobileNumber);
                                    intent.putExtra("deeplinkFirebase",deepLink);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }else{
                                    Intent intent = new Intent(RegistrationActivity.this, IntroActivity.class);
                                    intent.putExtra("mobileNo",mobileNumber);
                                    intent.putExtra("deeplinkFirebase",deepLink);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                                finish();

                            } else {
                                String message = dataObj.getString("input_validation_message");
                                Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "maintainance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegistrationActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = RegistrationActivity.this.getLayoutInflater();
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
                            Toast.makeText(RegistrationActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(RegistrationActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
