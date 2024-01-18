package com.group25.ecommercefashionapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnCheckBoxItemClickListener;
import com.group25.ecommercefashionapp.R;

import java.util.Arrays;
import java.util.List;

public class FilterSizeAdapter extends RecyclerView.Adapter<FilterSizeAdapter.ViewHolder>{
    private final List<String> items;
    private final OnCheckBoxItemClickListener clickListener;
    private final List<String> selectedItems;
    public FilterSizeAdapter(OnCheckBoxItemClickListener clickListener, List<String> selectedItems) {
        this.clickListener = clickListener;
        this.selectedItems = selectedItems;
        this.items = Arrays.asList("XS", "S", "M", "L", "XL", "XXL");
    }

    @NonNull
    @Override
    public FilterSizeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_filter_size_checkbox, parent, false);
        return new FilterSizeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilterSizeAdapter.ViewHolder holder, int position) {
        String item = items.get(position);

        // Bind your data to the UI components of the CardView
        holder.filterSizeCheckBox.setChecked(selectedItems.contains(item));
        holder.filterSizeCheckBox.setText(item);
        holder.filterSizeCheckBox.setOnClickListener(v -> clickListener.onItemClick(item, position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatCheckBox filterSizeCheckBox;

        public ViewHolder(View view) {
            super(view);
            filterSizeCheckBox = view.findViewById(R.id.radio_button);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
