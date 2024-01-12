package com.vision_digital.model.milestone;

import java.io.Serializable;

public class ItemMileStone implements Serializable {
    String id;
    String title;
    String sort_order;
    String duration;
    String videoUrl;
    String activityType;
    int videoPosition;
    Boolean isSelected;
    String milestoneType;
    int chapterId;

    String searchKeyword;
    String searchId;
    String searchMileId;

    String courseId;
    String chapterIdString;

    String courseLogo;



    public ItemMileStone(String id, String title, String sort_order, String duration, String videoUrl, String activityType, int videoPosition, Boolean isSelected, String milestoneType, int chapterId, String searchKeyword, String searchId, String searchMileId, String courseId, String chapterIdString, String courseLogo) {
        this.id = id;
        this.title = title;
        this.sort_order = sort_order;
        this.duration = duration;
        this.videoUrl = videoUrl;
        this.activityType = activityType;
        this.videoPosition = videoPosition;
        this.isSelected = isSelected;
        this.milestoneType = milestoneType;
        this.chapterId = chapterId;
        this.searchKeyword = searchKeyword;
        this.searchId = searchId;
        this.searchMileId = searchMileId;
        this.courseId = courseId;
        this.chapterIdString = chapterIdString;
        this.courseLogo = courseLogo;
    }

    public ItemMileStone() {
    }


    public String getCourseLogo() {
        return courseLogo;
    }

    public void setCourseLogo(String courseLogo) {
        this.courseLogo = courseLogo;
    }

    public String getChapterIdString() {
        return chapterIdString;
    }

    public void setChapterIdString(String chapterIdString) {
        this.chapterIdString = chapterIdString;
    }

    public String getSearchMileId() {
        return searchMileId;
    }

    public void setSearchMileId(String searchMileId) {
        this.searchMileId = searchMileId;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
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

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getMilestoneType() {
        return milestoneType;
    }

    public void setMilestoneType(String milestoneType) {
        this.milestoneType = milestoneType;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }
}
