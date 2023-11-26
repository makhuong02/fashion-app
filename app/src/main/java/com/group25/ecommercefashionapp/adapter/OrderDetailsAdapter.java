package com.group25.ecommercefashionapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.Product;

import org.w3c.dom.Text;

import java.util.List;

public class OrderDetailsAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Product> productList;


    public OrderDetailsAdapter(Context context, int layout, List<Product> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
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

        TextView txtID = convertView.findViewById(R.id.detailsID);
        TextView txtName = convertView.findViewById(R.id.detailsName);
        TextView txtPrice = convertView.findViewById(R.id.detailsPrice);

        Product product = productList.get(position);
        txtID.setText(String.valueOf(product.getId()));
        txtName.setText(product.getName());
        txtPrice.setText(String.format("%,d", product.getPrice()));

        return convertView;
    }
}
