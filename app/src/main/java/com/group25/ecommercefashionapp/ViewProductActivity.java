package com.group25.ecommercefashionapp;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.text.HtmlCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.repository.ProductRepository;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class ViewProductActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    MainActivity mainActivity;
    TextView txtName, txtActualPrice, txtDiscountPrice, txtId, txtHighlight, txtRating, txtReview;
    private final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    RatingBar ratingBar;
    ActionMenuItemView cart;

    ImageView productImage;
    private DecimalFormat VNDFormat;
    ActionMenuItemView share;


    ProductRepository productRepository;
    Product products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);

        productRepository = MyApp.getMainActivityInstance().productRepository;
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        products = productRepository.getProductById(id);

        mainActivity = MyApp.getMainActivityInstance();

        txtName = findViewById(R.id.productNameTextView);
        txtHighlight = findViewById(R.id.productHighlightsTextView);
        txtActualPrice = findViewById(R.id.productActualPriceTextView);
        txtDiscountPrice = findViewById(R.id.productDiscountPriceTextView);
        txtRating = findViewById(R.id.ratingTextView);
        txtReview = findViewById(R.id.seeReviewsTextView);
        ratingBar = findViewById(R.id.ratingBar);
        txtId = findViewById(R.id.productIDTextView);
        productImage = findViewById(R.id.productImageView);
        toolbar = findViewById(R.id.topAppBar);
        share = toolbar.findViewById(R.id.share);
        cart = toolbar.findViewById(R.id.cart);
        share.setOnClickListener(v -> shareContent());


        txtName.setText(products.getName());
        txtHighlight.setText(products.getDescription());
        txtActualPrice.setText(getString(R.string.product_price, VNDFormat.format(products.getPrice())));
        txtActualPrice.setPaintFlags(txtActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtDiscountPrice.setText(getString(R.string.product_price, VNDFormat.format(products.getPrice() * 0.9f)));
        txtId.setText(getString(R.string.product_id, String.valueOf(bundle.getInt("id"))));
        float rating = 3.8f;
        ratingBar.setRating(rating);
        txtRating.setText(String.valueOf(rating));
        txtReview.setText(HtmlCompat.fromHtml("<font color=\"blue\"><u>(See 5 reviews)</u></font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        productImage.setImageResource(products.getImage());

        toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            mainActivity.navController.popBackStack();
            onBackPressed();

        });
    }

    private void shareContent() {
        // Create an Intent to share content
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = products.getName() + "\n" + getString(R.string.product_price, VNDFormat.format(products.getPrice() * 0.9f));
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        // Start the activity for sharing
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
    }
}
