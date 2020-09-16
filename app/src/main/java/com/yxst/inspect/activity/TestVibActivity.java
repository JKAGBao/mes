package com.yxst.inspect.activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.nfc.rts.androidaar.Interface;
import com.nfc.rts.androidaar.VibUtils;
import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.MyApplication;
import com.yxst.inspect.R;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.Manager.ItemQueryUtil;
import com.yxst.inspect.database.dao.ItemDao;
import com.yxst.inspect.database.dao.RecordDao;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.model.Record;
import com.yxst.inspect.nfc.vib.WifiAPManager;

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


public class TestVibActivity extends BaseActivity implements Interface {
    private RecordDao recorddao;
    private ItemDao itemdao;
    private Long itemId;
    private Long placeId;
    private Long lineId;
    private Record record;
    private Item item;
    private String placeName;
    private TextView zt;
    private TextView Power;
    private TextView temperature;
    private TextView rms;//振动值
    private TextView collect;
    private TextView save;
    private KProgressHUD hud;
    MyApplication application;
    VibUtils vibUtils=new VibUtils();

    private GraphicalView mChart;
    private GraphicalView WaveChart;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYMultipleSeriesDataset WaveDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer WaveRenderer = new XYMultipleSeriesRenderer();
    private XYSeries WaveCurrentSeries;
    private XYSeriesRenderer WaveCurrentRenderer;
    private XYSeries mCurrentSeries;
    private XYSeriesRenderer mCurrentRenderer;
    public double VoltageLevel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_testvib );
        application=(MyApplication)getApplication();
        zt = (TextView) findViewById( R.id.zt );
        Power = (TextView) findViewById( R.id.teV_Power );
        temperature = (TextView) findViewById( R.id.textView_temperature );
        rms = (TextView) findViewById( R.id.textView_RMS );
        collect = (TextView) findViewById( R.id.vib );
        save = (TextView)findViewById(R.id.save);
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
            if(item.getCheckType()== ConfigInfo.ITME_TYPE_VIBRATE){
                rms.setText(item.getCheckValue());
            } else if(item.getCheckType()==ConfigInfo.ITME_TYPE_TEMPERATURE){
                temperature.setText(item.getCheckValue());
            }
        }else{
            setTitle("测温测振采集");
        }


        zt.setText( "未连接" );
        collect.setText( "设备连接" );

        double Factor=application.getFactor();
        byte[] SendData=null;
        hud = KProgressHUD.create( TestVibActivity.this )
                .setStyle( KProgressHUD.Style.SPIN_INDETERMINATE )
                .setCancellable( true )
                .setAnimationSpeed( 2 )
                .setDimAmount( 0.5f );
        if(!isWifiApOpen( getApplicationContext())) {
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                showApDialogOnM();
            }else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                showRequestApDialogOnN_MR1();
            }
        }else {

            hud.show();
            Boolean Result=vibUtils.connect(SendData,application.getFreqence(),application.getLines(),
                    application.getIP(), application.getHost(),Factor, TestVibActivity.this);
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
            //    save.setTextColor(getResources().getColor(R.color.white));
                finish();
            }
        });
        collect.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double Factor=application.getFactor();
                byte[] SendData=null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hud.show();
                    }
                });
                if(zt.getText().toString().equals( "连接" )){

                    Boolean Result=vibUtils.VibrationMeasure( SendData,application.getFreqence(),application.getLines(),
                            application.getIP(),application.getHost(),Factor, TestVibActivity.this );
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
                }else {
                    Boolean Result=vibUtils.connect(SendData,application.getFreqence(),application.getLines(),
                            application.getIP(),application.getHost(),application.getFactor(), TestVibActivity.this);
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
    /*
    连接热点弹出框，
     */
    private void showApDialogOnM() {

        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        //   builer.setTitle( "开启WIFI热点" );
        builer.setMessage("点击确定，开启wifi热点，等待设备连接！");
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    //注册handler
                    WifiAPManager.getInstance(TestVibActivity.this);
                    //开启wifi热点
                    WifiAPManager.getInstance(TestVibActivity.this).turnOnWifiAp(application.getWifiName(), application.getWifikey(), WifiAPManager.WifiSecurityType.WIFICIPHER_WPA);
                } catch (Exception ex) {
                }
            }
        });
        builer.setNegativeButton("取消", null);
        AlertDialog dialog = builer.create();
        dialog.show();
    }
    /*
    7.0以上版本连接热点弹出框
     */
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
    /*
       数据初始化
        */
    private void titleInit(){

        recorddao = DatabaseManager.getInstance().getRecordDao();
        itemdao = DatabaseManager.getInstance().getItemDao();
    }
    @Override
    public void getData(double[] waveDatas, double[] spectrumDatas) {
        VibUtils vibUtils=new VibUtils();
        double RMS= vibUtils.setData(spectrumDatas);
        double MAX=vibUtils.GetMAX(spectrumDatas);
        final float sp = new Float( new DecimalFormat( ".00" ).format(RMS ));
        int Frequency=application.getFreqence();
        int Lines=application.getLines();
        //时域
        double max = GetMax( waveDatas );
        WaveRenderer.setYAxisMin( -max * 1.2 );
        WaveRenderer.setYAxisMax( max * 1.2 );
        WaveRenderer.setXAxisMin( 0 );
        WaveRenderer.setXAxisMax( Lines*1000f/Frequency);
        //频谱
        mRenderer.setYAxisMin( 0 );
        mRenderer.setYAxisMax( MAX * 1.2 );
        mRenderer.setXAxisMin( 0 );
        mRenderer.setXAxisMax( Frequency );

        mCurrentSeries.clear();
        WaveCurrentSeries.clear();
        for (int i = 0; i < Lines; i++) {
            VoltageLevel = spectrumDatas[i];
            mCurrentSeries.add( Frequency*1.0f/Lines * i, VoltageLevel );
        }
        //时域波形开始
        for (int i = 0; i < waveDatas.length; i++) {
            VoltageLevel = waveDatas[i];
            WaveCurrentSeries.add( i*(1000f/(Frequency*2.5)), VoltageLevel );
        }
        //时域波形结束
        mChart.repaint();
        WaveChart.repaint();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rms.setText( ""+sp );
                hud.dismiss();
            }
        });

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
//                if(power<30 || power< 20){
//                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TestVibActivity.this)
//                            .setMessage("该充电了！电量小于20%，采集值会不准确的")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    finish();
//                                    dialog.cancel();
//                                }
//                            })
//                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    builder.show();
//                }
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
    @Override
    public void onDestroy() {
        super.onDestroy();
//        mChart=null;
//        WaveChart=null;
//        WaveDataset=null;
//        WaveRenderer=null;
//        WaveCurrentRenderer=null;
//        WaveCurrentSeries=null;
//        mDataset=null;
//        mRenderer=null;
//        mCurrentSeries=null;
//        mCurrentRenderer=null;
        hud.dismiss();
    }



}
