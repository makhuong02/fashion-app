package com.group25.ecommercefashionapp.ui.fragment.category;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.ui.activity.MainActivity;
import com.group25.ecommercefashionapp.OnListItemClick;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.CustomCategoryItemAdapter;
import com.group25.ecommercefashionapp.data.CategoryItem;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.ui.decorations.ProductItemDecoration;

import java.util.ArrayList;

public class CategoryFragment extends Fragment implements OnListItemClick {
    MainActivity mainActivity;
    Context context = null;
    RecyclerView categoryRecyclerView;
    ArrayList<CategoryItem> categories;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.category, container, false);

        // Initialize views and handle logic specific to this fragment
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);

        context = getActivity();
        mainActivity = (MainActivity) getActivity();

        ProductRepository productRepository = mainActivity.productRepository;
        categories = productRepository.getCategories();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        categoryRecyclerView.setLayoutManager(gridLayoutManager);

        CustomCategoryItemAdapter adapter = new CustomCategoryItemAdapter(categories, this);
        categoryRecyclerView.setAdapter(adapter);

        int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.product_vertical_spacing);
        int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.product_horizontal_spacing);
        categoryRecyclerView.addItemDecoration(new ProductItemDecoration(requireContext(), verticalSpacing, horizontalSpacing));

        return view;
    }


    @Override
    public void onItemClick(Item item, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("category", categories.get(position).getName());
        mainActivity.navController.navigate(R.id.action_categoryBotNav_to_filterCategory, bundle);

    }
}
