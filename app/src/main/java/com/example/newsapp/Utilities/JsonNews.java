package com.example.newsapp.Utilities;

import android.content.Context;

import com.example.newsapp.Model.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonNews {
    public static List<NewsItem> getNewsDataFromJson(Context context, String jsonNewsResponse) throws JSONException {
        final String NEWS_MESSAGE_CODE = "status";
        final String NEWS_DESCRIPTION = "description";
        final String NEWS_DATE = "publishedAt";
        final String NEWS_TITLE = "title";
        final String NEWS_IMAGE_URL = "urlToImage";
        final String NEWS_ARTICLE = "articles";
        final String NEWS_SOURCES = "source";
        final String NEWS_SOURCE_NAME = "name";
        final String NEWS_AUTHOR = "author";

        List<NewsItem> listItemArrayList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonNewsResponse);
        if (jsonObject.has(NEWS_MESSAGE_CODE)) {
            String errorCode = jsonObject.getString(NEWS_MESSAGE_CODE);
            switch (errorCode) {
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
        for (int i = 0; i < NewsArray.length(); i++) {
            JSONObject newsobj = NewsArray.getJSONObject(i);
            String originalSources = null;
            for (int j = 0; i < newsobj.length(); j++) {
                if (newsobj.has(NEWS_SOURCES)) {
                    // Extract the value for the key called "original_sources"
                    originalSources = newsobj.getString(NEWS_SOURCE_NAME);
                }
            }


            String originalAuthor = null;
            if (newsobj.has(NEWS_AUTHOR)) {
                // Extract the value for the key called "original_Author"
                originalAuthor = newsobj.getString(NEWS_AUTHOR);
            }


            String originalTitle = null;
            if (newsobj.has(NEWS_TITLE)) {
                // Extract the value for the key called "original_title"
                originalTitle = newsobj.getString(NEWS_TITLE);
            }


            String originalDes = null;
            if (newsobj.has(NEWS_DESCRIPTION)) {
                // Extract the value for the key called "original_description"
                originalDes = newsobj.getString(NEWS_DESCRIPTION);
            }

            String originalDate = null;
            if (newsobj.has(NEWS_DATE)) {
                // Extract the value for the key called "original_date"
                originalDate = newsobj.getString(NEWS_DATE);
            }

            String Imgurl = null;
            if (newsobj.has(NEWS_IMAGE_URL)) {
                // Extract the value for the key called "original_image url"
                Imgurl = newsobj.getString(NEWS_IMAGE_URL);
            }


            // Create a new {@link News} object
            NewsItem news = new NewsItem(originalTitle, originalDes, Imgurl, originalDate, originalSources,originalAuthor);
            // Add the new {@link News} to the list of News
            listItemArrayList.add(news);

        }


        return listItemArrayList;


    }
}
