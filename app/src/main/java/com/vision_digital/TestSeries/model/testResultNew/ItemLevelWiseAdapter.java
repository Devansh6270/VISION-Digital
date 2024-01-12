package com.vision_digital.TestSeries.model.testResultNew;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

import java.util.ArrayList;

public class ItemLevelWiseAdapter extends RecyclerView.Adapter<ItemLevelWiseAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<ItemLevelwiseModel> itemLevelwiseModelArrayList;
//    ArrayList<ItemLevelwiseModel> itemMedLevelwiseModelArrayList;
//    ArrayList<ItemLevelwiseModel> itemLevelwiseModelArrayList;



    public ItemLevelWiseAdapter(ArrayList<ItemLevelwiseModel> itemLevelwiseModelArrayList) {
        this.itemLevelwiseModelArrayList = itemLevelwiseModelArrayList;
//        this.itemMedLevelwiseModelArrayList = itemMedLevelwiseModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_levelwise_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.question_no.setText(itemLevelwiseModelArrayList.get(position).sno);
        String status = itemLevelwiseModelArrayList.get(position).getQuesStatus();

        Log.e("quesId",itemLevelwiseModelArrayList.get(position).getQuesStatus());

//        holder.question_no.setText(itemMedLevelwiseModelArrayList.get(position).sno);
//        String status1 = itemMedLevelwiseModelArrayList.get(position).getQuesStatus();
//        Log.e("quesId",itemMedLevelwiseModelArrayList.get(position).getQuesStatus());

//        if (status.equals("correct")){
//        }else{
//            holder.lin_layout.setBackgroundColor(Color.parseColor("#ffcccb"));
//        }

        if (status.equals("correct")){
        }else if (status.equals("skip")){
            holder.lin_layout.setBackgroundColor(Color.parseColor("#D3D3D3"));
        } else {
            holder.lin_layout.setBackgroundColor(Color.parseColor("#ffcccb"));
        }


    }

    @Override
    public int getItemCount() {
        return itemLevelwiseModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lin_layout;
        TextView question_no;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lin_layout = itemView.findViewById(R.id.lin_layout);
            question_no = itemView.findViewById(R.id.question_no);



        }
    }
}
