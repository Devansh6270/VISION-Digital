package com.vision_digital.TestSeries.model.allTestSeries;

public class ItemAllTestPageList {
    public String getTestName() {
        return testName;
    }

    public ItemAllTestPageList(){

    }

    public ItemAllTestPageList(String testName, String totalQuestions, String totalTime, String totalMarks, String result) {
        this.testName = testName;
        this.totalQuestions = totalQuestions;
        this.totalTime = totalTime;
        this.totalMarks = totalMarks;
        this.result = result;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(String totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    String testName, totalQuestions, totalTime, totalMarks, result;
    String id, desc, price;
    String lock, category_id, testListId, startTimeTestList, endTimeTestList, buy_status, buy_button;

    public String getStartTimeTestList() {
        return startTimeTestList;
    }

    public void setStartTimeTestList(String startTimeTestList) {
        this.startTimeTestList = startTimeTestList;
    }

    public String getEndTimeTestList() {
        return endTimeTestList;
    }

    public void setEndTimeTestList(String endTimeTestList) {
        this.endTimeTestList = endTimeTestList;
    }

    public String getBuy_status() {
        return buy_status;
    }

    public void setBuy_status(String buy_status) {
        this.buy_status = buy_status;
    }

    public String getBuy_button() {
        return buy_button;
    }

    public void setBuy_button(String buy_button) {
        this.buy_button = buy_button;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getTestListId() {
        return testListId;
    }

    public void setTestListId(String testListId) {
        this.testListId = testListId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
