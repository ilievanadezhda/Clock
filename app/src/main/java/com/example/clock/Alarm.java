package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class Alarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    private Button cancelButton;
    private TextView alarmTextView;
    public static Ringtone alarm;
    public static final String SHARED_PREFERENCES = "com.example.clock";
    public static final String KEY = "ALARM_STATUS";
    public static final String ALARM_SET = "Alarm set for ";
    public static final String ALARM_NOT_SET = "Not set!";
    public static final String ALARM_CANCELLED = "Alarm cancelled!";
    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getSupportActionBar().hide();

        cancelButton = (Button) findViewById(R.id.cancel_button);
        alarmTextView = (TextView) findViewById(R.id.alarm_time);

        //Dobij ja od SharedPreferences i postavi ja vrednosta na poleto alarmTextView
        preferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        if(!preferences.contains(KEY)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY, ALARM_NOT_SET);
            editor.commit();
            alarmTextView.setText(ALARM_NOT_SET);
        } else {
            alarmTextView.setText(preferences.getString(KEY, ""));
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
    }

    public void setAlarm(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        startAlarm(c);

        //String format na vremeto
        String text = updateTimeText(c);

        //Smeni go tekstot vo TextView
        alarmTextView.setText(ALARM_SET +"\n" + text);

        //Napravi Toast
        Toast.makeText(this, ALARM_SET + text, Toast.LENGTH_SHORT).show();

        //Zachuvaj go tekstot vo SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY, ALARM_SET + "\n" + text);
        editor.commit();
    }

    private String updateTimeText(Calendar c) {
        String timeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        return  timeText;
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        //Ako vremeto e pominato, postavi go alarmot za istoto vreme no sledniot den
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        //Otkazhi go alarmot
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);

        //Smeni go tekstot vo TextView
        alarmTextView.setText(ALARM_CANCELLED);

        //Smeni vo SharedPreferences za da na sledno vkluchuvanje pishuva Not set!
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY, ALARM_NOT_SET);
        editor.commit();

        //Prekini go dzvonenjeto vo sluchaj da dzvoni
        if(alarm != null) {
            alarm.stop();
        }
    }
}