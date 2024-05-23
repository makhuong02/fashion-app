package com.group25.ecommercefashionapp.ui.fragment.bottomsheet;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonElement;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.OrderHistoryItemAdapter;
import com.group25.ecommercefashionapp.data.NotificationDetails;
import com.group25.ecommercefashionapp.data.OrderHistoryItem;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.ui.fragment.dialog.ErrorDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelOrderBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String STATUS_CANCELLED = "CANCELLED";
    AppCompatButton noButton, yesButton;
    OrderHistoryItemAdapter orderHistoryItemAdapter;
    OrderHistoryItem orderHistoryItem;
    AppCompatButton cancelOrderButton;
    public CancelOrderBottomSheetFragment(OrderHistoryItemAdapter orderHistoryItemAdapter, OrderHistoryItem orderHistoryItem, AppCompatButton cancelOrderButton) {
        this.orderHistoryItemAdapter = orderHistoryItemAdapter;
        this.orderHistoryItem = orderHistoryItem;
        this.cancelOrderButton = cancelOrderButton;
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cancel_order_bottom_sheet, container, false);
        noButton = view.findViewById(R.id.no_button);
        yesButton = view.findViewById(R.id.yes_button);
        noButton.setOnClickListener(v -> dismiss());
        yesButton.setOnClickListener(v -> {
            cancelOrder();
            dismiss();
        });
        return view;
    }

    private void cancelOrder() {
        UserRepository.getInstance().updateOrder(orderHistoryItem.getId(), STATUS_CANCELLED, new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    orderHistoryItemAdapter.refreshOrderScreen();
                    cancelOrderButton.setVisibility(View.GONE);
                    orderCancelledNotify();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable throwable) {
                ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment("Error","Failed to cancel order");
                errorDialogFragment.show(getMainActivityInstance().getSupportFragmentManager(), "errorDialog");
            }
        });
    }

    private void orderCancelledNotify() {
        Context context = getMainActivityInstance().getApplicationContext();
        String title = "Order Cancelled";
        String description = "Your order has been cancelled";
        NotificationDetails notification = new NotificationDetails(context, title, description);
        notification.showNotification((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
    }
}
