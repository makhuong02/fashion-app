package com.group25.ecommercefashionapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.database.ProductDbHelper;
import com.group25.ecommercefashionapp.repository.ProductRepository;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    public NavController navController;

    ArrayList<Product> products = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProductDbHelper productDbHelper = new ProductDbHelper(this);
        ProductRepository productRepository = new ProductRepository(productDbHelper);

        productRepository.dropProductTable();
        productRepository.insertDbData();
        products = productRepository.getAllProducts();
        for (Product product : products) {
            Log.d("Product", String.valueOf(product.getId()));
        }

        // Initialize navigation components
        initializeViews();
        setupNavigation();


    }

    private void initializeViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public void updateNavigationBarState(int actionId){
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == actionId) {
                item.setChecked(true);
                break;
            }
        }
    }
}
