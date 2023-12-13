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
                    ProductContract.ProductEntry.COLUMN_CATEGORY + " TEXT," +
                    ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY + " INT)";

    private static final String CREATE_COLOR_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ProductContract.ColorEntry.TABLE_NAME + " (" +
                    ProductContract.ColorEntry.COLUMN_PRODUCT_ID + " INT," +
                    ProductContract.ColorEntry.COLUMN_HEX_COLOR + " TEXT," +
                    ProductContract.ColorEntry.COLUMN_COLOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "UNIQUE (" + ProductContract.ColorEntry.COLUMN_PRODUCT_ID + ", " + ProductContract.ColorEntry.COLUMN_HEX_COLOR + ")," +
                    "FOREIGN KEY (" + ProductContract.ColorEntry.COLUMN_PRODUCT_ID + ") REFERENCES " + ProductContract.ProductEntry.TABLE_NAME + "(" + ProductContract.ProductEntry.COLUMN_ID + "))";

    private static final String CREATE_SIZE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ProductContract.SizeEntry.TABLE_NAME + " (" +
                    ProductContract.SizeEntry.COLUMN_PRODUCT_ID + " INTEGER," +
                    ProductContract.SizeEntry.COLUMN_SIZE + " TEXT," +
                    ProductContract.SizeEntry.COLUMN_SIZE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "UNIQUE (" + ProductContract.SizeEntry.COLUMN_PRODUCT_ID + ", " + ProductContract.SizeEntry.COLUMN_SIZE + ")," +
                    "FOREIGN KEY (" + ProductContract.SizeEntry.COLUMN_PRODUCT_ID + ") REFERENCES " + ProductContract.ProductEntry.TABLE_NAME + "(" + ProductContract.ProductEntry.COLUMN_ID + "))";

    private static final String CREATE_PRODUCT_IMAGES_TABLE=
            "CREATE TABLE IF NOT EXISTS " + ProductContract.ImageEntry.TABLE_NAME + " (" +
                    ProductContract.ImageEntry.COLUMN_PRODUCT_ID + " INTEGER," +
                    ProductContract.ImageEntry.COLUMN_IMAGE + " INT," +
                    ProductContract.ImageEntry.COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "UNIQUE (" + ProductContract.ImageEntry.COLUMN_PRODUCT_ID + ", " + ProductContract.ImageEntry.COLUMN_IMAGE + ")," +
                    "FOREIGN KEY (" + ProductContract.ImageEntry.COLUMN_PRODUCT_ID + ") REFERENCES " + ProductContract.ProductEntry.TABLE_NAME + "(" + ProductContract.ProductEntry.COLUMN_ID + "))";

    private static final String CREATE_ORDER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + OrderContract.OrderEntry.TABLE_NAME + " (" +
                    OrderContract.OrderEntry.COLUMN_ORDER_ID + " INT, " +
                    OrderContract.OrderEntry.COLUMN_DATE + " TEXT)";

    private static final String CREATE_ORDERDETAILS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + OrderDetailsContract.OrderDetailsEntry.TABLE_NAME + " (" +
                    OrderDetailsContract.OrderDetailsEntry.COLUMN_ORDINAL_NUMBER + " INT," +
                    OrderDetailsContract.OrderDetailsEntry.COLUMN_ORDER_ID + " INT," +
                    OrderDetailsContract.OrderDetailsEntry.COLUMN_PRODUCT_ID + " INT)";

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
        db.execSQL(CREATE_ORDERDETAILS_TABLE);
        db.execSQL(CREATE_COLOR_TABLE);
        db.execSQL(CREATE_SIZE_TABLE);
        db.execSQL(CREATE_PRODUCT_IMAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement if needed when upgrading the database schema
    }
}
