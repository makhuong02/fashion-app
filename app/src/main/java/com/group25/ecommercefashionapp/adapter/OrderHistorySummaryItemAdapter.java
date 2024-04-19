package com.group25.ecommercefashionapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

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
//        Product product = getMainActivityInstance().productRepository.getProductById(item.getProductId());
        // Bind your data to the UI components of the CardView

//        holder.productImage.setImageResource(product.getImageList().get(0).getImage_int_id());
//        holder.productName.setText(product.getName());
//        holder.productPrice.setText(String.format("%s VND", VNDFormat.format(product.getPrice()*0.9f)));
//        holder.productId.setText(String.valueOf(product.getId()));
        fetchColorFromApi(item.getSelectedColorId(), item.getSelectedSizeId(), holder);
        fetchSizeFromApi(item.getSelectedSizeId(), holder);
        holder.productQuantity.setText(String.valueOf(item.getQuantity()));
//        holder.productTotalPrice.setText(String.format("%s VND", VNDFormat.format((long) product.getPrice()*0.9f * item.getQuantity())));

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

    private void fetchColorFromApi(Long colorId, Long sizeId, ViewHolder holder) {
        ProductRepository.getInstance().fetchColorByIdFromApi(colorId, getMainActivityInstance().getApplicationContext(), new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();
                    if (jsonElement != null) {
                        holder.productColor.setText(jsonElement.getAsJsonObject().get("hexCode").getAsString());
                    }
                }
                else {
                    Toast.makeText(getMainActivityInstance().getApplicationContext(), "Failed to fetch color", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getMainActivityInstance().getApplicationContext(), "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSizeFromApi(Long sizeId, ViewHolder holder) {
        ProductRepository.getInstance().fetchSizeByIdFromApi(sizeId, getMainActivityInstance().getApplicationContext(), new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();
                    if (jsonElement != null) {
                        holder.productSize.setText(jsonElement.getAsJsonObject().get("name").getAsString());
                    }
                }
                else {
                    Toast.makeText(getMainActivityInstance().getApplicationContext(), "Failed to fetch size", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getMainActivityInstance().getApplicationContext(), "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
