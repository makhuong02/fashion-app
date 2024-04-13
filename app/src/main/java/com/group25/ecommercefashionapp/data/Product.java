package com.group25.ecommercefashionapp.data;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Product extends Item {
    @SerializedName("description")
    private final String description;
    @SerializedName("price")
    private final Integer price;
    @SerializedName("category")
    private final String category;
    @SerializedName("images")
    private final List<ProductImage> imageList = new ArrayList<>();
    public Product(Long id, String name, String description, Integer price, String category) {
        super(name);
        this.id = id;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public Product(String description, Integer price, String category) {
        super("");
        this.description = description;
        this.price = price;
        this.id = -1L;
        this.category = category;
    }

    public Product(String name, String description, Integer price, String category) {
        super(name);
        this.description = description;
        this.price = price;
        this.id = -1L;
        this.category = category;
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

    public List<ProductImage> getImageList() {
        return imageList;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object object){
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Product product = (Product) object;
        return Objects.equals(id, product.id);
    }
//    public String getSizeRange() {
//        try {
//            String sizeRange = "";
//            if (sizeList.size() > 0) {
//                sizeRange = sizeList.get(0).getName();
//                if (sizeList.size() > 1) {
//                    sizeRange += " - " + sizeList.get(sizeList.size() - 1).getName();
//                }
//            }
//            return sizeRange;
//        }catch (Exception e) {
//            return "";
//        }
//    }
    public void addImages(List<ProductImage> images) {
        imageList.addAll(images);
    }

}
