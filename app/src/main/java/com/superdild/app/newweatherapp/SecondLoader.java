package com.superdild.app.newweatherapp;

import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gino on 19/03/18.
 */

public class SecondLoader extends AsyncTaskLoader<String> {

    OkHttpClient client = new OkHttpClient();
    Context context;
    String url, temp;
    SQLiteDatabase weatherDB;

    public SecondLoader(Context context, String url){//, SQLiteDatabase weatherDB) {
        super(context);
        this.context=context;
        this.url = url;
        WeatherDataBaseHelper weatherDBHelper = WeatherDataBaseHelper.getInstance(context);
        weatherDB = weatherDBHelper.getWritableDatabase();
        Log.i("SecondLoader ", "SecondURL: " + url.toString());
    }

    @Override
    protected void onStartLoading() {
        if (temp != null) {
            deliverResult(temp);
            Log.v("SecondLoader ", "deliveryResult");
        } else {
            forceLoad();
            Log.w("SecondLoader ", " forceLoad()");
        }
    }

    @Override
    public String loadInBackground() {

    Request request = new Request.Builder().url(url).build();
    try (
            Response response = client.newCall(request).execute()) {

        JSONObject jsonData = new JSONObject(response.body().string());
        JSONArray jsonArray = jsonData.getJSONArray("list");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONObject jsonObject1 = jsonObject.getJSONObject("temp");
        double temp_min = Math.round(jsonObject1.getDouble("min"));
        double temp_max = Math.round(jsonObject1.getDouble("max"));
       // temp = "min: " + (int) temp_min + " max: " + (int) temp_max;
        temp =  (int) temp_min + " | " + (int) temp_max;

        List<ContentValues> list = new ArrayList<ContentValues>();
        int count = jsonArray.length();

        for (int i = 0; i < count; i++) {
            ContentValues cv = new ContentValues();
            jsonObject = jsonArray.getJSONObject(i);
            jsonObject1 = jsonObject.getJSONObject("temp");
            double array_temp_min = Math.round(jsonObject1.getDouble("min"));
            double array_temp_max = Math.round(jsonObject1.getDouble("max"));
            double pressure = Math.round(jsonObject.getDouble("pressure"));
            double humidity = Math.round(jsonObject.getDouble("humidity"));
            double speed = Math.round(jsonObject.getDouble("speed"));
            double direction = Math.round(jsonObject.getDouble("deg"));
            long dateTime = jsonObject.getLong("dt");
            JSONArray jsonArray1 = jsonObject.getJSONArray("weather");
            jsonObject = jsonArray1.getJSONObject(0);
            String description = jsonObject.getString("description");
            String iconID = jsonObject.getString("icon");

            cv.put(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_TEMP_MAX, array_temp_max);
            cv.put(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_TEMP_MIN, array_temp_min);
            cv.put(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_PRESSURE, pressure);
            cv.put(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_HUMIDITY, humidity);
            cv.put(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_WIND_SPEED, speed);
            cv.put(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_WIND_DIRECTION, direction);
            cv.put(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_DESCRIPTION, description);
            cv.put(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_ICON_ID, iconID);
            cv.put(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_DATE_TIME, Utility.DateFormatExtended(dateTime));

            list.add(cv);
        }
        try {
            weatherDB.beginTransaction();
            weatherDB.delete(WeatherDataBaseContract.WeatherDataBaseEntry.TABLE_NAME, null, null);
            for (ContentValues c : list) {
                weatherDB.insert(WeatherDataBaseContract.WeatherDataBaseEntry.TABLE_NAME, null, c);
            }
            weatherDB.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            weatherDB.endTransaction();
        }
        //    }

    } catch (MalformedURLException e) {
        Log.v("Exception ", "MalformedUrlException");
        e.printStackTrace();
    } catch (IOException e) {
        Log.v("Exception ", "IOException");
        e.printStackTrace();
    } catch (JSONException e) {
        Log.v("Exception ", "JSONException");
        e.printStackTrace();
    }

        return temp;
    }

    @Override
    public void deliverResult(String data) {
        temp = data;
        super.deliverResult(data);
    }
}
