package com.group25.ecommercefashionapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.Product;

import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ViewHolder> {
    private List<Product> items;
    private OnItemClickListener clickListener;

    public ProductItemAdapter(List<Product> items, OnItemClickListener clickListener) {
        this.items = items;
        this.clickListener = clickListener;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtDescription;
        TextView txtPrice;
        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.productImage);
            txtDescription = view.findViewById(R.id.productName);
            txtPrice = view.findViewById(R.id.productPrice);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_filtered_items, parent, false);
        return new ViewHolder(view);
    }


    //    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ProductItemAdapter.ViewHolder holder;
//
//        if (convertView == null) {
//            holder = new ProductItemAdapter.ViewHolder();
//            convertView = LayoutInflater.from(context).inflate(layout, parent, false);
//
//            holder.txtName = convertView.findViewById(R.id.productName);
//            holder.txtPrice = convertView.findViewById(R.id.productPrice);
//            holder.img = convertView.findViewById(R.id.productImage);
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ProductItemAdapter.ViewHolder) convertView.getTag();
//        }
//
//        Product item = items.get(position);
//        holder.txtName.setText(item.getName());
//        holder.txtPrice.setText(String.format("%,d", item.getPrice()));
//        holder.img.setImageResource(item.getImage());
//
//        return convertView;
//    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product item = items.get(position);

        // Bind your data to the UI components of the CardView
        holder.txtDescription.setText(item.getDescription());
        holder.txtPrice.setText(String.format("%,d", item.getPrice()));
        holder.img.setImageResource(item.getImage());

        // Set click listener on the card
//        holder.cardView.setOnClickListener(v -> {
//
//            clickListener.onItemClick(item);
//        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
