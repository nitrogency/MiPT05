package com.example.mipt_05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvTemps;
    private ArrayAdapter listAdapter;
    private String urlString;
    private TextView tvStatus;
    private EditText edSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity | onCreate: ", "Started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlString = getResources().getString(R.string.url_addr);
        lvTemps = findViewById(R.id.lvTemps);
        tvStatus = findViewById(R.id.tvStatus);
        edSearch = findViewById(R.id.edSearch);

        this.listAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        this.lvTemps.setAdapter(this.listAdapter);
        startService();
    }

    public void startService(){
        JsonParser parser = new JsonParser(edSearch);
        Log.i("MainActivity | onCheckButtonClick: ", "Started");
        tvStatus.setText(getResources().getString(R.string.tv_status));

        Runnable getDataAndDisplayRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream content = DataLoader.getUrl(urlString);
                    List<String> results = parser.getForecast(content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateListView(results);
                        }
                    });
                } catch (Exception e){
                    Log.e("MainActivity | onCheckButtonClick: ", e.toString());
                }
            }
        };
        Thread thread = new Thread(getDataAndDisplayRunnable);
        thread.start();
    }
    public void onCheckButtonClick(View view){
        Log.i("MainActivity | onCheckButtonClick: ", "Started");
        startService();
    }

    @Override
    protected void onDestroy(){
        Log.i("MainActivity | onDestroy: ", "Started");
        super.onDestroy();
        stopService(new Intent(this, DataLoader.class));
    }

    private void updateListView(List<String> results){
        Log.i("MainActivity | updateListView: ", "Started");
        listAdapter.clear();
        listAdapter.addAll(results);
        listAdapter.notifyDataSetChanged();
    }

}