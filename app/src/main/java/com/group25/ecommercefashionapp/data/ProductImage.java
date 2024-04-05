package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

public class ProductImage extends Item {
    @SerializedName("productId")
    private final int productId;
    @SerializedName("imagePath")
    private final String imagePath;
    public ProductImage(int productId, String imagePath, Long imageId) {
        super("");
        this.productId = productId;
        this.imagePath = imagePath;
        this.id = imageId;
    }

    public ProductImage(int productId, String imagePath, String name) {
        super(name);
        this.id = -1L;
        this.imagePath = imagePath;
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public String getImagePath() {
        return imagePath;
    }
}
