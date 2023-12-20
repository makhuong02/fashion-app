package com.group25.ecommercefashionapp.ui.activity;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.CartItemAdapter;
import com.group25.ecommercefashionapp.data.Item;

public class CartActivity extends AppCompatActivity implements OnItemClickListener {
    AppCompatButton footerCheckoutButton, checkoutButton, orderSummaryExpandButton;
    MaterialCardView orderSummaryCardView;
    ConstraintLayout orderSummaryLayout, checkoutLayout;
    NestedScrollView nestedScrollView;
    LinearLayout footerLayout;
    RecyclerView cartRecyclerView;
    private boolean isFooterVisible = true;
    private int rotationAngle = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        checkoutButton = findViewById(R.id.checkout_button);
        footerCheckoutButton = findViewById(R.id.footer_checkout_button);
        footerLayout = findViewById(R.id.footer);
        checkoutLayout = findViewById(R.id.checkout_layout);
        nestedScrollView = findViewById(R.id.cart_nested_scroll_view);
        orderSummaryLayout = findViewById(R.id.order_summary_detail_layout);
        orderSummaryCardView = findViewById(R.id.order_summary_card_view);
        cartRecyclerView = findViewById(R.id.cart_list);
        checkoutButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                checkoutButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setupScrollListener();
            }
        });

        CartItemAdapter cartItemAdapter = new CartItemAdapter(getMainActivityInstance().userInteraction.getCartList(), this);
        Log.d("CartActivity", "onCreate: " + getMainActivityInstance().userInteraction.getCartList().size());
        cartRecyclerView.setAdapter(cartItemAdapter);


        orderSummaryCardView.setOnClickListener(v -> {
            orderSummaryExpandButton.performClick();
        });

        orderSummaryExpandButton = findViewById(R.id.order_summary_expand_button);
        orderSummaryExpandButton.setOnClickListener(v -> {
            rotationAngle = rotationAngle == 0 ? -180 : 0;
            orderSummaryExpandButton.animate().rotation(rotationAngle).setDuration(500).start();

            orderSummaryLayout.setVisibility(orderSummaryLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

    }
    private void setupScrollListener() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Check if the checkout_button is within the visible area
                int[] location = new int[2];
                checkoutButton.getLocationOnScreen(location);
                int checkoutButtonY = location[1];

                int scrollViewHeight = nestedScrollView.getHeight();
                int footerButtonHeight = footerCheckoutButton.getHeight();

                if (checkoutButtonY < scrollY + scrollViewHeight - footerButtonHeight) {
                    // Checkout button is within the visible area, hide the footer button
                    hideFooterButton();
                } else {
                    // Checkout button is not within the visible area, show the footer button
                    showFooterButton();
                }
            }
        });
    }

    private void hideFooterButton() {
        footerCheckoutButton.setVisibility(View.GONE);
    }

    private void showFooterButton() {
        footerCheckoutButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View view, Item item) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        getMainActivityInstance().navController.navigate(R.id.viewProduct, bundle);
    }
}
