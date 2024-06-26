package com.group25.ecommercefashionapp.adapter;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.api.ApiServiceBuilder;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.ProductColor;
import com.group25.ecommercefashionapp.data.ProductQuantity;
import com.group25.ecommercefashionapp.data.ProductSize;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnItemClickListener;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.ui.widget.ChipImagesView;
import com.group25.ecommercefashionapp.ui.widget.FavoriteCheckBox;
import com.group25.ecommercefashionapp.utilities.SizeUtils;
import com.group25.ecommercefashionapp.utilities.TokenUtils;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ViewHolder> {
    private final List<Product> items;
    private final OnItemClickListener clickListener;
    private final DecimalFormat VNDFormat;

    public ProductItemAdapter(List<Product> items, OnItemClickListener clickListener) {
        this.items = items;
        this.clickListener = clickListener;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product item = items.get(position);

        // Bind your data to the UI components of the CardView
        holder.txtName.setText(item.getName());
        holder.txtDiscount.setText(String.format("%s VND", VNDFormat.format(item.getPrice())));
        holder.txtDiscount.setPaintFlags(holder.txtDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtPrice.setText(String.format("%s VND", VNDFormat.format(item.getPrice() * 0.9f)));

        // Set click listener on the card
        holder.cardView.setOnClickListener(v -> clickListener.onItemClick(v, item));

        for (Product product : getMainActivityInstance().userInteraction.getFavoriteList()) {
            if (Objects.equals(product.getId(), item.getId())) {
                holder.favoriteCheckBox.setChecked(true);
                break;
            }
        }
        holder.favoriteCheckBox.setOnClickListener(v -> {
            if (holder.favoriteCheckBox.isChecked()) {
                addUserFavoriteProduct(item);
            } else {
                removeUserFavoriteProduct(item);
            }
        });
        fetchProductQuantitiesFromApi(item.getId(), holder);
        String imageNames = "";
        if(!item.getImageList().isEmpty()) {
            imageNames = item.getImageList().get(0).getImagePath();
        }
        Picasso.get()
                .load(ApiServiceBuilder.BASE_URL +"product-images/"+ imageNames)
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.ic_connection_error)
                .into(holder.img);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName, txtSizeRange, txtPrice, txtDiscount;
        CardView cardView;
        FavoriteCheckBox favoriteCheckBox;
        ChipImagesView chipImagesView;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.product_imageView);
            txtName = view.findViewById(R.id.productName);
            txtSizeRange = view.findViewById(R.id.product_size_range);
            txtPrice = view.findViewById(R.id.price_view);
            cardView = view.findViewById(R.id.productCardView);
            chipImagesView = view.findViewById(R.id.chip_imagesView);
            favoriteCheckBox = view.findViewById(R.id.favorite_checkBox);
            txtDiscount = view.findViewById(R.id.price_discount_view);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void addUserFavoriteProduct(Product item) {
        if(UserStatus._isLoggedIn) {
            UserRepository.getInstance().addFavoriteProduct(TokenUtils.bearerToken(UserStatus.access_token.token), item.getId(), getMainActivityInstance().getApplicationContext());
        }
        else {
            getMainActivityInstance().userInteraction.addFavorite(item);
        }
    }

    private void removeUserFavoriteProduct(Product item) {
        if(UserStatus._isLoggedIn) {
            UserRepository.getInstance().removeFavoriteProduct(TokenUtils.bearerToken(UserStatus.access_token.token), item.getId(), getMainActivityInstance().getApplicationContext());
        }
        else {
            getMainActivityInstance().userInteraction.removeFavorite(item);
        }
    }

    private void fetchProductQuantitiesFromApi(Long productId, ViewHolder holder) {
        ProductRepository.getInstance().fetchProductQuantitiesFromApi(productId, getMainActivityInstance().getApplicationContext(), new Callback<List<ProductQuantity>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductQuantity>> call, @NonNull Response<List<ProductQuantity>> response) {
                if (response.isSuccessful()) {
                    List<ProductQuantity> productQuantities = response.body();


                    Set<ProductColor> uniqueColors = new TreeSet<>(Comparator.comparing(ProductColor::getHexCode));
                    assert productQuantities != null;
                    productQuantities.forEach(productQuantity -> uniqueColors.add(productQuantity.getColor()));

                    List<ProductColor> colors = new ArrayList<>(uniqueColors);
                    List<String> colorChips = new ArrayList<>();
                    colors.forEach(productColor -> colorChips.add(productColor.getHexCode()));
                    ChipImagesView.setChips(holder.chipImagesView, colorChips);

                    List<ProductSize> sizes = new ArrayList<>();
                    productQuantities.forEach(productQuantity -> sizes.add(productQuantity.getSize()));

                    holder.txtSizeRange.setVisibility(View.VISIBLE);
                    holder.txtSizeRange.setText(getSizeRange(sizes));
                }
                else {
                    // Handle unsuccessful response
                    Toast.makeText(getMainActivityInstance().getApplicationContext(), "Failed to fetch product quantities", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductQuantity>> call, @NonNull Throwable t) {
                // Handle failure
                Toast.makeText(getMainActivityInstance().getApplicationContext(), "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getSizeRange(List<ProductSize> sizes) {
        String sizeRange = "";
        if (!sizes.isEmpty()) {
            List<ProductSize> sortedSizes = SizeUtils.sortSizes(sizes);
            sizeRange = sortedSizes.get(0).getSize();
            if (sortedSizes.size() > 1) {
                sizeRange += " - " + sortedSizes.get(sortedSizes.size() - 1).getSize();
            }
        }
        return sizeRange;
    }
}
