package com.shubzz.wireparent.etc;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.shubzz.wireparent.R;
import com.shubzz.wireparent.SOSNotification;

public class MySOSReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";


    @Override
    public void onReceive(Context context, Intent intent) {
//        String data = intent.getStringExtra("data");
//        Toast.makeText(context, "Data received:  " + data, Toast.LENGTH_LONG).show();

        // Prepare intent which is triggered if the
        // notification is selected
//        Intent intent = new Intent(this, NotificationReceiverActivity.class);
//        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Log.d("Working: ", "yes");
        Intent notificationIntent = new Intent(context, SOSNotification.class);
        notificationIntent.putExtra(SOSNotification.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(SOSNotification.NOTIFICATION, getNotification(context));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

    }

    private Notification getNotification(Context c) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(c, default_notification_channel_id);
        builder.setContentTitle("SOS Notification");
        builder.setContentText("Your Child Need Help");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }
}
