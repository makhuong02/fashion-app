package com.group25.ecommercefashionapp;

import java.util.ArrayList;

public class TestProductItem {
    private String name;
    private int price;
    private int image;

    public TestProductItem(String name, int price, int image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }

    public static ArrayList<TestProductItem> getProduct() {
        ArrayList<TestProductItem> items = new ArrayList<>();
        items.add(new TestProductItem("Đây là sản phẩm 1 có nhiều lượt bán nhất", 500000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 2 có nhiều lượt bán nhất", 580000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 3 có nhiều lượt bán nhất", 750000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 4 có nhiều lượt bán nhất", 800000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 5 có nhiều lượt bán nhất", 200000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 6 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 7 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 8 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 9 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 10 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 11 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 12 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 13 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 14 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));
        items.add(new TestProductItem("Đây là sản phẩm 15 có nhiều lượt bán nhất", 330000, R.drawable.tshirt));

        return items;
    }
}
