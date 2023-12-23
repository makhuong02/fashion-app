package com.group25.ecommercefashionapp.ui.fragment.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.group25.ecommercefashionapp.R;

public class ErrorDialogFragment extends DialogFragment {
    TextView errorTitleTextView, errorContentTextView;
    String errorTitle, errorContent;
    public ErrorDialogFragment(String errorTitle, String errorContent) {
        this.errorTitle = errorTitle;
        this.errorContent = errorContent;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_error, container, false);

        // Initialize UI components
        AppCompatButton closeButton = view.findViewById(R.id.close_button);
        errorTitleTextView = view.findViewById(R.id.error_title_text);
        errorContentTextView = view.findViewById(R.id.error_content_text);
        // Set click listeners
        errorTitleTextView.setText(errorTitle);
        errorContentTextView.setText(errorContent);

        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Adjust dialog size
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        }
    }
}
