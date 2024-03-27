package com.group25.ecommercefashionapp.repository;

import com.group25.ecommercefashionapp.api.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductRepositoryB {
    private static final String BASE_URL = "http://192.168.1.11:8080/api/v1/";
    private static ProductRepositoryB instance;
    private final ApiService apiService;

    private ProductRepositoryB() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized ProductRepositoryB getInstance() {
        if (instance == null) {
            instance = new ProductRepositoryB();
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
