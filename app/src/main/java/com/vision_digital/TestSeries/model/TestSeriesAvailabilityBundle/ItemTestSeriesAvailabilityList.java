package com.vision_digital.TestSeries.model.TestSeriesAvailabilityBundle;

public class ItemTestSeriesAvailabilityList {

    String id, courseType, title, startTime, endTime, shortDesc, subscriptionValidity, categoryName,
            actualPrice, sellingPrice, testSchedulePDFURl , categoryImage, sub_category_name ,total_test_count;


    public ItemTestSeriesAvailabilityList(String id, String courseType, String title, String startTime, String endTime, String shortDesc, String subscriptionValidity, String categoryName, String actualPrice, String sellingPrice, String testSchedulePDFURl,
                                          String categoryImage, String sub_category_name, String total_test_count) {
        this.id = id;
        this.courseType = courseType;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shortDesc = shortDesc;
        this.subscriptionValidity = subscriptionValidity;
        this.categoryName = categoryName;
        this.actualPrice = actualPrice;
        this.sellingPrice = sellingPrice;
        this.testSchedulePDFURl = testSchedulePDFURl;
        this.categoryImage = categoryImage;
        this.sub_category_name = sub_category_name;
        this.total_test_count = total_test_count;
    }

    public ItemTestSeriesAvailabilityList(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getSubscriptionValidity() {
        return subscriptionValidity;
    }

    public void setSubscriptionValidity(String subscriptionValidity) {
        this.subscriptionValidity = subscriptionValidity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getTestSchedulePDFURl() {
        return testSchedulePDFURl;
    }

    public void setTestSchedulePDFURl(String testSchedulePDFURl) {
        this.testSchedulePDFURl = testSchedulePDFURl;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getTotal_test_count() {
        return total_test_count;
    }

    public void setTotal_test_count(String total_test_count) {
        this.total_test_count = total_test_count;
    }
}
