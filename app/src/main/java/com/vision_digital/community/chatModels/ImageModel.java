package com.vision_digital.community.chatModels;

public class ImageModel {

    private String chatImageUrl;

    public ImageModel() {
    }

    public ImageModel(String chatImageUrl) {
        this.chatImageUrl = chatImageUrl;
    }

    public String getChatImageUrl() {
        return chatImageUrl;
    }

    public void setChatImageUrl(String chatImageUrl) {
        this.chatImageUrl = chatImageUrl;
    }
}
