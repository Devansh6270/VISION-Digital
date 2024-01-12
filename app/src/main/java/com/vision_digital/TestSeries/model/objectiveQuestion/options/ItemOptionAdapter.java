package com.vision_digital.TestSeries.model.objectiveQuestion.options;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.vision_digital.TestSeries.model.objectiveQuestion.ItemObjectiveQuestion;

import java.util.ArrayList;

import static com.vision_digital.TestSeries.TestDetailsActivity.sectionArrayList;
import static com.vision_digital.TestSeries.model.objectiveQuestion.ItemObjectiveQuestionAdapter.currentQuestion;

public class ItemOptionAdapter extends RecyclerView.Adapter<ItemOptionViewHolder> {

    Context context;
    ArrayList<ItemOption> optionArrayList = new ArrayList<>();
    int clicked = 100;

    public ItemOptionAdapter(ArrayList<ItemOption> optionArrayList) {
        this.optionArrayList = optionArrayList;
    }

    @NonNull
    @Override
    public ItemOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemOptionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_objective_question_option, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemOptionViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.option.setText(optionArrayList.get(position).getOption());
//        Glide.with(context)
//                .load(optionArrayList.get(position).getOptionImageUrl())
//                .into(holder.optionImage);
        if (!optionArrayList.get(position).getOptionImageUrl().equals("")) {
            holder.optionImage.setVisibility(View.VISIBLE);
            holder.zoomImage.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(optionArrayList.get(position).getOptionImageUrl())
                    .into(holder.optionImage);
        } else {
            holder.optionImage.setVisibility(View.GONE);
            holder.zoomImage.setVisibility(View.GONE);

        }


        if (optionArrayList.get(position).isSelected()) {
            holder.optionCard.setBackgroundColor(Color.parseColor("#1893C5"));
        } else {
            holder.optionCard.setBackgroundColor(Color.WHITE);
        }

        holder.optionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked != position) {
                    for (int i = 0; i < optionArrayList.size(); i++) {
                        if (i == position) {
                            optionArrayList.get(i).setSelected(true);
                            clicked = position;
                            ItemObjectiveQuestion itemObjectiveQuestion = (ItemObjectiveQuestion) sectionArrayList.get(0).getQuestionsList().get(currentQuestion);
                            itemObjectiveQuestion.setAnsweredAnswer(optionArrayList.get(i).getOptNo());

                            Log.e("aaaaa",optionArrayList.get(i).getOptNo());
                            SharedPreferences.Editor test = context.getSharedPreferences(itemObjectiveQuestion.getId(),Context.MODE_PRIVATE).edit();
                            test.putString(itemObjectiveQuestion.getId(),optionArrayList.get(i).getOptNo());
                            test.apply();

                        } else {
                            optionArrayList.get(i).setSelected(false);
//                            ItemObjectiveQuestion itemObjectiveQuestion = (ItemObjectiveQuestion) sectionArrayList.get(0).getQuestionsList().get(currentQuestion);
//                            itemObjectiveQuestion.setAnsweredAnswer("");
//                            SharedPreferences.Editor test = context.getSharedPreferences("cnb" + test_id, Context.MODE_PRIVATE).edit();
//                            test.putString(itemObjectiveQuestion.getId(), "");
//                            test.apply();

                        }
                    }
                    notifyDataSetChanged();
                } else {
                    optionArrayList.get(position).setSelected(false);

                    holder.optionCard.setBackgroundColor(Color.WHITE);
//                    holder.option.setTextColor(Color.BLACK);
//                    String text =  "<span style='color:black;'>"
//                            + optionArrayList.get(position).getOption()
//                            + "</span>";
//
//                    holder.option.loadData(text, "text/html", "utf-8");
                    clicked = 100;
                    ItemObjectiveQuestion itemObjectiveQuestion = (ItemObjectiveQuestion) sectionArrayList.get(0).getQuestionsList().get(currentQuestion);
                    itemObjectiveQuestion.setAnsweredAnswer("");
                    SharedPreferences.Editor test = context.getSharedPreferences(itemObjectiveQuestion.getId(),Context.MODE_PRIVATE).edit();
                    test.putString(itemObjectiveQuestion.getId(),"");
                    test.apply();

                    notifyDataSetChanged();
                }
            }
        });
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
                        .load(optionArrayList.get(position).getOptionImageUrl())
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


    }

    @Override
    public int getItemCount() {
        return optionArrayList != null ? optionArrayList.size() : 0;
    }


}
