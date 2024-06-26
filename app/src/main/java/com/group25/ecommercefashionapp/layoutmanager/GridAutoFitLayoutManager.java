package com.group25.ecommercefashionapp.layoutmanager;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridAutoFitLayoutManager extends GridLayoutManager
{
    private int columnWidth;
    private boolean isColumnWidthChanged = true;
    public GridAutoFitLayoutManager(@NonNull final Context context, final int columnWidth) {
        /* Initially set spanCount to 1, will be changed automatically later. */
        super(context, 1);
        setColumnWidth(checkedColumnWidth(context, columnWidth));
    }

    public GridAutoFitLayoutManager(
            @NonNull final Context context,
            final int columnWidth,
            final int orientation,
            final boolean reverseLayout) {

        /* Initially set spanCount to 1, will be changed automatically later. */
        super(context, 1, orientation, reverseLayout);
        setColumnWidth(checkedColumnWidth(context, columnWidth));
    }

    private int checkedColumnWidth(@NonNull final Context context, int columnWidth) {
        if (columnWidth <= 0) {
            /* Set default columnWidth value (48dp here). It is better to move this constant
            to static constant on top, but we need context to convert it to dp, so can't really
            do so. */
            columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                    context.getResources().getDisplayMetrics());
        }
        return columnWidth;
    }

    public void setColumnWidth(final int newColumnWidth) {
        if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
            columnWidth = newColumnWidth;
            isColumnWidthChanged = true;
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state)
    {
        if (isColumnWidthChanged && columnWidth > 0)
        {
            int totalSpace;
            if (getOrientation() == VERTICAL)
            {
                totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
            }
            else
            {
                totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
            }
            int spanCount = Math.max(1, totalSpace / columnWidth);
            setSpanCount(spanCount);
            isColumnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }
}