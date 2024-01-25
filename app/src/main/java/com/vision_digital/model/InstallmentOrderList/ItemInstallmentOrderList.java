package com.vision_digital.model.InstallmentOrderList;

public class ItemInstallmentOrderList {
    String id, order_id, amount, paid_date, installment, payment_type, status;

    public ItemInstallmentOrderList(String id, String order_id, String amount, String paid_date, String installment, String payment_type, String status) {
        this.id = id;
        this.order_id = order_id;
        this.amount = amount;
        this.paid_date = paid_date;
        this.installment = installment;
        this.payment_type = payment_type;
        this.status = status;
    }
    public ItemInstallmentOrderList(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(String paid_date) {
        this.paid_date = paid_date;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
