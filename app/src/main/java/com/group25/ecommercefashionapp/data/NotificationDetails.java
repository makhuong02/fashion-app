package com.group25.ecommercefashionapp.data;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.group25.ecommercefashionapp.MyApp;
import com.group25.ecommercefashionapp.R;

import java.util.Date;

public class NotificationDetails {
    public Context context;
    public String title;
    public String description;

    public NotificationDetails(Context context, String title, String description) {
        this.context = context;
        this.title = title;
        this.description = description;
    }

    public void showNotification(NotificationManager notificationManager) {
        Notification notification = new NotificationCompat.Builder(context, MyApp.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_navbar_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        if (notificationManager != null) {
            notificationManager.notify(getNotificiationID(), notification);
        }
    }

    private int getNotificiationID() {
        return (int) new Date().getTime();
    }
}
