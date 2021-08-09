package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;

import java.util.Calendar;

public class Timer extends AppCompatActivity {
    private TextView timerLength;
    private static int millis = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getSupportActionBar().hide();

        timerLength = (TextView) findViewById(R.id.timer_length);

        //Mozhnost servisot da go update TextView vo aktivnosta
        //Tajmerot da odbrojuva i vo notifikacijata i vo aplikacijata
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String time = intent.getStringExtra("time");
                        timerLength.setText(time);
                    }
                }, new IntentFilter(TimerService.ACTION_TIMER_BROADCAST)
        );
    }

    public void startTimerService(View view) {
        //Da ne mozhe da se stavi Timer so dolzhina 0
        if(millis != 0) {
            Intent serviceIntent = new Intent (this, TimerService.class);
            serviceIntent.setAction("Start");
            serviceIntent.putExtra("length", millis);
            ContextCompat.startForegroundService(this, serviceIntent);
        } else {
            Toast.makeText(this, "Please select timer duration!", Toast.LENGTH_SHORT).show();
        }

    }

    public void stopTimerService(View view) {
        Intent serviceIntent = new Intent(this, TimerService.class);
        stopService(serviceIntent);
        millis = 0;
        timerLength.setText("00:00:00");
    }

    public void showPicker(View view) {
        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {
            @SuppressLint("DefaultLocale")
            public void onTimeSet(com.ikovac.timepickerwithseconds.TimePicker view, int hourOfDay, int minute, int seconds) {
                timerLength.setText(String.format("%02d", hourOfDay)+ ":" + String.format("%02d", minute) + ":" + String.format("%02d", seconds));
                millis = calculateMillis(hourOfDay, minute, seconds);
            }
        }, 0, 0, 0, true);
        mTimePicker.show();
    }

    public int calculateMillis (int hours, int minutes, int seconds) {
        return (hours*3600 + minutes*60 + seconds)*1000;
    }
}