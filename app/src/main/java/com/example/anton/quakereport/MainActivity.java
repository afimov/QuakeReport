package com.example.anton.quakereport;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();
    ArrayList<Earthquake> earthquakeList;
    private EarthquakesAdapter adapter;
    String fullRequest = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.geojson";
    String topRequest = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* ArrayList<Earthquake> earthquakes = new ArrayList<>();
        earthquakes.add(new Earthquake("7.2","San Francisco","Feb 2,2016"));
        earthquakes.add(new Earthquake("6.1","London","July 20,2015"));
        earthquakes.add(new Earthquake("3.9","Tokyo","Nov 10,2014"));
        earthquakes.add(new Earthquake("5.4","Mexico City","May 3,2014"));
        earthquakes.add(new Earthquake("2.8","Moscow","Jan 31,2013"));
        earthquakes.add(new Earthquake("4.9","Rio de Janeiro","Aug 19,2012"));
        earthquakes.add(new Earthquake("1.6","Paris","Oct 30,2011"));*/

        Earthquake[] earthquakes1 = {
                new Earthquake(10,"London","Yesterday"),
                new Earthquake(20,"paris","Today")
        };

        earthquakeList = new ArrayList<>(Arrays.asList(earthquakes1));

        ListView earthquakeListView = (ListView)findViewById(R.id.list);
        adapter = new EarthquakesAdapter(this, earthquakeList);

        earthquakeListView.setAdapter(adapter);

        FetchEarthquakesTask task = new FetchEarthquakesTask();
        task.execute();


    }

    public void updateWeather(View view){
        FetchEarthquakesTask task = new FetchEarthquakesTask();
        task.execute();
    }

    private class FetchEarthquakesTask extends AsyncTask<Void, Void, Earthquake[]> {

        @Override
        protected Earthquake[] doInBackground(Void... params) {
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
                        Log.v("InnerCycle",location);
                        earthquakes[i] = new Earthquake(magnitude,location, Utils.formatDate(date));
                    }

                    return earthquakes;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Earthquake[] earthquakes) {
            //super.onPostExecute(earthquakes);
            if (earthquakes != null){
                adapter.clear();
                    for (Earthquake earthquake : earthquakes) {
                    adapter.add(earthquake);
                    }
            }
        }
    }
}
