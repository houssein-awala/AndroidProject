package com.master1.newsapplication.androidproject;

import java.net.URL;
import java.util.Date;

public class News {
    protected String title;
    protected String text;
    protected Date date;
    protected URL image;
    public News(String title, String text, Date date,URL image) {
        this.image = image;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public News() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", image=" + image +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }
}
