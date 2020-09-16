package com.yxst.inspect.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yxst.inspect.R;
import com.yxst.inspect.activity.adapter.LubeAdapter;
import com.yxst.inspect.database.Manager.LubeDeviceQuery;
import com.yxst.inspect.database.Manager.LubeItemQuery;
import com.yxst.inspect.database.model.LubeDevice;
import com.yxst.inspect.database.model.LubeItem;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.util.SharedPreferenceUtil;
import com.yxst.inspect.util.ThrowableUtil;
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
    @BindView(R.id.rv_all)
    RecyclerView rv;//列表显示
    @BindView(R.id.tv_load) TextView tvLoad;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新
    @BindView(R.id.progressBarLarge) LinearLayout progressBar;

    private List<LubeDevice> adapterData;
    private LubeAdapter adapter;//列表适配器
    private Long userid;//用户ID

    @SuppressLint("WrongConstant")
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
        //下拉刷新
        initPullRefresh();
    }
    /*
    条目点击事件
     */
    @Override
    public void onClick(Long deviceId, Long lineId,int position) {
        //跳转到润滑项界面
        Intent intent = new Intent(this,LubeItemActivity.class);
        intent.putExtra("deviceId",deviceId);
        intent.putExtra("lineId",lineId);
        startActivity(intent);

    }
    /*
    下拉刷新，加载数据
     */
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
            // 列表布局样式
            LinearLayoutManager layoutManager = new LinearLayoutManager(LubeActivity.this);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            rv.setLayoutManager(layoutManager);
            //填充数据
            adapter = new LubeAdapter(LubeActivity.this, adapterData);
            rv.setAdapter(adapter);
            adapter.setOnItemClickListener(LubeActivity.this);

            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(LubeActivity.this, "已获取最新！", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    };
    /*
    下拉刷新加载
     */
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
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

}
