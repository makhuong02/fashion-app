package com.group25.ecommercefashionapp.data;

import com.group25.ecommercefashionapp.R;

import java.util.ArrayList;

public class Product {
    private final int id;
    private final String name;
    private final String description;
    private final Integer price;
    private final int imageId;
    private final String category;
    private final Integer availableQuantity;

    public Product(Integer id, String name, String description, Integer price, int imageId, String category, Integer availableQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageId = imageId;
        this.category = category;
        this.availableQuantity = availableQuantity;
    }

    public Product(String description, Integer price, int imageId) {
        this.description = description;
        this.price = price;
        this.imageId = imageId;
        this.id = -1;
        this.name = "";
        this.category = "";
        this.availableQuantity = 0;

    }

    public static ArrayList<Product> getProduct() {
        ArrayList<Product> items = new ArrayList<>();
        items.add(new Product("Đây là sản phẩm 1 có nhiều lượt bán nhất", 1500000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 2 có nhiều lượt bán nhất", 580000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 3 có nhiều lượt bán nhất", 750000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 4 có nhiều lượt bán nhất", 800000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 5 có nhiều lượt bán nhất", 200000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 6 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 7 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 8 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 9 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 10 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 11 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 12 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 13 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 14 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new Product("Đây là sản phẩm 15 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));

        return items;
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
