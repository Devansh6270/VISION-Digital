package com.vision_digital.community;

public class ItemCommunityModel {
    String community_id;
    String community_name;
    String community_image_url;
//    String lastChat;
//    Date lastChatTime;

    public ItemCommunityModel() {
    }

    public ItemCommunityModel(String community_id, String community_name, String community_image_url) {
        this.community_id = community_id;
        this.community_name = community_name;
        this.community_image_url = community_image_url;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getCommunity_image_url() {
        return community_image_url;
    }

    public void setCommunity_image_url(String community_image_url) {
        this.community_image_url = community_image_url;
    }


}
