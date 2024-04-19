package com.group25.ecommercefashionapp.adapter;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.group25.ecommercefashionapp.api.ApiServiceBuilder;
import com.group25.ecommercefashionapp.data.ProductQuantity;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.repository.ProductRepository;
import com.group25.ecommercefashionapp.repository.UserRepository;
import com.group25.ecommercefashionapp.ui.activity.CartActivity;
import com.group25.ecommercefashionapp.ui.fragment.bottomsheet.RemoveItemBottomSheetFragment;
import com.squareup.picasso.Picasso;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Objects;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{
    private final List<CartItem> items;
    private final OnItemClickListener clickListener;
    private final DecimalFormat VNDFormat;
    private final Context context;
    private Product product;
    public CartItemAdapter(List<CartItem> items, OnItemClickListener clickListener, Context context) {
        this.items = items;
        this.clickListener = clickListener;
        this.context = context;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);

        fetchProductFromApi(item, holder);
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

    private void setupQuantitySpinnerOnItemSelected( CartItem item, ViewHolder holder) {
        holder.quantitySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setQuantity(position + 1);

                updateCartItemQuantity(item, holder);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setupOnCartItemClick(CartItem item, Product product, ViewHolder holder) {
        holder.cardView.setOnClickListener(v -> clickListener.onItemClick(v, product));
        holder.removeButton.setOnClickListener(v ->
        {
            RemoveItemBottomSheetFragment removeItemBottomSheetFragment = new RemoveItemBottomSheetFragment(item, items, this, context);
            removeItemBottomSheetFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), removeItemBottomSheetFragment.getTag());
        });
    }

    private void fetchProductFromApi(CartItem item, ViewHolder holder){
        ProductRepository.getInstance().fetchProductByProductIdFromApi(item.getProductId(), getMainActivityInstance().getApplicationContext(), new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();
                    if (jsonElement != null) {
                        product = ProductRepository.getInstance().parseJsonToProduct(jsonElement);
                        setupProductProperties(product, holder, item);
                        setupProductImage(product, holder);
                        setupOnCartItemClick(item, product, holder);
                        fetchColorFromApi(item.getSelectedColorId(), holder);
                        fetchSizeFromApi(item.getSelectedSizeId(), holder);
                        fetchQuantityFromApi(product.getId(), item.getSelectedColorId(), item.getSelectedSizeId(), item, holder);
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

    private void fetchQuantityFromApi(Long productId, Long colorId, Long sizeId, CartItem item, ViewHolder holder) {
        ApiServiceBuilder.buildService().getProductQuantity(productId, colorId, sizeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<ProductQuantity>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull io.reactivex.rxjava3.disposables.Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ProductQuantity productQuantity) {
                        int availableQuantity = productQuantity.getQuantity();
                        ArrayAdapter<CharSequence> spinnerEntries = new ArrayAdapter<>(getMainActivityInstance(), android.R.layout.simple_spinner_item);
                        spinnerEntries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        for (int i = 1; i <= availableQuantity && i <= 5; i++) {
                            spinnerEntries.add(String.valueOf(i));
                        }
                        holder.quantitySpinner.setAdapter(spinnerEntries);
                        holder.quantitySpinner.setSelection(item.getQuantity() - 1);
                        setupQuantitySpinnerOnItemSelected(item, holder);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(getMainActivityInstance().getApplicationContext(), "Failed to fetch quantity", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateCartItemQuantity(CartItem item, ViewHolder holder) {
//        item.setQuantity(holder.quantitySpinner.getSelectedItemPosition() + 1);
        UserRepository.getInstance().updateCartItem(item.getId(), item, getMainActivityInstance().getApplicationContext(), new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    CartItem item = gson.fromJson(Objects.requireNonNull(response.body()).getAsJsonObject(), CartItem.class);
                    setupProductProperties(product, holder, item);
                    items.set(holder.getAdapterPosition(), item);
                    ((CartActivity) context).updateCartSummaryView(items);
                    ((CartActivity) context).setupInitialVisibility();
                }
                else {
                    Toast.makeText(getMainActivityInstance().getApplicationContext(), "Failed to update quantity", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                Toast.makeText(getMainActivityInstance().getApplicationContext(), "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupProductProperties(Product product, ViewHolder holder, CartItem item) {
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("%s VND", VNDFormat.format(item.getTotalPrice())));
        holder.productId.setText(String.valueOf(product.getId()));
        holder.productTotalPrice.setText(String.format("%s VND", VNDFormat.format(item.getTotalPrice())));
    }

    private void fetchColorFromApi(Long colorId, ViewHolder holder) {
        ProductRepository.getInstance().fetchColorByIdFromApi(colorId, getMainActivityInstance().getApplicationContext(), new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {
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
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                Toast.makeText(getMainActivityInstance().getApplicationContext(), "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSizeFromApi(Long sizeId, ViewHolder holder) {
        ProductRepository.getInstance().fetchSizeByIdFromApi(sizeId, getMainActivityInstance().getApplicationContext(), new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();
                    if (jsonElement != null) {
                        holder.productSize.setText(jsonElement.getAsJsonObject().get("size").getAsString());
                    }
                }
                else {
                    Toast.makeText(getMainActivityInstance().getApplicationContext(), "Failed to fetch size", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                Toast.makeText(getMainActivityInstance().getApplicationContext(), "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(items == null) {
            return 0;
        }
        return items.size();
    }
    public void notifyDataChanged() {
        notifyDataSetChanged();
        ((CartActivity) context).updateCartSummaryView(items);
        ((CartActivity) context).setupInitialVisibility();
    }
}
