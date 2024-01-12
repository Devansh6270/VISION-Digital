package com.vision_digital.community.chatModels;

import java.util.Date;

public class ItemChat {
    String message;
    String userId;
    String msgType;
    Date msgTime;
    String chatImage;
    String chatAudio;
    String msgCategory;
    Boolean isReply;
    String replyOfMsg;
    String replyOfUser;
    String chatId;
    String invitationTimeInString;
    String repliedOfChatId;
    String userName;
    String userImage;
    int doubtWeight;
    String invitationTime;
    String milestoneID;
    int videoPosition;
    String videoStatusId;


    public ItemChat() {
    }

    public ItemChat(String message, String userId, String msgType, Date msgTime, String chatImage, String chatAudio, String msgCategory, Boolean isReply, String replyOfMsg, String replyOfUser, String chatId, String invitationTimeInString, String repliedOfChatId, String userName, String userImage, int doubtWeight, String invitationTime, String milestoneID, int videoPosition, String videoStatusId) {
        this.message = message;
        this.userId = userId;
        this.msgType = msgType;
        this.msgTime = msgTime;
        this.chatImage = chatImage;
        this.chatAudio = chatAudio;
        this.msgCategory = msgCategory;
        this.isReply = isReply;
        this.replyOfMsg = replyOfMsg;
        this.replyOfUser = replyOfUser;
        this.chatId = chatId;
        this.invitationTimeInString = invitationTimeInString;
        this.repliedOfChatId = repliedOfChatId;
        this.userName = userName;
        this.userImage = userImage;
        this.doubtWeight = doubtWeight;
        this.invitationTime = invitationTime;
        this.milestoneID = milestoneID;
        this.videoPosition = videoPosition;
        this.videoStatusId = videoStatusId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Date getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Date msgTime) {
        this.msgTime = msgTime;
    }

    public String getChatImage() {
        return chatImage;
    }

    public void setChatImage(String chatImage) {
        this.chatImage = chatImage;
    }

    public String getChatAudio() {
        return chatAudio;
    }

    public void setChatAudio(String chatAudio) {
        this.chatAudio = chatAudio;
    }

    public String getMsgCategory() {
        return msgCategory;
    }

    public void setMsgCategory(String msgCategory) {
        this.msgCategory = msgCategory;
    }

    public Boolean getReply() {
        return isReply;
    }

    public void setReply(Boolean reply) {
        isReply = reply;
    }

    public String getReplyOfMsg() {
        return replyOfMsg;
    }

    public void setReplyOfMsg(String replyOfMsg) {
        this.replyOfMsg = replyOfMsg;
    }

    public String getReplyOfUser() {
        return replyOfUser;
    }

    public void setReplyOfUser(String replyOfUser) {
        this.replyOfUser = replyOfUser;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getInvitationTimeInString() {
        return invitationTimeInString;
    }

    public void setInvitationTimeInString(String invitationTimeInString) {
        this.invitationTimeInString = invitationTimeInString;
    }

    public String getRepliedOfChatId() {
        return repliedOfChatId;
    }

    public void setRepliedOfChatId(String repliedOfChatId) {
        this.repliedOfChatId = repliedOfChatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getDoubtWeight() {
        return doubtWeight;
    }

    public void setDoubtWeight(int doubtWeight) {
        this.doubtWeight = doubtWeight;
    }

    public String getInvitationTime() {
        return invitationTime;
    }

    public void setInvitationTime(String invitationTime) {
        this.invitationTime = invitationTime;
    }

    public String getMilestoneID() {
        return milestoneID;
    }

    public void setMilestoneID(String milestoneID) {
        this.milestoneID = milestoneID;
    }

    public int getVideoPosition() {
        return videoPosition;
    }

    public void setVideoPosition(int videoPosition) {
        this.videoPosition = videoPosition;
    }

    public String getVideoStatusId() {
        return videoStatusId;
    }

    public void setVideoStatusId(String videoStatusId) {
        this.videoStatusId = videoStatusId;
    }
}
