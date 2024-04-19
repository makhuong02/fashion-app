package com.group25.ecommercefashionapp.data;

import java.util.ArrayList;
import java.util.List;

public class UserInteraction {
    private List<Product> favoriteList;
    private List<CartItem> cartList;
    private List<OrderHistoryItem> orderList;
    public UserInteraction() {
        this.favoriteList = new ArrayList<>();
        this.cartList = new ArrayList<>();
        this.orderList = new ArrayList<>();
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
    public int getCartTotalPrice() {
        int totalPrice = 0;
        for(CartItem cartItem : cartList) {
//            totalPrice += cartItem.getQuantity() * getMainActivityInstance().productRepository.getProductById(cartItem.getProductId()).getPrice()*0.9f;
        }
        return totalPrice;
    }
    public void clearCart() {
        cartList.clear();
    }

    public List<CartItem> shallowCopyCartList(List<CartItem> cartList) {
        List<CartItem> shallowCopy = new ArrayList<>();
        for(CartItem cartItem : cartList) {
            shallowCopy.add(new CartItem(cartItem.getProductId(), cartItem.getQuantity(),cartItem.getTotalPrice(), cartItem.getSelectedColorId(), cartItem.getSelectedSizeId()));
        }
        return shallowCopy;
    }

    public void setOrderList(List<OrderHistoryItem> orderList) {
        this.orderList = orderList;
    }

    public void addOrder(OrderHistoryItem orderHistoryItem) {
        orderList.add(orderHistoryItem);
    }

    public void removeOrder(OrderHistoryItem orderHistoryItem) {
        orderList.remove(orderHistoryItem);
    }

    public List<OrderHistoryItem> getOrderList() {
        return orderList;
    }

}