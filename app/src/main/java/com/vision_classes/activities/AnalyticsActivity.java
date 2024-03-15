package com.vision_classes.activities;

import static android.content.ContentValues.TAG;


import static com.vision_classes.activities.DashboardActivity.sid;
import static com.vision_classes.activities.DashboardActivity.uid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.vision_classes.R;
import com.vision_classes.helperClasses.JSONParser;
import com.vision_classes.model.analytics.ItemStrongWeakAdapter;
import com.vision_classes.model.analytics.ItemStrongWeakOnEdge;
import com.orbitalsonic.waterwave.WaterWaveView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsActivity extends AppCompatActivity {
    TextView accuracy_attempted, tv_completion, tv_curiosity,
            curiosity_desc, tv_concentration, curiosity_title,
            concentration_desc, concentration_title, concen_desc2, curiosity_desc2,
            strong_chap_desc, onedge_chap_desc, weak_chap_desc,
            tv_accuracy, tv_status, user_name ,notStartedYetText;

    ImageView downloadAnalyticsBtn , concentrationRedArrowImg,concentrationGreenArrowImg;
    RecyclerView strong_chap_recycler, onedge_chap_recycler, weak_chap_recycler,group_pos_recycler ,accuracy_recycler, status_recycler;
    CardView strongCardView, onEdgeCardView, weakCardView, group_pos_cardView,downloadCard;
    LinearLayout accuracyLayout, statusLayout;
    LinearLayout item_red_layout, item_red_layout1;
    WaterWaveView waterWaveView;

    ImageView backBtn;

    String studName;

    NestedScrollView scrollViewAnalytics;
    LottieAnimationView animationViewAnalytics;

    ArrayList<ItemStrongWeakOnEdge> strongArrayList = new ArrayList<>();
    ArrayList<ItemStrongWeakOnEdge> onEdgeArrayList = new ArrayList<>();
    ArrayList<ItemStrongWeakOnEdge> weakArrayList = new ArrayList<>();
    ArrayList<ItemStrongWeakOnEdge> groupPosArrayList = new ArrayList<>();
    ArrayList<ItemStrongWeakOnEdge> accuracyArrayList = new ArrayList<>();
    ArrayList<ItemStrongWeakOnEdge> statusArrayList = new ArrayList<>();

    ItemStrongWeakAdapter itemStrongWeakAdapter;
    PieChart pieChart;

    CardView heath_card;
    private String courseId = "";
    private String courseName = "";
    String getAnalytics = "";
    String statusCertificate="";

    private static final int REQUEST_PERMISSION = 1;
    String urlCertificateDownload="";
    private static String PDF_FILE_NAME = "IRCTC_i_Prepare.pdf";
    private static  String PDF_URL ="";

    private boolean isPermissionRequested = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        getAnalytics = getApplicationContext().getString(R.string.apiURL)+"courseAnalyticsApiNew";
        courseId = getIntent().getStringExtra("id");
        Log.e("CourseID in Analytics ",courseId+"");
        pieChart = findViewById(R.id.pieChart_view);
        accuracy_attempted = findViewById(R.id.accuracy_attempted);
        heath_card = findViewById(R.id.heath_card);
        tv_completion = findViewById(R.id.tv_completion);
        tv_curiosity = findViewById(R.id.tv_curiosity);
        curiosity_desc = findViewById(R.id.curiosity_desc);
        tv_concentration = findViewById(R.id.concentration);
        concentrationRedArrowImg=findViewById(R.id.concentrationRedArrowImg);
        concentrationGreenArrowImg=findViewById(R.id.concentrationGreenArrowImg);
        curiosity_title = findViewById(R.id.curiosity_title);
        concentration_desc = findViewById(R.id.concentration_desc);
        concentration_title = findViewById(R.id.concentration_title);
        concen_desc2 = findViewById(R.id.concen_desc2);
        curiosity_desc2 = findViewById(R.id.curiosity_desc2);
        strong_chap_recycler = findViewById(R.id.strong_chap_recycler);
        onedge_chap_recycler = findViewById(R.id.onedge_chap_recycler);
        weak_chap_recycler = findViewById(R.id.weak_chap_recycler);
        strong_chap_desc = findViewById(R.id.strong_chap_desc);
        onedge_chap_desc = findViewById(R.id.onedge_chap_desc);
        weak_chap_desc = findViewById(R.id.weak_chap_desc);
        strongCardView = findViewById(R.id.strong_cardView);
        onEdgeCardView = findViewById(R.id.onEdge_cardView);
        weakCardView = findViewById(R.id.weak_cardView);
        group_pos_recycler = findViewById(R.id.group_pos_recycler);
        group_pos_cardView = findViewById(R.id.group_pos_cardview);
        accuracy_recycler = findViewById(R.id.accuracy_recycler);
        status_recycler = findViewById(R.id.status_recycler);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_status = findViewById(R.id.tv_status);
        accuracyLayout = findViewById(R.id.accuracyLayout);
        statusLayout = findViewById(R.id.statusLayout);
        item_red_layout = findViewById(R.id.item_red_layout);
        item_red_layout1 = findViewById(R.id.item_red_layout1);
        waterWaveView = findViewById(R.id.waterWave);
        user_name = findViewById(R.id.user_name);
        backBtn=findViewById(R.id.backBtn);
        animationViewAnalytics = findViewById(R.id.animation_viewAnalytics);
        notStartedYetText=findViewById(R.id.notStartedYetText);
        scrollViewAnalytics=findViewById(R.id.scrollViewAnalytics);
        downloadAnalyticsBtn=findViewById(R.id.downloadAnalyticsImgBtn);
        downloadCard=findViewById(R.id.downloadCard3);


        accuracy_recycler.setVisibility(View.GONE);
        status_recycler.setVisibility(View.GONE);

        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        studName = studDetails.getString("profileName", "NO_NAME");
        user_name.setText(studName);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//               // startDownload();
//            } else {
//                // Request the permission if not granted
//                if (!isPermissionRequested) {
//                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
//                    isPermissionRequested = true;
//                }
//            }
//        }


        downloadCard.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (statusCertificate.equals("active")){
                    new downloadCertificate().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AnalyticsActivity.this);
                    // ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = AnalyticsActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.popup_certificate_status, null);
                    dialogBuilder.setView(dialogView);

                    final AlertDialog alertDialog = dialogBuilder.create();
//                TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(true);
                }
            }
        });
        tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accuracyLayout.setVisibility(View.GONE);
                statusLayout.setVisibility(View.VISIBLE);
                item_red_layout.setVisibility(View.GONE);
                item_red_layout1.setVisibility(View.VISIBLE);
                accuracy_recycler.setVisibility(View.GONE);
                status_recycler.setVisibility(View.VISIBLE);
                tv_status.setTextColor(Color.parseColor("#ffffff"));
                tv_accuracy.setTextColor(Color.parseColor("#000000"));
            }
        });

        tv_accuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accuracyLayout.setVisibility(View.VISIBLE);
                statusLayout.setVisibility(View.GONE);
                item_red_layout.setVisibility(View.VISIBLE);
                item_red_layout1.setVisibility(View.GONE);
                accuracy_recycler.setVisibility(View.VISIBLE);
                status_recycler.setVisibility(View.GONE);
                tv_accuracy.setTextColor(Color.parseColor("#ffffff"));
                tv_status.setTextColor(Color.parseColor("#000000"));
            }
        });

        new GetAnalyticsData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    private void showPieChart(String weak, String onedge, String strong) {

        if (weak.equals("0") && onedge.equals("0") && strong.equals("0")) {
            heath_card.setVisibility(View.GONE);
        } else {
            heath_card.setVisibility(View.VISIBLE);
            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            String label = "type";

            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setEntryLabelTextSize(10);
            pieChart.animate();
            pieChart.setCenterText("");
            pieChart.setCenterTextSize(24);
            //initializing data
            Map<String, Integer> typeAmountMap = new HashMap<>();
            if ((int)Double.parseDouble(weak) >0){
                typeAmountMap.put("Weak%",(int)Double.parseDouble(weak));
            }
            if ((int)Double.parseDouble(strong)>0){
                typeAmountMap.put("Strong%",(int)Double.parseDouble(strong));
            }
            if ((int)Double.parseDouble(onedge) >0){
                typeAmountMap.put("On The Edge%",(int)Double.parseDouble(onedge) );
            }
//            typeAmountMap.put("Strong%",(int)Double.parseDouble(strong));   // Integer.valueOf(strong)
//            typeAmountMap.put("Weak%",(int)Double.parseDouble(weak) );     // Integer.valueOf(weak)
//            typeAmountMap.put("On The Edge%",(int)Double.parseDouble(onedge) );  //Integer.valueOf(onedge)

            //initializing colors for the entries
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#e73561"));
            colors.add(Color.parseColor("#CBEBFC"));
            colors.add(Color.parseColor("#00ffcd"));


            //input data and fit data into pie chart entry
            for (String type : typeAmountMap.keySet()) {
                pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
            }

            //collecting the entries with label name
            PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
            //setting text size of the value
            pieDataSet.setValueTextSize(12f);
            //providing color list for coloring different entries
            pieDataSet.setColors(colors);
            //grouping the data set from entry to chart
            PieData pieData = new PieData(pieDataSet);
            //showing the value of the entries, default true if not set
            pieData.setDrawValues(true);
            pieDataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
            pieData.setValueTextColor(Color.BLACK);
            pieChart.getDescription().setEnabled(false);

            pieChart.setData(pieData);
            pieChart.invalidate();
        }


    }

    class GetAnalyticsData extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(AnalyticsActivity.this);

            String param = "uid=" + uid + "&sid=" + sid + "&course_id=" +courseId;

            Log.e("AnalyticsParam", param);
            Log.e("AnalyticsUrl", getAnalytics);

            JSONObject jsonObject = jsonParser.makeHttpRequest(getAnalytics, "POST", param);
            if (jsonObject != null) {
                return jsonObject.toString();
            } else {

            }
            return "";
        }

        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
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
                    final JSONObject dataObj = jsonObject.getJSONObject("data");
                    switch (status) {
                        case "success":

                            courseName = dataObj.getString("course_name");
                            String course_status = dataObj.getString("course_status");
                            Log.e("course_status", course_status);
                            Log.e(TAG, courseName);

                            if (course_status.equals("Not started yet")){
                                notStartedYetText.setVisibility(View.VISIBLE);
                                animationViewAnalytics.setVisibility(View.VISIBLE);
                                scrollViewAnalytics.setVisibility(View.GONE);
                                downloadAnalyticsBtn.setVisibility(View.GONE);
                            }

                            // accuracy and copl
                            JSONObject accuracyAttempted = dataObj.getJSONObject("course_accuracy_on_attempted");
                            JSONObject completionObject = dataObj.getJSONObject("course_percentage_play");
                            accuracy_attempted.setText(accuracyAttempted.getString("percentage") + "%");
                            tv_completion.setText(completionObject.getString("percentage") + "%");

                            //certificate Download
//                            JSONObject certificateDownload = dataObj.getJSONObject("certificate");
//                             statusCertificate =certificateDownload.getString("status");

//                            if (statusCertificate.equals("active")){
//                                Log.e(TAG, "ONCREATE: In Active button change state" );
//                                downloadCard.setCardBackgroundColor(Color.parseColor("#01CF76"));
//
//                            }else{
//
//                            }





                            // Health-------------------
                            JSONObject healthObject = dataObj.getJSONObject("health");
                            String title = healthObject.getString("title");
                            JSONObject chapterList = healthObject.getJSONObject("chapter_list");
                            String weak = chapterList.getString("weak");
                            String onEdge = chapterList.getString("onedge");
                            String strong = chapterList.getString("strong");
                            showPieChart(weak, onEdge, strong);

                            //Curiosity------------------
                            JSONObject curiosityObject = dataObj.getJSONObject("curiosity");
                            tv_curiosity.setText(curiosityObject.getString("point"));
                            String curiosityTitle = curiosityObject.getString("title");
                            curiosity_title.setText(curiosityTitle);
                            String curiosityDes = curiosityObject.getString("description1");
                            String curiosityDes2 = curiosityObject.getString("description2");
                            curiosity_desc.setText(curiosityDes);
                            curiosity_desc2.setText(curiosityDes2);

                            //Concentration------------------
                            JSONObject concenObject = dataObj.getJSONObject("concentration");
                            String concentration = concenObject.getString("point");
                            String update_percent = concenObject.getString("update_percent");
                            String conceTitle = concenObject.getString("title");
                            String concdesc = concenObject.getString("description1");
                            String concdes2 = concenObject.getString("description2");
                            String type=concenObject.getString("type");
                            tv_concentration.setText(concentration+"%");
                            concentration_desc.setText(concdesc);
                            concentration_title.setText(conceTitle);
                            if (type.equals("increase")){
                                concentrationRedArrowImg.setVisibility(View.GONE);
                                concentrationGreenArrowImg.setVisibility(View.VISIBLE);

                            }else{
                                concentrationRedArrowImg.setVisibility(View.VISIBLE);
                                concentrationGreenArrowImg.setVisibility(View.GONE);
                            }
                            concen_desc2.setText(concdes2);
                            int concentrationInt = Integer.parseInt(update_percent);
                            waterWaveView.setProgress(concentrationInt);
                            waterWaveView.setHideText(true);
                            if (concentrationInt>50){
                                waterWaveView.setFrontWaveColor(Color.parseColor("#02B894"));
                                waterWaveView.setBehindWaveColor(Color.parseColor("#00ffcd"));
                            }else if(concentrationInt<50){
                                waterWaveView.setFrontWaveColor(Color.parseColor("#E41B4D"));
                                waterWaveView.setBehindWaveColor(Color.parseColor("#e73561"));
                            }else{
                                waterWaveView.setFrontWaveColor(Color.parseColor("#F3C230"));
                                waterWaveView.setBehindWaveColor(Color.parseColor("#DFA801"));
                            }

                            waterWaveView.setAnimationSpeed(100);

                            // Strong chapters
                            JSONObject strongJsonObject = dataObj.getJSONObject("strong");
                            strong_chap_desc.setText(strongJsonObject.getString("description"));
                            JSONArray strongChapters = strongJsonObject.getJSONArray("chapter_list");
                            strongArrayList.clear();
                            if (strongChapters.length() == 0) {
                                Log.e(TAG, "strong: nhi hai");
                                strongCardView.setVisibility(View.GONE);
                            } else {
                                strongCardView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < strongChapters.length(); i++) {
                                    ItemStrongWeakOnEdge itemStrongWeakOnEdge = new ItemStrongWeakOnEdge();
                                    JSONObject chapterLists = strongChapters.getJSONObject(i);
                                    itemStrongWeakOnEdge.setChapter_id(chapterLists.getString("chapter_id"));
                                    itemStrongWeakOnEdge.setChapter_name(chapterLists.getString("chapter_name"));
                                    itemStrongWeakOnEdge.setType("strong");
                                    strongArrayList.add(itemStrongWeakOnEdge);
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(AnalyticsActivity.this, LinearLayoutManager.VERTICAL, false);
                                strong_chap_recycler.setLayoutManager(layoutManager);
                                itemStrongWeakAdapter = new ItemStrongWeakAdapter(strongArrayList);
                                strong_chap_recycler.setAdapter(itemStrongWeakAdapter);

                                itemStrongWeakAdapter.notifyDataSetChanged();
                            }


                            // On Edge chapters
                            JSONObject onEdgesonObject = dataObj.getJSONObject("on_edge");
                            onedge_chap_desc.setText(onEdgesonObject.getString("description"));
                            JSONArray onEdgeChapters = onEdgesonObject.getJSONArray("chapter_list");
                            onEdgeArrayList.clear();
                            if (onEdgeChapters.length() == 0) {
                                Log.e(TAG, "onedge: nhi hai");
                                onEdgeCardView.setVisibility(View.GONE);
                            } else {
                                onEdgeCardView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < onEdgeChapters.length(); i++) {
                                    ItemStrongWeakOnEdge itemStrongWeakOnEdge = new ItemStrongWeakOnEdge();
                                    JSONObject chapterLists = onEdgeChapters.getJSONObject(i);
                                    itemStrongWeakOnEdge.setChapter_id(chapterLists.getString("chapter_id"));
                                    itemStrongWeakOnEdge.setChapter_name(chapterLists.getString("chapter_name"));
                                    itemStrongWeakOnEdge.setType("onEdge");
                                    onEdgeArrayList.add(itemStrongWeakOnEdge);
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(AnalyticsActivity.this, LinearLayoutManager.VERTICAL, false);
                                onedge_chap_recycler.setLayoutManager(layoutManager);
                                itemStrongWeakAdapter = new ItemStrongWeakAdapter(onEdgeArrayList);
                                onedge_chap_recycler.setAdapter(itemStrongWeakAdapter);

                                itemStrongWeakAdapter.notifyDataSetChanged();
                            }


                            // weak chapters
                            JSONObject weakObject = dataObj.getJSONObject("weak");
                            weak_chap_desc.setText(weakObject.getString("description"));
                            JSONArray weakChapters = weakObject.getJSONArray("chapter_list");
                            Log.e("weakchapters", String.valueOf(weakChapters));
                            weakArrayList.clear();
                            if (weakChapters.length() == 0) {
                                Log.e(TAG, "weak: nhi hai");
                                weakCardView.setVisibility(View.GONE);

                            } else {
                                weakCardView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < weakChapters.length(); i++) {
                                    ItemStrongWeakOnEdge itemStrongWeakOnEdge = new ItemStrongWeakOnEdge();
                                    JSONObject chapterLists = weakChapters.getJSONObject(i);
                                    itemStrongWeakOnEdge.setChapter_id(chapterLists.getString("chapter_id"));
                                    itemStrongWeakOnEdge.setChapter_name(chapterLists.getString("chapter_name"));
                                    itemStrongWeakOnEdge.setType(("weak"));
                                    weakArrayList.add(itemStrongWeakOnEdge);
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(AnalyticsActivity.this, LinearLayoutManager.VERTICAL, false);
                                weak_chap_recycler.setLayoutManager(layoutManager);
                                itemStrongWeakAdapter = new ItemStrongWeakAdapter(weakArrayList);
                                weak_chap_recycler.setAdapter(itemStrongWeakAdapter);

                            }

                            //Group position
                            JSONObject groupPosition = dataObj.getJSONObject("studentGroup");
                            JSONArray studentLists = groupPosition.getJSONArray("student_list");
                            groupPosArrayList.clear();
                            if (studentLists.length() == 0) {
                                Log.e(TAG, "student: nhi hai");
                                group_pos_cardView.setVisibility(View.GONE);

                            } else {
                                group_pos_cardView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < studentLists.length(); i++) {
                                    ItemStrongWeakOnEdge itemStrongWeakOnEdge = new ItemStrongWeakOnEdge();
                                    JSONObject studentListObj = studentLists.getJSONObject(i);
                                    itemStrongWeakOnEdge.setChapter_id(studentListObj.getString("student_id"));
                                    itemStrongWeakOnEdge.setChapter_name(studentListObj.getString("student_name"));
                                    itemStrongWeakOnEdge.setRanking(studentListObj.getString("rank"));
                                    itemStrongWeakOnEdge.setType("groupPosition");

                                    groupPosArrayList.add(itemStrongWeakOnEdge);
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(AnalyticsActivity.this, LinearLayoutManager.VERTICAL, false);
                                group_pos_recycler.setLayoutManager(layoutManager);
                                itemStrongWeakAdapter = new ItemStrongWeakAdapter(groupPosArrayList);
                                group_pos_recycler.setAdapter(itemStrongWeakAdapter);

                            }

                            //Accuracy and status position
                            JSONArray allChapterObj = dataObj.getJSONArray("all_chapter_list");
                            accuracyArrayList.clear();
                            if (allChapterObj.length() == 0) {
                                Log.e(TAG, "student: nhi hai");
                                accuracy_recycler.setVisibility(View.GONE);

                            } else {
                                accuracy_recycler.setVisibility(View.VISIBLE);
                                for (int i = 0; i < allChapterObj.length(); i++) {
                                    ItemStrongWeakOnEdge itemStrongWeakOnEdge = new ItemStrongWeakOnEdge();
                                    JSONObject chapterListObj = allChapterObj.getJSONObject(i);
                                    itemStrongWeakOnEdge.setChapter_id(chapterListObj.getString("chapter_id"));
                                    itemStrongWeakOnEdge.setChapter_name(chapterListObj.getString("chapter_name"));
                                    itemStrongWeakOnEdge.setProgress(Double.parseDouble(chapterListObj.getString("chapter_attempt_milestone_percentage")));
                                    itemStrongWeakOnEdge.setRank(Double.parseDouble(chapterListObj.getString("chapter_percentage_QBM_On_Attempt")));
                                    itemStrongWeakOnEdge.setType("accuracy");
                                    accuracyArrayList.add(itemStrongWeakOnEdge);
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(AnalyticsActivity.this, LinearLayoutManager.VERTICAL, false);
                                accuracy_recycler.setLayoutManager(layoutManager);
                                itemStrongWeakAdapter = new ItemStrongWeakAdapter(accuracyArrayList);
                                accuracy_recycler.setAdapter(itemStrongWeakAdapter);

                            }

                            //Status
                            JSONArray allChapterObject = dataObj.getJSONArray("all_chapter_list");
                            statusArrayList.clear();
                            if (allChapterObject.length() == 0) {
                                Log.e(TAG, "student: nhi hai");
                                status_recycler.setVisibility(View.GONE);

                            } else {
                                status_recycler.setVisibility(View.GONE);
                                for (int i = 0; i < allChapterObject.length(); i++) {
                                    ItemStrongWeakOnEdge itemStrongWeakOnEdge = new ItemStrongWeakOnEdge();
                                    JSONObject chapterListObj = allChapterObj.getJSONObject(i);
                                    itemStrongWeakOnEdge.setChapter_id(chapterListObj.getString("chapter_id"));
                                    itemStrongWeakOnEdge.setChapter_name(chapterListObj.getString("chapter_name"));
                                    itemStrongWeakOnEdge.setProgress(Double.parseDouble(chapterListObj.getString("chapter_attempt_milestone_percentage")));
                                    itemStrongWeakOnEdge.setRank(Double.parseDouble(chapterListObj.getString("chapter_percentage_exact_play")));
                                    itemStrongWeakOnEdge.setStatus(chapterListObj.getString("statusPlay"));
                                    itemStrongWeakOnEdge.setType("status");
                                    statusArrayList.add(itemStrongWeakOnEdge);
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(AnalyticsActivity.this, LinearLayoutManager.VERTICAL, false);
                                status_recycler.setLayoutManager(layoutManager);
                                itemStrongWeakAdapter = new ItemStrongWeakAdapter(statusArrayList);
                                status_recycler.setAdapter(itemStrongWeakAdapter);

                            }


                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AnalyticsActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = AnalyticsActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
                            String msgContent = dataObj.getString("message");
                            maintainanceContent.setText(Html.fromHtml(msgContent));

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
                            Toast.makeText(AnalyticsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(AnalyticsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
        }
    }


    private void downloadPdf() {
        // Check if permission to write to external storage is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            } else {
                // Request the permission if not granted
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            }
        }
    }

    private void startDownload() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(PDF_URL), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);

        if (activities.size() > 0) {
            startActivity(intent);
        } else {
            // No PDF viewer app found, open the link in a web browser
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PDF_URL));
            startActivity(webIntent);
        }

//        try {
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            // Handle the case where no PDF viewer app is installed on the device
//            Toast.makeText(this, "No PDF viewer app found", Toast.LENGTH_SHORT).show();
//        }

//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(PDF_URL));
//        request.setTitle("Downloading PDF");
//        request.setDescription("Please wait...");
//
//
//        // Set the destination folder and file name
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, courseName+sid+"CNB"+PDF_FILE_NAME);
//
//        // Get the download service and enqueue the request
//        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        long downloadId = downloadManager.enqueue(request);
//
//        // Optionally, you can listen for download completion using a BroadcastReceiver
//        // and handle any post-download actions.
//        // Here's a sample code snippet to register the receiver:
//        // IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        // registerReceiver(downloadReceiver, filter);
//
//        Toast.makeText(this, "PDF download started!..", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                  requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                  //  isPermissionRequested=true;
                  //  requestMethod();

                }
              //  Toast.makeText(this, "Permission denied , Please Allow File Storage Permission", Toast.LENGTH_SHORT).show();

            }
        }
    }

     class downloadCertificate extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser(AnalyticsActivity.this);

           // studId = DashboardActivity.sid;
            String param = "sid=" + sid + "&course_id=" + courseId;
              String  url = getApplicationContext().getString(R.string.apiURL)+"certificate-download";

            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
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
                    String url = jsonObject.getString("url");
                    PDF_URL=url;


                    switch (status) {
                        case "success":
                            if (!PDF_URL.isEmpty()){
                              // downloadPdf();
                                startDownload();
                            }
                            break;

                        case "maintenance":
//
                            break;
                        case "failure":
                            Toast.makeText(AnalyticsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(AnalyticsActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }



    @Override
    protected void onStart() {
        super.onStart();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//
//            } else {
//                // Request the permission if not granted
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
//                // Toast.makeText(this, "Allow for Certificate", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}