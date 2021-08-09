package com.example.clock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import static com.example.clock.App.CHANNEL_TWO_ID;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Klik na notifikacijata da nosi kon aktivnosta Alarm
        Intent notificationIntent = new Intent(context, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_TWO_ID)
                .setContentTitle("Alarm")
                .setContentText("This is your alarm!")
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentIntent(pendingIntent)
                .setSilent(true)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(2, notification);

        //Dzvonenje na Alarmot
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Alarm.alarm = RingtoneManager.getRingtone(context, sound);
        Alarm.alarm.play();
    }
}
