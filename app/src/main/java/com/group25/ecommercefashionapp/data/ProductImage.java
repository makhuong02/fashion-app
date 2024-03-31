package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

public class ProductImage extends Item {
    @SerializedName("productId")
    private final int product_id;
    @SerializedName("imagePath")
    private final String image_name;
    public ProductImage(int product_id, String image_name, Long imageId) {
        super("");
        this.product_id = product_id;
        this.image_name = image_name;
        this.id = imageId;
    }

    public ProductImage(int product_id, String image_name, String name) {
        super(name);
        this.id = -1L;
        this.image_name = image_name;
        this.product_id = product_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getImage_name() {
        return image_name;
    }
}
