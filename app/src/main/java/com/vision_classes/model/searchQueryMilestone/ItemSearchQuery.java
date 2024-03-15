package com.vision_classes.model.searchQueryMilestone;

public class ItemSearchQuery {

    String id;
    String mid;
    String chapter_id;
    String course_id;
    String title;
    String duration;
    String videoUrl;
    String activityType;
    int videoPosition;
    String milestoneType;

    public ItemSearchQuery(String id, String mid, String chapter_id, String course_id, String title, String duration, String videoUrl, String activityType, int videoPosition, String milestoneType) {
        this.id = id;
        this.mid = mid;
        this.chapter_id = chapter_id;
        this.course_id = course_id;
        this.title = title;
        this.duration = duration;
        this.videoUrl = videoUrl;
        this.activityType = activityType;
        this.videoPosition = videoPosition;
        this.milestoneType = milestoneType;
    }

    public ItemSearchQuery() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getVideoPosition() {
        return videoPosition;
    }

    public void setVideoPosition(int videoPosition) {
        this.videoPosition = videoPosition;
    }

    public String getMilestoneType() {
        return milestoneType;
    }

    public void setMilestoneType(String milestoneType) {
        this.milestoneType = milestoneType;
    }
}
