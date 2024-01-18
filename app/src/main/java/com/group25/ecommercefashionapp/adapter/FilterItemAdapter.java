package com.group25.ecommercefashionapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.group25.ecommercefashionapp.interfaces.callback.FilterDialogCallback;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.FilterType;
import com.group25.ecommercefashionapp.ui.fragment.dialog.FilterDialog;

import java.util.List;

public class FilterItemAdapter extends RecyclerView.Adapter<FilterItemAdapter.ViewHolder> implements FilterDialogCallback {
    private final List<FilterType> items;
    private final String category;
    private final FilterDialogCallback filterDialogCallback;
    private final List<String> selectedItems;
    private final String search;
    public FilterItemAdapter(List<FilterType> items, List<String> selectedItems, String category, String search, FilterDialogCallback filterDialogCallback) {
        this.items = items;
        this.selectedItems = selectedItems;
        this.category = category;
        this.search = search;
        this.filterDialogCallback = filterDialogCallback;
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
            FilterDialog filterDialog = new FilterDialog(item.getTitle(), category, selectedItems, search);
            filterDialog.setFilterDialogCallback(this);
            filterDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), filterDialog.getTag());
        });
    }

    @Override
    public void onFilterApplied(List<String> selectedItems, String category) {
        if (filterDialogCallback != null)
            filterDialogCallback.onFilterApplied(selectedItems, category);
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
