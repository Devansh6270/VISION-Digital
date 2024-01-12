package com.vision_digital.TestSeries.model.testResultNew;

public class ItemLevelwiseModel {


     String level;
     String correct;
     String quesNo;
     String quesId;
     String quesStatus;
     String quesLevel;
     String quesTopic;
    String sno;

    public ItemLevelwiseModel(String level, String correct, String quesNo, String quesId, String quesStatus, String quesLevel, String quesTopic,String sno) {
        this.level = level;
        this.correct = correct;
        this.quesNo = quesNo;
        this.quesId = quesId;
        this.quesStatus = quesStatus;
        this.quesLevel = quesLevel;
        this.quesTopic = quesTopic;
        this.sno = sno;
    }

    public ItemLevelwiseModel() {
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getQuesNo() {
        return quesNo;
    }

    public void setQuesNo(String quesNo) {
        this.quesNo = quesNo;
    }

    public String getQuesId() {
        return quesId;
    }

    public void setQuesId(String quesId) {
        this.quesId = quesId;
    }

    public String getQuesStatus() {
        return quesStatus;
    }

    public void setQuesStatus(String quesStatus) {
        this.quesStatus = quesStatus;
    }

    public String getQuesLevel() {
        return quesLevel;
    }

    public void setQuesLevel(String quesLevel) {
        this.quesLevel = quesLevel;
    }

    public String getQuesTopic() {
        return quesTopic;
    }

    public void setQuesTopic(String quesTopic) {
        this.quesTopic = quesTopic;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }
}
