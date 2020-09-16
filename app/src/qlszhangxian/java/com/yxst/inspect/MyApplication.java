package com.yxst.inspect;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.StrictMode;

import com.inuker.bluetooth.library.BluetoothContext;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.util.SharedPreferenceUtil;

import project.bridgetek.com.bridgelib.app.Black;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CrashHandler;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.Logger;

public class MyApplication extends Application {
    private String IP;
    private String host;
    private String wifiName;
    private String wifikey;
    private int Freqence ;//频宽
    private int Lines ;//线数
    private double Factor;//传感器系数
    public static Long InInpectLine = 1L;
    public static String UserName;
    public static Long UserID;
    public static String BASE_URL = "http://60.164.211.4:9800";
    public static String IMAGE_PATH =  "/FilesServer/WebService.asmx";//wsdl 的uri
    public static String WSDL_URI =  BASE_URL+IMAGE_PATH;//wsdl 的uri

    @Override
    public void onCreate() {
        super.onCreate();
        String ip = SharedPreferenceUtil.getServeIp(this,"IP");
        if(!(ip.equals(""))){
            BASE_URL  = ip;
        }
        String imagePath = SharedPreferenceUtil.getServeIp(this,"ImagePath");
        if(!(imagePath.equals(""))){
            IMAGE_PATH  = imagePath;
        }
        Black.init(this)
                .withApiHost("http://192.168.2.1")
                .withIcon(new FontAwesomeModule())
                .configure();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        BluetoothContext.set(this);
        HiApplication.getTypeface(this);
        HiApplication.getInstance(this);
        initCrash();
        SharedPreferenceUtil.getServeIp(this,"");
        DatabaseManager databaseManager  = DatabaseManager.getInstance().init(getApplicationContext());
        IP="192.168.43.120";
        host="8000";
        wifiName="RTS";
        wifikey="00000000";
        Freqence=1000;
        Lines=1600;
        Factor=0.22;

    }
    @SuppressLint("MissingPermission")
    private void initCrash() {
        CrashHandler.init(Constants.FILE_LOG_PATH, new CrashHandler.OnCrashListener() {
            @Override
            public void onCrash(String crashInfo, Throwable e) {
                Logger.e(crashInfo);
//                relaunchApp();
            }
        });
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getWifikey() {
        return wifikey;
    }

    public void setWifikey(String wifikey) {
        this.wifikey = wifikey;
    }

    public int getFreqence() {
        return Freqence;
    }

    public void setFreqence(int freqence) {
        Freqence = freqence;
    }

    public int getLines() {
        return Lines;
    }

    public void setLines(int lines) {
        Lines = lines;
    }

    public double getFactor() {
        return Factor;
    }

    public void setFactor(double factor) {
        Factor = factor;
    }
}


