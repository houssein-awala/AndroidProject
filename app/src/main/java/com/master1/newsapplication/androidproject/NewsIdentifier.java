package com.master1.newsapplication.androidproject;

public class NewsIdentifier {
    private String id;
    private String categorie;

    public NewsIdentifier(String id, String categorie) {
        this.id = id;
        this.categorie = categorie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
