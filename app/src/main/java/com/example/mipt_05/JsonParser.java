package com.example.mipt_05;

import android.util.Log;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {


    private EditText edSearch;

    public JsonParser(EditText edSearch){
        this.edSearch = edSearch;
    }

    private boolean containsSearch(String input){
        Log.i("JsonParser | containsSearch: ", "Started");
        String searchString = edSearch.getText().toString().toLowerCase();
        if (searchString.isEmpty()){
            Log.i("JsonParser | containsSearch: ", "searchString is null");
            return true;
        }
        else return input.toLowerCase().contains(searchString);
    }
    public List<String> getForecast(InputStream stream) throws IOException {
        Log.i("JsonParser | getForecast: ", "Started");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";

        while (line != null) {
            line = bufferedReader.readLine();
            stringBuilder.append(line);
        }

        List<String> result = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(stringBuilder.toString());
            JSONObject placeNode = data.getJSONObject("place");

            String location = placeNode.getString("administrativeDivision");
            String coordinates = placeNode.getString("coordinates");

            if (containsSearch(location) || containsSearch(coordinates)){
                Log.i("JsonParser | getForecast: ", "Search found");
                String formattedInfo = String.format("Location: %s, \nCoordinates: %s", location, coordinates);
                result.add(formattedInfo);
            }

            JSONArray timestamps = data.getJSONArray("forecastTimestamps");
            for (int i = 0; i < timestamps.length(); i++) {
                JSONObject item = timestamps.getJSONObject(i);

                String forecastTime = item.getString("forecastTimeUtc");
                double airTemp = item.getDouble("airTemperature");
                double feelsLikeTemp = item.getDouble("feelsLikeTemperature");

                if (containsSearch(forecastTime) || containsSearch(String.valueOf(airTemp)) || containsSearch(String.valueOf(feelsLikeTemp))){
                    Log.i("JsonParser | getForecast: ", "Search found");
                    String formattedForecast = String.format("Time (UTC): %s \nTemperature: %sC°\nFelt temperature: %sC°\n", forecastTime, airTemp, feelsLikeTemp);
                    result.add(formattedForecast);
                }
            }


        } catch (JSONException e) {
            Log.e("JsonParser | getForecast: ", e.toString());
        }
        return result;
    }
}
