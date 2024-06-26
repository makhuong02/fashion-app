package com.group25.ecommercefashionapp.ui.fragment.home;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.JsonElement;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.ProductItemAdapter;
import com.group25.ecommercefashionapp.data.CategoryItem;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.UserInteraction;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnItemClickListener;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.ui.activity.MainActivity;
import com.group25.ecommercefashionapp.ui.decorations.ProductItemDecoration;
import com.group25.ecommercefashionapp.utilities.TokenUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnItemClickListener {
    MainActivity mainActivity;
    Context context = null;
    ImageSlider imageSlider;
    Button categoryButton;
    MaterialCardView tShirtCardView, skirtCardView, sneakersCardView, pantsCardView, walletCardView;
    RecyclerView productRecyclerView;
    List<Product> products;
    ArrayList<CategoryItem> categories;
    ProductItemAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home, container, false);

        // Initialize views and handle logic specific to this fragment
        imageSlider = view.findViewById(R.id.imageSlider);
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        categoryButton = view.findViewById(R.id.homeToCategoryBtn);
        tShirtCardView = view.findViewById(R.id.tshirtCardView);
        skirtCardView = view.findViewById(R.id.skirtCardView);
        sneakersCardView = view.findViewById(R.id.sneakersCardView);
        pantsCardView = view.findViewById(R.id.pantsCardView);
        walletCardView = view.findViewById(R.id.walletCardView);


        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.image1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image3, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
        imageSlider.startSliding(3000);


        context = getActivity();
        mainActivity = (MainActivity) getActivity();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        productRecyclerView.setLayoutManager(gridLayoutManager);

        int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.product_vertical_spacing);
        int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.product_horizontal_spacing);
        productRecyclerView.addItemDecoration(new ProductItemDecoration(requireContext(), verticalSpacing, horizontalSpacing));
        ProductRepository.getInstance().fetchProductsFromApi(context, new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();
                    if (jsonElement != null) {
                        products = ProductRepository.getInstance().parseJsonToProducts(jsonElement);
                        if(UserStatus._isLoggedIn) {
                            fetchFavoriteListFromApi();
                        }

                    } else {
                        Toast.makeText(context, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });


        categoryButton.setOnClickListener(v -> mainActivity.navController.navigate(R.id.categoryBotNav));

        categories = getHomePageCategories();
        tShirtCardView.setOnClickListener(v -> categoryClick(0));

        skirtCardView.setOnClickListener(v -> categoryClick(1));

        sneakersCardView.setOnClickListener(v -> categoryClick(2));

        pantsCardView.setOnClickListener(v -> categoryClick(3));

        walletCardView.setOnClickListener(v -> categoryClick(4));

        return view;
    }

    private void setupRecyclerView() {
        int subListSize = Math.min(products.size(), 6);

        adapter = new ProductItemAdapter(products.subList(0,subListSize), this);
        productRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, Item item) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", item.getId());
        mainActivity.navController.navigate(R.id.viewProduct, bundle);
    }

    private void categoryClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("category", categories.get(position).getName());
        mainActivity.navController.navigate(R.id.action_homeBotNav_to_filterCategory, bundle);
    }

    private ArrayList<CategoryItem> getHomePageCategories() {
        ArrayList<CategoryItem> items = new ArrayList<>();
        items.add(new CategoryItem("Áo thun", ""));
        items.add(new CategoryItem("Váy", ""));
        items.add(new CategoryItem("Giày", ""));
        items.add(new CategoryItem("Quần", ""));
        items.add(new CategoryItem("Bóp", ""));
        return items;
    }

    private void fetchFavoriteListFromApi() {
        UserRepository.getInstance().fetchFavoriteList(TokenUtils.bearerToken(UserStatus.access_token.token), getMainActivityInstance().getApplicationContext(), new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    UserInteraction userInteraction = getMainActivityInstance().userInteraction;
                    userInteraction.setFavoriteList(response.body());
                    setupRecyclerView();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }
}
