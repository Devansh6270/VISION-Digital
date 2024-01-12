package com.vision_digital.community.chooseadminModel;

public class ItemChooseModel {
    String invStudId;
    String invStudUsername;
    String micStatus;
    String status;
    String userType;
    boolean isSelected;

    public ItemChooseModel(String invStudId, String invStudUsername, String micStatus, String status, String userType) {
        this.invStudId = invStudId;
        this.invStudUsername = invStudUsername;
        this.micStatus = micStatus;
        this.status = status;
        this.userType = userType;
    }

    public ItemChooseModel(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public ItemChooseModel() {
    }

    public String getInvStudId() {
        return invStudId;
    }

    public void setInvStudId(String invStudId) {
        this.invStudId = invStudId;
    }

    public String getInvStudUsername() {
        return invStudUsername;
    }

    public void setInvStudUsername(String invStudUsername) {
        this.invStudUsername = invStudUsername;
    }

    public String getMicStatus() {
        return micStatus;
    }

    public void setMicStatus(String micStatus) {
        this.micStatus = micStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
