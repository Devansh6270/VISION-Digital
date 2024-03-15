package com.vision_classes.model.liveClass.Classroom;

public class ItemLiveClassDate {
    String Date;
    String type;

    String month;

    public ItemLiveClassDate() {
    }

    public ItemLiveClassDate(String date, String type, String month) {
        Date = date;
        this.type = type;
        this.month = month;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
