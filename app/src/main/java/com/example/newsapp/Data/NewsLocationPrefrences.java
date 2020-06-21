package com.example.newsapp.Data;

import android.content.Context;

public class NewsLocationPrefrences {
    private static final String DEFAULT_LOCATION_NEWS = "in";
    public static String getPreferedWeatherLocation(Context context) {
        return getDefaultLocationNews();
    }

    public static String getDefaultLocationNews() {
        return DEFAULT_LOCATION_NEWS;
    }
}
