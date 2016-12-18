package com.example.anton.quakereport;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anton on 20.11.16.
 */

public class Utils {
    public static String formatDate(long timeInMilliseconds){
        Date dateObject = new Date(timeInMilliseconds);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);
        return dateToDisplay;
    }
}
