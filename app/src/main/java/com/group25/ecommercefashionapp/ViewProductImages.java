package com.group25.ecommercefashionapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.adapter.ProductViewImageCarouselAdapter;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.ui.activity.MainActivity;

public class ViewProductImages extends AppCompatActivity {
    ViewPager productCarousel;
    MaterialToolbar toolbar;
    MainActivity mainActivity;
    private int finalPosition;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_images);

        mainActivity = MyApp.getMainActivityInstance();

        productCarousel = findViewById(R.id.carousel);
        toolbar = findViewById(R.id.topAppBar);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("product_id");
        int position = bundle.getInt("position");
        Product product = mainActivity.productRepository.getProductById(id);

        ProductViewImageCarouselAdapter productImageCarouselAdapter = new ProductViewImageCarouselAdapter(this, product.getImageList());
        productCarousel.setAdapter(productImageCarouselAdapter);
        productCarousel.setCurrentItem(position);
        productCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
            }

            @Override
            public void onPageSelected(int position) {
                finalPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        toolbar.setNavigationOnClickListener(v -> {
            Bundle getBundle = new Bundle();
            getBundle.putInt("position", finalPosition);
            getBundle.putInt("product_id", id);

            Intent intent = new Intent();
            intent.putExtras(getBundle);

            setResult(RESULT_OK, intent);
            finish();

        });
    }

    @Override
    public void onBackPressed() {
        Bundle getBundle = new Bundle();
        getBundle.putInt("position", finalPosition);
        getBundle.putInt("product_id", id);

        Intent intent = new Intent();
        intent.putExtras(getBundle);

        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}