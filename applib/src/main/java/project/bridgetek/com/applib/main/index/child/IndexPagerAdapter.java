package project.bridgetek.com.applib.main.index.child;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import project.bridgetek.com.applib.main.index.child.childpager.OverFragment;
import project.bridgetek.com.applib.main.index.child.childpager.SnapFragment;
import project.bridgetek.com.applib.main.index.child.childpager.UpcomingFragment;

public class IndexPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles;

    public IndexPagerAdapter(FragmentManager fm, String... titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return UpcomingFragment.newInstance(mTitles[position]);
            case 1:
                return OverFragment.newInstance(mTitles[position]);
            case 2:
                return SnapFragment.newInstance(mTitles[position]);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
