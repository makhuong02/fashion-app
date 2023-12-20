package com.group25.ecommercefashionapp.ui.fragment.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.ui.activity.MainActivity;
import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.ProductItemAdapter;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.repository.ProductRepository;

import java.util.ArrayList;

public class CategoryFilteredFragment extends Fragment implements OnItemClickListener {
    MainActivity mainActivity;
    Context context = null;
    MaterialToolbar toolbar;

    ActionMenuItemView cart;
    RecyclerView productRecyclerView;
    ArrayList<Product> products;

    public CategoryFilteredFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_filtered, container, false);

        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        toolbar = view.findViewById(R.id.topAppBar);
        cart = view.findViewById(R.id.cart);
        String category = getArguments().getString("category");
        toolbar.setTitle(category);

        context = getActivity();
        mainActivity = (MainActivity) getActivity();

        cart.setOnClickListener(v -> {
            mainActivity.navController.navigate(R.id.cartActivity);
        });

        ProductRepository productRepository = mainActivity.productRepository;
        products = productRepository.getProductsByCategory(category);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        productRecyclerView.setLayoutManager(gridLayoutManager);

        ProductItemAdapter adapter = new ProductItemAdapter(products, this);
        productRecyclerView.setAdapter(adapter);

        toolbar.setNavigationOnClickListener(v -> mainActivity.navController.popBackStack());

//        toolbar.setOnMenuItemClickListener(item -> {
//            if (item.getItemId() == R.id.cart) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("id", 1);
//                mainActivity.navController.navigate(R.id.autoFitChipImagesView,bundle);
//            }
//            return false;
//        });
        mainActivity.updateNavigationBarState(R.id.categoryBotNav);
        return view;
    }


    @Override
    public void onItemClick(View view, Item item) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        mainActivity.navController.navigate(R.id.viewProduct, bundle);
    }
    private void shareContent() {
        // Create an Intent to share content
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Your content to share");

        // Start the activity for sharing
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
    }
}
