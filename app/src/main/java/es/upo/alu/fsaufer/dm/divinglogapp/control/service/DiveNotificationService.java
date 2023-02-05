package es.upo.alu.fsaufer.dm.divinglogapp.control.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

/**
 * Clase para gestionar las notificaciones
 */
public class DiveNotificationService {

    private final static int DEMO_DATA_LOADED_NOTIFICATION_ID = 1;

    public static void notify(Context context, String contentTitle, String contentText) {
        createNotificationChannel(context);

        NotificationManagerCompat.from(context)
                .notify(DEMO_DATA_LOADED_NOTIFICATION_ID,
                        buildNotification(context, contentTitle, contentText));
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            if (notificationManager.getNotificationChannel(context.getString(R.string.notification_channel_name)) == null) {
                NotificationChannel channel = new NotificationChannel(
                        Constant.DIVING_LOG_CHANNEL_ID,
                        context.getString(R.string.notification_channel_name),
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(context.getString(R.string.notification_channel_description));
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private static Notification buildNotification(Context context, String contentTitle, String contentText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.DIVING_LOG_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }
}
