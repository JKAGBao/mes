package com.yxst.inspect.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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
import com.yxst.inspect.activity.adapter.UnLubeAdapter;
import com.yxst.inspect.database.Manager.LubeDeviceQuery;
import com.yxst.inspect.database.Manager.UnLubeDeviceQuery;
import com.yxst.inspect.database.model.LubeDevice;
import com.yxst.inspect.database.model.UnLube;
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
public class UnLubeActivity extends BaseActivity implements UnLubeAdapter.OnItemClickListener {
    @BindView(R.id.rv_all)
    RecyclerView rv;
    @BindView(R.id.tv_load) TextView tvLoad;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.progressBarLarge) LinearLayout progressBar;

    private List<UnLube> adapterData;
    private UnLubeAdapter adapter;
    private Long userid;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lube);
        ButterKnife.bind(this);
        setTitle("漏润滑设备");
        userid = SharedPreferenceUtil.getId(this,"User");

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(layoutManager);
        //加载漏润滑数据
        adapterData = UnLubeDeviceQuery.findByTime(this);
        Log.e("unlube",adapterData.size()+",");
        adapter = new UnLubeAdapter(this, adapterData);
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
            if(list.get(0) instanceof UnLube){
                List<UnLube> deviceList = UnLubeDeviceQuery.findByTime(UnLubeActivity.this);
                List<LubeDevice> userList = LubeDeviceQuery.findByUserID(userid);
                if(deviceList.size()==0 || userList.size()==0){
                    UnLubeDeviceQuery.deleteByUserId(userid);
                    UnLubeDeviceQuery.saveWithList(list);
                }

            }

        }
        @Override
        public void onError(Throwable e) {
            ThrowableUtil.exceptionManager(e,UnLubeActivity.this);
            progressBar.setVisibility(View.GONE);

        }
        @Override
        public void onComplete() {
            tvLoad.setText("已获取最新！");

            adapterData = UnLubeDeviceQuery.findByTime(UnLubeActivity.this);
            // if(adapterData.size()!=0){
            LinearLayoutManager layoutManager = new LinearLayoutManager(UnLubeActivity.this);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            rv.setLayoutManager(layoutManager);

            adapter = new UnLubeAdapter(UnLubeActivity.this, adapterData);
            rv.setAdapter(adapter);
            adapter.setOnItemClickListener(UnLubeActivity.this);

            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(UnLubeActivity.this, "已获取最新！", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    };
    /*
    初始化下拉刷新
     */
    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Observable lubeDevice = RestCreator.getRxRestService().getMissLube(userid);
                Observable lube= RestCreator.getRxRestService().getLubeRecord(userid);
                lubeDevice.subscribeOn(Schedulers.io())
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

//        Intent intent = new Intent(this,LubeItemActivity.class);
////        intent.putExtra("deviceId",deviceId);
////        intent.putExtra("lineId",lineId);
////        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

}
