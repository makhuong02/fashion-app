package com.group25.ecommercefashionapp.ui.fragment.bottomsheet;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.group25.ecommercefashionapp.R;
import com.group25.ecommercefashionapp.utilities.ImageSaver;
import com.group25.ecommercefashionapp.utilities.ImageShareUtils;

public class PhotoLongClickBottomSheetFragment extends BottomSheetDialogFragment {

    CardView saveToPhoneCardView, shareCardView;
    BitmapDrawable drawable;

    public PhotoLongClickBottomSheetFragment(BitmapDrawable drawable) {
        // Required empty public constructor
        this.drawable = drawable;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_photo_long_click_bottom_sheet, container, false);

        saveToPhoneCardView = view.findViewById(R.id.saveActionCardView);
        shareCardView = view.findViewById(R.id.shareActionCardView);
        saveToPhoneCardView.setOnClickListener(v -> {
            ImageSaver.saveImageToPhone(getContext(), drawable.getBitmap());
            dismiss();
        });
        shareCardView.setOnClickListener(v -> {
            ImageShareUtils.shareImage(getContext(), drawable.getBitmap(),"");
            dismiss();
        });

        return view;
    }
}

