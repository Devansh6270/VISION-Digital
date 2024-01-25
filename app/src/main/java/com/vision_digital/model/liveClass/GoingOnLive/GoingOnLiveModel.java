package com.vision_digital.model.liveClass.GoingOnLive;

public class GoingOnLiveModel {
    String id;
    String CourseId;
    String liveDate;
    String liveTime;
    String description;
    String url;
    String title;
    String courseTitle;
    String liveStatus;
    String liveCurrentStatus;

    public GoingOnLiveModel(String id, String courseId, String liveDate,
                            String liveTime, String url, String title, String courseTitle,String description,
                            String liveStatus, String liveCurrentStatus) {
        this.id = id;
        CourseId = courseId;
        this.liveDate = liveDate;
        this.liveTime = liveTime;
        this.url = url;
        this.title = title;
        this.courseTitle = courseTitle;
        this.liveStatus = liveStatus;
        this.liveCurrentStatus = liveCurrentStatus;
        this.description = description;
    }

    public GoingOnLiveModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String courseId) {
        CourseId = courseId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getLiveCurrentStatus() {
        return liveCurrentStatus;
    }

    public void setLiveCurrentStatus(String liveCurrentStatus) {
        this.liveCurrentStatus = liveCurrentStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
