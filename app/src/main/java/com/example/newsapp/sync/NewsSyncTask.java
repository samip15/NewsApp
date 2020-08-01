package com.example.newsapp.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateUtils;

import com.example.newsapp.Data.NewsContract;
import com.example.newsapp.Data.NewsLocationPrefrences;
import com.example.newsapp.Utilities.JsonNews;
import com.example.newsapp.Utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class NewsSyncTask {


    /**
     * Gets the weather from the network and passes it to the database / content provider
     *
     * @param context
     */
    synchronized public static void syncWeather(Context context) {
        try {
            String newslocation = NewsLocationPrefrences.getPreferedNewsLocation(context);
            URL newsRequestUrl = NetworkUtils.buildUrl_topHeadline(newslocation);

            // responce
            String jsonWeatherResponce = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl);
            // parse
            ContentValues[] newsValue = JsonNews.getNewsContentValuesFromJson(context, jsonWeatherResponce);

            String newsSearch = NewsLocationPrefrences.getPreferedNewsSearch(context);
            URL newsRequestUrl2 = NetworkUtils.buildUrl_topHeadline(newsSearch);

            // responce
            String jsonNewsResponce2 = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl2);
            // parse
            ContentValues[] newsValue2 = JsonNews.getNewsContentValuesFromJson(context, jsonNewsResponce2);


            // assign to provider
            if (newsValue != null && newsValue.length != 0) {
                // add to content provider
                ContentResolver resolver = context.getContentResolver();
                // delete old data and add new one
                resolver.delete(NewsContract.NewsEntry.CONTENT_URI, null, null);
                // new data
                resolver.bulkInsert(NewsContract.NewsEntry.CONTENT_URI, newsValue);
            }

            // assign to provider
            if (newsValue2 != null && newsValue2.length != 0) {
                // add to content provider
                ContentResolver resolver = context.getContentResolver();
                // delete old data and add new one
                resolver.delete(NewsContract.NewsEntry.CONTENT_URI, null, null);
                // new data
                resolver.bulkInsert(NewsContract.NewsEntry.CONTENT_URI, newsValue2);
            }

        } catch (JSONException e) {
            e.fillInStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
