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
import com.vision_digital.databinding.ActivityOtherBinding;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.oboardingScreen.IntroActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class OtherActivity extends AppCompatActivity {
    ActivityOtherBinding binding;

    String urlQualification = "";
    ProgressDialog dialog;
    ArrayList<String> qualificationList = new ArrayList<>();
    String urlRegister = "";



    private int sid;

    String name = "";
    String genderOther = "";
    String qualification = "";
    String area_of_interest = "";
    private String dob = "";
    boolean isIntro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other);

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
            binding.btnNextOther.setBackgroundResource(R.drawable.topper_button);
        }else {
            binding.btnNextOther.setBackgroundResource(R.drawable.disable_btn);
            binding.btnNextOther.setEnabled(true);
            binding.btnNextOther.setTextColor(Color.parseColor("#ffffff"));
        }

        binding.otherDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
                checkEntry();
            }
        });

        binding.btnNextOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()){
                    new RegisterUserProfile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });

        binding.nameOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                name = s.toString();
                checkEntry();
            }
        });
        binding.otherGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                checkEntry();
                genderOther = selectedRadioButton.getText().toString();

                Toast.makeText(OtherActivity.this, "Selected: " + genderOther, Toast.LENGTH_SHORT).show();
            }
        });

        binding.spinnerQualificationOther.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qualification = parent.getItemAtPosition(position).toString();
                checkEntry();

                Log.e(TAG, "onItemSelected: " + qualification);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: " + "onNothingSelected");
            }
        });

        binding.areaOfInterest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                area_of_interest = s.toString();
                checkEntry();
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.show();
        new getQualificationData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                        binding.otherDob.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private boolean checkEntry() {
        if (name.isEmpty() || name.equals("")){
            return false;
        }
        if (genderOther.isEmpty() || genderOther.equals("")){
            return false;
        }
        if (qualification.isEmpty() || qualification.equals("") || qualification.equals("Select Qualification")){

            return false;
        }
        if (area_of_interest.isEmpty() || area_of_interest.equals("")){

            return false;
        }

        binding.btnNextOther.setBackgroundResource(R.drawable.topper_button);

        return true;

    }
    private boolean checkValidation() {
        if (name.isEmpty() || name.equals("")){
            Toast.makeText(this, "Enter your name!..", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (genderOther.isEmpty() || genderOther.equals("")){
            Toast.makeText(this, "Select Gender!..", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (qualification.isEmpty() || qualification.equals("") || qualification.equals("Select Qualification")){
            Toast.makeText(this, "Select Qualification!..", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (area_of_interest.isEmpty() || area_of_interest.equals("")){
            Toast.makeText(this, "Enter Area of Interest!..", Toast.LENGTH_SHORT).show();

            return false;
        }


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

            JSONParser jsonParser = new JSONParser(OtherActivity.this);
          //  int versionCode = BuildConfig.VERSION_CODE;

            Log.e(TAG, "doInBackground: " + "do in background");
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
                            qualificationList.clear();
                            JSONObject dataObj = jsonObject.getJSONObject("data");

                            qualificationList.add("Select Qualification");
                            qualificationList.add(dataObj.getString("Non Matriculate"));
                            qualificationList.add(dataObj.getString("Matriculate"));
                            qualificationList.add(dataObj.getString("Higher secondary"));
                            qualificationList.add(dataObj.getString("Graduate"));
                            qualificationList.add(dataObj.getString("Post Graduate"));
                            qualificationList.add(dataObj.getString("Others"));

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(OtherActivity.this, android.R.layout.simple_spinner_item, qualificationList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerQualificationOther.setAdapter(adapter);

                            Log.e(TAG, "onPostExecute: " + dataObj.getString("others"));
                            break;

                        case "false":
                            Toast.makeText(OtherActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(OtherActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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

            JSONParser jsonParser = new JSONParser(OtherActivity.this);
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

            Log.e(TAG, "doInBackground: " + "do in background");
            String param = "sid=" + sid + "&name=" + name + "&email=" + "" + "&dob=" +  dob+ "&gender=" + genderOther
                    + "&type=" + SelectProfileActivity.profileType + "&device_details=" + details +
                    "&token_id=" + SelectProfileActivity.tokenId + "&qualification=" + qualification + "&area_of_intrest=" + area_of_interest
                    +"&mobile=" + OTPActivity.mobileNum ;

            Log.e(TAG, "param: "+param);
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
                            Toast.makeText(OtherActivity.this, message, Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = getSharedPreferences("CNB", MODE_PRIVATE).edit();
                            editor.putBoolean("registered", true);
                            editor.putString("profileName", name);
                            editor.apply();
                            if (isIntro){
                                startActivity(new Intent(OtherActivity.this, DashboardActivity.class));
                                finishAffinity();

                            }else{
                                startActivity(new Intent(OtherActivity.this, IntroActivity.class));
                                finishAffinity();

                            }

                            break;
                        case "maintainance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OtherActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = OtherActivity.this.getLayoutInflater();
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
                            Toast.makeText(OtherActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(OtherActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}