package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class CartItem extends Item{
    @SerializedName("productId")
    private final Long productId;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("totalPrice")
    private double totalPrice;
    @SerializedName("selectedColorId")
    private final Long selectedColorId;
    @SerializedName("selectedSizeId")
    private final Long selectedSizeId;

    public CartItem(Long id, Long productId, int quantity, double totalPrice, Long selectedColorId, Long selectedSizeId) {
        super("");
        this.totalPrice = totalPrice;
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.selectedColorId = selectedColorId;
        this.selectedSizeId = selectedSizeId;
    }

    public CartItem(Long productId, int quantity, double totalPrice, Long selectedColorId, Long selectedSizeId) {
        super("");
        this.totalPrice = totalPrice;
        this.id = -1L;
        this.productId = productId;
        this.quantity = quantity;
        this.selectedColorId = selectedColorId;
        this.selectedSizeId = selectedSizeId;
    }

    public CartItem(Long productId, int quantity, Long selectedColorId, Long selectedSizeId) {
        super("");
        this.id = -1L;
        this.productId = productId;
        this.quantity = quantity;
        this.selectedColorId = selectedColorId;
        this.selectedSizeId = selectedSizeId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CartItem) {
            CartItem cartItem = (CartItem) obj;
            return Objects.equals(this.productId, cartItem.productId) &&
                    this.selectedColorId.equals(cartItem.selectedColorId) &&
                    this.selectedSizeId.equals(cartItem.selectedSizeId);
        }
        return false;
    }

    @Override
    public Long getId() {
        return id;
    }
}
