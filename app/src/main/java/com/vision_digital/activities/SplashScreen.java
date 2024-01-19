package com.vision_digital.activities;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vision_digital.R;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.internetConnectivity.ConnectivityReciever;
import com.vision_digital.internetConnectivity.MyApplication;
import com.vision_digital.signUp.LogInViaPhoneActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity implements ConnectivityReciever.ConnectivityRecieverListener {

    Animation left, right, fadeout;
    TextView logoone, sigma,logotwo, logothree;
    ImageView center_chalk;
    long delay = 2000;

    String checkLoginUrl = "";
    String isLogin="" , current_login_id="";
    FirebaseUser user;
    String uid = "";
    boolean goodToGo = false;
    int sid = 0;
    Uri deepLink;
    String deeplinkFirebase = "chalksnboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        checkLoginUrl = getApplicationContext().getString(R.string.apiURL) + "getConfig";

        //Checking internet Connectivity
        filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        reciever = new ConnectivityReciever();


//        logoone = findViewById(R.id.center_logo_one);
//        logotwo = findViewById(R.id.center_logo_two);
//        logothree = findViewById(R.id.center_logo_three);
//        sigma = findViewById(R.id.sigma);
        center_chalk = findViewById(R.id.center_chalk);

//        Glide.with(this).load(R.drawable.title_cnb).into(title_cnb);


        left = AnimationUtils.loadAnimation(this, R.anim.from_left);
        right = AnimationUtils.loadAnimation(this, R.anim.from_right);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.txt_fade_out);

        center_chalk.setAnimation(fadeout);


        SharedPreferences userIsRegisteredSuccessful = this.getSharedPreferences("CNB", MODE_PRIVATE);
        sid = userIsRegisteredSuccessful.getInt("sid", 0);
        isLogin=userIsRegisteredSuccessful.getString("isLogin","No");
        current_login_id = userIsRegisteredSuccessful.getString("current_login_id","");
        Log.e("TAG", "onCreate sid: "+sid);

        user = FirebaseAuth.getInstance().getCurrentUser();
        try {

            if (user != null) {
//                SharedPreferences userIsRegisteredSuccessful = this.getSharedPreferences("CNB", MODE_PRIVATE);
//                boolean registered = userIsRegisteredSuccessful.getBoolean("registered", false);
//                sid = userIsRegisteredSuccessful.getInt("sid", 0);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new GetConfig().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//        new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    startActivity(new Intent(SplashScreen.this, LogInViaPhoneActivity.class));
//                    finish();
//                }
//            }, 2000);
    }


//    private void timer() {

//        final Runnable runnableTimer = new Runnable() {
//            @Override
//            public void run() {
//                timeLimit--;
//                hours = timeLimit / 3600;
//
//                if (timeLimit % 10 == 0) {
//                    SharedPreferences.Editor timerPref = getSharedPreferences(test_id, MODE_PRIVATE).edit();
//                    timerPref.putLong("timeSave", timeLimit);
//                    Log.e("unsavedtime", String.valueOf(timeLimit));
//                    timerPref.apply();
//                }
//
//
//            }
//        };
//        timer.postDelayed(runnableTimer, 1000);
//    }


    class GetConfig extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(SplashScreen.this);
//            int versionCode = BuildConfig.VERSION_CODE;
//            int versionCode = BuildConfig.VERSION_CODE;


            int versionCode;

            SharedPreferences studDetails = getSharedPreferences("CNBUID", MODE_PRIVATE);
            uid = studDetails.getString("uid", "NO_NAME");

            Log.e("TAG", "doInBackground: uid Id"+uid);


            PackageInfo pInfo = null;
            try {
                pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            versionCode = pInfo.versionCode;
            Log.e("versionCode", String.valueOf(versionCode));


            String param = "uid=" + uid + "&app_version=" + versionCode + "&sid=" + sid+ "&current_login_id=" + current_login_id;

            Log.e("param", param);

            Log.e("urlLogin",checkLoginUrl);
            JSONObject jsonObject = jsonParser.makeHttpRequest(checkLoginUrl, "POST", param);
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
                    final JSONObject dataObj = jsonObject.getJSONObject("data");
                    switch (status) {
                        case "success":
                            //Running Fine
                            //Checking App update-------------------------------
                            int appUpdate = dataObj.getInt("app_update");
                            switch (appUpdate) {
                                case 0:
                                    //No Updates
                                    //Checking User Status------------------------------
                                    String userStatus = dataObj.getString("user_status");
                                    if (userStatus.equals("allowed")) {
                                        //Already Logged in-----------------------

                                        checkLoginStatus();

                                    } else if (userStatus.equals("banned")) {
                                        //Student is banned--------------------------
                                        String message = dataObj.getString("user_status_banned_message");
                                        Toast.makeText(SplashScreen.this, message, Toast.LENGTH_SHORT).show();
                                    } else if (userStatus.equals("not_registered")) {
//                                        startActivity(new Intent(SplashScreen.this, RegistrationActivity.class));
//                                        finish();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //This method will be executed once the timer is over
                                                // Start your app main activity
                                                Log.e(TAG, "run: " +"user registered nhi hai" );

                                                Intent i = new Intent(SplashScreen.this, LogInViaPhoneActivity.class);
                                                startActivity(i);

                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                                finish();
                                            }
                                        }, delay);


                                    } else {
//                                        startActivity(new Intent(SplashScreen.this, LogInViaPhoneActivity.class));
//                                        finish();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //This method will be executed once the timer is over
                                                // Start your app main activity
                                                checkLoginStatus();
                                            }
                                        }, delay);
                                    }

                                    break;
                                case 1:
                                    //Minor Update------------------------------------------------
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SplashScreen.this);
                                    // ...Irrelevant code for customizing the buttons and title
                                    LayoutInflater inflater = SplashScreen.this.getLayoutInflater();
                                    View dialogView = inflater.inflate(R.layout.update_available_dialog, null);
                                    dialogBuilder.setView(dialogView);
                                    //Alert Dialog Layout work
                                    final AlertDialog alertDialog = dialogBuilder.create();

                                    //Layout-----------------------------------------------
                                    TextView updateNow = dialogView.findViewById(R.id.updateBtn);
                                    updateNow.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Updating App--------------------------
                                            Uri uri = Uri.parse("market://details?id=" + SplashScreen.this.getPackageName());
                                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                            // To count with Play market backstack, After pressing back button,
                                            // to taken back to our application, we need to add following flags to intent.
                                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                            try {
                                                startActivity(goToMarket);
                                            } catch (ActivityNotFoundException e) {
                                                startActivity(new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse("http://play.google.com/store/apps/details?id=" + SplashScreen.this.getPackageName())));
                                            }
                                        }
                                    });
                                    TextView updateLater = dialogView.findViewById(R.id.updateLater);
                                    updateLater.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Posponding Update App--------------------------
                                            alertDialog.dismiss();
                                            //Checking User Status------------------------------
                                            String userStatus = null;
                                            try {
                                                userStatus = dataObj.getString("user_status");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            if (userStatus.equals("allowed")) {
                                                //Already Logged in-----------------------
//                                                startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
//                                                finish();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //This method will be executed once the timer is over
                                                        // Start your app main activity
                                                        Intent i = new Intent(SplashScreen.this, DashboardActivity.class);
                                                        i.putExtra("deeplinkFirebase",deeplinkFirebase);
                                                        startActivity(i);

                                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                                        finish();
                                                    }
                                                }, delay);


                                            } else if (userStatus.equals("banned")) {
                                                //Student is banned--------------------------
                                                String message = null;
                                                try {
                                                    message = dataObj.getString("user_status_banned_message");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Toast.makeText(SplashScreen.this, message, Toast.LENGTH_SHORT).show();
                                            } else {
//                                                startActivity(new Intent(SplashScreen.this, LogInViaPhoneActivity.class));
//                                                finish();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        //This method will be executed once the timer is over
                                                        // Start your app main activity
                                                        Intent i = new Intent(SplashScreen.this, LogInViaPhoneActivity.class);
                                                        i.putExtra("deeplinkFirebase",deeplinkFirebase);
                                                        startActivity(i);

                                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                                        finish();
                                                    }
                                                }, delay);


                                            }

                                        }
                                    });
                                    alertDialog.show();
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    break;
                                case 2:
                                    //Major Update------------------------------------------------
                                    AlertDialog.Builder mdialogBuilder = new AlertDialog.Builder(SplashScreen.this);
                                    // ...Irrelevant code for customizing the buttons and title
                                    LayoutInflater minflater = SplashScreen.this.getLayoutInflater();
                                    View mdialogView = minflater.inflate(R.layout.update_available_dialog, null);
                                    mdialogBuilder.setView(mdialogView);
                                    //Alert Dialog Layout work

                                    //Layout-----------------------------------------------
                                    TextView mupdateNow = mdialogView.findViewById(R.id.updateBtn);
                                    TextView updateLaterBtn = mdialogView.findViewById(R.id.updateLater);
                                    updateLaterBtn.setVisibility(View.GONE);
                                    mupdateNow.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Updating App--------------------------
                                            Uri uri = Uri.parse("market://details?id=" + SplashScreen.this.getPackageName());
                                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                            // To count with Play market backstack, After pressing back button,
                                            // to taken back to our application, we need to add following flags to intent.
                                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                            try {
                                                startActivity(goToMarket);
                                            } catch (ActivityNotFoundException e) {
                                                startActivity(new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse("http://play.google.com/store/apps/details?id=" + SplashScreen.this.getPackageName())));
                                            }
                                        }
                                    });

                                    AlertDialog malertDialog = mdialogBuilder.create();
                                    malertDialog.show();
                                    malertDialog.setCanceledOnTouchOutside(false);
                                    break;
                                default:
                                    Toast.makeText(SplashScreen.this, "Something went wrong. Contact to support over web", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SplashScreen.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = SplashScreen.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
                            String msgContent = dataObj.getString("message");
                            maintainanceContent.setText(Html.fromHtml(msgContent));

                            TextView btnOK = dialogView.findViewById(R.id.btnOK);
                            btnOK.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                            Toast.makeText(SplashScreen.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(SplashScreen.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
        }
    }

    private void checkLoginStatus() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will be executed once the timer is over
                // Start your app main activity
                if (isLogin.equals("Yes")){
                    Log.e(TAG, "run: " +"pehel se login hian" );
                    Intent i = new Intent(SplashScreen.this, DashboardActivity.class);
                    i.putExtra("deeplinkFirebase",deeplinkFirebase);
                    startActivity(i);

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    finish();
                }else {
                    Log.e(TAG, "run: " +"pehel se login nhi hian" );

                    Intent i = new Intent(SplashScreen.this, LogInViaPhoneActivity.class);
                    i.putExtra("deeplinkFirebase",deeplinkFirebase);
                    startActivity(i);

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    finish();
                }

            }
        }, delay);

    }

    private ConnectivityReciever reciever;
    IntentFilter filter;

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        ConnectivityReciever connectivityReciever = new ConnectivityReciever();
        connectivityReciever.showSnackbar(isConnected, findViewById(R.id.splash_screen), getApplicationContext());
    }


    @Override
    public void onBackPressed() {
        Log.e("onBack","onBack");
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onstop","onstop");
        ConnectivityReciever.snackbar = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(reciever);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
        registerReceiver(reciever, filter);
    }


}
