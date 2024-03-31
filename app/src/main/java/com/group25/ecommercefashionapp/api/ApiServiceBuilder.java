package com.group25.ecommercefashionapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceBuilder {
    // Link API: http://localhost:8080/api/v1/

    private static final String LOCALHOST = "192.168.1.11";
    private static final String PORT = "8080";
    public static final String BASE_URL = "http://" + LOCALHOST + ":" + PORT + "/api/v1/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiService buildService() {
        return retrofit.create(ApiService.class);
    }
}

