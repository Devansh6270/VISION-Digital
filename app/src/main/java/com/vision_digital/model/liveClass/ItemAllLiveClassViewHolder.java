package com.vision_digital.model.liveClass;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;


public class ItemAllLiveClassViewHolder extends RecyclerView.ViewHolder {

    public TextView courseTitle , courseStartEndDate;
    public ImageView courseImage;
    public CardView courseCard;

    public ItemAllLiveClassViewHolder(@NonNull View itemView) {
        super(itemView);

        courseStartEndDate = itemView.findViewById(R.id.startDateEndDateText);
        courseTitle = itemView.findViewById(R.id.liveCourseTitle);
        courseCard = itemView.findViewById(R.id.courseCard);

        courseImage =itemView.findViewById(R.id.liveCourseImage);

    }
}
