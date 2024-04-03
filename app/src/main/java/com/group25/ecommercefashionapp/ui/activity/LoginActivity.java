package com.group25.ecommercefashionapp.ui.activity;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.api.ApiService;
import com.group25.ecommercefashionapp.api.ApiServiceBuilder;
import com.group25.ecommercefashionapp.cache.UserCache;
import com.group25.ecommercefashionapp.data.Token;
import com.group25.ecommercefashionapp.data.UserProfile;
import com.group25.ecommercefashionapp.status.LoginStatus;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.utilities.TokenUtils;

import java.io.IOException;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout usernameEditLayout, passwordEditLayout;
    TextInputEditText usernameEditText, passwordEditText;
    TextView signup;
    View view;
    GifImageView loadingGif;
    Button loginButton;

    public static class LoginInfo {
        public String username;
        public String password;

        public LoginInfo(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupEmailEditText();
        setupPasswordEditText();
        setListeners();
    }

    private void setupEmailEditText() {
        usernameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        usernameEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        usernameEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (!usernameEditText.getText().toString().isEmpty()) {
                passwordEditText.requestFocus();
            } else {
                usernameEditLayout.setHelperText("invalid username*");
            }
            return false;
        });
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                usernameEditLayout.setHelperText("");
            }
        });
    }

    private void setupPasswordEditText() {
        passwordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            loginButton.performClick();
            return false;
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                passwordEditLayout.setHelperText("");
            }
        });
    }

    private void initializeViews() {
        usernameEditLayout = findViewById(R.id.usernameInputLayout);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditLayout = findViewById(R.id.passwordInputLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        loadingGif = findViewById(R.id.gifButton);
        signup = findViewById(R.id.signupClickableText);
        loginButton = findViewById(R.id.loginButton);
        view = findViewById(R.id.arrow_left_container);

        // Set underline text for signup
        signup.setPaintFlags(signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    // Function to set listeners
    private void setListeners() {
        // Set listener for arrow left container
        view.setOnClickListener(v -> onBackPressed());

        // Set listener for login button
        loginButton.setOnClickListener(v -> handleLogin());

        // Set listener for signup text
        signup.setOnClickListener(v -> navigateToSignup());
    }



    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private boolean backgroundTaskComplete = false;

        @Override
        protected void onPreExecute() {
            // This method runs on the UI thread before starting the background task.
            // You can show a loading spinner or perform UI updates here.
            loadingGif.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            usernameEditText.setEnabled(false);
            passwordEditText.setEnabled(false);

        }

        @Override
        protected Void doInBackground(Void... params) {
            // This method runs in the background thread.
            // Simulate some background processing here.
            backgroundTaskComplete = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // This method runs on the UI thread after the background task is complete.
            // You can update the UI or perform post-processing here.

            // Wait for a minimum duration before dismissing the progressDialog
            if (backgroundTaskComplete) {
                // The background task completed quickly, wait for a minimum duration
                new Handler().postDelayed(() -> {
                    loadingGif.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.VISIBLE);
                    usernameEditText.setEnabled(true);
                    passwordEditText.setEnabled(true);
                }, 0);
            }  // The background task is still running, progressDialog will be dismissed in onPreExecute
            // You may want to handle this case based on your specific requirements

        }
    }

    private void login(LoginInfo loginInfo) {
        ApiService apiService = ApiServiceBuilder.buildService();

        apiService.userLogin(loginInfo).enqueue(new Callback<LoginStatus>() {
            @Override
            public void onResponse(@NonNull Call<LoginStatus> call, @NonNull Response<LoginStatus> response) {
                if (response.isSuccessful()) {
                    LoginStatus loginStatus = response.body();

                    UserStatus.access_token = new Token(loginStatus.access_token);
                    UserStatus._isLoggedIn = true;
                    apiService.getUserInfo(TokenUtils.bearerToken(UserStatus.access_token.token)).enqueue(new Callback<UserProfile>() {
                        @Override
                        public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                            if (response.isSuccessful()) {
                                UserStatus.currentUser = response.body();
                                UserCache.getInstance().addUser(UserStatus.currentUser.getEmail(), UserStatus.currentUser);
                                MySharedPreferences sharedPreferences = new MySharedPreferences(getApplication());
                                sharedPreferences.updateUserLoginStatus();
                                sharedPreferences.saveTokens(UserStatus.access_token.token);
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                            Toast.makeText(LoginActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    if (response.isSuccessful()) {
                        new BackgroundTask().execute();
                        launchMainActivity();
                    }
                }else if (response.code() == 401) {
                    // Handle invalid username or password error
                    String errorMessage = "Invalid username or password";
                    usernameEditLayout.setHelperText(errorMessage);
                    passwordEditLayout.setHelperText(errorMessage);
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

                else {
                    // Handle login failure
                    String errorMessage = "Login failed";
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Gson gson = new Gson();
                            Map<String, String> errorMap = gson.fromJson(errorBody, Map.class);
                            errorMessage = errorMap.get("log");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    usernameEditLayout.setHelperText(errorMessage);
                    passwordEditLayout.setHelperText(errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginStatus> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                new BackgroundTask().execute();
            }
        });
    }

    private void handleLogin() {
        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (validateInput(email, password)) {
            LoginInfo loginInfo = new LoginInfo(email, password);
            login(loginInfo);
        }
    }

    // Function to navigate to signup activity
    private void navigateToSignup() {
        getMainActivityInstance().navController.navigate(R.id.signupActivity);
    }

    // Function to validate input fields
    private boolean validateInput(String username, String password) {
        if (username.isEmpty()) {
            usernameEditLayout.setHelperText("required*");
            return false;
        }
        if (password.isEmpty()) {
            passwordEditLayout.setHelperText("required*");
            return false;
        }
        return true;
    }

    private void launchMainActivity() {
        // For example, start the MainActivity after the background processing.
        Bundle bundle = new Bundle();
        bundle.putString("message", "logged");
        bundle.putString("button", "Continue");
        getMainActivityInstance().navController.navigate(R.id.successActivity, bundle);
        getMainActivityInstance().navController.popBackStack();
        onBackPressed();
    }
}