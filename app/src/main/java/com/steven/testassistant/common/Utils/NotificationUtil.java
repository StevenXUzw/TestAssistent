package com.steven.testassistant.common.Utils;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

public class NotificationUtil extends ContextWrapper {

    private NotificationManager manager;
    public static final String channelId = "message";
    public static final String channelName = "应用消息";

    public NotificationUtil(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content) {
        return new Notification.Builder(getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }

    public NotificationCompat.Builder getNotification_25(String title, String content) {
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }

    public void sendNotification(String title, String content, NotificationCompat.Action action) {
        if (Build.VERSION.SDK_INT >= 26) {
            return;
        }
        NotificationCompat.Builder notificationBuilder = getNotification_25(title, content);
        if (action != null) {
            notificationBuilder.addAction(action);
        }
        Notification notification = notificationBuilder.build();
        getManager().notify(1, notification);
    }

    public void sendNotification(String title, String content, Notification.Action... actions) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification.Builder notificationBuilder = getChannelNotification(title, content);
            for(Notification.Action action : actions){
                notificationBuilder.addAction(action);
            }
            Notification notification = notificationBuilder.build();
            getManager().notify(1, notification);
        }
    }
}