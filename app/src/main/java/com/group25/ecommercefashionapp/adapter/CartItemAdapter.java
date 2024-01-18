package com.group25.ecommercefashionapp.adapter;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.ui.activity.CartActivity;
import com.group25.ecommercefashionapp.ui.fragment.bottomsheet.RemoveItemBottomSheetFragment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{
    private final List<CartItem> items;
    private final List<CartItem> cartItems;
    private final OnItemClickListener clickListener;
    private final DecimalFormat VNDFormat;
    private final Context context;
    public CartItemAdapter(List<CartItem> items, OnItemClickListener clickListener, Context context) {
        this.items = items;
        this.cartItems = getMainActivityInstance().userInteraction.getCartList();
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
        CartItem cartItem = null;
        for(CartItem cartItem1 : cartItems) {
            if (cartItem1.getProductId() == item.getProductId() && cartItem1.getSelectedColor().getId() == item.getSelectedColor().getId() && cartItem1.getSelectedSize().getId() == item.getSelectedSize().getId() && cartItem1.getPhoneNumber().equals(item.getPhoneNumber())) {
                cartItem = cartItem1;
                item.setQuantity(cartItem.getQuantity());
                break;
            }
        }

        Product product = getMainActivityInstance().productRepository.getProductById(item.getProductId());
        // Bind your data to the UI components of the CardView
        if (product.getAvailableQuantity() == 0) {
            holder.outOfStockText.setVisibility(View.VISIBLE);
        } else {
            holder.outOfStockText.setVisibility(View.GONE);
        }
        ArrayAdapter<CharSequence> spinnerEntries = new ArrayAdapter<>(getMainActivityInstance(), android.R.layout.simple_spinner_item);
        spinnerEntries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 1; i <= product.getAvailableQuantity() && i <= 5; i++) {
            spinnerEntries.add(String.valueOf(i));
        }

        holder.productImage.setImageResource(product.getImageList().get(0).getImage_int_id());
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("%s VND", VNDFormat.format(product.getPrice()*0.9f)));
        holder.productId.setText(String.valueOf(product.getId()));
        holder.productSize.setText(item.getSelectedSize().getName());
        holder.productColor.setText(item.getSelectedColor().getName());
        holder.productTotalPrice.setText(String.format("%s VND", VNDFormat.format((long) product.getPrice()*0.9f * item.getQuantity())));
        holder.quantitySpinner.setAdapter(spinnerEntries);
        holder.quantitySpinner.setSelection(item.getQuantity() - 1);
        CartItem finalCartItem = cartItem;
        holder.quantitySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setQuantity(position + 1);
                finalCartItem.setQuantity(position + 1);
                holder.productTotalPrice.setText(String.format("%s VND", VNDFormat.format((long) product.getPrice()*0.9f * item.getQuantity())));
                ((CartActivity) context).updateCartSummaryView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Set click listener on the card
        holder.cardView.setOnClickListener(v -> clickListener.onItemClick(v, product));
        holder.removeButton.setOnClickListener(v ->
        {
            RemoveItemBottomSheetFragment removeItemBottomSheetFragment = new RemoveItemBottomSheetFragment(item, items, this);
            removeItemBottomSheetFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), removeItemBottomSheetFragment.getTag());
        });
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
    public void notifyDataChanged() {
        notifyDataSetChanged();
        ((CartActivity) context).updateCartSummaryView();
        ((CartActivity) context).setupInitialVisibility();
    }
}
