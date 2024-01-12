package com.vision_digital.TestSeries.model.testResultNew;

public class ItemTopicWiseModel {
    String topicName;
    String correctTopic;
    String topicLevel;

    public ItemTopicWiseModel(String topicName, String correctTopic, String topicLevel) {
        this.topicName = topicName;
        this.correctTopic = correctTopic;
        this.topicLevel = topicLevel;
    }

    public ItemTopicWiseModel() {
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getCorrectTopic() {
        return correctTopic;
    }

    public void setCorrectTopic(String correctTopic) {
        this.correctTopic = correctTopic;
    }

    public String getTopicLevel() {
        return topicLevel;
    }

    public void setTopicLevel(String topicLevel) {
        this.topicLevel = topicLevel;
    }
}
