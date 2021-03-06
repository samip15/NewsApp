package com.example.newsapp.sync;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class NewsFirebaseJobService extends JobService {
    private static final String TAG = "NewsFirebaseJobService";

    private AsyncTask<Void,Void,Void> mFetchWeatherTask;

    @Override
    public boolean onStartJob(@NonNull final JobParameters jobParameters) {
        Log.e(TAG,"weather is syncnking");
        mFetchWeatherTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                NewsSyncTask.syncNews(getApplicationContext());
                NewsSyncTask.syncEverythingNews(getApplicationContext());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobParameters,false);
            }
        };
        mFetchWeatherTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters job) {
        if (mFetchWeatherTask!=null){
            mFetchWeatherTask.cancel(true);
        }
        return true;
    }
}
