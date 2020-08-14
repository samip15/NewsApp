package com.example.newsapp.Data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.newsapp.R;

public class NewsLocationPrefrences {
    private static final String DEFAULT_LOCATION_NEWS = "in";
    private static final String PREF_BOOL_SEARCH = "bool_search";

    /**
     * This Method Stores Shared Preference Country Location
     *
     * @param context:Context For Method
     * @return: Preference String Of Location
     */
    public static String getPreferedNewsLocation(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String keyLocation = context.getString(R.string.pref_country_key);
        String defaultLocation = context.getString(R.string.pref_country_value_in);
        return pref.getString(keyLocation, defaultLocation);
    }

    /**
     * This Method Stores Shared Preference Search Order:
     *
     * @param context:
     * @return:default:covid19
     */
    public static String getPreferedNewsSearch(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String keyLocation = context.getString(R.string.pref_search_key);
        String defaultSearch = context.getString(R.string.pref_search_by_default);
        return pref.getString(keyLocation, defaultSearch);
    }

    public static String getDefaultLocationNews() {
        return DEFAULT_LOCATION_NEWS;
    }

    public static void setPrefBoolSearch(Context context, boolean searchBool) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(PREF_BOOL_SEARCH, searchBool);
        editor.apply();
    }

    public static boolean getPrefBoolSearch(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_BOOL_SEARCH, false);
    }

}
