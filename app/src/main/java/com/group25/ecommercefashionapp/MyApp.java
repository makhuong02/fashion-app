package com.group25.ecommercefashionapp;

import android.app.Application;

public class MyApp extends Application {
    private static MainActivity mainActivityInstance;

    public static void setMainActivityInstance(MainActivity activity) {
        mainActivityInstance = activity;
    }

    public static MainActivity getMainActivityInstance() {
        return mainActivityInstance;
    }
}
