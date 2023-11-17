package com.group25.ecommercefashionapp.ui.category;

import static com.group25.ecommercefashionapp.data.Product.getProduct;

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
import com.group25.ecommercefashionapp.data.ActionItem;
import com.group25.ecommercefashionapp.data.Product;
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


        productRecyclerView = view.findViewById(R.id.productGridView);

        products = getProduct();
        context = getActivity();
        mainActivity = (MainActivity) getActivity();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        productRecyclerView.setLayoutManager(gridLayoutManager);

        ProductItemAdapter adapter = new ProductItemAdapter(products, this);
        productRecyclerView.setAdapter(adapter);

        int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.product_vertical_spacing);
        int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.product_horizontal_spacing);
        productRecyclerView.addItemDecoration(new ProductItemDecoration(requireContext(), verticalSpacing, horizontalSpacing));

        toolbar = view.findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(v -> mainActivity.navController.popBackStack());

        return view;
    }

    @Override
    public void onItemClick(ActionItem item) {

        Log.d("MainActivity", "onItemClick: " + item.getName());

    }
}
