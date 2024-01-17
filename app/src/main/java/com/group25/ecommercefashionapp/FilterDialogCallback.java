package com.group25.ecommercefashionapp;

import java.util.List;

public interface FilterDialogCallback {
    void onFilterApplied(List<String> selectedItems, String category);
}
