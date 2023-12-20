package com.group25.ecommercefashionapp.ui.fragment.favorite;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;

import com.group25.ecommercefashionapp.R;

public class FavoriteFragment extends Fragment {
    ActionMenuItemView cart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View favoriteView = inflater.inflate(R.layout.fragment_favorite, container, false);

        cart = favoriteView.findViewById(R.id.cart);
        cart.setOnClickListener(v -> {
            getMainActivityInstance().navController.navigate(R.id.cartActivity);
        });

        return favoriteView;
    }
}
