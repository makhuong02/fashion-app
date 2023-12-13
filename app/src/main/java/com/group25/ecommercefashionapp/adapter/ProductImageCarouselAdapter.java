package com.group25.ecommercefashionapp.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;

import com.group25.ecommercefashionapp.OnItemClickListener;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.ProductImage;

import java.util.List;

public class ProductImageCarouselAdapter extends PagerAdapter {

    private final Context context;
    private final List<ProductImage> imageList;
    OnItemClickListener clickListener;

    public ProductImageCarouselAdapter(Context context, List<ProductImage> imageList, OnItemClickListener clickListener) {
        this.context = context;
        this.imageList = imageList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.product_carousel_item, container, false);

        // Customize the item view based on your requirements
        // For example, you can set text or images here
        ImageView image = itemView.findViewById(R.id.carousel_image_view);
        TextView currentImageTextView = itemView.findViewById(R.id.current_image_indicator_text_view);
        TextView totalImageTextView = itemView.findViewById(R.id.image_size_indicator_text_view);

        currentImageTextView.setText(String.valueOf(position + 1));
        totalImageTextView.setText(String.valueOf(imageList.size()));
        image.setImageResource(imageList.get(position).getImage_int_id());
        image.setOnClickListener(v -> clickListener.onItemClick(v, imageList.get(position)));

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
