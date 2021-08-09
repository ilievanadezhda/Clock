package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class WorldClock extends AppCompatActivity {
    private EditText mEditText;
    private ImageButton mButton;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_clock);
        getSupportActionBar().hide();

        mEditText = (EditText) findViewById(R.id.place_name);
        mButton = (ImageButton) findViewById(R.id.search_button);
        mTextView = (TextView) findViewById(R.id.results);
    }

    public void getWorldClock (View view) {
        // Get the search string from the input field.
        String queryString = mEditText.getText().toString();
        if(queryString != null && queryString != "" && queryString.length()!=0) {
            // Hide the keyboard when the button is pushed.
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            // Check the status of the network connection.
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connMgr != null) {
                networkInfo = connMgr.getActiveNetworkInfo();
            }
            // If the network is available, connected, and the search field
            // is not empty, start a GetWorldClock AsyncTask.
            if (networkInfo != null && networkInfo.isConnected()) {
                new GetWorldClock(mTextView).execute(queryString);
                mTextView.setText("Loading..");
            }
            // Otherwise update the TextView to tell the user there is no connection
            else {
                mTextView.setText("No network connection. Please enable WiFi or Mobile Data and try again!");
            }
        } else {
            Toast.makeText(this, "Enter Area/Location/Region", Toast.LENGTH_SHORT).show();
        }
    }
}