package com.vision_classes.coupons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;

import java.util.List;

public class ItemCouponAdapter extends RecyclerView.Adapter<ItemCouponViewHolder> {
    Context context;
    List<ItemCoupon> couponList;

    public ItemCouponAdapter(List<ItemCoupon> couponList) {
        this.couponList = couponList;
    }

    @NonNull
    @Override
    public ItemCouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemCouponViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coupon_list_layout, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull final ItemCouponViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (couponList.get(position).getCouponCode().equals("Other")){
            holder.code.setText(couponList.get(position).getCouponCode());
        }else {
            holder.code.setText("Coupon Code: "+couponList.get(position).getCouponCode());
        }
        holder.couponTitle.setText(couponList.get(position).getCouponTitle());
        holder.descCoupon.setText(couponList.get(position).getDescription());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (couponList.get(position).getCouponCode().equals("Other")){
//                    coupons_available_spinner.setVisibility(View.GONE);
//                    couponCode.setVisibility(View.GONE);
//                    coupon_code = couponCodeEdt.getText().toString();
//                    couponCodeEdt.setVisibility(View.VISIBLE);
//
//
//                }else{
//                    couponCode.setText(couponList.get(position).getCouponCode());
//                    coupon_code = couponCode.getText().toString();
//                    Log.e("cc",coupon_code);
//                    couponCodeEdt.setVisibility(View.GONE);
//                    coupons_available_spinner.setVisibility(View.GONE);
//                }
//
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return couponList != null ? couponList.size() : 0;
    }
}
