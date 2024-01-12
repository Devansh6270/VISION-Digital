package com.vision_digital.community.communitymodels;

public class CommunityItem {
    String community_id;
    String last_chat;
    String last_chat_type;
    String community_name;
    String community_time;
    String community_image_url;

    public CommunityItem(String community_id, String last_chat, String last_chat_type, String community_name, String community_time, String community_image_url) {
        this.community_id = community_id;
        this.last_chat = last_chat;
        this.last_chat_type = last_chat_type;
        this.community_name = community_name;
        this.community_time = community_time;
        this.community_image_url = community_image_url;
    }

    public CommunityItem(){

    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getLast_chat() {
        return last_chat;
    }

    public void setLast_chat(String last_chat) {
        this.last_chat = last_chat;
    }

    public String getLast_chat_type() {
        return last_chat_type;
    }

    public void setLast_chat_type(String last_chat_type) {
        this.last_chat_type = last_chat_type;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getCommunity_time() {
        return community_time;
    }

    public void setCommunity_time(String community_time) {
        this.community_time = community_time;
    }

    public String getImage_url() {
        return community_image_url;
    }

    public void setImage_url(String image_url) {
        this.community_image_url = image_url;
    }
}

