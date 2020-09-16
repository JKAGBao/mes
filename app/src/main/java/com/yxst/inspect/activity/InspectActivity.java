package com.yxst.inspect.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.yxst.inspect.R;
import com.yxst.inspect.fragment.FragmentInspect;
import com.yxst.inspect.fragment.FragmentInspectFinish;
import com.yxst.inspect.fragment.adapter.InspectTabPgAdapter;
import com.yxst.inspect.rx.manager.VisitManager;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InspectActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.vp_slide)
    ViewPager vpSlide;
    @BindView(R.id.tv_wait) TextView tvWait;
    @BindView(R.id.tv_finish) TextView tvFinish;
    private List<Fragment> mFragmentList;
    private InspectTabPgAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_inspect);
        setTitle("待检设备");
        ButterKnife.bind(this);
 //       VisitManager.pageRecord(this,this.getTitle().toString());
    }


    @Override
    public void onStart() {
        super.onStart();
        initData();
        adapter = new InspectTabPgAdapter(getSupportFragmentManager(),mFragmentList);
        vpSlide.setAdapter(adapter);
        vpSlide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetParams) {

                if(position==0){
                    tvWait.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    tvFinish.setTextColor(getResources().getColor(R.color.colorPrimary));
                }else if(position==1){
                    tvWait.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tvFinish.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onPageSelected(int i) {
                Log.e("position","onPageSelected:"+i);
//                vpSlide.setCurrentItem(i);

            }
            @Override
            public void onPageScrollStateChanged(int i) {
                Log.e("position","onPageScrollStateChanged:"+i);

            }
        });
    }

    private void initData(){
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(new FragmentInspect());
    //    mFragmentList.add(new FragmentInspectFinish());
        Log.e("size","initData="+mFragmentList.size());

    }
    @OnClick({R.id.tv_finish,R.id.tv_wait})
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_wait:
                vpSlide.setCurrentItem(0);
                break;
            case R.id.tv_finish:
                vpSlide.setCurrentItem(1);
                break;
        }
    }

}
