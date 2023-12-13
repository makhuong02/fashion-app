package com.group25.ecommercefashionapp.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.ProductImage;

import java.util.List;

public class ProductViewImageCarouselAdapter extends PagerAdapter {

    private final Context context;
    private final List<ProductImage> imageList;


    public ProductViewImageCarouselAdapter(Context context, List<ProductImage> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.view_product_carousel_item, container, false);

        ImageView image = itemView.findViewById(R.id.carousel_image_view);
        TextView currentImageTextView = itemView.findViewById(R.id.current_image_indicator_text_view);
        TextView totalImageTextView = itemView.findViewById(R.id.image_size_indicator_text_view);

        currentImageTextView.setText(String.valueOf(position + 1));
        totalImageTextView.setText(String.valueOf(imageList.size()));
        image.setImageResource(imageList.get(position).getImage_int_id());

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
