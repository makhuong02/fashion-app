package com.group25.ecommercefashionapp.ui.fragment.dialog;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.group25.ecommercefashionapp.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CartAddedDialogFragment extends DialogFragment {
    TextView itemCounterTextView, totalPriceTextView;
    int numberOfItems;
    long totalPrice;
    public CartAddedDialogFragment(int numberOfItems, long totalPrice) {
        this.numberOfItems = numberOfItems;
        this.totalPrice = totalPrice;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cart_added, container, false);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        final DecimalFormat VNDFormat = new DecimalFormat("###,###,###,###", symbols);
        // Initialize UI components
        AppCompatButton checkoutButton = view.findViewById(R.id.checkout_button);
        AppCompatButton closeButton = view.findViewById(R.id.close_button);
        itemCounterTextView = view.findViewById(R.id.number_of_items_text);
        totalPriceTextView = view.findViewById(R.id.price_text);

        itemCounterTextView.setText(String.format(getString(R.string.d_item_s), numberOfItems));
        totalPriceTextView.setText(String.format("%s VND", VNDFormat.format(totalPrice)));
        // Set click listeners
        checkoutButton.setOnClickListener(v -> {
            getMainActivityInstance().navController.navigate(R.id.cartActivity);
            dismiss();
        });

        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Adjust dialog size
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        }
    }
}
