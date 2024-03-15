package com.vision_classes.model.myCourses;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;

public class ItemMyCourseViewHolder extends RecyclerView.ViewHolder {

    public TextView teacherNameTV,classTitle,teacherQualification,startedAt, letsLearnBtn;
    public ImageView blueCard, teacherImage;
    public CardView itemLiveClass;
    public ItemMyCourseViewHolder(@NonNull View itemView) {
        super(itemView);
        teacherImage = itemView.findViewById(R.id.teacherImg);
        teacherNameTV = itemView.findViewById(R.id.teacherName);
        classTitle = itemView.findViewById(R.id.classTitle);
        teacherQualification = itemView.findViewById(R.id.techQuali);
        startedAt = itemView.findViewById(R.id.startedAt);
        letsLearnBtn=itemView.findViewById(R.id.letsLearnBtn);

       itemLiveClass = itemView.findViewById(R.id.itemLiveClass);

    }
}
