package com.group25.ecommercefashionapp.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group25.ecommercefashionapp.api.ApiService;
import com.group25.ecommercefashionapp.api.ApiServiceBuilder;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.UserProfile;
import com.group25.ecommercefashionapp.status.UserStatus;

import java.util.List;

import com.group25.ecommercefashionapp.utilities.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static UserRepository instance;
    private final ApiService apiService;

    public UserRepository() {
        this.apiService = ApiServiceBuilder.buildService();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public void fetchUserDetails(String token, Context context, Callback<UserProfile> callback) {
        Call<UserProfile> call = apiService.getUserInfo(token);
        // Fetch user details from the server
       call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    UserStatus.currentUser = response.body();
                    callback.onResponse(call, response);
                }
                else {

                    Toast.makeText(context, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                // Handle failure
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                callback.onFailure(call, t);
            }
        });
    }

    public void validateToken(String token, Context context, Callback<Boolean> callback) {
        Call<Boolean> call = apiService.validateToken(token);
        // Validate the token
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                }
                else {
                    Toast.makeText(context, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                // Handle failure
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                callback.onFailure(call, t);
            }
        });
    }

    public void fetchFavoriteList(String token, Context context, Callback<List<Product>> callback) {
        Call<List<Product>> call = apiService.getFavoriteProducts(token);
        // Fetch favorite list from the server
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                }
                else {
                    Toast.makeText(context, "Failed to fetch favorite list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                // Handle failure
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                callback.onFailure(call, t);
            }
        });
    }

    public void addFavoriteProduct(String token, long productId, Context context) {
        Call<JsonElement> call = apiService.addFavoriteProduct(productId, token);
        // Add a product to the favorite list
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Failed to add product to favorite list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                // Handle failure
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeFavoriteProduct(String token, long productId, Context context) {
        Call<Void> call = apiService.removeFavoriteProduct(productId, token);
        // Remove a product from the favorite list
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Failed to remove product from favorite list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                // Handle failure
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchCartItems(Context context, Callback<List<CartItem>> callback) {
        Call<List<CartItem>> call = apiService.getCartItems(TokenUtils.bearerToken(UserStatus.access_token.token));
        // Fetch cart items from the server
        call.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<CartItem>> call, @NonNull Response<List<CartItem>> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<List<CartItem>> call, @NonNull Throwable t) {
                // Handle failure
                callback.onFailure(call, t);
            }
        });
    }

    public void addCartItem(CartItem cartItem, Context context, Callback<JsonElement> callback) {
        JsonObject body = new JsonObject();
        body.addProperty("productId", cartItem.getProductId());
        body.addProperty("quantity", cartItem.getQuantity());
        body.addProperty("selectedColorId", cartItem.getSelectedColorId());
        body.addProperty("selectedSizeId", cartItem.getSelectedSizeId());
        Call<JsonElement> call = apiService.addCartItem(body, TokenUtils.bearerToken(UserStatus.access_token.token));

        // Add an item to the cart
        executeJsonElementCall(callback, call);
    }

    public void updateCartItem(Long cartItemId, CartItem cartItem, Context context, Callback<JsonElement> callback) {
        JsonObject body = new JsonObject();
        body.addProperty("quantity", cartItem.getQuantity());
        Call<JsonElement> call = apiService.updateCartItem(cartItemId, body, TokenUtils.bearerToken(UserStatus.access_token.token));
        // Update the quantity of an item in the cart
        executeJsonElementCall(callback, call);
    }

    private void executeJsonElementCall(Callback<JsonElement> callback, Call<JsonElement> call) {
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                // Handle failure
                callback.onFailure(call, t);
            }
        });
    }

    public void removeCartItem(Long cartItemId, Context context, Callback<Void> callback) {
        Call<Void> call = apiService.deleteCartItem(cartItemId, TokenUtils.bearerToken(UserStatus.access_token.token));

        // Delete an item from the cart
        executeVoidCall(callback, call);
    }

    public void removeAllCartItems(Context context, Callback<Void> callback) {
        Call<Void> call = apiService.deleteAllCartItems(TokenUtils.bearerToken(UserStatus.access_token.token));
        // Delete all items from the cart
        executeVoidCall(callback, call);
    }

    private void executeVoidCall(Callback<Void> callback, Call<Void> call) {
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                // Handle failure
                callback.onFailure(call, t);
            }
        });
    }
}
