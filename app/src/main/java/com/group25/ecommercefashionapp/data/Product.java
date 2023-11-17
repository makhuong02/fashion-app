package com.group25.ecommercefashionapp.data;

public class Product extends Item {
    private final int id;
    private final String description;
    private final Integer price;
    private final int imageId;
    private final String category;
    private final Integer availableQuantity;

    public Product(Integer id, String name, String description, Integer price, int imageId, String category, Integer availableQuantity) {
        super(name);
        this.id = id;
        this.description = description;
        this.price = price;
        this.imageId = imageId;
        this.category = category;
        this.availableQuantity = availableQuantity;
    }

    public Product(String description, Integer price, int imageId, String category) {
        super("");
        this.description = description;
        this.price = price;
        this.imageId = imageId;
        this.id = -1;
        this.category = category;
        this.availableQuantity = 0;

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getImage() {
        return imageId;
    }

    public String getCategory() {
        return category;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public Integer getId() {
        return id;
    }
}
