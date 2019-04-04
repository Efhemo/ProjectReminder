package com.efhems.newinsideproject.ui.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlertReceiver extends BroadcastReceiver {


    public static final String NOTIFICATION_BUILT = "notification built";
    public static final String NOTIFICATION_ID = "Notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {

        /*NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChan notification = intent.getParcelableExtra(NOTIFICATION);
        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);

        notificationManager.notify(notificationId, notification);*/

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION_BUILT);
        int notifyId = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(notifyId, notification);

    }
}
