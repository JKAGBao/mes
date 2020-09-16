package com.yxst.inspect.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * Created By YuanCheng on 2019/8/6 19:05
 */
public class NetworkUtil {
    public static String getIP(Context cxt){

        //获取wifi服务
        WifiManager wifiManager = (WifiManager) cxt.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }
    private static String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

    /****
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public static String getNetType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info == null) {
            return "";
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return "WIFI";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA) {
                return "CDMA";
            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
                return "EDGE";
            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0) {
                return "EVDO0";
            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A) {
                return "EVDOA";
            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS) {
                return "GPRS";
            }
        }
        return "";
    }

}
