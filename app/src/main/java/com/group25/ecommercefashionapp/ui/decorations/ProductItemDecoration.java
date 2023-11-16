package com.group25.ecommercefashionapp.ui.decorations;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductItemDecoration extends RecyclerView.ItemDecoration {
    private final int verticalSpacing;
    private final int horizontalSpacing;

    public ProductItemDecoration(Context context, int verticalSpacing, int horizontalSpacing) {
        this.verticalSpacing = dpToPx(context, verticalSpacing);
        this.horizontalSpacing = dpToPx(context, horizontalSpacing);
    }

    private int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();

        // Apply spacing to all items except the last one
        if (position < itemCount - 1) {
            outRect.bottom = verticalSpacing;
        }

        // Apply horizontal spacing to all items
        outRect.left = horizontalSpacing;
        outRect.right = horizontalSpacing;
    }
}
