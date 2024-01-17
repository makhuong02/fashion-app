package com.group25.ecommercefashionapp.ui.fragment.category;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.OnProductCountUpdateListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryFilteredFragment extends Fragment implements OnProductCountUpdateListener {
    MaterialToolbar toolbar;
    TextView productCountTextView;
    EditText searchEditText;
    ImageButton clearSearchButton;
    ActionMenuItemView cart;
    List<Product> products;
    private final List<String> selectedItems = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_filtered, container, false);

        toolbar = view.findViewById(R.id.topAppBar);
        cart = view.findViewById(R.id.cart);
        String category = getArguments().getString("category");
        toolbar.setTitle(category);
        productCountTextView = view.findViewById(R.id.text_product_count);
        searchEditText = view.findViewById(R.id.search_edit_text);
        clearSearchButton = view.findViewById(R.id.clear_button);

        MainActivity mainActivity = (MainActivity) getActivity();
        ProductRepository productRepository = mainActivity.productRepository;
        products = productRepository.getProductsByCategory(category);

        productCountTextView.setText(getString(R.string.text_product_count_item, products.size()));
        startTransaction(category, searchEditText.getText().toString());

        searchEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startTransaction(category, searchEditText.getText().toString());
            }

            return false;
        });
        searchEditText.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchEditText.getText().toString().isEmpty()) {
                    clearSearchButton.setVisibility(View.GONE);
                } else {
                    clearSearchButton.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });

        clearSearchButton.setOnClickListener(v -> {
            searchEditText.setText("");
            startTransaction(category, searchEditText.getText().toString());
        });

        cart.setOnClickListener(v -> {
            if (UserStatus._isLoggedIn) {
                getMainActivityInstance().navController.navigate(R.id.cartActivity);
            } else {
                getMainActivityInstance().navController.navigate(R.id.loginActivity);
            }
        });


        toolbar.setNavigationOnClickListener(v -> getMainActivityInstance().navController.popBackStack());

        return view;
    }
    private void startTransaction(String category, String search) {
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        bundle.putString("search", search);
        CategoryFilteredBodyFragment bodyFragment = new CategoryFilteredBodyFragment(selectedItems);
        bodyFragment.setArguments(bundle);

        // Callback to update product count
        bodyFragment.setProductCountUpdateListener(this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, bodyFragment);
        transaction.commit();
    }

    @Override
    public void onProductCountUpdate(int count) {
        productCountTextView.setText(getString(R.string.text_product_count_item, count));
    }
}
