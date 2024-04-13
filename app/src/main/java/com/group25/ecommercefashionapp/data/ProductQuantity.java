package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

public class ProductQuantity {
    @SerializedName("productId")
    private final Long productId;
    @SerializedName("quantity")
    private final Integer quantity;
    @SerializedName("id")
    private final Long quantityId;
    @SerializedName("size")
    private final ProductSize size;
    @SerializedName("color")
    private final ProductColor color;

    public ProductQuantity(Long productId, Integer quantity, Long quantityId, ProductSize size, ProductColor color) {
        this.productId = productId;
        this.quantityId = quantityId;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantityId() {
        return quantityId;
    }

    public ProductSize getSize() {
        return size;
    }

    public ProductColor getColor() {
        return color;
    }
}
