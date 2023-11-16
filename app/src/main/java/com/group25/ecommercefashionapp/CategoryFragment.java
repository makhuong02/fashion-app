package com.group25.ecommercefashionapp;

import static com.group25.ecommercefashionapp.CategoryItem.getCategory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    Context context = null;
    GridView gridCategory;
    ArrayList<CategoryItem> categories;

    public CategoryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.category, container, false);

        // Initialize views and handle logic specific to this fragment
        gridCategory = view.findViewById(R.id.gridViewCategory);

        categories = getCategory();
        context = getActivity();

        CustomCategoryItemAdapter adapter = new CustomCategoryItemAdapter(context, R.layout.category_items, categories);
        gridCategory.setAdapter(adapter);

        gridCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fragmentContainer, CategoryFilteredFragment.class, null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
