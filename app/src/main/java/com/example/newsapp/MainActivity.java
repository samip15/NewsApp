package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.newsapp.Data.NewsLocationPrefrences;
import com.example.newsapp.Utilities.NetworkUtils;
import com.example.newsapp.Model.NewsItem;
import com.example.newsapp.Utilities.JsonNews;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>>, NewsAdapter.newsAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {

    RecyclerView mRecycler;
    NewsAdapter myNewsAdapter;
    private TextView mErrorMessageDisplay;
    private static final int NEWS_LOADER_ID = 0;
    Context mContext = MainActivity.this;
    private ShimmerFrameLayout mShimmerViewContainer;
    // if shared preference has been changed
    private static boolean PREFRENCE_UPDATED = false;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Finding All Needed Items By Find View By Id*/
        mRecycler = findViewById(R.id.recycler_view);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        //set rv
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecycler.setLayoutManager(linearLayout);
        mRecycler.setHasFixedSize(true);
        List<NewsItem> news = new ArrayList<>();
        //adapter
        myNewsAdapter = new NewsAdapter(news);
        mRecycler.setAdapter(myNewsAdapter);
        /* Initializing loader For The First Time */
        getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
        // resister preference
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

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
     * @param id:         Initialized Id From MainActivity On Create
     * @param args:Bundle Args Null If Need Not To Specify
     * @return: N/A
     */
    @NonNull
    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, @Nullable Bundle args) {
        // create async task loader
        return new AsyncTaskLoader<List<NewsItem>>(this) {
            List<NewsItem> mNewsData = null;

            /**
             * This method is similar to on pre execute in async task
             */
            @Override
            protected void onStartLoading() {

                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();

                if (mNewsData != null) {
                    deliverResult(mNewsData);
                }

                // triggers the load in background function to load data
                forceLoad();
            }

            /**
             * This Method Loads Item In Background
             * @return
             */
            @Nullable
            @Override
            public List<NewsItem> loadInBackground() {
                String location = NewsLocationPrefrences.getPreferedNewsLocation(mContext);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                if (sharedPreferences.getString(getString(R.string.pref_sort_by_key), "").equals(getString(R.string.pref_sort_by_topheadline))) {
                    URL newsRequestUrl = NetworkUtils.buildUrl_topHeadline(location);
                    List<NewsItem> newsDataFromJson_topHeadline = null;
                    try {
                        String jsonNewsResponse = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl);
                        newsDataFromJson_topHeadline = JsonNews.getNewsDataFromJson(MainActivity.this, jsonNewsResponse);
                        return newsDataFromJson_topHeadline;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    String search_by = NewsLocationPrefrences.getPreferedNewsSearch(mContext);
                    URL newsRequestUrl = NetworkUtils.buildUrl_Everything(search_by);
                    List<NewsItem> newsDataFromJson_everything = null;
                    try {
                        String jsonNewsResponse = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl);
                        Log.e(TAG, "loadInBackground: "+jsonNewsResponse );
                        newsDataFromJson_everything = JsonNews.getNewsDataFromJson(MainActivity.this, jsonNewsResponse);
                        return newsDataFromJson_everything;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }

                }


            }

            /**
             * If Data Needs To Cached
             */
            @Override
            public void deliverResult(@Nullable List<NewsItem> data) {
                mNewsData = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * This Method Is Invoked When Load In Background Is Finished
     *
     * @param data:Load In Background Result Data
     */
    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsItem>> loader, List<NewsItem> data) {

        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.INVISIBLE);

        // Add the movie data
        if (data != null && !data.isEmpty()) {
            showNewsDataView();
            myNewsAdapter.setNewsData(data, this);
        } else {
            showErrorMessage();
        }

        if (!isOnline()) {
            showOfflineMessage();
        }

    }

    /**
     * If Loader Needs To Be Reset
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsItem>> loader) {

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

    private void invilidateNewsData() {
        myNewsAdapter.setNewsData(null, this);
    }

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
            invilidateNewsData();
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

    /**
     * Item Clicked From Each Views
     */
    @Override
    public void onItemClick(NewsItem news) {

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_NEWS, news);
        startActivity(intent);

    }

    /**
     * If Prefrence Is Changed
     *
     * @param sharedPreferences:Pref to save data
     * @param key:key                to identify
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFRENCE_UPDATED = true;

    }

    /*---------------------------- lifecycle methods------------------ */
    @Override
    protected void onStart() {
        super.onStart();
        if (PREFRENCE_UPDATED) {
            getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
            PREFRENCE_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}