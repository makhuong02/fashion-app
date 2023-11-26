package com.group25.ecommercefashionapp.database;

import android.provider.BaseColumns;

public final class OrderDetailsContract {

    private OrderDetailsContract() {
    }

    public static class OrderDetailsEntry implements BaseColumns {
        public static final String TABLE_NAME = "OrderDetails";
        public static final String COLUMN_ORDINAL_NUMBER = "ordinal_number";
        public static final String COLUMN_ORDER_ID = "order_id";
        public static final String COLUMN_PRODUCT_ID = "product_id";
    }
}
