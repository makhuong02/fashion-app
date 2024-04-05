package com.group25.ecommercefashionapp.ui.fragment.category;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.FilterItemAdapter;
import com.group25.ecommercefashionapp.adapter.ProductItemAdapter;
import com.group25.ecommercefashionapp.cache.ProductCache;
import com.group25.ecommercefashionapp.data.FilterType;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.interfaces.callback.FilterDialogCallback;
import com.group25.ecommercefashionapp.interfaces.callback.OnProductCountUpdateListener;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnItemClickListener;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.ui.activity.MainActivity;
import com.group25.ecommercefashionapp.ui.decorations.ProductItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFilteredBodyFragment extends Fragment implements OnItemClickListener, FilterDialogCallback{
    private MainActivity mainActivity;
    private Context context;
    private RecyclerView productRecyclerView;
    private OnProductCountUpdateListener onProductCountUpdateListener;
    private ImageView filterIndicator;
    private List<Product> products = new ArrayList<>();
    private String category = "", search = "";
    private Long categoryId= 0L;
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
            categoryId = bundle.getLong("categoryId");
        }
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        RecyclerView filterRecyclerView = view.findViewById(R.id.list);
        filterIndicator = view.findViewById(R.id.filterIndicator);

        setFilterIndicatorVisibility(!selectedItems.isEmpty());
        List<FilterType> filterSections = Collections.singletonList(FilterType.SIZE);
        FilterItemAdapter filterItemAdapter = new FilterItemAdapter(filterSections, selectedItems, category, search, this);

        filterRecyclerView.setAdapter(filterItemAdapter);

        mainActivity = (MainActivity) getActivity();
        context = getActivity();

        if (ProductCache.getInstance().containsCategory(categoryId)) {
            products = ProductCache.getInstance().getProducts(categoryId);
            setupRecyclerView();
        } else{
            ProductRepository.getInstance().fetchProductsByCategoryIdFromApi(categoryId, context, new Callback<List<Product>>() {
                @Override
                public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                    if (response.isSuccessful()) {
                        products = response.body();
                        setupRecyclerView();
                    } else {
                        Toast.makeText(context, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        updateFilterProducts(products);


        return view;
    }

    @Override
    public void onItemClick(View view, Item item) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", item.getId());
        mainActivity.navController.navigate(R.id.viewProduct, bundle);
    }
    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        productRecyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ProductItemAdapter(products, this);
        productRecyclerView.setAdapter(adapter);

        int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.product_vertical_spacing);
        int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.product_horizontal_spacing);
        productRecyclerView.addItemDecoration(new ProductItemDecoration(requireContext(), verticalSpacing, horizontalSpacing));

    }

    @Override
    public void onFilterApplied(List<String> selectedItems, String category) {
//        ProductRepository productRepository = getMainActivityInstance().productRepository;
        setFilterIndicatorVisibility(!selectedItems.isEmpty());
//        updateFilterProducts(productRepository.getProductsBySizeOptionsCategoryAndSearch(selectedItems, category, search));
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
        sharedPreferences.saveUserFavoriteList(getMainActivityInstance().userInteraction.getFavoriteList());
    }

    private void setFilterIndicatorVisibility(boolean isVisible) {
        if (isVisible) {
            filterIndicator.setVisibility(View.VISIBLE);
        } else {
            filterIndicator.setVisibility(View.GONE);
        }
    }
}
