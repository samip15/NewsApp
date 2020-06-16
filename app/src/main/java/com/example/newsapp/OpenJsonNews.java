package com.example.newsapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenJsonNews {
    public static List<NewsItem> getWeatherDataFromJson(Context context, String jsonNewsResponse) throws JSONException {
        final String NEWS_MESSAGE_CODE = "status";
        final String NEWS_DESCRIPTION = "description";
        final String NEWS_DATE = "description";
        final String NEWS_TITLE = "title";
        final String NEWS_IMAGE_URL = "urlToImage";
        final String NEWS_ARTICLE = "articles";

        List<NewsItem> arrmodel = new ArrayList<>();

        String[] parsedNewsData = null;
        JSONObject jsonObject = new JSONObject();
        if (jsonObject.has(NEWS_MESSAGE_CODE)) {
            String errorcode = jsonObject.getString(NEWS_MESSAGE_CODE);
            switch (errorcode) {
                case "ok":
                    break;
                case "error":
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray NewsArray = jsonObject.getJSONArray(NEWS_ARTICLE);
        parsedNewsData = new String[NewsArray.length()];
        for (int i = 0; i < NewsArray.length(); i++) {
            String title;
            String description;
            String imageurl;

            JSONObject newsobj = NewsArray.getJSONObject(i);

            String originalTitle = null;
            if (newsobj.has(NEWS_TITLE)) {
                // Extract the value for the key called "original_title"
                originalTitle = newsobj.getString(NEWS_TITLE);
            }


            String originalDes = null;
            if (newsobj.has(NEWS_DESCRIPTION)) {
                // Extract the value for the key called "original_description"
                originalTitle = newsobj.getString(NEWS_DESCRIPTION);
            }

            String originalDate = null;
            if (newsobj.has(NEWS_DESCRIPTION)) {
                // Extract the value for the key called "original_description"
                originalTitle = newsobj.getString(NEWS_DESCRIPTION);
            }

            String imgurl = null;
            if (newsobj.has(NEWS_IMAGE_URL)) {
                // Extract the value for the key called "original_description"
                originalTitle = newsobj.getString(NEWS_IMAGE_URL);
            }


            // Create a new {@link Movie} object
            NewsItem news = new NewsItem(originalTitle,originalDes,imgurl,originalDate);
            // Add the new {@link Movie} to the list of movies
            arrmodel.add(news);

        }


        return  arrmodel;


    }
}
