package com.group25.ecommercefashionapp.data;

public class ProductImage extends Item {
    private final int product_id;
    private final int image_int_id;
    private final int image_id;

    public ProductImage(int product_id, int image_int_id, int image_id) {
        super("");
        this.product_id = product_id;
        this.image_int_id = image_int_id;
        this.image_id = image_id;
    }

    public ProductImage(int product_id, int image_int_id, String name) {
        super(name);
        this.image_id = -1;
        this.image_int_id = image_int_id;
        this.product_id = product_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getImage_int_id() {
        return image_int_id;
    }
    public int getImage_id() {
        return image_id;
    }

}
