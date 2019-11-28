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
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.InspectDeviceDao;
import com.yxst.mes.database.dao.LineDao;
import com.yxst.mes.database.model.InspectDevice;
import com.yxst.mes.database.model.Item;
import com.yxst.mes.database.model.ItemValue;
import com.yxst.mes.database.model.Line;
import com.yxst.mes.database.Manager.InspectDevicQueryUtil;
import com.yxst.mes.database.Manager.ItemQueryUtil;
import com.yxst.mes.database.Manager.ItemValueQueryUtil;
import com.yxst.mes.database.Manager.PlaceQueryUtil;
import com.yxst.mes.database.Manager.PlaceValueQueryUtil;
import com.yxst.mes.database.model.Place;
import com.yxst.mes.database.model.PlaceValue;
import com.yxst.mes.fragment.ConfigInfo;
import com.yxst.mes.net.RestCreator;
import com.yxst.mes.nfc.adapter.NFCLineAdapter;
import com.yxst.mes.rx.manager.BindDeviceManager;
import com.yxst.mes.view.Topbar;

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

import static android.content.DialogInterface.OnClickListener;

public class NFCLineActivity extends AppCompatActivity implements NFCLineAdapter.OnItemClickListener {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    @BindView(R.id.lv_nfc_place) RecyclerView rvLine;
    @BindView(R.id.tv_nfc_pname) TextView deviceName;
    @BindView(R.id.topbar) Topbar topbar;
    private InspectDeviceDao inspectDevicedao;
    private List<Place> places;
    private Long lineId;
    private static final int INSPECT_RESULTL = 1;
    private NFCLineAdapter nfcLineAdapter;
    private Line inspectLine;
    private InspectDevice inspectDevice;

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
        identifyNfc();
        BindDeviceManager.obtainline(this);
        topbar.setOnClickListener(new Topbar.TopbarOnclickListener() {
            @Override
            public void onLeftClick() {

            }
            @Override
            public void onRightClick() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(NFCLineActivity.this)
                        .setMessage("确定要报备停机吗？")
                        .setPositiveButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, Object> params = new LinkedHashMap<>();
                                params.put("LineID",inspectDevice.getLineID());
                                params.put("BeginTime",inspectDevice.getBeginTime());
                                params.put("EndTime",inspectDevice.getEndTime());
                                params.put("RunStates","1");
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), new Gson().toJson(params));
                                Observable statusValue = RestCreator.getRxRestService().stopLineStatus(requestBody);
                                statusValue.subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer() {
                                            @Override
                                            public void accept(Object o) throws Exception {
                                                if(Integer.valueOf(o.toString())>0){
                                                    List<InspectDevice> inspectDevices = InspectDevicQueryUtil.getByLineID(NFCLineActivity.this,inspectDevice.getLineID());
                                                    for(InspectDevice device : inspectDevices){
                                                        device.setRunStates(1);
                                                        InspectDevicQueryUtil.updateInpectDevice(device);
                                                    }
                                                    Toast.makeText(NFCLineActivity.this, "停机已发送！" +
                                                            "", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                Toast.makeText(NFCLineActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

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

                   places = PlaceQueryUtil.getPlaceByLineId(this,line.getID());
                   Log.e("nfcline",places.size()+"");
                   //设置布局管理器
                   LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                   layoutManager.setOrientation(OrientationHelper.VERTICAL);
                   rvLine.setLayoutManager(layoutManager);
                   nfcLineAdapter = new NFCLineAdapter(this, places);
                   nfcLineAdapter.setOnItemClickListener(this);
                   rvLine.setAdapter(nfcLineAdapter);
                   //完成，提示已经完成的AlertDialog
                   isShowInspectFinishAlartDialog(inspectDevice.getEquipmentID(),inspectDevice.getLineID());

               }
            }
        }

    }

    private void isShowInspectFinishAlartDialog(Long deviceId,Long lineId){
        List<Place> placeList = PlaceQueryUtil.getPlaceByLineId(this,lineId);
        List<PlaceValue> finishList = PlaceQueryUtil.getPlaceValueByStatus(this,lineId,1);
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
                List<ItemValue> finishItems = ItemValueQueryUtil.getItemValueByPIdAndStatus(this,place.getPlaceID(),place.getLineID(),1);
         //       Toast.makeText(this, ""+items.size()+","+finishItems.size(), Toast.LENGTH_SHORT).show();
                if(items.size()!=0&&finishItems.size()!=0&&items.size() == finishItems.size()){
                    PlaceValueQueryUtil.savePlaceValue(this,place);
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
        Intent intent;
        List<Item> items = ItemQueryUtil.getItemByPlaceId(this,placeId,lineId);
        if(items.size() != 0){
            int checkType = items.get(0).getCheckType();//.NumberFormatException: Invalid int: ""
            switch (checkType){
                case (ConfigInfo.ITME_TYPE_OBSERVE):
                    intent = new Intent(this,ObserveActivity.class);
                    intent.putExtra("name",placeName);
                    intent.putExtra("lineId",lineId);
                    intent.putExtra("placeId",placeId);
                    intent.putExtra("itemId",items.get(0).getItemID());
                    startActivity(intent);
//                    startActivityForResult(intent,ConfigInfo.ITME_TYPE_OBSERVE);//2观察型
                    break;
                case (ConfigInfo.ITME_TYPE_VIBRATE):
                    intent = new Intent(this,MainActivity.class);
                    intent.putExtra("name",items.get(0).getCheckContent());
                    intent.putExtra("lineId",lineId);
                    intent.putExtra("placeId",placeId);
                    intent.putExtra("itemId",items.get(0).getItemID());
                    startActivity(intent);
                    // startActivityForResult(intent,ConfigInfo.ITME_TYPE_OBSERVE);
                    break;

                case(ConfigInfo.ITME_TYPE_TEMPERATURE):
                    intent = new Intent(this,MainActivity.class);
                    intent.putExtra("name",placeName);
                    intent.putExtra("itemId",items.get(0).getItemID());
                    intent.putExtra("lineId",lineId);
                    intent.putExtra("placeId",placeId);
                    startActivity(intent);
                    break;

                case(ConfigInfo.ITME_TYPE_READMETER):
                    intent = new Intent(this,ObserveActivity.class);
                    intent.putExtra("name",placeName);
                    intent.putExtra("lineId",lineId);
                    intent.putExtra("placeId",placeId);
                    intent.putExtra("itemId",items.get(0).getItemID());
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    }
}
