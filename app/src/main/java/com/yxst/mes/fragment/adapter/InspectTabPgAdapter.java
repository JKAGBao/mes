package com.yxst.mes.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.yxst.mes.fragment.inspect.BaseFragment;
import java.util.List;

public class InspectTabPgAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    public InspectTabPgAdapter(FragmentManager fm,List<Fragment> fragmentList) {
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
