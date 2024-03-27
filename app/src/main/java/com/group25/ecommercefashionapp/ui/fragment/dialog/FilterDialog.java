package com.group25.ecommercefashionapp.ui.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.adapter.FilterSizeAdapter;
import com.group25.ecommercefashionapp.interfaces.callback.FilterDialogCallback;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnCheckBoxItemClickListener;

import java.util.List;
import java.util.Objects;

public class FilterDialog extends BottomSheetDialogFragment implements OnCheckBoxItemClickListener{
    TextView titleTextView, resetTextView, filteredCountTextView;
    BottomSheetDialog dialog;
    BottomSheetBehavior bottomSheetBehavior;
    RecyclerView filterRecyclerView;
    AppCompatButton applyButton;
    Integer titleId;
    String category;
    List<String> selectedItems;
    private FilterDialogCallback filterDialogCallback;
    private String search;
    public FilterDialog(Integer titleId, String category, List<String> selectedItems, String search) {
        this.titleId = titleId;
        this.category = category;
        this.selectedItems = selectedItems;
        this.search = search;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_store_list_filter, container, false);

        // Initialize UI components

        titleTextView = view.findViewById(R.id.title);
        resetTextView = view.findViewById(R.id.filterReset);
        filteredCountTextView = view.findViewById(R.id.filteredCount);
        filterRecyclerView = view.findViewById(R.id.filterSections);
        applyButton = view.findViewById(R.id.filterApply);


        applyButton.setOnClickListener(v -> {
            dismiss();
        });

        resetTextView.setOnClickListener(v -> {
            selectedItems.clear();
//            ProductRepository productRepository = getMainActivityInstance().productRepository;
//            List<Product> filteredProducts = productRepository.getProductsBySizeOptionsCategoryAndSearch(selectedItems, category, search);
//            filteredCountTextView.setText(getString(R.string.text_product_count_item, filteredProducts.size()));
            setAdapter();
        });

//        ProductRepository productRepository = getMainActivityInstance().productRepository;
//        List<Product> filteredProducts = productRepository.getProductsBySizeOptionsCategoryAndSearch(selectedItems, category, search);
//        filteredCountTextView.setText(getString(R.string.text_product_count_item, filteredProducts.size()));

        titleTextView.setText(getString(titleId));
        if(Objects.equals(getString(titleId), getString(R.string.text_size))) {
            setAdapter();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        CoordinatorLayout coordinatorLayout = dialog.findViewById(R.id.coordinatorLayout);
        assert coordinatorLayout != null;
        coordinatorLayout.setMinimumHeight(getResources().getDisplayMetrics().heightPixels);

    }

    @Override
    public void onItemClick(String name, int position) {
        if(selectedItems.contains(name))
            selectedItems.remove(name);
        else
            selectedItems.add(name);
//        ProductRepository productRepository = getMainActivityInstance().productRepository;
//        List<Product> filteredProducts = productRepository.getProductsBySizeOptionsCategoryAndSearch(selectedItems, category, search);
//        filteredCountTextView.setText(getString(R.string.text_product_count_item, filteredProducts.size()));
    }

    public void setFilterDialogCallback(FilterDialogCallback callback) {
        this.filterDialogCallback = callback;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // Notify the hosting fragment with the selected items
        if (filterDialogCallback != null) {
            filterDialogCallback.onFilterApplied(selectedItems, category);
        }
    }

    private void setAdapter() {
        FilterSizeAdapter filterSizeAdapter = new FilterSizeAdapter(this, selectedItems);
        filterRecyclerView.setAdapter(filterSizeAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        filterRecyclerView.setLayoutManager(gridLayoutManager);
    }
}
