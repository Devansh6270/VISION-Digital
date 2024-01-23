package com.vision_digital.model.liveClass.GoingOnLive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;
import com.vision_digital.activities.videoPlayer.YouTubeVideoPlayerActivity;
import com.vision_digital.model.liveClass.Classroom.ItemLiveClassesDateWise;
import com.vision_digital.model.liveClass.Classroom.ItemLiveClassesDateWiseAdapter;

import java.util.List;

public class GoingOnLiveAdapter extends RecyclerView.Adapter<GoingOnLiveAdapter.goingOnLiveViewHolder>{


    List<GoingOnLiveModel> goingOnLiveModelList;
    Context context;

    public GoingOnLiveAdapter(List<GoingOnLiveModel> goingOnLiveModelList, Context context) {
        this.goingOnLiveModelList = goingOnLiveModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public goingOnLiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return  new goingOnLiveViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ongoing_live,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull goingOnLiveViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try {

            Log.e("TAG","IN ITEM DATE WISE Called--------");

            holder.title.setText(goingOnLiveModelList.get(position).getTitle());
            holder.courseName.setText(goingOnLiveModelList.get(position).getCourseTitle());
            holder.liveTime.setText(goingOnLiveModelList.get(position).getLiveTime());

            String youtubeEmbedUrl = goingOnLiveModelList.get(position).getUrl();

            String videoId = extractVideoIdFromEmbedUrl(youtubeEmbedUrl);


            holder.startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("click","click");
                    if (goingOnLiveModelList.get(position).liveCurrentStatus.equals("false")){
                        Toast.makeText(context, "Please Subscribe the Course!", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent i = new Intent(context, YouTubeVideoPlayerActivity.class);
                        i.putExtra("videoId",videoId);
                        i.putExtra("videoTitle",goingOnLiveModelList.get(position).getTitle());
                        i.putExtra("videoDescription",goingOnLiveModelList.get(position).getDescription());
                        context.startActivity(i);
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String extractVideoIdFromEmbedUrl(String embedUrl) {
        String videoId = null;
        if (embedUrl != null && !embedUrl.isEmpty()) {
            int index = embedUrl.lastIndexOf('/');
            if (index != -1) {
                videoId = embedUrl.substring(index + 1);
            }
        }
        return videoId;
    }

    @Override
    public int getItemCount() {
        return goingOnLiveModelList.size();
    }

    public class goingOnLiveViewHolder extends RecyclerView.ViewHolder{

        TextView  title , courseName , liveTime , startBtn;

        CardView  cardView;

        public goingOnLiveViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            courseName=itemView.findViewById(R.id.courseName);
            liveTime=itemView.findViewById(R.id.time);
            startBtn=itemView.findViewById(R.id.startBtn);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
