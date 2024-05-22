package com.group25.ecommercefashionapp.ui.fragment.bottomsheet;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.CartItemAdapter;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.ui.activity.CartActivity;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class RemoveItemBottomSheetFragment extends BottomSheetDialogFragment {
    AppCompatButton removeButton, cancelButton;
    CartItem cartItem;
    List<CartItem> cartItems;
    CartItemAdapter cartItemAdapter;
    Context context;
    public RemoveItemBottomSheetFragment(CartItem cartItem, List<CartItem> cartItems, CartItemAdapter cartItemAdapter, Context context) {
        this.cartItem = cartItem;
        this.cartItems = cartItems;
        this.cartItemAdapter = cartItemAdapter;
        this.context = context;
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_remove_item_bottom_sheet, container, false);
        removeButton = view.findViewById(R.id.footer_remove_button);
        cancelButton = view.findViewById(R.id.footer_cancel_button);

        cancelButton.setOnClickListener(v -> dismiss());
        removeButton.setOnClickListener(v -> {
            removeCartItem(cartItem);
            dismiss();
        });
        return view;
    }

    public void removeCartItem(CartItem cartItem) {
        UserRepository.getInstance().removeCartItem(cartItem.getId(), new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    cartItems.remove(cartItem);
                    cartItemAdapter.notifyDataChanged();
                    if(cartItems.isEmpty()){
                        ((CartActivity) context).setupEmptyCartView();
                    }
                }
                else{
                    Toast.makeText(getMainActivityInstance(), "Failed to remove item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable throwable) {
                Toast.makeText(getMainActivityInstance(), "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
