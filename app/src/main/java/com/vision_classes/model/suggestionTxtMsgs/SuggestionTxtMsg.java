package com.vision_classes.model.suggestionTxtMsgs;

public class SuggestionTxtMsg {

    String msgContent;
    long count;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SuggestionTxtMsg(String msgContent, long count, String key) {
        this.msgContent = msgContent;
        this.count = count;
        this.key = key;
    }

    public SuggestionTxtMsg() {
    }

    public SuggestionTxtMsg(String msgContent, long count) {
        this.msgContent = msgContent;
        this.count = count;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
