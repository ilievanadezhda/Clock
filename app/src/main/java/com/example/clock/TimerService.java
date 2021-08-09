package com.example.clock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Locale;

import static com.example.clock.App.CHANNEL_ONE_ID;

public class TimerService extends Service {
    public static final String ACTION_TIMER_BROADCAST = TimerService.class.getName() + "TimerBroadcast";
    private long START_TIME_IN_MILLIS = 0;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private Ringtone ringtone;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Proverka dali e pritisnato Snooze na notifikacijata
        if(intent.getAction() != null && intent.getAction().equals("Snooze")) {
            if(ringtone != null) {
                ringtone.stop();
            }
            stopSelf();
        }

        //Dolzhina na Timer
        int millis = intent.getIntExtra("length", 0);
        START_TIME_IN_MILLIS = millis;
        mTimeLeftInMillis = START_TIME_IN_MILLIS;

        Intent notificationIntent = new Intent(this, Timer.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ONE_ID)
                .setContentTitle("Timer")
                .setSmallIcon(R.drawable.ic_timer)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        startTimer();
        return START_NOT_STICKY;
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownNotification();
                sendBroadcastMessage(updateCountDownText());
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateFinishedNotification();
                playSound();
            }
        }.start();
        mTimerRunning = true;
    }

    private void updateCountDownNotification() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) - hours*3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        Intent notificationIntent = new Intent(this, Timer.class);
        notificationIntent.setAction("Ongoing");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ONE_ID)
                .setContentTitle("Timer")
                .setContentText(timeLeftFormatted)
                .setSmallIcon(R.drawable.ic_timer)
                .setContentIntent(pendingIntent)
                .setSilent(true)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    private void updateFinishedNotification () {
        Intent snoozeIntent = new Intent (this, TimerService.class);
        snoozeIntent.setAction("Snooze");
        PendingIntent pendingSnoozeIntent = PendingIntent.getService(this, 0, snoozeIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ONE_ID)
                .setContentTitle("Timer")
                .setContentText("Time is up!")
                .setSmallIcon(R.drawable.ic_timer)
                .setSilent(true)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_snooze, "Snooze", pendingSnoozeIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    private void playSound() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alert);
        ringtone.play();
    }

    private String updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) - hours*3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        return timeLeftFormatted;
    }

    //Za update na TextView vo aktivnosta Timer
    private void sendBroadcastMessage(String time) {
        if (time != null) {
            Intent intent = new Intent(ACTION_TIMER_BROADCAST);
            intent.putExtra("time", time);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
    @Override
    public void onDestroy() {
        mCountDownTimer.cancel();
        if(ringtone != null) {
            ringtone.stop();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
