package com.group25.ecommercefashionapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.adapter.ProductViewImageCarouselAdapter;
import com.group25.ecommercefashionapp.data.Product;

public class ViewProductImages extends AppCompatActivity {
    ViewPager productCarousel;
    MaterialToolbar toolbar;
    MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_images);

        mainActivity = MyApp.getMainActivityInstance();

        productCarousel = findViewById(R.id.carousel);
        toolbar = findViewById(R.id.topAppBar);


        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("product_id");
        int position = bundle.getInt("position");
        Product product = mainActivity.productRepository.getProductById(id);

        ProductViewImageCarouselAdapter productImageCarouselAdapter = new ProductViewImageCarouselAdapter(this, product.getImageList());
        productCarousel.setAdapter(productImageCarouselAdapter);
        productCarousel.setCurrentItem(position);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }

}
