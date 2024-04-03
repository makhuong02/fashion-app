package com.group25.ecommercefashionapp.cache;

import com.group25.ecommercefashionapp.data.UserProfile;

import java.util.HashMap;
import java.util.Map;

public class UserCache {
    private static UserCache instance;
    private final Map<String, UserProfile> cache;

    private UserCache() {
        cache = new HashMap<>();
    }

    public static synchronized UserCache getInstance() {
        if (instance == null) {
            instance = new UserCache();
        }
        return instance;
    }

    public void addUser(String email, UserProfile user) {
        cache.put(email, user);
    }

    public UserProfile getUser(String email) {
        return cache.get(email);
    }

    public boolean containsUser(String email) {
        return cache.containsKey(email);
    }

    public void clearCache() {
        cache.clear();
    }
}
