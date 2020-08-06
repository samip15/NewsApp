package com.example.newsapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NewsProvider extends ContentProvider {

    // constant that helps us to match with uri
    public static final int CODE_NEWS = 100;
    public static final int CODE_NEWS_WITH_ID = 101;

    // uri matcher
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // built uri matcher
    public static UriMatcher buildUriMatcher() {
        // initialize
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // adding two uri
        uriMatcher.addURI(NewsContract.AUTHORITY, NewsContract.PATH_NOTES, CODE_NEWS);
        uriMatcher.addURI(NewsContract.AUTHORITY, NewsContract.PATH_NOTES + "/#", CODE_NEWS_WITH_ID);
        return uriMatcher;
    }

    // ======================== Database Operations=======================
    private NewsDbHelper newsDBHelper;


    @Override
    public boolean onCreate() {
        newsDBHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = newsDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case CODE_NEWS:
                retCursor = db.query(NewsContract.NewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_NEWS_WITH_ID:
                retCursor = db.query(NewsContract.NewsEntry.TABLE_NAME,
                        projection,
                        NewsContract.NewsEntry._ID+" = ?",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI" + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Nullable
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        // database access
        final SQLiteDatabase db = newsDBHelper.getWritableDatabase();
        // match anusar cursor ra data pathuxa
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CODE_NEWS:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    throw e;
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // database access
        final SQLiteDatabase db = newsDBHelper.getWritableDatabase();
        // match anusar cursor ra data pathuxa
        int match = sUriMatcher.match(uri);
        // delete int
        int newsDeleted;
        if (selection == null) {
            selection = "1";
        }
        switch (match) {
            case CODE_NEWS:
                newsDeleted = db.delete(NewsContract.NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        // notify content resolver about new data insertion
        if (newsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);

        }

        return newsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
