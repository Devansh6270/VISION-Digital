package com.vision_digital.TestSeries;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vision_digital.R;
import com.vision_digital.TestSeries.model.result.objectiveQuestion.ItemObjectiveQuestion;
import com.vision_digital.TestSeries.model.result.objectiveQuestion.options.ItemOption;
import com.vision_digital.TestSeries.model.section.ItemSection;
import com.vision_digital.TestSeries.model.section.ItemSectionAdapter;
import com.vision_digital.helperClasses.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ShowResultActivity extends AppCompatActivity {
    private CardView closeDrawerBtn, openDrawerBtn;
    public static DrawerLayout drawer_ans;
    int sid;
    String testId = "";
    String testName = "";
    ProgressDialog dialog;
    public static ImageView ansImage;
    TextView testNameTv;

    RecyclerView sectionListView;
    ItemSectionAdapter itemSectionAdapter;
    LinearLayout goToAnalysis;

    public static RecyclerView questionsListView;
    com.vision_digital.TestSeries.model.result.objectiveQuestion.ItemObjectiveQuestionAdapter itemObjectiveQuestionAdapter;
    public static ArrayList<ItemSection> sectionthisArrayList = new ArrayList<>();

    //Question Layout

    public static TextView questionNumber;
    public static TextView questionView,note_one;
    public static RecyclerView optionsListView;
    public static TextView nextQuesBtn, prevQuesBtn;
    ImageView backBtn;


    //api
    JSONObject dataObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        questionNumber = findViewById(R.id.questionNumber);
        optionsListView = findViewById(R.id.optionsList);
        questionView = findViewById(R.id.question);
        nextQuesBtn = findViewById(R.id.nextQuesBtn);
        prevQuesBtn = findViewById(R.id.prevQuesBtn);
        note_one = findViewById(R.id.note_one);
        ansImage = findViewById(R.id.ansImage);
        goToAnalysis = findViewById(R.id.goToAnalysis);
        backBtn = findViewById(R.id.backBtn);
        testNameTv = findViewById(R.id.testNameTv);
//        testSeriesTitle = findViewById(R.id.testSeriesTitle);

        nextQuesBtn.setVisibility(View.VISIBLE);
        prevQuesBtn.setVisibility(View.VISIBLE);
        note_one.setVisibility(View.VISIBLE);

        testId = getIntent().getStringExtra("id");
        testName = getIntent().getStringExtra("testName");
        dialog = new ProgressDialog(ShowResultActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading");
        dialog.show();

        drawer_ans = findViewById(R.id.drawer_layout_result);

        testNameTv.setText(testName);

        closeDrawerBtn = findViewById(R.id.closeDrawerBtn);
        closeDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_ans.closeDrawers();
            }
        });
        openDrawerBtn = findViewById(R.id.openDrawerBtn);
        openDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_ans.openDrawer(Gravity.RIGHT);
            }
        });

        goToAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    class GetTestResultDetails extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {


            JSONParser jsonParser = new JSONParser(ShowResultActivity.this);

            SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
            sid = studDetails.getInt("sid", 0);

//            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
            String param = "test_id=" + testId + "&sid=" + sid;
            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(getApplicationContext().getString(R.string.apiURL4) + "getMyTestResultData", "POST", param);
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


                    switch (status) {
                        case "success":

                            dataObj = jsonObject.getJSONObject("data");

                            //fetching sections--------------
                            JSONArray sectionsArray = dataObj.getJSONArray("sections");
                            sectionthisArrayList.clear();
                            for (int i = 0; i < sectionsArray.length(); i++) {
                                JSONObject sectionObject = sectionsArray.getJSONObject(i);
                                ItemSection section = new ItemSection();

                                section.setId(sectionObject.getString("id"));
//                                section.setName("Section" + sectionObject.getString("id"));
                                section.setType(sectionObject.getString("type"));
                                section.setName(sectionObject.getString("name"));

                                JSONArray questionsArray = sectionObject.getJSONArray("questions");
                                ArrayList<ItemObjectiveQuestion> questionArrayList = null;
                                switch (section.getType()) {
                                    case "Objective":
                                        questionArrayList = new ArrayList<ItemObjectiveQuestion>();
                                        for (int j = 0; j < questionsArray.length(); j++) {
                                           ItemObjectiveQuestion objectiveQuestion = new ItemObjectiveQuestion();
                                            JSONObject questionObject = questionsArray.getJSONObject(j);
                                            objectiveQuestion.setId(questionObject.getString("id"));
                                            objectiveQuestion.setQuestion(questionObject.getString("question"));
                                            objectiveQuestion.setQuestionImageURL(questionObject.getString("image"));
                                            objectiveQuestion.setAnswer_status(questionObject.getString("answer_status"));
                                            objectiveQuestion.setCorrect_answer(questionObject.getString("correct_option"));
                                            objectiveQuestion.setSelected_answer(questionObject.getString("selected_answer"));
//                                            JSONArray optionsArray = questionObject.getJSONArray("options");
                                            ArrayList<ItemOption> optionsList = new ArrayList<>();
                                            JSONObject optionObject = questionObject.getJSONObject("options");
                                            int a =1;
                                            for (int k = 0; k < optionObject.length(); k++) {
                                                com.vision_digital.TestSeries.model.result.objectiveQuestion.options.ItemOption itemOption = new com.vision_digital.TestSeries.model.result.objectiveQuestion.options.ItemOption();


                                                JSONObject optionObjectvar = optionObject.getJSONObject("option"+a);
                                                Log.e("objectoption", String.valueOf(optionObject.getJSONObject("option1" )));

                                                itemOption.setOption(optionObjectvar.getString("title"));
                                                Log.e("title",(optionObjectvar.getString("title")));
                                                itemOption.setOptionImageUrl(optionObjectvar.getString("image"));
                                                itemOption.setOptionNo("option"+a);




//                                                itemOption.setOption(optionsArray.getString(k));
                                                itemOption.setCorrect_ans(questionObject.getString("correct_option"));
                                                itemOption.setAns_status(questionObject.getString("answer_status"));
                                                itemOption.setSelected_ans(questionObject.getString("selected_answer"));

                                                objectiveQuestion.setStatus(questionObject.getString("answer_status"));
//


                                                optionsList.add(itemOption);
                                                a++;
                                            }
                                            Collections.shuffle(optionsList);
                                            objectiveQuestion.setOptions(optionsList);
                                            questionArrayList.add(objectiveQuestion);
                                            Log.e("Questions Added","Done");
                                        }


                                        break;
                                    default:

                                        break;

                                }
                                // Collections.shuffle(questionArrayList);
                                Object questionsList = questionArrayList;
                                section.setQuestionsList((ArrayList<Object>) questionsList);
                                sectionthisArrayList.add(section);
                                Log.e("Test", sectionthisArrayList.toString());
                            }

                            //SectionList Work from here------------------------
                            sectionListView = findViewById(R.id.sectionsList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowResultActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            sectionListView.setLayoutManager(linearLayoutManager);
                            itemSectionAdapter = new ItemSectionAdapter(sectionthisArrayList);
                            sectionListView.setAdapter(itemSectionAdapter);

                            //Questions List----------------------------
                            questionsListView = findViewById(R.id.questionsListView);
                            LinearLayoutManager gridLayoutManager = new GridLayoutManager(ShowResultActivity.this, 5);
                            questionsListView.setLayoutManager(gridLayoutManager);
                            itemObjectiveQuestionAdapter = new
                                    com.vision_digital.TestSeries.model.result.objectiveQuestion.ItemObjectiveQuestionAdapter
                                    (sectionthisArrayList.get(0).getQuestionsList());
//        Log.e("List", sectionArrayList.get(0).getQuestionsList().toString());

                            questionsListView.setAdapter(itemObjectiveQuestionAdapter);
                            questionsListView.scrollToPosition(0);

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShowResultActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = ShowResultActivity.this.getLayoutInflater();
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
                            questionNumber.setText("No Data Available!..");
                            nextQuesBtn.setVisibility(View.GONE);
                            prevQuesBtn.setVisibility(View.GONE);
                            note_one.setVisibility(View.GONE);
//                            Toast.makeText(ShowResultActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ShowResultActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
            dialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetTestResultDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}