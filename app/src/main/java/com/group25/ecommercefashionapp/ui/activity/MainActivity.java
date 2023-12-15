package com.group25.ecommercefashionapp.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group25.ecommercefashionapp.MyApp;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.api.ApiService;
import com.group25.ecommercefashionapp.data.UserInteraction;
import com.group25.ecommercefashionapp.database.DatabaseHelper;
import com.group25.ecommercefashionapp.repository.OrdersRepository;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.status.LoginStatus;
import com.group25.ecommercefashionapp.status.UserStatus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    public NavController navController;
    public ProductRepository productRepository;
    public OrdersRepository ordersRepository;
    public UserInteraction userInteraction = new UserInteraction();
    public MySharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DatabaseHelper productDbHelper = new DatabaseHelper(this);
        productRepository = new ProductRepository(productDbHelper);
        productRepository.dropProductTable();
        ordersRepository = new OrdersRepository(productDbHelper);
        MyApp.setMainActivityInstance(this, ordersRepository, productRepository);

        productRepository.insertDbData();

        // Initialize navigation components
        initializeViews();
        setupNavigation();

        Context context = getApplication();
        sharedPreferences = new MySharedPreferences(context);
        if (sharedPreferences.getUserLoginStatus()) {
            LoginActivity.LoginInfo loginInfo = sharedPreferences.getUserLoginInfo();
            login(loginInfo);
        }
    }

    private void login(LoginActivity.LoginInfo loginInfo) {
        ApiService.apiService.userLogin(loginInfo).enqueue(new Callback<LoginStatus>() {
            @Override
            public void onResponse(Call<LoginStatus> call, Response<LoginStatus> response) {
                if (response.body() != null) {
                    LoginStatus result = response.body();
                    result.data.user.setPassword(loginInfo.password);

                    UserStatus._isLoggedIn = true;
                    UserStatus.currentUser = result.data.user;
                    UserStatus.access_token = result.data.access_token;

                    sharedPreferences.updateUserLoginStatus();
                    sharedPreferences.putUserLoginInfo(loginInfo);
                }
            }

            @Override
            public void onFailure(Call<LoginStatus> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
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
}
