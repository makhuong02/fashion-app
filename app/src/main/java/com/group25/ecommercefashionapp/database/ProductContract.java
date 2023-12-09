package com.group25.ecommercefashionapp.database;

import android.provider.BaseColumns;

public final class ProductContract {

    private ProductContract() {
    }

    public static class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "Product";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_AVAILABLE_QUANTITY = "available_quantity";
    }

    public static class ColorEntry {
        public static final String TABLE_NAME = "ProductColor";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_HEX_COLOR = "hex";
        public static final String COLUMN_COLOR_ID = "color_id";
    }
    public static class SizeEntry implements BaseColumns {
        public static final String TABLE_NAME = "ProductSize";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_SIZE_ID = "size_id";
    }
}
