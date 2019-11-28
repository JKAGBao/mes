package com.yxst.mes.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yxst.mes.R;
import com.yxst.mes.activity.ScheduleActivity;
import com.yxst.mes.fragment.adapter.DeviceAdPgAdapter;
import com.yxst.mes.fragment.adapter.ItemRvAdapter;
import com.yxst.mes.fragment.bean.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import static android.support.v4.view.ViewPager.*;

public class DeviceFragment extends Fragment implements ItemRvAdapter.OnItemClickListener {


    @BindView(R.id.vp_show)
    ViewPager vpShow;
    @BindView(R.id.ll_point)
    LinearLayout llPoint;
    @BindView(R.id.rv_item) RecyclerView rvItem;
    private Unbinder unbinder;
    private List<Item> items = new ArrayList<Item>();
    private int lastPosition = 0;
    private int[] adImageRes;
    private ArrayList<ImageView> adImages;
    private Context mContext;
    private ScheduledExecutorService scheduledExecutorService;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(), 5, 5, TimeUnit.SECONDS);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        vpShow = (ViewPager) view.findViewById(R.id.vp_show);
        unbinder = ButterKnife.bind(this, view);

        rvItem.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        //   recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
        ItemRvAdapter mAdapter = new ItemRvAdapter(ConfigInfo.getInstance().ITEMS);
        rvItem.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        //初始化Adapter
        adImageRes = new int[]{R.drawable.misic,R.drawable.leak,R.drawable.lub};
        ImageView imageView;
        adImages= new ArrayList<ImageView>();
        View pointView;
        LinearLayout.LayoutParams params;
        if(adImages.size()==0) {
            for (int i = 0; i < adImageRes.length; i++) {
                imageView = new ImageView(mContext);
                imageView.setBackgroundResource(adImageRes[i]);
                adImages.add(imageView);
                //增加小圆点
                pointView = new View(mContext);
                pointView.setBackgroundResource(R.drawable.ad_point_selector);
                params = new LinearLayout.LayoutParams(18, 18);
                if (i != 0) {
                    params.leftMargin = 20;
                }
                pointView.setEnabled(false);
                llPoint.addView(pointView, params);
            }
        }
        vpShow.setAdapter(new DeviceAdPgAdapter(adImages));
        vpShow.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            @Override
            public void onPageSelected(int position) {
                //当前的位置可能很大，为了防止下标越界，对要显示的图片的总数进行取余
                int newPosition = position % adImageRes.length;
                //设置小圆点为高亮或暗色
                llPoint.getChildAt(lastPosition).setEnabled(false);
                llPoint.getChildAt(newPosition).setEnabled(true);
                lastPosition = newPosition; //记录之前的点
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        llPoint.getChildAt(0).setEnabled(true);
        return view;

    }


    private int currentItem = 0;
    /*
    首页 9宫格
     */
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = null;
        switch (position){
            case 20:
                intent = new Intent(getActivity(),ScheduleActivity.class);
                startActivity(intent);

                break;
            case ConfigInfo.BIND_DEVICE:

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_load,new BindFragment())
                        .addToBackStack(null);

                break;
            case ConfigInfo.WAIT_INSPECT:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_load,new InspectFragment())
                        .addToBackStack(null)
                        .commit();
                intent = new Intent();
                break;
            case 3:
                 intent = new Intent();
                break;
            case 4:
                 intent = new Intent();
                break;
            case 6:
                 intent = new Intent();
                break;
            case ConfigInfo.MONITOR://6

                break;
            case ConfigInfo.SETTING:

                break;

            default:
                break;

        }
        Toast.makeText(getActivity(), "点击第" + (position) + "条", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onItemLongClick(View view, int position) {

    }

    private class ViewPageTask implements Runnable{
        @Override
        public void run() {
            currentItem = (currentItem ) %3;

            mHandler.sendEmptyMessage(0);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
      //  unbinder.unbind();
        items.clear();



    }

    /**
     * 接收子线程传递过来的数据
     */

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {

            vpShow.setCurrentItem(vpShow.getCurrentItem()+1%3);
            Log.e("111111","vpShow:"+vpShow+",getCurretnItem:"+vpShow.getCurrentItem());
        };
    };
}