package com.group25.ecommercefashionapp.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.group25.ecommercefashionapp.api.ApiService;
import com.group25.ecommercefashionapp.api.ApiServiceBuilder;
import com.group25.ecommercefashionapp.cache.ProductCache;
import com.group25.ecommercefashionapp.data.Product;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
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


    public void fetchProductsByCategoryIdFromApi(Long categoryId, Context context, Callback<List<Product>> callback) {
        Call<List<Product>> call = apiService.getProductsByCategory(categoryId);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    ProductCache.getInstance().addProducts(categoryId, products);
                    callback.onResponse(call, response);
                } else {
                    Toast.makeText(context, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                callback.onFailure(call, t);
            }
        });
    }

    public void fetchProductByProductIdFromApi(Long productId, Context context, Callback<JsonElement> callback) {
        Call<JsonElement> call = apiService.getProductById(productId);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();
                    ProductCache.getInstance().addProduct(productId, parseJsonToProduct(jsonElement));
                    callback.onResponse(call, response);
                } else {
                    Toast.makeText(context, "Failed to fetch product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                callback.onFailure(call, t);
            }

        });
    }

    public void fetchProductsFromApi(Context context, Callback<JsonElement> callback) {
        ApiService apiService = ProductRepository.getInstance().getApiService();
        Call<JsonElement> call = apiService.getProducts();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();
                    if(jsonElement != null) {
                        List<Product> products = parseJsonToProducts(jsonElement);
                        assert products != null;
                        for (Product product : products) {
                            ProductCache.getInstance().addProduct(product.getId(), product);
                        }
                        callback.onResponse(call, response);
                    }

                } else {
                    Toast.makeText(context, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public ApiService getApiService() {
        return apiService;
    }

    public List<Product> parseJsonToProducts(JsonElement jsonElement) {
        // Parse jsonElement to List<Product>
        // You need to implement this method based on the structure of your JSON response
        // For example, using Gson:
        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>() {}.getType();
        return gson.fromJson(jsonElement, productListType);
    }

    public Product parseJsonToProduct(JsonElement jsonElement) {
        // Parse jsonElement to Product
        // You need to implement this method based on the structure of your JSON response
        // For example, using Gson:
        Gson gson = new Gson();
        return gson.fromJson(jsonElement, Product.class);
    }
}
