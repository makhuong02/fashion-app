package com.group25.ecommercefashionapp.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.group25.ecommercefashionapp.R;

public class SuccessActivity extends AppCompatActivity {
    AppCompatButton btnContinue;
    TextView tvMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        String title = bundle.getString("button");

        tvMessage = findViewById(R.id.successfulDescription);
        tvMessage.setText(getString(R.string.successfulDescription, message));

        btnContinue = findViewById(R.id.continueButton);
        btnContinue.setText(title);
        btnContinue.setOnClickListener(v -> {
            finish();
        });
    }
}
