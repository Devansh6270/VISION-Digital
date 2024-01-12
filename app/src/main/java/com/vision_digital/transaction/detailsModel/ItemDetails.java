package com.vision_digital.transaction.detailsModel;

public class ItemDetails {
    String courseId;
    String subscriptionDate;
    String accessibleDate;
    String lastRenewedDate;
    String courseStatus;
    String subscribedMonth;
    String title;
    String ImageUrl;

    public ItemDetails(String courseId, String subscriptionDate, String accessibleDate, String lastRenewedDate, String courseStatus, String subscribedMonth, String title, String imageUrl) {
        this.courseId = courseId;
        this.subscriptionDate = subscriptionDate;
        this.accessibleDate = accessibleDate;
        this.lastRenewedDate = lastRenewedDate;
        this.courseStatus = courseStatus;
        this.subscribedMonth = subscribedMonth;
        this.title = title;
        ImageUrl = imageUrl;
    }

    public ItemDetails(){

    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(String subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public String getAccessibleDate() {
        return accessibleDate;
    }

    public void setAccessibleDate(String accessibleDate) {
        this.accessibleDate = accessibleDate;
    }

    public String getLastRenewedDate() {
        return lastRenewedDate;
    }

    public void setLastRenewedDate(String lastRenewedDate) {
        this.lastRenewedDate = lastRenewedDate;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getSubscribedMonth() {
        return subscribedMonth;
    }

    public void setSubscribedMonth(String subscribedMonth) {
        this.subscribedMonth = subscribedMonth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
