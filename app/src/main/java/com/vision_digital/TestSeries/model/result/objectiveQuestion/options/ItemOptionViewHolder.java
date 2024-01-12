package com.vision_digital.TestSeries.model.result.objectiveQuestion.options;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class ItemOptionViewHolder extends RecyclerView.ViewHolder {

    TextView option;
    ImageView optionImage,zoomImage;
    CardView optionCard;
    public ItemOptionViewHolder(@NonNull View itemView) {
        super(itemView);

        option = itemView.findViewById(R.id.option);
        optionImage = itemView.findViewById(R.id.optionImage);
        optionCard = itemView.findViewById(R.id.optionCard);
        zoomImage = itemView.findViewById(R.id.zoomImage);
    }
}
