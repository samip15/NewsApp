package com.example.newsapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsItem implements Parcelable {
    String title;
    String description;
    String imgUrl;
    String date;

    public NewsItem(String title, String description, String imgUrl, String date) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    protected NewsItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        imgUrl = in.readString();
        date = in.readString();
    }

    public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String title) {
        this.title = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(imgUrl);
        dest.writeString(date);
    }
}
