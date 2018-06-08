package com.master1.newsapplication.androidproject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {

    ArrayList<String> list;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        /*list=new ArrayList<>();
        list.add("sport");
        list.add("politics");
        list.add("test");
        list.add("bati5");*/
        list=MainActivity.categories;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        Bundle bundle=new Bundle();
        newsOfCategorie categorie=new newsOfCategorie();
        bundle.putString("name",list.get(position));
        categorie.setArguments(bundle);
        return categorie;
    }
    @Override
    public int getCount() {
        return list.size()>3?3:list.size();
    }

    public CharSequence getPageTitle(int position) {
       return list.get(position).toUpperCase();
    }
}
