package com.vision_digital.model.milestone;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

class ItemMileStoneViewHolder extends RecyclerView.ViewHolder {

    TextView mileStoneName, mileStoneDuration;
    View itemView;
    ImageView play_icon, pdf_icon, milestone_select;


    ItemMileStoneViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        mileStoneName = itemView.findViewById(R.id.mileStoneName);
        mileStoneDuration = itemView.findViewById(R.id.mileStoneDuration);
        play_icon = itemView.findViewById(R.id.play_icon);
        pdf_icon = itemView.findViewById(R.id.pdfs_icon);
        milestone_select = itemView.findViewById(R.id.milestone_select);

    }
}
