package com.vision_classes.signUp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vision_classes.R;
import com.vision_classes.activities.DashboardActivity;
import com.vision_classes.databinding.ActivityVisionStudentBinding;
import com.vision_classes.helperClasses.JSONParser;
import com.vision_classes.oboardingScreen.IntroActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class VisionStudentActivity extends AppCompatActivity {

    ActivityVisionStudentBinding binding;

    String registerStudent = "";
    ProgressDialog dialog;

    String dob = "";

    String urlRegister = "";

    String type = "";
    String otherName = "";
    String visionName = "";

    String name = "";

    String registerNumber = "";
    private int sid;
    private boolean isIntro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vision_student);
        // setContentView(R.layout.activity_vision_student);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");

        urlRegister = getApplicationContext().getString(R.string.apiURL) + "registerStudent";

        SharedPreferences userIsRegisteredSuccessful = this.getSharedPreferences("CNB", MODE_PRIVATE);
        sid = userIsRegisteredSuccessful.getInt("sid", 0);
        isIntro = userIsRegisteredSuccessful.getBoolean("intro", false);


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.operationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle radio button selection here
                switch (checkedId) {
                    case R.id.visionStudent:
                        // Call a function for radioButton1 selection
                        type = "visionStudent";
                        binding.otherStudentLayout.setVisibility(View.GONE);
                        binding.visionStudentLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.otherStudent:
                        // Call a function for radioButton2 selection
                        type = "other";
                        binding.otherStudentLayout.setVisibility(View.VISIBLE);
                        binding.visionStudentLayout.setVisibility(View.GONE);

                        break;
                    // Add cases for other radio buttons if needed
                }
            }
        });

        binding.dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        binding.otherName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = "";
                otherName = s.toString();
                name = otherName;


            }
        });

        binding.visionStudentName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = "";
                visionName = s.toString();
                name = visionName;


            }
        });

        binding.registerNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerNumber = s.toString();
            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
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
                        binding.dob.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private boolean checkValidation() {
        if (name.isEmpty() || name.equals("")) {
            Toast.makeText(this, "Enter your name!..", Toast.LENGTH_SHORT).show();

            return false;
        }

        if ((registerNumber.isEmpty() || registerNumber.equals("0")) && type.equals("visionStudent")) {
            Toast.makeText(this, "Enter Register Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        //  binding.btnNextLicenseeStaff.setBackgroundResource(R.drawable.topper_button);

        return true;

    }


    class RegisterUserProfile extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(VisionStudentActivity.this);
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
            String param = "sid=" + sid + "&name=" + name + "&dob=" + dob + "&enrollno=" + registerNumber+
                    "&type=" + type + "&device_details=" + details
                    + "&mobile=" + OTPActivity.mobileNum;

            Log.e(TAG, "param: " + param);

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
                            Log.e("EnrollNo",binding.registerNo.getText().toString().trim());

                            String message = dataObj.getString("message");
                            SharedPreferences.Editor editor = getSharedPreferences("CNB", MODE_PRIVATE).edit();
                            editor.putBoolean("registered", true);
                            editor.putString("profileName", name);
                            editor.putString("enrollno", binding.registerNo.getText().toString().trim());
                            editor.putString("current_login_id", dataObj.getString("current_login_id"));                            editor.apply();
                            Toast.makeText(VisionStudentActivity.this, message, Toast.LENGTH_SHORT).show();

                            if (isIntro) {
                                startActivity(new Intent(VisionStudentActivity.this, DashboardActivity.class));
                                finishAffinity();

                            } else {
                                startActivity(new Intent(VisionStudentActivity.this, IntroActivity.class));
                                finishAffinity();

                            }

                            break;
                        case "maintainance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(VisionStudentActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = VisionStudentActivity.this.getLayoutInflater();
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
                            Toast.makeText(VisionStudentActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(VisionStudentActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}