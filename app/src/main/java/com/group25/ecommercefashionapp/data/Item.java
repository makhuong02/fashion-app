package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

abstract public class Item {
    @SerializedName(value = "Name", alternate = "CategoryName")
    protected final String name;
    protected int id;

    protected Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
}
