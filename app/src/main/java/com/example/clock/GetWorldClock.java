package com.example.clock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.nio.channels.AsynchronousChannelGroup;

public class GetWorldClock extends AsyncTask<String, Void, String> {
    private WeakReference<TextView> mTextView;

    GetWorldClock(TextView textView) {
        this.mTextView = new WeakReference<>(textView);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return NetworkUtils.getWorldClockInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject data = new JSONObject(s);
            String datetime = (String) data.get("datetime");
            if (datetime != null) {
                mTextView.get().setText(datetime);
            } else {
                mTextView.get().setText("No Results Found!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mTextView.get().setText("No Results Found!");
        }
    }
}
