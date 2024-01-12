package com.vision_digital.model.popularTeachers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class ItemTeacherViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView name;
    TextView instituteName;
    TextView subject;
    TextView subscriber;


    public ItemTeacherViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.teacherImg);
        name = itemView.findViewById(R.id.teacherName);
        instituteName = itemView.findViewById(R.id.institueName);
        subject = itemView.findViewById(R.id.subjectName);
        subscriber = itemView.findViewById(R.id.subscriber);

    }
}
