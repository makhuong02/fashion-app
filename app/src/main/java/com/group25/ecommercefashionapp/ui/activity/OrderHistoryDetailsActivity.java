package com.group25.ecommercefashionapp.ui.activity;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.divider.MaterialDivider;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.OrderHistorySummaryItemAdapter;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.OrderHistoryItem;
import com.group25.ecommercefashionapp.data.UserInteraction;
import com.group25.ecommercefashionapp.layoutmanager.LinearLayoutManagerWrapper;
import com.group25.ecommercefashionapp.status.UserStatus;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryDetailsActivity extends AppCompatActivity {
    TextView orderDate, orderStatus, orderTotalPrice, orderAddress, orderClass, orderCustomerName, orderCustomerPhone, storeAddress, orderShippingDeliveryPrice, orderDeliveryDate;
    TextView totalPrice, shippingFee, subTotalPrice, VATPrice, orderTotalPrice2, shippingFeeTextView;
    Toolbar toolbar;
    RecyclerView cartRecyclerView;
    MaterialCardView shippingAddressCardView, clickAndCollectCardView;
    OrderHistoryItem orderHistoryItem;
    private DecimalFormat VNDFormat;
    int totalOrderPrice = 0;
    MaterialDivider shippingFeeDivider;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);
        initView();
        MainActivity mainActivity = getMainActivityInstance();
        UserInteraction userInteraction = mainActivity.userInteraction;
        List<OrderHistoryItem> orderList = userInteraction.getOrderList();
        Bundle bundle = getIntent().getExtras();
        String orderDateString = bundle.getString("orderDate");
        for (OrderHistoryItem orderHistoryItem : orderList) {
            if (orderHistoryItem.getOrderDate().equals(orderDateString)) {
                this.orderHistoryItem = orderHistoryItem;
                break;
            }
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);
        orderDate.setText(orderHistoryItem.getOrderDate());
        orderStatus.setText(orderHistoryItem.getOrderStatus());
        orderTotalPrice.setText(VNDFormat.format(orderHistoryItem.getTotalPrice()) + " VND");
        orderClass.setText(orderHistoryItem.getOrderClass());

        if (orderHistoryItem.getAddress().equals("")) {
            shippingAddressCardView.setVisibility(android.view.View.GONE);
            clickAndCollectCardView.setVisibility(android.view.View.VISIBLE);
            storeAddress.setText(orderHistoryItem.getPickupPlace());
        } else {
            shippingAddressCardView.setVisibility(android.view.View.VISIBLE);
            clickAndCollectCardView.setVisibility(android.view.View.GONE);
            orderCustomerName.setText(orderHistoryItem.getFirstName() + " " + orderHistoryItem.getLastName());
            orderAddress.setText(orderHistoryItem.getAddress());
            orderCustomerPhone.setText(UserStatus.currentUser.getPhoneNumber());
        }

        orderShippingDeliveryPrice.setText(VNDFormat.format(50000) + " VND");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault());

        Date orderDate = null;
        try {
            orderDate = dateFormat.parse(orderDateString);
            long oneHourMillis = 60 * 60 * 1000;  // 1 hour in milliseconds
            Date deliveryDate = new Date(orderDate.getTime() + oneHourMillis);
            orderDeliveryDate.setText("Delivered on " + dateFormat.format(deliveryDate));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<CartItem> cartList = orderHistoryItem.getCartList();

        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false);
        cartRecyclerView.setLayoutManager(linearLayoutManagerWrapper);

        OrderHistorySummaryItemAdapter orderHistorySummaryItemAdapter = new OrderHistorySummaryItemAdapter(cartList, this);
        cartRecyclerView.setAdapter(orderHistorySummaryItemAdapter);

        if (!orderHistoryItem.getAddress().equals("")) {
            if (orderHistoryItem.getCartTotalPrice() < 999000) {
                totalPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getCartTotalPrice())));
                shippingFee.setText(getString(R.string.product_price, VNDFormat.format(50000)));
                subTotalPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getCartTotalPrice() + 50000)));
                VATPrice.setText(getString(R.string.product_price, VNDFormat.format((orderHistoryItem.getCartTotalPrice() + 50000) * 0.1)));
                totalOrderPrice = (int) ((orderHistoryItem.getCartTotalPrice() + 50000) * 1.1);
                orderTotalPrice2.setText(getString(R.string.product_price, VNDFormat.format(totalOrderPrice)));
            } else {
                totalPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getCartTotalPrice())));
                shippingFee.setVisibility(View.GONE);
                shippingFeeTextView.setVisibility(View.GONE);
                shippingFeeDivider.setVisibility(View.GONE);
                subTotalPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getCartTotalPrice())));
                VATPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getCartTotalPrice() * 0.1)));
                totalOrderPrice = (int) (orderHistoryItem.getCartTotalPrice() * 1.1);
                orderTotalPrice2.setText(getString(R.string.product_price, VNDFormat.format(totalOrderPrice)));
            }
        } else {
            totalPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getCartTotalPrice())));
            shippingFeeTextView.setVisibility(View.GONE);
            shippingFee.setVisibility(View.GONE);
            shippingFeeDivider.setVisibility(View.GONE);
            subTotalPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getCartTotalPrice())));
            VATPrice.setText(getString(R.string.product_price, VNDFormat.format(orderHistoryItem.getCartTotalPrice() * 0.1)));
            totalOrderPrice = (int) (orderHistoryItem.getCartTotalPrice() * 1.1);
            orderTotalPrice2.setText(getString(R.string.product_price, VNDFormat.format(totalOrderPrice)));

        }

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

        cartRecyclerView = findViewById(R.id.order_summary_recycler_view);

        totalPrice = findViewById(R.id.totalPrice);
        shippingFee = findViewById(R.id.shipping_fee_price);
        subTotalPrice = findViewById(R.id.subtotal_price);
        shippingFeeTextView = findViewById(R.id.shipping_fee_text);
        shippingFeeDivider = findViewById(R.id.shipping_fee_divider);
        VATPrice = findViewById(R.id.VATPrice);
        orderTotalPrice2 = findViewById(R.id.orderTotalPrice);

    }
}
