package com.group25.ecommercefashionapp.ui.activity;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;
import static com.group25.ecommercefashionapp.utilities.InputValidator.isValidEmail;
import static com.group25.ecommercefashionapp.utilities.InputValidator.isValidPassword;
import static com.group25.ecommercefashionapp.utilities.InputValidator.isValidPhoneNumber;

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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.api.ApiServiceBuilder;
import com.group25.ecommercefashionapp.data.UserProfile;
import com.group25.ecommercefashionapp.status.RegisterStatus;
import com.group25.ecommercefashionapp.utilities.PhoneNumberFormatter;

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
        usernameET.setOnFocusChangeListener((v, hasFocus) -> {
            usernameEditLayout.setCounterEnabled(hasFocus);
        });
        addTextChanged(usernameET, usernameEditLayout);
        addTextChanged(emailET, emailEditLayout);
        addTextChanged(passwordET, passwordEditLayout);
        addTextChanged(confirmPasswordET, confirmPasswordEditLayout);
        phoneNumberET.addTextChangedListener(new PhoneNumberFormatter(phoneNumberEditLayout));

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
            else if(isValidEmail(emailET.getText().toString())){
                emailEditLayout.setHelperText("");
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
            else if(isValidPhoneNumber(phoneNumberET.getText().toString())){
                phoneNumberEditLayout.setHelperText("");
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
            else if(isValidPassword(passwordET.getText().toString())){
                passwordEditLayout.setHelperText("");
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
        else if(!isValidEmail(emailET.getText().toString())){
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
        if(!isValidPassword(passwordET.getText().toString())){
            passwordEditLayout.setHelperText("at least 8 characters, 1 uppercase*");
            passwordET.requestFocus();
            return false;
        }
        if(!isValidPhoneNumber(phoneNumberET.getText().toString())){
            phoneNumberEditLayout.setHelperText("invalid phone number*");
            phoneNumberET.requestFocus();
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

        ApiServiceBuilder.buildService().userRegister(userProfile).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    new BackgroundTask().execute();
                    Toast.makeText(SignupActivity.this, "Sign Up success", Toast.LENGTH_SHORT).show();
                    switchToLoginActivity();
                }
                else {
                    Gson gson = new Gson();
                    RegisterStatus registerStatus = gson.fromJson(response.errorBody().charStream(), RegisterStatus.class);
                    new BackgroundTask().execute();
                    Toast.makeText(SignupActivity.this, registerStatus.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                new BackgroundTask().execute();
                Toast.makeText(SignupActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
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
        Bundle bundle = new Bundle();
        bundle.putString("message", "registered");
        bundle.putString("button", "Login");
        getMainActivityInstance().navController.navigate(R.id.successActivity, bundle);
        getMainActivityInstance().navController.popBackStack();
        onBackPressed();
    }
}