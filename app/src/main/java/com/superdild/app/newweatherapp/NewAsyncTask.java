package com.superdild.app.newweatherapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gino on 03/03/18.
 */

public class NewAsyncTask extends AsyncTaskLoader<WeatherDay> {

    OkHttpClient client = new OkHttpClient();
    WeatherDay weatherDay;
    String url, unit;


    public NewAsyncTask(Context context, String url) {
        super(context);
        this.url = url;

        Log.v("URL: ", "" + url);

    }

    @Override
    protected void onStartLoading() {
        if (weatherDay != null) {
            deliverResult(weatherDay);
        } else {
            forceLoad();
            Log.i("NewAsyncTask", "forceLoad");
        }
    }

    @Override
    public WeatherDay loadInBackground() {

        Request request = new Request.Builder().url(url).build();
        try (
                Response response = client.newCall(request).execute()) {

            JSONObject jsonData = new JSONObject(response.body().string());
            long data = jsonData.getLong("dt");
            JSONObject jsonObject = jsonData.getJSONObject("main");
            double temp = jsonObject.getDouble("temp");
            double temp_min = jsonObject.getDouble("temp_min");
            double temp_max = jsonObject.getDouble("temp_max");
            double pressure = jsonObject.getDouble("pressure");
            double umidity = jsonObject.getDouble("humidity");
            jsonObject = jsonData.getJSONObject("sys");
            long sunrise = jsonObject.getLong("sunrise");
            long sunset = jsonObject.getLong("sunset");
            JSONArray jsonArray = jsonData.getJSONArray("weather");
            jsonObject = jsonArray.getJSONObject(0);
            String description = jsonObject.getString("description");
            String icon = jsonObject.getString("icon");

            weatherDay = new WeatherDay();
            weatherDay.setTemp(temp);
            weatherDay.setTemp_min(temp_min);
            weatherDay.setTemp_max(temp_max);
            weatherDay.setHumidity(umidity);
            weatherDay.setPressure(pressure);
            weatherDay.setDescription(description);
            weatherDay.setData(Utility.DateFormat(data));
            weatherDay.setIcon(icon);
            weatherDay.setSunrise(Utility.DateFormatHHmm(sunrise));
            weatherDay.setSunset(Utility.DateFormatHHmm(sunset));
            JSONObject jsonWind = jsonData.getJSONObject("wind");
            double wind_speed = jsonWind.getDouble("speed");
            weatherDay.setWind((int) (wind_speed * 3.6));
            double dir = jsonWind.getDouble("deg");
            int i = (int) Math.round((dir + 11.25) / 22.5);
            weatherDay.setWind_dir(windDir(i));

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

        return weatherDay;

    }

    @Override
    public void deliverResult(WeatherDay data) {
        weatherDay = data;
        super.deliverResult(data);
    }

    public String windDir(int dir) {
        switch (dir) {
            case 1:
                return "N";
            case 2:
                return "N-NE";
            case 3:
                return "NE";
            case 4:
                return "E-NE";
            case 5:
                return "E";
            case 6:
                return "E-SE";
            case 7:
                return "SE";
            case 8:
                return "S-SE";
            case 9:
                return "S";
            case 10:
                return "S-SO";
            case 11:
                return "SO";
            case 12:
                return "O-SO";
            case 13:
                return "O";
            case 14:
                return "O-NO";
            case 15:
                return "NO";
            case 16:
                return "N-NO";
            default:
                return "N";

        }
    }


}
