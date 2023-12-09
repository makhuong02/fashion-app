package com.group25.ecommercefashionapp.data;

public class ProductColor extends Item {
    private final int product_id;
    private final String hexColor;
    private final int colorId;

    public ProductColor(int product_id, String hexColor, int colorId) {
        super("");
        this.product_id = product_id;
        this.hexColor = hexColor;
        this.colorId = colorId;
    }

    public ProductColor(int product_id, String hexColor, String name) {
        super(name);
        this.colorId = -1;
        this.hexColor = hexColor;
        this.product_id = product_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getHexColor() {
        return hexColor;
    }

    public int getColorId() {
        return colorId;
    }

}
