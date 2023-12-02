package com.group25.ecommercefashionapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.ChipImagesView;
import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.Product;

import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ViewHolder> {
    private final List<Product> items;
    private final OnItemClickListener clickListener;

    public ProductItemAdapter(List<Product> items, OnItemClickListener clickListener) {
        this.items = items;
        this.clickListener = clickListener;
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
        holder.txtDescription.setText(item.getDescription());
        holder.txtPrice.setText(String.format("%,d", item.getPrice()));
        holder.img.setImageResource(item.getImage());

        // Set click listener on the card
        holder.cardView.setOnClickListener(v -> clickListener.onItemClick(item));
        holder.chipImagesView.setChipImages(item.getColors());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtDescription;
        TextView txtPrice;
        CardView cardView;

        ChipImagesView chipImagesView;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.product_imageView);
            txtDescription = view.findViewById(R.id.productName);
            txtPrice = view.findViewById(R.id.price_view);
            cardView = view.findViewById(R.id.productCardView);
            chipImagesView = view.findViewById(R.id.chip_imagesView);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
