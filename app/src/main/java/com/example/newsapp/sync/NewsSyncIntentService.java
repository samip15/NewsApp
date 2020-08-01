package com.example.newsapp.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

public class NewsSyncIntentService extends IntentService {
    private static final String TAG = "NewsSyncIntentService";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public NewsSyncIntentService() {
        super("NewsSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(TAG,"News is syncing");
        NewsSyncTask.syncWeather(this);
    }
}
