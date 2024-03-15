package com.vision_classes.model.myCourses;

public class ItemSubscribedCourse {
    String id;
    String name;

    String image;
    String type;

    public ItemSubscribedCourse(String id, String name, String image, String type) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.type = type;
    }

    public ItemSubscribedCourse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
