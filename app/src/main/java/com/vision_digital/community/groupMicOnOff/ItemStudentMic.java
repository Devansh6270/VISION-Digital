package com.vision_digital.community.groupMicOnOff;

public class ItemStudentMic {
    String invStudUsername;
    String invStudId;
    String userType;
    String status;
    String micStatus;
    String isSelected;

    public ItemStudentMic(String invStudUsername, String invStudId, String userType, String status, String micStatus, String isSelected) {
        this.invStudUsername = invStudUsername;
        this.invStudId = invStudId;
        this.userType = userType;
        this.status = status;
        this.micStatus = micStatus;
        this.isSelected = isSelected;
    }

    public ItemStudentMic() {
    }

    public String getInvStudUsername() {
        return invStudUsername;
    }

    public void setInvStudUsername(String invStudUsername) {
        this.invStudUsername = invStudUsername;
    }

    public String getInvStudId() {
        return invStudId;
    }

    public void setInvStudId(String invStudId) {
        this.invStudId = invStudId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMicStatus() {
        return micStatus;
    }

    public void setMicStatus(String micStatus) {
        this.micStatus = micStatus;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }
}
