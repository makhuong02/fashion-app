package com.group25.ecommercefashionapp.repository;

import com.group25.ecommercefashionapp.api.ApiService;
import com.group25.ecommercefashionapp.api.ApiServiceBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductRepository {
    private static final String BASE_URL = "http://192.168.1.11:8080/api/v1/";
    private static ProductRepository instance;
    private final ApiService apiService;

    private ProductRepository() {
        apiService = ApiServiceBuilder.buildService();
    }

    public static synchronized ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
