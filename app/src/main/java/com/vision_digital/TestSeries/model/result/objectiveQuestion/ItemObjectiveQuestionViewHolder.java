package com.vision_digital.TestSeries.model.result.objectiveQuestion;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class ItemObjectiveQuestionViewHolder extends RecyclerView.ViewHolder {
    TextView questionNumberTitle;
    CardView questionCard;
    public ItemObjectiveQuestionViewHolder(@NonNull View itemView) {
        super(itemView);
        questionNumberTitle = itemView.findViewById(R.id.questionNumberTitle);
        questionCard = itemView.findViewById(R.id.questionBox);
    }
}
