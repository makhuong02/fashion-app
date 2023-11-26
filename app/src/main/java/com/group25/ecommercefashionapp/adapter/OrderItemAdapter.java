package com.group25.ecommercefashionapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.Orders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class OrderItemAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Orders> orderList;

    public OrderItemAdapter(Context context, int layout, List<Orders> orderList) {
        this.context = context;
        this.layout = layout;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);

        TextView txtID = convertView.findViewById(R.id.orderID);
        TextView txtDate = convertView.findViewById(R.id.orderDate);
        TextView txtPrice = convertView.findViewById(R.id.totalPrice);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Orders order = orderList.get(position);
        txtID.setText(String.valueOf(order.getOrderID()));
        txtDate.setText(dateFormat.format(order.getDate()));
        txtPrice.setText(String.format("%,d", order.getTotalPrice()));

        return convertView;
    }
}
