package com.group25.ecommercefashionapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.group25.ecommercefashionapp.extensions.ImageViewExtension;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ChipImagesView extends LinearLayout {

    private int maxChipCount;
    private int chipImageSize;
    private Map<Integer, View> chipViews = new LinkedHashMap<>();

    public ChipImagesView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);

        LayoutInflater.from(context).inflate(R.layout.view_progressbar, this, true);
        final int[] Q = {R.styleable.chipSizeAppearance_chipSize, R.styleable.maxSizeAppearance_maxSize};
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Q,0,0);
        this.maxChipCount = obtainStyledAttributes.getInteger(R.styleable.YourCustomView_maxChipCount, 8);
        this.chipImageSize = (int) obtainStyledAttributes.getDimension(0, getResources().getDimension(R.dimen.size_chip_image));
        obtainStyledAttributes.recycle();
    }

    public static final void setChips(ChipImagesView chipImagesView, List<String> colorChips) {
        chipImagesView.setChipImages(colorChips);
    }

    private void setChipImages(List<String> colorChips) {
        List<ImageView> chipImageViews = initializeChipImageViews(
                R.id.chip_image1, R.id.chip_image2, R.id.chip_image3,
                R.id.chip_image4, R.id.chip_image5, R.id.chip_image6,
                R.id.chip_image7, R.id.chip_image8
        );

        int chipCount = Math.min(colorChips.size(), maxChipCount);
        for (int i = 0; i < chipImageViews.size(); i++) {
            ImageView chipImageView = chipImageViews.get(i);
            if (i < chipCount) {
                configureVisibleChip(chipImageView, colorChips.get(i));
            } else if (i == chipImageViews.size() - 2 && colorChips.size() >= maxChipCount) {
                configurePlusIcon(chipImageView);
            } else if (i >= maxChipCount - 2) {
                chipImageView.setVisibility(View.GONE);
            } else {
                configureVisibleChip(chipImageView, "#FFFFFF");
            }
        }
    }

    private List<ImageView> initializeChipImageViews(int... chipImageIds) {
        List<ImageView> chipImageViews = new ArrayList<>();
        for (int chipImageId : chipImageIds) {
            ImageView chipImageView = (ImageView) a(chipImageId);
            ViewGroup.LayoutParams layoutParams = chipImageView.getLayoutParams();
            layoutParams.width = chipImageSize;
            layoutParams.height = chipImageSize;
            chipImageViews.add(chipImageView);
        }
        return chipImageViews;
    }

    private void configureVisibleChip(ImageView chipImageView, String color) {
        chipImageView.setVisibility(View.VISIBLE);
        chipImageView.setBackgroundColor(Color.parseColor(color));
        ViewGroup.LayoutParams layoutParams = chipImageView.getLayoutParams();
        layoutParams.width = chipImageSize;
        layoutParams.height = chipImageSize;
    }

    private void configurePlusIcon(ImageView chipImageView) {
        chipImageView.setVisibility(View.VISIBLE);
        chipImageView.setImageResource(R.drawable.ic_plus_small);
        ViewGroup.LayoutParams layoutParams = chipImageView.getLayoutParams();
        layoutParams.width = (int) chipImageView.getResources().getDimension(R.dimen.size_chip_image_small);
        layoutParams.height = (int) chipImageView.getResources().getDimension(R.dimen.size_chip_image_small);
    }

    public View a(int viewId) {
        Map<Integer, View> viewMap = chipViews;
        View view = viewMap.get(viewId);
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(viewId);
        if (findViewById != null) {
            viewMap.put(viewId, findViewById);
            return findViewById;
        }
        return null;
    }
}
