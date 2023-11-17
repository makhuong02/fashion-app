package com.group25.ecommercefashionapp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.CategoryItem;
import com.group25.ecommercefashionapp.data.Orders;
import com.group25.ecommercefashionapp.data.Product;
import com.group25.ecommercefashionapp.database.OrderContract;
import com.group25.ecommercefashionapp.database.ProductContract;
import com.group25.ecommercefashionapp.database.DatabaseHelper;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrdersRepository {
    private SQLiteDatabase db;
    private final DatabaseHelper orderDBHelper;

    public OrdersRepository(DatabaseHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
        this.orderDBHelper = dbHelper;
    }

    public void insertProductData(Orders order) {
        ContentValues values = new ContentValues();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Product i : order.getProducts()) {
            values.put(OrderContract.OrderEntry.COLUMN_ORDINAL_NUMBER, order.getOrdinalNumber());
            values.put(OrderContract.OrderEntry.COLUMN_ORDER_ID, order.getOrderID());
            values.put(OrderContract.OrderEntry.COLUMN_DATE, dateFormat.format(order.getDate()));
            values.put(OrderContract.OrderEntry.COLUMN_PRODUCT_ID, i.getId());
            db.insert(OrderContract.OrderEntry.TABLE_NAME, null, values);
        }
    }

    public ArrayList<Orders> getAllOrders() {
        ArrayList<Orders> orders = new ArrayList<>();
        Cursor cursor = orderDBHelper.QueryData("Select * from Orders");

        while (cursor.moveToNext()) {
            int orderID = cursor.getInt(1);
            int productID = cursor.getInt(3);
            Orders order = (Orders) orders.stream().filter(filterOrder -> orderID == filterOrder.getOrderID());
            if (order != null) {
//                order.getProducts().add();
            }
            else {
                int ordinalNumnber = cursor.getInt(0);
                Date date = new Date(cursor.getLong(2));

            }
        }

        cursor.close();
        return orders;
    }
}
