package com.example.newsapp.Data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.newsapp.R;

public class NewsLocationPreferences {
    private static final String DEFAULT_LOCATION_NEWS = "in";
    private static final String PREF_TYPE_SEARCH = "type_search";

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

    public static void setPrefTypeSearch(Context context, String searchType) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREF_TYPE_SEARCH, searchType);
        editor.apply();
    }

    public static String getPrefTypeSearch(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_TYPE_SEARCH, "");
    }

}
