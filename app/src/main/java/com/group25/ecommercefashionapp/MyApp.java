package com.group25.ecommercefashionapp;

import android.app.Application;

import com.group25.ecommercefashionapp.repository.OrdersRepository;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.ui.Activity.MainActivity;

public class MyApp extends Application {
    private static MainActivity mainActivityInstance;

    public static void setMainActivityInstance(MainActivity activity, OrdersRepository ordersRepository, ProductRepository productRepository) {
        mainActivityInstance = activity;
        mainActivityInstance.ordersRepository = ordersRepository;
        mainActivityInstance.productRepository = productRepository;
    }

    public static MainActivity getMainActivityInstance() {
        return mainActivityInstance;
    }
}
