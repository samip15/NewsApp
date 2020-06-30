package com.example.newsapp.Data;

import android.provider.BaseColumns;

public class NewsContract {
    // inner class
    public static final class NewsEntry implements BaseColumns {
        // table name
        public static final String TABLE_NAME = "news";
        // news columns
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_description = "description";
        public static final String COLUMN_IMAGE_URL = "imageUrl";
    }
}
