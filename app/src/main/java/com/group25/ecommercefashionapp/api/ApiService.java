package com.group25.ecommercefashionapp.api;

import com.group25.ecommercefashionapp.data.CategoryItem;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.ProductColor;
import com.group25.ecommercefashionapp.data.ProductImage;
import com.group25.ecommercefashionapp.data.ProductSize;
import com.group25.ecommercefashionapp.data.UserProfile;
import com.group25.ecommercefashionapp.status.LoginStatus;
import com.group25.ecommercefashionapp.status.RegisterStatus;
import com.group25.ecommercefashionapp.ui.activity.LoginActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // Link API: http://localhost:8080/api/v1/
    String LOCALHOST = "192.168.1.11";
    String PORT = "8080";
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://" + LOCALHOST + ":" + PORT + "/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);

    @POST("auth/register")
    Call<RegisterStatus> userRegister(@Body UserProfile userProfile);

    @POST("auth/login")
    Call<LoginStatus> userLogin(@Body LoginActivity.LoginInfo loginInfo);

    @POST("auth/logout")
    Call<LoginStatus> userLogout(@Body LoginActivity.LoginInfo loginInfo);

    @GET("public/products")
    Call<List<Product>> getProducts();

    @GET("public/categories")
    Call<List<CategoryItem>> getCategories();

    @GET("public/products/{id}")
    Call<Product> getProductById(@Path("id") Long productId);

    @GET("public/categories/{id}/products")
    Call<List<Product>> getProductsByCategory(@Path("id") Long categoryId);

    @GET("public/products/{id}/colors")
    Call<List<ProductColor>> getProductColors(@Path("id") Long productId);

    @GET("public/colors/{id}/sizes")
    Call<List<ProductSize>> getColorSizes(@Path("id") Long colorId);

    @GET("public/products/{product-id}/product-images")
    Call<List<ProductImage>> getProductImages(@Path("product-id") Long productId);

}
