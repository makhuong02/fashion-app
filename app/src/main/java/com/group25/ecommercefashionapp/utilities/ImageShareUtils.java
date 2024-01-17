package com.group25.ecommercefashionapp.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageShareUtils {

    public static void shareImage(Context context, Bitmap bitmap, String title) {
        // Save the bitmap to a temporary file
        File imagePath = saveBitmapToCache(context, bitmap);

        if (imagePath != null) {
            // Create an intent to share the image
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");

            Uri contentUri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".fileprovider",
                    imagePath);

            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);

            // Grant read permission to other apps
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Start the chooser to select a sharing app
            Intent chooserIntent = Intent.createChooser(shareIntent, "Share Image");
            context.startActivity(chooserIntent);
        } else {
            // Handle the case when saving the bitmap fails
            Toast.makeText(context, "Failed to share image", Toast.LENGTH_SHORT).show();
        }
    }

    private static File saveBitmapToCache(Context context, Bitmap bitmap) {
        try {
            // Save the bitmap to a temporary file
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            File imagePath = new File(cachePath, "shared_image.jpg");

            FileOutputStream stream = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();

            return imagePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
