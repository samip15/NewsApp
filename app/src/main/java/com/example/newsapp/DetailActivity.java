package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapp.Model.NewsItem;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_NEWS = "news";
    private static final String HAS_TAG = "# Indian News";
    NewsItem newsItem;
    TextView titleTv, descriptionTv, mAuthor, mSources;
    ImageView detailImage;
    String newsTitle, newsDescription,newsSources,newsAuthor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleTv = findViewById(R.id.title_tv_detail);
        descriptionTv = findViewById(R.id.discryption_tv_detail);
        detailImage = findViewById(R.id.img_iv_detail);
        mAuthor = findViewById(R.id.author);
        mSources = findViewById(R.id.source_name);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_NEWS)) {
                newsItem = intent.getParcelableExtra(EXTRA_NEWS);
                newsSources = newsItem.getSourceName();
                mSources.setText(newsSources);
                newsAuthor = newsItem.getAuthor();
                mAuthor.setText(newsAuthor);
                newsTitle = newsItem.getTitle();
                titleTv.setText(newsTitle);
                newsDescription = newsItem.getDescription();
                descriptionTv.setText(newsDescription);
                Picasso.get().load(newsItem.getImgUrl()).into(detailImage);
            }
        }
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
}