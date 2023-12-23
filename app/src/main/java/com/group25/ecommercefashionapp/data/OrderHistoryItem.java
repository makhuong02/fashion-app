package com.group25.ecommercefashionapp.data;

import java.util.List;

public class OrderHistoryItem extends Item {
    private List<CartItem> cartList;
    private String orderDate;
    private String orderStatus;
    private String orderClass;
    private String pickupPlace;
    private String firstName;
    private String lastName;
    private String address;
    private String deliveryOption;
    private int totalPrice;
    public OrderHistoryItem(List<CartItem> cartList, String pickupPlace, String firstName, String lastName, String address, String deliveryOption, int totalPrice) {
        super("");
        this.pickupPlace = pickupPlace;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.deliveryOption = deliveryOption;
        this.totalPrice = totalPrice;
        this.orderDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());
        this.orderStatus = "Pending";
        this.cartList = cartList;
        this.orderClass = "Online Order";
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderClass() {
        return orderClass;
    }

    public String getPickupPlace() {
        return pickupPlace;
    }

    public List<CartItem> getCartList() {
        return cartList;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getAddress() {
        return address;
    }
    public String getDeliveryOption() {
        return deliveryOption;
    }

    @Override
    public boolean equals(Object object){
        if(object == null) return false;
        if(!(object instanceof OrderHistoryItem)) return false;
        OrderHistoryItem item = (OrderHistoryItem) object;
        return item.getOrderDate().equals(this.getOrderDate());
    }

    public int getTotalPrice() {
        return totalPrice;
    }

}
