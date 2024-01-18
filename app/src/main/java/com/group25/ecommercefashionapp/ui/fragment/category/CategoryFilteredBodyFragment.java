package com.group25.ecommercefashionapp.ui.fragment.category;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.interfaces.callback.FilterDialogCallback;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnItemClickListener;
import com.group25.ecommercefashionapp.interfaces.callback.OnProductCountUpdateListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.FilterItemAdapter;
import com.group25.ecommercefashionapp.adapter.ProductItemAdapter;
import com.group25.ecommercefashionapp.data.FilterType;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryFilteredBodyFragment extends Fragment implements OnItemClickListener, FilterDialogCallback{
    private MainActivity mainActivity;
    private RecyclerView productRecyclerView;
    private OnProductCountUpdateListener onProductCountUpdateListener;
    private ImageView filterIndicator;
    private List<Product> products;
    private String category = "", search = "";
    private ProductItemAdapter adapter;
    private final List<String> selectedItems;
    public CategoryFilteredBodyFragment(List<String> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public CategoryFilteredBodyFragment() {
        this.selectedItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_filtered_fragment, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            category = bundle.getString("category");
            search = bundle.getString("search");
        }
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        RecyclerView filterRecyclerView = view.findViewById(R.id.list);
        filterIndicator = view.findViewById(R.id.filterIndicator);

        setFilterIndicatorVisibility(!selectedItems.isEmpty());
        List<FilterType> filterSections = Arrays.asList(FilterType.SIZE);
        FilterItemAdapter filterItemAdapter = new FilterItemAdapter(filterSections,selectedItems, category, search, this);

        filterRecyclerView.setAdapter(filterItemAdapter);

        Context context = getActivity();
        mainActivity = (MainActivity) getActivity();

        ProductRepository productRepository = mainActivity.productRepository;
        products = productRepository.getProductsByCategory(category);
        updateFilterProducts(products);

        if(search != null && !search.isEmpty()) {
            products = productRepository.getProductsBySizeOptionsCategoryAndSearch(selectedItems, category, search);
            updateFilterProducts(products);
        }
        else {
            products = productRepository.getProductsBySizeOptionsAndCategory(selectedItems, category);
            updateFilterProducts(products);
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
    public void onFilterApplied(List<String> selectedItems, String category) {
        ProductRepository productRepository = getMainActivityInstance().productRepository;
        setFilterIndicatorVisibility(!selectedItems.isEmpty());
        updateFilterProducts(productRepository.getProductsBySizeOptionsCategoryAndSearch(selectedItems, category, search));
    }

    // Set the product count update listener
    public void setProductCountUpdateListener(OnProductCountUpdateListener onProductCountUpdateListener) {
        this.onProductCountUpdateListener = onProductCountUpdateListener;
    }

    // Update the product count
    private void updateProductCount(int count) {
        if (onProductCountUpdateListener != null) {
            onProductCountUpdateListener.onProductCountUpdate(count);
        }
    }

    // Update the product list after applying the filter in the CategoryFilteredFragment
    private void updateFilterProducts(List<Product> products) {
        this.products = products;
        adapter = new ProductItemAdapter(products, this);
        productRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateProductCount(products.size());
    }

    @Override
    public void onPause() {
        super.onPause();
        MySharedPreferences sharedPreferences = new MySharedPreferences(requireContext());
        sharedPreferences.putUserFavoriteList(getMainActivityInstance().userInteraction.getFavoriteList());
    }

    private void setFilterIndicatorVisibility(boolean isVisible) {
        if (isVisible) {
            filterIndicator.setVisibility(View.VISIBLE);
        } else {
            filterIndicator.setVisibility(View.GONE);
        }
    }
}
