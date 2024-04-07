package com.group25.ecommercefashionapp.adapter;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.api.ApiServiceBuilder;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.ProductColor;
import com.group25.ecommercefashionapp.data.UserInteraction;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnItemClickListener;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.status.UserStatus;
import com.group25.ecommercefashionapp.ui.widget.FavoriteCheckBox;
import com.group25.ecommercefashionapp.utilities.TokenUtils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FavoriteProductAdapter extends RecyclerView.Adapter<FavoriteProductAdapter.ViewHolder>{
    private final List<Product> items;
    private boolean shouldRemoveFavorite = false;
    private final List<Product> removedItems = new ArrayList<>();
    private final OnItemClickListener clickListener;
    private final DecimalFormat VNDFormat;
    public FavoriteProductAdapter(List<Product> items, OnItemClickListener clickListener) {
        this.items = items;
        this.clickListener = clickListener;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);
    }

    @NonNull
    @Override
    public FavoriteProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_favorite_product, parent, false);
        return new FavoriteProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteProductAdapter.ViewHolder holder, int position) {
        Product item = items.get(position);

        // Bind your data to the UI components of the CardView
        holder.txtName.setText(item.getName());
        holder.txtProductID.setText(String.valueOf(item.getId()));
        List<ProductColor> colorList = item.getColorList();
        if(colorList.size() > 0) {
            holder.txtColor.setText(colorList.get(0).getName());
        } else {
            holder.txtColor.setVisibility(View.GONE);
        }
        holder.txtActualPrice.setText(String.format("%s VND",VNDFormat.format(item.getPrice())));
        holder.txtActualPrice.setPaintFlags(holder.txtActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtDiscountPrice.setText(String.format("%s VND", VNDFormat.format(item.getPrice() * 0.9f)));
        String imageNames = "";
        if(item.getImageList().size() != 0) {
            imageNames = item.getImageList().get(0).getImagePath();
        }

        Picasso.get()
                .load(ApiServiceBuilder.BASE_URL +"public/product-images/"+ imageNames)
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.ic_connection_error)
                .into(holder.img);

        // Set click listener on the card
        if(position == items.size() - 1) {
            holder.divider.setVisibility(View.GONE);
        }
        holder.favoriteButton.setChecked(true);
        holder.productLayout.setOnClickListener(v -> clickListener.onItemClick(v, item));
        holder.favoriteButton.setOnClickListener(v -> {
            if(holder.favoriteButton.isChecked()) {
                shouldRemoveFavorite = false;
                addUserFavoriteProduct(item);
            } else {
                shouldRemoveFavorite = true;
                removeUserFavoriteProduct(item);
                removedItems.add(item);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout productLayout;
        ImageView img;
        TextView txtName, txtProductID, txtColor, txtActualPrice, txtDiscountPrice;
        FavoriteCheckBox favoriteButton;
        View divider;

        public ViewHolder(View view) {
            super(view);
            productLayout = view.findViewById(R.id.product_cell);
            img = view.findViewById(R.id.product_image);
            txtName = view.findViewById(R.id.product_name);
            txtProductID = view.findViewById(R.id.product_id);
            txtColor = view.findViewById(R.id.product_color);
            txtActualPrice = view.findViewById(R.id.productActualPriceTextView);
            txtDiscountPrice = view.findViewById(R.id.productDiscountPriceTextView);
            favoriteButton = view.findViewById(R.id.favorite_button);
            divider = view.findViewById(R.id.divider);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void checkAndRemoveFavorite() {
        if (shouldRemoveFavorite) {
            UserInteraction userInteraction = getMainActivityInstance().userInteraction;
            Iterator<Product> iterator = removedItems.iterator();
            while (iterator.hasNext()) {
                Product itemToRemove = iterator.next();
                userInteraction.removeFavorite(itemToRemove);
                iterator.remove();
            }
            shouldRemoveFavorite = false; // Reset the flag
        }
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

}
