package com.group25.ecommercefashionapp.ui.activity;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.group25.ecommercefashionapp.MySharedPreferences;
import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.ProductColorAdapter;
import com.group25.ecommercefashionapp.adapter.ProductImageCarouselAdapter;
import com.group25.ecommercefashionapp.adapter.ProductSizeAdapter;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.Item;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.ProductColor;
import com.group25.ecommercefashionapp.data.ProductImage;
import com.group25.ecommercefashionapp.data.ProductSize;
import com.group25.ecommercefashionapp.data.UserInteraction;
import com.group25.ecommercefashionapp.layoutmanager.GridAutoFitLayoutManager;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.ui.fragment.dialog.CartAddedDialogFragment;
import com.group25.ecommercefashionapp.ui.widget.FavoriteCheckBox;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class ViewProductActivity extends AppCompatActivity implements OnItemClickListener {
    MaterialToolbar toolbar;
    MainActivity mainActivity;
    Button addToCartButton;
    TextView txtName, txtActualPrice, txtDiscountPrice, txtId, txtHighlight, txtRating, txtReview, selectedColorTextView, selectedSizeTextView;
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
    ProductRepository productRepository;
    Product product;
    Spinner spinner;
    FavoriteCheckBox favoriteCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);

        mainActivity = getMainActivityInstance();

        UserInteraction UserInteraction = mainActivity.userInteraction;
        List<Product> favoriteList = UserInteraction.getFavoriteList();

        productRepository = mainActivity.productRepository;
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        product = productRepository.getProductById(id);


        initializeViews();

        favoriteCheckBox.setOnClickListener(v -> {
            if (favoriteCheckBox.isChecked()) {
                UserInteraction.addFavorite(product);
            } else {
                UserInteraction.removeFavorite(product);
            }
        });

        if(favoriteList.size() != 0) {
            for (Product product : favoriteList) {
                if (product.getId() == id) {
                    favoriteCheckBox.setChecked(true);
                    break;
                }
            }
        }

        txtName.setText(product.getName());
        txtHighlight.setText(product.getDescription());
        txtActualPrice.setText(getString(R.string.product_price, VNDFormat.format(product.getPrice())));
        txtActualPrice.setPaintFlags(txtActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtDiscountPrice.setText(getString(R.string.product_price, VNDFormat.format(product.getPrice() * 0.9f)));
        txtId.setText(getString(R.string.product_id, String.valueOf(bundle.getInt("id"))));
        float rating = 3.8f;
        ratingBar.setRating(rating);
        txtRating.setText(String.valueOf(rating));
        txtReview.setText(HtmlCompat.fromHtml("<font color=\"blue\"><u>(See 5 reviews)</u></font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        List<ProductImage> imageList = mainActivity.productRepository.getProductById(id).getImageList();

        ProductImageCarouselAdapter productImageCarouselAdapter = new ProductImageCarouselAdapter(this, imageList, this);
        productCarousel.setAdapter(productImageCarouselAdapter);
        share.setOnClickListener(v -> shareContent());

        // Set up Color recycler view
        GridAutoFitLayoutManager colorGridLayoutManager = new GridAutoFitLayoutManager(this, 0, GridLayoutManager.VERTICAL, false);
        colorRecyclerView.setLayoutManager(colorGridLayoutManager);
        ProductColorAdapter colorAdapter = new ProductColorAdapter(product.getColorList(), this, colorRecyclerView);
        colorRecyclerView.setAdapter(colorAdapter);

        // Set up Size recycler view
        GridAutoFitLayoutManager sizeGridLayoutManager = new GridAutoFitLayoutManager(this, 0, GridLayoutManager.VERTICAL, false);
        sizeRecyclerView.setLayoutManager(sizeGridLayoutManager);
        ProductSizeAdapter sizeAdapter = new ProductSizeAdapter(product.getSizeList(), this, sizeRecyclerView);
        sizeRecyclerView.setAdapter(sizeAdapter);

        ArrayAdapter<CharSequence> spinnerEntries = new ArrayAdapter<>(getMainActivityInstance(), android.R.layout.simple_spinner_item);
        spinnerEntries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 1; i <= product.getAvailableQuantity() && i <= 5; i++) {
            spinnerEntries.add(String.valueOf(i));
        }
        spinner.setAdapter(spinnerEntries);

        addToCartButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(spinner.getSelectedItem().toString());
            CartItem cartItem = new CartItem(id, quantity, selectedColor, selectedSize);
            mainActivity.userInteraction.addCart(cartItem);
            CartAddedDialogFragment cartAddedDialogFragment = new CartAddedDialogFragment(quantity, (long) (product.getPrice() * 0.9f * quantity));
            cartAddedDialogFragment.show(getSupportFragmentManager(), "cart_added");
        });

        toolbar.setNavigationOnClickListener(v -> {
            mainActivity.navController.popBackStack();
            onBackPressed();
        });
        cart.setOnClickListener(v ->
        {
            if (UserStatus._isLoggedIn) {
                getMainActivityInstance().navController.navigate(R.id.cartActivity);
            } else {
                getMainActivityInstance().navController.navigate(R.id.loginActivity);
            }
        });
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
        } else if (view.getId() == R.id.chip_size_card) {
            selectedSizeTextView.setText(item.getName());
            selectedSize = (ProductSize) item;
        }else if(view.getId() == R.id.carousel_image_view){
            Bundle bundle = new Bundle();
            bundle.putInt("product_id", product.getId());
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
                int id = bundle.getInt("product_id");
                int position = bundle.getInt("position");
                Product product = mainActivity.productRepository.getProductById(id);
                ProductImageCarouselAdapter productImageCarouselAdapter = new ProductImageCarouselAdapter(this, product.getImageList(), this);
                productCarousel.setAdapter(productImageCarouselAdapter);
                productCarousel.setCurrentItem(position);
            }
        }
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
    }
    public void onFavoriteButtonClicked(View view) {
        favoriteCheckBox.performClick(); // This will simulate a click on the FavoriteCheckBox
    }
    @Override
    public void onPause() {
        super.onPause();
        MySharedPreferences sharedPreferences = new MySharedPreferences(this);
        sharedPreferences.putUserFavoriteList(getMainActivityInstance().userInteraction.getFavoriteList());
        sharedPreferences.putUserCartList(getMainActivityInstance().userInteraction.getCartList());
    }
}
