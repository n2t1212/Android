package com.mimi.mimigroup.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

//import com.npc.grc.app.App;

import java.util.ArrayList;
import java.util.List;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList;

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
        notifyDataSetChanged();
    }


}
