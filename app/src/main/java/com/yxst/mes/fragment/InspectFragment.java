package com.yxst.mes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxst.mes.R;
import com.yxst.mes.fragment.adapter.InspectTabPgAdapter;
import com.yxst.mes.fragment.inspect.FragmentAll;
import com.yxst.mes.fragment.inspect.FragmentFinish;
import com.yxst.mes.fragment.inspect.FragmentWait;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class InspectFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.vp_slide) ViewPager vpSlide;
    @BindView(R.id.iv_navline) ImageView ivNavline;
    @BindView(R.id.tv_wait) TextView tvWait;
    @BindView(R.id.tv_finish) TextView tvFinish;
    private Unbinder unbinder;
    private Context mContext;
    private List<Fragment> mFragmentList;
    private InspectTabPgAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_inspect,container,false);
        unbinder = ButterKnife.bind(this,view);
        Log.e("on","onCreateView");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        initWidth();
        adapter = new InspectTabPgAdapter(getChildFragmentManager(),mFragmentList);

        vpSlide.setAdapter(adapter);
        vpSlide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetParams) {
                if(position==0){
                    tvWait.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tvFinish.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }else if(position==1){
                    tvWait.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    tvFinish.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivNavline.getLayoutParams();
//                params.leftMargin = sreenWidth/6 + positionOffsetParams/2;
//                ivNavline.setLayoutParams(params);
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
       // vpSlide.setCurrentItem(0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private int sreenWidth;
    private void initWidth(){
        DisplayMetrics  dm = new DisplayMetrics();
        //取得窗口属性
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        sreenWidth = dm.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ivNavline.getLayoutParams();
        lp.width = sreenWidth/6;
        ivNavline.setLayoutParams(lp);
    }
    private void initData(){
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(new FragmentWait());
        mFragmentList.add(new FragmentFinish());

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
