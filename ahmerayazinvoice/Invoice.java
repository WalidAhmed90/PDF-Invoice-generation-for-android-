package com.example.ahmerayazinvoice;

public class Invoice {
    String customerName, phoneNo, totalAmount, createDTM ;

    public Invoice() {
    }

    public Invoice(String customerName, String phoneNo, String totalAmount, String createDTM) {
        this.customerName = customerName;
        this.phoneNo = phoneNo;
        this.totalAmount = totalAmount;
        this.createDTM = createDTM;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreateDTM() {
        return createDTM;
    }

    public void setCreateDTM(String createDTM) {
        this.createDTM = createDTM;
    }
}
