package com.example.newsapp;

public class NewsItem {
    String title;
    String descryption;
    String imgurl;
    String date;

    public NewsItem(String title, String descryption, String imgurl,String date) {
        this.title = title;
        this.descryption = descryption;
        this.imgurl = imgurl;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescryption() {
        return descryption;
    }

    public void setDescryption(String descryption) {
        this.descryption = descryption;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDate() {
        return title;
    }

    public void setDate(String title) {
        this.title = title;
    }
}