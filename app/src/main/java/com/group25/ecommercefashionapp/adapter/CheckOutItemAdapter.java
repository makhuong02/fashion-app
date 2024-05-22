package com.group25.ecommercefashionapp.adapter;

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
import com.group25.ecommercefashionapp.api.ApiServiceBuilder;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

public class CheckOutItemAdapter extends RecyclerView.Adapter<CheckOutItemAdapter.ViewHolder> {
    private final List<CartItem> items;
    private Product product;

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
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        CartItem item = items.get(position);
        fetchProductFromApi(item, holder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView txtQuantity;

        public ViewHolder(View view) {
            super(view);
            productImage = view.findViewById(R.id.ivProductImage);
            txtQuantity = view.findViewById(R.id.tvQuantity);
        }
    }

    private void fetchProductFromApi(CartItem item, ViewHolder holder){
        ProductRepository.getInstance().fetchProductByProductIdFromApi(item.getProductId(), getMainActivityInstance().getApplicationContext(), new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();
                    if (jsonElement != null) {
                        product = ProductRepository.getInstance().parseJsonToProduct(jsonElement);
                        setupProductImage(product, holder);
                        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));
                    }
                }
                else {
                    Toast.makeText(getMainActivityInstance().getApplicationContext(), "Failed to fetch product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                Toast.makeText(getMainActivityInstance().getApplicationContext(), "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupProductImage(Product item, ViewHolder holder) {
        String imagePath = "";
        if(!item.getImageList().isEmpty()) {
            imagePath = item.getImageList().get(0).getImagePath();
        }

        Picasso.get()
                .load(ApiServiceBuilder.BASE_URL +"product-images/"+ imagePath)
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.ic_connection_error)
                .into(holder.productImage);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

}
