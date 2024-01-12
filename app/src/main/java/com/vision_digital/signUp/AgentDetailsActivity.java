package com.vision_digital.signUp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vision_digital.R;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.databinding.ActivityAgentDetailsBinding;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.oboardingScreen.IntroActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AgentDetailsActivity extends AppCompatActivity {

    ActivityAgentDetailsBinding binding;
    ArrayList<String> agentPSPList = new ArrayList<>();
    ArrayList<String> agentPSPIdList = new ArrayList<>();
    ArrayList<String> qualificationAgent = new ArrayList<>();
    ProgressDialog dialog;
    String urlQualification = "";
    String urlTicketingAgent = "";

    String dob = "";
    String urlRegister = "";

    String radioSelected="";

    int count =0;
    String qualification_agent = "";
    String gender_agent = "";
    String service_provider = "";
    String agent_name = "";
   public String pspType = "";

    private int sid;
    boolean isIntro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_agent_details);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.show();
        urlQualification = getApplicationContext().getString(R.string.apiURL1) + "selectQualification";
        urlTicketingAgent = getApplicationContext().getString(R.string.apiURL1) + "selectTicketingAgent";

        urlRegister = getApplicationContext().getString(R.string.apiURL) + "registerLearners";

        new getQualificationData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        SharedPreferences userIsRegisteredSuccessful = this.getSharedPreferences("CNB", MODE_PRIVATE);
        sid = userIsRegisteredSuccessful.getInt("sid", 0);
        isIntro = userIsRegisteredSuccessful.getBoolean("intro",false);

        binding.backBtnAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (checkValidation()){
            binding.btnNextAgent.setBackgroundResource(R.drawable.topper_button);
            binding.btnNextAgent.setEnabled(true);
            binding.btnNextAgent.setTextColor(Color.parseColor("#ffffff"));
        }else {
            binding.btnNextAgent.setBackgroundResource(R.drawable.disable_btn);
        }
        binding.spinnerQualificationAgent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qualification_agent = parent.getItemAtPosition(position).toString();
                count++;
                checkEntry();

                Log.e(TAG, "onItemSelected: " + qualification_agent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: " + "onNothingSelected");
            }
        });

        binding.agentDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
               // checkEntry();
            }
        });



        binding.spinnerSelectPSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                service_provider = agentPSPIdList.get(position);
                count++;
                checkEntry();
                Log.e(TAG, "onItemSelected: " + qualification_agent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: " + "onNothingSelected");
            }
        });

        binding.genderRadioGroupAgent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                gender_agent = selectedRadioButton.getText().toString();
                count++;
                checkEntry();
                Toast.makeText(AgentDetailsActivity.this, "Selected: " + gender_agent, Toast.LENGTH_SHORT).show();
            }
        });
        binding.spinnerSelectPSP.setVisibility(View.GONE);

        binding.pspRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);

                pspType = selectedRadioButton.getText().toString();
                if (pspType.equals("B2B PSP")){
                    pspType= "B2B";
                    binding.spinnerSelectPSP.setVisibility(View.VISIBLE);
                }else{
                    pspType= "ICS";
                    binding.spinnerSelectPSP.setVisibility(View.VISIBLE);
                }
                checkEntry();
                new getTicketingAgent().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                Toast.makeText(AgentDetailsActivity.this, "Selected: " + pspType, Toast.LENGTH_SHORT).show();
            }
        });

        binding.nameAgent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                count++;
                checkEntry();
//                binding.btnNextAgent.setBackgroundResource(R.drawable.topper_button);
//                binding.btnNextAgent.setEnabled(true);
//                binding.btnNextAgent.setTextColor(Color.parseColor("#ffffff"));
                agent_name = s.toString();
            }
        });



        binding.btnNextAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if (checkValidation()) {
                  dialog.show();
                    new RegisterUserProfile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Set the selected date to the EditText
                        // Format the selected date
                        Calendar selectedDateCalendar = Calendar.getInstance();
                        selectedDateCalendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("d-MMM-yy", Locale.getDefault());
                        String selectedDate = sdf.format(selectedDateCalendar.getTime());
                        dob = selectedDate;
                        binding.agentDob.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private boolean checkValidation() {
        if (agent_name.isEmpty() || agent_name.equals("")){
            return false;
        }
        if (gender_agent.isEmpty() || gender_agent.equals("")){
            return false;
        }
        if (qualification_agent.isEmpty() || qualification_agent.equals("") || qualification_agent.equals("Select Qualification")){
            Toast.makeText(this, "Select Qualification!..", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pspType.isEmpty() || pspType.equals("")){
            return false;
        }
        if (service_provider.isEmpty() || pspType.equals("0")){
            Toast.makeText(this, "Select Service Provider!..", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkEntry() {
        if (agent_name.isEmpty() || agent_name.equals("")){
            return false;
        }
        if (gender_agent.isEmpty() || gender_agent.equals("")){
            return false;
        }
        if (qualification_agent.isEmpty() || qualification_agent.equals("") || qualification_agent.equals("Select Qualification")){
            return false;
        }
        if (pspType.isEmpty() || pspType.equals("")){
            return false;
        }
        if (service_provider.isEmpty() || pspType.equals("0")){
            return false;
        }
        binding.btnNextAgent.setBackgroundResource(R.drawable.topper_button);

        return true;
    }

    class getQualificationData extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(AgentDetailsActivity.this);
           // int versionCode = BuildConfig.VERSION_CODE;

            Log.e(TAG, "doInBackground: " +"do in background" );
            String param = "";
            Log.e(TAG, "Agent: " +"do in background" );
            JSONObject jsonObject = jsonParser.makeHttpRequest(urlQualification, "GET", param);
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
                            qualificationAgent.clear();
                            JSONObject dataObj = jsonObject.getJSONObject("data");

                            qualificationAgent.add("Select Qualification");
                            qualificationAgent.add(dataObj.getString("Non Matriculate"));
                            qualificationAgent.add(dataObj.getString("Matriculate"));
                            qualificationAgent.add(dataObj.getString("Higher secondary"));
                            qualificationAgent.add(dataObj.getString("Graduate"));
                            qualificationAgent.add(dataObj.getString("Post Graduate"));
                            qualificationAgent.add(dataObj.getString("Others"));

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(AgentDetailsActivity.this, android.R.layout.simple_spinner_item, qualificationAgent);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerQualificationAgent.setAdapter(adapter);

                            break;

                        case "false":
                            Toast.makeText(AgentDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(AgentDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class getTicketingAgent extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(AgentDetailsActivity.this);
         //   int versionCode = BuildConfig.VERSION_CODE;

            Log.e(TAG, "doInBackground: " +"do in background" );
            String param = "type=" +  pspType;
            Log.e(TAG, "param: "+param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlTicketingAgent, "POST", param);
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
                        case "success":
                            //Running Fine
                            agentPSPList.clear();
                            agentPSPIdList.clear();
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            if (binding.b2b.isChecked()){
                                agentPSPList.add("Select Your B2B PSP");
                            }else{
                                agentPSPList.add("Select Your ICS PSP");
                            }

                            agentPSPIdList.add("0");
                            for (int i=0; i<dataArray.length(); i++){
                                JSONObject jsonDataObj = dataArray.getJSONObject(i);
                                agentPSPIdList.add(jsonDataObj.getString("id"));
                                agentPSPList.add(jsonDataObj.getString("service_provider"));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(AgentDetailsActivity.this, android.R.layout.simple_spinner_item, agentPSPList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerSelectPSP.setAdapter(adapter);

                            break;

                        case "failure":
                            Toast.makeText(AgentDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(AgentDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class RegisterUserProfile extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(AgentDetailsActivity.this);
           // int versionCode = BuildConfig.VERSION_CODE;

            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            String device = Build.DEVICE;
            String product = Build.PRODUCT;
            String modelID = Build.ID;
            String modelName = Build.DISPLAY;
            String androidVersion = Build.VERSION.RELEASE;
            int sdkVersion = Build.VERSION.SDK_INT;

            String details = "Manufacturer: " + manufacturer +
                    "\nModel: " + model +
                    "\nDevice: " + device +
                    "\nProduct: " + product +
                    "\nModel ID: " + modelID +
                    "\nModel Name: " + modelName +
                    "\nAndroid Version: " + androidVersion +
                    "\nSDK Version: " + sdkVersion;

            Log.d("DeviceDetails", details);

            Log.e(TAG, "doInBackground: " +"do in background" );
            String param = "sid=" + sid +"&name=" + agent_name + "&email=" + "" +"&dob=" +dob + "" +"&gender=" + gender_agent
                    +"&type=" + SelectProfileActivity.profileType + "&device_details="+ details +
                    "&token_id=" + SelectProfileActivity.tokenId +"&qualification=" + qualification_agent +"&service_provider=" +service_provider
                    +"&mobile=" + OTPActivity.mobileNum ;

            Log.e(TAG, "AgentDeatis: " +param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlRegister, "POST", param);
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
                        case "success":
                            //Running Fine
                            JSONObject dataObj = jsonObject.getJSONObject("data");
                            String message = dataObj.getString("message");
                            Toast.makeText(AgentDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = getSharedPreferences("CNB", MODE_PRIVATE).edit();
                            editor.putBoolean("registered", true);
                            editor.putString("profileName", agent_name);
                            editor.apply();
                            if (isIntro){
                                startActivity(new Intent(AgentDetailsActivity.this, DashboardActivity.class));
                                finishAffinity();

                            }else{
                                startActivity(new Intent(AgentDetailsActivity.this, IntroActivity.class));
                                finishAffinity();

                            }


                            finishAffinity();
                            break;
                        case "maintainance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AgentDetailsActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = AgentDetailsActivity.this.getLayoutInflater();
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
                            Toast.makeText(AgentDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(AgentDetailsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}