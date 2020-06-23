package com.example.newsapp.Data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.newsapp.R;

public class NewsLocationPrefrences {
    private static final String DEFAULT_LOCATION_NEWS = "in";
    public static String getPreferedWeatherLocation(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String keyLocation = context.getString(R.string.pref_country_key);
        String defaultLocation = context.getString(R.string.pref_country_value_in);
        return pref.getString(keyLocation,defaultLocation);
    }

    public static String getDefaultLocationNews() {
        return DEFAULT_LOCATION_NEWS;
    }
}
