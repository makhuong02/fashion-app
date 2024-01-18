package com.group25.ecommercefashionapp.data;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private String phoneNumber;
    private int totalPrice;

    public OrderHistoryItem(List<CartItem> cartList, String pickupPlace, String firstName, String lastName, String address, String deliveryOption, String phoneNumber, int totalPrice) {
        super("");
        this.pickupPlace = pickupPlace;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.deliveryOption = deliveryOption;
        this.phoneNumber = phoneNumber;
        this.totalPrice = totalPrice;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date());
        this.orderDate = formattedDate;
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

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getCartTotalPrice() {
        int totalPrice = 0;
        for (CartItem cartItem : cartList) {
            totalPrice += cartItem.getQuantity() * getMainActivityInstance().productRepository.getProductById(cartItem.getProductId()).getPrice()*0.9f;
        }
        return totalPrice;
    }

}
