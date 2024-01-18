package com.group25.ecommercefashionapp.adapter;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.NotificationDetails;
import com.group25.ecommercefashionapp.data.OrderHistoryItem;
import com.group25.ecommercefashionapp.ui.fragment.bottomsheet.CancelOrderBottomSheetFragment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class OrderHistoryItemAdapter extends RecyclerView.Adapter<OrderHistoryItemAdapter.ViewHolder> {
    private final List<OrderHistoryItem> items;
    private final DecimalFormat VNDFormat;
    private final Context context;


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
        List<CartItem> cartList = item.getCartList();
        Log.d("cartList", cartList.size()+"");
        holder.orderDate.setText(item.getOrderDate());
        holder.orderStatus.setText(item.getOrderStatus());
        holder.orderTotal.setText(VNDFormat.format(item.getTotalPrice()) + " VND");
        holder.orderClass.setText(item.getOrderClass());
        if(holder.orderStatus.getText().equals("Cancelled")) {
            holder.orderCancel.setVisibility(View.GONE);
        }
        if (Objects.equals(item.getPickupPlace(), "")) {
            String pickupString = "Your address: " + item.getAddress();
            holder.pickupPlace.setText(pickupString);
        } else
            holder.pickupPlace.setText(item.getPickupPlace());
        String orderDateString = item.getOrderDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());

        Date orderDate = null;
        try {
            orderDate = dateFormat.parse(orderDateString);

            long orderTimeMillis = orderDate.getTime();
            long currentTimeMillis = System.currentTimeMillis();
            long timeDifferenceMillis = currentTimeMillis - orderTimeMillis;
            long thirtyMinutesMillis = 30 * 60 * 1000;  // 30 minutes in milliseconds
            long oneHourMillis = 60 * 60 * 1000;  // 1 hour in milliseconds

            // Hide cancel button if the order is older than 30 minutes
            if (timeDifferenceMillis > thirtyMinutesMillis) {
                holder.orderCancel.setVisibility(View.GONE);
                if(!holder.orderStatus.getText().equals("Cancelled")) {
                    if (holder.pickupPlace.getText().toString().contains("Your address")) {
                        holder.orderStatus.setText("Available for pickup");
                        item.setOrderStatus("Available for pickup");
                        String title = "Order Available for pickup";
                        String description = "Your order is available for pickup";
                        NotificationDetails notification = new NotificationDetails(context, title, description);
                        notification.showNotification((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
                    }
                    else {
                        holder.orderStatus.setText("On the way");
                        item.setOrderStatus("On the way");
                        String title = "Order Delivering";
                        String description = "Your order is on the way";
                        NotificationDetails notification = new NotificationDetails(context, title, description);
                        notification.showNotification((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
                    }
                }
            } else {
                // Set click listener for cancel button
                holder.orderCancel.setOnClickListener(v -> {
                    // Cancel order
                    CancelOrderBottomSheetFragment cancelOrderBottomSheetFragment = new CancelOrderBottomSheetFragment(holder.orderStatus, item, holder.orderCancel);
                    cancelOrderBottomSheetFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), cancelOrderBottomSheetFragment.getTag());
                });
            }

            if (timeDifferenceMillis > oneHourMillis) {
                if(holder.orderStatus.getText().equals("On the way") || holder.orderStatus.getText().equals("Available for pickup"))
                    if(!holder.pickupPlace.getText().toString().contains("Your address")) {
                        holder.orderStatus.setText("Picked up");
                        item.setOrderStatus("Picked up");
                        String title = "Order Picked up";
                        String description = "Your order has been picked up";
                        NotificationDetails notification = new NotificationDetails(context, title, description);
                        notification.showNotification((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
                    }
                    else {
                        holder.orderStatus.setText("Delivered");
                        item.setOrderStatus("Delivered");
                        String title = "Order Delivered";
                        String description = "Your order has been delivered";
                        NotificationDetails notification = new NotificationDetails(context, title, description);
                        notification.showNotification((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
                    }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.orderDetail.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("orderDate", item.getOrderDate());
            getMainActivityInstance().navController.navigate(R.id.orderHistoryDetailsActivity, bundle);
        });

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

    @Override
    public int getItemCount() {
        return items.size();
    }
}
