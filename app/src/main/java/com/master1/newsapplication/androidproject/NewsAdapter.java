package com.master1.newsapplication.androidproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter {
    private final Context context;
    private final int resourceID;
    ArrayList<News> objects;

    public NewsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<News> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceID = resource;
        this.objects=objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.news_in_list,null);
        TextView title=(TextView)rowView.findViewById(R.id.title);
        TextView text=(TextView)rowView.findViewById(R.id.text);
        TextView date=(TextView)rowView.findViewById(R.id.date);
        ImageView image=(ImageView)rowView.findViewById(R.id.image);
        title.setText(objects.get(position).getTitle());
        text.setText(objects.get(position).getText());
        date.setText(objects.get(position).getDate().toString());

       /* Storage s=new Storage(context);
        try {
            s.getImage(image);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/

        return rowView;
    }
}
