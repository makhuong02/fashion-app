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

import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.FavoriteProductAdapter;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.User;

import java.util.List;

public class FavoriteProductListFragment extends Fragment implements OnItemClickListener {
    TextView favoriteCountTextView;
    RecyclerView favoriteRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        User user = getMainActivityInstance().user;
        List<Product> favoriteList = user.getFavoriteList();

        if(favoriteList.size() == 0) {
            View view = inflater.inflate(R.layout.cell_favorite_product_empty, container, false);
            favoriteCountTextView = view.findViewById(R.id.text_favorite_count);
            favoriteCountTextView.setText(getString(R.string.text_favorite_count_item, favoriteList.size()));

            return view;
        }

        View view = inflater.inflate(R.layout.fragment_favorite_product_list, container, false);


        favoriteCountTextView = view.findViewById(R.id.text_favorite_count);
        favoriteRecyclerView = view.findViewById(R.id.product_list);

        favoriteCountTextView.setText(getString(R.string.text_favorite_count_item, favoriteList.size()));
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteRecyclerView.setAdapter(new FavoriteProductAdapter(favoriteList, this));

        return view;
    }

    @Override
    public void onItemClick(View view, Item item) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        getMainActivityInstance().navController.navigate(R.id.viewProduct, bundle);
    }
}