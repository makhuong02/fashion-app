package com.group25.ecommercefashionapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.api.ApiService;
import com.group25.ecommercefashionapp.status.LoginStatus;
import com.group25.ecommercefashionapp.status.UserStatus;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText;
    TextView loginError, signup;
    Button loginButton;
    MySharedPreferences sharedPreferences;

    public class LoginInfo {
        public String email;
        public String password;

        public LoginInfo(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginError = findViewById(R.id.loginError);
        signup = findViewById(R.id.signupClickableText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    LoginInfo loginInfo = new LoginInfo(email, password);
                    login(loginInfo);
                }
                else {
                    loginError.setText("* blank email or password");
                    loginError.setVisibility(View.VISIBLE);
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void login(LoginInfo loginInfo) {
        ApiService.apiService.userLogin(loginInfo).enqueue(new Callback<LoginStatus>() {
            @Override
            public void onResponse(Call<LoginStatus> call, Response<LoginStatus> response) {
                if (response.body() != null) {
                    LoginStatus result = response.body();
                    result.data.user.setPassword(loginInfo.password);

                    UserStatus._isLoggedIn = true;
                    UserStatus.currentUser = result.data.user;
                    UserStatus.access_token = result.data.access_token;

                    sharedPreferences = new MySharedPreferences(getApplication());
                    sharedPreferences.updateUserLoginStatus();
                    sharedPreferences.putUserLoginInfo(loginInfo);


                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    String text = null;
                    Map<String, String> errorMap;
                    try {
                        text = response.errorBody().string();
                        Gson gson = new Gson();
                        errorMap = gson.fromJson(text, Map.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    loginError.setText("* " + errorMap.get("log"));
                    loginError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<LoginStatus> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}