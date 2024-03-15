package com.vision_classes.TestSeries.model.objectiveQuestion.options;

public class ItemOption {
    String option;
    String optNo;
    String optionImageUrl;
    boolean selected = false;

    public ItemOption(String option, String optNo, String optionImageUrl, boolean selected) {
        this.option = option;
        this.optNo = optNo;
        this.optionImageUrl = optionImageUrl;
        this.selected = selected;
    }

    public String getOptNo() {
        return optNo;
    }

    public void setOptNo(String optNo) {
        this.optNo = optNo;
    }

    // this check selected
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
}
