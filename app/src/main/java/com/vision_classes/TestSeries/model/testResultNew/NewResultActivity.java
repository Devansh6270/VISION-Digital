package com.vision_classes.TestSeries.model.testResultNew;

import static com.vision_classes.TestSeries.AllTestPageActivity.testSeriesId;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.vision_classes.R;
import com.vision_classes.TestSeries.ShowResultActivity;
import com.vision_classes.helperClasses.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class NewResultActivity extends AppCompatActivity {
    public RecyclerView level_wise_rv, level_wise_rv1, level_wise_rv2, topic_wise_weak, topic_wise_average, topic_wise_strong;
    public View view;
    LinearLayout goToSolution, item_red_layout, item_red_layout1, item_red_layout2;
    String test_id_this = "";
    private String sid = "";
    private String test_name_string = "";
    public static String apiURL = "";
    TextView score, rank, percentile, correct, incorrect, not_attempt, correctPerEasy, mediumCorrect, toughCorrect, test_name_new, no_data_available,questionDifficultyLevel, solutionUrlPdf;
    CardView tough_card, medium_card, easy_card;
    ImageView backBtn;
    LottieAnimationView no_result_lottieAnimationView;
    TextView no_result_tv;
    LinearLayout all_data_linear_layout;

    TextView weak, average, strong;
    public ItemLevelWiseAdapter adapter;
    ArrayList<ItemLevelwiseModel> itemLevelwiseModelArrayList = new ArrayList<>();
    ArrayList<ItemLevelwiseModel> itemMedLevelwiseModelArrayList = new ArrayList<>();
    ArrayList<ItemLevelwiseModel> itemToughLevelwiseModelArrayList = new ArrayList<>();

    ItemTopicWiseModelAdapter topicWiseModelAdapterWK;
    ItemTopicWiseModelAdapter topicWiseModelAdapterAV;
    ItemTopicWiseModelAdapter topicWiseModelAdapterST;

    ArrayList<ItemTopicWiseModel> itemTopicWiseWeakModelArrayList = new ArrayList<>();
    ArrayList<ItemTopicWiseModel> itemTopicWiseAverageModelArrayList = new ArrayList<>();
    ArrayList<ItemTopicWiseModel> itemTopicWiseStrongModelArrayList = new ArrayList<>();

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_result);
        level_wise_rv = findViewById(R.id.level_wise_rv);
        level_wise_rv1 = findViewById(R.id.level_wise_rv1);
        level_wise_rv2 = findViewById(R.id.level_wise_rv2);
        item_red_layout = findViewById(R.id.item_red_layout);
        item_red_layout1 = findViewById(R.id.item_red_layout1);
        item_red_layout2 = findViewById(R.id.item_red_layout2);
        no_result_lottieAnimationView = findViewById(R.id.no_result_lottieAnimationView);
        no_result_tv = findViewById(R.id.no_result_tv);
        all_data_linear_layout = findViewById(R.id.all_data_linear_layout);

        no_result_tv.setVisibility(View.GONE);
        no_result_lottieAnimationView.setVisibility(View.GONE);
        all_data_linear_layout.setVisibility(View.GONE);


        tough_card = findViewById(R.id.tough_card);
        medium_card = findViewById(R.id.medium_card);
        easy_card = findViewById(R.id.easy_card);


        topic_wise_weak = findViewById(R.id.topic_wise_weak);
        topic_wise_average = findViewById(R.id.topic_wise_average);
        topic_wise_strong = findViewById(R.id.topic_wise_strong);
        no_data_available = findViewById(R.id.no_data_available);
        no_data_available.setVisibility(View.GONE);
        questionDifficultyLevel=findViewById(R.id.questionDifficultyLevel);
        questionDifficultyLevel.setVisibility(View.GONE);
        solutionUrlPdf = findViewById(R.id.solutionUrlPdf);


        correctPerEasy = findViewById(R.id.correctPerEasy);
        mediumCorrect = findViewById(R.id.mediumCorrect);
        toughCorrect = findViewById(R.id.toughCorrect);
        weak = findViewById(R.id.weak);
        average = findViewById(R.id.average);
        strong = findViewById(R.id.strong);
        goToSolution = findViewById(R.id.goToSolution);
        score = findViewById(R.id.score);
        rank = findViewById(R.id.rank);
        percentile = findViewById(R.id.percentileAna);
//        percentile = findViewById(R.id.percentile);
        correct = findViewById(R.id.correct);
        incorrect = findViewById(R.id.incorrect);
        not_attempt = findViewById(R.id.notattempt);
        test_name_new = findViewById(R.id.test_name_new);
        backBtn = findViewById(R.id.backBtn);



        apiURL = getApplicationContext().getString(R.string.apiURL)+ "getTestResultAdvance";

        SharedPreferences userIsRegisteredSuccessful = getSharedPreferences("CNB", MODE_PRIVATE);
        sid = String.valueOf(userIsRegisteredSuccessful.getInt("sid", 0));

        test_id_this = getIntent().getStringExtra("id");

        goToSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewResultActivity.this, ShowResultActivity.class);
                i.putExtra("id", test_id_this);
                i.putExtra("testName", test_name_string);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        weak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_red_layout.setVisibility(View.VISIBLE);
                item_red_layout1.setVisibility(View.GONE);
                item_red_layout2.setVisibility(View.GONE);
                weak.setTextColor(Color.parseColor("#ffffff"));
                average.setTextColor(Color.parseColor("#000000"));
                strong.setTextColor(Color.parseColor("#000000"));
                topic_wise_weak.setVisibility(View.VISIBLE);
                topic_wise_average.setVisibility(View.GONE);
                topic_wise_strong.setVisibility(View.GONE);
                if (!(itemTopicWiseWeakModelArrayList.size() > 0)) {
                    no_data_available.setVisibility(View.VISIBLE);
                } else {
                    no_data_available.setVisibility(View.GONE);
                }
            }
        });

        average.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_red_layout.setVisibility(View.GONE);
                item_red_layout1.setVisibility(View.VISIBLE);
                item_red_layout2.setVisibility(View.GONE);
                weak.setTextColor(Color.parseColor("#000000"));
                average.setTextColor(Color.parseColor("#ffffff"));
                strong.setTextColor(Color.parseColor("#000000"));
                topic_wise_weak.setVisibility(View.GONE);
                topic_wise_average.setVisibility(View.VISIBLE);
                topic_wise_strong.setVisibility(View.GONE);
                if (!(itemTopicWiseAverageModelArrayList.size() > 0)) {
                    no_data_available.setVisibility(View.VISIBLE);
                } else {
                    no_data_available.setVisibility(View.GONE);
                }
            }
        });

        strong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_red_layout.setVisibility(View.GONE);
                item_red_layout1.setVisibility(View.GONE);
                item_red_layout2.setVisibility(View.VISIBLE);
                weak.setTextColor(Color.parseColor("#000000"));
                average.setTextColor(Color.parseColor("#000000"));
                strong.setTextColor(Color.parseColor("#ffffff"));
                topic_wise_weak.setVisibility(View.GONE);
                topic_wise_average.setVisibility(View.GONE);
                topic_wise_strong.setVisibility(View.VISIBLE);
                if (!(itemTopicWiseStrongModelArrayList.size() > 0)) {
                    no_data_available.setVisibility(View.VISIBLE);
                } else {
                    no_data_available.setVisibility(View.GONE);
                }
            }
        });
        dialog = new ProgressDialog(NewResultActivity.this);
        dialog.setMessage("Please wait!...");
        dialog.show();

        new GetResultDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void openPdfInBrowser(String pdfUrl) {
        // Create an Intent to open the PDF in a browser
        Intent intentPdf = new Intent(Intent.ACTION_VIEW);
        intentPdf.setData(Uri.parse(pdfUrl));

        try {
            startActivity(intentPdf);
        } catch (Exception e) {
            // Handle exceptions, such as the browser not being available or the URL being invalid
            Toast.makeText(NewResultActivity.this, "Document will be uploaded soon", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    class GetResultDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(NewResultActivity.this);
            int versionCode;
            PackageInfo pInfo = null;
            try {
                pInfo = NewResultActivity.this.getPackageManager().getPackageInfo(NewResultActivity.this.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            versionCode = pInfo.versionCode;
            Log.d("versionCode", String.valueOf(versionCode));
            String param = "sid=" + sid + "&test_id=" + test_id_this +"&testseries_id="+testSeriesId;
            Log.d("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(apiURL, "POST", param);
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
                    Log.d("Data: ", s);
                    String status = jsonObject.getString("status");
                    final JSONObject dataObj = jsonObject.getJSONObject("data");

                    switch (status) {
                        case "success":

                            String queryScore = dataObj.getString("gain_marks");
                            String queryRank = dataObj.getString("rank");
                            String queryPercentile = dataObj.getString("percentile_rank");
                            String queryCorrect = dataObj.getString("no_ques_correct");
                            String queryIncorrect = dataObj.getString("no_ques_incorrect");
                            String queryNotAttempt = dataObj.getString("no_not_ques_attempt");
                            String queryTestName = dataObj.getString("test_name");
                            String queryTotalMarks = dataObj.getString("total_marks");
                            String queryTotalQues = dataObj.getString("no_ques_total");
                            String solutionUrl = dataObj.getString("solution_url");

                            score.setText(queryScore + "/" + queryTotalMarks);
                            rank.setText(queryRank);
                            percentile.setText(queryPercentile);
                            correct.setText(queryCorrect + "/" + queryTotalQues);
                            incorrect.setText(queryIncorrect + "/" + queryTotalQues);
                            not_attempt.setText(queryNotAttempt + "/" + queryTotalQues);
                            test_name_new.setText(queryTestName);
                            test_name_string = queryTestName;

                            if (solutionUrl.equals("") || solutionUrl.isEmpty() || solutionUrl.equals(null)){
                                solutionUrlPdf.setVisibility(View.GONE);
                            } else {
                                solutionUrlPdf.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        openPdfInBrowser(solutionUrl);
                                    }
                                });
                            }



                            String resultStatus = dataObj.getString("status");
                            Log.e("resultStatus", resultStatus);
                            if (resultStatus.equals("published")) {

                                no_result_tv.setVisibility(View.GONE);
                                no_result_lottieAnimationView.setVisibility(View.GONE);
                                all_data_linear_layout.setVisibility(View.VISIBLE);
                                Log.e("data", "data");

                                final JSONObject jsonObjLevel = dataObj.getJSONObject("levelwise");
                                Log.e("jsonObjLevel", String.valueOf(jsonObjLevel));

                                //-------------------------------------for easy questions------------------------------------------------

                                JSONObject easyObject = jsonObjLevel.getJSONObject("easy");

                                String correctEasy = easyObject.getString("correct");
                                Log.e("correctEasy", correctEasy);
                                correctPerEasy.setText("Correct " + correctEasy);

                                itemLevelwiseModelArrayList.clear();
                                final JSONArray jsonObjInnerData = easyObject.getJSONArray("data");
                                Log.e("jsonObjInnerData", String.valueOf(jsonObjInnerData));

                                if (jsonObjInnerData.length() <= 0) {
                                    easy_card.setVisibility(View.GONE);
                                } else {
                                    easy_card.setVisibility(View.VISIBLE);
                                    questionDifficultyLevel.setVisibility(View.VISIBLE);
                                }
                                for (int j = 0; j < jsonObjInnerData.length(); j++) {
                                    ItemLevelwiseModel itemLevelwiseModel = new ItemLevelwiseModel();
                                    JSONObject popularCoursesJSONObject = jsonObjInnerData.getJSONObject(j);
                                    itemLevelwiseModel.setLevel(popularCoursesJSONObject.getString("qlevel"));
                                    itemLevelwiseModel.setQuesId(popularCoursesJSONObject.getString("qid"));
                                    itemLevelwiseModel.setQuesStatus(popularCoursesJSONObject.getString("status"));
                                    itemLevelwiseModel.setQuesTopic(popularCoursesJSONObject.getString("topic"));
                                    itemLevelwiseModel.setSno(popularCoursesJSONObject.getString("sno"));
                                    itemLevelwiseModelArrayList.add(itemLevelwiseModel);
                                    Log.e("jsonObjInnerDataval", String.valueOf(itemLevelwiseModelArrayList));

                                }
                                LinearLayoutManager gridLayoutManager = new GridLayoutManager(NewResultActivity.this, 7);
                                level_wise_rv.setLayoutManager(gridLayoutManager);
                                adapter = new ItemLevelWiseAdapter(itemLevelwiseModelArrayList);
                                level_wise_rv.setAdapter(adapter);

                                //for easy quest end-----------------------------------------------------------------

//                               ------------------------------- medium start---------------------------

                                JSONObject mediumObject = jsonObjLevel.getJSONObject("medium");

                                String correctMedium = mediumObject.getString("correct");
                                Log.e("correctMedium", correctMedium);
                                mediumCorrect.setText("Correct " + correctMedium);

                                itemMedLevelwiseModelArrayList.clear();
                                final JSONArray jsonObjInnerDataMed = mediumObject.getJSONArray("data");
                                Log.e("jsonObjInnerDataMed", String.valueOf(jsonObjInnerDataMed));

                                if (jsonObjInnerDataMed.length() <= 0) {
                                    medium_card.setVisibility(View.GONE);
                                } else {
                                    medium_card.setVisibility(View.VISIBLE);
                                    questionDifficultyLevel.setVisibility(View.VISIBLE);
                                }
                                for (int j = 0; j < jsonObjInnerDataMed.length(); j++) {

                                    ItemLevelwiseModel itemLevelwiseModel = new ItemLevelwiseModel();
                                    JSONObject popularCoursesJSONObjectMed = jsonObjInnerDataMed.getJSONObject(j);
                                    itemLevelwiseModel.setLevel(popularCoursesJSONObjectMed.getString("qlevel"));
                                    itemLevelwiseModel.setQuesId(popularCoursesJSONObjectMed.getString("qid"));
                                    itemLevelwiseModel.setQuesStatus(popularCoursesJSONObjectMed.getString("status"));
                                    itemLevelwiseModel.setQuesTopic(popularCoursesJSONObjectMed.getString("topic"));
                                    itemLevelwiseModel.setSno(popularCoursesJSONObjectMed.getString("sno"));
                                    itemMedLevelwiseModelArrayList.add(itemLevelwiseModel);
                                    Log.e("jsonObjInnerDataval", String.valueOf(itemMedLevelwiseModelArrayList));

                                }
                                LinearLayoutManager gridLayoutManagerMed = new GridLayoutManager(NewResultActivity.this, 7);
                                level_wise_rv1.setLayoutManager(gridLayoutManagerMed);
                                adapter = new ItemLevelWiseAdapter(itemMedLevelwiseModelArrayList);
                                level_wise_rv1.setAdapter(adapter);

//                                ------------------------------ medium end-------------------------------


//                                ---------------tough start-------------------------
                                JSONObject toughObject = jsonObjLevel.getJSONObject("tough");

                                String correctTough = toughObject.getString("correct");
                                Log.e("correctTough", correctTough);
                                toughCorrect.setText("Correct " + correctTough);

                                itemToughLevelwiseModelArrayList.clear();
                                final JSONArray jsonObjInnerDataTough = toughObject.getJSONArray("data");
                                Log.e("jsonObjInnerDataTough", String.valueOf(jsonObjInnerDataTough));

                                if (jsonObjInnerDataTough.length() <= 0) {
                                    tough_card.setVisibility(View.GONE);
                                } else {
                                    tough_card.setVisibility(View.VISIBLE);
                                    questionDifficultyLevel.setVisibility(View.VISIBLE);
                                }
                                for (int j = 0; j < jsonObjInnerDataTough.length(); j++) {

                                    ItemLevelwiseModel itemLevelwiseModel = new ItemLevelwiseModel();
                                    JSONObject popularCoursesJSONObjectTough = jsonObjInnerDataTough.getJSONObject(j);
                                    itemLevelwiseModel.setLevel(popularCoursesJSONObjectTough.getString("qlevel"));
                                    itemLevelwiseModel.setQuesId(popularCoursesJSONObjectTough.getString("qid"));
                                    itemLevelwiseModel.setQuesStatus(popularCoursesJSONObjectTough.getString("status"));
                                    itemLevelwiseModel.setQuesTopic(popularCoursesJSONObjectTough.getString("topic"));
                                    itemLevelwiseModel.setSno(popularCoursesJSONObjectTough.getString("sno"));
                                    itemToughLevelwiseModelArrayList.add(itemLevelwiseModel);
                                    Log.e("jsonObjInnerDataval", String.valueOf(itemToughLevelwiseModelArrayList));

                                }
                                LinearLayoutManager gridLayoutManagerTough = new GridLayoutManager(NewResultActivity.this, 7);
                                level_wise_rv2.setLayoutManager(gridLayoutManagerTough);
                                adapter = new ItemLevelWiseAdapter(itemToughLevelwiseModelArrayList);
                                level_wise_rv2.setAdapter(adapter);


                                //Topicwise wala kaam yha se------------------------------------------------------------------------

                                final JSONObject jsonObjTopic = dataObj.getJSONObject("topicwise");
                                Log.e("jsonObjTopic", String.valueOf(jsonObjTopic));

                                itemTopicWiseWeakModelArrayList.clear();
                                itemTopicWiseAverageModelArrayList.clear();
                                itemTopicWiseStrongModelArrayList.clear();
                                ArrayList<String> dataKeysObjTopic = new ArrayList<>();
                                Iterator<String> iterObjTopic = jsonObjTopic.keys();
                                Log.e("iterObjTopic", String.valueOf(iterObjTopic)); //This should be the iterator you want.
                                while (iterObjTopic.hasNext()) {
                                    String key = String.valueOf(iterObjTopic.next());
                                    dataKeysObjTopic.add(key);
                                    Log.e("dataKeysObjTopic", key);//This should be the iterator you want.
                                }

                                for (int j = 0; j < jsonObjTopic.length(); j++) {
                                    JSONObject questionNoObjectWSA = jsonObjTopic.getJSONObject(String.valueOf(dataKeysObjTopic.get(j)));
                                    Log.e("questionNoObjectWSA", String.valueOf(dataKeysObjTopic.get(j)));

                                    if (questionNoObjectWSA.length() > 0) {
                                        ArrayList<String> dataKeysWeak = new ArrayList<>();
                                        Iterator<String> iterWeak = questionNoObjectWSA.keys();
                                        Log.e("questionNoObjectWSA", String.valueOf(iterWeak)); //This should be the iterator you want.
                                        while (iterWeak.hasNext()) {
                                            String key = String.valueOf(iterWeak.next());
                                            dataKeysWeak.add(key);
                                            Log.e("keyWSA", key);//This should be the iterator you want.
                                        }


                                        for (int k = 0; k < questionNoObjectWSA.length(); k++) {

                                            JSONObject questionNoObjectWeak = questionNoObjectWSA.getJSONObject(String.valueOf(dataKeysWeak.get(k)));


                                            Log.e("questionNoObjectMed", String.valueOf(questionNoObjectWeak));
                                            ItemTopicWiseModel itemTopicWiseModel = new ItemTopicWiseModel();
                                            itemTopicWiseModel.setTopicName(String.valueOf(dataKeysWeak.get(k)));
                                            itemTopicWiseModel.setTopicLevel(dataKeysObjTopic.get(j));
                                            itemTopicWiseModel.setCorrectTopic(questionNoObjectWeak.getString("correct"));
//                                        itemTopicWiseWeakModelArrayList.add(itemTopicWiseModel);
                                            Log.e("topic", String.valueOf(dataKeysObjTopic.get(j)));

                                            if (dataKeysObjTopic.get(j).equals("Weak")) {
                                                itemTopicWiseWeakModelArrayList.add(itemTopicWiseModel);
                                            } else if (dataKeysObjTopic.get(j).equals("Average")) {
                                                itemTopicWiseAverageModelArrayList.add(itemTopicWiseModel);
                                            } else if (dataKeysObjTopic.get(j).equals("Strong")) {
                                                itemTopicWiseStrongModelArrayList.add(itemTopicWiseModel);
                                            } else {

                                            }

                                            Log.e("itemTopicWiseWeakList", String.valueOf(itemTopicWiseWeakModelArrayList));
                                            Log.e("itemToWiseAverageList", String.valueOf(itemTopicWiseAverageModelArrayList));
                                            Log.e("itemTopicWiseStrongList", String.valueOf(itemTopicWiseStrongModelArrayList));

                                        }


//                                    weak
                                        LinearLayoutManager LayoutManagerTopicWK
                                                = new LinearLayoutManager(NewResultActivity.this, LinearLayoutManager.VERTICAL, false);
                                        topic_wise_weak.setLayoutManager(LayoutManagerTopicWK);
                                        topicWiseModelAdapterWK = new ItemTopicWiseModelAdapter(itemTopicWiseWeakModelArrayList);
                                        topic_wise_weak.setAdapter(topicWiseModelAdapterWK);

//                                    Average
                                        LinearLayoutManager LayoutManagerTopicAV
                                                = new LinearLayoutManager(NewResultActivity.this, LinearLayoutManager.VERTICAL, false);

                                        topic_wise_average.setLayoutManager(LayoutManagerTopicAV);
                                        topicWiseModelAdapterAV = new ItemTopicWiseModelAdapter(itemTopicWiseAverageModelArrayList);
                                        topic_wise_average.setAdapter(topicWiseModelAdapterAV);

//Strong
                                        LinearLayoutManager LayoutManagerTopicST
                                                = new LinearLayoutManager(NewResultActivity.this, LinearLayoutManager.VERTICAL, false);

                                        topic_wise_strong.setLayoutManager(LayoutManagerTopicST);
                                        topicWiseModelAdapterST = new ItemTopicWiseModelAdapter(itemTopicWiseStrongModelArrayList);
                                        topic_wise_strong.setAdapter(topicWiseModelAdapterST);

                                    } else {


                                    }
                                }



                            } else {
                                no_result_tv.setVisibility(View.VISIBLE);
                                no_result_lottieAnimationView.setVisibility(View.VISIBLE);
                                all_data_linear_layout.setVisibility(View.GONE);
                                Log.e("do data","nodtaa");
                            }
                            break;
                        case "failure":
                            Toast.makeText(NewResultActivity.this, "Data Failure...", Toast.LENGTH_SHORT).show();
                            no_result_tv.setVisibility(View.VISIBLE);
                            no_result_lottieAnimationView.setVisibility(View.VISIBLE);
                            all_data_linear_layout.setVisibility(View.GONE);
                            Log.e("do data","nodtaa");
                            break;
                        default:
                            Toast.makeText(NewResultActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                            no_result_tv.setVisibility(View.VISIBLE);
                            no_result_lottieAnimationView.setVisibility(View.VISIBLE);
                            all_data_linear_layout.setVisibility(View.GONE);
                            Log.e("do data","nodtaa");
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            dialog.dismiss();
            item_red_layout.setVisibility(View.VISIBLE);
            item_red_layout1.setVisibility(View.GONE);
            item_red_layout2.setVisibility(View.GONE);
            weak.setTextColor(Color.parseColor("#ffffff"));
            average.setTextColor(Color.parseColor("#000000"));
            strong.setTextColor(Color.parseColor("#000000"));
            topic_wise_weak.setVisibility(View.VISIBLE);
            topic_wise_average.setVisibility(View.GONE);
            topic_wise_strong.setVisibility(View.GONE);
            if (!(itemTopicWiseWeakModelArrayList.size() > 0)) {
                no_data_available.setVisibility(View.VISIBLE);
            } else {
                no_data_available.setVisibility(View.GONE);
            }
        }

    }

}