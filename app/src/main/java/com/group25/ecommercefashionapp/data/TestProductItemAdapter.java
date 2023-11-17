package com.group25.ecommercefashionapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.group25.ecommercefashionapp.R;

import java.util.List;

public class TestProductItemAdapter extends BaseAdapter {
    private Context context;
    private List<TestProductItem> items;
    private int layout;

    public TestProductItemAdapter(Context context, int layout, List<TestProductItem> items) {
        this.context = context;
        this.layout = layout;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder {
        ImageView img;
        TextView txtName;
        TextView txtPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TestProductItemAdapter.ViewHolder holder;

        if (convertView == null) {
            holder = new TestProductItemAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder.txtName = (TextView) convertView.findViewById(R.id.productName);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.productPrice);
            holder.img = (ImageView) convertView.findViewById(R.id.productImage);
            convertView.setTag(holder);
        } else {
            holder = (TestProductItemAdapter.ViewHolder) convertView.getTag();
        }

        TestProductItem item = items.get(position);
        holder.txtName.setText(item.getName());
        holder.txtPrice.setText(String.format("%,d", item.getPrice()));
        holder.img.setImageResource(item.getImage());

        return convertView;
    }
}
