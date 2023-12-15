package com.group25.ecommercefashionapp.ui.fragment.order;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.group25.ecommercefashionapp.ui.Activity.MainActivity;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.OrderDetailsAdapter;
import com.group25.ecommercefashionapp.adapter.OrderItemAdapter;
import com.group25.ecommercefashionapp.data.Orders;
import com.group25.ecommercefashionapp.repository.OrdersRepository;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    MainActivity mainActivity;
    Context context = null;
    ListView orderListView, orderDetailsListView;
    OrderItemAdapter orderItemAdapter;
    OrderDetailsAdapter orderDetailsAdapter;
    ArrayList<Orders> orders;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.order, container, false);

        context = getContext();
        mainActivity = (MainActivity) getContext();

        // Initialize views and handle logic specific to this fragment
        orderListView = view.findViewById(R.id.orderListView);
        OrdersRepository ordersRepository = mainActivity.ordersRepository;
        orders = ordersRepository.orders;

        orderItemAdapter = new OrderItemAdapter(context, R.layout.order_item, orders);
        orderListView.setAdapter(orderItemAdapter);

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailsDialog(orders.get(position));
            }
        });

        return view;
    }

    private void DetailsDialog(Orders order) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.order_details);

        orderDetailsListView = dialog.findViewById(R.id.orderDetailsListView);
        orderDetailsAdapter = new OrderDetailsAdapter(context, R.layout.order_details_item, order.getProducts());
        orderDetailsListView.setAdapter(orderDetailsAdapter);

        TextView txtTotalPrice = dialog.findViewById(R.id.orderDetailsTotalPrice);
        txtTotalPrice.setText(String.format("Total Price: %,d", order.getTotalPrice()));

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
