package com.group25.ecommercefashionapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.api.ApiServiceBuilder;
import com.group25.ecommercefashionapp.data.ProductImage;
import com.group25.ecommercefashionapp.interfaces.onclicklistener.OnItemClickListener;
import com.squareup.picasso.Picasso;

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

        ImageView image = itemView.findViewById(R.id.carousel_image_view);
        TextView currentImageTextView = itemView.findViewById(R.id.current_image_indicator_text_view);
        TextView totalImageTextView = itemView.findViewById(R.id.image_size_indicator_text_view);

        currentImageTextView.setText(String.valueOf(position + 1));
        totalImageTextView.setText(String.valueOf(imageList.size()));
        String imageNames = "";
        if(!imageList.isEmpty()) {
            imageNames = imageList.get(position).getImagePath();
        }
        Picasso.get()
                .load(ApiServiceBuilder.BASE_URL +"product-images/"+ imageNames)
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.ic_connection_error)
                .into(image);
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
