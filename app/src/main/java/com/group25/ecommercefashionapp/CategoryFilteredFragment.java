package com.group25.ecommercefashionapp;

import static com.group25.ecommercefashionapp.TestProductItem.getProduct;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CategoryFilteredFragment extends Fragment {
    Context context = null;
    GridView productGridView;
    ArrayList<TestProductItem> products;

    public CategoryFilteredFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_filtered, container, false);

        productGridView = view.findViewById(R.id.productGridView);

        products = getProduct();
        context = getParentFragment().getActivity();

        TestProductItemAdapter adapter = new TestProductItemAdapter(context, R.layout.category_filtered_items, products);
        productGridView.setAdapter(adapter);;


        return view;
    }
}
