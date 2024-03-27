package com.group25.ecommercefashionapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.CategoryItem;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnListItemClick;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CustomCategoryItemAdapter extends RecyclerView.Adapter<CustomCategoryItemAdapter.ViewHolder> {
    private final List<CategoryItem> items;
    private final OnListItemClick clickListener;

    public CustomCategoryItemAdapter(List<CategoryItem> items, OnListItemClick clickListener) {
        this.items = items;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryItem item = items.get(position);

        // Bind your data to the UI components of the CardView
        holder.txtCategoryName.setText(item.getName());
        Picasso.get()
                .load(item.getImageUrl())
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.ic_connection_error)
                .into(holder.img);

        // Set click listener on the card
        holder.cardView.setOnClickListener(v -> clickListener.onItemClick(item, position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtCategoryName;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.categoryImage);
            txtCategoryName = view.findViewById(R.id.categoryName);
            cardView = view.findViewById(R.id.categoryCardView);
        }
    }
}