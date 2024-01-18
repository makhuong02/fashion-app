package com.group25.ecommercefashionapp.ui.fragment.favorite;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.List;

public class FavoriteProductListFragment extends Fragment implements OnItemClickListener {
    TextView favoriteCountTextView;
    RecyclerView favoriteRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    FavoriteProductAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UserInteraction UserInteraction = getMainActivityInstance().userInteraction;
        List<Product> favoriteList = UserInteraction.getFavoriteList();

        if(favoriteList.size() == 0) {
            View view = inflater.inflate(R.layout.cell_favorite_product_empty, container, false);
            favoriteCountTextView = view.findViewById(R.id.text_favorite_count);
            favoriteCountTextView.setText(getString(R.string.text_favorite_count_item, favoriteList.size()));
            return view;
        }

        View view = inflater.inflate(R.layout.fragment_favorite_product_list, container, false);

        favoriteCountTextView = view.findViewById(R.id.text_favorite_count);
        favoriteRecyclerView = view.findViewById(R.id.product_list);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        favoriteCountTextView.setText(getString(R.string.text_favorite_count_item, favoriteList.size()));

        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false);
        favoriteRecyclerView.setLayoutManager(linearLayoutManagerWrapper);

        adapter = new FavoriteProductAdapter(favoriteList, this);
        favoriteRecyclerView.setAdapter(adapter);

        favoriteRecyclerView.setHasFixedSize(true);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.checkAndRemoveFavorite();
            favoriteCountTextView.setText(getString(R.string.text_favorite_count_item, favoriteList.size()));
            if(favoriteList.size() == 0) {
                getMainActivityInstance().navController.popBackStack();
                getMainActivityInstance().navController.navigate(R.id.favoriteBotNav);
            }
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });
        return view;
    }

    @Override
    public void onItemClick(View view, Item item) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        getMainActivityInstance().navController.navigate(R.id.viewProduct, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null)
            adapter.checkAndRemoveFavorite();
    }
}
