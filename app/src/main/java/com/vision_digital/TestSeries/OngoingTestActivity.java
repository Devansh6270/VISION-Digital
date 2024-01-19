package com.vision_digital.TestSeries;

import static com.vision_digital.TestSeries.AllTestPageActivity.testSeriesId;
import static com.vision_digital.activities.DashboardActivity.sid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;
import com.vision_digital.TestSeries.model.objectiveQuestion.ItemObjectiveQuestion;
import com.vision_digital.TestSeries.model.objectiveQuestion.ItemObjectiveQuestionAdapter;
import com.vision_digital.TestSeries.model.objectiveQuestion.options.ItemOption;
import com.vision_digital.TestSeries.model.section.ItemSection;
import com.vision_digital.TestSeries.model.section.ItemSectionAdapter;
import com.vision_digital.helperClasses.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OngoingTestActivity extends AppCompatActivity {

    public static CardView closeDrawerBtn, openDrawerBtn;

    TextView testSeriesTitle;
    TextView navigationTimer;
    TextView submitTestBtn;
    ProgressDialog dialog;
    String resultData;
    public static String test_id;

    public static ArrayList<ItemSection> sectionArrayList = new ArrayList<>();

    long timeLimit = 60 * 30;
    String timeDuration;
    String testId = "";

    String titleTestSeries;

    ArrayList<ItemObjectiveQuestion> questionArrayList = null;

    //Shared preference option
    String answer;
    RecyclerView sectionListView;
    ItemSectionAdapter itemSectionAdapter;

    public static RecyclerView questionsListView;
    ItemObjectiveQuestionAdapter itemObjectiveQuestionAdapter;

    //Question Layout

    public static TextView questionNumber, questionPageTimer;


    public static TextView questionView, tvPositiveMarks, tvNegativeMarks;
    public static ImageView questionImage;
    public static RecyclerView optionsListView;
    public static TextView nextQuesBtn, prevQuesBtn, proceedToSubBtn, hint_txt;

    public static DrawerLayout drawer;
    final Handler timer = new Handler(Looper.getMainLooper());


    //Timmer------------------------
    long min, sec, hours;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_test);
        testId = getIntent().getStringExtra("id");

        dialog = new ProgressDialog(OngoingTestActivity.this);
        dialog.setMessage("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        new GetTestDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //Setting Question Page layout:
        questionNumber = findViewById(R.id.questionNumber);
        optionsListView = findViewById(R.id.optionsList);
        questionView = findViewById(R.id.question);
        questionPageTimer = findViewById(R.id.questionPageTimmer);
        nextQuesBtn = findViewById(R.id.nextQuesBtn);
        prevQuesBtn = findViewById(R.id.prevQuesBtn);
        proceedToSubBtn = findViewById(R.id.proceedToSubBtn);
        hint_txt = findViewById(R.id.hint_txt);
        questionImage = findViewById(R.id.questionImage);
        tvPositiveMarks = findViewById(R.id.tvPositiveMarks);
        tvNegativeMarks = findViewById(R.id.tvNegativeMarks);


        drawer = findViewById(R.id.drawer_layout);


        test_id = getIntent().getStringExtra("id");

        Log.e("test_id", test_id);

        closeDrawerBtn = findViewById(R.id.closeDrawerBtn);
        closeDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
            }
        });
        openDrawerBtn = findViewById(R.id.openDrawerBtn);


        testSeriesTitle = findViewById(R.id.testSeriesTitle);
        testSeriesTitle.setText(titleTestSeries);


        submitTestBtn = findViewById(R.id.submitTest);
        submitTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OngoingTestActivity.this);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = OngoingTestActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.exit_popup, null);
                dialogBuilder.setView(dialogView);

                //Alert Dialog Layout work
                final AlertDialog alertDialog = dialogBuilder.create();
//                      TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button cancelBtn = dialogView.findViewById(R.id.cancelButton);
                Button yesBtn = dialogView.findViewById(R.id.yesButton);
                Button okBtn = dialogView.findViewById(R.id.okButton);

                yesBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.VISIBLE);
                okBtn.setVisibility(View.GONE);
                TextView message = dialogView.findViewById(R.id.message);
                TextView title = dialogView.findViewById(R.id.title);
                title.setText("Alert!");
                message.setText("Are you sure to submit the answer ?");

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new SubmitAnswer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        alertDialog.dismiss();

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OngoingTestActivity.this);
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = OngoingTestActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.exit_popup, null);
                        dialogBuilder.setView(dialogView);

                        //Alert Dialog Layout work
                        final AlertDialog alertDialogend = dialogBuilder.create();
//                      TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
                        alertDialogend.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Button cancelBtn = dialogView.findViewById(R.id.cancelButton);
                        Button yesBtn = dialogView.findViewById(R.id.yesButton);
                        Button okBtn = dialogView.findViewById(R.id.okButton);

                        yesBtn.setVisibility(View.GONE);
                        cancelBtn.setVisibility(View.GONE);
                        okBtn.setVisibility(View.VISIBLE);
                        TextView message = dialogView.findViewById(R.id.message);
                        TextView title = dialogView.findViewById(R.id.title);
                        title.setVisibility(View.GONE);

                        message.setText("Your test has been submitted successfully.\n your result will be live soon..");


                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Intent intent = new Intent(OngoingTestActivity.this, AllTestPageActivity.class);
                                intent.putExtra("id", testSeriesId);
                                startActivity(intent);
                                questionArrayList.clear();
                                sectionArrayList.clear();
                                OngoingTestActivity.this.finish();


                                alertDialogend.dismiss();

                            }
                        });


                        alertDialogend.show();
                        alertDialogend.setCancelable(false);
                        alertDialogend.setCanceledOnTouchOutside(false);

                    }
                });


                alertDialog.show();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);


            }
        });


        //SectionList Work from here------------------------
//        sectionListView = findViewById(R.id.sectionsList);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        sectionListView.setLayoutManager(linearLayoutManager);
//        itemSectionAdapter = new ItemSectionAdapter(sectionArrayList);
//        sectionListView.setAdapter(itemSectionAdapter);
//
//        //Questions List----------------------------
//        questionsListView = findViewById(R.id.questionsListView);
//        LinearLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
//
//        questionsListView.setLayoutManager(gridLayoutManager);
//        itemObjectiveQuestionAdapter = new ItemObjectiveQuestionAdapter(sectionArrayList.get(0).getQuestionsList());
////        Log.e("List", sectionArrayList.get(0).getQuestionsList().toString());
//        questionsListView.setAdapter(itemObjectiveQuestionAdapter);
//        questionsListView.scrollToPosition(0);

        navigationTimer = findViewById(R.id.timer);


//Loading only first section-----------------
//        Object temp = sectionArrayList.get(0).getQuestionsList();
//        ArrayList<ItemObjectiveQuestion> questionArrayList= (ArrayList<ItemObjectiveQuestion>) temp;
        timer();

    }

    private void timer() {

        final Runnable runnableTimer = new Runnable() {
            @Override
            public void run() {
                timeLimit--;
                hours = timeLimit / 3600;
                long remainder = timeLimit - hours * 3600;
                min = remainder / 60;
//                min = timeLimit / 60;
                sec = timeLimit % 60;
                String hour = String.format("%02d", hours);
                String minut = String.format("%02d", min);
                String second = String.format("%02d", sec);

                if (hours == 0) {
                    questionPageTimer.setText(minut + ":" + second);
                    navigationTimer.setText(minut + ":" + second);

                } else {
                    questionPageTimer.setText(hours + ":" + minut + ":" + second);
                    navigationTimer.setText(hours + ":" + minut + ":" + second);
                }


                Log.e("timmer", "" + timeLimit);

                if (timeLimit != 0) {
                    timer.postDelayed(this, 1000);


                }
                if (timeLimit == 0) {
                    // Submit test result
                    new SubmitAnswer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    timer.removeCallbacksAndMessages(null);

                    //Alert for AutoSubmitted
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OngoingTestActivity.this);
                    // ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = OngoingTestActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.exit_popup, null);
                    dialogBuilder.setView(dialogView);

                    //Alert Dialog Layout work
                    final AlertDialog alertDialog = dialogBuilder.create();
//                  TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button cancelBtn = dialogView.findViewById(R.id.cancelButton);
                    Button yesBtn = dialogView.findViewById(R.id.yesButton);
                    Button okBtn = dialogView.findViewById(R.id.okButton);

                    yesBtn.setVisibility(View.GONE);
                    cancelBtn.setVisibility(View.GONE);
                    okBtn.setVisibility(View.VISIBLE);
                    TextView message = dialogView.findViewById(R.id.message);
                    TextView title = dialogView.findViewById(R.id.title);
                    title.setText("Time Out!");


                    message.setText("Your test has been auto submitted successfully.");


                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(OngoingTestActivity.this, AllTestPageActivity.class);
                            intent.putExtra("id", testSeriesId);

                            startActivity(intent);
                            OngoingTestActivity.this.finish();
                            questionArrayList.clear();
                            sectionArrayList.clear();
                            alertDialog.dismiss();

                        }
                    });


                    alertDialog.show();
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);


                }
                if (timeLimit % 10 == 0) {
                    SharedPreferences.Editor timerPref = getSharedPreferences(test_id, MODE_PRIVATE).edit();
                    timerPref.putLong("timeSave", timeLimit);
                    Log.e("unsavedtime", String.valueOf(timeLimit));
                    timerPref.apply();
                }


            }
        };
        timer.postDelayed(runnableTimer, 1000);
    }

    class SubmitAnswer extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);
        String url = getApplicationContext().getString(R.string.apiURL) + "submitTestSeries";


        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
////            this.dialog.show();

            timer.removeCallbacksAndMessages(null);
            SharedPreferences.Editor timerPref = getSharedPreferences(test_id, MODE_PRIVATE).edit();
            timerPref.putLong("timeSave", 0);
            timerPref.apply();

            Map<String, String> answerMap = new HashMap<String, String>();

            for (int i = 0; i < sectionArrayList.get(0).getQuestionsList().size(); i++) {
                ItemObjectiveQuestion objectiveQuestion = (ItemObjectiveQuestion) sectionArrayList.get(0).getQuestionsList().get(i);

                Log.e("Questionlist", String.valueOf(sectionArrayList.get(0).getQuestionsList()));
                Log.e("questions", String.valueOf(((ItemObjectiveQuestion) sectionArrayList.get(0).getQuestionsList().get(i)).getQuestion()));
                Log.e("answered", String.valueOf(((ItemObjectiveQuestion) sectionArrayList.get(0).getQuestionsList().get(i)).getAnsweredAnswer()));

                if (objectiveQuestion.getStatus().equals("Answered")) {
                    Log.e("CHECKING ANswer", "---" + objectiveQuestion.getAnsweredAnswer());
                    Log.e("CHECKING ANswer", "---" + objectiveQuestion.getId());
                    answerMap.put(objectiveQuestion.getId(), "option" + objectiveQuestion.getAnsweredAnswer());
                    Log.e("answer", objectiveQuestion.getAnsweredAnswer());
                }
            }


            Log.e("InputData", String.valueOf((JSONObject.wrap(answerMap))));

            Map<String, Map> resultMap = new HashMap<>();
            resultMap.put("result", answerMap);

            Log.e("result map", String.valueOf(JSONObject.wrap(resultMap)));

            Log.e("InputEncoded", URLEncoder.encode(String.valueOf(JSONObject.wrap(answerMap))));
            resultData = URLEncoder.encode(String.valueOf(JSONObject.wrap(resultMap)));


        }

        @Override
        protected String doInBackground(String... params) {
            Intent intent = getIntent();
            String test_id_intent = intent.getStringExtra("id");
            int test_id = Integer.parseInt(test_id_intent);

            SharedPreferences preferences = getSharedPreferences("cnb" + test_id, Context.MODE_PRIVATE);
            Log.e("preference", preferences.toString());

            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            JSONParser jsonParser = new JSONParser(OngoingTestActivity.this);
//            int sid;
//            SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
//            sid = studDetails.getInt("sid", 0);
            //json raw data should be encoded**


            String param = "sid=" + sid + "&test_id=" + test_id + "&result=" + resultData + "&testseries_id=" + testSeriesId;
            Log.e("params", param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
            if (jsonObject == null) {
                return "";
            }
            Log.e("Log result >>", jsonObject.toString());
            if (jsonObject != null) {
                Log.e("Log result >>", jsonObject.toString());
                return jsonObject.toString();
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Response reached---------------value in 's' variable
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        timer.removeCallbacksAndMessages(null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OngoingTestActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = OngoingTestActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exit_popup, null);
        dialogBuilder.setView(dialogView);

        //Alert Dialog Layout work
        final AlertDialog alertDialog = dialogBuilder.create();
//                TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button cancelBtn = dialogView.findViewById(R.id.cancelButton);
        Button yesBtn = dialogView.findViewById(R.id.yesButton);
        TextView message = dialogView.findViewById(R.id.message);
        TextView title = dialogView.findViewById(R.id.title);
        title.setText("Alert!");


        message.setText("Are you Sure, you want to exit from the Test before final submit?");
        cancelBtn.setText("No");

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OngoingTestActivity.super.onBackPressed();
                timer.removeCallbacksAndMessages(null);
            }
        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
//

    }

    class GetTestDetails extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(OngoingTestActivity.this);


//            String param = "uid=" + uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseId;
            String param = "test_id=" + testId + "&sid=" + sid;
            Log.e("param", param);

            String url = getApplicationContext().getString(R.string.apiURL) + "getTestQueList";

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
                    final JSONObject dataObj = jsonObject.getJSONObject("data");
                    dialog.dismiss();
                    switch (status) {
                        case "success":


                            SharedPreferences saveTimeData = getSharedPreferences(testId, MODE_PRIVATE);

                            Long time_saved = saveTimeData.getLong("timeSave", 0);
                            if (time_saved == 0) {
                                timeLimit = dataObj.getLong("duration");

                                long hours = timeLimit / 3600;
                                long remainder = timeLimit - hours * 3600;
                                long min = remainder / 60;
                                long sec = timeLimit % 60;
                                String hour = String.format("%02d", hours);
                                String minut = String.format("%02d", min);

                                if (hours == 0) {
                                    timeDuration = minut + " minutes";
                                } else {
                                    timeDuration = hour + ":" + minut + " hours";
                                }

                            } else {
                                timeLimit = time_saved;

                            }
                            Log.e("timesaved", String.valueOf(time_saved));

                            titleTestSeries = dataObj.getString("title");


                            SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat outputFormatter = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa");


                            //fetching sections--------------
                            JSONArray sectionsArray = dataObj.getJSONArray("section_list");
                            sectionArrayList.clear();
                            for (int i = 0; i < sectionsArray.length(); i++) {
                                JSONObject sectionObject = sectionsArray.getJSONObject(i);
                                ItemSection section = new ItemSection();

                                section.setId(sectionObject.getString("id"));
                                section.setName(sectionObject.getString("name"));


                                JSONArray questionsArray = sectionObject.getJSONArray("question_list");


                                questionArrayList = new ArrayList<ItemObjectiveQuestion>();
                                questionArrayList.clear();


                                for (int j = 0; j < questionsArray.length(); j++) {
                                    ItemObjectiveQuestion objectiveQuestion = new ItemObjectiveQuestion();
                                    JSONObject questionObject = questionsArray.getJSONObject(j);

                                    SharedPreferences saveTestData = getSharedPreferences("cnb" + testId, MODE_PRIVATE);

                                    answer = saveTestData.getString(questionObject.getString("id"), "NO_VALUE");
                                    Log.e("answer", answer);

                                    if (answer.equals("NO_VALUE")) {
                                        objectiveQuestion.setStatus("");

                                    } else if (answer.equals("")) {
                                        objectiveQuestion.setStatus("");
                                    } else {
                                        objectiveQuestion.setStatus("Answered");
                                        objectiveQuestion.setAnsweredAnswer(answer);


                                    }
                                    objectiveQuestion.setId(questionObject.getString("id"));

                                    objectiveQuestion.setQuestion(questionObject.getString("question"));
                                    objectiveQuestion.setPositiveMarks(" +" + questionObject.getDouble("p_marks") + "");
                                    objectiveQuestion.setNegativeMarks(" -" + questionObject.getDouble("n_marks") + "");
                                    if (!questionObject.getString("image").equals(null)) {
                                        objectiveQuestion.setQuestionImageURL(questionObject.getString("image"));

                                    }
//                                            JSONArray optionsArray = questionObject.getJSONArray("options");
//                                            ArrayList<ItemOption> optionsList = new ArrayList<>();
//                                            for (int k = 0; k < optionsArray.length(); k++) {
//                                                ItemOption itemOption = new ItemOption();
//                                                itemOption.setOption(optionsArray.getString(k));
//                                                if (optionsArray.getString(k).trim().equals(answer.trim())) {
//                                                    itemOption.setSelected(true);
//                                                }
//                                                optionsList.add(itemOption);
//                                            }

                                    JSONObject optionObject = questionObject.getJSONObject("options");
                                    ArrayList<ItemOption> optionsList = new ArrayList<>();
                                    int a = 1;
                                    optionsList.clear();
                                    for (int k = 0; k < optionObject.length(); k++) {
                                        ItemOption itemOption = new ItemOption();

                                        JSONObject optionObjectvar = optionObject.getJSONObject("option" + a);
                                        Log.e("objectoption", String.valueOf(optionObject.getJSONObject("option1")));
                                        itemOption.setOptNo("option" + a);
                                        String optionNumber = "option" + a;
                                        itemOption.setOption(optionObjectvar.getString("title"));
                                        itemOption.setOptNo(String.valueOf(k + 1));


                                        itemOption.setOptionImageUrl(optionObjectvar.getString("image"));
                                        if (optionNumber.trim().equals(answer.trim())) {
                                            itemOption.setSelected(true);
                                        }

                                        optionsList.add(itemOption);
                                        a++;

                                    }

//                                            Collections.shuffle(questionArrayList);
                                    Object questionsList = questionArrayList;
                                    section.setQuestionsList((ArrayList<Object>) questionsList);
                                    sectionArrayList.add(section);
                                    Log.e("Test Question Size-", String.valueOf(sectionArrayList.size()));


//                                            Collections.shuffle(optionsList);
                                    objectiveQuestion.setOptions(optionsList);
                                    questionArrayList.add(objectiveQuestion);

                                    //SectionList Work from here------------------------
                                    sectionListView = findViewById(R.id.sectionsList);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OngoingTestActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                    sectionListView.setLayoutManager(linearLayoutManager);
                                    itemSectionAdapter = new ItemSectionAdapter(sectionArrayList);
                                    sectionListView.setAdapter(itemSectionAdapter);

                                    //Questions List----------------------------
                                    questionsListView = findViewById(R.id.questionsListView);
                                    LinearLayoutManager gridLayoutManager = new GridLayoutManager(OngoingTestActivity.this, 5);

                                    questionsListView.setLayoutManager(gridLayoutManager);
                                    itemObjectiveQuestionAdapter = new ItemObjectiveQuestionAdapter(sectionArrayList.get(0).getQuestionsList());
//        Log.e("List", sectionArrayList.get(0).getQuestionsList().toString());
                                    questionsListView.setAdapter(itemObjectiveQuestionAdapter);
                                    questionsListView.scrollToPosition(0);


                                }


                            }

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OngoingTestActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = OngoingTestActivity.this.getLayoutInflater();
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
                            Toast.makeText(OngoingTestActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(OngoingTestActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
