package com.yxst.mes.util;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created By YuanCheng on 2019/6/18 19:14
 */
public class WiFiUtil {

    private static WifiManager mWifiManager;
    /**
     * 检查是否开启Wifi热点
     * @return
     */
    public static WiFiUtil getInstance(Context ctx){
        WiFiUtil wiFiUtil = null;
        mWifiManager=(WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
        if(wiFiUtil == null){
            wiFiUtil = new WiFiUtil();
        }else{
            return wiFiUtil;
        }
        return wiFiUtil;
    }

    public boolean isWifiApEnabled(){
        try {
            Method method=mWifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (boolean) method.invoke(mWifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 关闭热点
     */
    public void closeWifiAp(){
            try {
                Method method=mWifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config= (WifiConfiguration) method.invoke(mWifiManager);
                Method method2=mWifiManager.getClass().getMethod("setWifiApEnabled",WifiConfiguration.class,boolean.class);
                method2.invoke(mWifiManager,config,false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

