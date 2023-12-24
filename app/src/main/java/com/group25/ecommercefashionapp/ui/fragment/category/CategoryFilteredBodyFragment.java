package com.group25.ecommercefashionapp.ui.fragment.category;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.ProductItemAdapter;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.ui.activity.MainActivity;

import java.util.List;

public class CategoryFilteredBodyFragment extends Fragment implements OnItemClickListener {
    MainActivity mainActivity;
    Context context = null;
    TextView productCountTextView;
    EditText searchEditText;
    ImageButton clearSearchButton;
    RecyclerView productRecyclerView;
    List<Product> products;
    String category = "";
    ProductItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_filtered_fragment, container, false);

        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productCountTextView = view.findViewById(R.id.text_product_count);
        searchEditText = view.findViewById(R.id.search_edit_text);
        clearSearchButton = view.findViewById(R.id.clear_button);


        context = getActivity();
        mainActivity = (MainActivity) getActivity();

        Bundle bundle = getArguments();
        if (bundle != null) {
            category = bundle.getString("category");
        }

        ProductRepository productRepository = mainActivity.productRepository;
        products = productRepository.getProductsByCategory(category);

        productCountTextView.setText(getString(R.string.text_product_count_item, products.size()));

        searchEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("MainActivity", "onEditorAction: " + searchEditText.getText().toString());
                if (searchEditText.getText().toString().isEmpty()) {
                    products = productRepository.getProductsByCategory(category);
                } else {
                    products = productRepository.getProductsBySearchAndCategory(searchEditText.getText().toString(), category);
                }
                adapter = new ProductItemAdapter(products, this);
                productRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                productCountTextView.setText(getString(R.string.text_product_count_item, products.size()));

            }

            return false;
        });
        if (searchEditText.getText().toString().isEmpty()) {
            clearSearchButton.setVisibility(View.GONE);
        } else {
            clearSearchButton.setVisibility(View.VISIBLE);
        }

        clearSearchButton.setOnClickListener(v -> {
            searchEditText.setText("");
            clearSearchButton.setVisibility(View.GONE);
            products = productRepository.getProductsByCategory(category);
            adapter = new ProductItemAdapter(products, this);
            productRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            productCountTextView.setText(getString(R.string.text_product_count_item, products.size()));
        });

        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                clearSearchButton.setVisibility(View.VISIBLE);
            } else {
                clearSearchButton.setVisibility(View.GONE);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        productRecyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ProductItemAdapter(products, this);
        productRecyclerView.setAdapter(adapter);

        mainActivity.updateNavigationBarState(R.id.categoryBotNav);
        return view;
    }


    @Override
    public void onItemClick(View view, Item item) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        mainActivity.navController.navigate(R.id.viewProduct, bundle);
    }

    @Override
    public void onPause() {
        super.onPause();
        MySharedPreferences sharedPreferences = new MySharedPreferences(requireContext());
        sharedPreferences.putUserFavoriteList(getMainActivityInstance().userInteraction.getFavoriteList());
    }
}
