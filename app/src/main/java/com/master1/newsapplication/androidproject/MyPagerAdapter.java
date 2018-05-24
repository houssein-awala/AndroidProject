package com.master1.newsapplication.androidproject;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public class MyPagerAdapter extends FragmentPagerAdapter {


    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        Bundle bundle=new Bundle();
        newsOfCategorie categorie=new newsOfCategorie();
        switch (position)
        {
            case 0:
            {
                bundle.putString("name","Sport");
                break;
            }
            case 1:
            {
                bundle.putString("name","economie");
                break;
            }
            case 2:
            {
                bundle.putString("name","absar");
                break;
            }

        }
        categorie.setArguments(bundle);
        return categorie;
    }
    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {
        return "test";
    }
}
