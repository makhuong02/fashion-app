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

public class OrdersItemAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Orders> ordersList;

    public OrdersItemAdapter(Context context, int layout, List<Orders> ordersList) {
        this.context = context;
        this.layout = layout;
        this.ordersList = ordersList;
    }

    @Override
    public int getCount() {
        return ordersList.size();
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

        TextView txtOrderID = (TextView) convertView.findViewById(R.id.orderID);
        TextView txtOrderDate = (TextView) convertView.findViewById(R.id.orderDate);
        TextView txtTotalPrice = (TextView) convertView.findViewById(R.id.totalPrice);

        Orders order = ordersList.get(position);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(order.getDate());

        txtOrderID.setText(order.getOrderID());
        txtOrderDate.setText(strDate);
        txtTotalPrice.setText(order.getTotalPrice());

        return convertView;
    }
}
