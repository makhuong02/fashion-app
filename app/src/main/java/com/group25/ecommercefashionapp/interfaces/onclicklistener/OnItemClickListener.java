package com.group25.ecommercefashionapp.interfaces.onclicklistener;

import android.view.View;

import com.group25.ecommercefashionapp.data.Item;

// Interface to handle item click events
public interface OnItemClickListener {
    void onItemClick(View view, Item item);
}
