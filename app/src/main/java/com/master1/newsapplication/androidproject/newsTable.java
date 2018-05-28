package com.master1.newsapplication.androidproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class newsTable extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="ANDROID_PROJECT";
    private static final String NEWS_TABLE="NEWS";
    private static final String KEY_ID="id";
    private static final String KEY_CATEGORIE="categorie";
    private static final String KEY_AUTHOR="author";
    private static final String KEY_DATE="date";
    private static final String KEY_TITLE="title";
    private static final String KEY_TEXT="text";
    private static final String KEY_MAIN_PHOTO="photo";
    private static final String KEY_PHOTOS="photos";

    public newsTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NEWS_TABLE=
                "CREATE TABLE "+NEWS_TABLE+" ("+KEY_ID+" TEXT, "+KEY_CATEGORIE+" TEXT, "+KEY_AUTHOR+" TEXT, "
                +KEY_TITLE+" TEXT, "+KEY_TEXT+" TEXT, "+KEY_DATE+" TEXT, "+KEY_MAIN_PHOTO+" TEXT, "+KEY_PHOTOS+" " +
                 " TEXT, PRIMARY KEY ("+KEY_ID+", "+KEY_CATEGORIE+"))";
        db.execSQL(CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNews(FullNews news)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_ID,news.getId());
        values.put(KEY_AUTHOR,news.getAuthor());
        values.put(KEY_CATEGORIE,news.getCategorie());
        values.put(KEY_DATE,news.getDate().toString());
        values.put(KEY_TITLE,news.getTitle());
        values.put(KEY_TEXT,news.getNewsText());
        values.put(KEY_MAIN_PHOTO,news.getPathMainPhot());
        values.put(KEY_PHOTOS,news.getPathPhotos());
        db.insert(NEWS_TABLE,null,values);
        db.close();
    }

    public FullNews getNewsByIdAndCategorie(int id,String categorie)
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(NEWS_TABLE,new String[]{KEY_ID,KEY_TITLE,KEY_DATE,KEY_AUTHOR,KEY_TEXT,KEY_CATEGORIE,KEY_MAIN_PHOTO,KEY_PHOTOS,
        },KEY_ID+" = ? AND "+KEY_CATEGORIE+" = ? ",new String[]{String.valueOf(id),categorie},null,null,
                null);
        if (cursor!=null)
        {
            cursor.moveToFirst();
            FullNews news=new FullNews(cursor.getString(0),cursor.getString(1),
                    new Date(cursor.getString(2)),cursor.getString(3),cursor.getString(4),
                    cursor.getString(5),cursor.getString(6),cursor.getString(7));
            db.close();
            return news;
        }
        db.close();
        return null;
    }

    public ArrayList<FullNews> getAllNewOfCategorie(String categorie)
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(NEWS_TABLE,new String[]{KEY_ID,KEY_TITLE,KEY_DATE,KEY_AUTHOR,KEY_TEXT,KEY_CATEGORIE,KEY_MAIN_PHOTO,KEY_PHOTOS,
                },KEY_CATEGORIE+" = ? ",new String[]{categorie},null,null, KEY_DATE );
        ArrayList<FullNews> news=new ArrayList<>();
        if (cursor.moveToFirst())
        {
            do {
                FullNews news1=new FullNews(cursor.getString(0),cursor.getString(1),
                        new Date(cursor.getString(2)),cursor.getString(3),cursor.getString(4),
                        cursor.getString(5),cursor.getString(6),cursor.getString(7));
                news.add(news1);
            }while (cursor.moveToNext());

        }
        db.close();
        return news;
    }

    public Date getMaxDate(String categorie)
    {
        SQLiteDatabase db=getReadableDatabase();
        Date max=null;
        Cursor cursor=db.query(NEWS_TABLE,new String[]{KEY_DATE},KEY_CATEGORIE+" = ?",new String[]{categorie},null,null,null);
        if (cursor.moveToFirst())
        {
            max=new Date(cursor.getString(0));

            while (cursor.moveToNext()) {
                Date date;
                date=new Date(cursor.getString(0));
                if(date.after(max))
                    max=date;
            }
        }
        return max;
    }
}
