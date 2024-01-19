package com.vision_digital.TestSeries.model.objectiveQuestion;

import androidx.annotation.Nullable;

import com.vision_digital.TestSeries.model.objectiveQuestion.options.ItemOption;

import java.util.ArrayList;

public class ItemObjectiveQuestion {
    String id;
    String question;
    @Nullable
    String questionImageURL;
    String status;
    String answeredAnswer = "";
    String positiveMarks, negativeMarks;

    public String getPositiveMarks() {
        return positiveMarks;
    }

    public void setPositiveMarks(String positiveMarks) {
        this.positiveMarks = positiveMarks;
    }

    public String getNegativeMarks() {
        return negativeMarks;
    }

    public void setNegativeMarks(String negativeMarks) {
        this.negativeMarks = negativeMarks;
    }

    ArrayList<ItemOption> options = new ArrayList<>();


    public ItemObjectiveQuestion(String id, String question, @Nullable String questionImageURL, String status, String answeredAnswer,
                                 String positiveMarks, String negativeMarks, ArrayList<ItemOption> options) {
        this.id = id;
        this.question = question;
        this.questionImageURL = questionImageURL;
        this.status = status;
        this.answeredAnswer = answeredAnswer;
        this.positiveMarks = positiveMarks;
        this.negativeMarks = negativeMarks;
        this.options = options;
    }

    public ItemObjectiveQuestion() {

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Nullable
    public String getQuestionImageURL() {
        return questionImageURL;
    }

    public void setQuestionImageURL(@Nullable String questionImageURL) {
        this.questionImageURL = questionImageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnsweredAnswer() {
        return answeredAnswer;
    }

    public void setAnsweredAnswer(String answeredAnswer) {
        this.answeredAnswer = answeredAnswer;
    }

    public ArrayList<ItemOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<ItemOption> options) {
        this.options = options;
    }
}
