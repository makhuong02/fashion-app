package com.group25.ecommercefashionapp.ui.fragment.bottomsheet;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.OrderHistoryItem;
import com.group25.ecommercefashionapp.data.UserInteraction;

public class CancelOrderBottomSheetFragment extends BottomSheetDialogFragment {
    AppCompatButton noButton, yesButton;
    TextView orderStatus;
    OrderHistoryItem orderHistoryItem;
    AppCompatButton cancelOrderButton;
    public CancelOrderBottomSheetFragment(TextView orderStatus, OrderHistoryItem orderHistoryItem, AppCompatButton cancelOrderButton) {
        this.orderStatus = orderStatus;
        this.orderHistoryItem = orderHistoryItem;
        this.cancelOrderButton = cancelOrderButton;
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cancel_order_bottom_sheet, container, false);
        noButton = view.findViewById(R.id.no_button);
        yesButton = view.findViewById(R.id.yes_button);
        UserInteraction userInteraction = getMainActivityInstance().userInteraction;
        noButton.setOnClickListener(v -> dismiss());
        yesButton.setOnClickListener(v -> {
            orderStatus.setText("Cancelled");
            orderHistoryItem.setOrderStatus("Cancelled");
            userInteraction.getOrderList().get(userInteraction.getOrderList().indexOf(orderHistoryItem)).setOrderStatus("Cancelled");
            cancelOrderButton.setVisibility(View.GONE);
            dismiss();
        });
        return view;
    }
}
