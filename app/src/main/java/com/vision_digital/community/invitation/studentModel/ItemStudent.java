package com.vision_digital.community.invitation.studentModel;

public class ItemStudent {
    String student_username;
    String student_id;
    String userType;
    String status;
    String activity;
    boolean isSelected = false;


    public ItemStudent(String student_username, String student_id, String userType, String status, boolean isSelected) {
        this.student_username = student_username;
        this.student_id = student_id;
        this.userType = userType;
        this.status = status;
        this.isSelected = isSelected;
    }

    public ItemStudent(String activity) {
        this.activity = activity;
    }

    public ItemStudent(){

    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getStudent_username() {
        return student_username;
    }

    public void setStudent_username(String student_username) {
        this.student_username = student_username;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
