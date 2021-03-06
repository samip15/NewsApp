package com.example.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.Model.NewsItem;
import com.squareup.picasso.Picasso;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private static final String TAG = "NewsAdapter";
    private Cursor mCursor;
    private final Context mContext;
    // onclick for adapter
    private final NewsAdapterOnclickListner mOnclickListnrer;

    // -------------------------Rv on click listener-------------------
    public interface NewsAdapterOnclickListner {
        void onClick(long idDate);
    }

    public NewsAdapter(Context context, NewsAdapterOnclickListner listner) {
        this.mContext = context;
        this.mOnclickListnrer = listner;
    }

    // =========================Cursor Function===========================
    // swap cursor for new weather data
    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_item, parent, false);
        view.setFocusable(true);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        // getting all columns values
        String date = mCursor.getString(MainActivity.INDEX_NEWS_DATE);
        String[] dateTime = date.split("T");
        String convertedDT = dateTime[0] + " " + dateTime[1].replace("Z","");
        String title =
                mCursor.getString(MainActivity.INDEX_NEWS_TITLE);
        String description = mCursor.getString(MainActivity.INDEX_NEWS_DESC);
        String imageUrl = mCursor.getString(MainActivity.INDEX_NEWS_IMAGE_URL);
        holder.titleTextView.setText(title);
        holder.tvDescription.setText(description);
        holder.tvDate.setText(convertedDT);
        if (imageUrl == null) {
            holder.ivNewsImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Picasso.get().load(imageUrl).into(holder.ivNewsImage);
        }

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView, tvDescription, tvDate;
        ImageView ivNewsImage;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNewsImage = itemView.findViewById(R.id.img_iv);
            titleTextView = itemView.findViewById(R.id.title_tv);
            tvDescription = itemView.findViewById(R.id.discryption_tv);
            tvDate = itemView.findViewById(R.id.date_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterposition = getAdapterPosition();
            mCursor.moveToPosition(adapterposition);
            long idDate = mCursor.getLong(MainActivity.INDEX_NEWS_ID);
            mOnclickListnrer.onClick(idDate);
        }
    }
}
