package com.vision_classes.TestSeries.model.result.objectiveQuestion;

import static com.vision_classes.TestSeries.ShowResultActivity.ansImage;
import static com.vision_classes.TestSeries.ShowResultActivity.btnUnderstood;
import static com.vision_classes.TestSeries.ShowResultActivity.dialogExplain;
import static com.vision_classes.TestSeries.ShowResultActivity.dialogWvResult;
import static com.vision_classes.TestSeries.ShowResultActivity.drawer_ans;
import static com.vision_classes.TestSeries.ShowResultActivity.nextQuesBtn;
import static com.vision_classes.TestSeries.ShowResultActivity.optionsListView;
import static com.vision_classes.TestSeries.ShowResultActivity.prevQuesBtn;
import static com.vision_classes.TestSeries.ShowResultActivity.questionNumber;
import static com.vision_classes.TestSeries.ShowResultActivity.questionView;
import static com.vision_classes.TestSeries.ShowResultActivity.tvExplaination;
import static com.vision_classes.TestSeries.ShowResultActivity.tvNegativeMarks;
import static com.vision_classes.TestSeries.ShowResultActivity.tvPositiveMarks;
import static com.vision_classes.TestSeries.ShowResultActivity.wvResult;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_classes.R;
import com.vision_classes.TestSeries.model.result.objectiveQuestion.options.ItemOptionAdapter;

import java.util.ArrayList;

public class ItemObjectiveQuestionAdapter extends RecyclerView.Adapter<ItemObjectiveQuestionViewHolder> {
    Context context;
    ArrayList<Object> objectiveQuestionList;
    public static int currentQuestion = 0;
    boolean firstTime = true, skip = false;

    public ItemObjectiveQuestionAdapter(ArrayList<Object> objectiveQuestionList) {
        this.objectiveQuestionList = objectiveQuestionList;
    }

    @NonNull
    @Override
    public ItemObjectiveQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemObjectiveQuestionViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test_series_question, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemObjectiveQuestionViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //for first time it will be automatic--------

        final Object questionsList = objectiveQuestionList;
        if (position == 0 && firstTime) {
            loadObjectiveQuestion((ArrayList<ItemObjectiveQuestion>) questionsList, position);
            currentQuestion = 0;
            firstTime = false;
        }

        holder.questionNumberTitle.setText("" + (position + 1));

        nextQuesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ArrayList<ItemObjectiveQuestion> itemObjectiveQuestionArrayList = (ArrayList<ItemObjectiveQuestion>) questionsList;
//                for (int i = 0; i < itemObjectiveQuestionArrayList.get(currentQuestion).getOptions().size(); i++) {
//                    if (itemObjectiveQuestionArrayList.get(currentQuestion).getOptions().get(i).isSelected()) {
//                        itemObjectiveQuestionArrayList.get(currentQuestion).setStatus("Answered");
//                        Log.e("Answered","True"+(i+1));
//                        break;
//                    } else {
//                        Log.e("Answered","false");
//                        itemObjectiveQuestionArrayList.get(currentQuestion).setStatus("NotAnswered");
//                    }
//                }
//                notifyItemChanged(currentQuestion);

                loadObjectiveQuestion((ArrayList<ItemObjectiveQuestion>) questionsList, currentQuestion + 1);

            }
        });

        prevQuesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//--------------Update list of question layout-------

                loadObjectiveQuestion((ArrayList<ItemObjectiveQuestion>) questionsList, currentQuestion - 1);

            }
        });

        final ItemObjectiveQuestion objectiveQuestion = (ItemObjectiveQuestion) objectiveQuestionList.get(position);


        if (objectiveQuestion.getAnswer_status().equals("correct")) {
            holder.questionNumberTitle.setBackgroundColor(Color.parseColor("#03980A"));
    } else if (objectiveQuestion.getStatus().equals("incorrect")){
            holder.questionNumberTitle.setBackgroundColor(Color.parseColor("#D30404"));
        }else{
            holder.questionNumberTitle.setBackgroundColor(Color.GRAY);
        }

        holder.questionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadObjectiveQuestion((ArrayList<ItemObjectiveQuestion>) questionsList, position);
            }
        });


        String solutionResult =  ((ItemObjectiveQuestion) objectiveQuestionList.get(position)).getWvResult();

        Log.e("solutionResult", solutionResult);
        if (solutionResult.equals("")){
            tvExplaination.setVisibility(View.GONE);
            wvResult.setVisibility(View.GONE);
        } else {
//========Currently we are not using wvResult in the main screen instead of that we are using Dialog wvResult=============================
            wvResult.setVisibility(View.GONE);
            String testHtml = "";
//================================================================================================================================

            wvResult.loadDataWithBaseURL(null, solutionResult, "text/html", "UTF-8", null);
            dialogWvResult.loadDataWithBaseURL(null, solutionResult, "text/html", "UTF-8", null);


            Log.e("In ELSE",solutionResult);
            tvExplaination.setVisibility(View.VISIBLE);

            tvExplaination.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogExplain.show();
                }
            });
            btnUnderstood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogExplain.dismiss();
                }
            });
        }

    }


    public void loadObjectiveQuestion(final ArrayList<ItemObjectiveQuestion> questionArrayList, final int position) {

        ArrayList<ItemObjectiveQuestion> itemObjectiveQuestionArrayList = (ArrayList<ItemObjectiveQuestion>) questionArrayList;
        Log.e("currentValue",""+currentQuestion);
//        for (int i = 0; i < itemObjectiveQuestionArrayList.get(currentQuestion).getOptions().size(); i++) {
//            if (itemObjectiveQuestionArrayList.get(currentQuestion).getOptions().get(i).isSelected()) {
//                itemObjectiveQuestionArrayList.get(currentQuestion).setStatus("Answered");
//                Log.e("Answered", "True" + (i + 1));
//                break;
//            } else {
//                Log.e("Answered", "false");
//                itemObjectiveQuestionArrayList.get(currentQuestion).setStatus("NotAnswered");
//            }
//        }
        if (skip) {
            notifyItemChanged(currentQuestion);

        }
        skip = true;
        currentQuestion = position;
        Log.e("SelectedAnswer"+position,questionArrayList.get(position).getSelected_answer());
        questionView.setText(questionArrayList.get(position).getQuestion().toString());
        tvNegativeMarks.setText(questionArrayList.get(position).getNegativeMarks().toString());
        tvPositiveMarks.setText(questionArrayList.get(position).getPositiveMarks().toString());

        Glide.with(context)
                .load(questionArrayList.get(position).getQuestionImageURL())
                .into(ansImage);



        ansImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View dialogView = inflater.inflate(R.layout.activity_image_transition, null);
                dialogBuilder.setView(dialogView);

                //Alert Dialog Layout work
                final AlertDialog alertDialog = dialogBuilder.create();
//                TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                ImageView optionImageTrans = dialogView.findViewById(R.id.optionImageTrans);
                Glide.with(context)
                        .load(questionArrayList.get(position).getQuestionImageURL())
                        .into(optionImageTrans);
                ImageView closeBtnn = dialogView.findViewById(R.id.closeBtnn);
                closeBtnn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(true);
            }
        });

        questionNumber.setText("" + (position + 1) + " of " + questionArrayList.size());
        //SectionList Work from here------------------------

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        optionsListView.setLayoutManager(linearLayoutManager);
        ItemOptionAdapter itemOptionAdapter = new ItemOptionAdapter(questionArrayList.get(position).getOptions());
        optionsListView.setAdapter(itemOptionAdapter);

        //Next and Prev layout work----
        if (position == 0) {
            prevQuesBtn.setVisibility(View.GONE);
        } else {
            prevQuesBtn.setVisibility(View.VISIBLE);
        }

        if (position == questionArrayList.size() - 1) {
            nextQuesBtn.setVisibility(View.GONE);
        } else {
            nextQuesBtn.setVisibility(View.VISIBLE);
        }


        drawer_ans.closeDrawers();

    }

    @Override
    public int getItemCount() {
        return objectiveQuestionList != null ? objectiveQuestionList.size() : 0;
    }
}
