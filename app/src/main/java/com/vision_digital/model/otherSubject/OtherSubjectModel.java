package com.vision_digital.model.otherSubject;

public class OtherSubjectModel {
    String hospitalityTxt;
    String topicTxt;
    String subjectImage;

    public OtherSubjectModel() {
    }

    public OtherSubjectModel(String hospitalityTxt, String topicTxt, String subjectImage) {
        this.hospitalityTxt = hospitalityTxt;
        this.topicTxt = topicTxt;
        this.subjectImage = subjectImage;
    }

    public String getHospitalityTxt() {
        return hospitalityTxt;
    }

    public void setHospitalityTxt(String hospitalityTxt) {
        this.hospitalityTxt = hospitalityTxt;
    }

    public String getTopicTxt() {
        return topicTxt;
    }

    public void setTopicTxt(String topicTxt) {
        this.topicTxt = topicTxt;
    }

    public String getSubjectImage() {
        return subjectImage;
    }

    public void setSubjectImage(String subjectImage) {
        this.subjectImage = subjectImage;
    }
}
