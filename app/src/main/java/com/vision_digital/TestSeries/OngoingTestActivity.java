package com.vision_digital.TestSeries;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vision_digital.R;
import com.vision_digital.TestSeries.model.objectiveQuestion.ItemObjectiveQuestion;
import com.vision_digital.TestSeries.model.objectiveQuestion.ItemObjectiveQuestionAdapter;
import com.vision_digital.TestSeries.model.section.ItemSectionAdapter;
import com.vision_digital.helperClasses.JSONParser;


import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.vision_digital.TestSeries.TestDetailsActivity.sectionArrayList;
import static com.vision_digital.TestSeries.TestDetailsActivity.timeLimit;
import static com.vision_digital.TestSeries.TestDetailsActivity.titleTestSeries;

public class OngoingTestActivity extends AppCompatActivity {

    public static CardView closeDrawerBtn, openDrawerBtn;

    TextView testSeriesTitle;
    TextView navigationTimer;
    TextView submitTestBtn;
    JSONObject jsonRawData = new JSONObject();
    JSONObject jsonRawData_one = new JSONObject();
    String resultData;
    public static String test_id;


    RecyclerView sectionListView;
    ItemSectionAdapter itemSectionAdapter;

    public static RecyclerView questionsListView;
    ItemObjectiveQuestionAdapter itemObjectiveQuestionAdapter;

    //Question Layout

    public static TextView questionNumber, questionPageTimer;
    public static TextView questionView;
    public static ImageView questionImage;
    public static RecyclerView optionsListView;
    public static TextView nextQuesBtn, prevQuesBtn,proceedToSubBtn,hint_txt;

    public static DrawerLayout drawer;
    final Handler timer = new Handler(Looper.getMainLooper());


    //Timmer------------------------
    long min, sec, hours;

    //Global data:
    long currentQuestionNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_test);

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


        drawer = findViewById(R.id.drawer_layout);


        test_id = getIntent().getStringExtra("id");
        Log.e("test_id",test_id);

        closeDrawerBtn = findViewById(R.id.closeDrawerBtn);
        closeDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
            }
        });
        openDrawerBtn = findViewById(R.id.openDrawerBtn);
//        openDrawerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.openDrawer(Gravity.RIGHT);
//            }
//        });

        testSeriesTitle = findViewById(R.id.testSeriesTitle);
        testSeriesTitle.setText(titleTestSeries);


        submitTestBtn = findViewById(R.id.submitTest);
        submitTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(OngoingTestActivity.this);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = OngoingTestActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.exit_popup, null);
                dialogBuilder.setView(dialogView);

                //Alert Dialog Layout work
                final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();
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

                        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(OngoingTestActivity.this);
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = OngoingTestActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.exit_popup, null);
                        dialogBuilder.setView(dialogView);

                        //Alert Dialog Layout work
                        final androidx.appcompat.app.AlertDialog alertDialogend = dialogBuilder.create();
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

                                Intent intent = new Intent(OngoingTestActivity.this,TestSeriesDashboardActivity.class);
                                startActivity(intent);
//                                OngoingTestActivity.this.finish();
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
        sectionListView = findViewById(R.id.sectionsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        sectionListView.setLayoutManager(linearLayoutManager);
        itemSectionAdapter = new ItemSectionAdapter(sectionArrayList);
        sectionListView.setAdapter(itemSectionAdapter);

        //Questions List----------------------------
        questionsListView = findViewById(R.id.questionsListView);
        LinearLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);

        questionsListView.setLayoutManager(gridLayoutManager);
        itemObjectiveQuestionAdapter = new ItemObjectiveQuestionAdapter(sectionArrayList.get(0).getQuestionsList());
//        Log.e("List", sectionArrayList.get(0).getQuestionsList().toString());
        questionsListView.setAdapter(itemObjectiveQuestionAdapter);
        questionsListView.scrollToPosition(0);

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
                    androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(OngoingTestActivity.this);
                    // ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = OngoingTestActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.exit_popup, null);
                    dialogBuilder.setView(dialogView);

                    //Alert Dialog Layout work
                    final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();
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
                            Intent intent = new Intent(OngoingTestActivity.this,TestSeriesDashboardActivity.class);
                            startActivity(intent);
//                            OngoingTestActivity.this.finish();
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
                    answerMap.put(objectiveQuestion.getId(), objectiveQuestion.getAnsweredAnswer());
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

            SharedPreferences preferences =getSharedPreferences("cnb"+test_id,Context.MODE_PRIVATE);
            Log.e("preference",preferences.toString());

            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            JSONParser jsonParser = new JSONParser(OngoingTestActivity.this);
            int sid;
            SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
            sid = studDetails.getInt("sid", 0);
            //json raw data should be encoded**


            String param = "sid=" + sid + "&test_id=" + test_id + "&result=" + resultData;
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

        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(OngoingTestActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = OngoingTestActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exit_popup, null);
        dialogBuilder.setView(dialogView);

        //Alert Dialog Layout work
        final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();
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
}
