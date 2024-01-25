package com.vision_digital.TestSeries.model.dashboardTestSeries;

public class DashTestSeriesModel {
    String id;
    String title;
    String sellingPrice;
    String actualPrice;
    String image;

    public DashTestSeriesModel(String id, String title, String sellingPrice, String actualPrice, String image) {
        this.id = id;
        this.title = title;
        this.sellingPrice = sellingPrice;
        this.actualPrice = actualPrice;
        this.image = image;
    }

    public DashTestSeriesModel() {
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

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
