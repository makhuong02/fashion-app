package com.group25.ecommercefashionapp.data;

import java.util.ArrayList;
import java.util.List;

public class UserInteraction {
    private List<Product> favoriteList;
    private List<Product> cartList;
    private List<Product> orderList;
    private List<Product> productList;

    public UserInteraction() {
        this.favoriteList = new ArrayList<>();
        this.cartList = new ArrayList<>();
        this.orderList = new ArrayList<>();
        this.productList = new ArrayList<>();
    }
    public UserInteraction(List<Product> favoriteList) {
        this.favoriteList = favoriteList;
    }
    public void addFavorite(Product product) {
        favoriteList.add(product);
    }
    public void removeFavorite(Product product) {
        favoriteList.remove(product);
    }
    public List<Product> getFavoriteList() {
        return favoriteList;
    }

}