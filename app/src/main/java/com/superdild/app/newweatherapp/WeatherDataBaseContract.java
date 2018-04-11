package com.superdild.app.newweatherapp;

import android.provider.BaseColumns;

import java.sql.SQLException;

/**
 * Created by gino on 22/03/18.
 */

public final class WeatherDataBaseContract {

    private WeatherDataBaseContract() {
    }

    public final static class WeatherDataBaseEntry implements BaseColumns {

        public static final String TABLE_NAME = "WeatherDB";
        public static final String COLUMN_TEMP = "temp";
        public static final String COLUMN_TEMP_MAX = "temp_max";
        public static final String COLUMN_TEMP_MIN = "temp_min";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ICON_ID = "iconID";
        public static final String COLUMN_WIND_SPEED = "windSpeed";
        public static final String COLUMN_WIND_DIRECTION = "windDirection";
        public static final String COLUMN_DATE_TIME = "dateTime";

    }

}
