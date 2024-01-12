package com.vision_digital.TestSeries.model.section;

import java.util.ArrayList;

public class ItemSection {

    String id;
    String name;
    String type;
    ArrayList<Object> questionsList = new ArrayList<Object>();

    public ItemSection() {
    }

    public ItemSection(String id, String name, String type, ArrayList<Object> questionsList) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.questionsList = questionsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Object> getQuestionsList() {
        return questionsList;
    }

    public void setQuestionsList(ArrayList<Object> questionsList) {
        this.questionsList = questionsList;
    }


}
