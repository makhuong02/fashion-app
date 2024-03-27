package com.group25.ecommercefashionapp.status;

import com.group25.ecommercefashionapp.data.UserProfile;

public class LoginStatus {
    public Data data;

    public static class Data {
        public UserProfile user;
        public String access_token;
    }
}
