package project.bridgetek.com.applib.main.index.child;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.yokeyword.fragmentation.SupportFragment;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.AppActivity;
import project.bridgetek.com.applib.main.bean.TaskInfo;
import project.bridgetek.com.applib.main.index.child.childpager.OverFragment;
import project.bridgetek.com.applib.main.index.child.childpager.SnapFragment;
import project.bridgetek.com.applib.main.index.child.childpager.UpcomingFragment;
import project.bridgetek.com.applib.main.index.child.entity.TabEntity;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.bridgelib.app.Black;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

public class ViewPagerFragment extends SupportFragment {
    private Context mContext = Black.getApplicationContext();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = null;
    private int[] mIconUnselectIds = {
            R.mipmap.login_account, R.mipmap.login_account,
            R.mipmap.login_account};
    private int[] mIconSelectIds = {
            R.mipmap.login_account, R.mipmap.login_account,
            R.mipmap.login_account};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private MyPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout_2;
    Random mRandom = new Random();
    private TextView mTextview;
    private ImageView mImgTab;
    private BlackDao mBlackDao;
    private LocalUserInfo mUserInfo;
    private String mAccountid;
    private List<TaskInfo> mTaskInfos = new ArrayList<>();
    private static final String ARG_TYPE = "3";
    private List<String> permissions;

    public static ViewPagerFragment newInstance() {
        Bundle args = new Bundle();
        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delegate_viewpagerfragment, container, false);
        mUserInfo = LocalUserInfo.getInstance(mContext);
        permissions = mUserInfo.getDataList(Constants.PERMISSIONS);
        boolean isFreePatrol = false;
        for (int i = 0; i < permissions.size(); i++) {
            if (Constants.FREEPATROL.equals(permissions.get(i))) {
                isFreePatrol = true;
            }
        }
        if (!isFreePatrol) {
            mTitles = new String[]{getString(R.string.app_mybottom_viewpager_title_text), getString(R.string.app_mybottom_viewpage_title_text2)};
            mFragments.add(UpcomingFragment.newInstance(mTitles[0]));
            mFragments.add(OverFragment.newInstance(mTitles[1]));
        } else {
            mTitles = new String[]{getString(R.string.app_mybottom_viewpager_title_text), getString(R.string.app_mybottom_viewpage_title_text2), getString(R.string.app_mybottom_viewpage_title_text3)};
            mFragments.add(UpcomingFragment.newInstance(mTitles[0]));
            mFragments.add(OverFragment.newInstance(mTitles[1]));
            AppActivity.snapFragment = SnapFragment.newInstance(mTitles[2]);
            mFragments.add(AppActivity.snapFragment);
        }
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mViewPager = view.findViewById(R.id.vp_content);
        mAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout_2 = view.findViewById(R.id.ctl_title);
        mTabLayout_2.setupWithViewPager(mViewPager);
        tl_2();
        reflex(mTabLayout_2);
        setTab();
        mBlackDao = BlackDao.getInstance(mContext);
        mAccountid = mUserInfo.getUserInfo(Constants.ACCOUNTID);

        //设置未读消息红点
        // mTabLayout_2.showDot(1);
        //  MsgView rtv_2_2 = mTabLayout_2.getMsgView(2);
//        if (rtv_2_2 != null) {
//            UnreadMsgUtils.setSize(rtv_2_2, dp2px(7.5f));
//        }
        return view;
    }

    public void setTab() {
        TabLayout.Tab at = mTabLayout_2.getTabAt(1);
        View tabView = LayoutInflater.from(mContext).inflate(R.layout.item_tab_layout, null);
        mTextview = tabView.findViewById(R.id.tv_tab);
        mImgTab = tabView.findViewById(R.id.img_tab);
        mTextview.setTypeface(HiApplication.MEDIUM);
        at.setCustomView(tabView);
    }

    @Override
    public void onResume() {
        super.onResume();
        // mContrast = mBlackDao.getContrast(mAccountid);
        mTaskInfos = mBlackDao.getOverTask(0, 20, ARG_TYPE, mAccountid);
        int count = 0;
        for (int i = 0; i < mTaskInfos.size(); i++) {
            int overUpload = mBlackDao.getOverUpload(mTaskInfos.get(0).getTaskID());
            count = count + overUpload;
        }
        if (count > 0) {
            mImgTab.setVisibility(View.VISIBLE);
        } else {
            mImgTab.setVisibility(View.GONE);
        }
    }

    //设置下划线和字体
    public void reflex(final TabLayout tabLayout) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = dp2px(22);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
                        //拿到tabView的mTextView属性
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);
                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        mTextView.setTypeface(HiApplication.MEDIUM);
                        tabView.setPadding(0, 0, 0, 0);
                        int width = 0;
                        width = dp2px(80);
                        //设置tab左右间距为22dp
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void tl_2() {
        mTabLayout_2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 1) {
                    mTextview.setTextColor(getResources().getColor(R.color.text_color));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 1) {
                    mTextview.setTextColor(getResources().getColor(R.color.percentage));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
