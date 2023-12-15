package com.group25.ecommercefashionapp.api;

import com.group25.ecommercefashionapp.data.UserProfile;
import com.group25.ecommercefashionapp.status.LoginStatus;
import com.group25.ecommercefashionapp.status.RegisterStatus;
import com.group25.ecommercefashionapp.ui.Activity.LoginActivity;
import com.group25.ecommercefashionapp.ui.Activity.LoginActivity.*;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface ApiService {
    // Link API: http://localhost:8080/api/v1/
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.189:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);

    @POST("auth/register")
    Call<RegisterStatus> userRegister(@Body UserProfile userProfile);

    @POST("auth/login")
    Call<LoginStatus> userLogin(@Body LoginActivity.LoginInfo loginInfo);
}
