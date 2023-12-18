package com.group25.ecommercefashionapp.ui.activity;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.api.ApiService;
import com.group25.ecommercefashionapp.data.UserProfile;
import com.group25.ecommercefashionapp.status.RegisterStatus;

import java.io.IOException;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    TextInputLayout usernameEditLayout, emailEditLayout, phoneNumberEditLayout, passwordEditLayout, confirmPasswordEditLayout;
    TextInputEditText usernameET, emailET, phoneNumberET, passwordET, confirmPasswordET;
    GifImageView loadingGif;
    TextView login;
    View backArrow;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeView();

        login.setPaintFlags(login.getPaintFlags() |   android.graphics.Paint.UNDERLINE_TEXT_FLAG);
        addTextChanged(usernameET, usernameEditLayout);
        addTextChanged(emailET, emailEditLayout);
        addTextChanged(passwordET, passwordEditLayout);
        addTextChanged(confirmPasswordET, confirmPasswordEditLayout);
        addTextChanged(phoneNumberET, phoneNumberEditLayout);

        usernameET.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        usernameET.setOnEditorActionListener((v, actionId, event) -> {
            if (usernameET.getText().toString().isEmpty()) {
                usernameEditLayout.setHelperText("required*");
            }
            return false;
        });
        emailET.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        emailET.setOnEditorActionListener((v, actionId, event) -> {
            if (emailET.getText().toString().isEmpty()) {
                emailEditLayout.setHelperText("required*");
            }
            else {
                emailEditLayout.setHelperText("invalid email*");
            }
            return false;
        });
        phoneNumberET.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        phoneNumberET.setOnEditorActionListener((v, actionId, event) -> {
            if (phoneNumberET.getText().toString().isEmpty()) {
                phoneNumberEditLayout.setHelperText("required*");
            }
            else {
                phoneNumberEditLayout.setHelperText("invalid phone number*");
            }
            return false;
        });
        passwordET.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        passwordET.setOnEditorActionListener((v, actionId, event) -> {
            if (passwordET.getText().toString().isEmpty()) {
                passwordEditLayout.setHelperText("required*");
            }
            else {
                passwordEditLayout.setHelperText("at least 8 characters, 1 uppercase*");
            }
            return false;
        });
        confirmPasswordET.setImeOptions(EditorInfo.IME_ACTION_DONE);
        confirmPasswordET.setOnEditorActionListener((v, actionId, event) -> {
            if (confirmPasswordET.getText().toString().isEmpty()) {
                confirmPasswordEditLayout.setHelperText("required*");
            }
            signup.performClick();
            return false;
        });


        // BackArrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivityInstance().navController.popBackStack();
                onBackPressed();
            }
        });

        // Button and clickable TextView
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInput()){
                    clickSignUp();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLoginActivity();
            }
        });
    }

    private void addTextChanged(TextInputEditText editText, TextInputLayout editLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editLayout.setHelperText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.getId()==emailET.getId() && emailEditLayout.getHelperText()=="invalid email*"){
                    if(emailET.getText().toString().contains("@") && emailET.getText().toString().contains(".")){
                        emailEditLayout.setHelperText("");
                    }
                }
                else if(editText.getText().toString().isEmpty()){
                    editLayout.setHelperText("required*");
                }
                else
                    editLayout.setHelperText("");
            }
        });
    }

    private boolean isValidInput() {
        // Check empty
        if(usernameET.getText().toString().isEmpty()){
            usernameEditLayout.setHelperText("required*");
            usernameET.requestFocus();
            return false;
        }
        // Check email
        if(emailET.getText().toString().isEmpty()){
            emailEditLayout.setHelperText("required*");
            emailET.requestFocus();
            return false;
        }
        if(emailET.getText().toString().contains("@") && emailET.getText().toString().contains(".")){
            emailEditLayout.setHelperText("");
        }
        else {
            emailEditLayout.setHelperText("invalid email*");
            emailET.requestFocus();
            return false;
        }
        if(phoneNumberET.getText().toString().isEmpty()){
            phoneNumberEditLayout.setHelperText("required*");
            phoneNumberET.requestFocus();
            return false;
        }
        if(passwordET.getText().toString().isEmpty()){
            passwordEditLayout.setHelperText("required*");
            passwordET.requestFocus();
            return false;
        }
        if(confirmPasswordET.getText().toString().isEmpty()){
            confirmPasswordEditLayout.setHelperText("required*");
            confirmPasswordET.requestFocus();
            return false;
        }

        if(!passwordET.getText().toString().equals(confirmPasswordET.getText().toString())){
            passwordEditLayout.setHelperText("password not match*");
            confirmPasswordEditLayout.setHelperText("password not match*");
            confirmPasswordET.requestFocus();
            return false;
        }
        return true;
    }

    private void initializeView() {
        // EditText
        usernameET = findViewById(R.id.usernameEditText);
        emailET = findViewById(R.id.emailEditText);
        phoneNumberET = findViewById(R.id.phoneNumberEditText);
        passwordET = findViewById(R.id.passwordEditText);
        confirmPasswordET = findViewById(R.id.passwordConfirmEditText);
        loadingGif = findViewById(R.id.gifButton);

        // TextLayout
        usernameEditLayout = findViewById(R.id.usernameInputLayout);
        emailEditLayout = findViewById(R.id.emailInputLayout);
        phoneNumberEditLayout = findViewById(R.id.phoneNumberInputLayout);
        passwordEditLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordEditLayout = findViewById(R.id.passwordConfirmInputLayout);

        // BackArrow view
        backArrow = findViewById(R.id.arrow_left_container);

        // Clickable TextView
        login = findViewById(R.id.loginClickableText);

        // Button
        signup = findViewById(R.id.createAccountButton);
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private boolean backgroundTaskComplete = false;
        @Override
        protected void onPreExecute() {
            // This method runs on the UI thread before starting the background task.
            // You can show a loading spinner or perform UI updates here.
            loadingGif.setVisibility(View.VISIBLE);
            signup.setVisibility(View.INVISIBLE);
            usernameET.setEnabled(false);
            emailET.setEnabled(false);
            phoneNumberET.setEnabled(false);
            passwordET.setEnabled(false);
            confirmPasswordET.setEnabled(false);

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
                        signup.setVisibility(View.VISIBLE);
                        usernameET.setEnabled(true);
                        emailET.setEnabled(true);
                        phoneNumberET.setEnabled(true);
                        passwordET.setEnabled(true);
                        confirmPasswordET.setEnabled(true);
                    }
                }, 0);
            } else {
                // The background task is still running, progressDialog will be dismissed in onPreExecute
                // You may want to handle this case based on your specific requirements
            }
        }


    }

    private void clickSignUp() {
        UserProfile userProfile = getUserProfile();

        ApiService.apiService.userRegister(userProfile).enqueue(new Callback<RegisterStatus>() {
            @Override
            public void onResponse(Call<RegisterStatus> call, Response<RegisterStatus> response) {
                if (response.body() != null) {
                    RegisterStatus result = response.body();
                    if (result.status) {
                        new BackgroundTask().execute();
                        Toast.makeText(SignupActivity.this, "Sign Up success", Toast.LENGTH_SHORT).show();
                        switchToLoginActivity();
                    }
                }
                else {
                    String text = null;
                    Map<String, String> errorMap;
                    try {
                        text = response.errorBody().string();
                        text = text.substring(0, 1).toUpperCase() + text.substring(1);
                        Gson gson = new Gson();
                        errorMap = gson.fromJson(text, Map.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    new BackgroundTask().execute();
                    Toast.makeText(SignupActivity.this, errorMap.get("log"), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterStatus> call, Throwable t) {
                new BackgroundTask().execute();
                Toast.makeText(SignupActivity.this, "Sign up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private UserProfile getUserProfile() {
        String email = emailET.getText().toString();
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String phoneNumber = phoneNumberET.getText().toString();

        UserProfile userProfile = new UserProfile(
                email,
                username,
                password,
                phoneNumber
        );

        return userProfile;
    }

    private void switchToLoginActivity() {
        getMainActivityInstance().navController.popBackStack();
        onBackPressed();
    }
}