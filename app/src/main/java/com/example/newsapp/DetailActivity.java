package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapp.Data.NewsContract;
import com.example.newsapp.Model.NewsItem;
import com.example.newsapp.sync.NewsSyncUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {



    private static final int NEWS_LOADER_ID = 1;
    Context mContext = DetailActivity.this;

    public static final String EXTRA_NEWS = "news";
    private static final String HAS_TAG = "# Indian News";
    NewsItem newsItem;
    TextView titleTv, descriptionTv, mAuthor, mSources;
    ImageView detailImage;
    String newsTitle, newsDescription;
    // take uri
    private Uri mUri;


    public static final String[] MAIN_NEWS_PROJECTION = {
            NewsContract.NewsEntry.COLUMN_TITLE,
            NewsContract.NewsEntry.COLUMN_DESCRIPTION,
            NewsContract.NewsEntry.COLUMN_IMAGE_URL,
            NewsContract.NewsEntry.COLUMN_SOURCE,
            NewsContract.NewsEntry.COLUMN_AUTHOR,
    };
    // weather table ko column ko indexes
    public static final int INDEX_NEWS_TITLE = 0;
    public static final int INDEX_NEWS_DESC = 1;
    public static final int INDEX_NEWS_IMAGE_URL = 2;
    public static final int INDEX_NEWS_SOURCE = 3;
    public static final int INDEX_NEWS_AUTHOR = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleTv = findViewById(R.id.title_tv_detail);
        descriptionTv = findViewById(R.id.discryption_tv_detail);
        detailImage = findViewById(R.id.img_iv_detail);
        mAuthor = findViewById(R.id.author);
        mSources = findViewById(R.id.source_name);
        // intent
        mUri = getIntent().getData();
        if (mUri == null) {
            throw new NullPointerException("Uri for DetailActivity cannot be null");
        }
        getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);

    }

    /**
     * This Is The Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createNewsIntent());
        return true;
    }

    private Intent createNewsIntent() {

        Intent shareintent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(newsTitle + "\n" + "\n" + newsDescription + "\n" + HAS_TAG)
                .getIntent();
        return shareintent;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {
            case NEWS_LOADER_ID:
                //uri
                Uri forecastQueryUri = NewsContract.NewsEntry.CONTENT_URI;
                String sortOrder = NewsContract.NewsEntry.COLUMN_DATE + " ASC";
                String selection = NewsContract.NewsEntry.getSqlSelectForId();
                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_NEWS_PROJECTION,
                        selection,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader Not Implemented" + loaderId);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        //validating cursor data
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }
        if (!cursorHasValidData) {
            return;
        }

        //description
       String descriptionText = data.getString(INDEX_NEWS_DESC);
        descriptionTv.setText(descriptionText);

        // title
        String titleText = data.getString(INDEX_NEWS_TITLE);
        titleTv.setText(titleText);

        // Author
        String author = data.getString(INDEX_NEWS_AUTHOR);
        mAuthor.setText(author);

        // Sources
        String sources = data.getString(INDEX_NEWS_SOURCE);
        mSources.setText(sources);

        String imageUrl = data.getString(INDEX_NEWS_IMAGE_URL);
        Picasso.get().load(imageUrl).into(detailImage);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}