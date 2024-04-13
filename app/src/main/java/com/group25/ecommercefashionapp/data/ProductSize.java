package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

public class ProductSize extends Item {
    @SerializedName("size")
    private final String size;

    protected ProductSize(String size) {
        super(size);
        this.size = size;
    }

    public String getSize() {
        return size;
    }
}
