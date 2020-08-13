package com.example.newsapp.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.example.newsapp.Data.NewsLocationPrefrences;
import com.example.newsapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    /* Json Variables */
    private static final String TAG = "com/example/newsapp/Utilities";
    private static final String BASE_NEWS_URL_top_headline = "http://newsapi.org/v2/top-headlines";
    private static final String BASE_NEWS_URL_everything = "http://newsapi.org/v2/everything";
    private static final String appid = "2feae0cc3b534626b20a8eaee2b63a03";
    static String APPID_PARAM = "apiKey";
    static String QUERY_PARAM = "country";
    static String SEARCH_QUERY_PARAM = "q";

    /**
     * Building Uri to Url
     *
     * @param countryname:Country Name
     * @return: returns url
     */
    public static URL buildUrlTopHeadline(String countryname) {
        Uri builduri_top_headline = Uri.parse(BASE_NEWS_URL_top_headline).buildUpon()
                .appendQueryParameter(QUERY_PARAM, countryname)
                .appendQueryParameter(APPID_PARAM, appid)
                .build();
        URL url = null;
        try {

            url = new URL(builduri_top_headline.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Building Uri to Url For Sort By Everything
     *
     * @param search:Country Name
     * @return: returns url
     */
    @SuppressLint("LongLogTag")
    public static URL buildUrl_Everything(String search) {
        Uri builduri_everything = Uri.parse(BASE_NEWS_URL_everything).buildUpon()
                .appendQueryParameter(SEARCH_QUERY_PARAM, search)
                .appendQueryParameter(APPID_PARAM, appid)
                .build();
        URL url = null;
        try {
            url = new URL(builduri_everything.toString());
            Log.e(TAG, "url is "+ url );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    /**
     * This Method Triggers Each Url And Scans
     *
     * @param url:So Called Url
     * @throws IOException:Input Output Exception
     * @return:String Or Null
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            // determines the start of the string
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
