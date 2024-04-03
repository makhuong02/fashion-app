package com.group25.ecommercefashionapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.OrderHistoryItem;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.status.UserStatus;

import java.lang.reflect.Type;
import java.util.List;

public class MySharedPreferences {
    private static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    private static final String KEY_LOGIN_STATUS = "KEY_LOGIN";
    private static final String KEY_FAVORITE_LIST = "KEY_FAVORITE_LIST";
    private static final String KEY_CART_LIST = "KEY_CART_LIST";
    private static final String KEY_ADDRESS = "KEY_ADDRESS";
    private static final String KEY_FIRST_NAME = "KEY_FIRST_NAME";
    private static final String KEY_LAST_NAME = "KEY_LAST_NAME";
    private static final String KEY_ORDER_LIST = "KEY_ORDER_LIST";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private Context context;
    private static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public MySharedPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveTokens(String accessToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }


    public void clearTokens() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_ACCESS_TOKEN);
        editor.apply();
    }

    public void updateUserLoginStatus() {
        boolean isLoggedIn = UserStatus._isLoggedIn;
        editor.putBoolean(KEY_LOGIN_STATUS, isLoggedIn);
        if (!isLoggedIn) {
            editor.remove(KEY_ACCESS_TOKEN);
        }
        editor.apply();
    }


    public boolean getUserLoginStatus() {
        try {
            return sharedPreferences.getBoolean(KEY_LOGIN_STATUS, false);
        }
        catch (Exception e) {
            return false;
        }
    }
    public void putUserFavoriteList(List<Product> favoriteList) {
        Gson gson = new Gson();
        String json = gson.toJson(favoriteList);
        editor.putString(KEY_FAVORITE_LIST, json);
        editor.apply();
    }

    // Retrieve the user's favorite list from SharedPreferences
    public List<Product> getUserFavoriteList() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_FAVORITE_LIST, "");
        Type type = new TypeToken<List<Product>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void putUserCartList(List<CartItem> cartList) {
        Gson gson = new Gson();
        String json = gson.toJson(cartList);
        editor.putString(KEY_CART_LIST, json);
        editor.apply();
    }

    // Retrieve the user's cart list from SharedPreferences
    public List<CartItem> getUserCartList() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_CART_LIST, "");
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void putUserAddress(String address) {
        editor.putString(KEY_ADDRESS, address);
        editor.apply();
    }

    public String getUserAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, "");
    }

    public void putUserFirstName(String firstName) {
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.apply();
    }

    public String getUserFirstName() {
        return sharedPreferences.getString(KEY_FIRST_NAME, "");
    }

    public void putUserLastName(String lastName) {
        editor.putString(KEY_LAST_NAME, lastName);
        editor.apply();
    }

    public String getUserLastName() {
        return sharedPreferences.getString(KEY_LAST_NAME, "");
    }

    public void putUserOrderList(List<OrderHistoryItem> orderList) {
        Gson gson = new Gson();
        String json = gson.toJson(orderList);
        editor.putString(KEY_ORDER_LIST, json);
        editor.apply();
    }

    public List<OrderHistoryItem> getUserOrderList() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_ORDER_LIST, "");
        Type type = new TypeToken<List<OrderHistoryItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void clearAll() {
        editor.clear();
        editor.apply();
    }

}
