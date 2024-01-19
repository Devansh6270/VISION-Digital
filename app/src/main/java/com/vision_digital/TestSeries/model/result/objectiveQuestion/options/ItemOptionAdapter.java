package com.vision_digital.TestSeries.model.result.objectiveQuestion.options;

import static com.vision_digital.TestSeries.ShowResultActivity.note_one;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;

import java.util.ArrayList;

public class ItemOptionAdapter extends RecyclerView.Adapter<ItemOptionViewHolder> {

    Context context;
    ArrayList<ItemOption> optionArrayLists = new ArrayList<>();
    int clicked = 100;

    public ItemOptionAdapter(ArrayList<ItemOption> optionArrayLists) {
        this.optionArrayLists = optionArrayLists;
    }

    @NonNull
    @Override
    public ItemOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemOptionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_objective_question_option, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemOptionViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.option.setText(optionArrayLists.get(position).getOption());
        String optionNumber = optionArrayLists.get(position).getTvOptionNo();
        if (optionArrayLists.get(position).getOption().equals("")){
            holder.llOption.setVisibility(View.GONE);
        } else {
            holder.llOption.setVisibility(View.VISIBLE);
            holder.tvOptionNo.setText(optionNumber+") ");
        }

        if (!optionArrayLists.get(position).getOptionImageUrl().equals("")) {
            holder.optionImage.setVisibility(View.VISIBLE);
            holder.zoomImage.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(optionArrayLists.get(position).getOptionImageUrl())
                    .into(holder.optionImage);
        } else {
            holder.optionImage.setVisibility(View.GONE);
            holder.zoomImage.setVisibility(View.GONE);


        }
        holder.zoomImage.setOnClickListener(new View.OnClickListener() {
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
                        .load(optionArrayLists.get(position).getOptionImageUrl())
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



        if (optionArrayLists.get(position).getCorrect_ans().trim().equals(optionArrayLists.get(position).getOptionNo().trim())) {

//            holder.optionCard.setBackgroundColor(Color.parseColor("#03980A"));
            holder.optionCard.setBackgroundResource(R.drawable.bg_correct_answer);

            holder.option.setTextColor(Color.WHITE);
        }
        Log.e("Incorrect","User Selected Option"+optionArrayLists.get(position).getSelected_ans().trim());
        if (optionArrayLists.get(position).getOptionNo().trim().equals(optionArrayLists.get(position).getSelected_ans().trim())) {
//            holder.optionCard.setBackgroundColor(Color.parseColor("#D30404"));
            holder.optionCard.setBackgroundResource(R.drawable.bg_incorrect_answer);
            holder.option.setTextColor(Color.WHITE);
//            note_one.setText("Hey! You did it Right...");
//            note_two.setText("You are among 10.0% of the student who attempted it and got the right answer");
//
////            holder.optionCard.setBackgroundColor(Color.WHITE);

        }
        if (optionArrayLists.get(position).getSelected_ans().trim().equals(optionArrayLists.get(position).getCorrect_ans().trim())) {
            if (optionArrayLists.get(position).getOptionNo().trim().equals(optionArrayLists.get(position).getSelected_ans().trim())) {
//               holder.optionCard.setBackgroundColor(Color.parseColor("#03980A"));
                holder.optionCard.setBackgroundResource(R.drawable.bg_correct_answer);
                holder.option.setTextColor(Color.WHITE);
//                note_one.setText("Hey! You did it Right...");
//                note_two.setText("You are among 10.0% of the student who attempted it and got the right answer");
//
////            holder.optionCard.setBackgroundColor(Color.WHITE);

            }
        }
        // my change


//
//
//        }
        else {
//            holder.optionCard.setBackgroundColor(Color.WHITE);
//            holder.option.setTextColor(Color.BLACK);
        }


        if (optionArrayLists.get(position).ans_status.equals("correct")) {
//            holder.optionCard.setBackgroundColor(Color.parseColor("#03980A"));
//            holder.optionCard.setBackgroundColor(Color.RED);
//            holder.option.setTextColor(Color.WHITE);
            note_one.setTextColor(Color.parseColor("#03980A"));

            note_one.setText("Hey! You did it Right...");
//            note_two.setText("You are among 10.0% of the student who attempted it and got the right answer");

        } else if (optionArrayLists.get(position).ans_status.equals("incorrect")) {
//            holder.optionCard.setBackgroundColor(Color.GREEN);
//            holder.option.setTextColor(Color.WHITE);
            note_one.setText("Oops! it's a wrong answer...");
            note_one.setTextColor(Color.parseColor("#F10202"));
//            note_two.setText("Out of all 80.5% of the students did it right and you missed it.");

        } else if (optionArrayLists.get(position).ans_status.equals("not attempted")) {
            note_one.setText("Not Attempted");
            note_one.setTextColor(Color.parseColor("#FFC107"));
//            note_two.setText("You didn't attempted this question.");
        } else {
            note_one.setText("");
            note_one.setTextColor(Color.parseColor("#FFC107"));
//            note_two.setText("");
        }

//        holder.optionCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (clicked != position) {
//                    for (int i = 0; i < optionArrayList.size(); i++) {
//                        if (i == position) {
//                            optionArrayList.get(i).setSelected(true);
//                            clicked = position;
//                            ItemObjectiveQuestion itemObjectiveQuestion = (ItemObjectiveQuestion) sectionArrayList.get(0).getQuestionsList().get(currentQuestion);
//                            itemObjectiveQuestion.setAnsweredAnswer(optionArrayList.get(i).getOption());
//                        } else {
//                            optionArrayList.get(i).setSelected(false);
//                        }
//                    }
//                    notifyDataSetChanged();
//                } else {
//                    optionArrayList.get(position).setSelected(false);
//                    holder.optionCard.setBackgroundColor(Color.WHITE);
//                    holder.option.setTextColor(Color.BLACK);
//                    clicked = 100;
//                    ItemObjectiveQuestion itemObjectiveQuestion = (ItemObjectiveQuestion) sectionArrayList.get(0).getQuestionsList().get(currentQuestion);
//                    itemObjectiveQuestion.setAnsweredAnswer("");
//                    notifyDataSetChanged();
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return optionArrayLists != null ? optionArrayLists.size() : 0;
    }
}
