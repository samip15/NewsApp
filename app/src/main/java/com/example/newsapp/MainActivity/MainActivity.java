package com.example.newsapp.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.newsapp.Detail.DetailActivity;
import com.example.newsapp.Prefrences.LocationPrefrences;
import com.example.newsapp.Network.NetworkUtils;
import com.example.newsapp.Adapter.NewsAdapter;
import com.example.newsapp.Items.NewsItem;
import com.example.newsapp.Json.OpenJsonNews;
import com.example.newsapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>>,NewsAdapter.newsAdapterOnClickHandler {

    RecyclerView mRecycler;
    NewsAdapter myNewsAdapter;
    private TextView mErrorMessageDisplay;
    private static final int NEWS_LOADER_ID = 0;
    Context mContext = MainActivity.this;
    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);

    }

    /*
     *  ----------------------  Loading Functions --------------------------
     * */


    private void showNewsDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecycler.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, @Nullable Bundle args) {
        // create async task loader
        return new AsyncTaskLoader<List<NewsItem>>(this) {
            List<NewsItem> mNewsData = null;

            @Override
            protected void onStartLoading() {
                mShimmerViewContainer.startShimmerAnimation();

                if (mNewsData != null) {
                    deliverResult(mNewsData);
                }

                // triggers the load in background function to load data
                forceLoad();
            }

            @Nullable
            @Override
            public List<NewsItem> loadInBackground() {
                String location = LocationPrefrences.getPreferedWeatherLocation(mContext);
                URL newsRequestUrl = NetworkUtils.buildUrl(location);
                List<NewsItem> newsDataFromJson = null;
                try {
                    String jsonNewsResponse = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl);
                    newsDataFromJson = OpenJsonNews.getWeatherDataFromJson(MainActivity.this, jsonNewsResponse);
                    return newsDataFromJson;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable List<NewsItem> data) {
                mNewsData = data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsItem>> loader, List<NewsItem> data) {

        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);

        // Add the movie data
        if (data != null && !data.isEmpty()) {
            showNewsDataView();
            myNewsAdapter.setNewsData(data,this);
        } else {
            showErrorMessage();
        }

        if (!isOnline()) {
            showOfflineMessage();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsItem>> loader) {

    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void showOfflineMessage() {
        mRecycler.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    //------------------------Menu items---------------------

    private void invilidateNewsData() {
        myNewsAdapter.setNewsData(null,this);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(NewsItem news) {

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_NEWS, news);
        startActivity(intent);

    }
}