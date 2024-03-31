package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

abstract public class Item {
    @SerializedName(value = "name")
    protected final String name;
    @SerializedName(value = "id")
    protected Long id;

    protected Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public Long getId() {
        return id;
    }
}
