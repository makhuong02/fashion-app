package com.group25.ecommercefashionapp.data;


import java.util.ArrayList;
import java.util.List;

public class Product extends Item {
    private final String description;
    private final Integer price;
    private final String category;
    private Integer availableQuantity;
    private final List<ProductImage> imageList = new ArrayList<>();
    private final List<ProductColor> colorList = new ArrayList<>();
    private final List<ProductSize> sizeList = new ArrayList<>();

    public Product(Integer id, String name, String description, Integer price, String category, Integer availableQuantity) {
        super(name);
        this.id = id;
        this.description = description;
        this.price = price;
        this.category = category;
        this.availableQuantity = availableQuantity;
    }

    public Product(String description, Integer price, String category) {
        super("");
        this.description = description;
        this.price = price;
        this.id = -1;
        this.category = category;
        this.availableQuantity = 100;

    }

    public Product(String name, String description, Integer price, String category) {
        super(name);
        this.description = description;
        this.price = price;
        this.id = -1;
        this.category = category;
        this.availableQuantity = 100;
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

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }
    public void setAvailableQuantity(Integer quantity) {
        availableQuantity = quantity;
    }

    public int getId() {
        return id;
    }

    public List<String> getColors() {
        List<String> colors = new ArrayList<>();
        for (ProductColor color : colorList) {
            colors.add(color.getHexColor());
        }
        return colors;
    }

    public List<ProductColor> getColorList() {
        return colorList;
    }

    public void addColor(ProductColor color) {
        colorList.add(color);
    }

    public void addColors(List<ProductColor> colors) {
        colorList.addAll(colors);
    }

    public List<ProductSize> getSizeList() {
        return sizeList;
    }

    public List<String> getSizes() {
        List<String> sizes = new ArrayList<>();
        for (ProductSize size : sizeList) {
            sizes.add(size.getName());
        }
        return sizes;
    }

    public void addSize(ProductSize size) {
        sizeList.add(size);
    }

    public void addSizes(List<ProductSize> sizes) {
        sizeList.addAll(sizes);
    }

    @Override
    public boolean equals(Object object){
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Product product = (Product) object;
        return id == product.id;
    }
    public String getSizeRange() {
        String sizeRange = "";
        if (sizeList.size() > 0) {
            sizeRange = sizeList.get(0).getName();
            if (sizeList.size() > 1) {
                sizeRange += " - " + sizeList.get(sizeList.size() - 1).getName();
            }
        }
        return sizeRange;
    }
    public void addImages(List<ProductImage> images) {
        imageList.addAll(images);
    }

}
