package com.mimi.mimigroup.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentPagerTabAdapter extends FragmentStatePagerAdapter {


     List<Fragment> fragmentList;
     List<String> titles;

    public FragmentPagerTabAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        titles.add(title);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
