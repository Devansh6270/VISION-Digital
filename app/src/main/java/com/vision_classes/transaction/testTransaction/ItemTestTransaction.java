package com.vision_classes.transaction.testTransaction;

public class ItemTestTransaction {
    String testId;
    String testSubsDate;
    String startAt;
    String status;
    String Image;
    String title;

    public ItemTestTransaction(String testId, String testSubsDate, String startAt, String status, String image, String title) {
        this.testId = testId;
        this.testSubsDate = testSubsDate;
        this.startAt = startAt;
        this.status = status;
        Image = image;
        this.title = title;
    }

    public ItemTestTransaction(){

    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestSubsDate() {
        return testSubsDate;
    }

    public void setTestSubsDate(String testSubsDate) {
        this.testSubsDate = testSubsDate;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
