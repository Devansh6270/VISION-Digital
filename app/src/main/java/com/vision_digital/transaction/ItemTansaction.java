package com.vision_digital.transaction;

public class ItemTansaction {
    String id;
    String orderId;
    String amount;
    String status;
    String date;

    public ItemTansaction(String id, String orderId, String amount, String status, String date) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.date = date;
    }

    public ItemTansaction(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
