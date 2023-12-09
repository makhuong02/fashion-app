package com.group25.ecommercefashionapp.data;

public class ProductSize extends Item {
    final int product_id;
    final int size_id;

    public ProductSize(String name, int product_id, int size_id) {
        super(name);
        this.product_id = product_id;
        this.size_id = size_id;
    }
    public ProductSize(String name, int product_id) {
        super(name);
        this.size_id = -1;
        this.product_id = product_id;
    }
    public int getProduct_id() {
        return product_id;
    }
}
