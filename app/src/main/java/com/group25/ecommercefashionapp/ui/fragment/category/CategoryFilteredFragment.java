package com.group25.ecommercefashionapp.ui.fragment.category;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.MainActivity;
import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.ProductItemAdapter;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.ui.decorations.ProductItemDecoration;

import java.util.ArrayList;

public class CategoryFilteredFragment extends Fragment implements OnItemClickListener {
    MainActivity mainActivity;
    Context context = null;
    MaterialToolbar toolbar;

    RecyclerView productRecyclerView;
    ArrayList<Product> products;


    public CategoryFilteredFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_filtered, container, false);
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        toolbar = view.findViewById(R.id.topAppBar);

        String category = getArguments().getString("category");
        toolbar.setTitle(category);
        context = getActivity();
        mainActivity = (MainActivity) getActivity();
        ProductRepository productRepository = mainActivity.productRepository;
        products = productRepository.getProductsByCategory(category);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        productRecyclerView.setLayoutManager(gridLayoutManager);

        ProductItemAdapter adapter = new ProductItemAdapter(products, this);
        productRecyclerView.setAdapter(adapter);

        int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.product_vertical_spacing);
        int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.product_horizontal_spacing);
        productRecyclerView.addItemDecoration(new ProductItemDecoration(requireContext(), verticalSpacing, horizontalSpacing));

        toolbar.setNavigationOnClickListener(v -> mainActivity.navController.popBackStack());

        mainActivity.updateNavigationBarState(R.id.categoryBotNav);
        return view;
    }


    @Override
    public void onItemClick(Item item) {
        Log.d("MainActivity", "onItemClick: " + item.getName());
    }
}
