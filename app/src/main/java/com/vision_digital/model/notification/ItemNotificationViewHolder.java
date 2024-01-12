package com.vision_digital.model.notification;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class ItemNotificationViewHolder extends RecyclerView.ViewHolder {

    public TextView notifTitle,notifDesc;
    public ImageView clearBtn, notifImg;

    public ItemNotificationViewHolder(@NonNull View itemView) {
        super(itemView);

        notifDesc = itemView.findViewById(R.id.notifDesc);
        notifTitle = itemView.findViewById(R.id.notifTitle);
        clearBtn = itemView.findViewById(R.id.clearBtn);
        notifImg = itemView.findViewById(R.id.notifImg);


    }
}
