package com.vision_digital.model.videoItem;

import com.google.android.exoplayer2.SimpleExoPlayer;

public class VideoItem {

    String videoUrl;
    public SimpleExoPlayer player;


    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public void setPlayer(SimpleExoPlayer player) {
        this.player = player;
    }

    public VideoItem(String videoUrl, SimpleExoPlayer player) {
        this.videoUrl = videoUrl;
        this.player = player;
    }


    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public VideoItem() {
    }

}
