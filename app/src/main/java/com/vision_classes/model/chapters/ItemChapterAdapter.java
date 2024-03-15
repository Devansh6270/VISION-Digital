package com.vision_classes.model.chapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;
import com.vision_classes.model.milestone.ItemMileStoneAdapter;

import java.util.List;

public class ItemChapterAdapter extends RecyclerView.Adapter<ItemChapterViewHolder> {

    List<ItemChapter> chapterList;
    Context context;
    boolean isShow=false;

    public ItemChapterAdapter(List<ItemChapter> chapterList) {
        this.chapterList = chapterList;
    }

    @NonNull
    @Override
    public ItemChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemChapterViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemChapterViewHolder holder, final int position) {

        Log.e("test", chapterList.get(position).getTitle());
        holder.chapterName.setText(chapterList.get(position).getTitle());

        holder.min_month.setText(chapterList.get(position).getMin_month());


        ItemMileStoneAdapter itemMileStoneAdapter;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.mileStoneList.setLayoutManager(layoutManager);
        itemMileStoneAdapter = new ItemMileStoneAdapter(chapterList.get(position).mileStonesList);
        holder.mileStoneList.setAdapter(itemMileStoneAdapter);


        holder.linear_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    holder.min_month.setVisibility(View.GONE);
                    holder.up_arrow.setVisibility(View.GONE);
                    holder.down_arrow.setVisibility(View.VISIBLE);
                    holder.mileStoneList.setVisibility(View.GONE);
                    holder.chapterName.setMaxLines(1);
                    isShow = false;
                } else {
                    holder.min_month.setVisibility(View.VISIBLE);
                    holder.mileStoneList.setVisibility(View.VISIBLE);
                    holder.up_arrow.setVisibility(View.VISIBLE);
                    holder.down_arrow.setVisibility(View.GONE);
                    holder.chapterName.setMaxLines(2);
                    isShow = true;

                }

            }
        });

        if (position == 0) {
            holder.min_month.setVisibility(View.VISIBLE);
            holder.mileStoneList.setVisibility(View.VISIBLE);
            holder.up_arrow.setVisibility(View.VISIBLE);
            holder.down_arrow.setVisibility(View.GONE);
            holder.chapterName.setMaxLines(2);
            isShow = true;
        }


    }

    @Override
    public int getItemCount() {
        return chapterList != null ? chapterList.size() : 0;
    }
}
