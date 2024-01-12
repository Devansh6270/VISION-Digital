package com.vision_digital.model.analytics;

public class ItemAnalytics {
    long id ;
    String courseTitle ;
    String courseOwner;
    String image;

    String type;

    public ItemAnalytics() {
    }

    public ItemAnalytics(long id ,String courseTitle, String courseOwner , String image ,String type) {
        this.id=id;
        this.courseTitle = courseTitle;
        this.courseOwner = courseOwner;
        this.image=image;
        this.type=type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseOwner() {
        return courseOwner;
    }

    public void setCourseOwner(String courseOwner) {
        this.courseOwner = courseOwner;
    }
}
