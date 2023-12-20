package com.group25.ecommercefashionapp.data;

import java.util.ArrayList;
import java.util.List;

public class UserInteraction {
    private List<Product> favoriteList;
    private List<CartItem> cartList;
    private List<Product> orderList;
    private List<Product> productList;

    public UserInteraction() {
        this.favoriteList = new ArrayList<>();
        this.cartList = new ArrayList<>();
        this.orderList = new ArrayList<>();
        this.productList = new ArrayList<>();
    }
    public void setFavoriteList(List<Product> favoriteList) {
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

    public void setCartList(List<CartItem> cartList) {
        this.cartList = cartList;
    }
    public void addCart(CartItem cartItem) {
        if(cartList.contains(cartItem)) {
            cartList.get(cartList.indexOf(cartItem)).setQuantity(cartItem.getQuantity());
        }
        else
            cartList.add(cartItem);
    }
    public void removeCart(CartItem cartItem) {
        cartList.remove(cartItem);
    }
    public List<CartItem> getCartList() {
        return cartList;
    }

}