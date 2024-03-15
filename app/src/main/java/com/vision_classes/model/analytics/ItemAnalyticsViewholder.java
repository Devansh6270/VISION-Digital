package com.vision_classes.model.analytics;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;

public class ItemAnalyticsViewholder extends RecyclerView.ViewHolder {

    TextView courseTitle , courseOwner;
    ImageView image;
    CardView itemAnalytics;
    public ItemAnalyticsViewholder(@NonNull View itemView) {
        super(itemView);
        courseTitle=itemView.findViewById(R.id.classTitle);
        courseOwner=itemView.findViewById(R.id.teacherName);
        image=itemView.findViewById(R.id.courseImg);
        itemAnalytics=itemView.findViewById(R.id.itemAnalytics);


    }


}
