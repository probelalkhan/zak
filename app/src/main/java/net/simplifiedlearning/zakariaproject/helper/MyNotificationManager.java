package net.simplifiedlearning.zakariaproject.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import net.simplifiedlearning.zakariaproject.R;
import net.simplifiedlearning.zakariaproject.activities.GraphActivity;

/**
 * Created by Belal on 10/1/2017.
 */

public class MyNotificationManager {

    Context mCtx;

    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    private boolean isNotificationVisible() {
        Intent notificationIntent = new Intent(mCtx, GraphActivity.class);
        PendingIntent test = PendingIntent.getActivity(mCtx, 0, notificationIntent, PendingIntent.FLAG_NO_CREATE);
        return test != null;
    }

    public void addNotification() {
        if (isNotificationVisible())
            return;
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(mCtx)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setSound(alarmSound)
                        .setContentTitle("My notification") //change it to what you want to be displayed
                        .setContentText("Hello World!");

        Intent resultIntent = new Intent(mCtx, GraphActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyMgr.notify(1, mBuilder.build());
    }
}
