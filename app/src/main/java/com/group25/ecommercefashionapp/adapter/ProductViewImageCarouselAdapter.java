package com.group25.ecommercefashionapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.data.ProductImage;
import com.group25.ecommercefashionapp.ui.fragment.bottomsheet.PhotoLongClickBottomSheetFragment;

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

        PhotoView image = itemView.findViewById(R.id.carousel_image_view);
        TextView currentImageTextView = itemView.findViewById(R.id.current_image_indicator_text_view);
        TextView totalImageTextView = itemView.findViewById(R.id.image_size_indicator_text_view);

        currentImageTextView.setText(String.valueOf(position + 1));
        totalImageTextView.setText(String.valueOf(imageList.size()));

//        BitmapDrawable drawable = (BitmapDrawable) context.getResources().getDrawable(imageList.get(position).getImage_int_id());
//        Bitmap bitmap = drawable.getBitmap();

//        image.setImageBitmap(bitmap);
        image.setOnLongClickListener(v -> {
            PhotoLongClickBottomSheetFragment photoLongClickBottomSheetFragment = new PhotoLongClickBottomSheetFragment((BitmapDrawable) image.getDrawable());
            photoLongClickBottomSheetFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "photo_long_click_bottom_sheet");
            return false;
        });

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
