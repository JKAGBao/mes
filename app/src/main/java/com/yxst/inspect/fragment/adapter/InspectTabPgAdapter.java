package com.yxst.inspect.fragment.adapter;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;


public class InspectTabPgAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    public InspectTabPgAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        Log.e("size","getItem="+mFragmentList.get(i));
        return mFragmentList!=null&&i<mFragmentList.size()?mFragmentList.get(i):null;
    }

    @Override
    public int getCount() {
        Log.e("size","getCount="+mFragmentList.size());
        return mFragmentList==null ? 0 : mFragmentList.size();
    }


}
