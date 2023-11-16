package com.group25.ecommercefashionapp.ui.category;

import static com.group25.ecommercefashionapp.data.CategoryItem.getCategory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.group25.ecommercefashionapp.MainActivity;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.CustomCategoryItemAdapter;
import com.group25.ecommercefashionapp.data.CategoryItem;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    MainActivity mainActivity;
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
        mainActivity = (MainActivity) getActivity();
        CustomCategoryItemAdapter adapter = new CustomCategoryItemAdapter(context, R.layout.category_items, categories);
        gridCategory.setAdapter(adapter);

        gridCategory.setOnItemClickListener((parent, view1, position, id) -> mainActivity.navController.navigate(R.id.action_categoryBotNav_to_filterCategory));

        return view;
    }
}
