package com.master1.newsapplication.androidproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Set<String> nameOfCategoriesFromFirebase;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ArrayList<News> list;
    private NewsAdapter adapter;
    private FirebaseDatabase database;
    private HashMap<String,News> map;
    // release listener in onStop
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ...
        /* AUTHENTICATION
          *START
        */
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    System.out.println(user.getUid());
                    System.out.println("token "+ FirebaseInstanceId.getInstance().getToken());
                    final String address="http://172.20.10.3/firebase/addToken.php?token="+FirebaseInstanceId.getInstance().getToken();
                    Toast.makeText(MainActivity.this, address, Toast.LENGTH_SHORT).show();
                    
                    new Thread()
                    {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                System.out.println(address);
                                URL url=new URL(address);
                                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("GET");
                                System.out.println(connection.getResponseMessage());
                                connection.connect();
                            } catch (MalformedURLException e) {
                                System.out.println(e.getMessage());

                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }.start();
                    Log.d("signed_in", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("signed_out", "onAuthStateChanged:signed_out");
                }

            }
        };
        /* AUTHENTICATION
         * END
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //call methode


        //SETUP THE PAGER
        PagerFrag frag=new PagerFrag();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction trans=manager.beginTransaction();
        trans.replace(R.id.Fullcontainer,frag);
        trans.commit();


            //test db

        newsTable table=new newsTable(this);
        //table.insertNews(new FullNews("1","title",new Date(2018,05,12,0,0,0),"HUSSEIN AWALA","TEXT","sport","/","/"));
        //Toast.makeText(this, table.getAllNewOfCategorie("sport").get(0).toString(), Toast.LENGTH_SHORT).show();
        //end test
        //nameOfCategoriesFromFirebase=getPreferences(MODE_PRIVATE).getStringSet("categories",new HashSet<String>());
        //Toast.makeText(this, nameOfCategoriesFromFirebase.toString(), Toast.LENGTH_SHORT).show();
            nameOfCategoriesFromFirebase=new HashSet<>();
            db.collection("categories").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot documentSnapshots=task.getResult();
                            for(DocumentSnapshot d:documentSnapshots.getDocuments())
                            {
                                nameOfCategoriesFromFirebase.add(d.getId());
                               // Toast.makeText(MainActivity.this, d.getId(), Toast.LENGTH_SHORT).show();
                            }
                            /*if(getPreferences(MODE_PRIVATE).edit().putStringSet("categories",nameOfCategoriesFromFirebase).commit())
                            {
                                Toast.makeText(MainActivity.this, nameOfCategoriesFromFirebase.toString(), Toast.LENGTH_SHORT).show();
                            }*/
                            addMenuItemInNavMenuDrawer();
                        }
                    });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(MainActivity.this,ControlPanel.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        nameOfCategoriesFromFirebase = getName();
        String t = item.getTitle().toString();
        for (String categorie : nameOfCategoriesFromFirebase) {
            if (t.toLowerCase() .equals( categorie.toLowerCase())) {
                Bundle bundle=new Bundle();
                newsOfCategorie cat=new newsOfCategorie();
                bundle.putString("name",categorie);
                cat.setArguments(bundle);
                FragmentManager manager=getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.Fullcontainer, cat);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            }
        }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Complete", "OnComplete : " +task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("Failed", "Failed : ", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                           // System.out.println();
                        }


                    }
                });


    }

    public void setupWithViewPager(ViewPager pager)
    {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
    }
    //methode mn database btrj3 arrayist mn lcategorie
    public Set<String> getName()
    {
        return nameOfCategoriesFromFirebase;
    }
    //methode pour remplir navigation view dynamic
    private void addMenuItemInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        Menu submenu = menu.addSubMenu("Categorie");
        Set<String> categories=getName();
        int i=0;
        for(String categorie  : categories)
        {
            //   submenu.add(categorie);
            //    submenu.add(0, Integer.parseInt(categorie),1,categorie);
            submenu.add(R.id.gp,1,1,categorie.toUpperCase());
        }

        navView.invalidate();
    }
    public  void showDetails(FullNews news)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("news",news);
        NewsDetails details=new NewsDetails();
        details.setArguments(bundle);
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            transaction.replace(R.id.frag2, details);
        }
        else {

            transaction.replace(R.id.Fullcontainer, details);
            transaction.addToBackStack(null);
        }
        transaction.commit();
     }

}
