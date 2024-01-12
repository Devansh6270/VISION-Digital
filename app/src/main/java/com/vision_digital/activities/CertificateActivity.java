package com.vision_digital.activities;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.vision_digital.BuildConfig;
import com.vision_digital.R;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.Certificate.CertificateAdapter;
import com.vision_digital.model.Certificate.CertificateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CertificateActivity extends AppCompatActivity {

    String certificateUrl ;
    String certificateDownloadUrl;
    public static String studId;
    TextView noCertificateTxt,yourcertificateTxt ;
    ArrayList<CertificateModel> certificateArrayList = new ArrayList<>();

    CertificateAdapter itemCertificateAdapter;

    private static final int REQUEST_PERMISSION = 1;


    RecyclerView recyclerViewCertificate;
    ImageView backbtn;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

      // certificateUrl=getApplicationContext().getString(R.string.apiURL) + "getCoursesCertificate";
       certificateUrl="https://irctc.chalksnboard.com/api/v2/getCoursesCertificate";
       certificateDownloadUrl=getApplicationContext().getString(R.string.apiURL)+"certificate-download";
        studId  = getIntent().getStringExtra("student_id");
        recyclerViewCertificate=findViewById(R.id.recyclarViewCertificate);
        backbtn=findViewById(R.id.backBtn);
        animationView = findViewById(R.id.animation_view);
        noCertificateTxt=findViewById(R.id.noCertificateTxt);
        yourcertificateTxt=findViewById(R.id.yourcertificateTxt);
        noCertificateTxt.setVisibility(View.GONE);



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });









//        animationView
//                .addAnimatorUpdateListener(
//                        (animation) -> {
//                            // Do something.
//                        });
//        animationView
//                .playAnimation();
//
//        if (animationView.isAnimating()) {
//            // Do something.
//        }
    }

    public class GetCertificateList extends AsyncTask<String,Void,String> {
        private ProgressDialog dialog = new ProgressDialog(CertificateActivity.this);


        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser(CertificateActivity.this);
            int versionCode = BuildConfig.VERSION_CODE;
            String param = "sid=" + studId; //stuId



            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(certificateUrl, "POST", param);
            if (jsonObject != null) {
                return jsonObject.toString();
            } else {
//              noCertificateTxt.
//                      setVisibility(View.VISIBLE);
//              animationView.setVisibility(View.VISIBLE);

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
                        case "true":
                            //Running Fine
                            certificateArrayList.clear();
                            JSONArray certificateContent = jsonObject.getJSONArray("data");
                            if (certificateContent.length()==0){
                                noCertificateTxt.setVisibility(View.GONE);
                                animationView.setVisibility(View.VISIBLE);
                                yourcertificateTxt.setVisibility(View.GONE);
                            }
                            for (int i = 0; i < certificateContent.length(); i++) {
//                                JSONObject contentSubscribe = courseContent.getJSONObject(i);
                                CertificateModel certificateModel = new CertificateModel();
                                JSONObject certificateObject = certificateContent.getJSONObject(i);
                                certificateModel.setImage(certificateObject.getString("image"));
                                certificateModel.setSubjectName(certificateObject.getString("title"));
                                certificateModel.setId(certificateObject.getString("id"));
                                certificateArrayList.add(certificateModel);
                            }
                            yourcertificateTxt.setVisibility(View.VISIBLE);
                            LinearLayoutManager layoutManager = new GridLayoutManager(CertificateActivity.this, 2);
                            // LinearLayoutManager layoutManager = new LinearLayoutManager(TransactionHistory.this, LinearLayoutManager.VERTICAL, false);
                            recyclerViewCertificate.setLayoutManager(layoutManager);
                            itemCertificateAdapter = new CertificateAdapter(getApplicationContext(), certificateArrayList);
                            recyclerViewCertificate.setAdapter(itemCertificateAdapter);

                            break;

                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CertificateActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = CertificateActivity.this.getLayoutInflater();
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
                            Toast.makeText(CertificateActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(CertificateActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Response reached---------------value in 's' variable
                dialog.dismiss();
            }
            else {
                dialog.dismiss();
                noCertificateTxt.setVisibility(View.GONE);
                animationView.setVisibility(View.VISIBLE);
                yourcertificateTxt.setVisibility(View.GONE);
            }
        }
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        new GetCertificateList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    protected void onStart() {
        super.onStart();

            // Check if permission to write to external storage is granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // Request the permission if not granted
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                   // Toast.makeText(this, "Allow for Certificate", Toast.LENGTH_SHORT).show();
                }

        }

    }
}

