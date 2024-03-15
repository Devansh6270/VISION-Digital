package com.vision_classes.transaction.detailsModel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.vision_classes.activities.CourseDetailsActivity;
import com.vision_classes.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemDetailsAdapter extends RecyclerView.Adapter<ItemDetailsViewHolder> {
    Context context;
    List<ItemDetails> itemDetailsList;

    public ItemDetailsAdapter(List<ItemDetails> itemDetailsList) {
        this.itemDetailsList = itemDetailsList;
    }

    @NonNull
    @Override
    public ItemDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemDetailsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_details_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemDetailsViewHolder holder, int position) {
        Date renewedDate, subsDate, accDate;
        String renewedDateString = itemDetailsList.get(position).getLastRenewedDate();
        String subsDateString = itemDetailsList.get(position).getSubscriptionDate();
        String accDateString = itemDetailsList.get(position).getAccessibleDate();
        String courseIdSt = itemDetailsList.get(position).getCourseId();

        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd MMM yyyy");


        try {
            renewedDate = inputFormatter.parse(renewedDateString);
            String renewedTime = outputFormatter.format(renewedDate);
            holder.renewed_date.setText(renewedTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            subsDate = inputFormatter.parse(subsDateString);
            String subsTime = outputFormatter.format(subsDate);
            holder.subs_date.setText(subsTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            accDate = inputFormatter.parse(accDateString);
            String accTime = outputFormatter.format(accDate);
            holder.acc_date.setText(accTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.course_name.setText(itemDetailsList.get(position).getTitle());

        if (itemDetailsList.get(position).getSubscribedMonth().equals("1")) {
            holder.subs_month.setText(itemDetailsList.get(position).getSubscribedMonth() + " month");

        } else {
            holder.subs_month.setText(itemDetailsList.get(position).getSubscribedMonth() + " months");

        }
        Glide.with(context)
                .load(itemDetailsList.get(position).getImageUrl())
                .into(holder.course_image);

        holder.cardDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent td = new Intent(context, CourseDetailsActivity.class);
                td.putExtra("id",courseIdSt);
                context.startActivity(td);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemDetailsList != null ? itemDetailsList.size() : 0;
    }
}
