package com.vision_digital.transaction.testTransaction;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class ItemTestTransactionViewHolder extends RecyclerView.ViewHolder {
    TextView test_name, test_subs_date, test_start_date, test_status;
    ImageView test_image;
    CardView test_card;


    public ItemTestTransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        test_name= itemView.findViewById(R.id.test_name);
        test_subs_date= itemView.findViewById(R.id.test_subs_date);
        test_start_date= itemView.findViewById(R.id.start_at_date);
        test_status= itemView.findViewById(R.id.test_status);
        test_image= itemView.findViewById(R.id.test_image);
        test_card= itemView.findViewById(R.id.test_card);



    }
}
