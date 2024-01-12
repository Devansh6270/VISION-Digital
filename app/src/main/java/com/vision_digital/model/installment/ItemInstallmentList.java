package com.vision_digital.model.installment;

public class ItemInstallmentList {
    String installmentNumber, dueDate, amount, pendingAmount, date;
    String id, fee_id, admno, status, payment_alert, payment_status, payment_history;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFee_id() {
        return fee_id;
    }

    public void setFee_id(String fee_id) {
        this.fee_id = fee_id;
    }

    public String getAdmno() {
        return admno;
    }

    public void setAdmno(String admno) {
        this.admno = admno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_alert() {
        return payment_alert;
    }

    public void setPayment_alert(String payment_alert) {
        this.payment_alert = payment_alert;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_history() {
        return payment_history;
    }

    public void setPayment_history(String payment_history) {
        this.payment_history = payment_history;
    }

    public String getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(String installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ItemInstallmentList() {
    }

    public ItemInstallmentList(String installmentNumber, String dueDate, String amount, String pendingAmount, String date) {
        this.installmentNumber = installmentNumber;
        this.dueDate = dueDate;
        this.amount = amount;
        this.pendingAmount = pendingAmount;
        this.date = date;
    }



}
