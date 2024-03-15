package com.vision_classes.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vision_classes.BuildConfig;
import com.vision_classes.R;
import com.vision_classes.helperClasses.JSONParser;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class NeedHelpActivity extends AppCompatActivity {

    int sid = 0;
    String priority = "";
    Spinner queryPrio;
    String category = "";
    Spinner queryCat;
    String query_title = "", queryDescription = "";
    TextInputLayout title, description;
    String raiseQueryUrl = "";

    TextView submitBtn;

    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_help);

        raiseQueryUrl = getApplicationContext().getString(R.string.apiURL) + "raiseQuery";

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        description = findViewById(R.id.queryDesc);
        title = findViewById(R.id.queryTitle);
        queryPrio = findViewById(R.id.queryPriorty);
        queryCat = findViewById(R.id.category);
        queryCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    category = String.valueOf(queryCat.getSelectedItemId());
                } else {
                    category = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query_title = title.getEditText().getText().toString().trim();
                queryDescription = description.getEditText().getText().toString().trim();
                if (!category.equals("") && !priority.equals("") && !query_title.equals("") && !queryDescription.equals("")) {
                    new RaiseQuery().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Toast.makeText(NeedHelpActivity.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        queryPrio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    priority = String.valueOf(queryPrio.getSelectedItemId());
                } else {
                    priority = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        SharedPreferences userIsRegisteredSuccessful = getSharedPreferences("CNB", MODE_PRIVATE);
        boolean registered = userIsRegisteredSuccessful.getBoolean("registered", false);
        sid = userIsRegisteredSuccessful.getInt("sid", 0);

    }

    class RaiseQuery extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(NeedHelpActivity.this);
            int versionCode = BuildConfig.VERSION_CODE;
            String param = "app_version=" + versionCode + "&sid=" + sid + "&category=" + category + "&title=" + query_title + "&description=" + queryDescription + "&priority=" + priority;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(raiseQueryUrl, "POST", param);
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
                    Log.e("status", String.valueOf(status));

//                    JSONObject dataObj = jsonObject.getJSONObject("data");
                    String message = jsonObject.getString("data");
                    Log.e("message", String.valueOf(message));
                    switch (status) {
                        case "success":
                            //Running Fine

                            //Undermaintance
                            Log.e("Enter", String.valueOf(message));

                            AlertDialog.Builder qdialogBuilder = new AlertDialog.Builder(NeedHelpActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater qinflater = NeedHelpActivity.this.getLayoutInflater();
                            View qdialogView = qinflater.inflate(R.layout.under_maintanance_dialog, null);
                            qdialogBuilder.setView(qdialogView);
                            //Alert Dialog Layout work
                            TextView qmaintainanceContent = qdialogView.findViewById(R.id.underMaintananceContent);
//                            String qmsgContent = dataObj.getString("message");
                            qmaintainanceContent.setText(Html.fromHtml(message));

                            TextView qbtnOK = qdialogView.findViewById(R.id.btnOK);
                            qbtnOK.setText("OK");
                            qbtnOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onBackPressed();
                                }
                            });
                            AlertDialog qalertDialog = qdialogBuilder.create();
                            qalertDialog.show();
                            qalertDialog.setCanceledOnTouchOutside(false);

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NeedHelpActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = NeedHelpActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
//                            String msgContent = dataObj.getString("message");
                            maintainanceContent.setText(Html.fromHtml(message));

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
                            Toast.makeText(NeedHelpActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(NeedHelpActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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
