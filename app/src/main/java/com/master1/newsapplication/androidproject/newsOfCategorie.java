package com.master1.newsapplication.androidproject;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class newsOfCategorie extends Fragment{
    String categorieName;
    private ArrayList<FullNews> list;
    private NewsAdapter adapter;
    private FirebaseDatabase database;
    private HashSet<String> keys;
    private FirebaseFirestore db;
    public newsOfCategorie() {
        list=new ArrayList<>();
        keys=new HashSet<>();
        db = FirebaseFirestore.getInstance();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_of_categorie,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle=getArguments();
        categorieName=bundle.getString("name");;
        adapter=new NewsAdapter(getActivity(),R.layout.news_of_categorie,list);
        ListView listView=(ListView)view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        getAllNews(adapter);
        SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllNews(adapter);
            }
        });
    }

    public void getAllNews(final NewsAdapter adapter)
    {
        if (categorieName==null)
            return;
        /*final DocumentReference docRef = db.collection("categories")
                .document(categorieName)
                .collection("News").document();
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    if (map.containsKey(snapshot.getId())) {

                    } else {
                        News news = null;
                        news = new News(snapshot.getString("title"), snapshot.getString("text"), snapshot.getDate("date"), null);
                        list.add(news);
                        map.put(snapshot.getId(), news);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    Log.d("", "Current data: null");
                }
            }
        });*/
        final newsTable table=new newsTable(getContext());
        // table.insertNews(new FullNews(1,"title","2015-04-01","HUSSEIN AWALA","TEXT","sport","/","/"));
        ArrayList<FullNews> fromDB=table.getAllNewOfCategorie(categorieName);
        for (FullNews news:fromDB)
        {
            if (!keys.contains(news.getId())) {
                list.add(0,news);
                keys.add(news.getId());
            }
        }
        adapter.notifyDataSetChanged();
        db.collection("categories")
                .document(categorieName)
                .collection("News")
                .whereGreaterThan("date",table.getMaxDate(categorieName) )
                .orderBy("date")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QuerySnapshot documentSnapshots=task.getResult();

                            for(DocumentSnapshot d:documentSnapshots.getDocuments())
                            {
                                if (!keys.contains(d.getId())) {
                                    FullNews news = null;
                                    news = new FullNews(d.getId(), d.getString("title"), d.getDate("date"), "", d.getString("text"), categorieName,"","");
                                    System.out.println(d.getDate("date"));
                                    list.add(0, news);
                                    keys.add(d.getId());
                                   table.insertNews(news);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            if(getView()!=null) {
                                SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe);
                                if (swipeRefreshLayout.isRefreshing())
                                    swipeRefreshLayout.setRefreshing(false);
                            }
                        } else {
                            if (getView()!=null) {
                                SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe);
                                if (swipeRefreshLayout.isRefreshing())
                                    swipeRefreshLayout.setRefreshing(false);
                            }
                            Log.w("Error", "Error getting documents.", task.getException());
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        /*myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //Toast.makeText(MainActivity.this, "xxxx", Toast.LENGTH_SHORT).show();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    if (map.containsKey(messageSnapshot.getKey())) {

                    } else {
                        String title = (String) messageSnapshot.child("title").getValue();
                        String text = (String) messageSnapshot.child("text").getValue();
                        String date = (String) messageSnapshot.child("date").getValue();
                        URL image = null;
                        try {
                        //    image = new URL((String) messageSnapshot.child("image").getValue());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        News temp = new News(title, text, new Date(), image);
                        map.put(messageSnapshot.getKey(), temp);
                        list.add(0, temp);
                        adapter.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
}
}
