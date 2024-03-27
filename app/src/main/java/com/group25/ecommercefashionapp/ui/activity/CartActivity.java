package com.group25.ecommercefashionapp.ui.activity;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;
import static com.group25.ecommercefashionapp.ui.activity.MapsActivity.getCurrentAddress;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.textfield.TextInputEditText;
import com.group25.ecommercefashionapp.interfaces.callback.AddressCallback;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.CartItemAdapter;
import com.group25.ecommercefashionapp.adapter.CheckOutItemAdapter;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.NotificationDetails;
import com.group25.ecommercefashionapp.data.OrderHistoryItem;
import com.group25.ecommercefashionapp.data.UserInteraction;
import com.group25.ecommercefashionapp.layoutmanager.GridAutoFitLayoutManager;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.ui.fragment.dialog.ErrorDialogFragment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<CartItem> fakeCartList = userInteraction.getCartList();
        List<CartItem> cartList = new ArrayList<>();

        customerPhoneNumber = UserStatus.currentUser.getPhoneNumber();
        for(CartItem cartItem : fakeCartList) {
            if (Objects.equals(cartItem.getPhoneNumber(), customerPhoneNumber)) {
                cartList.add(cartItem);
            }
        }
        if (cartList.size() == 0) {
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
            return;
        }
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

        CartItemAdapter cartItemAdapter = new CartItemAdapter(cartList, this, this);
        cartRecyclerView.setAdapter(cartItemAdapter);

        orderSummaryCardView.setOnClickListener(v -> orderSummaryExpandButton.performClick());

        orderSummaryExpandButton = findViewById(R.id.order_summary_expand_button);
        orderSummaryExpandButton.setOnClickListener(v -> {
            rotationAngle = rotationAngle == 0 ? -180 : 0;
            orderSummaryExpandButton.animate().rotation(rotationAngle).setDuration(500).start();

            orderSummaryLayout.setVisibility(orderSummaryLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

//        for (CartItem item : userInteraction.getCartList()) {
//            if (productRepository.getProductById(item.getProductId()).getAvailableQuantity() == 0) {
//                outOfStockText.setVisibility(View.VISIBLE);
//                break;
//            }
//        }

        toolbar.setNavigationOnClickListener(v -> {
            getMainActivityInstance().navController.popBackStack();
            onBackPressed();
        });

        checkoutButton.setOnClickListener(v -> checkout());
        footerCheckoutButton.setOnClickListener(v -> checkout());

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if (cartList.size() == 0) {
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
                return;
            }
            cartItemAdapter.notifyDataSetChanged();
        });

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
        bundle.putLong("id", item.getId());
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
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    public void updateCartSummaryView() {
        List<CartItem> cartList = new ArrayList<>();
        List<CartItem> fakeCartList = userInteraction.getCartList();
        for(CartItem cartItem : fakeCartList) {
            if (Objects.equals(cartItem.getPhoneNumber(), customerPhoneNumber)) {
                cartList.add(cartItem);
            }
        }
        itemCounterTextView.setText(String.valueOf(cartList.size()));
        subTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice())));
        taxTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice() * 0.1)));
        orderTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice() * 1.1)));
        footerTotalPriceTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice() * 1.1)));
    }

    private void checkout() {
        setContentView(R.layout.activity_checkout);
        toolbar = findViewById(R.id.toolbar);
        List<CartItem> fakeCartList = userInteraction.getCartList();
        List<CartItem> cartList = new ArrayList<>();
        customerPhoneNumber = UserStatus.currentUser.getPhoneNumber();
        for (CartItem cartItem : fakeCartList) {
            if (Objects.equals(cartItem.getPhoneNumber(), customerPhoneNumber)) {
                cartList.add(cartItem);
            }
        }
        TextView freeShip = findViewById(R.id.free_shipping_text);
        AppCompatCheckBox shipToAddressCheckBox = findViewById(R.id.ship_to_address_check_box);
        ConstraintLayout shipToAddressLayout = findViewById(R.id.ship_to_address_layout);
        AppCompatCheckBox clickAndCollectCheckBox = findViewById(R.id.click_and_collect_check_box);
        ConstraintLayout clickAndCollectLayout = findViewById(R.id.click_and_collect_layout);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        addressEditText = findViewById(R.id.addressDetailsEditText);
        ImageView mapImageView = findViewById(R.id.map_button);
        TextView setCurrentLocationTextView = findViewById(R.id.current_location_text);

        firstNameEditText2 = findViewById(R.id.firstNameEditText2);
        lastNameEditText2 = findViewById(R.id.lastNameEditText2);

        AppCompatButton placeOrderButton = findViewById(R.id.place_order_button);
        AppCompatCheckBox cashOnDeliveryCheckBox = findViewById(R.id.cash_on_delivery_check_box);

        RecyclerView orderItemsRecyclerView = findViewById(R.id.order_items_recycler_view);
        TextView shippingFeeTextView = findViewById(R.id.shipping_fee_text);
        TextView shippingFeePriceTextView = findViewById(R.id.shipping_fee_price);
        MaterialDivider shippingFeeDivider = findViewById(R.id.shipping_fee_divider);
        TextView totalTextView = findViewById(R.id.subtotal_price);

        itemCounterTextView = findViewById(R.id.item_count);
        subTotalTextView = findViewById(R.id.totalPrice);
        taxTextView = findViewById(R.id.VATPrice);
        orderTotalTextView = findViewById(R.id.orderTotalPrice);
        MySharedPreferences sharedPreferences = new MySharedPreferences(this);

        if (!sharedPreferences.getUserAddress().isEmpty()) {
            addressEditText.setText(sharedPreferences.getUserAddress());
        }
        if (!sharedPreferences.getUserFirstName().isEmpty()) {
            firstNameEditText.setText(sharedPreferences.getUserFirstName());
            firstNameEditText2.setText(sharedPreferences.getUserFirstName());
        }
        if (!sharedPreferences.getUserLastName().isEmpty()) {
            lastNameEditText.setText(sharedPreferences.getUserLastName());
            lastNameEditText2.setText(sharedPreferences.getUserLastName());
        }

        itemCounterTextView.setText(String.valueOf(cartList.size()));
        if(shipToAddressCheckBox.isChecked()){
            if (userInteraction.getCartTotalPrice() < 999000) {
                subTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice())));
                shippingFeeTextView.setText(getString(R.string.product_price, VNDFormat.format(50000)));
                totalTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice() + 50000)));
                taxTextView.setText(getString(R.string.product_price, VNDFormat.format((userInteraction.getCartTotalPrice() + 50000) * 0.1)));
                totalOrderPrice = (int) ((userInteraction.getCartTotalPrice() + 50000) * 1.1);
                orderTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(totalOrderPrice)));
            } else {
                subTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice())));
                shippingFeeTextView.setVisibility(View.GONE);
                shippingFeePriceTextView.setVisibility(View.GONE);
                shippingFeeDivider.setVisibility(View.GONE);
                totalTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice())));
                taxTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice() * 0.1)));
                totalOrderPrice = (int) (userInteraction.getCartTotalPrice() * 1.1);
                orderTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(totalOrderPrice)));
            }
        }else {
            subTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice())));
            shippingFeeTextView.setVisibility(View.GONE);
            shippingFeePriceTextView.setVisibility(View.GONE);
            shippingFeeDivider.setVisibility(View.GONE);
            totalTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice())));
            taxTextView.setText(getString(R.string.product_price, VNDFormat.format(userInteraction.getCartTotalPrice() * 0.1)));
            totalOrderPrice = (int) (userInteraction.getCartTotalPrice() * 1.1);
            orderTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(totalOrderPrice)));

        }

        setCurrentLocationTextView.setOnClickListener(v -> getCurrentAddress(this, new AddressCallback() {
            @Override
            public void onAddressReceived(String address) {
                addressEditText.setText(address);
            }
        }));
        addressEditText.setOnClickListener(v -> mapImageView.performClick());

        toolbar.setNavigationOnClickListener(v1 -> {
            getMainActivityInstance().navController.popBackStack();
            onBackPressed();
        });

        int columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70,
                getResources().getDisplayMetrics());
        toolbar.setTitle("Checkout");
        GridAutoFitLayoutManager gridAutoFitLayoutManager = new GridAutoFitLayoutManager(this, columnWidth, LinearLayoutManager.VERTICAL, false);
        orderItemsRecyclerView.setAdapter(new CheckOutItemAdapter(userInteraction.getCartList()));
        orderItemsRecyclerView.setLayoutManager(gridAutoFitLayoutManager);

        mapImageView.setOnClickListener(v -> {
            mapImageView.setEnabled(false);
            Intent intent = new Intent(CartActivity.this, MapsActivity.class);
            MapsActivity.OnMapLoadCompleteListener onMapLoadCompleteListener = new MapsActivity.OnMapLoadCompleteListener() {
                @Override
                public void onMapLoadComplete() {
                    mapImageView.setEnabled(true);
                }
            };
            startActivityForResult(intent, MAP_REQUEST_CODE);
        });

        placeOrderButton.setOnClickListener(v -> {
            if (!clickAndCollectCheckBox.isChecked() && !shipToAddressCheckBox.isChecked()) {
                ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment("Checkout Failed", "Please select a delivery method");
                errorDialogFragment.show(getSupportFragmentManager(), "error");
                return;
            }
            if (shipToAddressCheckBox.isChecked()) {
                if (firstNameEditText.getText().toString().isEmpty()) {
                    firstNameEditText.setError("Please enter your first name");
                    firstNameEditText.requestFocus();
                    return;
                }
                if (lastNameEditText.getText().toString().isEmpty()) {
                    lastNameEditText.setError("Please enter your last name");
                    lastNameEditText.requestFocus();
                    return;
                }
                if (addressEditText.getText().toString().isEmpty()) {
                    addressEditText.setError("Please enter your address");
                    addressEditText.requestFocus();
                    return;
                }
            }
            if (clickAndCollectCheckBox.isChecked()) {
                if (firstNameEditText2.getText().toString().isEmpty()) {
                    firstNameEditText2.setError("Please enter your first name");
                    firstNameEditText2.requestFocus();
                    return;
                }
                if (lastNameEditText2.getText().toString().isEmpty()) {
                    lastNameEditText2.setError("Please enter your last name");
                    lastNameEditText2.requestFocus();
                    return;
                }
            }
            if (!cashOnDeliveryCheckBox.isChecked()) {
                cashOnDeliveryCheckBox.setError("Please select a payment method");
                cashOnDeliveryCheckBox.requestFocus();
            }
            if (clickAndCollectCheckBox.isChecked()) {

                OrderHistoryItem orderHistoryItem = new OrderHistoryItem(userInteraction.shallowCopyCartList(cartList),
                        "ChicCloth - 227 Đ. Nguyễn Văn Cừ, Phường 4, Quận 5, Thành phố Hồ Chí Minh, Việt Nam",
                        firstNameEditText2.getText().toString(),
                        lastNameEditText2.getText().toString(),
                        "",
                        "Click and Collect",
                        UserStatus.currentUser.getPhoneNumber(), totalOrderPrice);
                getMainActivityInstance().userInteraction.addOrder(orderHistoryItem);
            } else {
                OrderHistoryItem orderHistoryItem = new OrderHistoryItem(userInteraction.shallowCopyCartList(cartList),
                        "",
                        firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),
                        addressEditText.getText().toString(),
                        "Ship to Address",
                        UserStatus.currentUser.getPhoneNumber(), totalOrderPrice);
                getMainActivityInstance().userInteraction.addOrder(orderHistoryItem);
            }

            Context context = getMainActivityInstance();
            String title = "Payment success";
            String description = "Your order will be delivered soon.";
            NotificationDetails notification = new NotificationDetails(context, title, description);
            notification.showNotification((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));

            Bundle bundle = new Bundle();
            bundle.putString("message", "ordered");
            bundle.putString("button", "Continue Shopping");
//            getMainActivityInstance().productRepository.updateProductAfterCheckout(userInteraction.getCartList());
            getMainActivityInstance().navController.navigate(R.id.successActivity, bundle);
            getMainActivityInstance().navController.popBackStack();
            onBackPressed();
            getMainActivityInstance().userInteraction.clearCart();
        });

        shipToAddressCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                shipToAddressLayout.setVisibility(View.VISIBLE);
                clickAndCollectCheckBox.setChecked(false);
                clickAndCollectLayout.setVisibility(View.GONE);
            } else {
                shipToAddressLayout.setVisibility(View.GONE);
            }
        });

        clickAndCollectCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                clickAndCollectLayout.setVisibility(View.VISIBLE);
                shipToAddressCheckBox.setChecked(false);
                shipToAddressLayout.setVisibility(View.GONE);
            } else {
                clickAndCollectLayout.setVisibility(View.GONE);
            }
        });

        MainActivity mainActivity = getMainActivityInstance();
        if (mainActivity.userInteraction.getCartTotalPrice() < 999000) {
            freeShip.setVisibility(View.VISIBLE);
        } else {
            freeShip.setVisibility(View.GONE);
        }
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
        sharedPreferences.putUserCartList(getMainActivityInstance().userInteraction.getCartList());
        if (addressEditText != null) {
            if (!addressEditText.getText().toString().isEmpty()) {
                sharedPreferences.putUserAddress(addressEditText.getText().toString());
            }
        }
        if (firstNameEditText != null || firstNameEditText2 != null) {
            if (!firstNameEditText.getText().toString().isEmpty()) {
                sharedPreferences.putUserFirstName(firstNameEditText.getText().toString());
            }
            if (!firstNameEditText2.getText().toString().isEmpty()) {
                sharedPreferences.putUserFirstName(firstNameEditText2.getText().toString());
            }
        }
        if (lastNameEditText != null || lastNameEditText2 != null) {
            if (!lastNameEditText.getText().toString().isEmpty()) {
                sharedPreferences.putUserLastName(lastNameEditText.getText().toString());
            }
            if (!lastNameEditText2.getText().toString().isEmpty()) {
                sharedPreferences.putUserLastName(lastNameEditText2.getText().toString());
            }
        }
        sharedPreferences.putUserOrderList(getMainActivityInstance().userInteraction.getOrderList());
    }
}
