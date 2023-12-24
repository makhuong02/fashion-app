package com.group25.ecommercefashionapp.adapter;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.ui.widget.ChipImagesView;
import com.group25.ecommercefashionapp.ui.widget.FavoriteCheckBox;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

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
        holder.img.setImageResource(item.getImageList().get(0).getImage_int_id());
        holder.txtSizeRange.setText(item.getSizeRange());

        // Set click listener on the card
        holder.cardView.setOnClickListener(v -> clickListener.onItemClick(v, item));
        for (Product product : getMainActivityInstance().userInteraction.getFavoriteList()) {
            if (product.getId() == item.getId()) {
                holder.favoriteCheckBox.setChecked(true);
                break;
            }
        }
        holder.favoriteCheckBox.setOnClickListener(v -> {
            if (holder.favoriteCheckBox.isChecked()) {
                getMainActivityInstance().userInteraction.addFavorite(item);
            } else {
                getMainActivityInstance().userInteraction.removeFavorite(item);
            }
        });
        holder.chipImagesView.setChipImages(item.getColors());
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

}
