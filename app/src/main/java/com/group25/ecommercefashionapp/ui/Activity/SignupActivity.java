package com.group25.ecommercefashionapp.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.api.ApiService;
import com.group25.ecommercefashionapp.data.UserProfile;
import com.group25.ecommercefashionapp.status.RegisterStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    EditText firstNameET, lastNameET, usernameET, emailET, phoneNumberET, passwordET, confirmPasswordET;
    TextView nameError, usernameError, emailError, phoneNumberError, passwordError, signUpError, login;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findView();

        // Display Error TextView
        firstNameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (firstNameET.getText().toString().isEmpty()) {
                        nameError.setVisibility(View.VISIBLE);
                    }
                    else {
                        nameError.setVisibility(View.GONE);
                    }
                }
            }
        });
        lastNameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (lastNameET.getText().toString().isEmpty()) {
                        nameError.setVisibility(View.VISIBLE);
                    }
                    else {
                        nameError.setVisibility(View.GONE);
                    }
                }
            }
        });
        usernameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (usernameET.getText().toString().isEmpty()) {
                        usernameError.setVisibility(View.VISIBLE);
                    }
                    else {
                        usernameError.setVisibility(View.GONE);
                    }
                }
            }
        });
        emailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String email = emailET.getText().toString();
                    if (email.isEmpty()) {
                        emailError.setText("* Must not blank here");
                        emailError.setVisibility(View.VISIBLE);
                    }
                    else if (email.contains("@")) {
                        emailError.setVisibility(View.GONE);
                    }
                    else {
                        emailError.setText("* Incorrect email");
                        emailError.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        phoneNumberET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (phoneNumberET.getText().toString().isEmpty()) {
                        phoneNumberError.setVisibility(View.VISIBLE);
                    }
                    else {
                        phoneNumberError.setVisibility(View.GONE);
                    }
                }
            }
        });
        passwordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (passwordET.getText().toString().isEmpty()) {
                        passwordError.setVisibility(View.VISIBLE);
                    }
                    else {
                        passwordError.setVisibility(View.GONE);
                    }
                }
            }
        });
        confirmPasswordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (confirmPasswordET.getText().toString().isEmpty()) {
                        signUpError.setText("* Must not blank here");
                        signUpError.setVisibility(View.VISIBLE);
                    }
                    else {
                        nameError.setVisibility(View.GONE);
                    }
                }
            }
        });


        // Button and clickable TextView
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordET.getText().toString();
                String confirmPassword = confirmPasswordET.getText().toString();

                if (password.equals(confirmPassword) && !checkEmpty()) {
                    clickSignUp();
                }
                else if (!password.equals(confirmPassword)) {
                    signUpError.setText("* Confirm password is not correct");
                    signUpError.setVisibility(View.VISIBLE);
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

    private void findView() {
        // EditText
        firstNameET = findViewById(R.id.firstNameEditText);
        lastNameET = findViewById(R.id.lastNameEditText);
        usernameET = findViewById(R.id.signUpUsernameEditText);
        emailET = findViewById(R.id.signUpEmailEditText);
        phoneNumberET = findViewById(R.id.phoneNumberEditText);
        passwordET = findViewById(R.id.signUpPasswordEditText);
        confirmPasswordET = findViewById(R.id.confirmPasswordEditText);

        // TextView Error
        nameError = findViewById(R.id.nameError);
        usernameError = findViewById(R.id.usernameError);
        emailError = findViewById(R.id.emailError);
        phoneNumberError = findViewById(R.id.phoneNumberError);
        passwordError = findViewById(R.id.passwordError);
        signUpError = findViewById(R.id.signUpError);

        // Clickable TextView
        login = findViewById(R.id.loginClickableTextView);

        // Button
        signup = findViewById(R.id.signUpButton);
    }

    private void clickSignUp() {
        UserProfile userProfile = getUserProfile();

        ApiService.apiService.userRegister(userProfile).enqueue(new Callback<RegisterStatus>() {
            @Override
            public void onResponse(Call<RegisterStatus> call, Response<RegisterStatus> response) {
                if (response.body() != null) {
                    RegisterStatus result = response.body();
                    if (result.status) {
                        Toast.makeText(SignupActivity.this, "Sign Up success", Toast.LENGTH_SHORT).show();
                        switchToLoginActivity();
                    }
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
                    Toast.makeText(SignupActivity.this, errorMap.get("log"), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterStatus> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Sign up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private UserProfile getUserProfile() {
        String email = emailET.getText().toString();
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String firstName = firstNameET.getText().toString();
        String lastName = lastNameET.getText().toString();
        String phoneNumber = phoneNumberET.getText().toString();

        UserProfile userProfile = new UserProfile(
                email,
                username,
                password,
                firstName,
                lastName,
                phoneNumber
        );

        return userProfile;
    }

    private boolean checkEmpty() {
        String email = emailET.getText().toString();
        String username = usernameET.getText().toString();
        String firstName = firstNameET.getText().toString();
        String lastName = lastNameET.getText().toString();
        String phoneNumber = phoneNumberET.getText().toString();
        String password = passwordET.getText().toString();
        String confirmPassword = confirmPasswordET.getText().toString();

        List<String> tempList = new ArrayList<>();
        tempList.add(email);
        tempList.add(username);
        tempList.add(firstName);
        tempList.add(lastName);
        tempList.add(phoneNumber);
        tempList.add(password);
        tempList.add(confirmPassword);

        for (String item : tempList) {
            if (item.isEmpty()) return true;
        }
        return false;
    }

    private void switchToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}