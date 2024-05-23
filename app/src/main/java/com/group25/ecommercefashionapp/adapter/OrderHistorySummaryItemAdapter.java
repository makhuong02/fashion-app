package com.group25.ecommercefashionapp.adapter;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.api.ApiServiceBuilder;
import com.group25.ecommercefashionapp.data.OrderItem;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistorySummaryItemAdapter extends RecyclerView.Adapter<OrderHistorySummaryItemAdapter.ViewHolder> {
    private final Set<OrderItem> items;
    private final DecimalFormat VNDFormat;
    private final Context context;

    public OrderHistorySummaryItemAdapter(Set<OrderItem> items, Context context) {
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
        OrderItem item = (OrderItem) items.toArray()[position];
        fetchProductFromApi(item.getProductId(), holder);
        fetchColorFromApi(item.getSelectedColorId(), holder);
        fetchSizeFromApi(item.getSelectedSizeId(), holder);
        holder.productQuantity.setText(String.valueOf(item.getQuantity()));
        holder.productTotalPrice.setText(VNDFormat.format(item.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productId, productPrice, productSize, productColor, productTotalPrice;
        TextView productQuantity;
        ImageView productImage;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.item_name);
            productId = itemView.findViewById(R.id.product_id);
            productPrice = itemView.findViewById(R.id.item_price);
            productSize = itemView.findViewById(R.id.product_size);
            productColor = itemView.findViewById(R.id.product_color);
            productTotalPrice = itemView.findViewById(R.id.product_subtotal_price);
            productImage = itemView.findViewById(R.id.item_image);
            productQuantity = itemView.findViewById(R.id.product_quantity);
        }
    }

    private void fetchProductFromApi(Long productId, ViewHolder holder) {
        ProductRepository.getInstance().fetchProductByProductIdFromApi(productId, getMainActivityInstance().getApplicationContext(), new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    holder.productName.setText(jsonObject.get("name").getAsString());
                    holder.productId.setText(jsonObject.get("id").getAsString());
                    holder.productPrice.setText(jsonObject.get("price").getAsString());

                    JsonArray imagesArray = jsonObject.getAsJsonArray("images");
                    if (imagesArray != null && imagesArray.size() > 0) {
                        JsonObject imageObject = imagesArray.get(0).getAsJsonObject();
                        String imagePath = imageObject.get("imagePath").getAsString();
                        Picasso.get()
                                .load(ApiServiceBuilder.BASE_URL + "product-images/" + imagePath)
                                .placeholder(R.drawable.loading_img)
                                .error(R.drawable.ic_connection_error)
                                .into(holder.productImage);
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchColorFromApi(Long colorId, ViewHolder holder) {
        ProductRepository.getInstance().fetchColorByIdFromApi(colorId, getMainActivityInstance().getApplicationContext(), new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    holder.productColor.setText(jsonObject.get("name").getAsString());
                } else {
                    Toast.makeText(context, "Failed to fetch color", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSizeFromApi(Long sizeId, ViewHolder holder) {
        ProductRepository.getInstance().fetchSizeByIdFromApi(sizeId, getMainActivityInstance().getApplicationContext(), new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    holder.productSize.setText(jsonObject.get("size").getAsString());
                } else {
                    Toast.makeText(context, "Failed to fetch size", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
