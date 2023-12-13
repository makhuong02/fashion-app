package com.group25.ecommercefashionapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.ProductSize;

import java.util.List;

public class ProductSizeAdapter extends RecyclerView.Adapter<ProductSizeAdapter.ViewHolder> {
    private final List<ProductSize> items;
    private final OnItemClickListener clickListener;
    private final RecyclerView recyclerView;

    public ProductSizeAdapter(List<ProductSize> items, OnItemClickListener clickListener, RecyclerView recyclerView) {
        this.items = items;
        this.clickListener = clickListener;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ProductSizeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_button_option, parent, false);
        return new ProductSizeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductSizeAdapter.ViewHolder holder, int position) {
        ProductSize item = items.get(position);

        holder.textView.setText(item.getName());
        holder.cardView.setOnClickListener(v ->
        {
            uncheckAll(holder);
            holder.textView.setTextColor(Color.parseColor("#FFFFFF"));
            holder.layout.setBackgroundColor(Color.parseColor("#000000"));
            holder.cardView.setChecked(true);
            holder.cardView.setStrokeWidth(4);
            clickListener.onItemClick(v, item);
        });
        if(position == 0) {
            holder.cardView.performClick();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ConstraintLayout layout;
        MaterialCardView cardView;
        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.chip_text);
            layout = view.findViewById(R.id.chip_size_container);
            cardView = view.findViewById(R.id.chip_size_card);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void uncheckAll(ProductSizeAdapter.ViewHolder holder) {
        for (int i = 0; i < getItemCount(); i++) {
            ProductSizeAdapter.ViewHolder viewHolder = (ProductSizeAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                viewHolder.cardView.setChecked(false);
                viewHolder.cardView.setStrokeWidth(0);
                viewHolder.layout.setBackgroundResource(R.drawable.layout_border);
                viewHolder.textView.setTextColor(Color.parseColor("#000000"));
            }
        }
    }
}
