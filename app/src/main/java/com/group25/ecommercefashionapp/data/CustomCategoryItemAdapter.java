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

public class CustomCategoryItemAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryItem> items;
    private int layout;

    public CustomCategoryItemAdapter(Context context, int layout, List<CategoryItem> items) {
        this.context = context;
        this.items = items;
        this.layout = layout;
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

    private class ViewHolder {
        ImageView img;
        TextView txt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txt = (TextView) convertView.findViewById(R.id.categoryName);
            holder.img = (ImageView) convertView.findViewById(R.id.categoryImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CategoryItem item = items.get(position);
        holder.txt.setText(item.getCategory_name());
        holder.img.setImageResource(item.getImgID());

        return convertView;
    }
}
