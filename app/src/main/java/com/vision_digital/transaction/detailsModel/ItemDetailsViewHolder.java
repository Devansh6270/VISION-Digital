package com.vision_digital.transaction.detailsModel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class ItemDetailsViewHolder extends RecyclerView.ViewHolder {
    TextView course_name, subs_date, acc_date, renewed_date, subs_month;
    ImageView course_image;
    CardView cardDetails;

    public ItemDetailsViewHolder(@NonNull View itemView) {
        super(itemView);
        course_name =itemView.findViewById(R.id.course_name);
        subs_month = itemView.findViewById(R.id.profile_sex);
        subs_date = itemView.findViewById(R.id.profile_email);
        acc_date = itemView.findViewById(R.id.profile_mobile);
        renewed_date = itemView.findViewById(R.id.profile_dob);
        course_image = itemView.findViewById(R.id.course_image);
        cardDetails = itemView.findViewById(R.id.cardDetails);



    }
}
