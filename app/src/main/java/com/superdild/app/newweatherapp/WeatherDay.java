package com.superdild.app.newweatherapp;

/**
 * Created by gino on 03/03/18.
 */

public class WeatherDay {
    private double temp;
    private double temp_min;
    private double temp_max, pressure, humidity, wind;
    private String icon, data, description, wind_dir,sunrise,sunset;


    //  public WeatherDay(double temp, double temp_min, double temp_max , String icon, String data) {
    //      this.temp = temp;
    //      this.temp_min = temp_min;
    //       this.temp_max = temp_max;
    //       this.icon = icon;
    //       this.data = data;
    //       count++ ;
    //       Log.v("weatherDay",  "Constructor: " + getCount());
    //  }
    public WeatherDay(double temp, double temp_max, double temp_min, double pressure, double humidity, double wind, String wind_dir) {
        this.temp = temp;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
        this.wind_dir = wind_dir;

    }

    public WeatherDay() {

    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public void setCount(int count) {
        count++;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public void setWind_dir(String wind_dir) {
        this.wind_dir = wind_dir;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
