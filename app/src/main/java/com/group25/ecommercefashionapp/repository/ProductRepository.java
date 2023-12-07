package com.group25.ecommercefashionapp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.CategoryItem;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.ProductColor;
import com.group25.ecommercefashionapp.database.ProductContract;
import com.group25.ecommercefashionapp.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductRepository {
    private SQLiteDatabase db;
    private final DatabaseHelper productDbHelper;

    public ProductRepository(DatabaseHelper dbHelper) {
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

        long productId = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);

    }

    public void insertProductColorData(ProductColor color) {
        ContentValues values = new ContentValues();
        values.put(ProductContract.ColorEntry.COLUMN_PRODUCT_ID, color.getProduct_id());
        values.put(ProductContract.ColorEntry.COLUMN_COLOR_PATH, color.getPath());

        long colorId = db.insert(ProductContract.ColorEntry.TABLE_NAME, null, values);


    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        Cursor cursor = db.query(ProductContract.ProductEntry.TABLE_NAME, null, null, null, null, null, null

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

            List<ProductColor> colors = getColorsForProduct(productId);
            Product product = new Product(productId, productName, productDescription, productPrice, productImage, productCategory, productQuantity);
            product.addColors(colors);
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
            db.execSQL("DROP TABLE IF EXISTS " + ProductContract.ColorEntry.TABLE_NAME);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void insertDbData() {
        db.beginTransaction();
        try {
            productDbHelper.onCreate(db);
            insertProductData(new Product("Beige Shirt", "Linen cotton fabric keep you warm through out the year.", 1500000, R.drawable.man_beige_shirt_pants_casual_wear_fashion, "Áo thun"));
            insertProductData(new Product("Beige Shirt", "Linen cotton fabric keep you warm through out the year.", 510000, R.drawable.man_beige_shirt_pants_casual_wear_fashion, "Áo thun"));
            insertProductData(new Product("Beige Shirt", "Linen cotton fabric keep you warm through out the year.", 750000, R.drawable.man_beige_shirt_pants_casual_wear_fashion, "Áo thun"));
            insertProductData(new Product("Váy", "Đây là sản phẩm 4 có nhiều lượt bán nhất", 800000, R.drawable.skirt, "Váy"));
            insertProductData(new Product("Beige Shirt", "Linen cotton fabric keep you warm through out the year.", 500000, R.drawable.man_beige_shirt_pants_casual_wear_fashion, "Áo thun"));
            insertProductData(new Product("Quần", "Đây là sản phẩm 6 có nhiều lượt bán nhất", 300000, R.drawable.jeans, "Quần"));
            insertProductData(new Product("Quần", "Đây là sản phẩm 7 có nhiều lượt bán nhất", 300000, R.drawable.jeans, "Quần"));
            insertProductData(new Product("Giày", "Đây là sản phẩm 8 có nhiều lượt bán nhất", 300000, R.drawable.sneakers, "Giày"));
            insertProductData(new Product("Bóp", "Đây là sản phẩm 9 có nhiều lượt bán nhất", 300000, R.drawable.wallet, "Bóp"));
            insertProductData(new Product("Áo khoác", "Đây là sản phẩm 10 có nhiều lượt bán nhất", 200000, R.drawable.jacket, "Áo khoác"));
            insertProductData(new Product("Áo khoác", "Đây là sản phẩm 11 có nhiều lượt bán nhất", 200000, R.drawable.jacket, "Áo khoác"));
            insertProductData(new Product("Hoodie", "Đây là sản phẩm 12 có nhiều lượt bán nhất", 100000, R.drawable.hoodie, "Hoodie"));
            insertProductData(new Product("Hoodie", "Đây là sản phẩm 13 có nhiều lượt bán nhất", 100000, R.drawable.hoodie, "Hoodie"));
            insertProductData(new Product("Túi xách", "Đây là sản phẩm 14 có nhiều lượt bán nhất", 860000, R.drawable.handbag, "Túi xách"));
            insertProductData(new Product("Áo thun", "Đây là sản phẩm 15 có nhiều lượt bán nhất", 350000, R.drawable.tshirt, "Áo thun"));
            insertProductData(new Product("Áo thun", "Đây là sản phẩm 16 có nhiều lượt bán nhất", 4600000, R.drawable.tshirt, "Áo thun"));
            insertProductData(new Product("Đồng hồ", "Đây là sản phẩm 17 có nhiều lượt bán nhất", 4600000, R.drawable.watch, "Đồng hồ"));
            insertProductData(new Product("Túi xách", "Đây là sản phẩm 18 có nhiều lượt bán nhất", 8700000, R.drawable.handbag, "Túi xách"));
            insertProductData(new Product("Áo khoác", "Đây là sản phẩm 19 có nhiều lượt bán nhất", 8700000, R.drawable.handbag, "Túi xách"));
            insertProductData(new Product("Áo thun", "Đây là sản phẩm 20 có nhiều lượt bán nhất", 8800000, R.drawable.tshirt, "Áo thun"));
            insertProductData(new Product("Áo thun", "Đây là sản phẩm 21 có nhiều lượt bán nhất", 1200000, R.drawable.tshirt, "Áo thun"));
            insertProductData(new Product("Áo thun", "Đây là sản phẩm 22 có nhiều lượt bán nhất", 3400000, R.drawable.tshirt, "Áo thun"));


            randomInsertProductColorData();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.endTransaction();
        }
    }

    private void randomInsertProductColorData() {
        db.beginTransaction();
        try {
            List<Product> products = getAllProducts();
            for (Product product : products) {
                int colorCount = new Random().nextInt(8) + 1;
                for (int i = 0; i < colorCount; i++) {
                    String hexColor = generateRandomHexColor();

                    insertProductColorData(new ProductColor(product.getId(), hexColor, "colorName"));
                }
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.endTransaction();
        }
    }

    private static String generateRandomHexColor() {
        Random random = new Random();

        // Generate random RGB values
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        // Convert RGB to hex
        String hexColor = String.format("#%02X%02X%02X", red, green, blue);

        return hexColor;
    }


    public ArrayList<Product> getProductsByCategory(String category) {
        ArrayList<Product> products = new ArrayList<>();

        db = productDbHelper.getReadableDatabase();
        String[] projection = {ProductContract.ProductEntry.COLUMN_ID, ProductContract.ProductEntry.COLUMN_NAME, ProductContract.ProductEntry.COLUMN_DESCRIPTION, ProductContract.ProductEntry.COLUMN_PRICE, ProductContract.ProductEntry.COLUMN_IMAGE, ProductContract.ProductEntry.COLUMN_CATEGORY, ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY};

        String selection = ProductContract.ProductEntry.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = {category};

        Cursor cursor = db.query(ProductContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

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

            List<ProductColor> colors = getColorsForProduct(productId);
            Product product = new Product(productId, productName, productDescription, productPrice, productImage, productCategory, productQuantity);
            product.addColors(colors);
            products.add(product);

            cursor.moveToNext();
        }

        cursor.close();
        return products;
    }

    public Product getProductById(int id) {
        Product product = null;

        db = productDbHelper.getReadableDatabase();
        String[] projection = {ProductContract.ProductEntry.COLUMN_ID, ProductContract.ProductEntry.COLUMN_NAME, ProductContract.ProductEntry.COLUMN_DESCRIPTION, ProductContract.ProductEntry.COLUMN_PRICE, ProductContract.ProductEntry.COLUMN_IMAGE, ProductContract.ProductEntry.COLUMN_CATEGORY, ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY};

        String selection = ProductContract.ProductEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(ProductContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

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

            List<ProductColor> colors = getColorsForProduct(productId);
            product = new Product(productId, productName, productDescription, productPrice, productImage, productCategory, productQuantity);
            product.addColors(colors);

            cursor.moveToNext();
        }

        cursor.close();
        return product;
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

    private List<ProductColor> getColorsForProduct(int productId) {
        List<ProductColor> colors = new ArrayList<>();
        String[] colorProjection = {ProductContract.ColorEntry.COLUMN_COLOR_PATH};

        String colorSelection = ProductContract.ColorEntry.COLUMN_PRODUCT_ID + " = ?";
        String[] colorSelectionArgs = {String.valueOf(productId)};

        Cursor colorCursor = db.query(ProductContract.ColorEntry.TABLE_NAME, colorProjection, colorSelection, colorSelectionArgs, null, null, null);

        int colorPathIndex = colorCursor.getColumnIndex(ProductContract.ColorEntry.COLUMN_COLOR_PATH);

        colorCursor.moveToFirst();
        while (!colorCursor.isAfterLast()) {
            String colorPath = colorCursor.getString(colorPathIndex);
            ProductColor color = new ProductColor(productId, colorPath, "colorName");
            colors.add(color);

            colorCursor.moveToNext();
        }

        colorCursor.close();
        return colors;
    }


}
