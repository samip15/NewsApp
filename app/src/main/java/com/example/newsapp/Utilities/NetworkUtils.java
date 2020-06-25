package com.example.newsapp.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.preference.PreferenceManager;

import com.example.newsapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = "com/example/newsapp/Utilities";
    private static final String BASE_NEWS_URL_top_headline = "http://newsapi.org/v2/top-headlines";
    private static final String BASE_NEWS_URL_everything = "http://newsapi.org/v2/everything";
    private static final String BASE_NEWS_URL_sources = "https://newsapi.org/v2/sources";
    private static final String appid = "2feae0cc3b534626b20a8eaee2b63a03";
    static String APPID_PARAM = "apiKey";
    static String QUERY_PARAM = "country";

    public static URL buildUrl(String countryname) {
        Uri builduri = Uri.parse(BASE_NEWS_URL_top_headline).buildUpon()
                .appendQueryParameter(QUERY_PARAM, countryname)
                .appendQueryParameter(APPID_PARAM, appid)
                .build();
        URL url = null;
        try {
            url = new URL(builduri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;

            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
