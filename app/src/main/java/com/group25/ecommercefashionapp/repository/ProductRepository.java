package com.group25.ecommercefashionapp.repository;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.CartItem;
import com.group25.ecommercefashionapp.data.CategoryItem;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.data.ProductColor;
import com.group25.ecommercefashionapp.data.ProductImage;
import com.group25.ecommercefashionapp.data.ProductSize;
import com.group25.ecommercefashionapp.database.DatabaseHelper;
import com.group25.ecommercefashionapp.database.ProductContract;
import com.group25.ecommercefashionapp.utilities.ColorUtils;

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
        values.put(ProductContract.ProductEntry.COLUMN_CATEGORY, product.getCategory());
        values.put(ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY, product.getAvailableQuantity());

        long productId = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);

    }

    public void updateProductAfterCheckout(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Product product = getMainActivityInstance().productRepository.getProductById(cartItem.getProductId());
            product.setAvailableQuantity(product.getAvailableQuantity() - cartItem.getQuantity());
            updateProductData(product);
        }
    }

    private void updateProductData(Product product) {
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_NAME, product.getName());
        values.put(ProductContract.ProductEntry.COLUMN_DESCRIPTION, product.getDescription());
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, product.getPrice());
        values.put(ProductContract.ProductEntry.COLUMN_CATEGORY, product.getCategory());
        values.put(ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY, product.getAvailableQuantity());

        String selection = ProductContract.ProductEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(product.getId())};

        db.update(
                ProductContract.ProductEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void insertProductColorData(ProductColor color) {
        ContentValues values = new ContentValues();
        values.put(ProductContract.ColorEntry.COLUMN_PRODUCT_ID, color.getProduct_id());
        values.put(ProductContract.ColorEntry.COLUMN_HEX_COLOR, color.getHexColor());

        long colorId = db.insert(ProductContract.ColorEntry.TABLE_NAME, null, values);
    }

    public void insertProductSizeData(ProductSize size) {
        ContentValues values = new ContentValues();
        values.put(ProductContract.SizeEntry.COLUMN_PRODUCT_ID, size.getProduct_id());
        values.put(ProductContract.SizeEntry.COLUMN_SIZE, size.getName());
        long sizeId = db.insert(ProductContract.SizeEntry.TABLE_NAME, null, values);
    }

    public void insertProductImageData(ProductImage image) {
        ContentValues values = new ContentValues();
        values.put(ProductContract.ImageEntry.COLUMN_PRODUCT_ID, image.getProduct_id());
        values.put(ProductContract.ImageEntry.COLUMN_IMAGE, image.getImage_int_id());
        long imageId = db.insert(ProductContract.ImageEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        Cursor cursor = db.query(ProductContract.ProductEntry.TABLE_NAME, null, null, null, null, null, null

        );
        int idIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_ID);
        int nameIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME);
        int descriptionIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_DESCRIPTION);
        int priceIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);
        int categoryIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_CATEGORY);
        int availableQuantityIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int productId = cursor.getInt(idIndex);
            String productName = cursor.getString(nameIndex);
            String productDescription = cursor.getString(descriptionIndex);
            Integer productPrice = cursor.getInt(priceIndex);
            String productCategory = cursor.getString(categoryIndex);
            Integer productQuantity = cursor.getInt(availableQuantityIndex);

            List<ProductColor> colors = getColorsForProduct(productId);
            List<ProductSize> sizes = getSizesForProduct(productId);
            List<ProductImage> images = getImagesForProduct(productId);
            Product product = new Product(productId, productName, productDescription, productPrice, productCategory, productQuantity);
            product.addColors(colors);
            product.addSizes(sizes);
            product.addImages(images);

            products.add(product);

            cursor.moveToNext();
        }

        cursor.close();
        return products;
    }

    public void deleteAllProducts() {
        db.delete(ProductContract.ProductEntry.TABLE_NAME, null, null);
    }

    public void dropProductTable() {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + ProductContract.ProductEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ProductContract.ColorEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ProductContract.SizeEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ProductContract.ImageEntry.TABLE_NAME);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void insertDbData() {
        db.beginTransaction();
        try {
            if (!isDatabaseEmpty()) {
                return;
            }
            productDbHelper.onCreate(db);
            insertProductData(new Product("Áo Polo Vải Dry Pique Ngắn Tay", "Được cập nhật để có thêm sự thoải mái và phong cách trong mùa này.", 489000, "Áo thun"));
            insertProductData(new Product("Áo Thun Supima Cotton Cổ Tròn", "100% bông SUPIMA® với kết cấu cao cấp. Được thiết kế tỉ mỉ đến từng chi tiết.", 391000, "Áo thun"));
            insertProductData(new Product("Áo Thun Vải Cotton Tay Ngắn", "Đặc biệt nhỏ gọn phù hợp và chiều dài ngắn. Một kiểu dáng mang hơi hướng của thập niên 90.", 293000, "Áo thun"));
            insertProductData(new Product("Chân Váy Xếp Ly Vải Chiffon eo cao", "Thiết kế in họa tiết hình cánh hoa nổi bật. Các nếp gấp tạo nên kiểu dáng thanh lịch.t", 980000, "Váy"));
            insertProductData(new Product("AIRism Áo Thun Vải Rũ Ngắn Tay", "Sự thoải mái và mát mẻ. Kiểu dáng suông mượt.", 391000, "Áo thun"));
            insertProductData(new Product("Quần Vải Cotton Trơn Dáng Relax", "Hiện tại chất liệu vải cotton đã dày hơn. Kiểu dáng đẹp, bạn có thể mặc ở bất cứ nơi nào.", 489000, "Quần"));
            insertProductData(new Product("Quần Smart Pants Dài Đến Mắt Cá", "Co giãn 2 chiều giúp bạn dễ mặc mà vẫn sang trọng. Đường cắt thon gọn cho kiểu dáng đẹp.", 784000, "Quần"));
            insertProductData(new Product("Giày New Balance Pro Court Navy, New Balance CRT300", "Với biểu tượng N nổi bật nằm hai bên thân giày, được tô điểm bằng màu xanh Navy, đôi giày này tạo ra điểm nhấn độc đáo và phong cách.", 1295000, "Giày"));
            insertProductData(new Product("Ví Da Nam Lata", "Mẫu ví da được thiết kế tối ưu gồm nhiều ngăn đựng lớn, nhỏ giúp thuận tiện hơn trong sử dụng.", 269000, "Bóp"));
            insertProductData(new Product("Áo Khoác Kiểu Sơ Mi Dáng Rộng", "Một lớp khoác ngoài với kiểu dáng vai rũ. Gói gọn trong một thiết kế đầy tinh tế.", 1471000, "Áo khoác"));
            insertProductData(new Product("Áo Khoác Giả Lông Cừu Kéo Khóa Dài Tay", "Trọng lượng nhẹ nhưng vẫn giữ ấm tốt: Chất vải giả lông cừu dày, đàn hồi.", 686000, "Áo khoác"));
            insertProductData(new Product("Studio Ghibli Áo Nỉ Dài Tay", "Bộ sưu tập khắc họa vũ trụ Studio Ghibli đa dạng và độc đáo đã đến với UT.", 784000, "Hoodie"));
            insertProductData(new Product("Áo Hoodie Chui Đầu Giả Lông Cừu Dài Tay", "Chất vải giả lông cừu mềm mại và ấm áp. Thêm phần phong cách với họa tiết nhỏ ở ngực áo.", 784000, "Hoodie"));
            insertProductData(new Product("Túi Xách Nhỏ Marina", "Túi thiết kế dáng nhỏ gọn, nổi bật khoá kim loại", 843000, "Túi xách"));
            insertProductData(new Product("Orient SK RA-AA0B02R19B", "Phiên bản mạ vàng với mẫu kim chỉ nổi bật trên mặt số", 4600000, "Đồng hồ"));
            insertProductData(new Product("Túi Bán Nguyệt Giả Da Mini Đeo Chéo", "Túi đeo vai tròn được làm bằng đệm để tạo cảm giác thoáng mát.", 7800000, "Túi xách"));
            insertProductData(new Product("AirSense Áo Khoác (Vải Lai Cotton)", "Kết hợp sự thoải mái và hiệu suất đáng kinh ngạc. Một chiếc áo khoác đa năng với cảm giác tự nhiên", 1471000, "Áo Khoác"));
            insertProductData(new Product("Chân Váy Mini Vải Lông Cừu Cao Cấp","Được dệt kim bằng len chất lượng cao. Tạo kiểu theo cách riêng của bạn.", 489000, "Váy"));
            insertProductData(new Product("Giày Sandal Gót Thanh Phối Si Vân", "Gót cao 11 cm cho bạn những bước đi uyển chuyển, tự tin khi diện", 489000, "Giày"));

            randomInsertProductColorData();
            randomInsertProductSizeData();
            randomInsertProductImageData();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.endTransaction();
        }
    }

    private boolean isDatabaseEmpty() {
        // Check if the database has any records
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + ProductContract.ProductEntry.TABLE_NAME, null);
        if (cursor != null) {
            try {
                cursor.moveToFirst();
                int count = cursor.getInt(0);
                return count == 0;
            } finally {
                cursor.close();
            }
        }
        return true;
    }

    private void randomInsertProductColorData() {
        db.beginTransaction();
        try {
            List<Product> products = getAllProducts();
            for (Product product : products) {
                int colorCount = new Random().nextInt(8) + 1;
                while (colorCount > 0) {
                    String hexColor = generateRandomHexColor();
                    String colorName = ColorUtils.getColorNameFromHex(hexColorToInteger(hexColor));
                    insertProductColorData(new ProductColor(product.getId(), hexColor, colorName));
                    colorCount--;
                }
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.endTransaction();
        }
    }

    private void randomInsertProductSizeData() {
        db.beginTransaction();
        try {
            List<Product> products = getAllProducts();
            for (Product product : products) {
                int sizeCount = new Random().nextInt(5) + 1;
                while (sizeCount > 0) {
                    String size = generateRandomSize();
                    insertProductSizeData(new ProductSize(size, product.getId()));
                    sizeCount--;
                }
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.endTransaction();
        }
    }

    private void randomInsertProductImageData() {
        db.beginTransaction();
        try {
            List<Product> products = getAllProducts();
            for (Product product : products) {
                int imageCount = new Random().nextInt(5) + 1;
                while (imageCount > 0) {
                    int image = generateImageByCategory(product.getCategory());
                    insertProductImageData(new ProductImage(product.getId(), image, product.getName()));
                    imageCount--;
                }
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.endTransaction();
        }
    }

    private static String generateRandomSize() {
        Random random = new Random();

        // Define an array of size labels
        String[] sizes = {"XXL", "XL", "L", "M", "S", "XS"};

        // Generate a random index to select a size label from the array
        int index = random.nextInt(sizes.length);

        // Get the random size label
        String randomSize = sizes[index];

        return randomSize;
    }

    private static List<ProductSize> sortSizes(List<ProductSize> sizes) {
        List<ProductSize> sortedSizes = new ArrayList<>();
        for (String size : new String[]{"XS", "S", "M", "L", "XL", "XXL"}) {
            for (ProductSize productSize : sizes) {
                if (productSize.getName().equals(size)) {
                    sortedSizes.add(productSize);
                }
            }
        }
        return sortedSizes;
    }

    private int hexColorToInteger(String hexColor) {
        return Integer.parseInt(hexColor.substring(1), 16);
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


    private static Integer generateImageByCategory(String category) {
        int image = 0;
        switch (category) {
            case "Áo thun":
                Random random = new Random();
                Integer[] images = {R.drawable.man_beige_shirt_pants_casual_wear_fashion, R.drawable.portrait_handsome_young_businessman};
                int index = random.nextInt(images.length);

                image = images[index];

                break;
            case "Váy":
                image = R.drawable.skirt1;
                break;
            case "Quần":
                image = R.drawable.pants1;
                break;
            case "Áo khoác":
                image = R.drawable.jacket1;
                break;
            case "Hoodie":
                image = R.drawable.hoodie1;
                break;
            case "Giày":
                image = R.drawable.shoes1;
                break;
            case "Đồng hồ":
                image = R.drawable.watch1;
                break;
            case "Bóp":
                image = R.drawable.wallet1;
                break;
            case "Túi xách":
                image = R.drawable.handbag1;
                break;
        }
        return image;
    }


    public ArrayList<Product> getProductsByCategory(String category) {
        ArrayList<Product> products = new ArrayList<>();

        db = productDbHelper.getReadableDatabase();
        String[] projection = {ProductContract.ProductEntry.COLUMN_ID, ProductContract.ProductEntry.COLUMN_NAME, ProductContract.ProductEntry.COLUMN_DESCRIPTION, ProductContract.ProductEntry.COLUMN_PRICE, ProductContract.ProductEntry.COLUMN_CATEGORY, ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY};

        String selection = ProductContract.ProductEntry.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = {category};

        Cursor cursor = db.query(ProductContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        int idIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_ID);
        int nameIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME);
        int descriptionIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_DESCRIPTION);
        int priceIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);
        int categoryIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_CATEGORY);
        int availableQuantityIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int productId = cursor.getInt(idIndex);
            String productName = cursor.getString(nameIndex);
            String productDescription = cursor.getString(descriptionIndex);
            int productPrice = cursor.getInt(priceIndex);
            String productCategory = cursor.getString(categoryIndex);
            int productQuantity = cursor.getInt(availableQuantityIndex);

            List<ProductColor> colors = getColorsForProduct(productId);
            List<ProductSize> sizes = getSizesForProduct(productId);
            List<ProductImage> images = getImagesForProduct(productId);
            Product product = new Product(productId, productName, productDescription, productPrice, productCategory, productQuantity);
            product.addColors(colors);
            product.addSizes(sortSizes(sizes));
            product.addImages(images);

            products.add(product);

            cursor.moveToNext();
        }

        cursor.close();
        return products;
    }

    public List<Product> getProductsBySearchAndCategory(String search, String category) {
        List<Product> products = new ArrayList<>();

        db = productDbHelper.getReadableDatabase();
        String[] projection = {ProductContract.ProductEntry.COLUMN_ID, ProductContract.ProductEntry.COLUMN_NAME, ProductContract.ProductEntry.COLUMN_DESCRIPTION, ProductContract.ProductEntry.COLUMN_PRICE, ProductContract.ProductEntry.COLUMN_CATEGORY, ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY};

        String selection = ProductContract.ProductEntry.COLUMN_CATEGORY + " = ? AND " + ProductContract.ProductEntry.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {category, "%" + search + "%"};

        Cursor cursor = db.query(ProductContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        int idIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_ID);
        int nameIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME);
        int descriptionIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_DESCRIPTION);
        int priceIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);
        int categoryIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_CATEGORY);
        int availableQuantityIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int productId = cursor.getInt(idIndex);
            String productName = cursor.getString(nameIndex);
            Log.d("MainActivity", "getProductsBySearchAndCategory: " + productName);
            String productDescription = cursor.getString(descriptionIndex);
            int productPrice = cursor.getInt(priceIndex);
            String productCategory = cursor.getString(categoryIndex);
            int productQuantity = cursor.getInt(availableQuantityIndex);

            List<ProductColor> colors = getColorsForProduct(productId);
            List<ProductSize> sizes = getSizesForProduct(productId);
            List<ProductImage> images = getImagesForProduct(productId);
            Product product = new Product(productId, productName, productDescription, productPrice, productCategory, productQuantity);
            product.addColors(colors);
            product.addSizes(sortSizes(sizes));
            product.addImages(images);

            products.add(product);

            cursor.moveToNext();
        }
        Log.d("MainActivity", "getProductsBySearchAndCategory: " + products.size());

        cursor.close();
        return products;
    }

    public Product getProductById(int id) {
        Product product = null;

        db = productDbHelper.getReadableDatabase();
        String[] projection = {ProductContract.ProductEntry.COLUMN_ID, ProductContract.ProductEntry.COLUMN_NAME, ProductContract.ProductEntry.COLUMN_DESCRIPTION, ProductContract.ProductEntry.COLUMN_PRICE, ProductContract.ProductEntry.COLUMN_CATEGORY, ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY};

        String selection = ProductContract.ProductEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(ProductContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        int idIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_ID);
        int nameIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME);
        int descriptionIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_DESCRIPTION);
        int priceIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);
        int categoryIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_CATEGORY);
        int availableQuantityIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_AVAILABLE_QUANTITY);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int productId = cursor.getInt(idIndex);
            String productName = cursor.getString(nameIndex);
            String productDescription = cursor.getString(descriptionIndex);
            int productPrice = cursor.getInt(priceIndex);
            String productCategory = cursor.getString(categoryIndex);
            int productQuantity = cursor.getInt(availableQuantityIndex);

            List<ProductColor> colors = getColorsForProduct(productId);
            List<ProductSize> sizes = getSizesForProduct(productId);
            List<ProductImage> images = getImagesForProduct(productId);
            product = new Product(productId, productName, productDescription, productPrice, productCategory, productQuantity);
            product.addColors(colors);
            product.addSizes(sortSizes(sizes));
            product.addImages(images);

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
        String[] colorProjection = {ProductContract.ColorEntry.COLUMN_HEX_COLOR};

        String colorSelection = ProductContract.ColorEntry.COLUMN_PRODUCT_ID + " = ?";
        String[] colorSelectionArgs = {String.valueOf(productId)};

        Cursor colorCursor = db.query(ProductContract.ColorEntry.TABLE_NAME, colorProjection, colorSelection, colorSelectionArgs, null, null, null);

        int colorHexIndex = colorCursor.getColumnIndex(ProductContract.ColorEntry.COLUMN_HEX_COLOR);

        colorCursor.moveToFirst();
        while (!colorCursor.isAfterLast()) {
            String colorHex = colorCursor.getString(colorHexIndex);
            String colorName = ColorUtils.getColorNameFromHex(hexColorToInteger(colorHex));
            ProductColor color = new ProductColor(productId, colorHex, colorName);
            colors.add(color);

            colorCursor.moveToNext();
        }

        colorCursor.close();
        return colors;
    }

    private List<ProductSize> getSizesForProduct(int productId) {
        List<ProductSize> sizes = new ArrayList<>();
        String[] sizeProjection = {ProductContract.SizeEntry.COLUMN_SIZE};

        String sizeSelection = ProductContract.SizeEntry.COLUMN_PRODUCT_ID + " = ?";
        String[] sizeSelectionArgs = {String.valueOf(productId)};

        Cursor sizeCursor = db.query(ProductContract.SizeEntry.TABLE_NAME, sizeProjection, sizeSelection, sizeSelectionArgs, null, null, null);

        int sizeNameIndex = sizeCursor.getColumnIndex(ProductContract.SizeEntry.COLUMN_SIZE);

        sizeCursor.moveToFirst();
        while (!sizeCursor.isAfterLast()) {
            String sizeName = sizeCursor.getString(sizeNameIndex);
            ProductSize size = new ProductSize(sizeName, productId);
            sizes.add(size);

            sizeCursor.moveToNext();
        }

        sizeCursor.close();
        return sortSizes(sizes);
    }

    private List<ProductImage> getImagesForProduct(int productId) {
        List<ProductImage> images = new ArrayList<>();
        String[] imageProjection = {ProductContract.ImageEntry.COLUMN_IMAGE};

        String imageSelection = ProductContract.ImageEntry.COLUMN_PRODUCT_ID + " = ?";
        String[] imageSelectionArgs = {String.valueOf(productId)};

        Cursor imageCursor = db.query(ProductContract.ImageEntry.TABLE_NAME, imageProjection, imageSelection, imageSelectionArgs, null, null, null);

        int imageIdIndex = imageCursor.getColumnIndex(ProductContract.ImageEntry.COLUMN_IMAGE);

        imageCursor.moveToFirst();
        while (!imageCursor.isAfterLast()) {
            int imageId = imageCursor.getInt(imageIdIndex);
            ProductImage image = new ProductImage(productId, imageId, "");
            images.add(image);

            imageCursor.moveToNext();
        }

        imageCursor.close();
        return images;
    }
}
