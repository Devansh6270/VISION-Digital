package com.vision_digital.TestSeries;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.vision_digital.BuildConfig;
import com.vision_digital.R;
import com.vision_digital.activities.PdfRenderActivity;
import com.vision_digital.helperClasses.JSONParser;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ResultActivity extends AppCompatActivity {

    PieChart pieChart;
    ImageView backBtn;
    int sid,pRank;
    String testId = "";
    Button showAnswerBtn;
    TextView testName, testDate, testCorrect, testIncorrect,percentile, testTotal,dateHead,noDataTex;
    LottieAnimationView lottieAnimationView;
    LinearLayout resultData;
    ProgressDialog progressDialog;
    float normal;
    float below_avg;
    float average;
    float toppers;
    float superToppers;
    Button download_ans;


    //Layout---------------

    ArrayList<Integer> colors = new ArrayList<>();

    private String filepath = "";
    private URL url = null;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressDialog = new ProgressDialog(ResultActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new GetTestResult().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        pieChart = findViewById(R.id.pieChartResult);
        dateHead= findViewById(R.id.dateHead);
        resultData= findViewById(R.id.resultData);

        noDataTex= findViewById(R.id.noDataTex);
        testName = findViewById(R.id.testName);
        testDate = findViewById(R.id.testDate);
        testCorrect = findViewById(R.id.testCorrect);
        testIncorrect = findViewById(R.id.testIncorrect);
        percentile = findViewById(R.id.percentile);
        testTotal = findViewById(R.id.testTotal);
        backBtn=findViewById(R.id.backBtn);
        showAnswerBtn = findViewById(R.id.show_ans);
        lottieAnimationView = findViewById(R.id.animationView);
        download_ans = findViewById(R.id.download_ans);







        download_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                downloadSolution();

                openfile();


            }
        });



        testId = getIntent().getStringExtra("id");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        showAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this,ShowResultActivity.class);
                intent.putExtra("id",testId);
                startActivity(intent);
            }
        });


    }

    private void openfile() {

        Intent intent = new Intent(ResultActivity.this, PdfRenderActivity.class);
        intent.putExtra("pdfLink",filepath);
        startActivity(intent);
    }

    private void downloadSolution(){
        ProgressDialog progressDialog = new ProgressDialog(ResultActivity.this);
        progressDialog.setMessage("downloading file..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgress(0);
        progressDialog.show();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url + ""));
        request.setTitle(fileName);
        request.setMimeType("applcation/pdf");
        request.allowScanningByMediaScanner();
        request.setAllowedOverMetered(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);
        progressDialog.dismiss();
        Toast.makeText(ResultActivity.this, "Solution downloaded..", Toast.LENGTH_SHORT).show();


    }

    void setUpPieChart() {

        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }


        pieChart.setUsePercentValues(false);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setCenterText("");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        LegendEntry[] legendEntries = new LegendEntry[]{
                new LegendEntry("0-30 Rank", Legend.LegendForm.DEFAULT, 10f, 2f, null, colors.get(0)),
                new LegendEntry("30-60 Rank", Legend.LegendForm.DEFAULT, 10f, 2f, null, colors.get(1)),
                new LegendEntry("60-70 Rank", Legend.LegendForm.DEFAULT, 10f, 2f, null, colors.get(2)),
                new LegendEntry("70-90 Rank", Legend.LegendForm.DEFAULT, 10f, 2f, null, colors.get(3)),
                new LegendEntry("90-100 Rank", Legend.LegendForm.DEFAULT, 10f, 2f, null, colors.get(4))

        };

        Legend legends = pieChart.getLegend();
        legends.setCustom(legendEntries);
        legends.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legends.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legends.setOrientation(Legend.LegendOrientation.VERTICAL);
        legends.setDrawInside(false);
        legends.setEnabled(false);
    }

    void loadPieChartData(int pRank) {
        //total students
        // super toppers
        // toppers
        //average
        //below avg.
        //normal
        // find percentage of each
        // value set -- graph value

//        String a= String.format("%.0f%%",superToppers);
        float sp = superToppers/100;
        float t = toppers/100;
        float ag = average/100;
        float ba = below_avg/100;
        float nl = normal/100;

        Log.e("normal", String.valueOf(nl));



        ArrayList<PieEntry> pieEntryArrayList = new ArrayList<>();
        if (0 <= pRank && pRank <= 30) {
            pieEntryArrayList.add(new PieEntry(nl, "You are here (0-30 Percentile)"));
        } else {
            if (nl==0.0){
                pieEntryArrayList.add(new PieEntry(0.02f,"0-30 Percentile"));

            }else{
                pieEntryArrayList.add(new PieEntry(nl, "0-30 Percentile"));
            }
        }
        if (30 < pRank && pRank <= 60) {
            pieEntryArrayList.add(new PieEntry(ba, "You are here (30-60 Percentile)"));
        } else {
            if (ba==0.0){
                pieEntryArrayList.add(new PieEntry(0.02f,"30-60 Percentile"));

            }else{
                pieEntryArrayList.add(new PieEntry(ba, "30-60 Percentile"));
            }
        }
        if (60 < pRank && pRank <= 70) {
            pieEntryArrayList.add(new PieEntry(ag, "You are here (60-70 Percentile)"));
        } else {
            if (ag==0.0){
                pieEntryArrayList.add(new PieEntry(0.02f,"60-70 Percentile"));

            }else{
                pieEntryArrayList.add(new PieEntry(ag, "60-70 Percentile"));
            }
        }
        if (70 < pRank && pRank <= 90) {
            pieEntryArrayList.add(new PieEntry(t, "You are here (70-90 Percentile)"));
        } else {
            if (t==0.0){
                pieEntryArrayList.add(new PieEntry(0.02f,"70-90 Percentile"));

            }else{
                pieEntryArrayList.add(new PieEntry(t,"70-90 Percentile"));
            }
        }
        if (90 < pRank && pRank <= 100) {
            pieEntryArrayList.add(new PieEntry(sp, "You are here (90-100 Percentile)"));
        } else {
            if (sp==0.0){
                pieEntryArrayList.add(new PieEntry(0.02f,"90-100 Percentile"));

            }else{
                pieEntryArrayList.add(new PieEntry(sp,"90-100 Percentile"));
            }
        }


        PieDataSet pieDataSet = new PieDataSet(pieEntryArrayList, "Ranks");
        pieDataSet.setColors(colors);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    class GetTestResult extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
            //            this.dialog.setMessage("Please wait");
            //            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(ResultActivity.this);
            int versionCode = BuildConfig.VERSION_CODE;

            SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
            sid = studDetails.getInt("sid", 0);

//            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
            String param = "test_id=" + testId + "&sid=" + sid;
            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(getApplicationContext().getString(R.string.apiURL) + "getTestResult", "POST", param);
            if (jsonObject != null) {
                return jsonObject.toString();
            } else {

            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

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
                            String result_status = dataObj.getString("result_status");

                            if (result_status.equals("published")){
                                pRank = dataObj.getInt("p_rank");
                                int correct = dataObj.getInt("correct");
                                int incorrect = dataObj.getInt("incorrect");
                                int total = dataObj.getInt("total");
                                String test_name = dataObj.getString("name");
                                String date = dataObj.getString("date");

//                                filepath = dataObj.getString("solutionUrl");

                                filepath = "https://firebasestorage.googleapis.com/v0/b/chalksnboard-8f7c3.appspot.com/o/ChatImage%2Fdocument%3A1000001265?alt=media&token=48ce3d32-61bf-4598-9a12-7fbba4e2880a";

                                JSONObject std_per_obj = dataObj.getJSONObject("percent_student");
                                normal = std_per_obj.getInt("0-30");
                                below_avg = std_per_obj.getInt("30-60");
                                average = std_per_obj.getInt("60-70");
                                toppers = std_per_obj.getInt("70-90");
                                superToppers = std_per_obj.getInt("90-100");
                                Log.e("super", String.valueOf(superToppers));


                                String testcorrectString  = String.valueOf(correct);
                                String testincorrectString  = String.valueOf(incorrect);
                                String totalString  = String.valueOf(total);

                                if (testcorrectString.equals("1")){
                                    testCorrect.setText("Correct: "+testcorrectString+" Question");

                                }else if (testcorrectString.equals("")){
                                    testCorrect.setText("Correct: "+testcorrectString);

                                }else{
                                    testCorrect.setText("Correct: "+testcorrectString+" Questions");

                                }

                                if (testincorrectString.equals("1")){
                                    testIncorrect.setText("Incorrect: "+testincorrectString+" Question");

                                }else if (testincorrectString.equals("")){
                                    testIncorrect.setText("Incorrect: "+testincorrectString);

                                }else{
                                    testIncorrect.setText("Incorrect: "+testincorrectString+" Questions");

                                }

                                String prankString = String.valueOf(pRank);
                                if (prankString.equals("")){
                                    percentile.setText("Percentile: " +pRank);

                                }else{
                                    percentile.setText("Percentile: " +pRank+"%");

                                }


                                if (totalString.equals("1")){
                                    testTotal.setText("Total: "+totalString+" Question");

                                }else if (totalString.equals("")){
                                    testTotal.setText("Total: "+totalString);

                                }else{
                                    testTotal.setText("Total: "+totalString+" Questions");

                                }


                                testName.setText(test_name);
                                testDate.setText(date);
                                setUpPieChart();
                                loadPieChartData(pRank);
                                resultData.setVisibility(View.VISIBLE);
                                lottieAnimationView.setVisibility(View.GONE);
                                noDataTex.setVisibility(View.GONE);


                                if (!filepath.equals("")){
                                    download_ans.setVisibility(View.VISIBLE);
                                    try {
                                        url = new URL(filepath);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    fileName = url.getPath();
                                    fileName = fileName.substring(fileName.lastIndexOf('/') + 1);


                                }else{
                                    download_ans.setVisibility(View.GONE);
                                }
                                // solution download filepath cde

                                if (filepath.equals("")){
                                    download_ans.setEnabled(false);
                                }else{
                                    download_ans.setEnabled(true);
                                }


                            }else{
                                resultData.setVisibility(View.GONE);
                                lottieAnimationView.setVisibility(View.VISIBLE);
                                noDataTex.setVisibility(View.VISIBLE);

                            }

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ResultActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = ResultActivity.this.getLayoutInflater();
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
                            resultData.setVisibility(View.GONE);
                            lottieAnimationView.setVisibility(View.VISIBLE);
                            noDataTex.setVisibility(View.VISIBLE);
                            Toast.makeText(ResultActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ResultActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
//            dialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    public void onBackPressed() {
//        Intent i= new Intent(ResultActivity.this,TestSeriesDashboardActivity.class);
//        i.putExtra("id",testId);
//        startActivity(i);
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//    }
}
