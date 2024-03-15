package com.vision_classes.coupons;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;

public class ItemCouponViewHolder extends RecyclerView.ViewHolder {
    TextView code, couponTitle, descCoupon;
    View itemView;


    public ItemCouponViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        code = itemView.findViewById(R.id.code);
        couponTitle = itemView.findViewById(R.id.titleCoupon);
        descCoupon = itemView.findViewById(R.id.descCoupon);
    }
}
