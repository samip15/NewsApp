package com.example.newsapp.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class NewsContract {

    // authority
    public static final String AUTHORITY = "com.example.newsapp";
    // base uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_NOTES  = "news";

    // inner class
    public static final class NewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES).build();

        public static Uri buildNewsUriWithDate(long date){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(date)).build();
        }

        // table name
        public static final String TABLE_NAME = "news";
        // news columns
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_description = "description";
        public static final String COLUMN_IMAGE_URL = "imageUrl";
    }
}
