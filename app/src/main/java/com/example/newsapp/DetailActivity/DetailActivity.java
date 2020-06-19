package com.example.newsapp.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapp.NewsItems.NewsItem;
import com.example.newsapp.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_NEWS = "news";

    NewsItem newsItem;

    TextView titleTv, descriptionTv;
    ImageView detailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleTv = findViewById(R.id.title_tv_detail);
        descriptionTv = findViewById(R.id.discryption_tv_detail);
        detailImage = findViewById(R.id.img_iv_detail);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_NEWS)) {
                newsItem = intent.getParcelableExtra(EXTRA_NEWS);
                titleTv.setText(newsItem.getTitle());
                descriptionTv.setText(newsItem.getDescription());
                Picasso.get().load(newsItem.getImgUrl()).into(detailImage);
            }
        }
    }
}