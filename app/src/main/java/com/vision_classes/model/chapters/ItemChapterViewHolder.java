package com.vision_classes.model.chapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;

class ItemChapterViewHolder extends RecyclerView.ViewHolder {

    TextView chapterName, min_month;
    RecyclerView mileStoneList;
    ImageView up_arrow, down_arrow;
    LinearLayout linear_drop;

    ItemChapterViewHolder(@NonNull View itemView) {
        super(itemView);
        chapterName = itemView.findViewById(R.id.chapterName);
        mileStoneList = itemView.findViewById(R.id.mileStoneList);
        min_month = itemView.findViewById(R.id.min_month);
        up_arrow = itemView.findViewById(R.id.up_arrow);
        down_arrow = itemView.findViewById(R.id.down_arrow);
        linear_drop = itemView.findViewById(R.id.linear_drop);

    }
}
