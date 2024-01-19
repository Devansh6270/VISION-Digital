package com.vision_digital.TestSeries.model.result.objectiveQuestion;

import com.vision_digital.TestSeries.model.result.objectiveQuestion.options.ItemOption;

import java.util.ArrayList;

public class ItemObjectiveQuestion {
    String id;
    String question;
    String questionImageURL;
    String status;
    String correct_answer;
    String answer_status;
    String selected_answer;

    String negativeMarks;
    String positiveMarks;
    ArrayList<ItemOption> options = new ArrayList<>();



    String wvResult;



    public ItemObjectiveQuestion() {

    }

    public ItemObjectiveQuestion(String id, String question, String questionImageURL, String status, String correct_answer,
                                 String answer_status, String selected_answer, String negativeMarks, String positiveMarks, ArrayList<ItemOption> options) {
        this.id = id;
        this.question = question;
        this.questionImageURL = questionImageURL;
        this.status = status;
        this.correct_answer = correct_answer;
        this.answer_status = answer_status;
        this.selected_answer = selected_answer;
        this.negativeMarks = negativeMarks;
        this.positiveMarks = positiveMarks;
        this.options = options;
    }

    public String getWvResult() {
        return wvResult;
    }

    public void setWvResult(String wvResult) {
        this.wvResult = wvResult;
    }
    public String getNegativeMarks() {
        return negativeMarks;
    }

    public void setNegativeMarks(String negativeMarks) {
        this.negativeMarks = negativeMarks;
    }

    public String getPositiveMarks() {
        return positiveMarks;
    }

    public void setPositiveMarks(String positiveMarks) {
        this.positiveMarks = positiveMarks;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public String getAnswer_status() {
        return answer_status;
    }

    public void setAnswer_status(String answer_status) {
        this.answer_status = answer_status;
    }

    public String getSelected_answer() {
        return selected_answer;
    }

    public void setSelected_answer(String selected_answer) {
        this.selected_answer = selected_answer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getQuestionImageURL() {
        return questionImageURL;
    }

    public void setQuestionImageURL(String questionImageURL) {
        this.questionImageURL = questionImageURL;
    }

    public ArrayList<ItemOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<ItemOption> options) {
        this.options = options;
    }
}
