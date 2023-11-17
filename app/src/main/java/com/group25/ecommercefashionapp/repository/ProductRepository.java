package com.group25.ecommercefashionapp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.CategoryItem;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.database.ProductContract;
import com.group25.ecommercefashionapp.database.ProductDbHelper;

import java.util.ArrayList;

public class ProductRepository {
    private SQLiteDatabase db;
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

            insertProductData(new Product("Đây là sản phẩm 1 có nhiều lượt bán nhất", 1500000, R.drawable.tshirt, "Áo thun"));
            insertProductData(new Product("Đây là sản phẩm 2 có nhiều lượt bán nhất", 580000, R.drawable.tshirt, "Áo thun"));
            insertProductData(new Product("Đây là sản phẩm 3 có nhiều lượt bán nhất", 750000, R.drawable.tshirt, "Áo thun"));
            insertProductData(new Product("Đây là sản phẩm 4 có nhiều lượt bán nhất", 800000, R.drawable.skirt, "Váy"));
            insertProductData(new Product("Đây là sản phẩm 5 có nhiều lượt bán nhất", 500000, R.drawable.tshirt, "Áo thun"));
            insertProductData(new Product("Đây là sản phẩm 6 có nhiều lượt bán nhất", 300000, R.drawable.jeans, "Quần"));
            insertProductData(new Product("Đây là sản phẩm 7 có nhiều lượt bán nhất", 300000, R.drawable.jeans, "Quần"));
            insertProductData(new Product("Đây là sản phẩm 8 có nhiều lượt bán nhất", 300000, R.drawable.sneakers, "Giày"));
            insertProductData(new Product("Đây là sản phẩm 9 có nhiều lượt bán nhất", 300000, R.drawable.wallet, "Bóp"));
            insertProductData(new Product("Đây là sản phẩm 10 có nhiều lượt bán nhất", 200000, R.drawable.jacket, "Áo khoác"));
            insertProductData(new Product("Đây là sản phẩm 11 có nhiều lượt bán nhất", 200000, R.drawable.jacket, "Áo khoác"));
            insertProductData(new Product("Đây là sản phẩm 12 có nhiều lượt bán nhất", 100000, R.drawable.hoodie, "Hoodie"));
            insertProductData(new Product("Đây là sản phẩm 13 có nhiều lượt bán nhất", 100000, R.drawable.hoodie, "Hoodie"));
            insertProductData(new Product("Đây là sản phẩm 14 có nhiều lượt bán nhất", 860000, R.drawable.handbag, "Túi xách"));
            insertProductData(new Product("Đây là sản phẩm 15 có nhiều lượt bán nhất", 350000, R.drawable.tshirt, "Áo thun"));
            insertProductData(new Product("Đây là sản phẩm 16 có nhiều lượt bán nhất", 4600000, R.drawable.tshirt, "Áo thun"));
            insertProductData(new Product("Đây là sản phẩm 17 có nhiều lượt bán nhất", 4600000, R.drawable.watch, "Đồng hồ"));
            insertProductData(new Product("Đây là sản phẩm 18 có nhiều lượt bán nhất", 8700000, R.drawable.handbag, "Túi xách"));
            insertProductData(new Product("Đây là sản phẩm 19 có nhiều lượt bán nhất", 8700000, R.drawable.handbag, "Túi xách"));
            insertProductData(new Product("Đây là sản phẩm 20 có nhiều lượt bán nhất", 8800000, R.drawable.tshirt, "Áo thun"));
            insertProductData(new Product("Đây là sản phẩm 21 có nhiều lượt bán nhất", 1200000, R.drawable.tshirt, "Áo thun"));
            insertProductData(new Product("Đây là sản phẩm 21 có nhiều lượt bán nhất", 3400000, R.drawable.tshirt, "Áo thun"));

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Product> getProductsByCategory(String category) {
        ArrayList<Product> products = new ArrayList<>();

        db = productDbHelper.getReadableDatabase();
        String[] projection = {
                ProductContract.ProductEntry.COLUMN_ID,
                ProductContract.ProductEntry.COLUMN_NAME,
                ProductContract.ProductEntry.COLUMN_DESCRIPTION,
                ProductContract.ProductEntry.COLUMN_PRICE,
                ProductContract.ProductEntry.COLUMN_IMAGE,
                ProductContract.ProductEntry.COLUMN_CATEGORY,
                ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY
        };

        String selection = ProductContract.ProductEntry.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = {category};

        Cursor cursor = db.query(
                ProductContract.ProductEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
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
            int productId = cursor.getInt(idIndex);
            String productName = cursor.getString(nameIndex);
            String productDescription = cursor.getString(descriptionIndex);
            int productPrice = cursor.getInt(priceIndex);
            int productImage = cursor.getInt(imageIndex);
            String productCategory = cursor.getString(categoryIndex);
            int productQuantity = cursor.getInt(availableQuantityIndex);

            Product product = new Product(productId, productName, productDescription, productPrice, productImage, productCategory, productQuantity);
            products.add(product);

            cursor.moveToNext();
        }

        cursor.close();
        return products;
    }

    public ArrayList<CategoryItem> getCategories() {
        ArrayList<CategoryItem> items = new ArrayList<>();
        items.add(new CategoryItem("Áo thun", R.drawable.tshirt));
        items.add(new CategoryItem("Váy", R.drawable.skirt));
        items.add(new CategoryItem("Quần", R.drawable.jeans));
        items.add(new CategoryItem("Áo khoác", R.drawable.jacket));
        items.add(new CategoryItem("Hoodie", R.drawable.hoodie));
        items.add(new CategoryItem("Giày", R.drawable.sneakers));
        items.add(new CategoryItem("Đồng hồ", R.drawable.watch));
        items.add(new CategoryItem("Bóp", R.drawable.wallet));
        items.add(new CategoryItem("Túi xách", R.drawable.handbag));
        return items;
    }

}
