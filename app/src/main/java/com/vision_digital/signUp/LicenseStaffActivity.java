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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vision_digital.R;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.databinding.ActivityLicenseStaffBinding;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.oboardingScreen.IntroActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LicenseStaffActivity extends AppCompatActivity {
    ActivityLicenseStaffBinding binding;
    ArrayList<String> trainUnitList = new ArrayList<>();
    ArrayList<String> licenseeCompList = new ArrayList<>();
    ArrayList<String> licenseeCompIdList = new ArrayList<>();

    ArrayList<String> jobRoleList = new ArrayList<>();
    ArrayList<String> qualification = new ArrayList<>();

    ProgressDialog dialog;
    String urlTrainUnitList = "";
    String urlLicenseeComp = "";
    String urlJobRole = "";
    String urlQualification = "";
    String licenseeId = "";
    String qualificationLicensee = "";
    String jobRole = "";
    String operational_unit = "";
    String category = "";
    String licenseeCompany = "";
    String nameLicensee = "";
    String genderLicensee = "";

        String dob ="";

        String urlRegister = "";
        private int sid;
        private boolean isIntro = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            binding= DataBindingUtil.setContentView(this,R.layout.activity_license_staff);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.show();
        urlTrainUnitList = getApplicationContext().getString(R.string.apiURL1) + "selectOperationalUnit";
        urlLicenseeComp = getApplicationContext().getString(R.string.apiURL1) + "selectLicenseeCompany";
        urlJobRole = getApplicationContext().getString(R.string.apiURL1) + "selectJobRole";
        urlQualification = getApplicationContext().getString(R.string.apiURL1) + "selectQualification";
        urlRegister = getApplicationContext().getString(R.string.apiURL) + "registerLearners";

        SharedPreferences userIsRegisteredSuccessful = this.getSharedPreferences("CNB", MODE_PRIVATE);
        sid = userIsRegisteredSuccessful.getInt("sid", 0);
        isIntro = userIsRegisteredSuccessful.getBoolean("intro",false);


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (checkValidation()){
            binding.btnNextLicenseeStaff.setBackgroundResource(R.drawable.topper_button);
            binding.btnNextLicenseeStaff.setEnabled(true);
            binding.btnNextLicenseeStaff.setTextColor(Color.parseColor("#ffffff"));
        }else {
            binding.btnNextLicenseeStaff.setBackgroundResource(R.drawable.disable_btn);
        }
        binding.operationUnitRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle radio button selection here
                binding.frameLicensee.setVisibility(View.VISIBLE);
                binding.frameUnit.setVisibility(View.VISIBLE);
                checkEntry();
                switch (checkedId) {
                    case R.id.train:
                        // Call a function for radioButton1 selection
                        category = "Train";
                        new getTrainUnitData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        break;
                    case R.id.station:
                        // Call a function for radioButton2 selection
                        category = "Station";
                        new getTrainUnitData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        break;
                    // Add cases for other radio buttons if needed
                }
            }
        });

        binding.licenseeDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
                checkEntry();
            }
        });

        binding.genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle radio button selection here
                checkEntry();

                switch (checkedId) {
                    case R.id.male:
                        // Call a function for radioButton1 selection
                        genderLicensee = "Male";
                        break;
                    case R.id.female:
                        genderLicensee = "Female";
                        // Call a function for radioButton2 selection

                        break;
                    case R.id.other:
                        genderLicensee = "Other";
                        break;
                    // Add cases for other radio buttons if needed
                }
            }
        });

        binding.spinnerSelectLicensee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                licenseeId = licenseeCompIdList.get(position);

                checkEntry();
                Log.e(TAG, "onItemSelected: " + licenseeCompIdList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: " + "onNothingSelected");
            }
        });

        binding.spinnerQualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qualificationLicensee = parent.getItemAtPosition(position).toString();

                checkEntry();
                Log.e(TAG, "onItemSelected: " + qualificationLicensee);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: " + "onNothingSelected");
            }
        });

        binding.spinnerJobRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jobRole = parent.getItemAtPosition(position).toString();
                checkEntry();


                Log.e(TAG, "onItemSelected: " + jobRole);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: " + "onNothingSelected");
            }
        });

        binding.spinnerSelectTrain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                operational_unit = parent.getItemAtPosition(position).toString();


                checkEntry();
                Log.e(TAG, "onItemSelected: " + category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: " + "onNothingSelected");
            }
        });

        binding.btnNextLicenseeStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()){
                    new RegisterUserProfile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });

        binding.nameLicensee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                nameLicensee = s.toString();
                checkEntry();

            }
        });



        new getQualificationData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new getLicenseeComp().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new getJobRoleData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


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
                        binding.licenseeDob.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private boolean checkEntry() {
        if (nameLicensee.isEmpty() || nameLicensee.equals("")){

            return false;
        }
        if (genderLicensee.isEmpty() || genderLicensee.equals("")){


            return false;
        }
        if (qualification.isEmpty() || qualification.equals("") || qualification.equals("Select Qualification")){

            return false;
        }
        if (jobRole.isEmpty() || jobRole.equals("") || jobRole.equals("Select Job Role")){
            return false;
        }
        if (operational_unit.isEmpty() || operational_unit.equals("0")){

            return false;
        }

        binding.btnNextLicenseeStaff.setBackgroundResource(R.drawable.topper_button);

        return true;

    }

    private boolean checkValidation() {
        if (nameLicensee.isEmpty() || nameLicensee.equals("")){
            Toast.makeText(this, "Enter your name!..", Toast.LENGTH_SHORT).show();

            return false;
        }
        if (genderLicensee.isEmpty() || genderLicensee.equals("")){
            Toast.makeText(this, "Select Gender!..", Toast.LENGTH_SHORT).show();

            return false;
        }
        if (qualification.isEmpty() || qualification.equals("") || qualification.equals("Select Qualification")){
            Toast.makeText(this, "Select Qualification!..", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (jobRole.isEmpty() || jobRole.equals("") || jobRole.equals("Select Job Role")){
            Toast.makeText(this, "Select your Job Role!..", Toast.LENGTH_SHORT).show();

            return false;
        }
        if (operational_unit.isEmpty() || operational_unit.equals("0")){
            Toast.makeText(this, "Select Operational Unit!..", Toast.LENGTH_SHORT).show();
            return false;
        }

        binding.btnNextLicenseeStaff.setBackgroundResource(R.drawable.topper_button);

        return true;

    }

    class getJobRoleData extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(LicenseStaffActivity.this);
           // int versionCode = BuildConfig.VERSION_CODE;

            Log.e(TAG, "doInBackground: " +"do in background" );
            String param = "";

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlJobRole, "GET", param);
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
                            jobRoleList.clear();
                            JSONObject dataObj = jsonObject.getJSONObject("data");

                            jobRoleList.add("Select Job Role");
                            jobRoleList.add(dataObj.getString("Cook"));
                            jobRoleList.add(dataObj.getString("Vendor"));
                            jobRoleList.add(dataObj.getString("Pantry Manager"));
                            jobRoleList.add(dataObj.getString("Shopkeeper"));
                            jobRoleList.add(dataObj.getString("Others"));

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(LicenseStaffActivity.this, android.R.layout.simple_spinner_item, jobRoleList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerJobRole.setAdapter(adapter);

                            break;

                        case "false":
                            Toast.makeText(LicenseStaffActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LicenseStaffActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
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

            JSONParser jsonParser = new JSONParser(LicenseStaffActivity.this);
          //  int versionCode = BuildConfig.VERSION_CODE;

            Log.e(TAG, "doInBackground: " +"do in background" );
            String param = "";

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
                            qualification.clear();
                            JSONObject dataObj = jsonObject.getJSONObject("data");

                            qualification.add("Select Qualification");
                            qualification.add(dataObj.getString("Non Matriculate"));
                            qualification.add(dataObj.getString("Matriculate"));
                            qualification.add(dataObj.getString("Higher secondary"));
                            qualification.add(dataObj.getString("Graduate"));
                            qualification.add(dataObj.getString("Post Graduate"));
                            qualification.add(dataObj.getString("Others"));

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(LicenseStaffActivity.this, android.R.layout.simple_spinner_item, qualification);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerQualification.setAdapter(adapter);

                            break;

                        case "false":
                            Toast.makeText(LicenseStaffActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LicenseStaffActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class getTrainUnitData extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(LicenseStaffActivity.this);
          //  int versionCode = BuildConfig.VERSION_CODE;

            Log.e(TAG, "doInBackground: " +"do in background" );
            String param = "";

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlTrainUnitList, "GET", param);
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
                            JSONObject trainJsonObj = dataObj.getJSONObject("unit_train");
                            JSONObject unitJsonObj = dataObj.getJSONObject("unit_station");



                            if (binding.station.isChecked()){
                                trainUnitList.clear();
                                trainUnitList.add("Select Unit");
                                trainUnitList.add(unitJsonObj.getString("Refreshment Room"));
                                trainUnitList.add(unitJsonObj.getString("Jan Aahaar"));
                                trainUnitList.add(unitJsonObj.getString("Base Kitchen"));
                                trainUnitList.add(unitJsonObj.getString("Retiring Room"));
                                trainUnitList.add(unitJsonObj.getString("Executive Launge"));
                                trainUnitList.add(unitJsonObj.getString("Food Court"));
                                trainUnitList.add(unitJsonObj.getString("Others"));
                            }else{
                                trainUnitList.clear();
                                trainUnitList.add("Select Train");
                                trainUnitList.add(trainJsonObj.getString("Vande Bharat"));
                                trainUnitList.add(trainJsonObj.getString("Rajdhani"));
                                trainUnitList.add(trainJsonObj.getString("Duranto"));
                                trainUnitList.add(trainJsonObj.getString("Shatabdi"));
                                trainUnitList.add(trainJsonObj.getString("Express"));
                                trainUnitList.add(trainJsonObj.getString("Mail"));
                                trainUnitList.add(trainJsonObj.getString("Others"));
                            }



                            ArrayAdapter<String> adapter = new ArrayAdapter<>(LicenseStaffActivity.this, android.R.layout.simple_spinner_item, trainUnitList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerSelectTrain.setAdapter(adapter);



                            break;

                        case "false":
                            Toast.makeText(LicenseStaffActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LicenseStaffActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class getLicenseeComp extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(LicenseStaffActivity.this);
          //  int versionCode = BuildConfig.VERSION_CODE;

            Log.e(TAG, "doInBackground: " +"do in background" );
            String param = "";

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlLicenseeComp, "GET", param);
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
                            licenseeCompList.clear();
                            licenseeCompIdList.clear();
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            licenseeCompList.add("Select of Licensee");
                            licenseeCompIdList.add("0");
                            for (int i=0; i<dataArray.length(); i++){
                                JSONObject jsonDataObj = dataArray.getJSONObject(i);
                                licenseeCompList.add(jsonDataObj.getString("name"));
                                licenseeCompIdList.add(jsonDataObj.getString("id"));

                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(LicenseStaffActivity.this, android.R.layout.simple_spinner_item, licenseeCompList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerSelectLicensee.setAdapter(adapter);

                            break;

                        case "failure":
                            Toast.makeText(LicenseStaffActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LicenseStaffActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(LicenseStaffActivity.this);
            //int versionCode = BuildConfig.VERSION_CODE;
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
            String param ="sid=" + sid +"&name=" + nameLicensee  +"&gender=" + genderLicensee + "&dob=" +  dob
                    +"&type=" + SelectProfileActivity.profileType + "&device_details="+ details +
                    "&token_id=" + SelectProfileActivity.tokenId +"&qualification=" + qualificationLicensee +"&job_role=" + jobRole
                    +"&operational_unit=" + category + "&category=" + operational_unit + "&licensee_company="+ licenseeId
                    +"&mobile=" + OTPActivity.mobileNum ;

            Log.e(TAG, "param: "+ param);

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
                            SharedPreferences.Editor editor = getSharedPreferences("CNB", MODE_PRIVATE).edit();
                            editor.putBoolean("registered", true);
                            editor.putString("profileName", nameLicensee);
                            editor.apply();
                            Toast.makeText(LicenseStaffActivity.this, message, Toast.LENGTH_SHORT).show();

                            if (isIntro){
                                startActivity(new Intent(LicenseStaffActivity.this, DashboardActivity.class));
                                finishAffinity();

                            }else{
                                startActivity(new Intent(LicenseStaffActivity.this, IntroActivity.class));
                                finishAffinity();

                            }

                            break;
                        case "maintainance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LicenseStaffActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = LicenseStaffActivity.this.getLayoutInflater();
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
                            Toast.makeText(LicenseStaffActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LicenseStaffActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}