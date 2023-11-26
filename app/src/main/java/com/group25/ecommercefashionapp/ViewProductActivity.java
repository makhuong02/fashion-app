package com.group25.ecommercefashionapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;


public class ViewProductActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    Context context;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        mainActivity = MyApp.getMainActivityInstance();

        toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            Log.d("ViewProductActivity", mainActivity.navController.getCurrentDestination().toString());
            mainActivity.navController.popBackStack();

        });
    }
}
