package com.group25.ecommercefashionapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.ProductColor;

import java.util.List;

public class ProductColorAdapter extends RecyclerView.Adapter<ProductColorAdapter.ViewHolder> {
    private final List<ProductColor> items;
    private final OnItemClickListener clickListener;
    private final RecyclerView recyclerView;

    public ProductColorAdapter(List<ProductColor> items, OnItemClickListener clickListener, RecyclerView recyclerView) {
        this.items = items;
        this.clickListener = clickListener;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_chip_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductColor item = items.get(position);

        holder.img.setBackgroundColor(Color.parseColor(item.getHexColor()));
        holder.img.setVisibility(View.VISIBLE);
        holder.cardView.setOnClickListener(v ->
        {
            uncheckAll(holder);
            holder.cardView.setChecked(true);
            holder.cardView.setStrokeWidth(4);
            clickListener.onItemClick(v, item);
        });
        if(position == 0) {
            holder.cardView.performClick();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        MaterialCardView cardView;
        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.chip_image);
            cardView = view.findViewById(R.id.chip_image_card);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void uncheckAll(ViewHolder holder) {
        for (int i = 0; i < getItemCount(); i++) {
            ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                viewHolder.cardView.setChecked(false);
                viewHolder.cardView.setStrokeWidth(0);
            }
        }
    }
}