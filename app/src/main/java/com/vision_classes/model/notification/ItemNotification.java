package com.vision_classes.model.notification;

public class ItemNotification {

    String title;
    String message; 
    String imageUrl;
    String pushKey;
    String docId;


    public ItemNotification(String title, String message, String imageUrl, String pushKey, String docId) {
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
        this.pushKey = pushKey;
        this.docId = docId;
    }

    public ItemNotification() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
