package com.vision_digital.model.liveClass;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class ItemLiveClassViewHolder extends RecyclerView.ViewHolder {

    public TextView teacherNameTV,classTitle,teacherQualification,startedAt;
    public ImageView blueCard, teacherImage;
    public RelativeLayout itemLiveClass;

    public ItemLiveClassViewHolder(@NonNull View itemView) {
        super(itemView);

        teacherImage = itemView.findViewById(R.id.teacherImg);
        teacherNameTV = itemView.findViewById(R.id.teacherName);
        classTitle = itemView.findViewById(R.id.classTitle);
        teacherQualification = itemView.findViewById(R.id.techQuali);
        startedAt = itemView.findViewById(R.id.startedAt);

        itemLiveClass = itemView.findViewById(R.id.itemLiveClass);

    }
}
