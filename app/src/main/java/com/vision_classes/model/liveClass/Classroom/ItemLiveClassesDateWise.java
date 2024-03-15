package com.vision_classes.model.liveClass.Classroom;

public class ItemLiveClassesDateWise {
    String id ;
    String title;
    String liveDate;
    String liveTime;
    String shortDesc;
    String videoUrl;
    String description;
    String accessType;
    String status;
    Boolean isLocked;
    String createdAt;
    String updatedAt;


    public ItemLiveClassesDateWise() {
    }

    public ItemLiveClassesDateWise(String id, String title, String liveDate, String liveTime, String shortDesc, String videoUrl, String description,
                                   String accessType, String status, Boolean isLocked, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.liveDate = liveDate;
        this.liveTime = liveTime;
        this.shortDesc = shortDesc;
        this.videoUrl = videoUrl;
        this.description = description;
        this.accessType = accessType;
        this.status = status;
        this.isLocked = isLocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLiveDate() {
        return liveDate;
    }

    public void setLiveDate(String liveDate) {
        this.liveDate = liveDate;
    }

    public String getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(String liveTime) {
        this.liveTime = liveTime;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
