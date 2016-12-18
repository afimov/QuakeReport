package com.example.anton.quakereport;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by anton on 18.12.16.
 */

public class EarthquakeLoader extends android.content.AsyncTaskLoader<List<Earthquake>> {

    String topRequest = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    public EarthquakeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public  List<Earthquake> loadInBackground() {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        Earthquake[] earthquakes;

        try {
            URL url = new URL(topRequest);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream == null){
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String currentLine;

            while ((currentLine = reader.readLine()) != null){
                builder.append(currentLine);
            }

            if (builder.length() == 0){
                return null;
            }

            String outputJson = builder.toString();

            try {
                JSONObject jsonEarth = new JSONObject(outputJson);
                JSONArray features = jsonEarth.getJSONArray("features");

                earthquakes = new Earthquake[features.length()];

                for (int i=0; i < features.length(); i++) {
                    JSONObject feature = features.getJSONObject(i);

                    JSONObject featureProperties = feature.getJSONObject("properties");
                    Double magnitude = featureProperties.getDouble("mag");
                    String location = featureProperties.getString("place");
                    Long date = featureProperties.getLong("time");
                    String feature_url = featureProperties.getString("url");
                    earthquakes[i] = new Earthquake(magnitude,location, Utils.formatDate(date),feature_url);
                }

                return Arrays.asList(earthquakes);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
