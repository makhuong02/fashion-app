package com.group25.ecommercefashionapp.api;

import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiServiceBuilder {
    // Link API: http://localhost:8080/api/v1/

    private static final String LOCALHOST = "192.168.1.5";
    private static final String PORT = "8080";
    public static final String BASE_URL = "http://" + LOCALHOST + ":" + PORT + "/api/v1/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();

    public static ApiService buildService() {
        return retrofit.create(ApiService.class);
    }
}

