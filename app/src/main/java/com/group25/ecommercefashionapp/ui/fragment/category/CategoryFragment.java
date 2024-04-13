package com.group25.ecommercefashionapp.ui.fragment.category;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.CustomCategoryItemAdapter;
import com.group25.ecommercefashionapp.api.ApiService;
import com.group25.ecommercefashionapp.data.CategoryItem;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnListItemClick;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.ui.activity.MainActivity;
import com.group25.ecommercefashionapp.ui.decorations.ProductItemDecoration;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment implements OnListItemClick {
    MainActivity mainActivity;
    Context context = null;
    RecyclerView categoryRecyclerView;
    List<CategoryItem> categories;
    SwipeRefreshLayout swipeRefreshLayout;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.category, container, false);

        // Initialize views and handle logic specific to this fragment
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        context = getActivity();
        mainActivity = (MainActivity) getActivity();
        initRecyclerView();

        fetchCategoriesFromApi();


        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchCategoriesFromApi();
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    private void initRecyclerView() {
        int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.product_vertical_spacing);
        int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.product_horizontal_spacing);
        categoryRecyclerView.addItemDecoration(new ProductItemDecoration(context, verticalSpacing, horizontalSpacing));
    }

    private void fetchCategoriesFromApi() {
        ApiService apiService = ProductRepository.getInstance().getApiService();
        Call<List<CategoryItem>> call = apiService.getCategories();
        call.enqueue(new Callback<List<CategoryItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryItem>> call, Response<List<CategoryItem>> response) {
                if (response.isSuccessful()) {
                    categories = response.body();
                    setupRecyclerView();
                } else {
                    Toast.makeText(context, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryItem>> call, Throwable t) {
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        categoryRecyclerView.setLayoutManager(gridLayoutManager);

        CustomCategoryItemAdapter adapter = new CustomCategoryItemAdapter(categories, this);
        categoryRecyclerView.setAdapter(adapter);

    }


    @Override
    public void onItemClick(Item item, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("category", categories.get(position).getName());
        bundle.putLong("categoryId", categories.get(position).getId());
        mainActivity.navController.navigate(R.id.action_categoryBotNav_to_filterCategory, bundle);

    }
}
