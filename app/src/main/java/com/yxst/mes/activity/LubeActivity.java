package com.yxst.mes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.yxst.mes.R;
import com.yxst.mes.activity.adapter.LubeAdapter;
import com.yxst.mes.database.Manager.LubeDeviceQuery;
import com.yxst.mes.database.Manager.LubeItemQuery;
import com.yxst.mes.database.model.LubeDevice;
import com.yxst.mes.database.model.LubeItem;
import com.yxst.mes.net.RestCreator;
import com.yxst.mes.util.SharedPreferenceUtil;
import com.yxst.mes.util.ThrowableUtil;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created By YuanCheng on 2019/8/8 15:07
 */
public class LubeActivity extends BaseActivity implements LubeAdapter.OnItemClickListener {
    @BindView(R.id.rv_all) RecyclerView rv;
    @BindView(R.id.tv_load) TextView tvLoad;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.progressBarLarge) LinearLayout progressBar;

    private List<LubeDevice> adapterData;
    private LubeAdapter adapter;
    private Long userid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lube);
        ButterKnife.bind(this);
        setTitle("待润滑设备");
        userid = SharedPreferenceUtil.getId(this,"User");

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(layoutManager);

        adapterData = LubeDeviceQuery.findByTime(this);
        adapter = new LubeAdapter(this, adapterData);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        initPullRefresh();
    }

    private Observer<List> observer  =   new Observer<List>() {
        @Override
        public void onSubscribe(Disposable d) {
            progressBar.setVisibility(View.VISIBLE);
            tvLoad.setText("正在加载...");
        }
        @Override
        public void onNext(List list) {
            //         Toast.makeText(PrimaryActivity.this,list.size()+","+list,Toast.LENGTH_LONG).show();
            if (list == null || list.size() == 0) {
                return;
            }
            if(list.get(0) instanceof LubeDevice){
                List<LubeDevice> deviceList = LubeDeviceQuery.findByTime(LubeActivity.this);
                List<LubeDevice> userList = LubeDeviceQuery.findByUserID(userid);
                if(deviceList.size()==0 || userList.size()==0){
                    LubeDeviceQuery.deleteByUserId(userid);
                    LubeDeviceQuery.saveWithList(list);
                }

            }
            else if (list.get(0) instanceof LubeItem) {
                List<LubeItem> timeList = LubeItemQuery.findByTime(LubeActivity.this);
                List<LubeItem> userList = LubeItemQuery.findByUserID(userid);
                if(timeList.size()==0 || userList.size()==0){
                    LubeItemQuery.deleteByUserId(userid);
                    LubeItemQuery.saveWithList(list);
                }
            }
        }
        @Override
        public void onError(Throwable e) {
            ThrowableUtil.exceptionManager(e,LubeActivity.this);
            progressBar.setVisibility(View.GONE);

        }
        @Override
        public void onComplete() {
            tvLoad.setText("已获取最新！");

            adapterData = LubeDeviceQuery.findByTime(LubeActivity.this);
            // if(adapterData.size()!=0){
            LinearLayoutManager layoutManager = new LinearLayoutManager(LubeActivity.this);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            rv.setLayoutManager(layoutManager);

            adapter = new LubeAdapter(LubeActivity.this, adapterData);
            rv.setAdapter(adapter);
            adapter.setOnItemClickListener(LubeActivity.this);

            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(LubeActivity.this, "已获取最新！", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    };

    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Observable lubeDevice = RestCreator.getRxRestService().getLubeDevice(userid);
                Observable lube= RestCreator.getRxRestService().getLubeRecord(userid);
                Observable.merge(lube,lubeDevice).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);

            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
//        if(adapterData!=null){
//            adapterData.clear();
//        }

    }

    @Override
    public void onClick(Long deviceId, Long lineId,int position) {

        Intent intent = new Intent(this,LubeItemActivity.class);

        intent.putExtra("deviceId",deviceId);
        intent.putExtra("lineId",lineId);
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

}
