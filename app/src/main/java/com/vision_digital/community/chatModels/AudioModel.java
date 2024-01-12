package com.vision_digital.community.chatModels;

public class AudioModel {

    private String chatAudioUri;

    public AudioModel(){

    }

    public AudioModel(String chatAudioUri) {
        this.chatAudioUri = chatAudioUri;
    }

    public String getChatAudioUri() {
        return chatAudioUri;
    }

    public void setChatAudioUri(String chatAudioUri) {
        this.chatAudioUri = chatAudioUri;
    }
}
