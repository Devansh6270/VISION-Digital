package com.vision_classes.TestSeries.model.section;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;

import java.util.ArrayList;

public class ItemSectionAdapter extends RecyclerView.Adapter<ItemSectionViewHolder> {

    ArrayList<ItemSection> sectionList;
    Context context;

    public ItemSectionAdapter(ArrayList<ItemSection> sectionList) {
        this.sectionList = sectionList;
    }

    @NonNull
    @Override
    public ItemSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemSectionViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test_series_section, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSectionViewHolder holder, int position) {
        holder.sectionTitle.setText(sectionList.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return sectionList != null ? sectionList.size() : 0;

    }
}
