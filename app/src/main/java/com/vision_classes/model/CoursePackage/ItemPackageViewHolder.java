package com.vision_classes.model.CoursePackage;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;

public class ItemPackageViewHolder extends RecyclerView.ViewHolder {
    public TextView teacherNameTV,classTitle;
    public ImageView  teacherImage;
    public CardView itemLiveClass;

    public ItemPackageViewHolder(@NonNull View itemView) {
        super(itemView);

        teacherImage = itemView.findViewById(R.id.teacherImg);
        teacherNameTV = itemView.findViewById(R.id.teacherName);
        classTitle = itemView.findViewById(R.id.classTitle);

        itemLiveClass = itemView.findViewById(R.id.itemLiveClass);
    }
}
