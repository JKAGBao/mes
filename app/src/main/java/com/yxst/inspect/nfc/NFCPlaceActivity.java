package com.yxst.inspect.nfc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.MyApplication;
import com.yxst.inspect.R;
import com.yxst.inspect.activity.FinishActivity;
import com.yxst.inspect.activity.InspectPlaceActivity;
import com.yxst.inspect.activity.ReadMeterActivity;
import com.yxst.inspect.activity.StartUpActivity;
import com.yxst.inspect.activity.VibraBlueActivity;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.DeviceDao;
import com.yxst.inspect.database.dao.InspectDeviceDao;
import com.yxst.inspect.database.model.Device;
import com.yxst.inspect.database.model.InspectDevice;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.Manager.InspectDevicQueryUtil;
import com.yxst.inspect.database.Manager.ItemQueryUtil;
import com.yxst.inspect.database.Manager.PlaceQueryUtil;
import com.yxst.inspect.database.model.Place;
import com.yxst.inspect.fragment.adapter.InspectAdapter;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.nfc.adapter.NFCLineAdapter;
import com.yxst.inspect.util.SharedPreferenceUtil;
import com.yxst.inspect.util.TitleBarUtil;
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

import static android.content.DialogInterface.*;

/**
 *
 * 扫nfid卡后弹出的巡检界面
 */
public class NFCPlaceActivity extends AppCompatActivity implements NFCLineAdapter.OnItemClickListener,InspectAdapter.OnItemClickListener {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    @BindView(R.id.lv_nfc_place)
    RecyclerView rvLine;
    @BindView(R.id.lv_nfc_device) RecyclerView rvDevice;
    @BindView(R.id.tv_nfc_pname) TextView deviceName;
    @BindView(R.id.topbar) Topbar topbar;
  //  private DeviceDao dao;
    private InspectDeviceDao inspectDevicedao;
    private Context mContext;
    private List<Place> places;
    private Long lineId;
    private Long deviceId;
    private static final int INSPECT_RESULTL = 1;
    private static final int ITME_OBSERVE_VALUE = 0;

    private NFCLineAdapter nfcLineAdapter;
    private InspectDevice inspectDevice;
    private  List<Place> finishPlace;
    private List<Place> placeList;
    private List<InspectDevice> adapterData;
    private  InspectAdapter adapter;
    private List<Device> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //标题栏设置
        TitleBarUtil.titlebarSetting(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_nfc_place);
        ButterKnife.bind(this);
        mContext = this;
//      dao = DatabaseManager.getInstance().getDeviceDao();
        inspectDevicedao = DatabaseManager.getInstance().getInspectDeviceDao();
        //nfc识别流程
        identifyNfc(getIntent());
        //停机点击事件
        topbar.setOnClickListener(topbarOnclickListener);
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
    /*
    停机按钮点击事件
     */
    Topbar.TopbarOnclickListener topbarOnclickListener = new Topbar.TopbarOnclickListener(){
        @Override
        public void onLeftClick() {

        }

        @Override
        public void onRightClick() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(NFCPlaceActivity.this)
                    .setMessage("确定要报备停用吗？")
                    .setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            long userId = SharedPreferenceUtil.getId(NFCPlaceActivity.this,"User");
                            Map<String, Object> params = new LinkedHashMap<>();
                            params.put("EquipmentID",inspectDevice.getEquipmentID());
                            params.put("BeginTime",inspectDevice.getBeginTime());
                            params.put("EndTime",inspectDevice.getEndTime());
                            params.put("RunStates","1");
                            params.put("UserID",userId);
                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), new Gson().toJson(params));
                            Observable statusValue = RestCreator.getRxRestService().stopStatus(requestBody);
                            statusValue.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer() {
                                        @Override
                                        public void accept(Object o) throws Exception {
                                            if(Integer.valueOf(o.toString())>0){
                                                inspectDevice.setRunStates(1);
                                                Toast.makeText(NFCPlaceActivity.this, "停用已报备！" +
                                                        "", Toast.LENGTH_SHORT).show();
                                                InspectDevicQueryUtil.updateInpectDevice(inspectDevice);
                                                finish();
                                            }
                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(Throwable throwable) throws Exception {
                                            Toast.makeText(NFCPlaceActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

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
    };
    /**
     * 获得焦点，按钮可以点击
     */
    @Override
    public void onResume() {
        super.onResume();
        if(nfcLineAdapter!=null) {
            nfcLineAdapter.notifyDataSetChanged();
        }
        //设置处理优于所有其他NFC的处理
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
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
        Log.e("nfcPlace:","onResume()");

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onNewIntent(Intent intent) {
        Log.e("nfc", intent.getAction());
        identifyNfc(intent);

    }

    /*
    设备巡检完逻辑
     */
    private void isShowInspectFinishAlartDialog(Long deviceId,Long lineId){
        //获取checkStatus状态大于2的设备，如果相等表示巡检完
        placeList = PlaceQueryUtil.getPlaceByDeviceId(this,deviceId,lineId);
        finishPlace = PlaceQueryUtil.getPlaceByGEStatus(this,deviceId,lineId,ConfigInfo.CHECKED_STATUS);
 //     Toast.makeText(this, placeList.size()+"+"+finishPlace.size(), Toast.LENGTH_SHORT).show();
        if(finishPlace.size() == placeList.size()&& placeList.size()!=0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("该设备已经巡检完毕,【已检设备】中可查看详情！")
                    .setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(mContext, FinishActivity.class);
                            startActivity(intent);
                            finish();
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
    }
    @Override
    public void onBackPressed() {
        if(devices.size()==1){
            placeList = PlaceQueryUtil.getPlaceByDeviceId(this,deviceId,lineId);
            finishPlace = PlaceQueryUtil.getPlaceByGEStatus(this,deviceId,lineId, ConfigInfo.CHECKED_STATUS);
            AlertDialog.Builder builder = new AlertDialog.Builder(NFCPlaceActivity.this);
            if(finishPlace.size()==placeList.size() && placeList.size()!=0) {
                builder.setMessage("巡检项已全部完成，确定要退出吗？")
                        .setPositiveButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }else{
                builder = new AlertDialog.Builder(NFCPlaceActivity.this)
                        .setMessage("有未完成的巡检项，确定要退出吗？")
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
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(NFCPlaceActivity.this)
                    .setMessage("确定要退出巡检吗？")
                    .setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            dialog.dismiss();
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

    }
    /*
   获取NFC数据
    */
    @SuppressLint("WrongConstant")
    private void identifyNfc(Intent intent) {
        //1.获取Tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
      //  Tag detectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Long cardNo = flipHexStr(ByteArrayToHexString(detectedTag.getId()));
        //if cardNo不为空，查询是否已经绑定过，未绑定的话，进行绑定
        List<InspectDevice> inspectDeviceList = InspectDevicQueryUtil.getInpectDeviceListByTime(NFCPlaceActivity.this);
        if(inspectDeviceList.size() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("请登录，获取今日待巡检设备！")
                    .setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(NFCPlaceActivity.this, StartUpActivity.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        else if (cardNo != null) {
//              Device device = DatabaseManager.getInstance().getDeviceDao().queryBuilder().where(DeviceDao.Properties.RFID.eq(cardNo)).unique();
            devices = DatabaseManager.getInstance().getDeviceDao().queryBuilder().where(DeviceDao.Properties.RFID.eq(cardNo)).list();
            //   Toast.makeText(this, "device:", Toast.LENGTH_SHORT).show();
            if(devices.size()==0){
                //未绑定
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false)
                        .setTitle("该nfc卡还未绑定设备，请先绑定！")
                        .setPositiveButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                builder.show();
            } else if (devices.size() ==1 ) {//已绑定
                Device device = devices.get(0);
                //    setTitle(device.getEquipmentName());
                topbar.leftButton.setText(device.getEquipmentName());
                //根据NFC卡绑定的设备，查询部位下
                inspectDevice = InspectDevicQueryUtil.getInpectDeviceByDeviceIDNo(this,device.getEquipmentID());
                if(inspectDevice == null){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setMessage("该设备，未添加为今日巡检项！")
                            .setPositiveButton("确定",new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    builder.show();

                }else{
                    lineId = inspectDevice.getLineID();
                    deviceId = inspectDevice.getEquipmentID();
                    //显示该设备下的部位
                    places = PlaceQueryUtil.getPlaceByDeviceId(this,deviceId,lineId);
                    Log.e("nfcplace",places.size()+"");
                    if(places.size()!=0){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                        layoutManager.setOrientation(OrientationHelper.VERTICAL);
                        rvLine.setLayoutManager(layoutManager);
                        nfcLineAdapter = new NFCLineAdapter(this, places);
                        nfcLineAdapter.setOnItemClickListener(this);
                        rvLine.setAdapter(nfcLineAdapter);
                    }else{
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                .setMessage("未获取到部位信息，请刷新数据试一下！")
                                .setPositiveButton("确定", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        builder.show();
                    }
//                    Log.e("nfc",deviceId+","+cardNo);
                    //完成，提示已经完成的AlertDialog
                    isShowInspectFinishAlartDialog(deviceId,lineId);
                }
            }else if(devices.size() >1){
                topbar.leftButton.setText("RFID设备");
                rvLine.setVisibility(View.INVISIBLE);
                rvDevice.setVisibility(View.VISIBLE);
                //设置布局管理器
                LinearLayoutManager layoutManager = new LinearLayoutManager(NFCPlaceActivity.this);
                layoutManager.setOrientation(OrientationHelper.VERTICAL);
                rvDevice.setLayoutManager(layoutManager);
                adapterData = InspectDevicQueryUtil.getInpectDevicesByRFID(NFCPlaceActivity.this,cardNo.toString());;
                List<InspectDevice> checkedDevice = InspectDevicQueryUtil.getInspectByRFIDAndStatus(NFCPlaceActivity.this,cardNo.toString(), ConfigInfo.CHECKED_STATUS);;
                Log.e("nfcplace","deviceDate:"+adapterData.size()+checkedDevice.size());
                adapter = new InspectAdapter(NFCPlaceActivity.this, adapterData);
                rvDevice.setAdapter(adapter);
                adapter.setOnItemClickListener(NFCPlaceActivity.this);
                if(checkedDevice.size() == adapterData.size() && checkedDevice.size()!=0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setMessage("该设备已经巡检完毕,【已检设备】中可查看详情！")
                            .setPositiveButton("确定", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mContext, FinishActivity.class);
                                    startActivity(intent);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });

                    builder.show();
                }

            }
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

    }
    @Override
    protected void onUserLeaveHint() {
     //   super.onUserLeaveHint();
   //     Toast.makeText(this, "UserLeaveHint", Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
        //查询是否巡检完毕。2/3表示巡检。1表示待巡检。
        List<Item> itemsStatus = ItemQueryUtil.getItemByStatus(this,placeId,lineId,ConfigInfo.ITEM_UNCHECK_STATUS);
        //查询该部位Place下巡检项
        List<Item> itemsList = ItemQueryUtil.getItemByPlaceId(this,placeId,lineId);
        if(itemsList.size() == 0){
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
        }
        //已经全部巡检完毕，显示出所有的巡检项和结果值界面
        if(itemsStatus.size() == 0){
            Intent intent = new Intent(this,NFCInspectActivity.class);
            intent.putExtra("placeid",placeId);
            intent.putExtra("lineid",lineId);
            intent.putExtra("placeName",placeName);
            startActivity(intent);
        } else{
            Intent intent;
            int checkType = itemsStatus.get(0).getCheckType();
            long itemId = itemsStatus.get(0).getItemID();
            switch (checkType){
                case (ConfigInfo.ITME_TYPE_VIBRATE):
                case(ConfigInfo.ITME_TYPE_TEMPERATURE):
                    boolean isOpen = SharedPreferenceUtil.isOpenBlueVib(this,"isBlue");
                    if(isOpen){
                        intent = new Intent(this, VibraBlueActivity.class);
                    }else{
                        intent = new Intent(this,MainActivity.class);
                    }
                    //点击的位置
                    //项id
                    intent.putExtra("name",placeName);
                    intent.putExtra("placeId",placeId);
                    intent.putExtra("itemId",itemId);
                    intent.putExtra("lineId",lineId);
                    startActivityForResult(intent,ITME_OBSERVE_VALUE);
                    break;
                case (ConfigInfo.ITME_TYPE_OBSERVE):
                    intent = new Intent(this,ObservedActivity.class);
                    intent.putExtra("name",placeName);
                    intent.putExtra("placeId",placeId);
                    intent.putExtra("itemId",itemId);
                    intent.putExtra("lineId",lineId);
                    startActivityForResult(intent,ITME_OBSERVE_VALUE);
                    break;
                case(ConfigInfo.ITME_TYPE_READMETER):
                    intent = new Intent(this, ReadMeterActivity.class);
                    intent.putExtra("name",placeName);
                    intent.putExtra("placeId",placeId);
                    intent.putExtra("itemId",itemId);
                    intent.putExtra("lineId",lineId);
                    startActivityForResult(intent,ITME_OBSERVE_VALUE);
                    break;
                case(ConfigInfo.ITME_TYPE_VIBRATE_TEMP):
                    break;
                default:
                    break;
            }

        }
    }
    /*
    条目点击事件，多个设备下
     */
    @Override
    public void onClick(Long deviceId, Long lineId, int position) {
        Intent intent = new Intent(mContext, InspectPlaceActivity.class);
        intent.putExtra("deviceId",deviceId);
        intent.putExtra("lineId",lineId);
        startActivity(intent);
    }
}
