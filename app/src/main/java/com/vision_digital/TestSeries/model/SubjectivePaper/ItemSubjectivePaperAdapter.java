package com.vision_digital.TestSeries.model.SubjectivePaper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;
import com.vision_digital.TestSeries.SubjectivePaperDetailActivity;

import java.util.ArrayList;

public class ItemSubjectivePaperAdapter extends RecyclerView.Adapter<ItemSubjectivePaperAdapter.ViewHolder> {

    Context context;
    ArrayList<ItemSubjectivePaperList> itemSubjectivePaperLists;
    String testSeriesId;
    public ItemSubjectivePaperAdapter(Context context, ArrayList<ItemSubjectivePaperList> itemSubjectivePaperLists, String testSeriesId){
        this.context = context;
        this.itemSubjectivePaperLists = itemSubjectivePaperLists;
        this.testSeriesId = testSeriesId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_subjective_paper, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String testId = itemSubjectivePaperLists.get(position).getSubjectiveId();
      holder.tvTestName.setText(itemSubjectivePaperLists.get(position).getSubjectiveTitle());
      holder.ivImageLock.setImageResource(R.drawable.ic_lock_red);

        String buyStatus = itemSubjectivePaperLists.get(position).getBuy_status();
        String buyButton = itemSubjectivePaperLists.get(position).getBuy_button();

        if (itemSubjectivePaperLists.get(position).getLock().equals("1") && itemSubjectivePaperLists.get(position).buy_status.equals("0")) {
            holder.ivImageUnLock.setVisibility(View.GONE);
            holder.ivImageLock.setVisibility(View.VISIBLE);
        } else if (itemSubjectivePaperLists.get(position).getLock().equals("1") && itemSubjectivePaperLists.get(position).buy_status.equals("1")) {
            holder.ivImageLock.setVisibility(View.GONE);
            holder.ivImageUnLock.setVisibility(View.VISIBLE);
        } else if (itemSubjectivePaperLists.get(position).getLock().equals("1") && itemSubjectivePaperLists.get(position).buy_status.equals("2")) {
            holder.ivImageLock.setVisibility(View.GONE);
            holder.ivImageUnLock.setVisibility(View.VISIBLE);
        } else if (itemSubjectivePaperLists.get(position).getLock().equals("0")) {
            holder.ivImageLock.setVisibility(View.GONE);
            holder.ivImageUnLock.setVisibility(View.VISIBLE);
        }

      holder.ivImageUnLock.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(context, SubjectivePaperDetailActivity.class);
              intent.putExtra("testId",testId);
              intent.putExtra("testSeriesId", testSeriesId);
              context.startActivity(intent);
          }
      });

    }

    @Override
    public int getItemCount() {
        return itemSubjectivePaperLists.size() != 0 ? itemSubjectivePaperLists.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImageLock, ivImageUnLock;
        TextView tvTestName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImageLock = itemView.findViewById(R.id.ivImageLock);
            tvTestName = itemView.findViewById(R.id.tvTestName);
            ivImageUnLock = itemView.findViewById(R.id.ivImageUnLock);
        }
    }
}
