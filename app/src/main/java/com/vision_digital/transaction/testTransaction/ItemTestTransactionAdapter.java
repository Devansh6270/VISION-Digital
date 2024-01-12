package com.vision_digital.transaction.testTransaction;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.TestSeries.TestDetailsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemTestTransactionAdapter extends RecyclerView.Adapter<ItemTestTransactionViewHolder> {
    Context context;
    List<ItemTestTransaction> testTransactionList;

    public ItemTestTransactionAdapter(List<ItemTestTransaction> testTransactionList) {
        this.testTransactionList = testTransactionList;
    }

    @NonNull
    @Override
    public ItemTestTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemTestTransactionViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test_transaction_layout, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull ItemTestTransactionViewHolder holder, int position) {
        Date startAtDate, subsDate;
        String startDateString = testTransactionList.get(position).getStartAt();
        String testSubsStringDate = testTransactionList.get(position).getTestSubsDate();
        String testIdSt = testTransactionList.get(position).getTestId();

        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa");
//
        try {
            startAtDate = inputFormatter.parse(startDateString);
            String  testTime = outputFormatter.format(startAtDate);
            holder.test_start_date.setText(testTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleDateFormat inputFormatterSubs = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat outputFormatterSubs= new SimpleDateFormat("dd MMM yyyy");
        try {
            subsDate = inputFormatterSubs.parse(testSubsStringDate);
            String  testSubsTime = outputFormatterSubs.format(subsDate);
            holder.test_subs_date.setText(testSubsTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.test_name.setText(testTransactionList.get(position).getTitle());
        holder.test_status.setText(testTransactionList.get(position).getStatus());
        Glide.with(context)
                .load(testTransactionList.get(position).getImage())
                .into(holder.test_image);

        holder.test_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent td = new Intent(context, TestDetailsActivity.class);
                td.putExtra("id",testIdSt);
                context.startActivity(td);
            }
        });
    }

    @Override
    public int getItemCount() {
        return testTransactionList != null ? testTransactionList.size() : 0;
    }
}
