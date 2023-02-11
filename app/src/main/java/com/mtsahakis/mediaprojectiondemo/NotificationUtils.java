package com.mtsahakis.mediaprojectiondemo;


import static android.app.Notification.EXTRA_NOTIFICATION_ID;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.util.Pair;


public class NotificationUtils {

    public static final int NOTIFICATION_ID = 1337;
    public static final int NOTIFICATION_ID_START = 1338;
    private static final String NOTIFICATION_CHANNEL_ID = "com.mtsahakis.mediaprojectiondemo.app";
    private static final String NOTIFICATION_CHANNEL_NAME = "com.mtsahakis.mediaprojectiondemo.app";

    public static Pair<Integer, Notification> getNotification(@NonNull Context context) {
        createNotificationChannel(context);
        Notification notification = createNotification(context,context.getString(R.string.recording));
        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
        return new Pair<>(NOTIFICATION_ID, notification);
    }
    public static Pair<Integer, Notification> getNotificationStart(@NonNull Context context) {
        createNotificationChannel(context);
        Notification notification = createNotification(context,"Tap to start Recording");
        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID_START, notification);
        return new Pair<>(NOTIFICATION_ID_START, notification);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }

    private static Notification createNotification(@NonNull Context context,String status) {
        Intent snoozeIntent = new Intent(context, SceenShotBroadCastReciver.class);

        snoozeIntent.setAction("ScreenShot");
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_camera);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(status);
        builder.setOngoing(true);

        builder.addAction(android.R.drawable.stat_sys_upload_done, "Make ScreenShot",
                snoozePendingIntent);
        builder.setCategory(Notification.CATEGORY_SERVICE);
        builder.setPriority(Notification.PRIORITY_LOW);
        builder.setShowWhen(true);
        return builder.build();
    }

}
