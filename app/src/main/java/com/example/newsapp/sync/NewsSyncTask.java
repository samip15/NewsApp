package com.example.newsapp.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import com.example.newsapp.Data.NewsContract;
import com.example.newsapp.Data.NewsLocationPrefrences;
import com.example.newsapp.Utilities.JsonNews;
import com.example.newsapp.Utilities.NetworkUtils;
import java.net.URL;

public class NewsSyncTask {


    /**
     * Gets the weather from the network and passes it to the database / content provider
     *
     * @param context
     */
    synchronized public static void syncNews(Context context) {
        try {
            String newslocation = NewsLocationPrefrences.getPreferedNewsLocation(context);
            URL newsRequestUrl = NetworkUtils.buildUrlTopHeadline(newslocation);
            // responce
            String jsonWeatherResponce = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl);
            // parse
            ContentValues[] newsValue = JsonNews.getNewsContentValuesFromJson(context, jsonWeatherResponce);
            // assign to provider
            if (newsValue != null && newsValue.length != 0) {
                // add to content provider
                ContentResolver resolver = context.getContentResolver();
                // delete old data and add new one
                resolver.delete(NewsContract.NewsEntry.CONTENT_URI, null, null);
                // new data
                resolver.bulkInsert(NewsContract.NewsEntry.CONTENT_URI, newsValue);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    
    }

    synchronized public static void syncEverythingNews(Context context) {
        try {
            String newsSearch = NewsLocationPrefrences.getPreferedNewsSearch(context);
            URL newsRequestUrl = NetworkUtils.buildUrl_Everything(newsSearch);
            // responce
            String jsonWeatherResponce = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl);
            // parse
            ContentValues[] newsValue = JsonNews.getNewsContentValuesFromJson(context, jsonWeatherResponce);
            // assign to provider
            if (newsValue != null && newsValue.length != 0) {
                // add to content provider
                ContentResolver resolver = context.getContentResolver();
                // delete old data and add new one
                resolver.delete(NewsContract.NewsEntry.CONTENT_URI, null, null);
                // new data
                resolver.bulkInsert(NewsContract.NewsEntry.CONTENT_URI, newsValue);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
