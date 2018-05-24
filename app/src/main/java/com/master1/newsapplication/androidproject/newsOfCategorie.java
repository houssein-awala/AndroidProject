package com.master1.newsapplication.androidproject;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class newsOfCategorie extends Fragment{
    ViewGroup viewGroup;
    public newsOfCategorie() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewGroup=container;
        container.removeAllViews();
        return inflater.inflate(R.layout.news_of_categorie,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle=getArguments();
        String name=bundle.getString("name");
        TextView textView=(TextView)viewGroup.findViewById(R.id.nameOfCategorie);
        textView.setText(name);
    }
}
