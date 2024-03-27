package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

public class CategoryItem extends Item {
    @SerializedName("imageUrl")
    private final String imageUrl;

    public CategoryItem(String category_name, String imageUrl) {
        super(category_name);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
