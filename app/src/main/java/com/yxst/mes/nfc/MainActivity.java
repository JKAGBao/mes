package com.yxst.mes.nfc;


import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.yxst.mes.MyApplication;
import com.yxst.mes.R;
import com.yxst.mes.activity.BaseActivity;
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.GradeDao;
import com.yxst.mes.database.dao.ItemDao;
import com.yxst.mes.database.dao.RecordDao;
import com.yxst.mes.database.model.Grade;
import com.yxst.mes.database.model.InspectDevice;
import com.yxst.mes.database.model.Item;
import com.yxst.mes.database.Manager.InspectDevicQueryUtil;
import com.yxst.mes.database.Manager.InspectDevicValueQueryUtil;
import com.yxst.mes.database.Manager.ItemQueryUtil;
import com.yxst.mes.database.Manager.ItemValueQueryUtil;
import com.yxst.mes.database.Manager.RecordQueryUtil;
import com.yxst.mes.database.model.Record;
import com.yxst.mes.fragment.ConfigInfo;
import com.yxst.mes.nfc.vib.WifiAPManager;
import com.yxst.mes.rx.manager.RecordManager;
import com.yxst.mes.util.SharedPreferenceUtil;
import com.yxst.mes.util.TimeUtil;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import vib.rts.mylibrary.Interface;
import vib.rts.mylibrary.VibUtils;


public class MainActivity extends BaseActivity implements Interface {

    private RecordDao recorddao;
    private ItemDao itemdao;
    private Long itemId;
    private Long placeId;
    private Long lineId;
    private TextView collect;
    private TextView zt;
    private TextView Power;
    private TextView temperature;
    private TextView vib;//振动值
    private TextView save;
   // private TextView ip;
   // private TextView port;
  //  private ImageButton btn_return;
    private KProgressHUD hud;
    MyApplication application;
    VibUtils vibUtils=new VibUtils();

    private GraphicalView mChart;
    private  GraphicalView WaveChart;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private  XYMultipleSeriesDataset WaveDataset = new XYMultipleSeriesDataset();
    private  XYMultipleSeriesRenderer WaveRenderer = new XYMultipleSeriesRenderer();
    private XYSeries WaveCurrentSeries;
    private XYSeriesRenderer WaveCurrentRenderer;
    private  XYSeries mCurrentSeries;
    private  XYSeriesRenderer mCurrentRenderer;
    public double VoltageLevel;
    private Record record;
    private Item item;
    private String placeName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_main );
        ButterKnife.bind(this);
        application=(MyApplication)getApplication();
        collect = (TextView) findViewById( R.id.vib );
        zt = (TextView) findViewById( R.id.zt );
        Power = (TextView) findViewById( R.id.teV_Power );
        temperature = (TextView) findViewById( R.id.textView_temperature );
        vib = (TextView) findViewById( R.id.textView_RMS );
        save = (TextView)findViewById(R.id.save);
//        ip = (TextView) findViewById( R.id.ip );
//        port = (TextView) findViewById( R.id.port );
//        btn_return=(ImageButton) findViewById( R.id.btn_return );
        titleInit();//标题初始化
        init();
        //获取之前数据
        placeName = getIntent().getStringExtra("name");
        itemId = getIntent().getLongExtra("itemId",1);
        placeId = getIntent().getLongExtra("placeId",1);
        lineId = getIntent().getLongExtra("lineId",1);
        item = ItemQueryUtil.getItemById(this,itemId,lineId);
        if(item!=null){
            setTitle(item.getCheckContent());
        }else{
            setTitle("测温测振采集");
        }
//        setTitle(placeName+":"+item.getCheckContent());
        List<Record> records = RecordQueryUtil.getRecordListByItemId(this,itemId,lineId);
        if(records.size()!=0){
            record = records.get(0);
            if(item.getCheckType()==ConfigInfo.ITME_TYPE_VIBRATE){
                vib.setText(record.getCheckValue());
            } else if(item.getCheckType()==ConfigInfo.ITME_TYPE_TEMPERATURE){
                temperature.setText(record.getCheckValue());
            }
        }
        zt.setText( "未连接" );
        collect.setText( "设备连接" );
//        ip.setText( application.getIP() );
//        port.setText( application.getHost() );
        double Factor=application.getFactor();
        byte[] SendData=null;
        if(!isWifiApOpen( getApplicationContext())) {
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                showApDialogOnM();
            }else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                showRequestApDialogOnN_MR1();
            }
        }else {
            hud = KProgressHUD.create( MainActivity.this )
                    .setStyle( KProgressHUD.Style.SPIN_INDETERMINATE )
                    .setCancellable( true )
                    .setAnimationSpeed( 2 )
                    .setDimAmount( 0.5f );
            hud.show();

            Boolean Result=vibUtils.connect(SendData, application.getIP(), application.getHost(),Factor,MainActivity.this);
            if(Result){
                zt.setText( "连接" );
                collect.setText( "数据采集" );
            }else {
                zt.setText( "未连接" );
                collect.setText( "设备连接" );
            }
            hud.dismiss();
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveValue();
                nextInspect();
                finish();
            }
        });
        collect.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String IP=ip.getText().toString();
//                String Port=port.getText().toString();
//                application.setIP( IP );
//                application.setHost( Port );
                double Factor=application.getFactor();
                byte[] SendData=null;

                hud = KProgressHUD.create(MainActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f);
                hud.show();
                if(zt.getText().toString().equals( "连接" )){
                    Boolean Result=vibUtils.VibrationMeasure( SendData,application.getIP(),application.getHost(),Factor,MainActivity.this );
                    if(Result){
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                zt.setText( "连接" );
                                collect.setText( "数据采集" );
                            }
                        } );

                    }else {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                zt.setText( "未连接" );
                                collect.setText( "设备连接" );
                            }
                        } );

                        hud.dismiss();
                    }
                }else {
                    Boolean Result=vibUtils.connect(SendData,application.getIP(),application.getHost(),Factor,MainActivity.this);
                    if(Result){
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                zt.setText( "连接" );
                                collect.setText( "数据采集" );

                            }
                        } );
                    }else {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                zt.setText( "未连接" );
                                collect.setText( "设备连接" );
                            }
                        } );
                    }
                    hud.dismiss();
                }

            }
        } );
    }
    //下一个巡检项逻辑
    private void nextInspect() {
        //查询要巡检的项
        List<Item> itemList = ItemQueryUtil.getItemByPIdAndStatus(this,placeId,lineId,0);
        if(itemList.size()!=0){
            Item localItem = itemList.get(0);
            int checkType = localItem.getCheckType();//.NumberFormatException: Invalid int: ""
            switch (checkType){
                case (ConfigInfo.ITME_TYPE_VIBRATE):
                case(ConfigInfo.ITME_TYPE_TEMPERATURE):
                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                    intent.putExtra("itemId",localItem.getItemID());
                    intent.putExtra("lineId",localItem.getLineID());
                    intent.putExtra("placeId",placeId);
                    intent.putExtra("name",localItem.getCheckContent());
                    startActivity(intent);
                    finish();
                    // startActivityForResult(intent,ConfigInfo.ITME_TYPE_OBSERVE);
                    break;
                case (ConfigInfo.ITME_TYPE_OBSERVE):

                    intent = new Intent(MainActivity.this,ObserveActivity.class);
                    intent.putExtra("itemId",localItem.getItemID());
                    intent.putExtra("lineId",localItem.getLineID());
                    intent.putExtra("placeId",placeId);
                    intent.putExtra("name",placeName);
                    startActivity(intent);
                    finish();
                    break;

                case(ConfigInfo.ITME_TYPE_READMETER):
                    break;
                case(ConfigInfo.ITME_TYPE_VIBRATE_TEMP):
                    break;
                default:
                    break;
            }
        }else{
            finish();
        }
    }
    private void showApDialogOnM() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        //   builer.setTitle( "开启WIFI热点" );
        builer.setMessage("点击确定，开启wifi热点，等待设备连接！");
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    //注册handler
                    WifiAPManager.getInstance(MainActivity.this);
                    //开启wifi热点
                    WifiAPManager.getInstance(MainActivity.this).turnOnWifiAp(application.getWifiName(), application.getWifikey(), WifiAPManager.WifiSecurityType.WIFICIPHER_WPA);
                } catch (Exception ex) {
                }
            }
        });
        builer.setNegativeButton("取消", null);
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    private void showRequestApDialogOnN_MR1() {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setMessage("1.开启传感器\n" +
                          "2.开启热点，并设置\n   热点名称：RTS\n   热点密码：00000000\n" +
                          "3.指示灯闪烁后，点击设备连接，等待连接\n");
        dialog.setPositiveButton("去开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                openAP();
            }
        });
        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //    finishBack();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    //打开系统的便携式热点界面
    private void openAP() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        ComponentName com = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
        intent.setComponent(com);
        startActivityForResult(intent, 1000);
    }

    public void init(){
        LinearLayout layout = (LinearLayout)findViewById( R.id.chart );
        LinearLayout layoutWave = (LinearLayout)findViewById( R.id.Wavechart );
        if (mChart == null) {
            initChart();
            initWaveChart();
            //mChart = ChartFactory.getCubeLineChartView(context, mDataset, mRenderer, 0.3f);
            mChart = ChartFactory.getLineChartView( this, mDataset, mRenderer );
//            WaveChart = ChartFactory.getCubeLineChartView(context, WaveDataset, WaveRenderer, 0.3f);
            WaveChart = ChartFactory.getLineChartView( this, WaveDataset, WaveRenderer );
            layout.addView( mChart );
            layoutWave.addView( WaveChart );
        } else {
            mChart.repaint();
            WaveChart.repaint();
        }
    }

    private void titleInit(){
        recorddao = DatabaseManager.getInstance().getRecordDao();
        itemdao = DatabaseManager.getInstance().getItemDao();

    }

    /*
     * 保存巡检项
     */
    private void saveValue(){
        if(temperature.getText().toString().equals("0.0") || vib.getText().toString().equals("0.0")){
            finish();
            return;
        }
        List<Record> records = RecordQueryUtil.getRecordListByItemId(this,itemId,lineId);
       //         recorddao.queryBuilder().where(RecordDao.Properties.InspectionItemID.eq(itemId)).list();
        if(records.size()!=0){
            record = records.get(0);
            InspectDevice inspectDevice = InspectDevicQueryUtil.getInpectDeviceByDeviceID(this,item.getEquipmentID(),item.getLineID());
            inspectDevice.setUploadStatus(0);
            InspectDevicQueryUtil.updateInpectDevice(inspectDevice);
            InspectDevicValueQueryUtil.saveStatus(MainActivity.this,inspectDevice,0,0);
            List<Item> items =  ItemQueryUtil.getItemByPlaceId(this,item.getPlaceID(),item.getLineID());
            //  Toast.makeText(this,items.size()+"place-item.size"+items.size(),Toast.LENGTH_LONG).show();
            for(int i=0;i<items.size();i++){
                Item itemType = items.get(i);
                if(item.getItemID() == itemType.getItemID()){
                    if(item.getCheckType()==ConfigInfo.ITME_TYPE_VIBRATE){
                        updateItemValue(record,item,vib.getText().toString());
                        if(i!=items.size()-1){
                            Item itemVib = items.get(i+1);
                            if(itemVib.getCheckType()==3){
                                Record vibrecords = recorddao.queryBuilder().where(RecordDao.Properties.InspectionItemID.eq(itemVib.getItemID())).list().get(0);
                                updateItemValue(vibrecords,itemVib,temperature.getText().toString());
                                return;
                            }else{
                                //如果+1有温度项获取，如果没有倒序查一遍
                                for(int j=i;j>=0;j--){
                                    Item itemvib2 = items.get(j);
                                    if(itemvib2.getCheckType()==3){
                                        Record vibrecords = recorddao.queryBuilder().where(RecordDao.Properties.InspectionItemID.eq(itemvib2.getItemID())).list().get(0);

                                        //    Toast.makeText(this,"==3：",Toast.LENGTH_LONG).show();
                                        updateItemValue(vibrecords,itemvib2,temperature.getText().toString());
                                        return;
                                    }
                                }
                            }
                        }else{
                            //最后一项振动值检测，同步温度。
                            for(int j=items.size()-1;j>=0;j--){
                                Item itemvib2 = items.get(j);
                                if(itemvib2.getCheckType()==3){
                                    Record vibrecords = RecordQueryUtil.getRecordByItemId(this,itemvib2.getItemID(),itemvib2.getLineID());
                                      //      recorddao.queryBuilder().where(RecordDao.Properties.InspectionItemID.eq(itemvib2.getItemID())).list().get(0);
                                    //           Toast.makeText(this,"==3：",Toast.LENGTH_LONG).show();
                                    updateItemValue(vibrecords,itemvib2,temperature.getText().toString());
                                    return;
                                }
                            }
                        }
                        return;
                    }else if(item.getCheckType()==ConfigInfo.ITME_TYPE_TEMPERATURE){
                        updateItemValue(record,item,temperature.getText().toString());
                        if(i!=items.size()-1){
                            Item itemVib = items.get(i+1);
                            if(itemVib.getCheckType()==1){
                                Record vibrecords = recorddao.queryBuilder().where(RecordDao.Properties.InspectionItemID.eq(itemVib.getItemID())).list().get(0);
                                updateItemValue(vibrecords,itemVib,vib.getText().toString());
                                return;
                            }else{
                                //如果+1有温度项获取，如果没有倒序查一遍
                                for(int j=i;j>=0;j--){
                                    Item itemvib2 = items.get(j);
                                    if(itemvib2.getCheckType()==1){
                                        Record vibrecords = RecordQueryUtil.getRecordByItemId(this,itemvib2.getItemID(),itemvib2.getLineID());
//                                                recorddao.queryBuilder().where(RecordDao.Properties.InspectionItemID.eq(itemvib2.getItemID())).list().get(0);
                                        //    Toast.makeText(this,"==3：",Toast.LENGTH_LONG).show();
                                        updateItemValue(vibrecords,itemvib2,vib.getText().toString());
                                        return;
                                    }
                                }
                            }
                        }else{
                            //最后一项振动值检测，同步温度。
                            for(int j=items.size()-1;j>=0;j--){
                                Item itemvib2 = items.get(j);
                                if(itemvib2.getCheckType()==1){
                                    Record vibrecords = RecordQueryUtil.getRecordByItemId(this,itemvib2.getItemID(),itemvib2.getLineID());
                                    //Toast.makeText(this,"==3：",Toast.LENGTH_LONG).show();
                                    updateItemValue(vibrecords,itemvib2,vib.getText().toString());
                                    return;
                                }
                            }
                        }
                        return;
                    }
                }
            }
        }else{
            Record newRecord = new Record();
            List<Item> items =  ItemQueryUtil.getItemByPlaceId(this,item.getPlaceID(),item.getLineID());
            //  Toast.makeText(this,items.size()+"place-item.size"+items.size(),Toast.LENGTH_LONG).show();
            for(int i=0;i<items.size();i++){
                Item itemType = items.get(i);
                if(item.getItemID() == itemType.getItemID()){
                    if(item.getCheckType()==ConfigInfo.ITME_TYPE_VIBRATE){
                        saveItemValue(item,vib.getText().toString());
                        if(i!=items.size()-1){
                            Item itemVib = items.get(i+1);
                            if(itemVib.getCheckType()==3){
                                saveItemValue(itemVib,temperature.getText().toString());
                                return;
                            }else{
                                //如果+1有温度项获取，如果没有倒序查一遍
                                for(int j=i;j>=0;j--){
                                    Item itemvib2 = items.get(j);
                                    if(itemvib2.getCheckType()==3){
                                        //    Toast.makeText(this,"==3：",Toast.LENGTH_LONG).show();
                                        saveItemValue(itemvib2,temperature.getText().toString());
                                        return;
                                    }
                                }
                            }
                        }else{
                            //最后一项振动值检测，同步温度。
                            for(int j=items.size()-1;j>=0;j--){
                                Item itemvib2 = items.get(j);
                                if(itemvib2.getCheckType()==3){
                                    //           Toast.makeText(this,"==3：",Toast.LENGTH_LONG).show();
                                    saveItemValue(itemvib2,temperature.getText().toString());
                                    return;
                                }
                            }
                        }
                        return;
                    }else if(item.getCheckType()==ConfigInfo.ITME_TYPE_TEMPERATURE){
                        saveItemValue(item,temperature.getText().toString());
                        if(i!=items.size()-1){
                            Item itemVib = items.get(i+1);
                            if(itemVib.getCheckType()==1){
                                saveItemValue(itemVib,vib.getText().toString());
                                return;
                            }else{
                                //如果+1有温度项获取，如果没有倒序查一遍
                                for(int j=i;j>=0;j--){
                                    Item itemvib2 = items.get(j);
                                    if(itemvib2.getCheckType()==1){
                                        //    Toast.makeText(this,"==3：",Toast.LENGTH_LONG).show();
                                        saveItemValue(itemvib2,vib.getText().toString());
                                        return;
                                    }
                                }
                            }
                        }else{
                            //最后一项振动值检测，同步温度。
                            for(int j=items.size()-1;j>=0;j--){
                                Item itemvib2 = items.get(j);
                                if(itemvib2.getCheckType()==1){
                                    //Toast.makeText(this,"==3：",Toast.LENGTH_LONG).show();
                                    saveItemValue(itemvib2,vib.getText().toString());
                                    return;
                                }
                            }
                        }
                        return;
                    }
                }
            }
        }


    }
    private void updateItemValue(Record record,Item item,String checkValue) {

        //上传后台项
        record.setCheckValue(checkValue);
        //标准值换为数值型
        float standard = Float.valueOf(item.getStandardValue());
        float value = Float.valueOf(checkValue);
        List<Grade> gradeList = getItemGrade(item, value);
//        Toast.makeText(this, ""+gradeList.size()+","+gradeList.get(0).getGradeCode(), Toast.LENGTH_SHORT).show();
        if(gradeList.size()!=0){
            Grade grade = gradeList.get(0);
            record.setCheckConclusion(grade.getGradeCode()); //5正常1234异常
        }else{
            record.setCheckConclusion((standard>=value)?5:1); //5正常1234异常
        }
        String name = SharedPreferenceUtil.getName(this,"User");
        Long id = SharedPreferenceUtil.getId(this,"User");
        record.setCheckUser(name);
        record.setUserId(id);
        record.setRemark("");
        record.setInputDate(TimeUtil.dateFormat(new Date()));
        record.setDangerTitle(placeName+item.getCheckContent());
        record.setEquipmentID(item.getEquipmentID());
        record.setPlanId(item.getPlanID());
        record.setLineID(item.getLineID());
        record.setInspectionItemID(item.getItemID());
        InspectDevice device = InspectDevicQueryUtil.getInpectDeviceByDeviceID(this,item.getEquipmentID(),item.getLineID());
        record.setBeginTime(TimeUtil.dateTimeFormat(device.getBeginTime()));
        record.setEndTime(TimeUtil.dateTimeFormat(device.getEndTime()));
        record.setCheckStatus(1);//1待2已巡检3已上传
        recorddao.update(record);
        List<Record> records = new ArrayList<>();
        records.add(record);
        RecordManager.realPostRecord(MainActivity.this,records);
        //保存item
        Item updateItem = ItemQueryUtil.getItemById(this,item.getItemID(),item.getLineID());
        item.setCheckValue(checkValue);//测试的结果值
        item.setCheckStatus(ConfigInfo.ITME_CHECKED_STATUS);
        item.setInpsectStatus(ConfigInfo.ITEM_INSPECT_STATUS);
        itemdao.update(updateItem);
        ItemValueQueryUtil.saveItemValue(this,updateItem,checkValue);
    }
    private void saveItemValue(Item item,String checkValue) {
        Record record = new Record();
        //上传后台项
        record.setCheckValue(checkValue);
        //标准值换为数值型
        float standard = Float.valueOf(item.getStandardValue());
        float value = Float.valueOf(checkValue);
        List<Grade> gradeList = getItemGrade(item, value);
//        Toast.makeText(this, ""+gradeList.size()+","+gradeList.get(0).getGradeCode(), Toast.LENGTH_SHORT).show();
        if(gradeList.size()!=0){
            Grade grade = gradeList.get(0);
            record.setCheckConclusion(grade.getGradeCode()); //5正常1234异常
        }else{
            record.setCheckConclusion((standard>=value)?5:1); //5正常1234异常
        }
        String name = SharedPreferenceUtil.getName(this,"User");
        Long id = SharedPreferenceUtil.getId(this,"User");
        record.setCheckUser(name);
        record.setUserId(id);
        record.setRemark("");
        record.setInputDate(TimeUtil.dateFormat(new Date()));
        record.setDangerTitle(placeName+item.getCheckContent());
        record.setEquipmentID(item.getEquipmentID());
        record.setPlanId(item.getPlanID());
        record.setLineID(item.getLineID());
        record.setInspectionItemID(item.getItemID());
        InspectDevice device = InspectDevicQueryUtil.getInpectDeviceByDeviceID(this,item.getEquipmentID(),item.getLineID());
        record.setBeginTime(TimeUtil.dateTimeFormat(device.getBeginTime()));
        record.setEndTime(TimeUtil.dateTimeFormat(device.getEndTime()));
        record.setCheckStatus(1);//1待2漏3已巡检
        recorddao.insert(record);
        List<Record> records = new ArrayList<>();
        records.add(record);
        RecordManager.realPostRecord(MainActivity.this,records);

        //保存item
        Item updateItem = ItemQueryUtil.getItemById(this,item.getItemID(),item.getLineID());
        item.setCheckValue(checkValue);//测试的结果值
        item.setCheckStatus(ConfigInfo.ITEM_INSPECT_STATUS);
        item.setInpsectStatus(ConfigInfo.ITEM_INSPECT_STATUS);
        itemdao.update(updateItem);
        ItemValueQueryUtil.saveItemValue(this,updateItem,checkValue);
    }

    private List<Grade> getItemGrade(Item item, float value) {
        return DatabaseManager.getInstance().getGradeDao().queryBuilder().where(
                            GradeDao.Properties.InspectionItemID.eq(item.getItemID()),
                            GradeDao.Properties.MinCheckValue.le(value),
                            GradeDao.Properties.MaxCheckValue.ge(value)).list();
    }

    @Override
    public void getUIData(final double n) {
        byte[] VibData=vibUtils.VibData();
        vibUtils.SendMsg( VibData );
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                temperature.setText("" + n);

            }
        });

    }

    @Override
    public void getData(double[] waveDatas, double[] spectrumDatas) {
        VibUtils vibUtils=new VibUtils();
        double RMS= vibUtils.setData(spectrumDatas);
        final float sp = new Float( new DecimalFormat( ".00" ).format(RMS ));
        //时域
        double max = GetMax( waveDatas );
        WaveRenderer.setYAxisMin( -max * 1.2 );
        WaveRenderer.setYAxisMax( max * 1.2 );
        WaveRenderer.setXAxisMin( 0 );
        WaveRenderer.setXAxisMax( 2000 );
        //频谱
        mRenderer.setYAxisMin( 0 );
        mRenderer.setYAxisMax( RMS * 1.2 );
        mRenderer.setXAxisMin( 0 );
        mRenderer.setXAxisMax( 1000 );

        mCurrentSeries.clear();
        WaveCurrentSeries.clear();


        for (int i = 0; i < 2000; i++) {
            VoltageLevel = spectrumDatas[i];
            mCurrentSeries.add( 0.5 * i, VoltageLevel );
        }
        //时域波形开始
        for (int i = 0; i < waveDatas.length; i++) {
            VoltageLevel = waveDatas[i];
            WaveCurrentSeries.add( 0.4 * i, VoltageLevel );
        }

        //时域波形结束
        mChart.repaint();
        WaveChart.repaint();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vib.setText( ""+sp );
            }
        });
        hud.dismiss();
    }


    public  double GetMax(double[] array) {
        double Max = 0;
        for (int i = 0; i < array.length; i++) {
            if(Max<array[i])
            {
                Max = array[i];
            }
        }
        return Max;
    }


    @Override
    public void getSuccess(String msg) {

    }

    @Override
    public void getPower(final int power) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Power.setText("" + power+"%");
                zt.setText( "连接" );
                collect.setText( "数据采集" );
            }
        });
    }


    public  boolean isWifiApOpen(Context context) {
        try {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            //通过放射获取 getWifiApState()方法
            Method method = manager.getClass().getDeclaredMethod("getWifiApState");
            //调用getWifiApState() ，获取返回值
            int state = (int) method.invoke(manager);
            //通过放射获取 WIFI_AP的开启状态属性
            Field field = manager.getClass().getDeclaredField("WIFI_AP_STATE_ENABLED");
            //获取属性值
            int value = (int) field.get(manager);
            //判断是否开启
            if (state == value) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 初始化图表
     */
    private  void initChart() {
        mCurrentSeries = new XYSeries("Sample Data");
        mRenderer.setXTitle("Hz");
        mRenderer.setYTitle("Velocity:mm/s");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mCurrentRenderer.setColor( Color.rgb(255, 153, 0));
        mRenderer.addSeriesRenderer(mCurrentRenderer);
        mRenderer.setPanEnabled(true, true);
        mRenderer.setZoomEnabled(true, true);
    //  mRenderer.setLabelsTextSize(38);
        mRenderer.setYAxisMin(0);
        mRenderer.setYAxisMax(10);
        mRenderer.setXAxisMin(0);
        mRenderer.setXAxisMax(500);
        mRenderer.setXLabelsColor(Color.WHITE);
        mRenderer.setChartTitle("Velocity(mm/s) Vs. Hz");
        mRenderer.setShowGrid(true);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setYLabels(10);
        mRenderer.setYLabelsAngle(0);
        mRenderer.setYLabelsAlign( Paint.Align.RIGHT);
//        mRenderer.setZoomButtonsVisible(true);//是否显示放大缩小按钮
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.BLACK);

//        mCurrentRenderer.setPointStyle(PointStyle.CIRCLE); //折线点的样式
//        mCurrentRenderer.setPointStrokeWidth(5f);//折线点的大小
//        mCurrentRenderer.setDisplayChartValues(true);//设置显示折线的点对应的值
    }

    private  void initWaveChart() {
        WaveCurrentSeries = new XYSeries("Wave Data");
        WaveRenderer.setXTitle("ms");
        WaveRenderer.setYTitle("Acceleration:G-S");
        WaveDataset.addSeries(WaveCurrentSeries);
        WaveCurrentRenderer = new XYSeriesRenderer();
        WaveCurrentRenderer.setColor(Color.rgb(255, 153, 0));
        WaveRenderer.addSeriesRenderer(WaveCurrentRenderer);
        WaveRenderer.setPanEnabled(true, true);
        WaveRenderer.setZoomEnabled(true, true);
        WaveRenderer.setYAxisMin(0);
        WaveRenderer.setYAxisMax(10);
        WaveRenderer.setXAxisMin(0);
        WaveRenderer.setXAxisMax(500);
        WaveRenderer.setXLabelsColor(Color.WHITE);
        WaveRenderer.setChartTitle("Acceleration(G-S) Vs. ms");
        WaveRenderer.setShowGrid(true);
        WaveRenderer.setLabelsTextSize(15);
        WaveRenderer.setYLabels(10);
        WaveRenderer.setYLabelsAngle(0);
        WaveRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        //WaveRenderer.setZoomButtonsVisible(true);//是否显示放大缩小按钮
        WaveRenderer.setApplyBackgroundColor(true);
        WaveRenderer.setBackgroundColor(Color.BLACK);
    }



}
