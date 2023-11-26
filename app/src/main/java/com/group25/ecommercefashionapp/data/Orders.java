package com.group25.ecommercefashionapp.data;

import java.util.ArrayList;
import java.util.Date;

public class Orders {
    private int orderID;
    private Date date;
    private int totalPrice;
    private ArrayList<Product> products;


    public Orders(int orderID, Date date) {
        this.orderID = orderID;
        this.date = date;
    }



    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
