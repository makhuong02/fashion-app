package com.group25.ecommercefashionapp.adapter;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.Product;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{
    private final List<CartItem> items;
    private final OnItemClickListener clickListener;
    private final DecimalFormat VNDFormat;

    public CartItemAdapter(List<CartItem> items, OnItemClickListener clickListener) {
        this.items = items;
        this.clickListener = clickListener;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartItem item = items.get(position);
        Product product = getMainActivityInstance().productRepository.getProductById(item.getProductId());
        Log.d("CartItemAdapter", "onBindViewHolder: " + product.getAvailableQuantity());
        Log.d("CartItemAdapter", "onBindViewHolder: " + item.getQuantity());
        // Bind your data to the UI components of the CardView
        if (product.getAvailableQuantity() == 0) {
            holder.outOfStockText.setVisibility(View.VISIBLE);
        } else {
            holder.outOfStockText.setVisibility(View.GONE);
        }
        holder.productImage.setImageResource(product.getImageList().get(0).getImage_int_id());
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));
        holder.productId.setText(String.valueOf(product.getId()));
        holder.productSize.setText(item.getSelectedColor().getName());
        holder.productColor.setText(item.getSelectedSize().getName());
        holder.productTotalPrice.setText(String.format("%s VND", VNDFormat.format(product.getPrice() * item.getQuantity())));
        holder.quantitySpinner.setSelection(item.getQuantity());
        // Set click listener on the card
        holder.cardView.setOnClickListener(v -> clickListener.onItemClick(v, item));
        holder.removeButton.setOnClickListener(v -> getMainActivityInstance().userInteraction.removeCart(item));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productId, productPrice, productSize, productColor, productTotalPrice, outOfStockText;
        ImageView productImage;
        LinearLayout cardView;
        Spinner quantitySpinner;
        AppCompatButton removeButton;
        public ViewHolder(View itemView) {
            super(itemView);
            outOfStockText = itemView.findViewById(R.id.item_out_of_stock_text);
            cardView = itemView.findViewById(R.id.product_cell);
            productName = itemView.findViewById(R.id.item_name);
            productPrice = itemView.findViewById(R.id.item_price);
            productId = itemView.findViewById(R.id.product_id);
            productSize = itemView.findViewById(R.id.product_size);
            productColor = itemView.findViewById(R.id.product_color);
            productTotalPrice = itemView.findViewById(R.id.product_subtotal_price);
            productImage = itemView.findViewById(R.id.item_image);
            quantitySpinner = itemView.findViewById(R.id.spinner);
            removeButton = itemView.findViewById(R.id.cancel_button);
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}
