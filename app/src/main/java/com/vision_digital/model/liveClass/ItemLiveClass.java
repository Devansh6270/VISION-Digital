package com.vision_digital.model.liveClass;

public class ItemLiveClass {

    String teacherName;
    String teacherQualification;
    String teacherImage;
    String classTitle;
    String classDescription;
    String classStartTimestamp;
    String broadcastId;

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    String applicationId;

    public String getTeacherQualification() {
        return teacherQualification;
    }

    public void setTeacherQualification(String teacherQualification) {
        this.teacherQualification = teacherQualification;
    }

    public String getTeacherImage() {
        return teacherImage;
    }

    public void setTeacherImage(String teacherImage) {
        this.teacherImage = teacherImage;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }


    public ItemLiveClass() {
    }

    public String getClassStartTimestamp() {
        return classStartTimestamp;
    }

    public void setClassStartTimestamp(String classStartTimestamp) {
        this.classStartTimestamp = classStartTimestamp;
    }

    public ItemLiveClass(String teacherName, String teacherQualification, String teacherImage, String classTitle, String classDescription, String classStartTimestamp, String broadcastId, String applicationId) {
        this.teacherName = teacherName;
        this.teacherQualification = teacherQualification;
        this.teacherImage = teacherImage;
        this.classTitle = classTitle;
        this.classDescription = classDescription;
        this.classStartTimestamp = classStartTimestamp;
        this.broadcastId = broadcastId;
        this.applicationId = applicationId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
