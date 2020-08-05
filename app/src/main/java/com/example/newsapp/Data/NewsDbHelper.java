package com.example.newsapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NewsDbHelper extends SQLiteOpenHelper {
    // db name
    public static final String DATABASE_NAME = "news.db";
    public static final int DATABASE_VERSION = 1;

    public NewsDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + NewsContract.NewsEntry.TABLE_NAME + "(" +
                NewsContract.NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NewsContract.NewsEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL," +
                NewsContract.NewsEntry.COLUMN_SOURCE + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL )";
        db.execSQL(SQL_CREATE_WEATHER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsEntry.TABLE_NAME);
        onCreate(db);
    }
}
