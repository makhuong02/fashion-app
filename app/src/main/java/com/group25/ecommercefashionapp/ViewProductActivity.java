package com.group25.ecommercefashionapp;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.adapter.ProductColorAdapter;
import com.group25.ecommercefashionapp.adapter.ProductImageCarouselAdapter;
import com.group25.ecommercefashionapp.adapter.ProductSizeAdapter;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.ProductImage;
import com.group25.ecommercefashionapp.layoutmanager.GridAutoFitLayoutManager;
import com.group25.ecommercefashionapp.repository.ProductRepository;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;


public class ViewProductActivity extends AppCompatActivity implements OnItemClickListener{
    MaterialToolbar toolbar;
    MainActivity mainActivity;
    TextView txtName, txtActualPrice, txtDiscountPrice, txtId, txtHighlight, txtRating, txtReview, selectedColorTextView, selectedSizeTextView;
    private final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    RatingBar ratingBar;
    ActionMenuItemView cart;
    RecyclerView colorRecyclerView, sizeRecyclerView;

    ViewPager productCarousel;
    private DecimalFormat VNDFormat;
    ActionMenuItemView share;


    ProductRepository productRepository;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);

        productRepository = MyApp.getMainActivityInstance().productRepository;
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        product = productRepository.getProductById(id);

        mainActivity = MyApp.getMainActivityInstance();

        initializeViews();

        txtName.setText(product.getName());
        txtHighlight.setText(product.getDescription());
        txtActualPrice.setText(getString(R.string.product_price, VNDFormat.format(product.getPrice())));
        txtActualPrice.setPaintFlags(txtActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtDiscountPrice.setText(getString(R.string.product_price, VNDFormat.format(product.getPrice() * 0.9f)));
        txtId.setText(getString(R.string.product_id, String.valueOf(bundle.getInt("id"))));
        float rating = 3.8f;
        ratingBar.setRating(rating);
        txtRating.setText(String.valueOf(rating));
        txtReview.setText(HtmlCompat.fromHtml("<font color=\"blue\"><u>(See 5 reviews)</u></font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        List<ProductImage> imageList = mainActivity.productRepository.getProductById(id).getImageList();

        ProductImageCarouselAdapter productImageCarouselAdapter = new ProductImageCarouselAdapter(this, imageList, this);
        productCarousel.setAdapter(productImageCarouselAdapter);
        share.setOnClickListener(v -> shareContent());

        // Set up Color recycler view
        GridAutoFitLayoutManager colorGridLayoutManager = new GridAutoFitLayoutManager(this, 0, GridLayoutManager.HORIZONTAL, false);
        colorRecyclerView.setLayoutManager(colorGridLayoutManager);
        ProductColorAdapter colorAdapter = new ProductColorAdapter(product.getColorList(), this, colorRecyclerView);
        colorRecyclerView.setAdapter(colorAdapter);


        // Set up Size recycler view
        GridAutoFitLayoutManager sizeGridLayoutManager = new GridAutoFitLayoutManager(this, 0, GridLayoutManager.HORIZONTAL, false);
        sizeRecyclerView.setLayoutManager(sizeGridLayoutManager);
        ProductSizeAdapter sizeAdapter = new ProductSizeAdapter(product.getSizeList(), this, sizeRecyclerView);
        sizeRecyclerView.setAdapter(sizeAdapter);

        toolbar.setNavigationOnClickListener(v -> {
            mainActivity.navController.popBackStack();
            onBackPressed();

        });
    }

    private void shareContent() {
        // Create an Intent to share content
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = product.getName() + "\n" + getString(R.string.product_price, VNDFormat.format(product.getPrice() * 0.9f));
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        // Start the activity for sharing
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
    }

    @Override
    public void onItemClick(View view, Item item) {
        if (view.getId() == R.id.chip_image_card) {
            selectedColorTextView.setText( item.getName());
        } else if (view.getId() == R.id.chip_size_card) {
            selectedSizeTextView.setText(item.getName());
        }
    }

    private void initializeViews() {
        txtName = findViewById(R.id.productNameTextView);
        txtHighlight = findViewById(R.id.productHighlightsTextView);
        txtActualPrice = findViewById(R.id.productActualPriceTextView);
        txtDiscountPrice = findViewById(R.id.productDiscountPriceTextView);
        txtRating = findViewById(R.id.ratingTextView);
        txtReview = findViewById(R.id.seeReviewsTextView);
        ratingBar = findViewById(R.id.ratingBar);
        txtId = findViewById(R.id.productIDTextView);
        productCarousel = findViewById(R.id.carousel);
        toolbar = findViewById(R.id.topAppBar);
        share = toolbar.findViewById(R.id.share);
        cart = toolbar.findViewById(R.id.cart);
        selectedColorTextView = findViewById(R.id.selectColorText);
        colorRecyclerView = findViewById(R.id.colorRecycler);
        selectedSizeTextView = findViewById(R.id.selectSizeText);
        sizeRecyclerView = findViewById(R.id.sizeRecycler);
        toolbar = findViewById(R.id.topAppBar);
    }
}
