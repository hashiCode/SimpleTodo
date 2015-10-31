package com.hashicode.simpletodo.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.preference.PreferenceManager;
import com.hashicode.simpletodo.R;

/**
 * Created by takahashi on 3/9/16.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        this.initializeSummarys();
    }

    /**
     * Initialize the preference summarys
     */
    private void initializeSummarys() {
        setDefaultSummary();

    }

    private void setDefaultSummary() {
        Preference defaultViewKey = findPreference(getActivity().getResources().getString(R.string.preferences_default_view_key));
        defaultViewKey.setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getActivity().getString(R.string.preferences_default_view_key), ""));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference connectionPref = findPreference(key);
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
