package com.yxst.mes.nfc;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yxst.mes.R;
import com.yxst.mes.activity.ReadMeterActivity;
import com.yxst.mes.activity.SignInActivity;
import com.yxst.mes.activity.StartUpActivity;
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.Manager.InspectDevicValueQueryUtil;
import com.yxst.mes.database.Manager.RecordQueryUtil;
import com.yxst.mes.database.dao.DeviceDao;
import com.yxst.mes.database.dao.InspectDeviceDao;
import com.yxst.mes.database.model.Device;
import com.yxst.mes.database.model.InspectDevice;
import com.yxst.mes.database.model.Item;
import com.yxst.mes.database.model.ItemValue;
import com.yxst.mes.database.Manager.InspectDevicQueryUtil;
import com.yxst.mes.database.Manager.ItemQueryUtil;
import com.yxst.mes.database.Manager.ItemValueQueryUtil;
import com.yxst.mes.database.Manager.PlaceQueryUtil;
import com.yxst.mes.database.Manager.PlaceValueQueryUtil;
import com.yxst.mes.database.model.Place;
import com.yxst.mes.database.model.PlaceValue;
import com.yxst.mes.database.model.Record;
import com.yxst.mes.fragment.ConfigInfo;
import com.yxst.mes.net.RestCreator;
import com.yxst.mes.nfc.adapter.NFCLineAdapter;
import com.yxst.mes.util.TitleBarUtil;
import com.yxst.mes.view.Topbar;

import java.util.ArrayList;
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

public class NFCPlaceActivity extends AppCompatActivity implements NFCLineAdapter.OnItemClickListener {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    @BindView(R.id.lv_nfc_place)
    RecyclerView rvLine;
    @BindView(R.id.tv_nfc_pname) TextView deviceName;
    @BindView(R.id.topbar) Topbar topbar; 
  //
  //  private DeviceDao dao;
    private InspectDeviceDao inspectDevicedao;
    private List<Place> places;
    private Long lineId;
    private Long deviceId;
    private static final int INSPECT_RESULTL = 1;
    private static final int ITME_OBSERVE_VALUE = 0;
 //   private NFCPlaceAdapter nfcPlaceAdapter;
    private NFCLineAdapter nfcLineAdapter;
    private InspectDevice inspectDevice;
    private  List<PlaceValue> finishList;
    private List<Place> placeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TitleBarUtil.titlebarSetting(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_nfc_place);
        ButterKnife.bind(this);
//        dao = DatabaseManager.getInstance().getDeviceDao();
        inspectDevicedao = DatabaseManager.getInstance().getInspectDeviceDao();
        identifyNfc();
    
        topbar.setOnClickListener(new Topbar.TopbarOnclickListener() {
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
                                Map<String, Object> params = new LinkedHashMap<>();
                                params.put("EquipmentID",inspectDevice.getEquipmentID());
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
            //    Observable statusValue = RestCreator.getRxRestService().stopStatus(jsonStr);


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
                List<ItemValue> finishItems = ItemValueQueryUtil.getItemValueByPIdAndStatus(this,place.getPlaceID(),place.getLineID(),1);
                if(items.size() == finishItems.size()){
                    PlaceValueQueryUtil.savePlaceValue(this,place);
                }
            }
        }
        Log.e("nfcPlace:","onResume()");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("nfcPlace:","onPause()");
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.e("nfc", intent.getAction());
        identifyNfc();

    }

    //获取NFC数据
    private void identifyNfc() {

        List<InspectDevice> inspectDeviceList = InspectDevicQueryUtil.getInpectDeviceListByTime(NFCPlaceActivity.this);
        //1.获取Tag对象
        Tag detectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Long cardNo = flipHexStr(ByteArrayToHexString(detectedTag.getId()));
        //if cardNo不为空，查询是否已经绑定过，未绑定的话，进行绑定
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
             List<Device> devices = DatabaseManager.getInstance().getDeviceDao().queryBuilder().where(DeviceDao.Properties.RFID.eq(cardNo)).list();

         //   Toast.makeText(this, "device:", Toast.LENGTH_SHORT).show();
            if(devices.size() == 0  ){
                   //未绑定
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false)
                            .setTitle("该nfc卡还未绑定设备，请先绑定！")
                            .setPositiveButton("确定", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    builder.show();
            } else if (devices.size() ==1 ) {//已绑定
                Device device = devices.get(0);
            //    setTitle(device.getEquipmentName());
                topbar.leftButton.setText(device.getEquipmentName());
                List<InspectDevice> inspectDevices = InspectDevicQueryUtil.getInpectDeviceListByTime(NFCPlaceActivity.this);
                if(inspectDevices.size()==0){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setMessage("请登录，获取今日巡检项！")
                            .setPositiveButton("确定",new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(NFCPlaceActivity.this, StartUpActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    builder.show();
                }
                //根据NFC卡绑定的设备，查询部位下
               inspectDevice = InspectDevicQueryUtil.getInpectDeviceByDeviceIDNo(this,device.getEquipmentID());
               if(inspectDevice == null){
                   final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                           .setMessage("该设备，未添加为今日巡检项！")
                           .setPositiveButton("确定",new DialogInterface.OnClickListener(){

                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   finish();
                               }
                           });
                   builder.show();

               }else{

                   lineId = inspectDevice.getLineID();
                   deviceId = inspectDevice.getEquipmentID();
                   places = PlaceQueryUtil.getPlaceByDeviceId(this,inspectDevice.getEquipmentID(),inspectDevice.getLineID());
                   //        DatabaseManager.getInstance().getPlaceDao()._queryInspectDevice_PlaceList(inspectDevice.getEquipmentID());
                   Log.e("nfcplace",places.size()+"");
                   if(places.size()!=0){

                       LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                       layoutManager.setOrientation(OrientationHelper.VERTICAL);
                       rvLine.setLayoutManager(layoutManager);
                       nfcLineAdapter = new NFCLineAdapter(this, places);
                       nfcLineAdapter.setOnItemClickListener(this);
                       rvLine.setAdapter(nfcLineAdapter);
                   }
                   //完成，提示已经完成的AlertDialog
                   isShowInspectFinishAlartDialog(inspectDevice.getEquipmentID(),inspectDevice.getLineID());

               }
            }else if(devices.size() >1){
                StringBuffer deviceStr = new StringBuffer();
                for(Device device : devices){
                    deviceStr.append(device.getEquipmentName()).append(",");
                }
                deviceStr.deleteCharAt(deviceStr.length()-1).toString();
                final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("该卡重复绑定，请修改！"+"\n"+"绑定了: "+deviceStr)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.show();
            }
        }

    }

    private void isShowInspectFinishAlartDialog(Long deviceId,Long lineId){
        placeList = PlaceQueryUtil.getPlaceByDeviceId(this,deviceId,lineId);
        finishList = PlaceQueryUtil.getPlaceValueByStatus(this,deviceId,lineId,1);
 //     Toast.makeText(this, placeList.size()+"+"+finishPlace.size(), Toast.LENGTH_SHORT).show();
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
        List<Record> recordList = RecordQueryUtil.getRecordListByDeviceId(this,deviceId,lineId,ConfigInfo.RECORD_UPLOAD_STATUS);
        List<Item> itemList = ItemQueryUtil.getItemByDeviceId(this,deviceId,lineId);
        if(itemList.size() == recordList.size()&&itemList.size()!=0){
            InspectDevice device = InspectDevicQueryUtil.getInpectDeviceByDeviceID(this, deviceId,lineId);
            device.setUploadStatus(1);//表示都已上传
            InspectDevicQueryUtil.updateInpectDevice(device);
            InspectDevicValueQueryUtil.saveInspectDeviceValue(this,device,ConfigInfo.ITEM_UPLOAD_STATUS);

        }
    }
    @Override
    public void onBackPressed() {
    //    placeList = PlaceQueryUtil.getPlaceByDeviceId(this,deviceId,lineId);
        finishList = PlaceQueryUtil.getPlaceValueByStatus(this,deviceId,lineId,1);
        AlertDialog.Builder builder = new AlertDialog.Builder(NFCPlaceActivity.this);
        if(finishList.size()==placeList.size() && placeList.size()!=0) {
            builder.setMessage("巡检项已全部完成，确定要退出吗？")
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
        List<Item> items = ItemQueryUtil.getItemByPIdAndStatus(this,placeId,lineId,0);

        //   Toast.makeText(NFCPlaceActivity.this,"Tast："+position,Toast.LENGTH_LONG).show();
       List<Item> itemsList = ItemQueryUtil.getItemByPlaceId(this,placeId,lineId);
        //     Toast.makeText(this, items.size()+",placeId查询items"+placeId, Toast.LENGTH_SHORT).show();
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
        }else if(items.size() == 0){

            Intent intent = new Intent(this,NFCInspectActivity.class);
            intent.putExtra("placeid",placeId);
            intent.putExtra("lineid",lineId);
            intent.putExtra("placeName",placeName);
            startActivity(intent);
        }
        else{
            //         Toast.makeText(this, "placeId="+placeId, Toast.LENGTH_SHORT).show();
            //startActivityForResult(intent,INSPECT_RESULT);
            Intent intent;
            int checkType = items.get(0).getCheckType();
            long itemId = items.get(0).getItemID();
            switch (checkType){
                case (ConfigInfo.ITME_TYPE_VIBRATE):
                    intent = new Intent(this,MainActivity.class);
                    //点击的位置
                    //项id
                    intent.putExtra("name",placeName);
                    intent.putExtra("itemId",itemId);
                    intent.putExtra("placeId",placeId);
                    intent.putExtra("lineId",lineId);
                    startActivityForResult(intent,ITME_OBSERVE_VALUE);
                    break;
                case(ConfigInfo.ITME_TYPE_TEMPERATURE):
                    intent = new Intent(this,MainActivity.class);
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

}
