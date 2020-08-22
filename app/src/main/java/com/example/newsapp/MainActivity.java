package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.Data.NewsContract;
import com.example.newsapp.sync.NewsSyncUtils;
import com.facebook.shimmer.ShimmerFrameLayout;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, NewsAdapter.NewsAdapterOnclickListner {
    RecyclerView mRecycler;
    NewsAdapter myNewsAdapter;
    private TextView mErrorMessageDisplay;
    private static final int NEWS_LOADER_ID = 0;
    Context mContext = MainActivity.this;
    private ShimmerFrameLayout mShimmerViewContainer;
    // if shared preference has been changed
    private static boolean PREFRENCE_UPDATED = false;
    private static final String TAG = "MainActivity";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    // if shared preference has been changed
    // weather columns that are displayed and queried
    public static final String[] MAIN_NEWS_PROJECTION = {
            NewsContract.NewsEntry.COLUMN_DATE,
            NewsContract.NewsEntry.COLUMN_TITLE,
            NewsContract.NewsEntry.COLUMN_DESCRIPTION,
            NewsContract.NewsEntry.COLUMN_IMAGE_URL,
            NewsContract.NewsEntry._ID
    };
    // weather table ko column ko indexes
    public static final int INDEX_NEWS_DATE = 0;
    public static final int INDEX_NEWS_TITLE = 1;
    public static final int INDEX_NEWS_DESC = 2;
    public static final int INDEX_NEWS_IMAGE_URL = 3;
    public static final int INDEX_NEWS_ID = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Finding All Needed Items By Find View By Id*/
        mRecycler = findViewById(R.id.recycler_view);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);

        //set rv
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecycler.setLayoutManager(linearLayout);
        mRecycler.setHasFixedSize(true);
        //adapter
        myNewsAdapter = new NewsAdapter(this, this);
        mRecycler.setAdapter(myNewsAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

        if (isOnline()) {

            /* Initializing loader For The First Time */
            getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);

            // get the data from our service
            NewsSyncUtils.initialized(this);

        } else {
            showErrorMessage();
            mShimmerViewContainer.setVisibility(View.INVISIBLE);
        }

        // resister preference
        // PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

    }

    /*
     *  ----------------------  Recyclerview OnClick --------------------------
     * */
    @Override
    public void onClick(long id) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        // uri
        Uri uriforDetail = NewsContract.NewsEntry.buildNewsUriWithId(id);
        intent.setData(uriforDetail);
        startActivity(intent);
    }




    /*
     *  ----------------------  Loading Functions --------------------------
     * */


    private void showNewsDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecycler.setVisibility(View.VISIBLE);
    }

    /**
     * This Method Is Used For Showing The Error Message
     */
    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.INVISIBLE);
    }

    /**
     * This Method Is On create Of Async Task Loader
     *
     * @param args:Bundle Args Null If Need Not To Specify
     * @return: N/A
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {

        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();

        switch (loaderId) {
            case NEWS_LOADER_ID:
                //uri
                Uri forecastQueryUri = NewsContract.NewsEntry.CONTENT_URI;
                String sortOrder = NewsContract.NewsEntry._ID + " ASC";
                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_NEWS_PROJECTION,
                        null,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader Not Implemented" + loaderId);
        }
    }


    /**
     * This Method Is Invoked When Load In Background Is Finished
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor newsData) {

        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.INVISIBLE);

        myNewsAdapter.swapCursor(newsData);

        if (newsData.getCount() != 0) {
            showNewsDataView();
        }

    }

    /**
     * If Loader Needs To Be Reset
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        myNewsAdapter.swapCursor(null);
    }

    /**
     * Checks Up If System Is Online Or Not
     *
     * @return :Current Network If Connected Or Connecting
     */
    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * Displays Offline Message If No Connection
     */
    private void showOfflineMessage() {
        mRecycler.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    //------------------------ Menu items ------------------------------------

    /**
     * This Is Created Menu
     *
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refreshnews, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
            return true;
        }

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    /**
//     * If Prefrence Is Changed
//     *
//     * @param sharedPreferences:Pref to save data
//     * @param key:key                to identify
//     */
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        PREFRENCE_UPDATED = true;
//
//    }
//
//    /*---------------------------- lifecycle methods------------------ */
//    @Override
//    protected void onStart() {
////        super.onStart();
////        if (PREFRENCE_UPDATED) {
////            getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
////            PREFRENCE_UPDATED = false;
////        }
////    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
//    }

}