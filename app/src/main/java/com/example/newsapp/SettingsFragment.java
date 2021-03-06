package com.example.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.example.newsapp.Data.NewsLocationPreferences;
import com.example.newsapp.sync.NewsSyncUtils;

import static com.example.newsapp.R.string.pref_sort_by_everything;
import static com.example.newsapp.R.string.pref_sort_by_topheadline;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private EditTextPreference editTextPreference;
    private ListPreference listPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        // get shared preference and preference screen when we add our summary
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();
        editTextPreference = (EditTextPreference) preferenceScreen.getPreference(0);
        listPreference = (ListPreference) preferenceScreen.getPreference(1);
        for (int i = 0; i < count; i++) {
            Preference p = preferenceScreen.getPreference(i);
            String value = sharedPreferences.getString(p.getKey(), "");
            showSearchEditPreference(p, value);
            showCountryListPreference(p, value);
            setPreferenceSummary(p, value);
        }
        Preference checkP = preferenceScreen.getPreference(2);
        String checkValue = sharedPreferences.getString(checkP.getKey(), "");
        if (checkValue.equals(getString(pref_sort_by_everything))) {
            listPreference.setVisible(false);
            showSearchEditPreference(checkP, checkValue);
            NewsLocationPreferences.setPrefTypeSearch(getActivity(), "everything");
        } else {
            listPreference.setVisible(true);
            showCountryListPreference(checkP,checkValue);
            NewsLocationPreferences.setPrefTypeSearch(getActivity(), "country");
        }
        NewsSyncUtils.startImmediatelySync(getActivity());
    }

    /**
     * To Show Or Disable Search Edit Preference
     *
     * @param p     = preference
     * @param value = Preference value
     */
    private void showSearchEditPreference(Preference p, String value) {
        if (p instanceof ListPreference && value.equals(getString(pref_sort_by_topheadline))) {
            editTextPreference.setVisible(false);
        } else {
            editTextPreference.setVisible(true);
        }

    }

    /**
     * To Show Or Disable Country List Preference
     *
     * @param p     = preference
     * @param value = preference value
     */
    private void showCountryListPreference(Preference p, String value) {
        if (p instanceof ListPreference && value.equals(getString(pref_sort_by_everything))) {
            listPreference.setVisible(false);
        } else {
            listPreference.setVisible(true);
        }

    }

    /**
     * To Set Summary
     *
     * @param p     : preference
     * @param value : Shared Preference Value
     */
    private void setPreferenceSummary(Preference p, String value) {
        if (p instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) p;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            p.setSummary(value);
        }
    }

    /**
     * To Listen If Preference Changed
     *
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference p = findPreference(key);
        if (p != null) {
            String value = sharedPreferences.getString(p.getKey(), "");
            if (value.equals(getString(R.string.pref_sort_by_topheadline))) {
                Log.e("settings","onSharedPreferenceChanged: "+value );
                editTextPreference.setVisible(false);
                listPreference.setVisible(true);
                NewsLocationPreferences.setPrefTypeSearch(getActivity(), "country");
            } else if(value.equals(getString(R.string.pref_sort_by_everything))) {
                Log.e("settings","onSharedPreferenceChanged: "+key );
                editTextPreference.setVisible(true);
                listPreference.setVisible(false);
                NewsLocationPreferences.setPrefTypeSearch(getActivity(), "everything");
            }
            NewsSyncUtils.startImmediatelySync(getActivity());

            setPreferenceSummary(p, value);
        }
    }

    //---- - - -----------------------------fragment Lifecycle------------------------//

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}