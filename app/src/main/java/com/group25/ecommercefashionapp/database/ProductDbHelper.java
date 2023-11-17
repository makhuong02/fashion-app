package com.group25.ecommercefashionapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecommerce_db";
    private static final int DATABASE_VERSION = 1;

    // Define the Product table schema
    private static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME + " (" +
                    ProductContract.ProductEntry.COLUMN_ID + " TEXT PRIMARY KEY," +
                    ProductContract.ProductEntry.COLUMN_NAME + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_DESCRIPTION + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_PRICE + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_IMAGE + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_CATEGORY + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY + " TEXT)";

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement if needed when upgrading the database schema
    }
}
