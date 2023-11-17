package com.group25.ecommercefashionapp.database;

import android.provider.BaseColumns;

public final class OrderContract {

    private OrderContract() {
    }

    public static class OrderEntry implements BaseColumns {
        public static final String TABLE_NAME = "Orders";
        public static final String COLUMN_ORDINAL_NUMBER = "ordinal_number";
        public static final String COLUMN_ORDER_ID = "order_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_PRODUCT_ID = "product_id";

    }
}
