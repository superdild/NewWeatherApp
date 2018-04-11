package com.superdild.app.newweatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

/**
 * Created by gino on 04/03/18.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_pref);

        SharedPreferences sharePRef = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int countPref = prefScreen.getPreferenceCount();

        for (int i = 0; i < countPref; i++) {

            Preference p = prefScreen.getPreference(i);

            if (p instanceof EditTextPreference) {
                setPreferenceSummary(p, sharePRef.getString(getString(R.string.location), getString(R.string.dialog_editText)));
            }
            if (p instanceof ListPreference) {
                String value = sharePRef.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }

        }
    }

    private void setPreferenceSummary(Preference preference, String value) {

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }

        if (preference instanceof EditTextPreference) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            editTextPreference.setSummary(getPreferenceManager().getSharedPreferences().getString(getString(R.string.location), getString(R.string.dialog_editText)));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equals(getString(R.string.location))) {
            Preference pref = findPreference(s);
            setPreferenceSummary(pref, sharedPreferences.getString(getString(R.string.location), getString(R.string.dialog_editText)));
        }
        if (s.equals(getString((R.string.listpref_key)))) {
            Preference pref = findPreference(s);
            String value = sharedPreferences.getString(pref.getKey(), null);
            setPreferenceSummary(pref, value);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
