package com.vision_digital.model.chats;

public class Messages {
    private  String messageId ,message,senderId  ;
    String  timestamp ;
    private int feeling = -1;

    String userName;

    public Messages() {
    }

    public Messages(String message, String senderId, String timestamp ,String userName) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.userName=userName;

    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}