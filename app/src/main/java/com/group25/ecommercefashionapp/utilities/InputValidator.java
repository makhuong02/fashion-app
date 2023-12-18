package com.group25.ecommercefashionapp.utilities;

import static com.group25.ecommercefashionapp.MyApp.getMainActivityInstance;

import com.group25.ecommercefashionapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {

    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(getString(R.string.password_regEx));
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(getString(R.string.email_regEx));
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern;
        Matcher matcher;

        if (phoneNumber.charAt(0) == '0') {
            pattern = Pattern.compile(getString(R.string.phone_number_regEx));
        } else {
            pattern = Pattern.compile(getString(R.string._84_phone_number_regEx));
        }

        matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private static String getString(int resId) {
        return getMainActivityInstance().getString(resId);
    }
}
