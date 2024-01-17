package com.group25.ecommercefashionapp.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.group25.ecommercefashionapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageSaver {
    public static void saveImageToPhone(Context context, Bitmap bitmap) {

        // Create a file name for the image
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = context.getString(R.string.app_name) + "_" + timeStamp + ".jpg";

        // Get the directory for storing images
        File directory = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                context.getString(R.string.app_name)
        );

        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create the file in the specified directory
        File file = new File(directory, fileName);
        try {
            // Save the bitmap to the file
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();

            scanFile(context, Uri.fromFile(file));

            Toast.makeText(context, "Image saved successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }
    // Scan the file to show it in the gallery
    private static void scanFile(Context context, Uri imageUri) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);
    }
}
