package com.vision_classes.TestSeries.model.SubjectivePaper;

public class ItemSubjectivePaperList {
    String subjectiveId, subjectiveTitle, test_paper_type, lock, subjectiveCategory_id, start_at, end_at, buy_status, buy_text, buy_button;

    public ItemSubjectivePaperList(){

    }
    public ItemSubjectivePaperList(String subjectiveId, String subjectiveTitle, String test_paper_type, String lock, String subjectiveCategory_id, String start_at, String end_at, String buy_status, String buy_text, String buy_button) {
        this.subjectiveId = subjectiveId;
        this.subjectiveTitle = subjectiveTitle;
        this.test_paper_type = test_paper_type;
        this.lock = lock;
        this.subjectiveCategory_id = subjectiveCategory_id;
        this.start_at = start_at;
        this.end_at = end_at;
        this.buy_status = buy_status;
        this.buy_text = buy_text;
        this.buy_button = buy_button;
    }

    public String getSubjectiveId() {
        return subjectiveId;
    }

    public void setSubjectiveId(String subjectiveId) {
        this.subjectiveId = subjectiveId;
    }

    public String getSubjectiveTitle() {
        return subjectiveTitle;
    }

    public void setSubjectiveTitle(String subjectiveTitle) {
        this.subjectiveTitle = subjectiveTitle;
    }

    public String getTest_paper_type() {
        return test_paper_type;
    }

    public void setTest_paper_type(String test_paper_type) {
        this.test_paper_type = test_paper_type;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getSubjectiveCategory_id() {
        return subjectiveCategory_id;
    }

    public void setSubjectiveCategory_id(String subjectiveCategory_id) {
        this.subjectiveCategory_id = subjectiveCategory_id;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public String getBuy_status() {
        return buy_status;
    }

    public void setBuy_status(String buy_status) {
        this.buy_status = buy_status;
    }

    public String getBuy_text() {
        return buy_text;
    }

    public void setBuy_text(String buy_text) {
        this.buy_text = buy_text;
    }

    public String getBuy_button() {
        return buy_button;
    }

    public void setBuy_button(String buy_button) {
        this.buy_button = buy_button;
    }
}
