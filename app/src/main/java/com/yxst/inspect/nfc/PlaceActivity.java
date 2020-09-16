package com.yxst.inspect.nfc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.R;
import com.yxst.inspect.activity.BaseActivity;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.InspectDeviceDao;
import com.yxst.inspect.database.model.InspectDevice;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.model.Line;
import com.yxst.inspect.database.Manager.InspectDevicQueryUtil;
import com.yxst.inspect.database.Manager.ItemQueryUtil;
import com.yxst.inspect.database.Manager.PlaceQueryUtil;
import com.yxst.inspect.database.model.Place;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.nfc.adapter.NFCLineAdapter;
import com.yxst.inspect.view.Topbar;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.content.DialogInterface.OnClickListener;

public class PlaceActivity extends BaseActivity implements NFCLineAdapter.OnItemClickListener {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    @BindView(R.id.lv_nfc_place)
    RecyclerView rvLine;
    @BindView(R.id.tv_nfc_pname) TextView deviceName;
    @BindView(R.id.topbar) Topbar topbar;
    private InspectDeviceDao inspectDevicedao;
    private List<Place> places;
    private Long lineId;
    private static final int INSPECT_RESULTL = 1;
    private NFCLineAdapter nfcLineAdapter;
    private Line inspectLine;
    private InspectDevice inspectDevice;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_nfc_line);
        ButterKnife.bind(this);
        inspectDevicedao = DatabaseManager.getInstance().getInspectDeviceDao();
      //  identifyNfc();
        Long deviceId = getIntent().getLongExtra("deviceId",0);
        lineId = getIntent().getLongExtra("lineId",0);
        inspectDevice = InspectDevicQueryUtil.getInpectDeviceByDeviceID(this,deviceId,lineId);
        topbar.leftButton.setText(inspectDevice.getEquipmentName());

        places = PlaceQueryUtil.getPlaceByDeviceId(this,deviceId,lineId);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rvLine.setLayoutManager(layoutManager);
        nfcLineAdapter = new NFCLineAdapter(this, places);
        nfcLineAdapter.setOnItemClickListener(this);
        rvLine.setAdapter(nfcLineAdapter);
        topbar.setOnClickListener(new Topbar.TopbarOnclickListener() {
            @Override
            public void onLeftClick() {

            }
            @Override
            public void onRightClick() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PlaceActivity.this)
                        .setMessage("确定要报备停机吗？")
                        .setPositiveButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, Object> params = new LinkedHashMap<>();
                                params.put("EquipmentID",inspectDevice.getEquipmentID());
//                                params.put("LineID",inspectDevice.getLineID());
                                params.put("BeginTime",inspectDevice.getBeginTime());
                                params.put("EndTime",inspectDevice.getEndTime());
                                params.put("RunStates","1");
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), new Gson().toJson(params));
                                Observable statusValue = RestCreator.getRxRestService().stopStatus(requestBody);
                                statusValue.subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer() {
                                            @Override
                                            public void accept(Object o) throws Exception {
                                                if(Integer.valueOf(o.toString())>0){
                                                    List<InspectDevice> inspectDevices = InspectDevicQueryUtil.getByLineID(PlaceActivity.this,inspectDevice.getLineID());
                                                    for(InspectDevice device : inspectDevices){
                                                        device.setRunStates(1);
                                                        InspectDevicQueryUtil.updateInpectDevice(device);
                                                    }
                                                    Toast.makeText(PlaceActivity.this, "停机已发送！" +
                                                            "", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                Toast.makeText(PlaceActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        }).setNegativeButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();

            }
        });
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
    protected void onStop() {
        super.onStop();
        if(places!=null && places.size()!=0){
            for(Place place:places){
                List<Item> items = ItemQueryUtil.getItemByPlaceId(this,place.getPlaceID(),place.getLineID());
                //一个部位下 完成的Item。
                List<Item> finishItems = ItemQueryUtil.getItemByGEStatus(this,place.getPlaceID(),place.getLineID(), ConfigInfo.CHECKED_STATUS);
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
        super.onBackPressed();

    }

    /*
        巡检项的 检查类型
        1 测振型
        2 观察型
        3 测温型
        4 抄表型
        5 测温测振型
     */
    @Override
    public void onClick(View view, String placeName, Long placeId, int position) {
        //   Toast.makeText(NFCPlaceActivity.this,"Tast："+position,Toast.LENGTH_LONG).show();
        List<Item> items = ItemQueryUtil.getItemByPlaceId(this,placeId,lineId);
        //     Toast.makeText(this, items.size()+",placeId查询items"+placeId, Toast.LENGTH_SHORT).show();
        if(items.size() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("该部位未配置巡检项，请确认！")
                    .setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            return;
                        }
                    });
            builder.show();
        }else{
            //         Toast.makeText(this, "placeId="+placeId, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,NFCInspectActivity.class);
            intent.putExtra("placeid",placeId);
            intent.putExtra("lineid",lineId);
            intent.putExtra("placeName",placeName);
            startActivity(intent);
            //startActivityForResult(intent,INSPECT_RESULT);

        }
    }
}
