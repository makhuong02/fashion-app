package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

public class OrderItem extends Item{
    @SerializedName("selectedColorId")
    private Long selectedColorId;
    @SerializedName("selectedSizeId")
    private Long selectedSizeId;
    @SerializedName("productId")
    private Long productId;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("totalPrice")
    private double totalPrice;

    public OrderItem(Long productId, int quantity, double totalPrice, Long selectedColorId, Long selectedSizeId) {
        super("");
        this.totalPrice = totalPrice;
        this.id = -1L;
        this.productId = productId;
        this.quantity = quantity;
        this.selectedColorId = selectedColorId;
        this.selectedSizeId = selectedSizeId;
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
    public Long getSelectedColorId() {
        return selectedColorId;
    }
    public Long getSelectedSizeId() {
        return selectedSizeId;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId=" + productId +
                ", selectedColorId=" + selectedColorId +
                ", selectedSizeId=" + selectedSizeId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
