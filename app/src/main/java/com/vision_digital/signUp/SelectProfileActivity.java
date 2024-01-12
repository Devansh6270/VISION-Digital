package com.vision_digital.signUp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vision_digital.R;
import com.vision_digital.databinding.ActivitySelectProfileBinding;
import com.vision_digital.helperClasses.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectProfileActivity extends AppCompatActivity {
    ActivitySelectProfileBinding binding;
    ProgressDialog dialog;
    ArrayList<String> profileTypeList = new ArrayList<>();
    ArrayList<String> profileTypeKeyList = new ArrayList<>();
    String url = "";

    Boolean selected=false;

    public static String tokenId = "";
    public static String profileType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_profile);

        url = getApplicationContext().getString(R.string.apiURL1) + "selectProfileIrctc";
        Log.e(TAG, "selectProfileIrctc: " + "selectProfileIrctc");
        dialog = new ProgressDialog(this);
        dialog.show();
        new getSelectProfile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                }
                tokenId = task.getResult();
                Log.e(TAG, "tokenId: " + tokenId);

            }
        });
        binding.btnNextSelectProfile.setBackgroundResource(R.drawable.disable_btn);
        binding.btnNextSelectProfile.setEnabled(true);
        binding.btnNextSelectProfile.setTextColor(Color.parseColor("#ffffff"));

        binding.spinnerProfileType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                profileType = parent.getItemAtPosition(position).toString();
                profileType = profileTypeKeyList.get(position);


                if (!profileType.equals("I am____")){
                    binding.btnNextSelectProfile.setBackgroundResource(R.drawable.topper_button);
                }else {
                    binding.btnNextSelectProfile.setBackgroundResource(R.drawable.disable_btn);
                    binding.btnNextSelectProfile.setEnabled(true);
                    binding.btnNextSelectProfile.setTextColor(Color.parseColor("#ffffff"));
                }

                Log.e(TAG, "onItemSelected: " + profileType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected=false;
                Log.e(TAG, "onNothingSelected: " + "onNothingSelected");
                binding.btnNextSelectProfile.setEnabled(true);
                binding.btnNextSelectProfile.setTextColor(Color.parseColor("#ffffff"));
            }
        });


        binding.btnNextSelectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!profileType.equals("I am____")) {
                    if (profileType.equals("irctc")) {
                        Log.e(TAG, "onClick: " + "click1");
                        startActivity(new Intent(SelectProfileActivity.this, IRCTCStaffDetailActivity.class));
                    } else if (profileType.equals("licensee")) {
                        Log.e(TAG, "onClick: " + "click1");
                        startActivity(new Intent(SelectProfileActivity.this, LicenseStaffActivity.class));
                    } else if (profileType.equals("agent")) {
                        Log.e(TAG, "onClick: " + "click1");
                        startActivity(new Intent(SelectProfileActivity.this, AgentDetailsActivity.class));
                    } else {
                        Log.e(TAG, "onClick: " + "click1");
                        startActivity(new Intent(SelectProfileActivity.this, OtherActivity.class));
                    }
                }else{

                    Toast.makeText(SelectProfileActivity.this, "Please select your Profile!..", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    class getSelectProfile extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(SelectProfileActivity.this);

            Log.e(TAG, "doInBackground: " + "do in background");
            String param = "";

            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "GET", param);
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
                    dialog.dismiss();
                    switch (status) {
                        case "true":
                            //Running Fine
                            JSONObject dataObj = jsonObject.getJSONObject("data");
                            profileTypeList.add("I am____");
                            profileTypeList.add(dataObj.getString("irctc"));
                            profileTypeList.add(dataObj.getString("licensee"));
                            profileTypeList.add(dataObj.getString("agent"));
                            profileTypeList.add(dataObj.getString("others"));


                            profileTypeKeyList.add("I am____");
                            profileTypeKeyList.add("irctc");
                            profileTypeKeyList.add("licensee");
                            profileTypeKeyList.add("agent");
                            profileTypeKeyList.add("others");

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectProfileActivity.this, android.R.layout.simple_spinner_item, profileTypeList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerProfileType.setAdapter(adapter);

                            Log.e(TAG, "onPostExecute: " + dataObj.getString("others"));
                            break;
                        case "maintainance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectProfileActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = SelectProfileActivity.this.getLayoutInflater();
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
                        case "false":
                            Toast.makeText(SelectProfileActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(SelectProfileActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}