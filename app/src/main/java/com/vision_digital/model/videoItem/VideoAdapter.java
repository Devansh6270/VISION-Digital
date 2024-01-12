package com.vision_digital.model.videoItem;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;


import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<VideoItem> videoItemList;

    Context context;


    public VideoAdapter(List<VideoItem> videoItemList) {
        this.videoItemList = videoItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_video, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri videoUri;
        SimpleExoPlayer player;
        videoUri = Uri.parse(videoItemList.get(position).getVideoUrl());
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        player = new SimpleExoPlayer.Builder(context).build();
        holder.videoView.setPlayer(player);
        player.setMediaItem(mediaItem);

        videoItemList.get(position).setPlayer(player);
        Log.e("position:"+position,""+player);
        Log.e("position:"+position,""+videoItemList.get(position));

    }

    @Override
    public int getItemCount() {
        return videoItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        PlayerView videoView;

        ViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
        }
    }
}
