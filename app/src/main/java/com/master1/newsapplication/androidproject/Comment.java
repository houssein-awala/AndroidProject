package com.master1.newsapplication.androidproject;

public class Comment {
    private int id;
    private int newsId;
    private String categorie;
    private String author;
    private String text;
    private String date;


    public Comment(int id, int newsId, String categorie, String author, String text, String date) {
        this.id = id;
        this.newsId = newsId;
        this.author = author;
        this.text = text;
        this.date = date;
        this.categorie = categorie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
