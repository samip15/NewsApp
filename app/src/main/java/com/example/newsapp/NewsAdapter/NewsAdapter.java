package com.example.newsapp.NewsAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.NewsItems.NewsItem;
import com.example.newsapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    List<NewsItem> mNewsItem;
    // setter for news data
    public void setNewsData(List<NewsItem> newsItem) {
        this.mNewsItem = newsItem;
        notifyDataSetChanged();
    }

    public NewsAdapter(List<NewsItem> news) {
        this.mNewsItem = news;
    }


    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {

        NewsItem news = mNewsItem.get(position);
        String title = news.getTitle();
        String description = news.getDescription();
        String imageNews = news.getImgUrl();
        String date = news.getDate();
        String[] dateTime = date.split("T");
        String convertedDT = dateTime[0] + " " + dateTime[1].replace("Z", "");
        holder.titleTextView.setText(title);
        holder.tvDescription.setText(description);
        holder.tvDate.setText(convertedDT);
        Picasso.get().load(imageNews).placeholder(R.mipmap.ic_launcher).into(holder.ivNewsImage);

    }

    @Override
    public int getItemCount() {
        if (mNewsItem == null) {
            return 0;
        } else {
            return  mNewsItem.size();
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, tvDescription, tvDate;
        ImageView ivNewsImage;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNewsImage = itemView.findViewById(R.id.img_iv);
            titleTextView = itemView.findViewById(R.id.title_tv);
            tvDescription = itemView.findViewById(R.id.discryption_tv);
            tvDate = itemView.findViewById(R.id.date_tv);
        }
    }
}
