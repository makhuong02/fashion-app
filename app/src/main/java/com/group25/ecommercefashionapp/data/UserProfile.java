package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

public class UserProfile {
    @SerializedName("ID") private int id;
    @SerializedName("Email") private String email;
    @SerializedName("Username") private String username;
    private String password;
    @SerializedName("Phone") private String phoneNumber;

    public UserProfile(String email, String username, String password, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
