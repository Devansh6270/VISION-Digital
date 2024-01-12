package com.vision_digital.model.suggestedMileStone;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.activities.MileStoneVideoPlayerActivity;
import com.vision_digital.R;
import com.vision_digital.model.milestone.ItemMileStone;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ItemSuggestedMileStoneAdapter extends RecyclerView.Adapter<ItemSuggestedMileStoneViewHolder> {

    List<ItemMileStone> milestoneList;
    Context context;
    private String admin = "";
    private String studentUId = DashboardActivity.uid;
    private String isAdmin = "false";

    public ItemSuggestedMileStoneAdapter(List<ItemMileStone> chapterList) {
        this.milestoneList = chapterList;
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @NonNull
    @Override
    public ItemSuggestedMileStoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemSuggestedMileStoneViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inner_milestone, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSuggestedMileStoneViewHolder holder, @SuppressLint("RecyclerView") final int position) {




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (milestoneList.get(position).getActivityType().equals("adda")) {


                } else {

                    Intent mileStonePlayer = new Intent(context, MileStoneVideoPlayerActivity.class);
                    mileStonePlayer.putExtra("videoPosition", 0);
                    mileStonePlayer.putExtra("seekTo", 0);
                    mileStonePlayer.putExtra("id", MileStoneVideoPlayerActivity.cid);
                    mileStonePlayer.putExtra("fromMile", true);
                    mileStonePlayer.putExtra("subscriptionStatus", "subscribed");
                    mileStonePlayer.putExtra("name", MileStoneVideoPlayerActivity.courseNameM);
                    mileStonePlayer.putExtra("logo", MileStoneVideoPlayerActivity.courseLogoM);
                    mileStonePlayer.putExtra("mileUpdateType","suggested_milestone");
                    ArrayList<ItemMileStone> singleMileStoneList = new ArrayList<>();
                    singleMileStoneList.add(milestoneList.get(position));
                    mileStonePlayer.putExtra("mileStonesList", singleMileStoneList);
                    context.startActivity(mileStonePlayer);
                }

            }
        });





        holder.mileStoneName.setText(milestoneList.get(position).getTitle());
        try {
            int duration = Integer.parseInt(milestoneList.get(position).getDuration());
            int min = duration / 60;
            int sec = duration % 60;
            holder.mileStoneDuration.setText(min + ":" + sec + " mins");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return milestoneList != null ? milestoneList.size() : 0;
    }
}