package com.vision_classes.model.offlineResult;

public class ItemOfflineResultList {
    String batchNumber, testDate, physics, chemistry, biology, maths, rank, totalMarks, percentage;

    public ItemOfflineResultList(){

    }
    public ItemOfflineResultList(String batchNumber, String testDate, String physics, String chemistry, String biology, String maths, String rank, String totalMarks, String percentage) {
        this.batchNumber = batchNumber;
        this.testDate = testDate;
        this.physics = physics;
        this.chemistry = chemistry;
        this.biology = biology;
        this.maths = maths;
        this.rank = rank;
        this.totalMarks = totalMarks;
        this.percentage = percentage;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getPhysics() {
        return physics;
    }

    public void setPhysics(String physics) {
        this.physics = physics;
    }

    public String getChemistry() {
        return chemistry;
    }

    public void setChemistry(String chemistry) {
        this.chemistry = chemistry;
    }

    public String getBiology() {
        return biology;
    }

    public void setBiology(String biology) {
        this.biology = biology;
    }

    public String getMaths() {
        return maths;
    }

    public void setMaths(String maths) {
        this.maths = maths;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
