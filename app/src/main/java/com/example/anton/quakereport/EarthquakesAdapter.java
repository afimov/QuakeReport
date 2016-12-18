package com.example.anton.quakereport;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anton on 19.11.16.
 */

public class EarthquakesAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOG_TAG = EarthquakesAdapter.class.getName();

    public EarthquakesAdapter(Activity context, ArrayList<Earthquake> earthquakes){
        super(context,0,earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Earthquake currentEarthquake = getItem(position);

        TextView magTextView = (TextView) listItemView.findViewById(R.id.magTextView);
        magTextView.setText("" + currentEarthquake.getMagnitude());

        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();
        magnitudeCircle.setColor(getMagnitudeColor(currentEarthquake.getMagnitude()));

        String originalLocation = currentEarthquake.getLocation();

        String primaryLocation;
        String locationOffset;
        String LOCATION_SEPARATOR = " of ";

        if (originalLocation.contains(LOCATION_SEPARATOR)){
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            locationOffset = "Near the";
            primaryLocation = originalLocation;
        }

        TextView offsetTextView = (TextView) listItemView.findViewById(R.id.offset);
        offsetTextView.setText(locationOffset);

        TextView primaryTextView = (TextView) listItemView.findViewById(R.id.primary);
        primaryTextView.setText(primaryLocation);


        TextView dateTextView = (TextView) listItemView.findViewById(R.id.dateTextView);
        dateTextView.setText(currentEarthquake.getDate());


        return listItemView;
    }

    private int getMagnitudeColor(double magnitude){
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;

        }

        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
