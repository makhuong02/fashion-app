package com.group25.ecommercefashionapp.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String convertDateFormat(String inputDate) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm:ss", Locale.getDefault());
        Date date;
        try {
            date = inputDateFormat.parse(inputDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // or handle the error as needed
        }
        return outputDateFormat.format(date);
    }
}
