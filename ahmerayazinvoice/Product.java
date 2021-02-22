package com.example.ahmerayazinvoice;

import java.util.List;

public class Product {

    public String productname;
    public String quantity;
    public String price;
    public String total;
    public String keyvalue;

    public Product() {
    }

    public Product(String productname, String quantity, String price, String total, String keyvalue) {
        this.productname = productname;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.keyvalue = keyvalue;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
