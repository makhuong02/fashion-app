package com.group25.ecommercefashionapp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.group25.ecommercefashionapp.data.Orders;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.database.DatabaseHelper;
import com.group25.ecommercefashionapp.database.OrderContract;
import com.group25.ecommercefashionapp.database.OrderDetailsContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrdersRepository {
    private SQLiteDatabase db;
    public ArrayList<Orders> orders = new ArrayList<>();
    ArrayList<Product> products = new ArrayList<>();
    private final DatabaseHelper orderDBHelper;

    public OrdersRepository(DatabaseHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
        this.orderDBHelper = dbHelper;

        dropTable();
        initDBData();
        getOrders();
    }



    private void initDBData() {
        db.beginTransaction();
        try {
            orderDBHelper.onCreate(db);

            insertOrder(1,"2023-11-13");
            insertOrder(2,"2023-11-15");
            insertOrder(3,"2023-11-10");
            insertOrder(4,"2023-11-20");
            insertOrder(5,"2023-11-21");
            insertOrder(6,"2023-11-11");
            insertOrder(7,"2023-11-27");
            insertOrder(8,"2023-11-18");
            insertOrder(9,"2023-11-19");
            insertOrder(10,"2023-11-09");
            insertOrder(11,"2023-11-07");
            insertOrder(12,"2023-11-10");
            insertOrder(13,"2023-11-11");
            insertOrder(14,"2023-11-17");
            insertOrder(15,"2023-11-24");
            insertOrder(16,"2023-11-21");

            insertOrderDetails(1,1,13);
            insertOrderDetails(2,1,1);
            insertOrderDetails(3,1,12);
            insertOrderDetails(1,2,2);
            insertOrderDetails(1,3,3);
            insertOrderDetails(1,4,15);
            insertOrderDetails(2,4,20);
            insertOrderDetails(1,5,21);
            insertOrderDetails(2,5,17);
            insertOrderDetails(3,5,7);
            insertOrderDetails(4,5,9);
            insertOrderDetails(1,6,10);
            insertOrderDetails(1,7,3);
            insertOrderDetails(2,7,5);
            insertOrderDetails(1,8,4);
            insertOrderDetails(1,9,14);
            insertOrderDetails(1,10,16);
            insertOrderDetails(1,11,19);
            insertOrderDetails(2,11,13);
            insertOrderDetails(1,12,14);
            insertOrderDetails(1,13,12);
            insertOrderDetails(1,14,11);
            insertOrderDetails(1,15,15);
            insertOrderDetails(1,16,17);
            insertOrderDetails(2,16,19);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.endTransaction();
        }
    }
    private void insertOrder(int id, String date) {
        ContentValues values = new ContentValues();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        values.put(OrderContract.OrderEntry.COLUMN_ORDER_ID, id);
        values.put(OrderContract.OrderEntry.COLUMN_DATE, date);

        db.insert(OrderContract.OrderEntry.TABLE_NAME, null, values);
    }

    private void insertOrderDetails(int ordinalNumber, int orderID, int productID) {
        ContentValues values = new ContentValues();

        values.put(OrderDetailsContract.OrderDetailsEntry.COLUMN_ORDINAL_NUMBER, ordinalNumber);
        values.put(OrderDetailsContract.OrderDetailsEntry.COLUMN_ORDER_ID, orderID);
        values.put(OrderDetailsContract.OrderDetailsEntry.COLUMN_PRODUCT_ID, productID);

        db.insert(OrderDetailsContract.OrderDetailsEntry.TABLE_NAME, null, values);
    }

    private ArrayList<Product> getOrderItems(int orderID) {
        ArrayList<Product> list = new ArrayList<>();
        String command = String.format("Select product_id from OrderDetails where order_id = %d", orderID);
        Cursor cursor = orderDBHelper.QueryData(command);
        while (cursor.moveToNext()) {
            if (!cursor.isNull(0)) {
                int itemID = cursor.getInt(0);
                for (Product p : products) {
                    if (p.getId() == itemID) {
                        list.add(p);
                        break;
                    }
                }
            }
        }
        cursor.close();

        return list;
    }
    private int getTotalPrice(ArrayList<Product> list) {
        int total = 0;
        for (Product product : list) {
            if(product.getPrice() != null) {
                total += product.getPrice();
            }
        }

        return total;
    }



    public ArrayList<Orders> getOrders() {
        orders.removeAll(orders);

//        ProductRepository productRepository = new ProductRepository(orderDBHelper);
//        products = productRepository.getAllProducts();

        Cursor cursor = orderDBHelper.QueryData("Select * from Orders");
        while (cursor.moveToNext()) {
            int orderID = cursor.getInt(0);
            Date date = null;
            try {
                date = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(1));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if(date != null) {
                Orders order = new Orders(orderID, date);
                orders.add(order);
            }
        }
        cursor.close();

        for (Orders order : orders) {
            order.setProducts(getOrderItems(order.getOrderID()));
            order.setTotalPrice(getTotalPrice(order.getProducts()));
        }

        return orders;
    }

    public void dropTable() {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + OrderContract.OrderEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + OrderDetailsContract.OrderDetailsEntry.TABLE_NAME);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
