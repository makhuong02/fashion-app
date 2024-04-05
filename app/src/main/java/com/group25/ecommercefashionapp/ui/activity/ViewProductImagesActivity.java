package com.group25.ecommercefashionapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.JsonElement;
import com.group25.ecommercefashionapp.MyApp;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.ProductViewImageCarouselAdapter;
import com.group25.ecommercefashionapp.cache.ProductCache;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.repository.ProductRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProductImagesActivity extends AppCompatActivity {
    ViewPager productCarousel;
    MaterialToolbar toolbar;
    MainActivity mainActivity;
    private int finalPosition;
    private Long id;
    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_images);

        mainActivity = MyApp.getMainActivityInstance();

        productCarousel = findViewById(R.id.carousel);
        toolbar = findViewById(R.id.topAppBar);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("product_id");
        int position = bundle.getInt("position");
        if(ProductCache.getInstance().containsProduct(id)) {
            product = ProductCache.getInstance().getProduct(id);
        } else {
            ProductRepository.getInstance().fetchProductByProductIdFromApi(id, this ,new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                    if (response.isSuccessful()) {
                        JsonElement jsonElement = response.body();
                        product = ProductRepository.getInstance().parseJsonToProduct(jsonElement);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {

                    // Handle network error

                }
            });

        }

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
            getBundle.putLong("product_id", id);

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
        getBundle.putLong("product_id", id);

        Intent intent = new Intent();
        intent.putExtras(getBundle);

        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}
