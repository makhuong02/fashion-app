package com.group25.ecommercefashionapp.data;

import com.group25.ecommercefashionapp.R;

public enum FilterType {
    SIZE(R.string.text_size);

    private final int title;
    FilterType(int title) {
        this.title = title;
    }

    public final int getTitle() {
        return this.title;
    }
}
