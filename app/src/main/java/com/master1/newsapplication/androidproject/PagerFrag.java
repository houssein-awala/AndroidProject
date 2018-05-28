package com.master1.newsapplication.androidproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PagerFrag extends Fragment {

    public PagerFrag(){super();}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pager_frag,container,false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager pager = (ViewPager) view.findViewById(R.id.viewPager);
        setupWithViewPager(pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
    }
    public void onStart()
    {
        super.onStart();

    }
    public void setupWithViewPager(ViewPager pager)
    {
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
    }
}
