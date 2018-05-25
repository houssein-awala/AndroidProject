package com.master1.newsapplication.androidproject;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class newsOfCategorie extends Fragment{
    String categorieName;
    private ArrayList<News> list;
    private NewsAdapter adapter;
    private FirebaseDatabase database;
    private HashMap<String,News> map;
    private FirebaseFirestore db;
    public newsOfCategorie() {
        list=new ArrayList<>();
        map=new HashMap<>();
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
        categorieName=bundle.getString("name");
        TextView textView=(TextView)view.findViewById(R.id.nameOfCategorie);
        textView.setText(categorieName);
        adapter=new NewsAdapter(getActivity(),R.layout.news_of_categorie,list);
        ListView listView=(ListView)view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        getAllNews(adapter);
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

        db.collection("categories")
                .document(categorieName)
                .collection("News")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QuerySnapshot documentSnapshots=task.getResult();

                            for(DocumentSnapshot d:documentSnapshots.getDocuments())
                            {
                                if (map.containsKey(d.getId())) {

                                } else {
                                    News news = null;
                                    news = new News(d.getString("title"), d.getString("text"), d.getDate("date"), null);
                                    list.add(news);
                                    map.put(d.getId(),news);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        } else {
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
