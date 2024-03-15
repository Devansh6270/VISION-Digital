package com.vision_classes.model.liveClass.Classroom;


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
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;
import com.vision_classes.activities.videoPlayer.YouTubeVideoPlayerActivity;

import java.util.List;

public class ItemLiveClassesDateWiseAdapter  extends  RecyclerView.Adapter<ItemLiveClassesDateWiseAdapter.LiveClassesDateWiseViewHolder> {

    List<ItemLiveClassesDateWise> liveClassDateWiseList;
    Context context;

    public ItemLiveClassesDateWiseAdapter(List<ItemLiveClassesDateWise> liveClassDateWiseList, Context context) {
        this.liveClassDateWiseList = liveClassDateWiseList;
        this.context = context;
    }

    @NonNull
    @Override
    public LiveClassesDateWiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LiveClassesDateWiseViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_play_live_class, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LiveClassesDateWiseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {

            Log.e("TAG","IN ITEM DATE WISE Called--------");

            holder.title.setText(liveClassDateWiseList.get(position).getTitle());
            holder.description.setText(liveClassDateWiseList.get(position).getDescription());
            holder.shortDescription.setText(liveClassDateWiseList.get(position).getShortDesc());
            holder.startTime.setText(liveClassDateWiseList.get(position).getLiveTime());

            String youtubeEmbedUrl = liveClassDateWiseList.get(position).getVideoUrl();

            String videoId = extractVideoIdFromEmbedUrl(youtubeEmbedUrl);


            holder.startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("click","click");
                    if (liveClassDateWiseList.get(position).isLocked){
                        Toast.makeText(context, "Please Subscribe the Course!", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent i = new Intent(context, YouTubeVideoPlayerActivity.class);
                        i.putExtra("videoId",videoId);
                        i.putExtra("videoTitle",liveClassDateWiseList.get(position).getTitle());
                        i.putExtra("videoDescription",liveClassDateWiseList.get(position).getDescription());
                        context.startActivity(i);
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return  liveClassDateWiseList != null ? liveClassDateWiseList.size() : 0;
    }

    // Function to extract the video ID from a YouTube embed URL
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


    public class LiveClassesDateWiseViewHolder extends RecyclerView.ViewHolder{

        TextView  startTime , startBtn,shortDescription,description,title;

        public LiveClassesDateWiseViewHolder(@NonNull View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.timeLiveClass);
            startBtn=itemView.findViewById(R.id.startLiveClass);
            title=itemView.findViewById(R.id.liveClassTitle);
            description=itemView.findViewById(R.id.liveClassDescription);
            shortDescription=itemView.findViewById(R.id.liveClassShortDescription);

        }
    }
}
