package project.bridgetek.com.applib.main.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by bridge on 18-7-2.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mlist;
    private String tabTitles[];
    private Context context;

    public MyFragmentAdapter(FragmentManager fm, Context context, List<Fragment> fragments, String[] tabTitle) {
        super(fm);
        this.context = context;
        this.mlist = fragments;
        this.tabTitles = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
