package com.vision_digital.TestSeries.model.bundle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.TestSeries.TestDetailsActivity;
import com.vision_digital.TestSeries.model.testResultNew.NewResultActivity;

import java.util.List;

public class ItemTestBundleAdapter extends RecyclerView.Adapter<ItemTestBundleViewHolder> {

    List<ItemTestBundle> myTestBundleList;
    Context context;
    final int MSG_TEST = 0;
    final int MSG_TEST_SERIES = 1;

    public ItemTestBundleAdapter(List<ItemTestBundle> myTestBundleList) {
        this.myTestBundleList = myTestBundleList;
    }

    @NonNull
    @Override
    public ItemTestBundleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        if (viewType == MSG_TEST_SERIES) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.horizontal_test_card, parent, false);
            return new ItemTestBundleViewHolder(view);

        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.verticle_testseries_card, parent, false);
            return new ItemTestBundleViewHolder(view);
        }




    }

    @Override
    public void onBindViewHolder(@NonNull ItemTestBundleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            holder.instituteName.setText(myTestBundleList.get(position).getOwner_name());

            holder.testTitle.setText(myTestBundleList.get(position).getTitle());

            if (myTestBundleList.get(position).getDuration().equals("")){
                if (position % 2 == 0) {

                    holder.verticleCard.setBackgroundResource(R.drawable.live_test_card_blue_background);
                } else {

                    holder.verticleCard.setBackgroundResource(R.drawable.live_test_card_green_background);
                }
                holder.duration.setVisibility(View.GONE);
                holder.startButton.setVisibility(View.GONE);
            }else{
                if (position % 2 == 0) {

                    holder.verticleCard.setBackgroundResource(R.drawable.verticle_testseries_card_background_blue);
                } else {

                    holder.verticleCard.setBackgroundResource(R.drawable.verticle_test_card_backgroung_green);
                }
                holder.duration.setVisibility(View.VISIBLE);
                holder.startButton.setVisibility(View.VISIBLE);
                holder.startButton.setText(myTestBundleList.get(position).getStatus());
                long second = Long.parseLong(myTestBundleList.get(position).getDuration());
                int hours = (int) second / 3600;
                int remainder = (int) second - hours * 3600;
                int mins = remainder / 60;


                if (hours < 10 && mins < 10) {
                    holder.duration.setText("Duration: 0" + hours + ":0" + mins + " Hrs");
                } else if (hours == 0 && mins < 10) {
                    holder.duration.setText("Duration: 00" + ":0" + mins + " Hrs");

                } else if (hours == 0 && mins > 10) {
                    holder.duration.setText("Duration: 00" + ":" + mins + " Hrs");

                } else if (hours > 10 && mins < 10) {
                    holder.duration.setText("Duration: " + hours + ":0" + mins + " Hrs");

                } else if (hours < 10 && mins > 10) {
                    holder.duration.setText("Duration: 0" + hours + ":" + mins + " Hrs");

                } else {

                }


            }

            Glide.with(context).load(myTestBundleList.get(position).getImage()).into(holder.testImg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("click", "click");
                    if (myTestBundleList.get(position).status.equals("See Result")){
                        Intent courseIntent = new Intent(context, NewResultActivity.class);
                        courseIntent.putExtra("id", myTestBundleList.get(position).getId());
                        context.startActivity(courseIntent);
                    }else{
                        Intent courseIntent = new Intent(context, TestDetailsActivity.class);
                        courseIntent.putExtra("desc", myTestBundleList.get(position).getDescription());
                        courseIntent.putExtra("price", myTestBundleList.get(position).getPrice());
                        courseIntent.putExtra("testType", myTestBundleList.get(position).getTestType());
                        courseIntent.putExtra("id", myTestBundleList.get(position).getId());
                        context.startActivity(courseIntent);
                    }

                }
            });




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return myTestBundleList != null ? myTestBundleList.size() : 0;

    }

    @Override
    public int getItemViewType(int position) {
        if (myTestBundleList.get(position).getType().equals("Test")){
            return MSG_TEST;
        }else {
            return MSG_TEST_SERIES;
        }

    }
}
