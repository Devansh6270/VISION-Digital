package com.vision_digital.model.CoursePackage;

public class ItemPackageList {
    String id ;
    String title;
    String image;
    String promoVideo;
    String price;
    String ownerName;

    public ItemPackageList() {
    }

    public ItemPackageList(String id, String title, String image, String promoVideo, String price, String ownerName) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.promoVideo = promoVideo;
        this.price = price;
        this.ownerName = ownerName;
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

    public String getPromoVideo() {
        return promoVideo;
    }

    public void setPromoVideo(String promoVideo) {
        this.promoVideo = promoVideo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
