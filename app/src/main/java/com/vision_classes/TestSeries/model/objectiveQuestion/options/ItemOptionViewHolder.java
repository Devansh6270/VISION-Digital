package com.vision_classes.TestSeries.model.objectiveQuestion.options;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;

public class ItemOptionViewHolder extends RecyclerView.ViewHolder {

    TextView option, tvOptioNo;
    ImageView optionImage, zoomImage;
//    CardView optionCard;
    LinearLayout optionCard, llOption;
    public ItemOptionViewHolder(@NonNull View itemView) {
        super(itemView);

        option = itemView.findViewById(R.id.option);
        optionImage = itemView.findViewById(R.id.optionImage);
        optionCard = itemView.findViewById(R.id.optionCard);
        zoomImage = itemView.findViewById(R.id.zoomImage);
        llOption = itemView.findViewById(R.id.llOption);
        tvOptioNo = itemView.findViewById(R.id.tvOptionNo);
    }
}
