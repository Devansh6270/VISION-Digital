package com.vision_digital.TestSeries.model.section;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class ItemSectionViewHolder extends RecyclerView.ViewHolder {

    public TextView sectionTitle;

    public ItemSectionViewHolder(@NonNull View itemView) {
        super(itemView);

        sectionTitle = itemView.findViewById(R.id.sectionTitle);

    }
}
