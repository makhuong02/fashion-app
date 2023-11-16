package com.group25.ecommercefashionapp;

import java.util.ArrayList;

public class CategoryItem {
    private String category_name;
    private int imgID;

    public CategoryItem(String category_name, int imgID) {
        this.category_name = category_name;
        this.imgID = imgID;
    }

    public String getCategory_name() {
        return category_name;
    }

    public int getImgID() {
        return imgID;
    }

    public static ArrayList<CategoryItem> getCategory() {
        ArrayList<CategoryItem> items = new ArrayList<>();
        items.add(new CategoryItem("Áo thun", R.drawable.tshirt));
        items.add(new CategoryItem("Váy", R.drawable.skirt));
        items.add(new CategoryItem("Quần", R.drawable.jeans));
        items.add(new CategoryItem("Áo khoác", R.drawable.jacket));
        items.add(new CategoryItem("Hoodie", R.drawable.hoodie));
        items.add(new CategoryItem("Giày", R.drawable.sneakers));
        items.add(new CategoryItem("Đồng hồ", R.drawable.watch));
        items.add(new CategoryItem("Bóp", R.drawable.wallet));
        items.add(new CategoryItem("Túi xách", R.drawable.handbag));

        return items;
    }
}
