package com.vision_digital.model.continueWatchingCourseItem;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class CourseItem {

    String courseId;
    String lastPlayedMilestoneId;
    String milestoneName;
    String courseTitle;
    double percentageWatched;
    double courseDuration;
    double totalPlayDuration;
    double accuracy;
    double actualPlayDuration;
    double actualMilestoneDuration;


    //    List<Float> timeList = new ArrayList<>();
    ArrayList<Entry> actualTimeList = new ArrayList<>();
    ArrayList<Entry> spentTimeList = new ArrayList<>();





    public CourseItem(){

    }


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getLastPlayedMilestoneId() {
        return lastPlayedMilestoneId;
    }

    public void setLastPlayedMilestoneId(String lastPlayedMilestoneId) {
        this.lastPlayedMilestoneId = lastPlayedMilestoneId;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public double getPercentageWatched() {
        return percentageWatched;
    }

    public void setPercentageWatched(double percentageWatched) {
        this.percentageWatched = percentageWatched;
    }

    public double getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(double courseDuration) {
        this.courseDuration = courseDuration;
    }

    public double getTotalPlayDuration() {
        return totalPlayDuration;
    }

    public void setTotalPlayDuration(double totalPlayDuration) {
        this.totalPlayDuration = totalPlayDuration;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getActualPlayDuration() {
        return actualPlayDuration;
    }

    public void setActualPlayDuration(double actualPlayDuration) {
        this.actualPlayDuration = actualPlayDuration;
    }

    public double getActualMilestoneDuration() {
        return actualMilestoneDuration;
    }

    public void setActualMilestoneDuration(double actualMilestoneDuration) {
        this.actualMilestoneDuration = actualMilestoneDuration;
    }

    public ArrayList<Entry> getActualTimeList() {
        return actualTimeList;
    }

    public void setActualTimeList(ArrayList<Entry> actualTimeList) {
        this.actualTimeList = actualTimeList;
    }

    public ArrayList<Entry> getSpentTimeList() {
        return spentTimeList;
    }

    public void setSpentTimeList(ArrayList<Entry> spentTimeList) {
        this.spentTimeList = spentTimeList;
    }

    public CourseItem(String courseId, String lastPlayedMilestoneId, String milestoneName, String courseTitle, double percentageWatched, double courseDuration, double totalPlayDuration, double accuracy, double actualPlayDuration, double actualMilestoneDuration, ArrayList<Entry> actualTimeList, ArrayList<Entry> spentTimeList) {


        this.courseId = courseId;
        this.lastPlayedMilestoneId = lastPlayedMilestoneId;
        this.milestoneName = milestoneName;
        this.courseTitle = courseTitle;
        this.percentageWatched = percentageWatched;
        this.courseDuration = courseDuration;
        this.totalPlayDuration = totalPlayDuration;
        this.accuracy = accuracy;
        this.actualPlayDuration = actualPlayDuration;
        this.actualMilestoneDuration = actualMilestoneDuration;
        this.actualTimeList = actualTimeList;
        this.spentTimeList = spentTimeList;
    }
}
