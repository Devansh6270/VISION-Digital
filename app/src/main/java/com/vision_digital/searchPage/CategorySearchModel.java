package com.vision_digital.searchPage;

public class CategorySearchModel {
    String categoryName;
    boolean isSelected;

    public CategorySearchModel(String categoryName, boolean isSelected) {
        this.categoryName = categoryName;
        this.isSelected = isSelected;
    }
    public CategorySearchModel(){

    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
