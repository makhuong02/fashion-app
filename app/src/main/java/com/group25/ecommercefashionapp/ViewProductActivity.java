package com.group25.ecommercefashionapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.repository.OrdersRepository;
import com.group25.ecommercefashionapp.repository.ProductRepository;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class ViewProductActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    MainActivity mainActivity;
    TextView txtName, txtPrice, txtId, txtHighlight;
    ImageView productImage;
    private final DecimalFormat VNDFormat = new DecimalFormat("###,###,###,###");

    ProductRepository productRepository;
    Product products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        productRepository = MyApp.getMainActivityInstance().productRepository;

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        products = productRepository.getProductById(id);

        mainActivity = MyApp.getMainActivityInstance();

        txtName = findViewById(R.id.productNameTextView);
        txtHighlight = findViewById(R.id.productHighlightsTextView);
        txtPrice = findViewById(R.id.productPriceTextView);
        txtId = findViewById(R.id.productIDTextView);
        productImage = findViewById(R.id.productImageView);

        txtName.setText(products.getName());
        txtHighlight.setText(products.getDescription());
        txtPrice.setText(getString(R.string.product_price, VNDFormat.format(products.getPrice())));
        txtId.setText(String.valueOf(bundle.getInt("id")));
        productImage.setImageResource(products.getImage());

        toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            Log.d("ViewProductActivity", mainActivity.navController.getCurrentDestination().toString());
            mainActivity.navController.popBackStack();

        });
    }
}
