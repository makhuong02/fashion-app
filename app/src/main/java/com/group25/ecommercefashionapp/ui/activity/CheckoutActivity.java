package com.group25.ecommercefashionapp.ui.activity;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;
import static com.group25.ecommercefashionapp.ui.activity.MapsActivity.getCurrentAddress;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.CheckOutItemAdapter;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.NotificationDetails;
import com.group25.ecommercefashionapp.interfaces.callback.AddressCallback;
import com.group25.ecommercefashionapp.layoutmanager.GridAutoFitLayoutManager;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.ui.fragment.dialog.ErrorDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {
    TextInputEditText firstNameEditText, lastNameEditText, addressEditText, firstNameEditText2, lastNameEditText2;
    TextView freeShip, totalTextView, itemCounterTextView, shipToAddressShippingFeeTextView, shippingFeePriceTextView, shippingFeeTextView, subTotalTextView, taxTextView, orderTotalTextView, setCurrentLocationTextView;
    AppCompatCheckBox shipToAddressCheckBox, clickAndCollectCheckBox, cashOnDeliveryCheckBox;
    ConstraintLayout shipToAddressLayout, clickAndCollectLayout;
    ImageView mapImageView;
    AppCompatButton placeOrderButton;
    RecyclerView orderItemsRecyclerView;
    MaterialDivider shippingFeeDivider;
    DecimalFormat VNDFormat;
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    int totalOrderPrice;
    List<CartItem> cartList = new ArrayList<>();
    Double cartTotalPrice;
    Toolbar toolbar;
    private static final int SHIPPING_FEE = 30000;
    private static final int MAP_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initializeViews();
        setUpCurrencyFormat();
        fetchCarts();
    }

    private void setUpShipToAddressCheckBox() {
        shipToAddressCheckBox.setOnClickListener(v -> {
            if (shipToAddressCheckBox.isChecked()) {
                shipToAddressLayout.setVisibility(View.VISIBLE);
                clickAndCollectLayout.setVisibility(View.GONE);
            } else {
                shipToAddressLayout.setVisibility(View.GONE);
            }
            clickAndCollectCheckBox.setChecked(false);
            setupCartView();
        });
    }

    private void setUpClickAndCollectCheckBox() {
        clickAndCollectCheckBox.setOnClickListener(v -> {
            if (clickAndCollectCheckBox.isChecked()) {
                clickAndCollectLayout.setVisibility(View.VISIBLE);
                shipToAddressLayout.setVisibility(View.GONE);
            } else {
                clickAndCollectLayout.setVisibility(View.GONE);
            }
            shipToAddressCheckBox.setChecked(false);
            setupCartView();
        });
    }

    private void setUpCurrentLocationTextView() {
        setCurrentLocationTextView.setOnClickListener(v -> getCurrentAddress(this, address -> addressEditText.setText(address)));
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(v1 -> {
            getMainActivityInstance().navController.popBackStack();
            onBackPressed();
        });
    }

    private void setUpMapImageView() {
        mapImageView.setOnClickListener(v -> {
            mapImageView.setEnabled(false);
            Intent intent = new Intent(this, MapsActivity.class);
            MapsActivity.OnMapLoadCompleteListener onMapLoadCompleteListener = new MapsActivity.OnMapLoadCompleteListener() {
                @Override
                public void onMapLoadComplete() {
                    mapImageView.setEnabled(true);
                }
            };
            startActivityForResult(intent, MAP_REQUEST_CODE);
        });
        addressEditText.setOnClickListener(v -> mapImageView.performClick());
    }

    private void setUpCurrencyFormat() {
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);
    }

    private void setUpOrderItemsRecyclerView() {
        int columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70,
                getResources().getDisplayMetrics());
        toolbar.setTitle("Checkout");
        GridAutoFitLayoutManager gridAutoFitLayoutManager = new GridAutoFitLayoutManager(this, columnWidth, LinearLayoutManager.VERTICAL, false);
        orderItemsRecyclerView.setAdapter(new CheckOutItemAdapter(cartList));
        orderItemsRecyclerView.setLayoutManager(gridAutoFitLayoutManager);
    }

    private void setUpListeners() {
        setUpShipToAddressCheckBox();
        setUpClickAndCollectCheckBox();
        setPlaceOrderButtonListener();
        setUpCurrentLocationTextView();
        setUpToolbar();
        setUpOrderItemsRecyclerView();
        setUpMapImageView();
    }

    private void setupCartView() {
        itemCounterTextView.setText(String.valueOf(cartList.size()));
        if (cartTotalPrice > 999000) {
            shipToAddressShippingFeeTextView.setPaintFlags(shippingFeePriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        shipToAddressShippingFeeTextView.setText(getString(R.string.product_price, VNDFormat.format(SHIPPING_FEE)));
        if (shipToAddressCheckBox.isChecked()) {
            shipToAddressSetupDetails();
        } else {
            collectAtStoreSetupDetails();
        }


    }

    private void setPlaceOrderButtonListener() {
        placeOrderButton.setOnClickListener(v -> {
            if (!clickAndCollectCheckBox.isChecked() && !shipToAddressCheckBox.isChecked()) {
                ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment("Checkout Failed", "Please select a delivery method");
                errorDialogFragment.show(getSupportFragmentManager(), "error");
                return;
            }
            if (shipToAddressCheckBox.isChecked()) {
                validateAddress();
            }
            if (clickAndCollectCheckBox.isChecked()) {
                validateName(firstNameEditText2, lastNameEditText2);
            }
            validateCashOnDelivery();
            if (clickAndCollectCheckBox.isChecked()) {
                addClickAndCollectOrder();
            } else {
                addShipToAddressOrder();
            }

        });
    }

    private void sendNotification() {
        String title = "Payment success";
        String description = "Your order will reach your hand soon.";
        NotificationDetails notification = new NotificationDetails(this, title, description);
        notification.showNotification((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
    }

    private void validateCashOnDelivery() {
        if (!cashOnDeliveryCheckBox.isChecked()) {
            cashOnDeliveryCheckBox.setError("Please select a payment method");
            cashOnDeliveryCheckBox.requestFocus();
        }
    }

    private void addClickAndCollectOrder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address", "227 Đ. Nguyễn Văn Cừ, Phường 4, Quận 5, Thành phố Hồ Chí Minh, Việt Nam");
        jsonObject.addProperty("classification", "ONLINE");
        jsonObject.addProperty("deliveryOption", "ON_STORE_PICKUP");
        jsonObject.add("items", new Gson().toJsonTree(cartList));

        UserRepository.getInstance().addOrder(jsonObject, new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                sendNotification();
                navigateToSuccessActivity();
                removeAllCartItems();
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable throwable) {
                ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment("Error", "Failed to add order");
                errorDialogFragment.show(getSupportFragmentManager(), "error");
            }
        });
    }

    private void addShipToAddressOrder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address", Objects.requireNonNull(addressEditText.getText()).toString());
        jsonObject.addProperty("classification", "ONLINE");
        jsonObject.addProperty("deliveryOption", "DELIVERY");
        jsonObject.add("items", new Gson().toJsonTree(cartList));

        UserRepository.getInstance().addOrder(jsonObject, new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                sendNotification();
                navigateToSuccessActivity();
                removeAllCartItems();
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable throwable) {
                ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment("Error", "Failed to add order");
                errorDialogFragment.show(getSupportFragmentManager(), "error");
            }
        });
    }

    public void removeAllCartItems() {
        UserRepository.getInstance().removeAllCartItems(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    private void validateAddress() {
        if (validateName(firstNameEditText, lastNameEditText))
            return;
        if (addressEditText.getText().toString().isEmpty()) {
            addressEditText.setError("Please enter your address");
            addressEditText.requestFocus();
        }
    }

    private boolean validateName(TextInputEditText firstNameEditText, TextInputEditText lastNameEditText) {
        if (firstNameEditText.getText().toString().isEmpty()) {
            firstNameEditText.setError("Please enter your first name");
            firstNameEditText.requestFocus();
            return true;
        }
        if (lastNameEditText.getText().toString().isEmpty()) {
            lastNameEditText.setError("Please enter your last name");
            lastNameEditText.requestFocus();
            return true;
        }
        return false;
    }

    private void navigateToSuccessActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("message", "ordered");
        bundle.putString("button", "Continue Shopping");
        getMainActivityInstance().navController.navigate(R.id.successActivity, bundle);
        getMainActivityInstance().navController.popBackStack();
        onBackPressed();
    }

    private void collectAtStoreSetupDetails() {
        standardOrderDetails();
    }

    private void setShippingFeeVisibility(int visibility) {
        shippingFeeTextView.setVisibility(visibility);
        shippingFeePriceTextView.setVisibility(visibility);
        shippingFeeDivider.setVisibility(visibility);
    }

    private void standardOrderDetails() {
        setShippingFeeVisibility(View.GONE);
        subTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(cartTotalPrice)));
        totalTextView.setText(getString(R.string.product_price, VNDFormat.format(cartTotalPrice)));
        taxTextView.setText(getString(R.string.product_price, VNDFormat.format(cartTotalPrice * 0.1)));
        totalOrderPrice = (int) (cartTotalPrice * 1.1);
        orderTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(totalOrderPrice)));
    }

    private void shipToAddressSetupDetails() {
        if (cartTotalPrice < 999000) {
            subTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(cartTotalPrice)));
            shippingFeePriceTextView.setText(getString(R.string.product_price, VNDFormat.format(SHIPPING_FEE)));
            totalTextView.setText(getString(R.string.product_price, VNDFormat.format(cartTotalPrice + SHIPPING_FEE)));
            taxTextView.setText(getString(R.string.product_price, VNDFormat.format((cartTotalPrice + SHIPPING_FEE) * 0.1)));
            totalOrderPrice = (int) ((cartTotalPrice + SHIPPING_FEE) * 1.1);
            orderTotalTextView.setText(getString(R.string.product_price, VNDFormat.format(totalOrderPrice)));
        } else {
            standardOrderDetails();
            shippingFeePriceTextView.setPaintFlags(shippingFeePriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            shippingFeePriceTextView.setText(getString(R.string.product_price, VNDFormat.format(SHIPPING_FEE)));
        }
        setShippingFeeVisibility(View.VISIBLE);
    }

    private void setUserDetailsFromPreferences() {
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
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        freeShip = findViewById(R.id.free_shipping_text);
        shipToAddressShippingFeeTextView = findViewById(R.id.ship_to_address_shipping_fee_text);
        shipToAddressCheckBox = findViewById(R.id.ship_to_address_check_box);
        shipToAddressLayout = findViewById(R.id.ship_to_address_layout);
        clickAndCollectCheckBox = findViewById(R.id.click_and_collect_check_box);
        clickAndCollectLayout = findViewById(R.id.click_and_collect_layout);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        addressEditText = findViewById(R.id.addressDetailsEditText);
        mapImageView = findViewById(R.id.map_button);
        setCurrentLocationTextView = findViewById(R.id.current_location_text);

        firstNameEditText2 = findViewById(R.id.firstNameEditText2);
        lastNameEditText2 = findViewById(R.id.lastNameEditText2);

        placeOrderButton = findViewById(R.id.place_order_button);
        cashOnDeliveryCheckBox = findViewById(R.id.cash_on_delivery_check_box);

        orderItemsRecyclerView = findViewById(R.id.order_items_recycler_view);
        shippingFeeTextView = findViewById(R.id.shipping_fee_text);
        shippingFeePriceTextView = findViewById(R.id.shipping_fee_price);
        shippingFeeDivider = findViewById(R.id.shipping_fee_divider);
        totalTextView = findViewById(R.id.subtotal_price);

        itemCounterTextView = findViewById(R.id.item_count);
        subTotalTextView = findViewById(R.id.totalPrice);
        taxTextView = findViewById(R.id.VATPrice);
        orderTotalTextView = findViewById(R.id.orderTotalPrice);
    }

    public void fetchCarts() {
        UserRepository.getInstance().fetchCartItems(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(@NotNull Call<List<CartItem>> call, @NotNull retrofit2.Response<List<CartItem>> response) {
                if (response.isSuccessful()) {
                    cartList = response.body();
                    assert cartList != null;
                    cartTotalPrice = cartList.stream().mapToDouble(CartItem::getTotalPrice).sum();
                    setupCartView();
                    setUpOrderItemsRecyclerView();
                    setUserDetailsFromPreferences();
                    setUpListeners();
                } else {
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

}
