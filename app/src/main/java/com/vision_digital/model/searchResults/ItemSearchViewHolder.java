package com.vision_digital.model.searchResults;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

class ItemSearchViewHolder extends RecyclerView.ViewHolder {

    TextView title, duration_text, ownedBy, description,title_dots,title_dots_desc;
    ImageView image;
    LinearLayout duration_linear;

    View itemView;

    ItemSearchViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        title = itemView.findViewById(R.id.searchTitle);
        duration_text = itemView.findViewById(R.id.duration_text);
        image = itemView.findViewById(R.id.searchResultImage);
        ownedBy = itemView.findViewById(R.id.ownedBy);
        description = itemView.findViewById(R.id.description);
        duration_linear = itemView.findViewById(R.id.duration_linear);
        title_dots = itemView.findViewById(R.id.title_dots);
        title_dots_desc = itemView.findViewById(R.id.title_dots_desc);
    }
}
