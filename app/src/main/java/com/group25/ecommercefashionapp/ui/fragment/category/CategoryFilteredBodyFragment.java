package com.group25.ecommercefashionapp.ui.fragment.category;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.FilterItemAdapter;
import com.group25.ecommercefashionapp.adapter.ProductItemAdapter;
import com.group25.ecommercefashionapp.data.FilterType;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.ui.activity.MainActivity;

import java.util.Arrays;
import java.util.List;

public class CategoryFilteredBodyFragment extends Fragment implements OnItemClickListener {
    MainActivity mainActivity;
    Context context = null;
    RecyclerView productRecyclerView, filterRecyclerView;
    List<Product> products;
    String category = "", search = "";
    ProductItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_filtered_fragment, container, false);

        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        filterRecyclerView = view.findViewById(R.id.list);
        List<FilterType> filterSections = Arrays.asList(FilterType.SIZE, FilterType.COLOR, FilterType.PRICE);
        FilterItemAdapter filterItemAdapter = new FilterItemAdapter(filterSections);

        filterRecyclerView.setAdapter(filterItemAdapter);

        context = getActivity();
        mainActivity = (MainActivity) getActivity();

        ProductRepository productRepository = mainActivity.productRepository;
        products = productRepository.getProductsByCategory(category);

        Bundle bundle = getArguments();
        if (bundle != null) {
            category = bundle.getString("category");
            search = bundle.getString("search");
        }

        if(search != null && !search.isEmpty()) {
            products = productRepository.getProductsBySearchAndCategory(search, category);
        }
        else {
            products = productRepository.getProductsByCategory(category);
        }
        adapter = new ProductItemAdapter(products, this);
        productRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
