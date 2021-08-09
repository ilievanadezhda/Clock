package com.example.clock;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ONE_ID = "TimerServiceChannel";
    public static final String CHANNEL_TWO_ID = "AlarmChannel";
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Timer
            NotificationChannel notificationChannelOne = new NotificationChannel(CHANNEL_ONE_ID, "Timer Service Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager managerOne = getSystemService(NotificationManager.class);
            managerOne.createNotificationChannel(notificationChannelOne);
            //Alarm
            NotificationChannel notificationChannelTwo = new NotificationChannel(CHANNEL_TWO_ID, "Alarm Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager managerTwo = getSystemService(NotificationManager.class);
            managerTwo.createNotificationChannel(notificationChannelTwo);
        }
    }
}
