package com.vision_digital.model.popularTeachers;

public class ItemTeacher {

    long id;
    String name;
    String mobile;
    String email;
    String qualification;
    String country;
    String state;
    String city;
    String subscriber;
    String image;
    long popularity;
    String type;

    public ItemTeacher() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public ItemTeacher(long id, String name, String mobile, String email, String qualification, String country, String state, String city, String subscriber, String image, long popularity, String type) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.qualification = qualification;
        this.country = country;
        this.state = state;
        this.city = city;
        this.subscriber = subscriber;
        this.image = image;
        this.popularity = popularity;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }
}
