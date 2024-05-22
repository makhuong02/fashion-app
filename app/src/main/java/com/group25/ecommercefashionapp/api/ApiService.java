package com.group25.ecommercefashionapp.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.CategoryItem;
import com.group25.ecommercefashionapp.data.OrderHistoryItem;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.ProductImage;
import com.group25.ecommercefashionapp.data.ProductQuantity;
import com.group25.ecommercefashionapp.data.UserProfile;
import com.group25.ecommercefashionapp.status.LoginStatus;
import com.group25.ecommercefashionapp.ui.activity.LoginActivity;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
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

    @GET("product-quantities/{productId}/{colorId}/{sizeId}")
    Observable<ProductQuantity> getProductQuantity(@Path("productId") Long productId, @Path("colorId") Long colorId, @Path("sizeId") Long sizeId);

    @GET("colors/{colorId}")
    Call<JsonElement> getProductColor(@Path("colorId") Long colorId);

    @GET("sizes/{sizeId}")
    Call<JsonElement> getProductSize(@Path("sizeId") Long sizeId);

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

    @GET("carts")
    Call<List<CartItem>> getCartItems(@Header("Authorization") String token);

    @POST("carts")
    Call<JsonElement> addCartItem(@Body JsonObject cartItem, @Header("Authorization") String token);

    @PATCH("carts/{cart-item-id}")
    Call<JsonElement> updateCartItem(@Path("cart-item-id") Long cartItemId, @Body JsonObject cartItem, @Header("Authorization") String token);

    @DELETE("carts/{cart-item-id}")
    Call<Void> deleteCartItem(@Path("cart-item-id") Long cartItemId, @Header("Authorization") String token);

    @DELETE("carts")
    Call<Void> deleteAllCartItems(@Header("Authorization") String token);

    @GET("orders")
    Call<List<OrderHistoryItem>> getOrders(@Header("Authorization") String token);

    @GET("orders/{order-id}")
    Call<OrderHistoryItem> getOrderById(@Path("order-id") Long orderId, @Header("Authorization") String token);

    @POST("orders")
    Call<JsonElement> addOrder(@Body JsonObject order, @Header("Authorization") String token);

    @PATCH("orders/{order-id}")
    Call<JsonElement> updateOrder(@Path("order-id") Long orderId, @Body JsonObject order, @Header("Authorization") String token);

    @DELETE("orders/{order-id}")
    Call<Void> deleteOrder(@Path("order-id") Long orderId, @Header("Authorization") String token);

}
