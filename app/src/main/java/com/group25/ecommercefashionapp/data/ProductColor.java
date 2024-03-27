package com.group25.ecommercefashionapp.data;

public class ProductColor extends Item {
    private final Long product_id;
    private final String hexColor;
    private final Long colorId;

    public ProductColor(Long product_id, String hexColor, Long colorId) {
        super("");
        this.product_id = product_id;
        this.hexColor = hexColor;
        this.colorId = colorId;
    }

    public ProductColor(Long product_id, String hexColor, String name) {
        super(name);
        this.colorId = -1L;
        this.hexColor = hexColor;
        this.product_id = product_id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public String getHexColor() {
        return hexColor;
    }

    public Long getColorId() {
        return colorId;
    }

}
