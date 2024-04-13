package com.group25.ecommercefashionapp.api;

import com.google.gson.JsonElement;
import com.group25.ecommercefashionapp.data.*;
import com.group25.ecommercefashionapp.status.LoginStatus;
import com.group25.ecommercefashionapp.ui.activity.LoginActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/register")
    Call<JsonElement> userRegister(@Body UserProfile userProfile);

    @POST("auth/login")
    Call<LoginStatus> userLogin(@Body LoginActivity.LoginInfo loginInfo);

    @POST("auth/logout")
    Call<LoginStatus> userLogout(@Body LoginActivity.LoginInfo loginInfo);

    @POST("auth/validate-token")
    Call<Boolean> validateToken(@Header("Authorization") String token);

    @GET("products")
    Call<JsonElement> getProducts();

    @GET("categories")
    Call<List<CategoryItem>> getCategories();

    @GET("products/{id}")
    Call<JsonElement> getProductById(@Path("id") Long productId);

    @GET("categories/{id}/products")
    Call<List<Product>> getProductsByCategory(@Path("id") Long categoryId);

    @GET("product-quantities/{productId}")
    Call<List<ProductQuantity>> getProductQuantities(@Path("productId") Long productId);

    @GET("products/{id}/colors")
    Call<List<ProductColor>> getProductColors(@Path("id") Long productId);

    @GET("colors/{id}/sizes")
    Call<List<ProductSize>> getColorSizes(@Path("id") Long colorId);

    @GET("products/{product-id}/product-images")
    Call<List<ProductImage>> getProductImages(@Path("product-id") Long productId);

    @POST("favorite-products/{product-id}")
    Call<JsonElement> addFavoriteProduct(@Path("product-id") Long productId, @Header("Authorization") String token);

    @GET("favorite-products")
    Call<List<Product>> getFavoriteProducts(@Header("Authorization") String token);

    @DELETE("favorite-products/{product-id}")
    Call<Void> removeFavoriteProduct(@Path("product-id") Long productId, @Header("Authorization") String token);

    @GET("users")
    Call<UserProfile> getUserInfo(@Header("Authorization") String token);

}
