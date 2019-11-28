package com.yxst.mes.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.yxst.mes.activity.StartUpActivity;

/**
 * Created By YuanCheng on 2019/7/2 18:53
 */
public class ThrowableUtil {
    public static void exceptionManager(Throwable e, Context context){
        Log.e("startup",e.getMessage());
        if(e.getMessage().contains("Failed to connect to")){
            //网络没有开，返回这个错误Failed to connect to /60.164.211.4:80,
            Toast.makeText(context,"网络没有连接！",Toast.LENGTH_LONG).show();

        }else if(e.getMessage().contains("HTTP 404 Not Found")){
            //API接口没有，HTTP 404 Not Found
            Toast.makeText(context,"服务器暂时不可用！",Toast.LENGTH_LONG).show();

        }else if(e.getMessage().contains("after")){
            //错误的服务器地址：failed to connect to /60.164.211.3 (port 80) from /192.168.0.17 (port 37350) after 10000ms
            Toast.makeText(context,"服务器地址错误！",Toast.LENGTH_LONG).show();

        }else if(e.getMessage().contains("connect timed out")){
            Toast.makeText(context,"服务器没有响应！",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"异常信息提示！"+e,Toast.LENGTH_LONG).show();
        }
    }
}
