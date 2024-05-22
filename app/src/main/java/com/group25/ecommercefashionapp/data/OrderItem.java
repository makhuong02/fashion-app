package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

public class OrderItem extends Item{
    @SerializedName("colorId")
    private Long colorId;
    @SerializedName("sizeId")
    private Long sizeId;
    @SerializedName("productId")
    private Long productId;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("totalPrice")
    private double totalPrice;

    public OrderItem(Long productId, int quantity, double totalPrice, Long colorId, Long sizeId) {
        super("");
        this.totalPrice = totalPrice;
        this.id = -1L;
        this.productId = productId;
        this.quantity = quantity;
        this.colorId = colorId;
        this.sizeId = sizeId;
    }

    // Getters and setters
    public Long getProductId() {
        return productId;
    }
    public int getQuantity() {
        return quantity;
    }
    public Double getTotalPrice() {
        return totalPrice;
    }
    public Long getColorId() {
        return colorId;
    }
    public Long getSizeId() {
        return sizeId;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId=" + productId +
                ", colorId=" + colorId +
                ", sizeId=" + sizeId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
