package com.group25.ecommercefashionapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.FilterType;

import java.util.List;

public class FilterItemAdapter extends RecyclerView.Adapter<FilterItemAdapter.ViewHolder>{
    private final List<FilterType> items;
    public FilterItemAdapter(List<FilterType> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_filter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FilterType item = items.get(position);

        // Bind your data to the UI components of the CardView
        holder.filterItemTextView.setText(item.getTitle());
        holder.itemView.setOnClickListener(v -> {

        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView filterItemTextView;

        public ViewHolder(View view) {
            super(view);
            filterItemTextView = view.findViewById(R.id.text_filter);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
