package com.vision_digital.profile;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.vision_digital.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.vision_digital.databinding.ActivityEditProfileBinding;
import com.vision_digital.helperClasses.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    ActivityEditProfileBinding binding;




    ProgressDialog dialog;

    File  imageFile ;

    Boolean onTextChanged= false;

    CircleImageView edit_profile_image;

    EditText edit_name, edit_username, edit_email, edit_mobile, edit_dob, edit_gender, edit_school;
    private int mYear, mMonth, mDay;


    private int sid;


    Spinner instituteSpinner;



//for getting profile data from firestore

    private String nameString = "";
    private String userNameString = "";
    private String dobString = "";
    private String mobString = "";
    private String genderString = "";
    private String schoolString = "";
    private String profileImageUrlString = "";
    private String emailString = "";
    private String forCommunity = "";
    private String studName = "";

    private String getProfileDataUrl = "";
    String updateProfileUrl = "";



    String setProfileImage="";

    Boolean onImageChanged=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);


        getProfileDataUrl = getApplicationContext().getString(R.string.apiURL) + "getProfile";
        updateProfileUrl = getApplicationContext().getString(R.string.apiURL) + "updateProfile";


        setProfileImage=getApplicationContext().getString(R.string.apiURL)+"updateImage";

        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.show();



        edit_profile_image = findViewById(R.id.edit_profile_image);

        edit_name = findViewById(R.id.edit_name);
        edit_username = findViewById(R.id.edit_username);
        edit_email = findViewById(R.id.edit_email);
        edit_mobile = findViewById(R.id.edit_mobile);
        edit_dob = findViewById(R.id.edit_dob);
        edit_dob.setFocusable(false);
        edit_school = findViewById(R.id.edit_school);
        edit_gender = findViewById(R.id.edit_gender);
        instituteSpinner = findViewById(R.id.instituteSpinner);
        edit_gender.setFocusable(false);

        edit_name.setText(getIntent().getStringExtra("name"));
        edit_username.setText(getIntent().getStringExtra("userName"));
        edit_email.setText(getIntent().getStringExtra("email"));
//        edit_mobile.setText(getIntent().getStringExtra("mobile"));
        edit_school.setText(getIntent().getStringExtra("school"));
        edit_gender.setText(getIntent().getStringExtra("gender"));
        edit_dob.setText(getIntent().getStringExtra("dob"));
        forCommunity = getIntent().getStringExtra("ForCommunity");
        // fromActivity = getIntent().getStringExtra("fromActivity");


        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("profileImage")).into(edit_profile_image);


        edit_mobile.setEnabled(false);
        SharedPreferences studDetails = getSharedPreferences("CNBMOBILE", MODE_PRIVATE);
        mobString = studDetails.getString("mobileNo", "NO_NAME");
        Log.e("mobile", mobString);
        edit_mobile.setText("+91-" + mobString);


        SharedPreferences userIsRegisteredSuccessful = getSharedPreferences("CNB", MODE_PRIVATE);
        sid = userIsRegisteredSuccessful.getInt("sid", 0);




        binding.editBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.editGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(EditProfileActivity.this, v);
                popup.setOnMenuItemClickListener(EditProfileActivity.this);
                popup.inflate(R.menu.choose_gender);
                popup.show();
            }
        });

        binding.editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDob();
                onTextChanged=true;
            }
        });


        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {

                    ImagePicker.Companion.with(EditProfileActivity.this).crop().compress(1024).maxResultSize(1080, 1080).start();
                }
            }
        });

        binding.editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {

                    ImagePicker.Companion.with(EditProfileActivity.this).crop().compress(1024).maxResultSize(1080, 1080).start();
                }
            }
        });


        binding.editSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(EditProfileActivity.this);
                dialog.setMessage("Please wait..");
//                dialog.show();


                Map<String, String> params = new HashMap<>();
                params.put("sid", String.valueOf(sid));
                //   params.put("param2", "value2");

                if (onImageChanged){
                    dialog.show();
                    MultipartAsyncTask multipartAsyncTask = new MultipartAsyncTask(setProfileImage, params, imageFile, getApplicationContext());
                    multipartAsyncTask.setProgressDialog(dialog);
                    multipartAsyncTask.execute();
                }else {

                }

                new updateProfileData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });


        binding.editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String emailString = edit_email.getText().toString().trim();
                emailValidator(emailString);
                onTextChanged=true;
            }
        });


        edit_name.setFocusable(true);
        edit_name.requestFocus();
        new getProfileData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        if (!matcher.matches()) {
           // edit_email.setError("Enter correct email!");
            edit_email.setFocusable(true);
            edit_email.requestFocus();
        }
    }


    @SuppressLint("NewApi")
    private void chooseDob() {


        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        //show dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                edit_dob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                onTextChanged=true;
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK) {
            Bundle extras = imageReturnedIntent.getExtras();
            Log.e(TAG, "onActivityResult: Extras " + extras);

            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Uri uri = imageReturnedIntent.getData();


            Log.e(TAG, "onActivityResult: Uri image" + uri);

            //   Log.e(TAG, "onActivityResult: Bitmap" + imageBitmap);

            if (uri != null) {
                Bitmap bitmap = loadBitmapFromUri(uri);
                edit_profile_image.setImageURI(uri);
                onImageChanged=true;
                Log.e(TAG, "Bitmap is: "+bitmap );
                if (bitmap != null) {
                    imageFile = null;
                    try {
                        imageFile = createImageFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    saveBitmapToFile(bitmap, imageFile);
                    Log.e(TAG, "onActivityResult: image file"+imageFile.getName());

                }
            }

        } else {
            Toast.makeText(this, "Something went wrong while Capturing Image", Toast.LENGTH_SHORT).show();
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile("JPEG_" + timeStamp + "_", ".jpg", storageDir);
    }

    private void saveBitmapToFile(Bitmap bitmap, File file) {
        Bitmap resizedBitmap = resizeBitmap(bitmap, 800, 800);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 32, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float aspectRatio = (float) width / height;

        int newWidth;
        int newHeight;

        if (aspectRatio > 1) {
            newWidth = maxWidth;
            newHeight = (int) (maxWidth / aspectRatio);
        } else {
            newWidth = (int) (maxHeight * aspectRatio);
            newHeight = maxHeight;
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }


    private Bitmap loadBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA}, 20);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else
            Toast.makeText(EditProfileActivity.this, "Permission not Granted", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.male:
                edit_gender.setText("Male");
                return true;
            case R.id.female:
                edit_gender.setText("Female");
                return true;
            case R.id.other:
                edit_gender.setText("Other");
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();


    }





    class updateProfileData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser(EditProfileActivity.this);

            String updatedName = binding.editName.getText().toString();
            String updatedEmail = binding.editEmail.getText().toString();
            String updatedMobile = binding.editMobile.getText().toString();
            String updatedUserName = binding.editUsername.getText().toString();
            String updatedDob = binding.editDob.getText().toString();
            String updatedGender = binding.editGender.getText().toString();
            // Others


            String param = "";

            param = "sid=" + sid + "&name=" + updatedName + "&username=" + updatedUserName +
                    "&email=" + updatedEmail + "&dob=" + updatedDob + "&gender=" + updatedGender +
                    "&type=" + "others" + "&mobile="
                    + mobString  + "&token_id=" + "" ;


            Log.e(TAG, "param: " + param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(updateProfileUrl, "POST", param);
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
                    Log.e("Result :", s);

                    String status = jsonObject.getString("status");

//                    dialog.dismiss();

                    switch (status) {
                        case "success":
                            //Running Fine
                            //  JSONObject dataObj = jsonObject.getJSONObject("data");
                            Log.e(TAG, "onPostExecute: ============================================================Success");
                            startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                            Toast.makeText(EditProfileActivity.this, "All Data Updated", Toast.LENGTH_SHORT).show();

                            break;
                        case "maintainance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditProfileActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = EditProfileActivity.this.getLayoutInflater();
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
                            Log.e(TAG, "onPostExecute: ============================================================Failure");
                            startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
                            Toast.makeText(EditProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Log.e(TAG, "onPostExecute: ============================================================Default ");
                            startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
                            Toast.makeText(EditProfileActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }

                } catch (Exception e) {

                }
            }
        }
    }

    class getProfileData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(EditProfileActivity.this);
          //  int versionCode = BuildConfig.VERSION_CODE;


            String param = "sid=" + sid;
            Log.e(TAG, "param: " + param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(getProfileDataUrl, "POST", param);
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

                            userNameString = dataObj.getString("username");
                            nameString = dataObj.getString("name");
                            dobString = dataObj.getString("dob");
                            mobString = dataObj.getString("mobile");
                            genderString = dataObj.getString("gender");
                            profileImageUrlString = dataObj.getString("image");
                            emailString = dataObj.getString("email");

                            binding.editName.setText(nameString);
                            binding.editUsername.setText(userNameString);
                            binding.editEmail.setText(emailString);
                            binding.editMobile.setText(mobString);
                            binding.editGender.setText(genderString);
                            binding.editDob.setText(dobString);

//                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EditProfileActivity.this);
//                            String mImageUri = preferences.getString("image", null);
//                            binding.editProfileImage.setImageURI(Uri.parse(mImageUri));

                            if (profileImageUrlString.equals("")) {
                                Glide.with(getApplicationContext()).load(R.drawable.user_profile).into(edit_profile_image);
                            } else {
                                Glide.with(getApplicationContext()).load(profileImageUrlString).into(edit_profile_image);
                            }


                            // category_layout.setVisibility(View.GONE);
                            break;
                        case "maintainance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditProfileActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = EditProfileActivity.this.getLayoutInflater();
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
//                            binding.profileName.setVisibility(View.GONE);
//                            binding.profileEmail.setVisibility(View.GONE);
//                            binding.profileUsername.setVisibility(View.GONE);
//                            binding.profileMobile.setVisibility(View.GONE);
//                            binding.profileDob.setVisibility(View.GONE);
//                            binding.profileGender.setVisibility(View.GONE);
//                            binding.profileType.setVisibility(View.GONE);
//                            binding.qualification.setVisibility(View.GONE);
//                            binding.areOfInterest.setVisibility(View.GONE);
//                            binding.jobRole.setVisibility(View.GONE);
//                            binding.operationalUnit.setVisibility(View.GONE);
//                            binding.categoryLayout.setVisibility(View.GONE);
//                            binding.licenseeCompany.setVisibility(View.GONE);
//                            binding.grade.setVisibility(View.GONE);
//                            binding.department.setVisibility(View.GONE);
//                            binding.designation.setVisibility(View.GONE);
//                            binding.zone.setVisibility(View.GONE);
//                            binding.placeOfPosting.setVisibility(View.GONE);
//                            binding.learnerCode.setVisibility(View.GONE);

//                            binding.cardDetails.setVisibility(View.GONE);
//                            binding.noDataFound.setVisibility(View.VISIBLE);
                            Toast.makeText(EditProfileActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(EditProfileActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


            } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }








}