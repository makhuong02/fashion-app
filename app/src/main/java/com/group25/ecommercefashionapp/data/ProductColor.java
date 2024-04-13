package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

public class ProductColor extends Item {
    @SerializedName("hexCode")
    private final String hexCode;

    public ProductColor(String hexCode) {
        super("");
        this.hexCode = hexCode;
    }

    public ProductColor(String hexCode, String name) {
        super(name);
        this.hexCode = hexCode;
    }

    public String getHexCode() {
        return hexCode;
    }
}
