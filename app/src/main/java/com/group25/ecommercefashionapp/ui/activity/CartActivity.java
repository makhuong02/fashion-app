package com.group25.ecommercefashionapp.ui.activity;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.CartItemAdapter;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.UserInteraction;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnItemClickListener;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.ui.fragment.dialog.ErrorDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CartActivity extends AppCompatActivity implements OnItemClickListener {
    AppCompatButton footerCheckoutButton, checkoutButton, orderSummaryExpandButton, continueShoppingButton;
    MaterialCardView orderSummaryCardView, checkoutCardView;
    ConstraintLayout orderSummaryLayout, checkoutLayout;
    TextInputEditText addressEditText, firstNameEditText, lastNameEditText, firstNameEditText2, lastNameEditText2;
    NestedScrollView nestedScrollView;
    LinearLayout footerLayout;
    RecyclerView cartRecyclerView;
    TextView itemCounterTextView, subTotalTextView, taxTextView, orderTotalTextView, outOfStockText, footerTotalPriceTextView;
    DecimalFormat VNDFormat;
    UserInteraction userInteraction = getMainActivityInstance().userInteraction;
    private final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    private int rotationAngle = 0;
    SwipeRefreshLayout swipeRefreshLayout;
    private static final int MAP_REQUEST_CODE = 2;
    int totalOrderPrice;
    Toolbar toolbar;
    String customerPhoneNumber;
    List<CartItem> cartList = new ArrayList<>();
    CartItemAdapter cartItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchCarts();


//        for (CartItem item : userInteraction.getCartList()) {
//            if (productRepository.getProductById(item.getProductId()).getAvailableQuantity() == 0) {
//                outOfStockText.setVisibility(View.VISIBLE);
//                break;
//            }
//        }

    }

    private void setupCartView() {
        setContentView(R.layout.activity_cart);
        setupCurrencyFormat();
        initView();
        setupCheckoutButton();
        setupContinueShoppingButton();
        setupRecyclerView();
        setupOrderSummary();
        setupToolbar();
        setupSwipeRefreshLayout();
        updateCartSummaryView(cartList);
    }

    private void setupContinueShoppingButton() {
        continueShoppingButton.setOnClickListener(v -> {
            getMainActivityInstance().navController.popBackStack();
            onBackPressed();
        });
    }

    private void setupCurrencyFormat() {
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);
    }

    public void setupEmptyCartView() {
        setContentView(R.layout.activity_empty_cart);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            getMainActivityInstance().navController.popBackStack();
            onBackPressed();
        });

        continueShoppingButton = findViewById(R.id.continue_shopping_button);
        continueShoppingButton.setOnClickListener(v -> {
            getMainActivityInstance().navController.popBackStack();
            onBackPressed();
        });
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            getMainActivityInstance().navController.popBackStack();
            onBackPressed();
        });
    }



    private void setupCheckoutButton() {
        checkoutButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                checkoutButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setupInitialVisibility();
                setupScrollListener();
            }
        });

        footerCheckoutButton.setOnClickListener(v -> checkout());
        checkoutButton.setOnClickListener(v -> checkout());
    }

    private void setupOrderSummary() {
        orderSummaryCardView.setOnClickListener(v -> orderSummaryExpandButton.performClick());

        orderSummaryExpandButton.setOnClickListener(v -> {
            rotationAngle = rotationAngle == 0 ? -180 : 0;
            orderSummaryExpandButton.animate().rotation(rotationAngle).setDuration(500).start();
            orderSummaryLayout.setVisibility(orderSummaryLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });
    }

    private void setupRecyclerView() {
        cartItemAdapter = new CartItemAdapter(cartList, this, this);
        cartRecyclerView.setAdapter(cartItemAdapter);
    }

    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            fetchCarts();
        });
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
        bundle.putLong("id", item.getId());
        getMainActivityInstance().navController.navigate(R.id.viewProduct, bundle);
    }

    private void initView() {
        orderSummaryExpandButton = findViewById(R.id.order_summary_expand_button);
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
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    public void updateCartSummaryView(List<CartItem> cartList) {
        itemCounterTextView.setText(String.valueOf(cartList.size()));
        subTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(cartList.stream().mapToDouble(CartItem::getTotalPrice).sum())));
        taxTextView.setText(getString(R.string.product_price, VNDFormat.format(cartList.stream().mapToDouble(CartItem::getTotalPrice).sum() * 0.1)));
        orderTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(cartList.stream().mapToDouble(CartItem::getTotalPrice).sum() * 1.1)));
        footerTotalPriceTextView.setText(getString(R.string.product_price, VNDFormat.format(cartList.stream().mapToDouble(CartItem::getTotalPrice).sum() * 1.1)));
    }

    public void fetchCarts() {
        UserRepository.getInstance().fetchCartItems(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(@NotNull Call<List<CartItem>> call, @NotNull retrofit2.Response<List<CartItem>> response) {
                if (response.isSuccessful()) {
                    cartList = response.body();
                    userInteraction.setCartList(cartList);
                    if (cartList.isEmpty()) {
                        setupEmptyCartView();
                        return;
                    }
                    setupCartView();
                }
                else{
                    ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment("Error", "Failed to fetch cart items");
                    errorDialogFragment.show(getSupportFragmentManager(), "error");
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<CartItem>> call, Throwable t) {
                ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment("Error", "Failed to fetch cart items");
                errorDialogFragment.show(getSupportFragmentManager(), "error");
            }
        });
    }

    private void checkout() {
        getMainActivityInstance().navController.navigate(R.id.checkoutActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String address = data.getStringExtra("address");
                addressEditText.setText(address);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MySharedPreferences sharedPreferences = new MySharedPreferences(this);
        if (addressEditText != null) {
            if (!addressEditText.getText().toString().isEmpty()) {
                sharedPreferences.saveUserAddress(addressEditText.getText().toString());
            }
        }
        if (firstNameEditText != null || firstNameEditText2 != null) {
            if (!firstNameEditText.getText().toString().isEmpty()) {
                sharedPreferences.saveUserFirstName(firstNameEditText.getText().toString());
            }
            if (!firstNameEditText2.getText().toString().isEmpty()) {
                sharedPreferences.saveUserFirstName(firstNameEditText2.getText().toString());
            }
        }
        if (lastNameEditText != null || lastNameEditText2 != null) {
            if (!lastNameEditText.getText().toString().isEmpty()) {
                sharedPreferences.saveUserLastName(lastNameEditText.getText().toString());
            }
            if (!lastNameEditText2.getText().toString().isEmpty()) {
                sharedPreferences.saveUserLastName(lastNameEditText2.getText().toString());
            }
        }
    }
}
