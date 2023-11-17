package com.group25.ecommercefashionapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecommerce_db";
    private static final int DATABASE_VERSION = 1;

    // Define the Product table schema
    private static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ProductContract.ProductEntry.TABLE_NAME + " (" +
                    ProductContract.ProductEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ProductContract.ProductEntry.COLUMN_NAME + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_DESCRIPTION + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_PRICE + " INT," +
                    ProductContract.ProductEntry.COLUMN_IMAGE + " INT," +
                    ProductContract.ProductEntry.COLUMN_CATEGORY + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY + " INT)";

    private static final String CREATE_ORDER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + OrderContract.OrderEntry.TABLE_NAME + " (" +
                    OrderContract.OrderEntry.COLUMN_ORDINAL_NUMBER + " INT, " +
                    OrderContract.OrderEntry.COLUMN_ORDER_ID + " INT, " +
                    OrderContract.OrderEntry.COLUMN_DATE + " DATE, " +
                    OrderContract.OrderEntry.COLUMN_PRODUCT_ID + " INT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // truy vấn không trả kết quả (create, insert,...)
    public void NonQueryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    // truy vấn trả kết quả (select)
    public Cursor QueryData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement if needed when upgrading the database schema
    }
}
