package com.yxst.inspect.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.R;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.InspectLineDao;
import com.yxst.inspect.database.model.InspectDevice;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.Manager.InspectDevicQueryUtil;
import com.yxst.inspect.database.Manager.ItemQueryUtil;
import com.yxst.inspect.database.Manager.PlaceQueryUtil;
import com.yxst.inspect.database.model.Place;
import com.yxst.inspect.database.model.InspectLine;
import com.yxst.inspect.fragment.adapter.AnInspectAdapter;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.util.SharedPreferenceUtil;
import com.yxst.inspect.util.ThrowableUtil;
import com.yxst.inspect.util.TitleBarUtil;
import com.yxst.inspect.view.Topbar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AnInspectActivity extends AppCompatActivity implements AnInspectAdapter.OnItemClickListener {
    @BindView(R.id.rv_all)
    RecyclerView rv;
    @BindView(R.id.topbar) Topbar topbar;
    @BindView(R.id.tv_load) TextView tvLoad;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.progressBarLarge) LinearLayout progressBar;
    private List<InspectDevice> adapterData;
    private AnInspectAdapter adapter;
    private Long userid;
    private Long lineId;
    private String lineName;
    Context context;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TitleBarUtil.titlebarSetting(this);
        countFinishPlace();//统计巡检完的部位个数
        setContentView(R.layout.fragment_inspect_an);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ButterKnife.bind(this);

        lineId = getIntent().getLongExtra("LineId",1);
        lineName = getIntent().getStringExtra("LineName");
        userid = SharedPreferenceUtil.getId(this,"User");
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapterData = InspectDevicQueryUtil.getByUnLineID(this,lineId, ConfigInfo.ITEM_UNCHECK_STATUS);
        adapter = new AnInspectAdapter(this, adapterData);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(AnInspectActivity.this);
        initPullRefresh();
        topbar.leftButton.setText(lineName);
        topbar.setOnClickListener(new Topbar.TopbarOnclickListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AnInspectActivity.this)
                        .setMessage("确定要报备停用吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                InspectLine line = DatabaseManager.getInstance().getInspectLineDao().queryBuilder().where(
                                        InspectLineDao.Properties.LineID.eq(lineId)
                                        ,InspectLineDao.Properties.BeginTime.le(new Date())
                                        ,InspectLineDao.Properties.EndTime.ge(new Date())
                                        ).list().get(0);
                              //  Log.e("saf","line"+line.getLineID());

                                Map<String, Object> params = new LinkedHashMap<>();
                                params.put("LineID",line.getLineID());
                                params.put("BeginTime",line.getBeginTime());
                                params.put("EndTime",line.getEndTime());
                                params.put("RunStates","1");

                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), new Gson().toJson(params));
                                Observable statusValue = RestCreator.getRxRestService().stopLineStatus(requestBody);
                                statusValue.subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer() {
                                            @Override
                                            public void accept(Object o) throws Exception {
                                                if(Integer.valueOf(o.toString())>0){
                                                    Toast.makeText(AnInspectActivity.this, "停用请求已发送！" +
                                                            "", Toast.LENGTH_SHORT).show();
                                                //    finish();
                                                }
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                Toast.makeText(AnInspectActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
                //    Observable statusValue = RestCreator.getRxRestService().stopStatus(jsonStr);


            }
        });

    }

    private Observer<List> observer  =   new Observer<List>() {
        @Override
        public void onSubscribe(Disposable d) {
           progressBar.setVisibility(View.VISIBLE);
            tvLoad.setText("正在加载...");
        }
        @Override
        public void onNext(List list) {
            tvLoad.setText("已获取最新！");
            //         Toast.makeText(PrimaryActivity.this,list.size()+","+list,Toast.LENGTH_LONG).show();
            if (list == null || list.size() == 0) {
                return;
            }
            if (list.get(0) instanceof InspectDevice) {
                List<InspectDevice> localDevice = InspectDevicQueryUtil.getInpectDeviceListByUserId(AnInspectActivity.this);
                List<InspectDevice> inspectDevices = InspectDevicQueryUtil.getInpectDeviceListByTime(AnInspectActivity.this);
                //    Toast.makeText(PrimaryActivity.this, inspectDevices.size()+","+localDevice.size()+","+list.size(), Toast.LENGTH_SHORT).show();
                if (list.size() != localDevice.size() || inspectDevices.size() == 0) {//没有更新inspectdevice
                    InspectDevicQueryUtil.deleteByUserID(userid);
                    InspectDevicQueryUtil.saveByList(list);
                }
            } else if (list.get(0) instanceof Place) {
                List<Place> placeList = PlaceQueryUtil.getPlaceByUserId(userid);
                List<Place> placesbyTime = PlaceQueryUtil.getPlaceByTime(AnInspectActivity.this);

         //       Toast.makeText(context, ""+list.size()+","+placeList.size()+","+placesbyTime.size(), Toast.LENGTH_SHORT).show();
                PlaceQueryUtil.deleteByUserId(userid);
                PlaceQueryUtil.saveWithList(list);
                if (list.size() != placeList.size() || placesbyTime.size() == 0) {
                }
            } else if (list.get(0) instanceof Item) {
                List<Item> itemList = ItemQueryUtil.getItemByUserId(userid);
                List<Item> items = ItemQueryUtil.getItemByTime(AnInspectActivity.this);
                //       Toast.makeText(PrimaryActivity.this,MyApplication.InInpectLine+","+list.size()+","+itemList.size(), Toast.LENGTH_SHORT).show();
                ItemQueryUtil.deleteByUserId(userid);
                ItemQueryUtil.saveWithItemList(list);
                if (list.size() != itemList.size() || items.size() == 0) {
                }

            }
        }
        @Override
        public void onError(Throwable e) {
            ThrowableUtil.exceptionManager(e, AnInspectActivity.this);
            progressBar.setVisibility(View.GONE);

        }
        @Override
        public void onComplete() {
        //    countFinishPlace();
            adapterData = InspectDevicQueryUtil.getInpectDevicesByEQStatus(AnInspectActivity.this,ConfigInfo.ITEM_UNCHECK_STATUS);;
           // if(adapterData.size()!=0){
                LinearLayoutManager layoutManager = new LinearLayoutManager(AnInspectActivity.this);
                layoutManager.setOrientation(OrientationHelper.VERTICAL);
                rv.setLayoutManager(layoutManager);

                adapter = new AnInspectAdapter(context, adapterData);
                rv.setAdapter(adapter);
                adapter.setOnItemClickListener(AnInspectActivity.this);
        //    }
            //      adapter.notifyDataSetChanged();;
            mSwipeRefreshLayout.setRefreshing(false);//不加载下拉刷新
            Toast.makeText(context, "已获取最新！", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);
        }
    };
    /*
    下拉刷新
     */
    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Observable inspectDevice = RestCreator.getRxRestService().getInspectDeviceByUserId(userid);
                Observable item = RestCreator.getRxRestService().getItemByUserId(userid);
                Observable place = RestCreator.getRxRestService().getPlaceByUserId(userid);
                Observable.merge(inspectDevice,place,item).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);

              }
        });
    }


    /*
    条目点击事件
     */
    @Override
    public void onClick(Long deviceId, Long lineId,int position) {

//        Intent intent = new Intent(AnInspectActivity.this,PlaceLookActivity.class);
//        intent.putExtra("deviceId",deviceId);
//        intent.putExtra("lineId",lineId);
//        startActivity(intent);

    }
    /*
    设备停机点击事件
     */
    @Override
    public void onItemClick(View view, View btn,final InspectDevice inspectDevice, int Position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AnInspectActivity.this)
                .setMessage("确定要报备设备停用吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //封装参数
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put("EquipmentID",inspectDevice.getEquipmentID());
                        params.put("BeginTime",inspectDevice.getBeginTime());
                        params.put("EndTime",inspectDevice.getEndTime());
                        params.put("RunStates","1");
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), new Gson().toJson(params));
                        //请求接口
                        Observable statusValue = RestCreator.getRxRestService().stopStatus(requestBody);
                        statusValue.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer() {
                                    @Override
                                    public void accept(Object o) throws Exception {
                                        //o值>0表示请求成功
                                        if(Integer.valueOf(o.toString())>0){
                                            inspectDevice.setRunStates(1);
                                            Toast.makeText(AnInspectActivity.this, "停用已报备！" +
                                                    "", Toast.LENGTH_SHORT).show();
                                            InspectDevicQueryUtil.updateInpectDevice(inspectDevice);
                                            finish();
                                        }                               }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Toast.makeText(AnInspectActivity.this, "asdf:"+throwable.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
    /*
    统计巡检完的部位个数
     */
    private void countFinishPlace(){
        InspectDevicQueryUtil.updateInspectDeviceFinishStatus(AnInspectActivity.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
    }

}
