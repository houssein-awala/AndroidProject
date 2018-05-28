package com.master1.newsapplication.androidproject;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ArrayList<News> list;
    private NewsAdapter adapter;
    private FirebaseDatabase database;
    private HashMap<String,News> map;
    private ArrayList<String> nameOfCategoriesFromFirebase;
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
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

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
        addMenuItemInNavMenuDrawer();

        //SETUP THE PAGER
        PagerFrag frag=new PagerFrag();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction trans=manager.beginTransaction();
        trans.replace(R.id.Fullcontainer,frag);
        trans.commit();


        //test db

        newsTable table=new newsTable(this);
        table.insertNews(new FullNews("1","title",new Date(2018,05,12,0,0,0),"HUSSEIN AWALA","TEXT","sport","/","/"));
        //Toast.makeText(this, table.getAllNewOfCategorie("sport").get(0).toString(), Toast.LENGTH_SHORT).show();

        //end test
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        nameOfCategoriesFromFirebase = getName();
        String t = item.getTitle().toString();
        for (String categorie : nameOfCategoriesFromFirebase) {
            if (t == categorie) {
                Toast.makeText(this, categorie, Toast.LENGTH_SHORT).show();
            } else {

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


                    }
                });


    }

    public void setupWithViewPager(ViewPager pager)
    {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
    }
    //methode mn database btrj3 arrayist mn lcategorie
    public ArrayList<String> getName()
    {
        ArrayList<String> categories=new ArrayList<>();
        categories.add("Sport");
        categories.add("Arts");
        categories.add("Policy");
        categories.add("Economie");
        return categories;
    }
    //methode pour remplir navigation view dynamic
    private void addMenuItemInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        Menu submenu = menu.addSubMenu("Categorie");
        ArrayList<String> categories=getName();
        int i=0;
        for(String categorie  : categories)
        {
            //   submenu.add(categorie);
            //    submenu.add(0, Integer.parseInt(categorie),1,categorie);
            submenu.add(R.id.gp,1,1,categorie);
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
        transaction.replace(R.id.Fullcontainer,details);
        transaction.addToBackStack(null);
        transaction.commit();
     }

}
