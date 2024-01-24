package com.vision_digital.model.PopularCourses;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class PopularCoursesViewHolder extends RecyclerView.ViewHolder {

    public TextView teacherNameTV,classTitle,teacherQualification,startedAt, price;
    public ImageView blueCard, teacherImage;
    public CardView itemLiveClass;

    public PopularCoursesViewHolder(@NonNull View itemView) {
        super(itemView);
        teacherImage = itemView.findViewById(R.id.courseImg);
        teacherNameTV = itemView.findViewById(R.id.teacherName);
        classTitle = itemView.findViewById(R.id.classTitle);
        teacherQualification = itemView.findViewById(R.id.techQuali);
        startedAt = itemView.findViewById(R.id.startedAt);
        price=itemView.findViewById(R.id.price);


        itemLiveClass = itemView.findViewById(R.id.itemPopularLiveClass);

    }
}
