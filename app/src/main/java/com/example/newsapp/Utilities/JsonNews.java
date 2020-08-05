package com.example.newsapp.Utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.newsapp.Data.NewsContract;
import com.example.newsapp.Model.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonNews {

    private static final String NEWS_MESSAGE_CODE = "status";
    private static final String NEWS_DESCRIPTION = "description";
    private static final String NEWS_DATE = "publishedAt";
    private static final String NEWS_TITLE = "title";
    private static final String NEWS_IMAGE_URL = "urlToImage";
    private static final String NEWS_ARTICLE = "articles";
    private static final String NEWS_SOURCES = "source";
    private static final String NEWS_SOURCE_NAME = "name";
    private static final String NEWS_AUTHOR = "author";


    public static List<NewsItem> getNewsDataFromJson(Context context, String jsonNewsResponse) throws JSONException {


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
            JSONObject newSourcesObj = null;
            String originalSources = null;
            if (newsobj.has(NEWS_SOURCES)) {
                // Extract the value for the key called "original_sources"
                newSourcesObj = newsobj.getJSONObject(NEWS_SOURCES);
                if (newSourcesObj.has(NEWS_SOURCE_NAME)) {
                    originalSources = newSourcesObj.getString(NEWS_SOURCE_NAME);
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
            NewsItem news = new NewsItem(originalTitle, originalDes, Imgurl, originalDate, originalSources, originalAuthor);
            // Add the new {@link News} to the list of News
            listItemArrayList.add(news);

        }


        return listItemArrayList;


    }

    /**
     * This method will help on:content provider bulk insert and json parse
     *
     * @param context
     * @param jsonNewsResponse
     * @return
     * @throws JSONException
     */


    public static ContentValues[] getNewsContentValuesFromJson(Context context, String jsonNewsResponse) throws JSONException {
        JSONObject newsJson = new JSONObject(jsonNewsResponse);
        if (newsJson.has(NEWS_MESSAGE_CODE)) {
            String errorCode = newsJson.getString(NEWS_MESSAGE_CODE);
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

        JSONArray newsArray = newsJson.getJSONArray(NEWS_ARTICLE);
        ContentValues[] newsContentValues = new ContentValues[newsArray.length()];


        for (int i = 0; i < newsArray.length(); i++) {
            JSONObject newsobj = newsArray.getJSONObject(i);
            JSONObject newSourcesObj = null;
            String originalSources = null;
            if (newsobj.has(NEWS_SOURCES)) {
                // Extract the value for the key called "original_sources"
                newSourcesObj = newsobj.getJSONObject(NEWS_SOURCES);
                if (newSourcesObj.has(NEWS_SOURCE_NAME)) {
                    originalSources = newSourcesObj.getString(NEWS_SOURCE_NAME);
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

            String imgurl = null;
            if (newsobj.has(NEWS_IMAGE_URL)) {
                // Extract the value for the key called "original_image url"
                imgurl = newsobj.getString(NEWS_IMAGE_URL);
            }


            // Create a new {@link News} object
            ContentValues newsValues = new ContentValues();
            newsValues.put(NewsContract.NewsEntry.COLUMN_DATE, originalDate);
            newsValues.put(NewsContract.NewsEntry.COLUMN_TITLE, originalTitle);
            newsValues.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, originalDes);
            newsValues.put(NewsContract.NewsEntry.COLUMN_IMAGE_URL, imgurl);
            newsValues.put(NewsContract.NewsEntry.COLUMN_SOURCE, originalSources);
            newsValues.put(NewsContract.NewsEntry.COLUMN_AUTHOR, originalAuthor);
            newsContentValues[i] = newsValues;
        }


        return newsContentValues;
    }
}
