package com.superdild.app.newweatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gino on 22/03/18.
 */

public class WeatherDataBaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "Weather.db";
    private final static int DATABASE_VERSION = 1;
    private static WeatherDataBaseHelper weatherDataBaseInstance;

    public WeatherDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized WeatherDataBaseHelper getInstance(Context context){

        if(weatherDataBaseInstance==null)
            weatherDataBaseInstance = new WeatherDataBaseHelper(context.getApplicationContext());

   return weatherDataBaseInstance; }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherDataBaseContract.WeatherDataBaseEntry.TABLE_NAME +
                " (" + WeatherDataBaseContract.WeatherDataBaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_TEMP_MAX + " DOUBLE NOT NULL, " +
                WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_TEMP_MIN + " DOUBLE NOT NULL, " +
                WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_PRESSURE + " DOUBLE NOT NULL, " +
                WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_HUMIDITY + " DOUBLE NOT NULL, " +
                WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_WIND_SPEED + " DOUBLE, " +
                WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_WIND_DIRECTION + " DOUBLE, " +
                WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_DESCRIPTION + " STRING NOT NULL, " +
                WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_ICON_ID + " STRING NOT NULL, " +
                WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_DATE_TIME + " STRING NOT NULL "+
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherDataBaseContract.WeatherDataBaseEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
