package com.vision_digital.community.studentModel;

public class ItemStudents {
    String student_id;
    String student_username;
    String user_image;

    public ItemStudents() {
    }

    public ItemStudents(String student_id, String student_username, String user_image) {
        this.student_id = student_id;
        this.student_username = student_username;
        this.user_image = user_image;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_username() {
        return student_username;
    }

    public void setStudent_username(String student_username) {
        this.student_username = student_username;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
