package com.group25.ecommercefashionapp.data;

public class ProductColor extends Item {
    private final int product_id;
    private final String path;
    private final int colorId;
    private String name;

    public ProductColor(int product_id, String path, int colorId) {
        super("");
        this.product_id = product_id;
        this.path = path;
        this.colorId = colorId;
    }

    public ProductColor(int product_id, String path, String name) {
        super(name);
        this.colorId = -1;
        this.path = path;
        this.product_id = product_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getPath() {
        return path;
    }

    public int getColorId() {
        return colorId;
    }

}
