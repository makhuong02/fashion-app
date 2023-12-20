package com.group25.ecommercefashionapp.ui.fragment.bottomsheet;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.CartItemAdapter;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.UserInteraction;

public class RemoveItemBottomSheetFragment extends BottomSheetDialogFragment {
    AppCompatButton removeButton, cancelButton;
    CartItem cartItem;
    CartItemAdapter cartItemAdapter;
    public RemoveItemBottomSheetFragment(CartItem cartItem, CartItemAdapter cartItemAdapter) {
        this.cartItem = cartItem;
        this.cartItemAdapter = cartItemAdapter;
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_remove_item_bottom_sheet, container, false);
        removeButton = view.findViewById(R.id.footer_remove_button);
        cancelButton = view.findViewById(R.id.footer_cancel_button);
        UserInteraction userInteraction = getMainActivityInstance().userInteraction;
        cancelButton.setOnClickListener(v -> dismiss());
        removeButton.setOnClickListener(v -> {
            userInteraction.removeCart(cartItem);
            cartItemAdapter.notifyDataChanged();
            dismiss();
        });
        return view;
    }
}
