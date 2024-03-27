package com.group25.ecommercefashionapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.CartItem;

import java.util.List;

public class CheckOutItemAdapter extends RecyclerView.Adapter<CheckOutItemAdapter.ViewHolder> {
    private final List<CartItem> items;

    public CheckOutItemAdapter(List<CartItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_out_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartItem item = items.get(position);
//        Product product = getMainActivityInstance().productRepository.getProductById(item.getProductId());

        // Bind your data to the UI components of the CardView

        // Set click listener on the card
//        holder.img.setImageResource(product.getImageList().get(0).getImage_int_id());
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtQuantity;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.ivProductImage);
            txtQuantity = view.findViewById(R.id.tvQuantity);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
