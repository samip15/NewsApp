package com.example.newsapp.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.newsapp.Data.NewsContract;
import com.example.newsapp.Data.NewsLocationPreferences;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import java.util.concurrent.TimeUnit;

public class NewsSyncUtils {
    private static final String TAG = "NewsSyncUtils";
    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FIX_TIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static boolean sInitialized;
    // unique tag for jobs
    private static final String NEWS_SYNC_TAG = "news-sync";

    static void scheduleFirebaseJobDispatcher(@NonNull final Context context) {
        Log.e(TAG, "news is syncing");
        // initializing
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        // create job
        Job syncWeatherJob = dispatcher.newJobBuilder()
                .setService(NewsFirebaseJobService.class)
                .setTag(NEWS_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FIX_TIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncWeatherJob);
    }
    /**
     * It helps to immediately begain our sync and get the news data that will be stored in database
     *
     * @param context
     */
    synchronized public static void initialized(@NonNull final Context context) {
        if (sInitialized) {
            return;
        }
        sInitialized = true;
        // gets the weather data in 4 hours
        scheduleFirebaseJobDispatcher(context);
        //if we don't have data present we check it through cursor and run our service
        // the weather data should not be query on the ui thread so we must create the thread that can handle it
        Thread checkThread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Uri forcastUri = NewsContract.NewsEntry.CONTENT_URI;
                String[] projectionColumn = {NewsContract.NewsEntry._ID};
                Cursor cursor = context.getContentResolver().query(
                        forcastUri,
                        projectionColumn,
                        null,
                        null,
                        null);
                // check cursor for validation
                if (cursor == null || cursor.getCount() == 0) {
                    Log.e(TAG,"news is syncing");
                    // immediately sync the News data
                    startImmediatelySync(context);
                }
                cursor.close();
            }
        });
        checkThread.start();
    }

    /**
     * Helps to immediately run the get weather data task
     *
     * @param context
     */
    public static void startImmediatelySync(@NonNull final Context context ) {
        Intent intentImmediately = new Intent(context, NewsSyncIntentService.class);
        String searchType = NewsLocationPreferences.getPrefTypeSearch(context);
        intentImmediately.putExtra("searchType",searchType);
        context.startService(intentImmediately);
    }
}
