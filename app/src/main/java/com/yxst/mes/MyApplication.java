package com.yxst.mes;

import android.app.Application;
import android.os.StrictMode;

import com.yxst.mes.database.DatabaseManager;

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

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        DatabaseManager databaseManager  = DatabaseManager.getInstance().init(getApplicationContext());
        IP="192.168.43.120";
        host="8000";
        wifiName="RTS";
        wifikey="00000000";
        Freqence=1000;
        Lines=2000;
        Factor=0.31108;

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


