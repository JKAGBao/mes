package com.yxst.inspect.nfc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.R;
import com.yxst.inspect.activity.BaseActivity;
import com.yxst.inspect.activity.adapter.DeviceAdapter;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.InspectDeviceDao;
import com.yxst.inspect.database.dao.LineDao;
import com.yxst.inspect.database.model.InspectDevice;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.model.Line;
import com.yxst.inspect.database.Manager.InspectDevicQueryUtil;
import com.yxst.inspect.database.Manager.ItemQueryUtil;
import com.yxst.inspect.database.Manager.PlaceQueryUtil;
import com.yxst.inspect.database.model.Place;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.nfc.adapter.NFCLineAdapter;
import com.yxst.inspect.rx.manager.BindDeviceManager;
import com.yxst.inspect.util.SharedPreferenceUtil;
import com.yxst.inspect.util.ThrowableUtil;
import com.yxst.inspect.view.Topbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.DialogInterface.OnClickListener;

public class NFCDeviceActivity extends BaseActivity implements DeviceAdapter.OnItemClickListener {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    @BindView(R.id.lv_nfc_place)
    RecyclerView rvLine;
    @BindView(R.id.topbar) Topbar topbar;
    @BindView(R.id.tv_load) TextView tvLoad;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.progressBarLarge)
    LinearLayout progressBar;
    private InspectDeviceDao inspectDevicedao;
    private List<Place> places;
    private Long lineId;
    private static final int INSPECT_RESULTL = 1;
    private NFCLineAdapter nfcLineAdapter;
    private Line inspectLine;
    private InspectDevice inspectDevice;
    private List<InspectDevice> inspectDeviceList;
    private DeviceAdapter adapter;
    private long userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_nfc_device);
        ButterKnife.bind(this);
        inspectDevicedao = DatabaseManager.getInstance().getInspectDeviceDao();
        userid = SharedPreferenceUtil.getId(NFCDeviceActivity.this,"User");
        identifyNfc();
        initPullRefresh();
        BindDeviceManager.obtainline(this);
//        topbar.setOnClickListener(new Topbar.TopbarOnclickListener() {
//            @Override
//            public void onLeftClick() {
//
//            }
//            @Override
//            public void onRightClick() {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(NFCDeviceActivity.this)
//                        .setMessage("确定要报备停机吗？")
//                        .setPositiveButton("确定", new OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Map<String, Object> params = new LinkedHashMap<>();
//                                params.put("LineID",inspectDevice.getLineID());
//                                params.put("BeginTime",inspectDevice.getBeginTime());
//                                params.put("EndTime",inspectDevice.getEndTime());
//                                params.put("RunStates","1");
//                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), new Gson().toJson(params));
//                                Observable statusValue = RestCreator.getRxRestService().stopLineStatus(requestBody);
//                                statusValue.subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(new Consumer() {
//                                            @Override
//                                            public void accept(Object o) throws Exception {
//                                                if(Integer.valueOf(o.toString())>0){
//                                                    List<InspectDevice> inspectDevices = InspectDevicQueryUtil.getByLineID(NFCDeviceActivity.this,inspectDevice.getLineID());
//                                                    for(InspectDevice device : inspectDevices){
//                                                        device.setRunStates(1);
//                                                        InspectDevicQueryUtil.updateInpectDevice(device);
//                                                    }
//                                                    Toast.makeText(NFCDeviceActivity.this, "停机已发送！" +
//                                                            "", Toast.LENGTH_SHORT).show();
//                                                    finish();
//                                                }
//                                            }
//                                        }, new Consumer<Throwable>() {
//                                            @Override
//                                            public void accept(Throwable throwable) throws Exception {
//                                                Toast.makeText(NFCDeviceActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                            }
//                                        });
//                            }
//                        }).setNegativeButton("取消", new OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                builder.show();
//
//            }
//        });
    }

    /**
     * 启动Activity，界面可见时
     */
    @Override
    protected void onStart() {
        super.onStart();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false)
                    .setTitle("请确认设备是否支持NFC功能！")
                    .setPositiveButton("确定", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();

        }
        //一旦截获NFC消息，就会通过PendingIntent调用窗口
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()), 0);

    }

    /**
     * 获得焦点，按钮可以点击
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.e("nfcPlace:","onResume()");
        if(nfcLineAdapter!=null)
            nfcLineAdapter.notifyDataSetChanged();
        //设置处理优于所有其他NFC的处理
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("nfcPlace:","onPause()");
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.e("nfc", intent.getAction());
//        dao = DatabaseManager.getInstance().getDeviceDao();
        Observable line = RestCreator.getRxRestService().getLine();
        line.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(List list) {
                        //请求所有的任务线成功后，先清一下Line表数据
                        DatabaseManager.getInstance().getLineDao().deleteAll();
                        DatabaseManager.getInstance().getLineDao().insertOrReplaceInTx(list);
                        Log.e("schedule", "Schedule-url-geDevice（）：" + list.size());

                    }
                    @Override
                    public void onError(Throwable e) {
                        identifyNfc();
                    }

                    @Override
                    public void onComplete() {
                        identifyNfc();
                    }
                });
    }
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
                List<InspectDevice> localDevice = InspectDevicQueryUtil.getInpectDeviceListByUserId(NFCDeviceActivity.this);
                List<InspectDevice> inspectDevices = InspectDevicQueryUtil.getInpectDeviceListByTime(NFCDeviceActivity.this);
                //    Toast.makeText(PrimaryActivity.this, inspectDevices.size()+","+localDevice.size()+","+list.size(), Toast.LENGTH_SHORT).show();
                if (list.size() != localDevice.size() || inspectDevices.size() == 0) {//没有更新inspectdevice
                    InspectDevicQueryUtil.deleteByUserID(userid);
                    InspectDevicQueryUtil.saveByList(list);
                }
            } else if (list.get(0) instanceof Place) {
                List<Place> placeList = PlaceQueryUtil.getPlaceByUserId(userid);
                List<Place> placesbyTime = PlaceQueryUtil.getPlaceByTime(NFCDeviceActivity.this);
                //       Toast.makeText(context, ""+list.size()+","+placeList.size()+","+placesbyTime.size(), Toast.LENGTH_SHORT).show();
                PlaceQueryUtil.deleteByUserId(userid);
                PlaceQueryUtil.saveWithList(list);
                if (list.size() != placeList.size() || placesbyTime.size() == 0) {
                }
            } else if (list.get(0) instanceof Item) {
                List<Item> itemList = ItemQueryUtil.getItemByUserId(userid);
                List<Item> items = ItemQueryUtil.getItemByTime(NFCDeviceActivity.this);
                //       Toast.makeText(PrimaryActivity.this,MyApplication.InInpectLine+","+list.size()+","+itemList.size(), Toast.LENGTH_SHORT).show();
                ItemQueryUtil.deleteByUserId(userid);
                ItemQueryUtil.saveWithItemList(list);
                if (list.size() != itemList.size() || items.size() == 0) {
                }

            }
        }
        @Override
        public void onError(Throwable e) {
            ThrowableUtil.exceptionManager(e, NFCDeviceActivity.this);
            progressBar.setVisibility(View.GONE);

        }
        @SuppressLint("WrongConstant")
        @Override
        public void onComplete() {
            //    countFinishPlace();
            inspectDeviceList = InspectDevicQueryUtil.getByLineID(NFCDeviceActivity.this,lineId
            );
            //设置布局管理器
            LinearLayoutManager layoutManager = new LinearLayoutManager(NFCDeviceActivity.this);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            rvLine.setLayoutManager(layoutManager);
            adapter = new DeviceAdapter(NFCDeviceActivity.this, inspectDeviceList);
            adapter.setOnItemClickListener(NFCDeviceActivity.this);
            rvLine.setAdapter(adapter);

            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(NFCDeviceActivity.this, "已获取最新！", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);


        }
    };
    //获取NFC数据
    private void identifyNfc() {
        //1.获取Tag对象
        Tag detectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Long cardNo = flipHexStr(ByteArrayToHexString(detectedTag.getId()));
        //if cardNo不为空，查询是否已经绑定过，未绑定的话，进行绑定
        if (cardNo != null) {
              Line line = DatabaseManager.getInstance().getLineDao().queryBuilder().where(LineDao.Properties.RFID.eq(cardNo)).unique();
         //   Toast.makeText(this, "device:", Toast.LENGTH_SHORT).show();
            if(line == null ){
                   //未绑定
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false)
                            .setTitle("该nfc卡还未绑定线路，请先绑定！")
                            .setPositiveButton("确定", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    builder.show();
            } else if (line != null) {//已绑定
                setTitle(line.getLineName());
                lineId = line.getID();
                topbar.leftButton.setText(line.getLineName());

                //根据NFC卡绑定的设备，查询部位下
                inspectDevice = InspectDevicQueryUtil.getInpectDeviceByLineID(this,line.getID());

          //      Toast.makeText(this, "是否为空"+(inspectDevice == null), Toast.LENGTH_SHORT).show();
               if(inspectDevice == null){
                   final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                           .setMessage("该线路，未添加为今日巡检项！")
                           .setPositiveButton("确定",new OnClickListener(){

                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   finish();
                               }
                           });
                   builder.show();

               }else{
                   inspectDeviceList = InspectDevicQueryUtil.getByLineID(NFCDeviceActivity.this,line.getID());
                   //设置布局管理器
                   LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                   layoutManager.setOrientation(OrientationHelper.VERTICAL);
                   rvLine.setLayoutManager(layoutManager);
                   adapter = new DeviceAdapter(this, inspectDeviceList);
                   adapter.setOnItemClickListener(this);
                   rvLine.setAdapter(adapter);
                   //完成，提示已经完成的AlertDialog
                   isShowInspectFinishAlartDialog(inspectDevice.getEquipmentID(),inspectDevice.getLineID());

               }
            }
        }

    }

    private void isShowInspectFinishAlartDialog(Long deviceId,Long lineId){
        List<Place> placeList = PlaceQueryUtil.getPlaceByLineId(this,lineId);
        List<Place> finishList = PlaceQueryUtil.getPlaceByGEStatusOnLine(this,lineId, ConfigInfo.CHECKED_STATUS);
   //     Toast.makeText(this, placeList.size()+"+"+finishList.size(), Toast.LENGTH_SHORT).show();
        if(finishList.size() == placeList.size()&& placeList.size()!=0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("该设备已经巡检完毕,已检设备中可查看详情！")
                    .setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.show();
        }
    }

    /*
    byte数组转为16进制数字
     */
    private  String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        StringBuilder out = new StringBuilder();

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out.append(hex[i]);
            i = in & 0x0f;
            out.append(hex[i]);
        } return out.toString();
    }
    /*
    转为10进制数字
     */
    private Long flipHexStr(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= s.length() - 2; i = i + 2) {
            result.append(new StringBuilder(s.substring(i, i + 2)).reverse());
        }
        String value = result.reverse().toString();
        return  Long.parseLong(value, 16);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(places!=null && places.size()!=0){
            for(Place place:places){
                List<Item> items = ItemQueryUtil.getItemByPlaceId(this,place.getPlaceID(),place.getLineID());
                //一个部位下 完成的Item。
                List<Item> finishItems = ItemQueryUtil.getItemByGEStatus(this,place.getPlaceID(),place.getLineID(),ConfigInfo.CHECKED_STATUS);
                if(items.size() == finishItems.size() && items.size()!=0){
                    //更新place状态
                    place.setCheckStatus(ConfigInfo.CHECKED_STATUS);
                    DatabaseManager.getInstance().getPlaceDao().update(place);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(NFCDeviceActivity.this)
                .setMessage("确定要退出巡检吗？")
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @Override
    public void onClick(Long deviceId, Long lineId, int position) {
        //   Toast.makeText(NFCPlaceActivity.this,"Tast："+position,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(NFCDeviceActivity.this,PlaceActivity.class);
        intent.putExtra("deviceId",deviceId);
        intent.putExtra("lineId",lineId);
        startActivity(intent);

    }
}
