package com.group25.ecommercefashionapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.status.UserStatus;

import java.lang.reflect.Type;
import java.util.List;

public class MySharedPreferences {
    private static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    private static final String KEY_LOGIN_STATUS = "KEY_LOGIN";
    private static final String KEY_FAVORITE_LIST = "KEY_FAVORITE_LIST";
    private static final String KEY_ADDRESS = "KEY_ADDRESS";
    private static final String KEY_FIRST_NAME = "KEY_FIRST_NAME";
    private static final String KEY_LAST_NAME = "KEY_LAST_NAME";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public MySharedPreferences(Context context) {
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

    public void saveUserFavoriteList(List<Product> favoriteList) {
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

    public void saveUserAddress(String address) {
        editor.putString(KEY_ADDRESS, address);
        editor.apply();
    }

    public String getUserAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, "");
    }

    public void saveUserFirstName(String firstName) {
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.apply();
    }

    public String getUserFirstName() {
        return sharedPreferences.getString(KEY_FIRST_NAME, "");
    }

    public void saveUserLastName(String lastName) {
        editor.putString(KEY_LAST_NAME, lastName);
        editor.apply();
    }

    public String getUserLastName() {
        return sharedPreferences.getString(KEY_LAST_NAME, "");
    }


}
