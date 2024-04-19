package com.group25.ecommercefashionapp.ui.activity;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.JsonElement;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.ProductColorAdapter;
import com.group25.ecommercefashionapp.adapter.ProductImageCarouselAdapter;
import com.group25.ecommercefashionapp.adapter.ProductSizeAdapter;
import com.group25.ecommercefashionapp.data.*;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnItemClickListener;
import com.group25.ecommercefashionapp.layoutmanager.GridAutoFitLayoutManager;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.ui.fragment.dialog.CartAddedDialogFragment;
import com.group25.ecommercefashionapp.ui.widget.FavoriteCheckBox;
import com.group25.ecommercefashionapp.utilities.SizeUtils;
import com.group25.ecommercefashionapp.utilities.TokenUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

import io.jsonwebtoken.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProductActivity extends AppCompatActivity implements OnItemClickListener {
    MaterialToolbar toolbar;
    MainActivity mainActivity;
    Context context;
    Button addToCartButton;
    TextView txtName, txtActualPrice, txtDiscountPrice, txtId, txtHighlight, txtRating, txtReview, selectedColorTextView, selectedSizeTextView, availableQuantityTextView;
    private final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    RatingBar ratingBar;
    private ProductColor selectedColor = null;
    private ProductSize selectedSize = null;
    ActionMenuItemView cart;
    RecyclerView colorRecyclerView, sizeRecyclerView;
    ViewPager productCarousel;
    private DecimalFormat VNDFormat;
    ActionMenuItemView share;
    private static final int VIEW_PRODUCT_IMAGES_REQUEST_CODE = 1;
    Product product;
    Spinner spinner;
    FavoriteCheckBox favoriteCheckBox;
    private long productId;

    List<Product> favoriteList = new ArrayList<>();
    List<ProductQuantity> productQuantities = new ArrayList<>();
    List<ProductColor> colorList = new ArrayList<>();
    List<ProductSize> sizeList = new ArrayList<>();
    private Integer productQuantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        mainActivity = getMainActivityInstance();
        context = getMainActivityInstance();

        Bundle bundle = getIntent().getExtras();
        productId = bundle.getLong("id");

        fetchProductByProductIdFromApi(productId);
    }

    private void initializeViews() {
        txtName = findViewById(R.id.productNameTextView);
        txtHighlight = findViewById(R.id.productHighlightsTextView);
        txtActualPrice = findViewById(R.id.productActualPriceTextView);
        txtDiscountPrice = findViewById(R.id.productDiscountPriceTextView);
        txtRating = findViewById(R.id.ratingTextView);
        txtReview = findViewById(R.id.seeReviewsTextView);
        ratingBar = findViewById(R.id.ratingBar);
        txtId = findViewById(R.id.productIDTextView);
        productCarousel = findViewById(R.id.carousel);
        toolbar = findViewById(R.id.topAppBar);
        share = toolbar.findViewById(R.id.share);
        cart = toolbar.findViewById(R.id.cart);
        selectedColorTextView = findViewById(R.id.selectColorText);
        colorRecyclerView = findViewById(R.id.colorRecycler);
        selectedSizeTextView = findViewById(R.id.selectSizeText);
        sizeRecyclerView = findViewById(R.id.sizeRecycler);
        toolbar = findViewById(R.id.topAppBar);
        addToCartButton = findViewById(R.id.addToCartButton);
        spinner = findViewById(R.id.productQuantitySpinner);
        favoriteCheckBox = findViewById(R.id.favorite_button);
        availableQuantityTextView = findViewById(R.id.productAvailableQuantityTextView);
    }

    private void setupFavoriteCheckBox() {
        favoriteCheckBox.setOnClickListener(v -> {
            if (favoriteCheckBox.isChecked()) {
                addFavoriteProduct();
            } else {
                removeFavoriteProduct();
            }
        });

        if (!favoriteList.isEmpty()) {
            for (Product product : favoriteList) {
                if (product.getId() == productId) {
                    favoriteCheckBox.setChecked(true);
                    break;
                }
            }
        }
    }

    private void fetchProductByProductIdFromApi(Long productId) {
        ProductRepository.getInstance().fetchProductByProductIdFromApi(productId, getApplicationContext(), new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    JsonElement jsonElement = response.body();
                    product = ProductRepository.getInstance().parseJsonToProduct(jsonElement);
                    fetchProductQuantitiesFromApi();
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(context, "Failed to fetch product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                // Handle network error
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFavoriteListFromApi() {
        UserRepository.getInstance().fetchFavoriteList(TokenUtils.bearerToken(UserStatus.access_token.token), getMainActivityInstance().getApplicationContext(), new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    favoriteList = response.body();
                    setupFavoriteCheckBox();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }

    private void fetchProductQuantitiesFromApi() {
        ProductRepository.getInstance().fetchProductQuantitiesFromApi(productId, getApplicationContext(), new Callback<List<ProductQuantity>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductQuantity>> call, @NonNull Response<List<ProductQuantity>> response) {
                if (response.isSuccessful()) {
                    productQuantities = response.body();
                    Set<ProductColor> uniqueColors = new TreeSet<>(Comparator.comparing(ProductColor::getHexCode));
                    assert productQuantities != null;
                    productQuantities.forEach(productQuantity -> uniqueColors.add(productQuantity.getColor()));
                    colorList = new ArrayList<>(uniqueColors);
                    sizeList = getSizeList(productQuantities, colorList.get(0));
                    productQuantity = productQuantities.get(0).getQuantity();
                    if(UserStatus._isLoggedIn) {
                        init();
                        fetchFavoriteListFromApi();
                    }
                    else {
                        favoriteList = mainActivity.userInteraction.getFavoriteList();
                        init();
                        setupFavoriteCheckBox();
                    }
                }
                else {
                    // Handle unsuccessful response
                    Toast.makeText(context, "Failed to fetch product quantities", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductQuantity>> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }

    private List<ProductSize> getSizeList(List<ProductQuantity> productQuantities, ProductColor color) {
        List<ProductSize> sizeList = new ArrayList<>();
        for(ProductQuantity productQuantity : productQuantities) {
            if(Objects.equals(productQuantity.getColor().getId(), color.getId())) {
                sizeList.add(productQuantity.getSize());
            }
        }

        return SizeUtils.sortSizes(sizeList);
    }

    private Integer getProductQuantity(List<ProductQuantity> productQuantities, ProductColor color, ProductSize size) {
        for(ProductQuantity productQuantity : productQuantities) {
            if(Objects.equals(productQuantity.getColor().getId(), color.getId()) && Objects.equals(productQuantity.getSize().getId(), size.getId())) {
                return productQuantity.getQuantity();
            }
        }
        return 0;
    }

    private void addFavoriteProduct() {
        if(UserStatus._isLoggedIn) {
            UserRepository.getInstance().addFavoriteProduct(TokenUtils.bearerToken(UserStatus.access_token.token), productId, getApplicationContext());
        }
        mainActivity.userInteraction.addFavorite(product);
    }

    private void removeFavoriteProduct() {
        if(UserStatus._isLoggedIn) {
            UserRepository.getInstance().removeFavoriteProduct(TokenUtils.bearerToken(UserStatus.access_token.token), productId, getApplicationContext());
        }
        mainActivity.userInteraction.removeFavorite(product);
    }

    private void displayProductDetails(Product product) {
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);
        txtName.setText(product.getName());
        txtHighlight.setText(product.getDescription());
        txtActualPrice.setText(getString(R.string.product_price, VNDFormat.format(product.getPrice())));
        txtActualPrice.setPaintFlags(txtActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtDiscountPrice.setText(getString(R.string.product_price, VNDFormat.format(product.getPrice() * 0.9f)));
        txtId.setText(getString(R.string.product_id, String.valueOf(product.getId())));
        float rating = 3.8f;
        ratingBar.setRating(rating);
        txtRating.setText(String.valueOf(rating));
        txtReview.setText(HtmlCompat.fromHtml("<font color=\"blue\"><u>(See 5 reviews)</u></font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void setupProductImagesCarousel(List<ProductImage> imageList) {
        ProductImageCarouselAdapter productImageCarouselAdapter = new ProductImageCarouselAdapter(this, imageList, this);
        productCarousel.setAdapter(productImageCarouselAdapter);
        share.setOnClickListener(v -> shareContent());
    }

    private void setupColorRecyclerView(List<ProductColor> colorList) {
        GridAutoFitLayoutManager colorGridLayoutManager = new GridAutoFitLayoutManager(this, 0, GridLayoutManager.VERTICAL, false);
        colorRecyclerView.setLayoutManager(colorGridLayoutManager);
        ProductColorAdapter colorAdapter = new ProductColorAdapter(colorList, this, colorRecyclerView);
        colorRecyclerView.setAdapter(colorAdapter);
    }

    private void setupSizeRecyclerView(List<ProductSize> sizeList) {
        GridAutoFitLayoutManager sizeGridLayoutManager = new GridAutoFitLayoutManager(this, 0, GridLayoutManager.VERTICAL, false);
        sizeRecyclerView.setLayoutManager(sizeGridLayoutManager);
        ProductSizeAdapter sizeAdapter = new ProductSizeAdapter(sizeList, this, sizeRecyclerView);
        sizeRecyclerView.setAdapter(sizeAdapter);
    }

    private void setupQuantitySpinner(int availableQuantity) {
        ArrayAdapter<CharSequence> spinnerEntries = new ArrayAdapter<>(getMainActivityInstance(), android.R.layout.simple_spinner_item);
        spinnerEntries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 1; i <= availableQuantity && i <= 5; i++) {
            spinnerEntries.add(String.valueOf(i));
        }
        spinner.setAdapter(spinnerEntries);
    }

    private void updateUI() {
        displayProductDetails(product);
        setupProductImagesCarousel(product.getImageList());
        setupColorRecyclerView(colorList);
        setupSizeRecyclerView(sizeList);
        setupQuantitySpinner(productQuantity);
        setupFavoriteCheckBox();
        setupAddToCartButton();
        initColorAndSize();
        availableQuantityTextView.setText(getString(R.string.available_quantity, productQuantity));
    }

    private void initColorAndSize() {
        selectedColor = colorList.get(0);
        selectedSize = sizeList.get(0);
        selectedColorTextView.setText(selectedColor.getName());
        selectedSizeTextView.setText(selectedSize.getSize());
    }

    private void setupShareButton() {
        share.setOnClickListener(v -> shareContent());
    }

    private void setupAddToCartButton() {
        addToCartButton.setOnClickListener(v -> {
            if (!UserStatus._isLoggedIn) {
                getMainActivityInstance().navController.navigate(R.id.loginActivity);
                return;
            }
            int quantity = Integer.parseInt(spinner.getSelectedItem().toString());
            CartItem cartItem = new CartItem(productId, quantity, selectedColor.getId(), selectedSize.getId());
            addToCart(cartItem);
        });
    }

    private void addToCart(CartItem cartItem){
        UserRepository.getInstance().addCartItem(cartItem, getApplicationContext(), new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    CartAddedDialogFragment cartAddedDialogFragment = new CartAddedDialogFragment(cartItem.getQuantity(), (long) (product.getPrice() * 0.9f * cartItem.getQuantity()));
                    cartAddedDialogFragment.show(getSupportFragmentManager(), "cart_added");
                } else if(response.code() == 400) {
                    String errorMessage;
                    try {
                        errorMessage = response.errorBody().string();
                    } catch (IOException | java.io.IOException e) {
                        errorMessage = "Failed to parse error message.";
                        e.printStackTrace();
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                // Handle network error
                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            mainActivity.navController.popBackStack();
            onBackPressed();
        });
    }

    private void setupCartButton() {
        cart.setOnClickListener(v -> {
            if (UserStatus._isLoggedIn) {
                getMainActivityInstance().navController.navigate(R.id.cartActivity);
            } else {
                getMainActivityInstance().navController.navigate(R.id.loginActivity);
            }
        });
    }

    private void setupOnClickListeners() {
        setupShareButton();
        setupAddToCartButton();
        setupToolbar();
        setupCartButton();
    }

    private void init(){
        initializeViews();
        updateUI();
        setupOnClickListeners();
    }

    private void shareContent() {
        // Create an Intent to share content
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = product.getName() + "\n" + getString(R.string.product_price, VNDFormat.format(product.getPrice() * 0.9f));
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        // Start the activity for sharing
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
    }

    @Override
    public void onItemClick(View view, Item item) {
        if (view.getId() == R.id.chip_image_card) {
            selectedColorTextView.setText( item.getName());
            selectedColor = (ProductColor) item;
            sizeList = getSizeList(productQuantities, selectedColor);
            productQuantity = getProductQuantity(productQuantities, selectedColor, sizeList.get(0));
            setupSizeRecyclerView(sizeList);
            setupQuantitySpinner(productQuantity);
            availableQuantityTextView.setText(getString(R.string.available_quantity, productQuantity));

        } else if (view.getId() == R.id.chip_size_card) {
            selectedSize = (ProductSize) item;
            selectedSizeTextView.setText(selectedSize.getSize());
            productQuantity = getProductQuantity(productQuantities, selectedColor, selectedSize);
            setupQuantitySpinner(productQuantity);
            availableQuantityTextView.setText(getString(R.string.available_quantity, productQuantity));

        }else if(view.getId() == R.id.carousel_image_view){
            Bundle bundle = new Bundle();
            bundle.putLong("product_id", product.getId());
            bundle.putInt("position", productCarousel.getCurrentItem());
            Intent intent = new Intent(this, ViewProductImagesActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, VIEW_PRODUCT_IMAGES_REQUEST_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIEW_PRODUCT_IMAGES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                int position = bundle.getInt("position");
                ProductImageCarouselAdapter productImageCarouselAdapter = new ProductImageCarouselAdapter(this, product.getImageList(), this);
                productCarousel.setAdapter(productImageCarouselAdapter);
                productCarousel.setCurrentItem(position);
            }
        }
    }
    public void onFavoriteButtonClicked(View view) {
        favoriteCheckBox.performClick(); // This will simulate a click on the FavoriteCheckBox
    }
    @Override
    public void onPause() {
        super.onPause();
        MySharedPreferences sharedPreferences = new MySharedPreferences(this);
        sharedPreferences.saveUserFavoriteList(getMainActivityInstance().userInteraction.getFavoriteList());
        sharedPreferences.saveUserCartList(getMainActivityInstance().userInteraction.getCartList());
    }
}
