package com.group25.ecommercefashionapp.ui.activity;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;
import static com.group25.ecommercefashionapp.utilities.InputValidator.isValidEmail;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.api.ApiService;
import com.group25.ecommercefashionapp.status.LoginStatus;
import com.group25.ecommercefashionapp.status.UserStatus;

import java.io.IOException;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout emailEditLayout, passwordEditLayout;
    TextInputEditText emailEditText, passwordEditText;
    TextView signup;
    View view;
    GifImageView loadingGif;
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

        emailEditLayout= findViewById(R.id.emailInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditLayout = findViewById(R.id.passwordInputLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        loadingGif = findViewById(R.id.gifButton);
        signup = findViewById(R.id.signupClickableText);
        loginButton = findViewById(R.id.loginButton);
        view = findViewById(R.id.arrow_left_container);

        signup.setPaintFlags(signup.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        emailEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        emailEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (isValidEmail(emailEditText.getText().toString())) {
                passwordEditText.requestFocus();
            }
            else{
                emailEditLayout.setHelperText("invalid email*");
            }
            return false;
        });
        passwordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            loginButton.performClick();
            return false;
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                emailEditLayout.setHelperText("");

            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordEditLayout.setHelperText("");
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(checkValidEmail(email, password)){
                    LoginInfo loginInfo = new LoginInfo(email, password);
                    login(loginInfo);
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivityInstance().navController.navigate(R.id.signupActivity);
            }
        });
    }

    private boolean checkValidEmail(String email, String password) {
        if(email.isEmpty()){
            emailEditLayout.setHelperText("required*");
            return false;
        }
        else if (!isValidEmail(email)) {
            emailEditLayout.setHelperText("invalid email*");
            return false;
        }
        if(password.isEmpty()){
            passwordEditLayout.setHelperText("required*");
            return false;
        }
        return true;
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private boolean backgroundTaskComplete = false;
        @Override
        protected void onPreExecute() {
            // This method runs on the UI thread before starting the background task.
            // You can show a loading spinner or perform UI updates here.
            loadingGif.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            emailEditText.setEnabled(false);
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingGif.setVisibility(View.INVISIBLE);
                        loginButton.setVisibility(View.VISIBLE);
                        emailEditText.setEnabled(true);
                        passwordEditText.setEnabled(true);
                    }
                }, 0);
            } else {
                // The background task is still running, progressDialog will be dismissed in onPreExecute
                // You may want to handle this case based on your specific requirements
            }
        }
    }

    private void login(LoginInfo loginInfo) {

        ApiService.apiService.userLogin(loginInfo).enqueue(new Callback<LoginStatus>() {
            @Override
            public void onResponse(Call<LoginStatus> call, Response<LoginStatus> response) {
                if (response.body() != null) {
                    LoginStatus loginStatus = response.body();
                    loginStatus.data.user.setPassword(loginInfo.password);

                    UserStatus._isLoggedIn = true;
                    UserStatus.currentUser = loginStatus.data.user;
                    UserStatus.access_token = loginStatus.data.access_token;

                    sharedPreferences = new MySharedPreferences(getApplication());
                    sharedPreferences.updateUserLoginStatus();
                    sharedPreferences.putUserLoginInfo(loginInfo);
                    if (response.isSuccessful()) {
                        new BackgroundTask().execute();
                        launchMainActivity();
                    }
                } else {
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
                    emailEditLayout.setHelperText(errorMessage);
                    passwordEditLayout.setHelperText(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<LoginStatus> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                new BackgroundTask().execute();
            }
        });
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