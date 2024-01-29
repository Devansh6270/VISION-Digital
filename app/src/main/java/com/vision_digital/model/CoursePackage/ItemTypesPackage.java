package com.vision_digital.model.CoursePackage;

public class ItemTypesPackage {

    String packageId;
    String productId;
    String productType;
    String productName;
    String productImage;

    String status;


    public ItemTypesPackage() {
    }

    public ItemTypesPackage(String packageId,String productId, String productType, String productName, String productImage, String status) {
        this.packageId=packageId;
        this.productId = productId;
        this.productType = productType;
        this.productName = productName;
        this.productImage = productImage;
        this.status=status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
}
