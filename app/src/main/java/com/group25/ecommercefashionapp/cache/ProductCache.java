package com.group25.ecommercefashionapp.cache;

import com.group25.ecommercefashionapp.data.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductCache {
    private static ProductCache instance;
    private final Map<Long, List<Product>> cache;

    private ProductCache() {
        cache = new HashMap<>();
    }

    public static synchronized ProductCache getInstance() {
        if (instance == null) {
            instance = new ProductCache();
        }
        return instance;
    }



    public void addProducts(Long categoryId, List<Product> products) {
        cache.put(categoryId, products);
    }

    public void addProduct(Long productId, Product product) {
        cache.put(productId, List.of(product));
    }

    public List<Product> getProducts(Long categoryId) {
        return cache.get(categoryId);
    }


    public boolean containsCategory(Long categoryId) {
        return cache.containsKey(categoryId);
    }

    public boolean containsProduct(Long productId) {
        return cache.containsKey(productId);

    }

    public Product getProduct(Long productId) {
        for (List<Product> productList : cache.values()) {
            for (Product product : productList) {
                if (product.getId() == productId) {
                    return product;
                }
            }
        }
        return null; // Product not found in cache
    }

    public void clearCache() {
        cache.clear();
    }
}