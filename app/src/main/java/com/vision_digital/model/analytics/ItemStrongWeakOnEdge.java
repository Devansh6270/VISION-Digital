package com.vision_digital.model.analytics;

public class ItemStrongWeakOnEdge {
    String chapter_id;
    String chapter_name;

    String status;

    String ranking;


    double rank;

    double progress;

    String type;

    public ItemStrongWeakOnEdge(String chapter_id, String chapter_name, String status, String ranking, double rank, double progress, String type) {
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.status = status;
        this.ranking = ranking;
        this.rank = rank;
        this.progress = progress;
        this.type = type;
    }

    public ItemStrongWeakOnEdge() {
    }


    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }
}
