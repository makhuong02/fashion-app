package com.group25.ecommercefashionapp.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class OrderHistoryItem extends Item {
    private final Set<OrderItem> orderItems;
    private final String orderDate;
    private String orderStatus;
    private final String orderClass;
    private final String pickupPlace;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String deliveryOption;
    private final String phoneNumber;
    private final double totalPrice;

    public OrderHistoryItem(Set<OrderItem> orderItems, String pickupPlace, String firstName, String lastName, String address, String deliveryOption, String phoneNumber, int totalPrice) {
        super("");
        this.pickupPlace = pickupPlace;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.deliveryOption = deliveryOption;
        this.phoneNumber = phoneNumber;
        this.totalPrice = totalPrice;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm:ss", Locale.getDefault());
        this.orderDate = dateFormat.format(new Date());
        this.orderStatus = "Pending";
        this.orderItems = orderItems;
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

    public Set<OrderItem> getOrderItems() {
        return orderItems;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (!(object instanceof OrderHistoryItem)) return false;
        OrderHistoryItem item = (OrderHistoryItem) object;
        return item.getOrderDate().equals(this.getOrderDate()) && item.getPhoneNumber().equals(this.getPhoneNumber());
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getOrderTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
