package com.group25.ecommercefashionapp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.divider.MaterialDivider;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.OrderHistorySummaryItemAdapter;
import com.group25.ecommercefashionapp.cache.UserCache;
import com.group25.ecommercefashionapp.data.OrderHistoryItem;
import com.group25.ecommercefashionapp.data.OrderItem;
import com.group25.ecommercefashionapp.data.UserProfile;
import com.group25.ecommercefashionapp.layoutmanager.LinearLayoutManagerWrapper;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.utilities.DateUtils;
import com.group25.ecommercefashionapp.utilities.TokenUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryDetailsActivity extends AppCompatActivity {
    TextView orderDate, orderStatus, orderTotalPrice, orderAddress, orderClass, orderCustomerName, orderCustomerPhone, storeAddress, orderShippingDeliveryPrice, orderDeliveryDate;
    TextView totalPrice, shippingFee, subTotalPrice, VATPrice, orderTotalPrice2, shippingFeeTextView;
    Toolbar toolbar;
    RecyclerView orderSummaryRecyclerView;
    MaterialCardView shippingAddressCardView, clickAndCollectCardView;
    OrderHistoryItem orderHistoryItem;
    private DecimalFormat VNDFormat;
    int totalOrderPrice = 0;
    MaterialDivider shippingFeeDivider;
    AppCompatButton returnToOrderHistoryButton;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);
        initView();

        Bundle bundle = getIntent().getExtras();
        Long orderId = bundle.getLong("orderId");

        fetchOrderHistoryItemByIdFromApi(orderId);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);

        returnToOrderHistoryButton.setOnClickListener(v -> onBackPressed());
        toolbar.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        orderDate = findViewById(R.id.order_date);
        orderStatus = findViewById(R.id.order_status);
        orderTotalPrice = findViewById(R.id.order_total);
        orderClass = findViewById(R.id.order_class);

        orderCustomerName = findViewById(R.id.customer_name);
        orderAddress = findViewById(R.id.customer_address);
        orderCustomerPhone = findViewById(R.id.customer_phone);

        storeAddress = findViewById(R.id.store_address_text);

        shippingAddressCardView = findViewById(R.id.shipping_address_card_view);
        clickAndCollectCardView = findViewById(R.id.click_and_collect_card_view);

        orderShippingDeliveryPrice = findViewById(R.id.shipping_fee_price_delivery);
        orderDeliveryDate = findViewById(R.id.delivery_date);

        orderSummaryRecyclerView = findViewById(R.id.order_summary_recycler_view);

        totalPrice = findViewById(R.id.totalPrice);
        shippingFee = findViewById(R.id.shipping_fee_price);
        subTotalPrice = findViewById(R.id.subtotal_price);
        shippingFeeTextView = findViewById(R.id.shipping_fee_text);
        shippingFeeDivider = findViewById(R.id.shipping_fee_divider);
        VATPrice = findViewById(R.id.VATPrice);
        orderTotalPrice2 = findViewById(R.id.orderTotalPrice);

        returnToOrderHistoryButton = findViewById(R.id.return_to_order_history_button);
    }

    private void fetchOrderHistoryItemByIdFromApi(Long orderId) {
        UserRepository.getInstance().getOrderById(orderId, new Callback<OrderHistoryItem>() {
            @Override
            public void onResponse(@NonNull Call<OrderHistoryItem> call, @NonNull Response<OrderHistoryItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderHistoryItem = response.body();
                    populateOrderDetails();
                } else {
                    Toast.makeText(OrderHistoryDetailsActivity.this, "Failed to fetch order history item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderHistoryItem> call, @NonNull Throwable t) {
                Toast.makeText(OrderHistoryDetailsActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateOrderDetails() {
        orderDate.setText(DateUtils.convertDateFormat(orderHistoryItem.getOrderDate()));
        orderStatus.setText(orderHistoryItem.getOrderStatus());
        orderTotalPrice.setText(String.format("%s VND", VNDFormat.format(orderHistoryItem.getTotalPrice())));
        orderClass.setText(orderHistoryItem.getOrderClass());

        if (orderHistoryItem.getAddress().equals("")) {
            shippingAddressCardView.setVisibility(View.GONE);
            clickAndCollectCardView.setVisibility(View.VISIBLE);
            storeAddress.setText(orderHistoryItem.getPickupPlace());
        } else {
            shippingAddressCardView.setVisibility(View.VISIBLE);
            clickAndCollectCardView.setVisibility(View.GONE);
            orderCustomerName.setText(String.format("%s %s", orderHistoryItem.getFirstName(), orderHistoryItem.getLastName()));
            orderAddress.setText(orderHistoryItem.getAddress());
            if (UserStatus.currentUser != null && UserCache.getInstance().containsUser(UserStatus.currentUser.getEmail())) {
                UserStatus.currentUser = UserCache.getInstance().getUser(UserStatus.currentUser.getEmail());
                orderCustomerPhone.setText(UserStatus.currentUser.getPhoneNumber());
            } else {
                UserRepository.getInstance().fetchUserDetails(TokenUtils.bearerToken(UserStatus.access_token.token), this, new Callback<UserProfile>() {
                    @Override
                    public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                        if (response.isSuccessful()) {
                            UserStatus.currentUser = response.body();
                            UserCache.getInstance().addUser(UserStatus.currentUser.getEmail(), UserStatus.currentUser);
                            orderCustomerPhone.setText(UserStatus.currentUser.getPhoneNumber());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                    }
                });
            }
        }

        orderShippingDeliveryPrice.setText(String.format("%s VND", VNDFormat.format(50000)));

        Set<OrderItem> cartList = orderHistoryItem.getOrderItems();

        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false);
        orderSummaryRecyclerView.setLayoutManager(linearLayoutManagerWrapper);

        OrderHistorySummaryItemAdapter orderHistorySummaryItemAdapter = new OrderHistorySummaryItemAdapter(cartList, this);
        orderSummaryRecyclerView.setAdapter(orderHistorySummaryItemAdapter);

        if (!orderHistoryItem.getAddress().equals("")) {
            if (orderHistoryItem.getOrderTotalPrice() < 999000) {
                totalPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getOrderTotalPrice())));
                shippingFee.setText(getString(R.string.product_price, VNDFormat.format(50000)));
                subTotalPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getOrderTotalPrice() + 50000)));
                VATPrice.setText(getString(R.string.product_price, VNDFormat.format((orderHistoryItem.getOrderTotalPrice() + 50000) * 0.1)));
                totalOrderPrice = (int) ((orderHistoryItem.getOrderTotalPrice() + 50000) * 1.1);
                orderTotalPrice2.setText(getString(R.string.product_price, VNDFormat.format(totalOrderPrice)));
            } else {
                totalPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getOrderTotalPrice())));
                shippingFee.setVisibility(View.GONE);
                shippingFeeTextView.setVisibility(View.GONE);
                shippingFeeDivider.setVisibility(View.GONE);
                subTotalPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getOrderTotalPrice())));
                VATPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getOrderTotalPrice() * 0.1)));
                totalOrderPrice = (int) (orderHistoryItem.getOrderTotalPrice() * 1.1);
                orderTotalPrice2.setText(getString(R.string.product_price, VNDFormat.format(totalOrderPrice)));
            }
        } else {
            totalPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getOrderTotalPrice())));
            shippingFeeTextView.setVisibility(View.GONE);
            shippingFee.setVisibility(View.GONE);
            shippingFeeDivider.setVisibility(View.GONE);
            subTotalPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getOrderTotalPrice())));
            VATPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getOrderTotalPrice() * 0.1)));
            totalOrderPrice = (int) (orderHistoryItem.getOrderTotalPrice() * 1.1);
            orderTotalPrice2.setText(getString(R.string.product_price, VNDFormat.format(totalOrderPrice)));
        }
    }
}
