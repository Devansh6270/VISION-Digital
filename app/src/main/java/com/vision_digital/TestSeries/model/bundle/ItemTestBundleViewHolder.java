package com.vision_digital.TestSeries.model.bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class ItemTestBundleViewHolder extends RecyclerView.ViewHolder {
    public TextView instituteName,testTitle,duration, startButton;
    public ImageView  testImg;
    public RelativeLayout itemLiveClass;
    public LinearLayout verticleCard;

    public ItemTestBundleViewHolder(@NonNull View itemView) {
        super(itemView);

        testImg = itemView.findViewById(R.id.testImg);
        instituteName = itemView.findViewById(R.id.instituteName);
        testTitle = itemView.findViewById(R.id.testTitle);
        duration = itemView.findViewById(R.id.duration);
        itemLiveClass = itemView.findViewById(R.id.itemLiveClass);
        startButton = itemView.findViewById(R.id.startButton);
        verticleCard = itemView.findViewById(R.id.verticleCard);


    }
}
