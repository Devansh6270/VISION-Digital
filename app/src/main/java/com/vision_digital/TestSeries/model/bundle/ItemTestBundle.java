package com.vision_digital.TestSeries.model.bundle;

public class ItemTestBundle {

    String id;
    String title;
    String description;
    String duration;
    String owner_name;
    String owner_qualification;
    String price;
    String image;
    String type="";
    String testType;
    String status;


    public ItemTestBundle() {
    }

    public ItemTestBundle(String id, String title, String description, String duration, String owner_name, String owner_qualification, String price, String image, String type, String testType, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.owner_name = owner_name;
        this.owner_qualification = owner_qualification;
        this.price = price;
        this.image = image;
        this.type = type;
        this.testType = testType;
        this.status = status;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_qualification() {
        return owner_qualification;
    }

    public void setOwner_qualification(String owner_qualification) {
        this.owner_qualification = owner_qualification;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
