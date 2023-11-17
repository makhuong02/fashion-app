package com.group25.ecommercefashionapp.data;

public class CategoryItem extends Item {
    private final int imgID;

    public CategoryItem(String category_name, int imgID) {
        super(category_name);
        this.imgID = imgID;
    }

    public int getImgID() {
        return imgID;
    }

}
