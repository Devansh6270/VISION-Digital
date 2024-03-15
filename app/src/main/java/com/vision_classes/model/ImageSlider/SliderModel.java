package com.vision_classes.model.ImageSlider;

public class SliderModel {
    String id;
    String title;
    String image;
    String courseId;
    String courseType;

    public SliderModel() {
    }

    public SliderModel(String id, String title, String image, String courseId, String courseType) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.courseId = courseId;
        this.courseType = courseType;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }
}
