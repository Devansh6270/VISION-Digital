package com.vision_classes.model.otherSubject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;

import java.util.ArrayList;

public class OtherSubjectAdapter extends RecyclerView.Adapter<OtherSubjectAdapter.ViewHolder>{
    Context context;
    ArrayList<OtherSubjectModel> arrayListSubject;

    public OtherSubjectAdapter(Context context, ArrayList<OtherSubjectModel> arrayListSubject) {
        this.context = context;
        this.arrayListSubject = arrayListSubject;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_other_subject,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.subjectTopic.setText(arrayListSubject.get(position).getHospitalityTxt());

    }

    @Override
    public int getItemCount() {
        return arrayListSubject.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView subjectTopic , topicSize;
        ImageView subjectImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectImage=itemView.findViewById(R.id.hospitalityImg);
            subjectTopic=itemView.findViewById(R.id.hospitalityTxt);
            topicSize=itemView.findViewById(R.id.hospitalityTxtDesc);


        }
    }
}
