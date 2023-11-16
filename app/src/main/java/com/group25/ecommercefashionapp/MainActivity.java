package com.group25.ecommercefashionapp;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {
    private NestedScrollView nestedScrollView;
    private MaterialCardView imageCardView;
    private BottomNavigationView bottomNavigationView;
    public NavController navController;


    // Define variables for other widgets as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize navigation components
        initializeViews();
        setupNavigation();


    }

    private void initializeViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);
        imageCardView = findViewById(R.id.imageCardView);
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
            item.setChecked(item.getItemId() == actionId);
        }
    }


}
