package com.master1.newsapplication.androidproject;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class NewsDetails extends Fragment
{
    FullNews news;
    TextView title,date,author,text;
    ImageView mainPhoto;
    LinearLayout photos;
    public NewsDetails(){super();}

    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState)
    {
        View view=inflater.inflate(R.layout.news_details,container,false);
        Bundle bundle=getArguments();
        news=(FullNews) bundle.getSerializable("news");
            title=view.findViewById(R.id.detailsTitle);
            title.setText(news.getTitle());
            date=view.findViewById(R.id.detailsDate);
            date.setText(news.getDate().toString());
            author=view.findViewById(R.id.detailsAuthor);
            text=view.findViewById(R.id.detailsText);
            text.setText(news.getNewsText());
        try {
            new Storage(getContext(),news).DownloadMain();
            mainPhoto=view.findViewById(R.id.detailsMainPhoto);
            mainPhoto.setImageBitmap(news.getMainPhoto());
        } catch (IOException e) {
            e.printStackTrace();
        }

            photos=view.findViewById(R.id.detailsPhotos);
            //getPhotos();
        return view;
    }

    private void getPhotos()
    {
        for(Bitmap b:news.getPhotos())
        {
            ImageView temp=new ImageView(getActivity());
            temp.setImageBitmap(b);
            temp.setAdjustViewBounds(true);
            temp.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            temp.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT));
            photos.addView(temp);
        }
    }
}
