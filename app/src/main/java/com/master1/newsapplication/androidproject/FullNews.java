package com.master1.newsapplication.androidproject;

import android.graphics.Bitmap;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class FullNews
{
    private int id;
    private String title;
    private String date;
    private String auhor;
    private Bitmap mainPhoto;
    private ArrayList<Bitmap> photos;
    private String newsText;

    public FullNews(int id, String title, String date, String auhor, String newsText, Bitmap mainPhoto, ArrayList<Bitmap> photos) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.auhor = auhor;
        this.mainPhoto = mainPhoto;
        this.photos=new ArrayList<>(photos);
        this.newsText = newsText;
    }

    public FullNews(int id, String title, String date, String auhor, String newsText, Bitmap mainPhoto) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.auhor = auhor;
        this.mainPhoto = mainPhoto;
        this.newsText = newsText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuhor() {
        return auhor;
    }

    public void setAuhor(String auhor) {
        this.auhor = auhor;
    }

    public Bitmap getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(Bitmap mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public ArrayList<Bitmap> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Bitmap> photos) {
        this.photos = new ArrayList<>(photos);
    }

    public String getNewsText() {
        return newsText;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
    }

    public void addPhoto(Bitmap photo)
    {
        photos.add(photo);
    }

    public void clearPhotos()
    {
        photos.clear();
    }
}
