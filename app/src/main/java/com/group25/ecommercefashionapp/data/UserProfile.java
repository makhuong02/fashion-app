package com.group25.ecommercefashionapp.data;

import com.google.gson.annotations.SerializedName;

public class UserProfile {
    @SerializedName("email") private String email;
    @SerializedName("username") private String username;
    @SerializedName("role")
    private String role;
    private String password;
    @SerializedName("phoneNum") private String phoneNumber;

    public UserProfile(String email, String username, String password, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = "USER";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
