package com.group25.ecommercefashionapp.ui.activity;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.CartItemAdapter;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.UserInteraction;
import com.group25.ecommercefashionapp.repository.ProductRepository;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CartActivity extends AppCompatActivity implements OnItemClickListener {
    AppCompatButton footerCheckoutButton, checkoutButton, orderSummaryExpandButton, continueShoppingButton;
    MaterialCardView orderSummaryCardView, checkoutCardView;
    ConstraintLayout orderSummaryLayout, checkoutLayout;
    NestedScrollView nestedScrollView;
    LinearLayout footerLayout;
    RecyclerView cartRecyclerView;
    TextView itemCounterTextView, subTotalTextView, taxTextView, orderTotalTextView, outOfStockText, footerTotalPriceTextView;
    DecimalFormat VNDFormat;
    UserInteraction userInteraction = getMainActivityInstance().userInteraction;
    ProductRepository productRepository = getMainActivityInstance().productRepository;
    private final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    private int rotationAngle = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);

        initView();

        checkoutButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                checkoutButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setupInitialVisibility();
                setupScrollListener();
            }
        });

        continueShoppingButton.setOnClickListener(v -> {
            getMainActivityInstance().navController.popBackStack();
            onBackPressed();
        });

        CartItemAdapter cartItemAdapter = new CartItemAdapter(getMainActivityInstance().userInteraction.getCartList(), this, this);
        cartRecyclerView.setAdapter(cartItemAdapter);

        orderSummaryCardView.setOnClickListener(v -> orderSummaryExpandButton.performClick());

        orderSummaryExpandButton = findViewById(R.id.order_summary_expand_button);
        orderSummaryExpandButton.setOnClickListener(v -> {
            rotationAngle = rotationAngle == 0 ? -180 : 0;
            orderSummaryExpandButton.animate().rotation(rotationAngle).setDuration(500).start();

            orderSummaryLayout.setVisibility(orderSummaryLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        for (CartItem item : userInteraction.getCartList()) {
            if (productRepository.getProductById(item.getProductId()).getAvailableQuantity() == 0) {
                outOfStockText.setVisibility(View.VISIBLE);
                break;
            }
        }

        updateCartSummaryView();
    }
    private void setupScrollListener() {
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // Check if the checkout_button is within the visible area
            int[] location = new int[2];
            checkoutCardView.getLocationOnScreen(location);
            int checkoutButtonY = location[1];

            int scrollViewHeight = nestedScrollView.getHeight();
            int footerButtonHeight = footerLayout.getHeight();

            if (checkoutButtonY < scrollY + scrollViewHeight - footerButtonHeight) {
                // Checkout button is within the visible area, hide the footer button
                hideFooterButton();
            } else {
                // Checkout button is not within the visible area, show the footer button
                showFooterButton();
            }
        });
    }

    public void setupInitialVisibility() {
        int[] checkoutLocation = new int[2];
        int[] footerLocation = new int[2];

        checkoutCardView.getLocationOnScreen(checkoutLocation);
        footerLayout.getLocationOnScreen(footerLocation);

        // Check if the footer's y position is below the checkout button's y position
        if (footerLocation[1] > checkoutLocation[1]) {
            hideFooterButton();
        } else {
            showFooterButton();
        }
    }

    private void hideFooterButton() {
        footerLayout.setVisibility(View.GONE);
    }

    private void showFooterButton() {
        footerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View view, Item item) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        getMainActivityInstance().navController.navigate(R.id.viewProduct, bundle);
    }

    private void initView() {
        outOfStockText = findViewById(R.id.item_out_of_stock_text);
        checkoutButton = findViewById(R.id.checkout_button);
        footerCheckoutButton = findViewById(R.id.footer_checkout_button);
        footerLayout = findViewById(R.id.footer);
        checkoutLayout = findViewById(R.id.checkout_layout);
        nestedScrollView = findViewById(R.id.cart_nested_scroll_view);
        orderSummaryLayout = findViewById(R.id.order_summary_detail_layout);
        orderSummaryCardView = findViewById(R.id.order_summary_card_view);
        cartRecyclerView = findViewById(R.id.cart_list);
        itemCounterTextView = findViewById(R.id.item_count);
        subTotalTextView = findViewById(R.id.totalPrice);
        taxTextView = findViewById(R.id.VATPrice);
        orderTotalTextView = findViewById(R.id.orderTotalPrice);
        footerTotalPriceTextView = findViewById(R.id.footer_total_price);
        checkoutCardView = findViewById(R.id.check_out_card_view);
        continueShoppingButton = findViewById(R.id.continue_shopping_button);
    }
    public void updateCartSummaryView() {
        itemCounterTextView.setText(String.valueOf(userInteraction.getCartList().size()));
        subTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice())));
        taxTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice() * 0.1)));
        orderTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice() * 1.1)));
        footerTotalPriceTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice() * 1.1)));
    }
}
