package com.vision_digital.profile;

import static android.content.ContentValues.TAG;
import static com.vision_digital.activities.DashboardActivity.myCoursesList;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.databinding.ActivityProfileBinding;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.myCourses.ItemMyCourseAdapter;
import com.vision_digital.signUp.LogInViaPhoneActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    ActivityProfileBinding binding;
    ItemMyCourseAdapter itemPopularCoursesAdapter;


    private int sid;
    String nameString = "";
    String userNameString = "";
    String dobString = "";
    String mobString = "";
    String genderString = "";
    String learnerCode = "";
    String profileImageUrlString = "";
    String emailString = "";
    String studName = "";

    String mImageUri = "";

    LinearLayout category_layout;

    private ArrayList<String> instituteForProfile = new ArrayList<>();
   private String url = "";

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        category_layout = findViewById(R.id.category_layout);

        url = getApplicationContext().getString(R.string.apiURL1) + "getProfile";
        dialog = new ProgressDialog(this);
       // dialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mImageUri = preferences.getString("image", null);

        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        studName = studDetails.getString("profileName", "NO_NAME");

        binding.profileName.setText(studName);

        SharedPreferences userIsRegisteredSuccessful = getSharedPreferences("CNB", MODE_PRIVATE);
        sid = userIsRegisteredSuccessful.getInt("sid", 0);


     //   instituteForProfile = getIntent().getStringArrayListExtra("instituteForProfile");
      //  Log.e("instituteProfilePro",instituteForProfile.toString());

        binding.profileBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
        binding.totalCourse.setLayoutManager(linearLayoutManager);
        itemPopularCoursesAdapter = new ItemMyCourseAdapter(myCoursesList);
        binding.totalCourse.setAdapter(itemPopularCoursesAdapter);

        if (myCoursesList.size()<10){
            binding.totalCourses.setText("0"+myCoursesList.size());
        }else{
            binding.totalCourses.setText(myCoursesList.size());
        }


        if (myCoursesList.size()<=0){
            binding.myCourseTxt.setVisibility(View.GONE);

        }else{
            binding.myCourseTxt.setVisibility(View.VISIBLE);
        }


        binding.profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ProfileActivity.this, v);
                popup.setOnMenuItemClickListener(ProfileActivity.this);
                popup.inflate(R.menu.profile_edit);
                popup.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mImageUri!=null){
            binding.profileImage.setImageURI(Uri.parse(mImageUri));
        }else{

        }
       new getProfileData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    class getProfileData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(ProfileActivity.this);



            String param = "sid=" + sid;
            Log.e(TAG, "param: " + param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
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

                            binding.cardDetails.setVisibility(View.VISIBLE);
                            binding.noDataFound.setVisibility(View.GONE);

                            userNameString = dataObj.getString("username");
                            nameString = dataObj.getString("name");
                            dobString = dataObj.getString("dob");
                            mobString = dataObj.getString("mobile");
                            genderString = dataObj.getString("gender");
                            profileImageUrlString = dataObj.getString("image");
                            emailString = dataObj.getString("email");

                            String cleanImageUrl = cleanImageUrl(profileImageUrlString);

                            if (cleanImageUrl.equals("")) {
                                Glide.with(getApplicationContext()).load(R.drawable.user_profile).into(binding.profileImage);
                            } else {
                                Glide.with(getApplicationContext()).load(cleanImageUrl).into(binding.profileImage);
                            }

                            if (!emailString.equals("")){
                                binding.profileEmail.setText(emailString);
                                binding.emailTitle.setVisibility(View.VISIBLE);
                                binding.profileEmail.setVisibility(View.VISIBLE);
                            }else{
                                binding.emailTitle.setVisibility(View.GONE);
                                binding.profileEmail.setVisibility(View.GONE);

                            }

                            if (!userNameString.equals("")){
                                binding.profileUsername.setText(userNameString);
                                binding.profileUsername.setVisibility(View.VISIBLE);
                            }else{
                                binding.profileUsername.setVisibility(View.GONE);
                            }


                            if (!dobString.equals("")){
                                binding.profileDob.setText(dobString);
                                binding.dobTitle.setVisibility(View.VISIBLE);
                                binding.profileDob.setVisibility(View.VISIBLE);
                            }else{

                                binding.dobTitle.setVisibility(View.GONE);
                                binding.profileDob.setVisibility(View.GONE);
                            }


                            if (!genderString.equals("")){
                                binding.profileGender.setText(genderString);
                                binding.genderTitle.setVisibility(View.VISIBLE);
                                binding.profileGender.setVisibility(View.VISIBLE);
                            }else{
                                binding.genderTitle.setVisibility(View.GONE);
                                binding.profileGender.setVisibility(View.GONE);
                            }

                            binding.profileName.setText(nameString);
                            binding.profileMobile.setText(mobString);
                            binding.profileType.setText(dataObj.getString("type"));

                            break;
                        case "maintainance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
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

                            binding.cardDetails.setVisibility(View.GONE);
                            binding.noDataFound.setVisibility(View.VISIBLE);
                            Toast.makeText(ProfileActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ProfileActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:
                Intent profileIntent = new Intent(getApplicationContext(), EditProfileActivity.class);
                profileIntent.putExtra("name", nameString);
                profileIntent.putExtra("userName", userNameString);
                profileIntent.putExtra("email", emailString);
                profileIntent.putExtra("dob", dobString);
                profileIntent.putExtra("gender", genderString);
                profileIntent.putExtra("profileImage", profileImageUrlString);
                profileIntent.putExtra("mobile", mobString);
                profileIntent.putExtra("ForCommunity","no");
              //  profileIntent.putExtra("instituteForProfile",instituteForProfile);
                profileIntent.putExtra("fromActivity","profile");
                startActivity(profileIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                return true;
            case R.id.sign_out:

                SharedPreferences.Editor editor = getSharedPreferences("CNB", MODE_PRIVATE).edit();
                editor.putString("isLogin","No");
                editor.apply();

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LogInViaPhoneActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finishAffinity();
                finish();
        }
        return false;
    }

    public static String cleanImageUrl(String imageUrl) {
        Pattern pattern = Pattern.compile("\\\\/");
        Matcher matcher = pattern.matcher(imageUrl);
        return matcher.replaceAll("/");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, DashboardActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}