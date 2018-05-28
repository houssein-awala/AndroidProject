package com.master1.newsapplication.androidproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final newsTable table=new newsTable(getContext());
                FullNews news=(FullNews) parent.getItemAtPosition(position);
                getNews(news.getId(),categorieName);
            }
        });
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

    public void getNews(String id, final String categorieName)
    {

        db.collection("categories")
                .document(categorieName)
                .collection("News")
                .whereEqualTo(FieldPath.documentId(),id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QuerySnapshot documentSnapshots=task.getResult();

                            for(DocumentSnapshot d:documentSnapshots.getDocuments())
                            {
                                FullNews news1;
                                news1 = new FullNews(d.getId(), d.getString("title"), d.getDate("date"), "", d.getString("text"), categorieName,"","");
                                ((MainActivity)getActivity()).showDetails(news1);

                            }

                        } else {
                            Log.w("Error", "Error getting documents.", task.getException());
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void getAllNews(final NewsAdapter adapter)
    {
        if (categorieName==null)
            return;

        final newsTable table=new newsTable(getContext());
        ArrayList<FullNews> fromDB=table.getAllNewOfCategorie(categorieName);
        for (FullNews news:fromDB)
        {
            if (!keys.contains(news.getId())) {
                list.add(0,news);
                keys.add(news.getId());
            }
        }
        adapter.notifyDataSetChanged();
        Query q;
        if(table.getMaxDate(categorieName)!=null)
        {
            q=db.collection("categories")
                    .document(categorieName)
                    .collection("News")
                    .whereGreaterThan("date",table.getMaxDate(categorieName) )
                    .orderBy("date");
        }
        else {
            q=db.collection("categories")
                    .document(categorieName)
                    .collection("News")
                    .orderBy("date");
        }
                q.get()
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

}
}
