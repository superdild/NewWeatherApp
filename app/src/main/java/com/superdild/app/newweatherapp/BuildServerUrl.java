package com.superdild.app.newweatherapp;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gino on 03/03/18.
 */

public class BuildServerUrl {
    static private final String KEY = "";
    static private final String DEFAULT_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    static private final String DEFAULT_DAYURL = "http://api.openweathermap.org/data/2.5/weather?";
    static private final String QUERY_PARAM = "q";
    static private final String UNITS = "units";
    static private final int NUM_DAYS = 16;
    static private final String COUNT = "cnt";
    static private final String PID = "APPID";
    static private final String ICON_URL = "http://openweathermap.org/img/w/";
    static private final String LANG = "lang";

    static URL BuildUrl(String city, String unit, String local) {

        //if (city==null)city="Ravenna";
        Uri buildUri = Uri.parse(DEFAULT_URL).buildUpon().appendQueryParameter(QUERY_PARAM, city).appendQueryParameter(LANG, local)
                .appendQueryParameter(UNITS, unit).appendQueryParameter(COUNT, String.valueOf(NUM_DAYS))
                .appendQueryParameter(PID, KEY).build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            Log.d("BuildUrl catch: ", "MalformedUrl: " + e);
        }
        return url;
    }

    static URL BuilDaylyUrl(String city, String unit, String local){
       // if (city==null)city="Ravenna";
        Uri buildDaylyUri = Uri.parse(DEFAULT_DAYURL).buildUpon().appendQueryParameter(QUERY_PARAM,city).appendQueryParameter(LANG,local)
                .appendQueryParameter(UNITS,unit).appendQueryParameter(PID, KEY).build();
        URL url = null;
        try {
            url = new URL(buildDaylyUri.toString());
        } catch (MalformedURLException e) {
            Log.d("BuildUrl catch: ", "MalformedUrl: " + e);
        }

        return url;
    }

}
