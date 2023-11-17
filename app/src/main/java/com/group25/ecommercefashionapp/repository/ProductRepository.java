package com.group25.ecommercefashionapp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.database.ProductContract;
import com.group25.ecommercefashionapp.database.ProductDbHelper;

import java.util.ArrayList;

public class ProductRepository {
    private final SQLiteDatabase db;
    private final ProductDbHelper productDbHelper;

    public ProductRepository(ProductDbHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
        this.productDbHelper = dbHelper;
    }

    public void insertProductData(Product product) {
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_NAME, product.getName());
        values.put(ProductContract.ProductEntry.COLUMN_DESCRIPTION, product.getDescription());
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, product.getPrice());
        values.put(ProductContract.ProductEntry.COLUMN_IMAGE, product.getImage());
        values.put(ProductContract.ProductEntry.COLUMN_CATEGORY, product.getCategory());
        values.put(ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY, product.getAvailableQuantity());

        db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        Cursor cursor = db.query(
                ProductContract.ProductEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null

        );
        int idIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_ID);
        int nameIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME);
        int descriptionIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_DESCRIPTION);
        int priceIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);
        int imageIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_IMAGE);
        int categoryIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_CATEGORY);
        int availableQuantityIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Integer productId = cursor.getInt(idIndex);
            String productName = cursor.getString(nameIndex);
            String productDescription = cursor.getString(descriptionIndex);
            Integer productPrice = cursor.getInt(priceIndex);
            int productImage = cursor.getInt(imageIndex);
            String productCategory = cursor.getString(categoryIndex);
            Integer productQuantity = cursor.getInt(availableQuantityIndex);

            Product product = new Product(productId, productName, productDescription, productPrice, productImage, productCategory, productQuantity);
            products.add(product);

            cursor.moveToNext();
        }

        cursor.close();
        return products;
    }

    private void deleteAllProducts() {
        db.delete(ProductContract.ProductEntry.TABLE_NAME, null, null);
    }

    public void dropProductTable() {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + ProductContract.ProductEntry.TABLE_NAME);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void insertDbData() {
        db.beginTransaction();
        try {
            productDbHelper.onCreate(db);

            insertProductData(new Product("Đây là sản phẩm 1 có nhiều lượt bán nhất", 500000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 2 có nhiều lượt bán nhất", 580000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 3 có nhiều lượt bán nhất", 320000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 4 có nhiều lượt bán nhất", 430000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 5 có nhiều lượt bán nhất", 6780000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 6 có nhiều lượt bán nhất", 3580000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 7 có nhiều lượt bán nhất", 580000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 8 có nhiều lượt bán nhất", 580000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 9 có nhiều lượt bán nhất", 580000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 10 có nhiều lượt bán nhất", 580000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 11 có nhiều lượt bán nhất", 580000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 12 có nhiều lượt bán nhất", 580000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 13 có nhiều lượt bán nhất", 580000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 14 có nhiều lượt bán nhất", 580000, R.drawable.tshirt));
            insertProductData(new Product("Đây là sản phẩm 15 có nhiều lượt bán nhất", 580000, R.drawable.tshirt));

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.endTransaction();
        }
    }
}
