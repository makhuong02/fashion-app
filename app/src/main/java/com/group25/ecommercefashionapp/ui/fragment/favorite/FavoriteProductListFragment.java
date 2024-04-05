package com.group25.ecommercefashionapp.ui.fragment.favorite;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.FavoriteProductAdapter;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.UserInteraction;
import com.group25.ecommercefashionapp.layoutmanager.LinearLayoutManagerWrapper;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.utilities.TokenUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteProductListFragment extends Fragment implements OnItemClickListener {
    TextView favoriteCountTextView;
    RecyclerView favoriteRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    FavoriteProductAdapter adapter;
    LinearLayout emptyFavoriteListLayout;
    List<Product> favoriteList = new ArrayList<>();
    private View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(UserStatus.access_token == null) {
            favoriteList = getMainActivityInstance().userInteraction.getFavoriteList();
        }
        fetchFavoriteListFromApi();

        view = inflater.inflate(R.layout.fragment_favorite_product_list, container, false);

        return view;
    }

    @Override
    public void onItemClick(View view, Item item) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", item.getId());
        getMainActivityInstance().navController.navigate(R.id.viewProduct, bundle);
    }

    private void initViews() {
        favoriteCountTextView = view.findViewById(R.id.text_favorite_count);
        favoriteRecyclerView = view.findViewById(R.id.product_list);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        emptyFavoriteListLayout = view.findViewById(R.id.empty_item_view);
        emptyFavoriteListLayout.setVisibility(View.GONE);
    }

    private void initEmptyFavoriteList() {
        emptyFavoriteListLayout = view.findViewById(R.id.empty_item_view);
        favoriteCountTextView = view.findViewById(R.id.text_favorite_count);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        favoriteRecyclerView = view.findViewById(R.id.product_list);
        emptyFavoriteListLayout.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerView() {
        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false);
        favoriteRecyclerView.setLayoutManager(linearLayoutManagerWrapper);
        adapter = new FavoriteProductAdapter(favoriteList, this);
        favoriteRecyclerView.setAdapter(adapter);
        favoriteRecyclerView.setHasFixedSize(true);
    }

    private void fetchFavoriteListFromApi() {
        UserRepository.getInstance().fetchFavoriteList(TokenUtils.bearerToken(UserStatus.access_token.token), getMainActivityInstance().getApplicationContext(), new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    updateFavoriteList(response.body());
                }
                else {
                    Toast.makeText(getMainActivityInstance().getApplicationContext(), "Failed to fetch favorite list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                // Handle failure
                Toast.makeText(getMainActivityInstance().getApplicationContext(), "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavoriteList(List<Product> fetchedList) {
        UserInteraction userInteraction = getMainActivityInstance().userInteraction;
        for (Product product : userInteraction.getFavoriteList()) {
            if (!fetchedList.contains(product)) {
                UserRepository.getInstance().addFavoriteProduct(TokenUtils.bearerToken(UserStatus.access_token.token), product.getId(), getMainActivityInstance().getApplicationContext());
            }
        }
        for (Product product : fetchedList) {
            if (!userInteraction.getFavoriteList().contains(product)) {
                userInteraction.addFavorite(product);
            }
        }
        favoriteList = userInteraction.getFavoriteList();

        if (favoriteList.size() == 0) {
            initEmptyFavoriteList();
        } else {
            initViews();
        }
        setupRecyclerView();
        setupSwipeRefreshLayout();
        updateFavoriteCount();
    }

    private void updateFavoriteCount() {
        favoriteCountTextView.setText(getString(R.string.text_favorite_count_item, favoriteList.size()));
    }

    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchFavoriteListFromApi();
            adapter.checkAndRemoveFavorite();
            updateFavoriteCount();
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null)
            adapter.checkAndRemoveFavorite();
    }
}