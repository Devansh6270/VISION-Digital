package com.vision_classes.model.Certificate;

public class CertificateModel {
    String subjectName;
    String Image;

    String id;

    public CertificateModel() {
    }

    public CertificateModel(String subjectName, String image, String id) {
        this.subjectName = subjectName;
        this.Image = image;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
