package com.example.newsapp.NewsItems;

public class NewsItem {
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
}
