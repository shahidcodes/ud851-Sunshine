package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

/**
 * Created by shaah on 21-03-2018.
 */
// COMPLETED (4) Create SettingsFragment and extend PreferenceFragmentCompat


public class SettingsFragment extends PreferenceFragmentCompat
        // COMPLETED (10) Implement OnSharedPreferenceChangeListener from SettingsFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs);
        // COMPLETED (9) Set the preference summary on each preference that isn't a CheckBoxPreference
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();

        for (int i=0; i<count; i++){
            Preference preference = preferenceScreen.getPreference(i);
            if (!(preference instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }


    // COMPLETED (8) Create a method called setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference

    private void setPreferenceSummary(Preference preference, String value){
        if (preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if (index >= 0 ){
                CharSequence entryValue = listPreference.getEntries()[index];
                preference.setSummary(entryValue);
            }
        }else if (preference instanceof EditTextPreference){
            preference.setSummary(value);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // COMPLETED (12) Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        // COMPLETED (13) Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
    // COMPLETED (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = getPreferenceScreen().findPreference(key);

        if (!(preference instanceof CheckBoxPreference)){
            setPreferenceSummary(preference, sharedPreferences.getString(key, ""));
        }
    }
}
