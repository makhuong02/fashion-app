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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.api.ApiService;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.interfaces.callback.OnProductCountUpdateListener;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.status.UserStatus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFilteredFragment extends Fragment implements OnProductCountUpdateListener {
    MaterialToolbar toolbar;
    TextView productCountTextView;
    EditText searchEditText;
    ImageButton clearSearchButton;
    ActionMenuItemView cart;
    List<Product> products = new ArrayList<>();
    private final List<String> selectedItems = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_filtered, container, false);

        toolbar = view.findViewById(R.id.topAppBar);
        cart = view.findViewById(R.id.cart);
        String category = getArguments().getString("category");
        Long categoryId = getArguments().getLong("categoryId");
        toolbar.setTitle(category);
        productCountTextView = view.findViewById(R.id.text_product_count);
        searchEditText = view.findViewById(R.id.search_edit_text);
        clearSearchButton = view.findViewById(R.id.clear_button);

        fetchProductsFromApi(categoryId);
        productCountTextView.setText(getString(R.string.text_product_count_item, products.size()));
        startTransaction(category, searchEditText.getText().toString(), categoryId);

        searchEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startTransaction(category, searchEditText.getText().toString(), categoryId);
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
            startTransaction(category, searchEditText.getText().toString(), categoryId);
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
    private void fetchProductsFromApi(Long categoryId) {
        ApiService apiService = ProductRepository.getInstance().getApiService();
        Call<List<Product>> call = apiService.getProductsByCategory(categoryId);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    products = response.body();

                } else {
                    Toast.makeText(requireContext(), "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, Throwable t) {
                Toast.makeText(requireContext(), "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void startTransaction(String category, String search, Long categoryId) {
        CategoryFilteredBodyFragment existingFragment = findExistingFragment();
        if (existingFragment != null) {
            updateFragmentArguments(existingFragment, category, search, categoryId);
        } else {
            createAndReplaceFragment(category, search, categoryId);
        }
    }

    private CategoryFilteredBodyFragment findExistingFragment() {
        return (CategoryFilteredBodyFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainer);
    }

    private void updateFragmentArguments(CategoryFilteredBodyFragment fragment, String category, String search, Long categoryId) {
        Bundle args = createArgumentsBundle(category, search, categoryId);
        fragment.setProductCountUpdateListener(this);
        fragment.setArguments(args);
    }

    private void createAndReplaceFragment(String category, String search, Long categoryId) {
        CategoryFilteredBodyFragment fragment = createFragment(category, search, categoryId);
        fragment.setProductCountUpdateListener(this);
        replaceFragment(fragment);
    }

    private CategoryFilteredBodyFragment createFragment(String category, String search, Long categoryId) {
        Bundle args = createArgumentsBundle(category, search, categoryId);
        CategoryFilteredBodyFragment fragment = new CategoryFilteredBodyFragment(selectedItems);
        fragment.setArguments(args);
        return fragment;
    }

    private Bundle createArgumentsBundle(String category, String search, Long categoryId) {
        Bundle args = new Bundle();
        args.putString("category", category);
        args.putString("search", search);
        args.putLong("categoryId", categoryId);
        return args;
    }

    private void replaceFragment(CategoryFilteredBodyFragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }


    @Override
    public void onProductCountUpdate(int count) {
        productCountTextView.setText(getString(R.string.text_product_count_item, count));
    }
}
