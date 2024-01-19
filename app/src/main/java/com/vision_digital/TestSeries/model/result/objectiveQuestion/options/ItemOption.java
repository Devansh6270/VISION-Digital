package com.vision_digital.TestSeries.model.result.objectiveQuestion.options;

public class ItemOption {
    String option;
    String optionImageUrl;
    String correct_ans;//Correct Incorrect NA
    String selected_ans;
    String ans_status;
    String optionNo;
    String tvOptionNo;

    public String getTvOptionNo() {
        return tvOptionNo;
    }

    public void setTvOptionNo(String tvOptionNo) {
        this.tvOptionNo = tvOptionNo;
    }

    public ItemOption(String option, String optionImageUrl, String correct_ans, String selected_ans, String ans_status, String optionNo) {
        this.option = option;
        this.optionImageUrl = optionImageUrl;
        this.correct_ans = correct_ans;
        this.selected_ans = selected_ans;
        this.ans_status = ans_status;
        this.optionNo = optionNo;
    }

    public String getCorrect_ans() {
        return correct_ans;
    }

    public void setCorrect_ans(String correct_ans) {
        this.correct_ans = correct_ans;
    }

    public String getSelected_ans() {
        return selected_ans;
    }

    public void setSelected_ans(String selected_ans) {
        this.selected_ans = selected_ans;
    }

    public String getAns_status() {
        return ans_status;
    }

    public void setAns_status(String ans_status) {
        this.ans_status = ans_status;
    }



    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public ItemOption() {
    }

    public ItemOption(String option) {
        this.option = option;
    }

    public String getOptionImageUrl() {
        return optionImageUrl;
    }

    public void setOptionImageUrl(String optionImageUrl) {
        this.optionImageUrl = optionImageUrl;
    }

    public String getOptionNo() {
        return optionNo;
    }

    public void setOptionNo(String optionNo) {
        this.optionNo = optionNo;
    }
}
