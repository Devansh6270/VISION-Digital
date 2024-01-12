package com.vision_digital.coupons;

public class ItemCoupon {
    String id;
    String couponCode;
    String couponTitle;
    String description;


    public ItemCoupon(String id, String couponCode, String couponTitle, String description) {
        this.id = id;
        this.couponCode = couponCode;
        this.couponTitle = couponTitle;
        this.description = description;
    }

    public ItemCoupon() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
