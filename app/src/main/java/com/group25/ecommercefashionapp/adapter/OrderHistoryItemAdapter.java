package com.group25.ecommercefashionapp.adapter;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.OrderHistoryItem;
import com.group25.ecommercefashionapp.ui.fragment.bottomsheet.CancelOrderBottomSheetFragment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryItemAdapter extends RecyclerView.Adapter<OrderHistoryItemAdapter.ViewHolder> {
    private List<OrderHistoryItem> items;
    private final DecimalFormat VNDFormat;
    private final Context context;

    private static final long THIRTY_MINUTES_MILLIS = 30 * 60 * 1000;

    public OrderHistoryItemAdapter(List<OrderHistoryItem> items, Context context) {
        this.items = items;
        this.context = context;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderHistoryItem item = items.get(position);
        holder.orderStatus.setText(item.getOrderStatus());
        holder.orderTotal.setText(String.format("%s VND", VNDFormat.format(item.getTotalPrice())));
        holder.orderClass.setText(item.getOrderClass());

        if ("Cancelled".equals(holder.orderStatus.getText().toString())) {
            holder.orderCancel.setVisibility(View.GONE);
        }

        String pickupString = TextUtils.isEmpty(item.getPickupPlace())
                ? "Your address: " + item.getAddress()
                : item.getPickupPlace();
        holder.pickupPlace.setText(pickupString);

        String orderDateString = item.getOrderDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        // convert to isoFormat dd MMM, yyyy HH:mm:ss
        SimpleDateFormat isoFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm:ss", Locale.getDefault());

        try {
            Date orderDate = dateFormat.parse(orderDateString);
            holder.orderDate.setText(isoFormat.format(orderDate));
            long orderTimeMillis = orderDate.getTime();
            long currentTimeMillis = System.currentTimeMillis();
            long timeDifferenceMillis = currentTimeMillis - orderTimeMillis;

            // Hide cancel button if the order is older than 30 minutes
            if (timeDifferenceMillis > THIRTY_MINUTES_MILLIS || item.getOrderStatus().equals("CANCELLED")) {
                holder.orderCancel.setVisibility(View.GONE);
            } else {
                // Set click listener for cancel button
                holder.orderCancel.setOnClickListener(v -> {
                    // Cancel order
                    CancelOrderBottomSheetFragment cancelOrderBottomSheetFragment = new CancelOrderBottomSheetFragment(this, item, holder.orderCancel);
                    if (context instanceof AppCompatActivity) {
                        cancelOrderBottomSheetFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), cancelOrderBottomSheetFragment.getTag());
                    }
                });
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        holder.orderDetail.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("orderId", item.getId());
            getMainActivityInstance().navController.navigate(R.id.orderHistoryDetailsActivity, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateOrderHistoryList(List<OrderHistoryItem> newOrderHistoryList) {
        this.items = newOrderHistoryList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderDate, orderStatus, orderTotal, orderClass, pickupPlace;
        AppCompatButton orderDetail, orderCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.order_date);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderTotal = itemView.findViewById(R.id.order_total);
            orderClass = itemView.findViewById(R.id.order_class);
            pickupPlace = itemView.findViewById(R.id.pickup_place);
            orderDetail = itemView.findViewById(R.id.view_details_button);
            orderCancel = itemView.findViewById(R.id.cancel_order_button);
        }
    }

    public void refreshOrderScreen() {
        ((AppCompatActivity) context).recreate();
    }
}
