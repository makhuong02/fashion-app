package com.group25.ecommercefashionapp.data;

abstract public class Item {
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
