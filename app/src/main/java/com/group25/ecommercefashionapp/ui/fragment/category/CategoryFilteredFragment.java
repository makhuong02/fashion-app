package com.group25.ecommercefashionapp.ui.fragment.category;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.status.UserStatus;

public class CategoryFilteredFragment extends Fragment {
    MaterialToolbar toolbar;

    ActionMenuItemView cart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_filtered, container, false);

        toolbar = view.findViewById(R.id.topAppBar);
        cart = view.findViewById(R.id.cart);
        String category = getArguments().getString("category");
        toolbar.setTitle(category);


        cart.setOnClickListener(v -> {
            if (UserStatus._isLoggedIn) {
                getMainActivityInstance().navController.navigate(R.id.cartActivity);
            } else {
                getMainActivityInstance().navController.navigate(R.id.loginActivity);
            }
        });
        CategoryFilteredBodyFragment bodyFragment = new CategoryFilteredBodyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category", category); // Replace 'categoryValue' with your actual category value
        bodyFragment.setArguments(bundle);

// Use a FragmentTransaction to replace or add the CategoryFilteredBodyFragment
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, bodyFragment);
        transaction.commit();

        toolbar.setNavigationOnClickListener(v -> getMainActivityInstance().navController.popBackStack());

        return view;
    }

}
