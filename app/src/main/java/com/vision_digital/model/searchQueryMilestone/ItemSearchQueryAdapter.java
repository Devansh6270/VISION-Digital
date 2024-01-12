package com.vision_digital.model.searchQueryMilestone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.vision_digital.R;
import com.vision_digital.activities.MileStoneVideoPlayerActivity;
import com.vision_digital.model.milestone.ItemMileStone;

import java.util.ArrayList;
import java.util.List;

public class ItemSearchQueryAdapter extends RecyclerView.Adapter<ItemSearchQueryAdapter.ViewHolder> {

    List<ItemMileStone> itemSearchQueryList;
    Context context;

    public ItemSearchQueryAdapter(List<ItemMileStone> itemSearchQueryList) {
        this.itemSearchQueryList = itemSearchQueryList;
    }

    @NonNull
    @Override
    public ItemSearchQueryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_query_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSearchQueryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.mileStoneName.setText(itemSearchQueryList.get(position).getTitle());


        try {
            int duration = Integer.parseInt(itemSearchQueryList.get(position).getDuration());
            int min = duration / 60;
            int sec = duration % 60;
            holder.mileStoneDuration.setText(min + ":" + sec + " mins");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Glide.with(context)
                .load(Uri.parse(itemSearchQueryList.get(position).getCourseLogo()))
                .into(holder.searchResultImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemSearchQueryList.get(position).getActivityType().equals("adda")) {

                } else {
                    Intent mileStonePlayer = new Intent(context, MileStoneVideoPlayerActivity.class);
                    mileStonePlayer.putExtra("videoPosition", 0);
                    mileStonePlayer.putExtra("seekTo", 0);
                    mileStonePlayer.putExtra("search_id", itemSearchQueryList.get(position).getSearchId());
                    mileStonePlayer.putExtra("id", itemSearchQueryList.get(position).getCourseId());
                    mileStonePlayer.putExtra("from_chapter_id", MileStoneVideoPlayerActivity.chapter_id_from);
                    mileStonePlayer.putExtra("from_course_id", MileStoneVideoPlayerActivity.courseIdM);
                    mileStonePlayer.putExtra("fromMile", true);
                    mileStonePlayer.putExtra("chapter_id",itemSearchQueryList.get(position).getChapterIdString());
                    mileStonePlayer.putExtra("course_id",itemSearchQueryList.get(position).getCourseId());
                    mileStonePlayer.putExtra("from_milestone", MileStoneVideoPlayerActivity.milestone_id);
                    mileStonePlayer.putExtra("to_milestone",itemSearchQueryList.get(position).getSearchMileId());
                    mileStonePlayer.putExtra("search_Keyword", itemSearchQueryList.get(position).getSearchKeyword());
                    mileStonePlayer.putExtra("subscriptionStatus", "subscribed");
                    mileStonePlayer.putExtra("name", "No Name");
                    mileStonePlayer.putExtra("logo", "No Name");
                    mileStonePlayer.putExtra("mileUpdateType","search_mile_click");
                    ArrayList<ItemMileStone> singleMileStoneList = new ArrayList<>();
                    singleMileStoneList.add(itemSearchQueryList.get(position));
                    Log.d("TAG", "onClick: singlemilestone"+singleMileStoneList);
                    mileStonePlayer.putExtra("mileStonesList", singleMileStoneList);
                    context.startActivity(mileStonePlayer);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemSearchQueryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mileStoneName;
        TextView mileStoneDuration;

        ImageView searchResultImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mileStoneName = itemView.findViewById(R.id.searchTitle);
            mileStoneDuration = itemView.findViewById(R.id.duration_text);
            searchResultImage = itemView.findViewById(R.id.searchResultImage);


        }
    }
}
