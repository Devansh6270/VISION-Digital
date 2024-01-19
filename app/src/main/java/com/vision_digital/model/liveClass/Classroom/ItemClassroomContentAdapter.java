package com.vision_digital.model.liveClass.Classroom;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.activities.videoPlayer.YouTubeVideoPlayerActivity;

import java.util.List;

public class ItemClassroomContentAdapter extends RecyclerView.Adapter<ItemClassroomContentAdapter.ItemClassroomContentViewHolder> {
    Context context;
    String imageUrl;
    List<ItemClassroomContentModel> itemClassroomContentModels;

    public ItemClassroomContentAdapter(Context context, List<ItemClassroomContentModel> itemClassroomContentModels) {
        this.context = context;
        this.itemClassroomContentModels = itemClassroomContentModels;
    }

    @NonNull
    @Override
    public ItemClassroomContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_classroom_content, parent, false);

        return new ItemClassroomContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemClassroomContentViewHolder holder, int position) {

        try {

            String youtubeEmbedUrl = itemClassroomContentModels.get(position).getVideo_url();
            String videoId = extractVideoIdFromEmbedUrl(youtubeEmbedUrl);

            holder.describeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle the click event for the forwordBtn here
                    // You can start a new activity or perform any other action
                    Log.e("click", "Classroom Content to YouTube Player clicked");
//
                      if (itemClassroomContentModels.get(position).isIs_locked()){
                          Toast.makeText(context, "Please Subscribe the course", Toast.LENGTH_SHORT).show();
                     }else {
                          Intent i = new Intent(context, YouTubeVideoPlayerActivity.class);
                          i.putExtra("videoId",videoId);
                          i.putExtra("videoTitle",itemClassroomContentModels.get(position).getTitle());
                          i.putExtra("videoDescription",itemClassroomContentModels.get(position).getDescription());
                          context.startActivity(i);
                     }




                }
            });

            holder.chapterTitle.setText(itemClassroomContentModels.get(position).getTitle());

            holder.chapterDescription.setText(itemClassroomContentModels.get(position).getShort_desc());
            holder.date.setText(itemClassroomContentModels.get(position).getLive_date());



            if (youtubeEmbedUrl.equals("") || youtubeEmbedUrl.equals(null) || youtubeEmbedUrl.isEmpty()){
                holder.chapterImage.setImageResource(R.drawable.img_banner_books);
            }else {

                imageUrl=convertToThumbnailUrl(youtubeEmbedUrl);

                Glide.with(context).load(imageUrl).into(holder.chapterImage);

            }







            holder.date.setVisibility(View.VISIBLE);

//            Toast.makeText(context, "ADAPTER IS WORKING", Toast.LENGTH_SHORT).show();
//            holder.describeLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Handle the click event for the forwordBtn here
//                    // You can start a new activity or perform any other action
//                    Log.e("click", "Classroom Content to YouTube Player clicked");
////                    Boolean bool = false;
//
////                    if (itemClassroomContentModels.get(position).is_locked().equals(bool)){
////                      if (itemClassroomContentModels.get(position).isIs_locked() == false){
//                        Intent i = new Intent(context, YouTubeVideoPlayerActivity.class);
//                        i.putExtra("videoId",videoId);
//                        i.putExtra("videoTitle",itemClassroomContentModels.get(position).getTitle());
//                        i.putExtra("videoDescription",itemClassroomContentModels.get(position).getDescription());
//                        context.startActivity(i);
////                    }else {
////                        Toast.makeText(context, "Please Subscribe the course", Toast.LENGTH_SHORT).show();
////                    }
//
//
//
//
//                }
//            });


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public  String convertToThumbnailUrl(String videoUrl) {
        String videoId = extractVideoIdFromEmbedUrl(videoUrl);
        if (videoId != null) {
            return "https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg";
        }
        return null;
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
        return itemClassroomContentModels != null ? itemClassroomContentModels.size() : 0;
    }

    public class ItemClassroomContentViewHolder extends RecyclerView.ViewHolder {
        TextView date, chapterDescription, chapterTitle;
        ImageView chapterImage;
        LinearLayout describeLayout;
        public ItemClassroomContentViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterTitle = itemView.findViewById(R.id.packageTitle);
            chapterDescription = itemView.findViewById(R.id.packageDescription);
            date = itemView.findViewById(R.id.dateId);
            chapterImage = itemView.findViewById(R.id.packageImg);
            describeLayout = itemView.findViewById(R.id.describeLayout);
        }
    }
}
