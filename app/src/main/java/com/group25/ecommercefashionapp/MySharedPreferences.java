package com.group25.ecommercefashionapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.ui.Activity.LoginActivity;

public class MySharedPreferences {
    private static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    private static final String KEY_LOGIN_STATUS = "KEY_LOGIN";
    private static final String KEY_EMAIL = "KEY_EMAIL";
    private static final String KEY_PASSWORD = "KEY_PASSWORD";

    private Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public MySharedPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void updateUserLoginStatus() {
        boolean isLoggedIn = UserStatus._isLoggedIn;
        editor.putBoolean(KEY_LOGIN_STATUS, isLoggedIn);
        if (!isLoggedIn) {
            editor.remove(KEY_EMAIL)
                    .remove(KEY_PASSWORD);
        }
        editor.apply();
    }

    public void putUserLoginInfo (LoginActivity.LoginInfo loginInfo) {
        editor.putString(KEY_EMAIL, loginInfo.email)
                .putString(KEY_PASSWORD, loginInfo.password);
        editor.apply();
    }

    public LoginActivity.LoginInfo getUserLoginInfo() {
        LoginActivity loginActivity = new LoginActivity();
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String password = sharedPreferences.getString(KEY_PASSWORD, "");
        return loginActivity.new LoginInfo(email, password);
    }

    public boolean getUserLoginStatus() {
        try {
            return sharedPreferences.getBoolean(KEY_LOGIN_STATUS, false);
        }
        catch (Exception e) {
            return false;
        }
    }
}
