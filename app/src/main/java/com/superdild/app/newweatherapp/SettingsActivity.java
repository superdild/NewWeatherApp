package com.superdild.app.newweatherapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_setting, new SettingsFragment());
        fragmentTransaction.commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.activity_setting, new SettingsFragment()).commit();
    }
}
