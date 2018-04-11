package com.superdild.app.newweatherapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.superdild.app.newweatherapp.databinding.ActivityMainBinding;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<WeatherDay>,
        SharedPreferences.OnSharedPreferenceChangeListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_GPS = 0;
    private final static int LOADER_ID = 22;
    private final static int LOADER_SECOND = 44;
    private final static String BUNDLE_ID = "bundle";
    private final static String BUNDLE_SECOND = "second_bundle";
    private final static String BUNDLE_KEY = "savedInstanceState";

    LoaderManager.LoaderCallbacks<WeatherDay> callbacks = MainActivity.this;
    ActivityMainBinding mBinding;
    Bundle bundle;
    String language, unit;
    Locale locale;
    LocationManager locationManager;
    View mLayout;

    boolean isGpsEnable = false;
    boolean isGpsPermission = false;

    LoaderManager.LoaderCallbacks<String> stringLoaderCallbacks = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int LOADER_SECOND, Bundle bundle) {
            return new SecondLoader(getApplicationContext(), bundle.getString(BUNDLE_SECOND));//, weatherdatebase);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String s) {
            if (s == null) {
                loader.cancelLoad();
                Toast.makeText(getApplicationContext(), "Oppss..qualcosa e' andato storto", Toast.LENGTH_LONG).show();
                return;
            }
            mBinding.tempMin.setText(s);
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main); usiamo DataBinding

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mLayout = findViewById(R.id.main_layout);  //mi serve per lo Snackbar dei permessi gps

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        isGpsEnable = (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
        isGpsPermission = (checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED);

        bundle = new Bundle();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        if (sharedPreferences.getBoolean("update_gps", false)) {

            if (!isGpsPermission) {
                Log.i("onCREATE", "! IS_GPS_PERMISSIONS");
                RequestGpsPermission();
            }

            if (!isGpsEnable) {
                Log.i("ONCREATE", "! IS_GPS_ENABLE ");
                enableGpsLocation();
            }

            GetLocationCoord(); //recupera le coord. gps e la localita'
            Log.v("MainActivity", "GPS localization active: " + sharedPreferences.getBoolean("update_gps", false));

        }

        unit = sharedPreferences.getString("units", null);

        mBinding.progressBar.setVisibility(View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        language = locale.getLanguage();

        if (getLoaderManager().getLoader(LOADER_ID) != null) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }
        if (getLoaderManager().getLoader(LOADER_SECOND) != null) {
            getLoaderManager().initLoader(LOADER_SECOND, null, stringLoaderCallbacks);
        }


        String localita = sharedPreferences.getString("location", null);
        if (savedInstanceState != null) {
            if (savedInstanceState.get(BUNDLE_KEY) != localita) startLaunchLoader(localita);
        } else if (localita != null) {
            startLaunchLoader(localita);
        } else
            Toast.makeText(getApplicationContext(), "Seleziona una Localita' in Settings", Toast.LENGTH_LONG).show();

        mBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (sharedPref.getBoolean("update_gps", false)) GetLocationCoord();

                String localita = sharedPref.getString("location", null);
                if (localita == null) {
                    Toast.makeText(getApplicationContext(), "Seleziona una Localita' in Settings", Toast.LENGTH_LONG).show();
                    return;
                } else
                    // launchLoader(localita);
                    startLaunchLoader(localita);

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_KEY, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("location", null));
        super.onSaveInstanceState(outState);
    }

    public void startLaunchLoader(String city) {
        URL daylyUrl = BuildServerUrl.BuilDaylyUrl(city, unit, language);
        Log.v("StartLaunchLoader " + city, "DailyUrl: " + daylyUrl.toString());
        URL url = BuildServerUrl.BuildUrl(city, unit, language);
        Log.v("StartLaunchLoader " + city, "url: " + url.toString());
        mBinding.progressBar.setVisibility(View.VISIBLE);
        bundle.putString(BUNDLE_ID, daylyUrl.toString());
        getLoaderManager().restartLoader(LOADER_ID, bundle, callbacks);
        bundle.putString(BUNDLE_SECOND, url.toString());
        getLoaderManager().restartLoader(LOADER_SECOND, bundle, stringLoaderCallbacks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case (R.id.update):
                String localita = PreferenceManager.getDefaultSharedPreferences(this).getString("location", null);
                startLaunchLoader(localita);
                break;

            case (R.id.setting):
                Intent startSettings = new Intent(this, SettingsActivity.class);
                startActivity(startSettings);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        switch (s) {
            case "location":
                String localita = sharedPreferences.getString("location", null);
                startLaunchLoader(localita);
                break;
            case "units":
                unit = sharedPreferences.getString("units", null);
                startLaunchLoader(sharedPreferences.getString("location", null));
                break;
            case "update_gps":
                if (sharedPreferences.getBoolean("update_gps", false)) {
                    enableGpsLocation();
                    GetLocationCoord();
                    startLaunchLoader(sharedPreferences.getString("location", null));
                }
                break;
        }
    }

    @Override
    public Loader<WeatherDay> onCreateLoader(int LOADER_ID, Bundle bund) {

        return new NewAsyncTask(this, bund.getString(BUNDLE_ID));

    }

    @Override
    public void onLoadFinished(Loader<WeatherDay> loader, WeatherDay s) {

        mBinding.progressBar.setVisibility(View.INVISIBLE);
        if (s == null) {
            loader.cancelLoad();
            Toast.makeText(getApplicationContext(), "Oppss..qualcosa e' andato storto", Toast.LENGTH_LONG).show();
        } else

            displayWeatherData(s);
    }

    @Override
    public void onLoaderReset(Loader<WeatherDay> loader) {
        Log.i("MainActivity", " ON LOADER RESET");

    }

    private void displayWeatherData(WeatherDay day) {
        String gradi;
        if (unit.equals("metric")) {
            gradi = " °C";
        } else gradi = " °F";
        mBinding.showTempMin.setText("" + Math.round(day.getTemp()) + gradi);
        // mBinding.tempMax.setText("" + Math.round(day.getTemp_max()) + gradi);
        mBinding.tempMax.setText("" + gradi);
        // mBinding.tempMin.setText("" + Math.round(day.getTemp_min()) + gradi);
        mBinding.pressure.setText("" + Math.round(day.getPressure()) + " hPa");
        mBinding.humidity.setText("" + Math.round(day.getHumidity()) + " %");
        mBinding.windSpeed.setText("" + Math.round(day.getWind()) + " km/h");
        mBinding.windDir.setText("" + day.getWind_dir());
        mBinding.description.setText(day.getDescription());
        int id = getResources().getIdentifier("ic_" + day.getIcon(), "drawable", getPackageName());
        mBinding.textImage.setBackground(getResources().getDrawable(id, null));
        mBinding.data.setText(day.getData());
        mBinding.alba.setText(day.getSunrise());
        mBinding.tramonto.setText(day.getSunset());

    }

    public void secondActivity(View v) {
        Intent startSecondActivity = new Intent(this, ListActivityForecast.class);
        startActivity(startSecondActivity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//if (isFinishing())
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);


    }

    protected void GetLocationCoord() {
        if (checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {

            Log.e("SelfCheckPermission ", "Permission Granted");

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                Log.w("GetLocationCoord ", "LocationNetwork: " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
            }
            if (location != null) {
                double lat = location.getLatitude();
                double longitude = location.getLongitude();
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, longitude, 1);
                    String cityName = addresses.get(0).getLocality();
                    Log.d("Lat: " + lat, " Long: " + longitude + " City: " + cityName);
                    SharedPreferences sharepref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharepref.edit();
                    editor.putString("location", cityName);
                    editor.apply();
                    //  startLaunchLoader(cityName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else RequestGpsPermission();
    }

    private void RequestGpsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(mLayout, R.string.gps_permission_required, Snackbar.LENGTH_INDEFINITE).
                    setAction(R.string.gps_ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSION_REQUEST_GPS);
                            isGpsPermission = true;
                        }
                    }).show();
        } else {
            Snackbar.make(mLayout, R.string.gps_unavailable, Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_GPS);
            isGpsPermission = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_GPS) {
            // Request for gps permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start Activity.
                Snackbar.make(mLayout, R.string.gps_permission_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
                // enableGpsLocation();
                GetLocationCoord();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, R.string.gps_permission_denied,
                        Snackbar.LENGTH_SHORT)
                        .show();
                isGpsPermission = false;
                return;
            }
        }
    }

    private void enableGpsLocation() {
        // if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) return;
        if (isGpsEnable) {
            Log.i("enableGpsLocation", " GPS IS ALREADY ACTIVE");
            return;
        }
        Log.i("enableGpsLocation", " ALERT DIALOG Build SETTINGS");
        AlertDialog.Builder alertBuild = new AlertDialog.Builder(this);
        alertBuild.setMessage(R.string.no_gps_alert_dialog).setCancelable(false).setTitle(R.string.gps_settings_title)
                .setPositiveButton(R.string.go_to_gps_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isGpsEnable = true;
                        Intent callGPSSettingIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                });
        alertBuild.setNegativeButton(R.string.no_gps_permission, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isGpsEnable = false;
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("update_gps", false);
                editor.commit();
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = alertBuild.create();
        dialog.show();
    }
}
