package com.group25.ecommercefashionapp.adapter;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.Product;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class OrderHistorySummaryItemAdapter extends RecyclerView.Adapter<OrderHistorySummaryItemAdapter.ViewHolder>{
    private final List<CartItem> items;
    private final DecimalFormat VNDFormat;
    private final Context context;
    public OrderHistorySummaryItemAdapter(List<CartItem> items, Context context) {
        this.items = items;
        this.context = context;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        VNDFormat = new DecimalFormat("###,###,###,###", symbols);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_summary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);
        Product product = getMainActivityInstance().productRepository.getProductById(item.getProductId());
        // Bind your data to the UI components of the CardView

        holder.productImage.setImageResource(product.getImageList().get(0).getImage_int_id());
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("%s VND", VNDFormat.format(product.getPrice()*0.9f)));
        holder.productId.setText(String.valueOf(product.getId()));
        holder.productSize.setText(item.getSelectedSize().getName());
        holder.productColor.setText(item.getSelectedColor().getName());
        holder.productQuantity.setText(String.valueOf(item.getQuantity()));
        holder.productTotalPrice.setText(String.format("%s VND", VNDFormat.format((long) product.getPrice()*0.9f * item.getQuantity())));

        // Set click listener on the card
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productId, productPrice, productSize, productColor, productTotalPrice;
        TextView productQuantity;
        ImageView productImage;
        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.item_name);
            productPrice = itemView.findViewById(R.id.item_price);
            productId = itemView.findViewById(R.id.product_id);
            productSize = itemView.findViewById(R.id.product_size);
            productColor = itemView.findViewById(R.id.product_color);
            productTotalPrice = itemView.findViewById(R.id.product_subtotal_price);
            productImage = itemView.findViewById(R.id.item_image);
            productQuantity = itemView.findViewById(R.id.product_quantity);
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}
