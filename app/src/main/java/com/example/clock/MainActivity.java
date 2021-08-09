package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
    }
    public void openAlarm (View view) {
        Intent intent = new Intent(this, Alarm.class);
        startActivity(intent);
    }
    public void openTimer (View view) {
        Intent intent = new Intent(this, Timer.class);
        startActivity(intent);
    }
    public void openWorldClock (View view) {
        Intent intent = new Intent(this, WorldClock.class);
        startActivity(intent);
    }
}