package com.group25.ecommercefashionapp;

import static com.group25.ecommercefashionapp.data.ActionItem.getActionItems;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.group25.ecommercefashionapp.data.ActionItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NestedScrollView nestedScrollView;
    private MaterialCardView imageCardView;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    List<ActionItem> items;

    // Define variables for other widgets as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        navController = navHostFragment.getNavController();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        items = getActionItems();
        // Initialize the widgets by finding their views by ID
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


    }
}
