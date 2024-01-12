package com.vision_digital.model.milestone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.activities.CourseDetailsActivity;
import com.vision_digital.activities.MileStoneVideoPlayerActivity;
import com.vision_digital.activities.PdfRenderActivity;
import com.vision_digital.R;

import java.util.List;

import static com.vision_digital.activities.CourseDetailsActivity.chapter_id;
import static com.vision_digital.activities.CourseDetailsActivity.courseId;
import static com.vision_digital.activities.CourseDetailsActivity.subscriptionStatus;

public class ItemMileStoneAdapter extends RecyclerView.Adapter<ItemMileStoneViewHolder> {

    List<ItemMileStone> milestoneList;
    Context context;
    int clicked = 1000000000;
    SharedPreferences.Editor courseEditor;


    //for selectmilestone activity
    public static String selectedMilestoneID = "";
    public static String selectedMilestoneName = "";
    public static String videoPos = "";
    public static boolean isHaveMilestoneId = false;


    public ItemMileStoneAdapter(List<ItemMileStone> chapterList) {
        this.milestoneList = chapterList;
    }

    @NonNull
    @Override
    public ItemMileStoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemMileStoneViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_milestone, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemMileStoneViewHolder holder, @SuppressLint("RecyclerView") final int position) {
//        String video = milestoneList.get(position).getVideoUrl();
//        final String lastThree = video.substring(video.length() - 3, video.length());
        holder.milestone_select.setVisibility(View.GONE);

        if (milestoneList.get(position).isSelected) {
            holder.milestone_select.setVisibility(View.VISIBLE);

        } else {
            holder.milestone_select.setVisibility(View.GONE);

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (milestoneList.get(position).getActivityType().equals("courseDetails")) {
                    holder.milestone_select.setVisibility(View.GONE);

                    if (milestoneList.get(position).getVideoUrl().equals("not_available")) {
                        Log.e("positionOfvideo", "" + milestoneList.get(position).getVideoPosition());

                        Toast.makeText(context, "Subscribe to watch", Toast.LENGTH_SHORT).show();
                    } else if (milestoneList.get(position).getVideoUrl().equals("")) {
                        Log.e("positionOfvideo", "" + milestoneList.get(position).getVideoPosition());

                        Toast.makeText(context, "Subscribe to watch", Toast.LENGTH_SHORT).show();

                    } else {
//                    Toast.makeText(context, "Subscribe to watch", Toast.LENGTH_SHORT).show();
                        if (milestoneList.get(position).getDuration().equals("0")) {
                            Intent mileStonePlayer = new Intent(context, PdfRenderActivity.class);
                            mileStonePlayer.putExtra("pdfLink", milestoneList.get(position).getVideoUrl());


                            context.startActivity(mileStonePlayer);
                        } else {
                            Intent mileStonePlayer = new Intent(context, MileStoneVideoPlayerActivity.class);
                            mileStonePlayer.putExtra("videoPosition", milestoneList.get(position).getVideoPosition());

                            Log.e("positionOfvideo", "" + milestoneList.get(position).getVideoPosition());
                            mileStonePlayer.putExtra("mileStonesList", CourseDetailsActivity.mileStonesArrayList);
                            mileStonePlayer.putExtra("chapter_id", chapter_id);
                            mileStonePlayer.putExtra("id", courseId);
                            mileStonePlayer.putExtra("name", CourseDetailsActivity.courseName);
                            mileStonePlayer.putExtra("logo", CourseDetailsActivity.courseLogo);
                            mileStonePlayer.putExtra("subscriptionStatus",subscriptionStatus);
                            mileStonePlayer.putExtra("mileUpdateType","chap_mile_click");
                            Log.e("courseIdMilestone",courseId);
                            Log.e("logoItem",CourseDetailsActivity.courseLogo);
                            context.startActivity(mileStonePlayer);
                        }

                    }
                } else {
                    if (clicked != position) {
                        for (int i = 0; i < milestoneList.size(); i++) {
                            if (i == position) {
                                milestoneList.get(i).setSelected(true);
                                clicked = position;


                                selectedMilestoneName = milestoneList.get(i).getTitle();
                                selectedMilestoneID = milestoneList.get(i).getId();
                                videoPos = String.valueOf(milestoneList.get(i).getVideoPosition());
                                isHaveMilestoneId = true;
                                Log.e("isHaveMilestoneId0", "isHaveMilestoneId true");

                                notifyDataSetChanged();
                            } else {
                                milestoneList.get(i).setSelected(false);
//                                isHaveMilestoneId = false;
                                Log.e("isHaveMilestoneId1", "isHaveMilestoneId false1");
                                notifyDataSetChanged();
                            }
                        }

                    } else {
                        milestoneList.get(position).setSelected(false);
                        holder.milestone_select.setVisibility(View.GONE);
                        clicked = 1000000000;
                        isHaveMilestoneId = false;
                        Log.e("isHaveMilestoneId2", "isHaveMilestoneId false1");
                        notifyDataSetChanged();

                    }



                }

            }
        });
        if (milestoneList.get(position).getDuration().equals("0")) {
            holder.play_icon.setVisibility(View.GONE);
            holder.pdf_icon.setVisibility(View.VISIBLE);
            holder.mileStoneDuration.setVisibility(View.GONE);

        } else {
            holder.play_icon.setVisibility(View.VISIBLE);
            holder.pdf_icon.setVisibility(View.GONE);
            holder.mileStoneDuration.setVisibility(View.VISIBLE);
        }


        holder.mileStoneName.setText(milestoneList.get(position).getTitle());
        String mid = milestoneList.get(position).getId();
        try {
            int duration = Integer.parseInt(milestoneList.get(position).getDuration());
            int min = duration / 60;
            int sec = duration % 60;
            holder.mileStoneDuration.setText(min + ":" + sec + " mins");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //selectMileStoneInvitationActivity next Button


    }

    @Override
    public int getItemCount() {
        return milestoneList != null ? milestoneList.size() : 0;
    }
}