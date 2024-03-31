package com.group25.ecommercefashionapp.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.group25.ecommercefashionapp.api.ApiService;
import com.group25.ecommercefashionapp.api.ApiServiceBuilder;
import com.group25.ecommercefashionapp.cache.ProductCache;
import com.group25.ecommercefashionapp.data.Product;

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

    public void fetchProductByProductIdFromApi(Long productId, Context context, Callback<Product> callback) {
        Call<Product> call = apiService.getProductById(productId);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    ProductCache.getInstance().addProduct(productId, product);
                    callback.onResponse(call, response);
                } else {
                    Toast.makeText(context, "Failed to fetch product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                callback.onFailure(call, t);
            }

        });
    }

    public void fetchProductsFromApi(Context context, Callback<List<Product>> callback) {
        ApiService apiService = ProductRepository.getInstance().getApiService();
        Call<List<Product>> call = apiService.getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    assert products != null;
                    for (Product product : products) {
                        ProductCache.getInstance().addProduct(product.getId(), product);
                    }
                    callback.onResponse(call, response);

                } else {
                    Toast.makeText(context, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ApiService getApiService() {
        return apiService;
    }
}
