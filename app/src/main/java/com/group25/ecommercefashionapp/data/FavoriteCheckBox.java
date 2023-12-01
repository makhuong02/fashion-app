package com.group25.ecommercefashionapp.data;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatCheckBox;

public class FavoriteCheckBox extends AppCompatCheckBox {

    private boolean isBffVersion2;

    public FavoriteCheckBox(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FavoriteCheckBox(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        initialize();
    }

    private void initialize() {
        // Additional initialization logic can be added here
    }

    public static void setBffVersion2(FavoriteCheckBox favoriteCheckBox, boolean isBffVersion2) {
        favoriteCheckBox.setIsBffVersion2(isBffVersion2);
    }

    private void setIsBffVersion2(boolean isBffVersion2) {
        this.isBffVersion2 = isBffVersion2;
    }

    @Override
    public boolean isChecked() {
        return !isBffVersion2 && super.isChecked();
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isBffVersion2) {
            setChecked(false);
        }
    }
}
