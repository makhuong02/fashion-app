package com.group25.ecommercefashionapp.utilities;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class PhoneNumberFormatter implements TextWatcher {
    private boolean isFormatting = false;
    private TextInputLayout textInputLayout;
    public PhoneNumberFormatter(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        // Not used
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        // Not used
    }

    @Override
    public void afterTextChanged(Editable editable) {
        textInputLayout.setHelperText("");
        if (!isFormatting) {
            isFormatting = true;
            String vietnamPhoneNumberPattern;
            try {
                if (editable.charAt(0) == '0') {
                    vietnamPhoneNumberPattern = "XXX XXX XXXX";
                } else {
                    vietnamPhoneNumberPattern = "XX XXX XXXX";
                }
            } catch (Exception ignored) {
                vietnamPhoneNumberPattern = "XX XXX XXXX";
            }

            // Delete all non-digits characters
            String phoneNumber = editable.toString().replaceAll("\\D", "");

            int index = 0;
            StringBuilder formattedPhoneNumber = new StringBuilder();

            // Format phone number
            for (int i = 0; i < vietnamPhoneNumberPattern.length(); i++) {
                char c = vietnamPhoneNumberPattern.charAt(i);
                if (c == 'X') {
                    if (index < phoneNumber.length()) {
                        formattedPhoneNumber.append(phoneNumber.charAt(index));
                        index++;
                    }
                } else {
                    if (index < phoneNumber.length()) {
                        formattedPhoneNumber.append(c);
                    }
                }
            }
            editable.replace(0, editable.length(), formattedPhoneNumber);

            isFormatting = false;
        }
    }
}
