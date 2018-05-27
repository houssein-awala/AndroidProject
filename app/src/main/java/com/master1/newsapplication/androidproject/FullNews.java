package com.master1.newsapplication.androidproject;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class FullNews implements Serializable
{
    private String id;
    private String title;
    private Date date;
    private String author;
    private String categorie;
    private Bitmap mainPhoto;
    private ArrayList<Bitmap> photos;
    private String newsText;
    private ArrayList<Comment> comments;
    private String pathMainPhot;
    private String pathPhotos;


    public FullNews(String id, String title, Date date, String author, String newsText, String categorie, String pathMainPhoto, String pathPhotos) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.author = author;
        this.categorie = categorie;
        this.pathMainPhot =pathMainPhoto;
        this.pathPhotos = pathPhotos;
        this.newsText = newsText;
        this.comments = new ArrayList<>();
        //create bitmap from path;
    }

    public String getPathMainPhot() {
        return pathMainPhot;
    }

    public void setPathMainPhot(String pathMainPhot) {
        this.pathMainPhot = pathMainPhot;
    }

    public String getPathPhotos() {
        return pathPhotos;
    }

    public void setPathPhotos(String pathPhotos) {
        this.pathPhotos = pathPhotos;
    }

    public FullNews(String id, String title, Date date, String author, String newsText, String pathPhotos) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.author = author;
        this.pathPhotos = pathPhotos;
        this.newsText = newsText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public void addComment(Comment comment){ comments.add(comment); }

    public String getCategorie() { return categorie; }

    public void setCategorie(String categorie) { this.categorie = categorie; }

    @Override
    public String toString() {
        return "FullNews{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", author='" + author + '\'' +
                ", categorie='" + categorie + '\'' +
                ", newsText='" + newsText + '\'' +
                ", comments=" + comments +
                ", pathMainPhot='" + pathMainPhot + '\'' +
                ", pathPhotos='" + pathPhotos + '\'' +
                '}';
    }
}
