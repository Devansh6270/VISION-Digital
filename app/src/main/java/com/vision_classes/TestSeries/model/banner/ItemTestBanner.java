package com.vision_classes.TestSeries.model.banner;

public class ItemTestBanner {

    String  idBanner ;
    String imageBanner;

    public ItemTestBanner(String idBanner, String imageBanner) {
        this.idBanner = idBanner;
        this.imageBanner = imageBanner;
    }

    public ItemTestBanner() {
    }

    public String getIdBanner() {
        return idBanner;
    }

    public void setIdBanner(String idBanner) {
        this.idBanner = idBanner;
    }

    public String getImageBanner() {
        return imageBanner;
    }

    public void setImageBanner(String imageBanner) {
        this.imageBanner = imageBanner;
    }
}