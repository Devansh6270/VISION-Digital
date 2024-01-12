package com.vision_digital.TestSeries.model.objectiveQuestion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.TestSeries.model.objectiveQuestion.options.ItemOptionAdapter;

import java.util.ArrayList;

import static com.vision_digital.TestSeries.OngoingTestActivity.hint_txt;
import static com.vision_digital.TestSeries.OngoingTestActivity.nextQuesBtn;
import static com.vision_digital.TestSeries.OngoingTestActivity.optionsListView;
import static com.vision_digital.TestSeries.OngoingTestActivity.prevQuesBtn;
import static com.vision_digital.TestSeries.OngoingTestActivity.proceedToSubBtn;
import static com.vision_digital.TestSeries.OngoingTestActivity.questionImage;
import static com.vision_digital.TestSeries.OngoingTestActivity.questionNumber;
import static com.vision_digital.TestSeries.OngoingTestActivity.questionView;
import static com.vision_digital.TestSeries.OngoingTestActivity.drawer;
import static com.vision_digital.TestSeries.OngoingTestActivity.openDrawerBtn;


public class ItemObjectiveQuestionAdapter extends RecyclerView.Adapter<ItemObjectiveQuestionViewHolder> {
    Context context;
    ArrayList<Object> objectiveQuestionList;
    public static int currentQuestion = 0;
    boolean firstTime = true, skip = false;
    String optionSaved;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final ItemObjectiveQuestionViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //for first time it will be automatic--------
        final ItemObjectiveQuestion objectiveQuestion = (ItemObjectiveQuestion) objectiveQuestionList.get(position);

        final Object questionsList = objectiveQuestionList;
        if (position == 0 && firstTime) {
            loadObjectiveQuestion((ArrayList<ItemObjectiveQuestion>) questionsList, position);
            currentQuestion = 0;
            firstTime = false;
        }
        final String question_id = objectiveQuestion.getId();

        holder.questionNumberTitle.setText("" + (position + 1));

        nextQuesBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
//                ArrayList<ItemObjectiveQuestion> itemObjectiveQuestionArrayList = (ArrayList<ItemObjectiveQuestion>) questionsList;
//                for (int i = 0; i < itemObjectiveQuestionArrayList.get(currentQuestion).getOptions().size(); i++) {
//                    if (itemObjectiveQuestionArrayList.get(currentQuestion).getOptions().get(i).isSelected()) {
//                        itemObjectiveQuestionArrayList.get(currentQuestion).setStatus("Answered");
//                        Log.e("nextcurrentOption", itemObjectiveQuestionArrayList.get(currentQuestion).getAnsweredAnswer());
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
//Update list of question layout-------

                loadObjectiveQuestion((ArrayList<ItemObjectiveQuestion>) questionsList, currentQuestion - 1);

            }
        });
        openDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                loadObjectiveQuestion((ArrayList<ItemObjectiveQuestion>) questionsList, currentQuestion);
                drawer.openDrawer(Gravity.RIGHT);
            }
        });
        proceedToSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadObjectiveQuestion((ArrayList<ItemObjectiveQuestion>) questionsList, currentQuestion);
                drawer.openDrawer(Gravity.RIGHT);
            }
        });


//        if (objectiveQuestion.getStatus().equals("NotAnswered")) {
//            holder.questionNumberTitle.setBackgroundColor(Color.parseColor("#D30404"));
//        }
        if (objectiveQuestion.getStatus().equals("Answered")) {
            holder.questionNumberTitle.setBackgroundColor(Color.parseColor("#fe6463"));
        } else {
            holder.questionNumberTitle.setBackgroundColor(Color.GRAY);
        }

        holder.questionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadObjectiveQuestion((ArrayList<ItemObjectiveQuestion>) questionsList, position);
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadObjectiveQuestion(final ArrayList<ItemObjectiveQuestion> questionArrayList, final int position) {

        ArrayList<ItemObjectiveQuestion> itemObjectiveQuestionArrayList = (ArrayList<ItemObjectiveQuestion>) questionArrayList;
        Log.e("currentValue", "" + currentQuestion);
        for (int i = 0; i < itemObjectiveQuestionArrayList.get(currentQuestion).getOptions().size(); i++) {


            if (itemObjectiveQuestionArrayList.get(currentQuestion).getOptions().get(i).isSelected()) {
                itemObjectiveQuestionArrayList.get(currentQuestion).setStatus("Answered");
                Log.e("Answered", "True" + (i + 1));
                break;

            }

            else {
                Log.e("Answered", "false");
                itemObjectiveQuestionArrayList.get(currentQuestion).setStatus("NotAnswered");
            }


        }
        if (skip) {
            notifyItemChanged(currentQuestion);

        }
        skip = true;
        currentQuestion = position;
//        questionView.setText(questionArrayList.get(position).getQuestion());
        questionView.setText(String.valueOf(Html.fromHtml(questionArrayList.get(position).getQuestion(), Html.FROM_HTML_MODE_COMPACT)));
        Glide.with(context)
                .load(questionArrayList.get(position).getQuestionImageURL())
                .into(questionImage);
        questionImage.setOnClickListener(new View.OnClickListener() {
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
        hint_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "No hint available!..", Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                View dialogView = inflater.inflate(R.layout.activity_image_transition, null);
//                dialogBuilder.setView(dialogView);
//
//                //Alert Dialog Layout work
//                final AlertDialog alertDialog = dialogBuilder.create();
////                TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//
//                ImageView optionImageTrans = dialogView.findViewById(R.id.optionImageTrans);
//                TextView hint_test = dialogView.findViewById(R.id.hint_test);
//                hint_test.setVisibility(View.VISIBLE);
////                Glide.with(context)
////                        .load(questionArrayList.get(position).getQuestionImageURL())
////                        .into(optionImageTrans);
//                ImageView closeBtnn = dialogView.findViewById(R.id.closeBtnn);
//                closeBtnn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//
//                alertDialog.show();
//                alertDialog.setCanceledOnTouchOutside(true);

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
            proceedToSubBtn.setVisibility(View.VISIBLE);
        } else {
            nextQuesBtn.setVisibility(View.VISIBLE);
            proceedToSubBtn.setVisibility(View.GONE);

        }


        drawer.closeDrawers();


    }

    @Override
    public int getItemCount() {
        return objectiveQuestionList != null ? objectiveQuestionList.size() : 0;
    }
}
