package com.vision_classes.model.liveClass.GoingOnLive;

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

import com.airbnb.lottie.LottieAnimationView;
import com.vision_classes.R;
import com.vision_classes.activities.videoPlayer.YouTubeVideoPlayerActivity;

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
            String liveStatus=goingOnLiveModelList.get(position).getLiveCurrentStatus();

            if (liveStatus.equals("true")){
                holder.startBtn.setVisibility(View.VISIBLE);
                holder.upcoming.setVisibility(View.GONE);
            }else {
                holder.startBtn.setVisibility(View.GONE);
                holder.upcoming.setVisibility(View.VISIBLE);
            }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("click","click");
                    if (goingOnLiveModelList.get(position).liveCurrentStatus.equals("false")){
                        Toast.makeText(context, goingOnLiveModelList.get(position).getLiveStatus(), Toast.LENGTH_SHORT).show();
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

        TextView  title , courseName , liveTime , upcoming;
        LottieAnimationView  startBtn;

        CardView  cardView;

        public goingOnLiveViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            courseName=itemView.findViewById(R.id.courseName);
            liveTime=itemView.findViewById(R.id.time);
            startBtn=itemView.findViewById(R.id.startBtn);
            cardView=itemView.findViewById(R.id.cardView);
            upcoming=itemView.findViewById(R.id.upcoming);
        }
    }
}
