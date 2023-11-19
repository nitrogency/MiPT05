package com.example.mipt_05;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataLoader extends Service {
    static InputStream getUrl(String urlString) throws IOException {
        Log.i("DataLoader | getUrl: ", "Started");
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(10000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        try{
            conn.connect();
            Log.i("DataLoader | getUrl: ", "Connection established to " + url);
        } catch (Exception e) {
            Log.e("DataLoader | getUrl: ", e.toString());
        }

        return conn.getInputStream();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
