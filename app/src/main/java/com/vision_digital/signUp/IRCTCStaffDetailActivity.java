package com.vision_digital.signUp;

import static android.content.ContentValues.TAG;

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
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.vision_digital.R;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.databinding.ActivityIrctcstaffDetailBinding;
import com.vision_digital.helperClasses.JSONParsePOST;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.oboardingScreen.IntroActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class IRCTCStaffDetailActivity extends AppCompatActivity {
    ActivityIrctcstaffDetailBinding binding;
     String empCode = "";
     String dob = "";
     String grade = "";
     String department = "";
     String designation = "";
     String zone = "";
     String place_of_posting = "";
     String name = "";
     String gender = "";

    ProgressDialog dialog;

    String url = "";
    String urlCheckLearnerByCode = "";
    private int sid;
    String mobile = "";
    boolean isIntro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_irctcstaff_detail);
        url = getApplicationContext().getString(R.string.apiURL) + "registerLearners";
        urlCheckLearnerByCode = getApplicationContext().getString(R.string.apiURL) + "checkLearnerByCode";


        SharedPreferences userIsRegisteredSuccessful = this.getSharedPreferences("CNB", MODE_PRIVATE);
        sid = userIsRegisteredSuccessful.getInt("sid", 0);
        mobile = userIsRegisteredSuccessful.getString("mobileNo","NO_NAME");
        isIntro = userIsRegisteredSuccessful.getBoolean("intro",false);


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (checkEntry()){
            binding.btnNextIrctcStaff.setBackgroundResource(R.drawable.topper_button);
        }else {
            binding.btnNextIrctcStaff.setBackgroundResource(R.drawable.disable_btn);
            binding.btnNextIrctcStaff.setEnabled(true);
            binding.btnNextIrctcStaff.setTextColor(Color.parseColor("#ffffff"));
        }
        binding.empCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                empCode = s.toString();
                binding.btnNextIrctcStaff.setBackgroundResource(R.drawable.topper_button);
            }
        });

        binding.empDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        binding.empDob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                binding.btnNextIrctcStaff.setBackgroundResource(R.drawable.topper_button);
                binding.btnNextIrctcStaff.setEnabled(true);
                binding.btnNextIrctcStaff.setTextColor(Color.parseColor("#ffffff"));

            }
        });

        binding.btnNextIrctcStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidation()){
                    dialog = new ProgressDialog(IRCTCStaffDetailActivity.this);
                    dialog.setMessage("Please wait..");
                    dialog.show();

                    new CheckDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }


            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private boolean checkValidation() {
        if (empCode.isEmpty() || empCode.equals("")){
            Toast.makeText(this, "Enter your Employee code!..", Toast.LENGTH_SHORT).show();
            binding.empCode.setError("Please enter a valid employee code");
            return false;
        }
        if (dob.isEmpty() || dob.equals("")){
            Toast.makeText(this, "Select your Date of Birth!..", Toast.LENGTH_SHORT).show();
            binding.empDob.setError("Please enter the registered date of birth");
            return false;
        }

        return true;

    }

    private boolean checkEntry() {
        if (empCode.isEmpty() || empCode.equals("")){
            return false;
        }
        if (dob.isEmpty() || dob.equals("")){
            return false;
        }

        return true;

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
                        binding.empDob.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    class CheckDetails extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(IRCTCStaffDetailActivity.this);
           // int versionCode = BuildConfig.VERSION_CODE;

            Log.e(TAG, "doInBackground: " +"do in background" );
            String param = "learner_code=" + empCode;

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlCheckLearnerByCode, "POST", param);
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
                    boolean status = jsonObject.getBoolean("success");
                    dialog.dismiss();
                    if (status) {//Running Fine
                        JSONObject dataObj = jsonObject.getJSONObject("data");

                        String emp_codeS = dataObj.getString("emp_code");
                        String dobS = dataObj.getString("dob");
                        grade = dataObj.getString("grade");
                        department = dataObj.getString("department");
                        designation = dataObj.getString("designation");
                        zone = dataObj.getString("zone");
                        place_of_posting = dataObj.getString("place_of_posting");
                        name = dataObj.getString("name");
                        gender = dataObj.getString("gender");

                        if (empCode.equals(emp_codeS)) {
                            if (dob.equals(dobS)) {
                                dialog.show();
                                new RegisterUserProfile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                SharedPreferences.Editor editor = getSharedPreferences("CNB", MODE_PRIVATE).edit();
                                editor.putBoolean("registered", true);
                                editor.putString("profileName", name);
                                editor.apply();
                            } else {
                                binding.empCode.setError("Enter correct Date of Birth!..");
                            }
                        } else {
                            binding.empCode.setError("Enter correct Employee Code!..");
                        }

                    }
                    else if (!status) {
                        Toast.makeText(IRCTCStaffDetailActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(IRCTCStaffDetailActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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

            JSONParsePOST jsonParser = new JSONParsePOST(IRCTCStaffDetailActivity.this);
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
            String param = "learner_code=" + empCode + "&grade=" + grade + "&department=" + department
                    +"&designation=" + designation +"&zone=" + zone +"&place_of_posting=" +place_of_posting
                    +"&sid=" + sid +"&name=" + name + "&email=" + "" +"&dob=" + dob +"&gender=" + gender
                    +"&type=" + SelectProfileActivity.profileType + "&device_details="+ details +
                    "&token_id=" + SelectProfileActivity.tokenId + "&mobile=" + OTPActivity.mobileNum ;


            Log.e("param", param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(url,param);
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
                    String  status = jsonObject.getString("status");
                    dialog.dismiss();
                    if (status.equals("success")) {//Running Fine
                        JSONObject dataObj = jsonObject.getJSONObject("data");
                        String message = dataObj.getString("message");
                        Toast.makeText(IRCTCStaffDetailActivity.this, message, Toast.LENGTH_SHORT).show();

                        if (isIntro){
                            startActivity(new Intent(IRCTCStaffDetailActivity.this, DashboardActivity.class));
                            finishAffinity();

                        }else{
                            startActivity(new Intent(IRCTCStaffDetailActivity.this, IntroActivity.class));
                            finishAffinity();

                        }

                    } else if (status.equals("failure")) {
                        Toast.makeText(IRCTCStaffDetailActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(IRCTCStaffDetailActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}