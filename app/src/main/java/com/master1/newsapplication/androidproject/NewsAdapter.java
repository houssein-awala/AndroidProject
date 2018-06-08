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
    ArrayList<FullNews> objects;

    public NewsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FullNews> objects) {
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
        //TextView id=(TextView)rowView.findViewById(R.id.id_news);
        ImageView image=(ImageView)rowView.findViewById(R.id.image);
        title.setText(objects.get(position).getTitle());
        text.setText(objects.get(position).getNewsText());
        date.setText(objects.get(position).getDate().toString());
        //System.out.println("hhhhhhhhhhhhh"+objects.get(position).getPathMainPhot());
       // if (!(objects.get(position).getPathMainPhot()).equals(""))
        {
            try {
                Storage storage = new Storage(getContext(), objects.get(position));
                storage.DownloadMain();
                if (objects.get(position).getMainPhoto() != null)
                    image.setImageBitmap(objects.get(position).getMainPhoto());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        //id.setText(objects.get(position).getId());
        //id.setVisibility(View.INVISIBLE);
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
