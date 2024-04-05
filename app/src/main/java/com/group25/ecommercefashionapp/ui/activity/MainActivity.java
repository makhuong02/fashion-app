package com.group25.ecommercefashionapp.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group25.ecommercefashionapp.MyApp;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.Token;
import com.group25.ecommercefashionapp.data.UserInteraction;
import com.group25.ecommercefashionapp.database.DatabaseHelper;
import com.group25.ecommercefashionapp.repository.OrdersRepository;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.utilities.TokenUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    public NavController navController;
    public OrdersRepository ordersRepository;
    public UserInteraction userInteraction = new UserInteraction();
    public MySharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DatabaseHelper productDbHelper = new DatabaseHelper(this);
        ordersRepository = new OrdersRepository(productDbHelper);
        MyApp.setMainActivityInstance(this, ordersRepository);


        // Initialize navigation components
        initializeViews();
        setupNavigation();

        Context context = getApplication();
        sharedPreferences = new MySharedPreferences(context);
        if (sharedPreferences.getAccessToken() != null) {
            validateToken(sharedPreferences.getAccessToken());
        }
        else {
            if (sharedPreferences.getUserFavoriteList() != null) {
                userInteraction.setFavoriteList(sharedPreferences.getUserFavoriteList());
            }
        }

//        if(sharedPreferences.getUserCartList() != null) {
//            userInteraction.setCartList(sharedPreferences.getUserCartList());
//        }
//        if(sharedPreferences.getUserOrderList() != null) {
//            userInteraction.setOrderList(sharedPreferences.getUserOrderList());
//        }

    }

    private void validateToken(String accessToken) {

        UserRepository.getInstance().validateToken(TokenUtils.bearerToken(accessToken), getApplicationContext(), new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (Boolean.TRUE.equals(response.body())) {
                        UserStatus._isLoggedIn = true;
                        UserStatus.access_token = new Token(accessToken);
                        fetchFavoriteListFromApi();
                    } else {
                        sharedPreferences.clearTokens();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                sharedPreferences.clearTokens();
            }
        });
    }

    private void fetchFavoriteListFromApi() {
        UserRepository.getInstance().fetchFavoriteList(TokenUtils.bearerToken(UserStatus.access_token.token), getApplicationContext(), new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful()) {

                    userInteraction.setFavoriteList(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
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

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences.saveUserFavoriteList(userInteraction.getFavoriteList());
    }
}
