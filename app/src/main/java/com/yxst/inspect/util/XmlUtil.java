package com.yxst.inspect.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.Xml;

import com.yxst.inspect.R;
import com.yxst.inspect.model.Version;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created By YuanCheng on 2019/7/29 16:52
 */
public class XmlUtil {

    public static Version readXmlFromServer(final String urlStr,final Context ctx) {
                 Version  version = null;
                //XML存放在ftp服务器的地址
               // String path = FileUtils.getDevice_address()+"News.XML";
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    //发送http GET请求，获取相应码
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        //使用pull解析器，开始解析这个流
                        version = parseNewsXml(is,ctx);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

        return version;
    }
    public static Version loadLocalXml(Context context) {
        Resources r = context.getResources();
        XmlResourceParser xrp = r.getXml(R.xml.update);
        Version version = new Version();
        try {
            // 当没有达到xml的逻辑结束终点
            // getEventType方法返回读取xml当前的事件
            int type = xrp.getEventType();
            while (type != XmlResourceParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if ("version".equals(xrp.getName())) {
                            break;
                        } else if ("code".equals(xrp.getName())) {
                            String code = xrp.nextText();
                            version.setCode(Integer.valueOf(code));
                            Log.e("version", "code=" + code);
                            break;
                        } else if ("name".equals(xrp.getName())) {
                            String name = xrp.nextText();
                            version.setName(name);
                            Log.e("version", "versionName=" + name);
                            break;
                        } else if ("url".equals(xrp.getName())) {
                            String apkUrl = xrp.nextText();
                            version.setUrl(apkUrl);
                            Log.e("version", "versionAPK=" + apkUrl);
                            break;
                        }
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;

                }
              type =  xrp.next();
            }
            } catch(XmlPullParserException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
            return version;
    }
    public static Version parseNewsXml(InputStream is, Context context) {
        XmlPullParser xp = Xml.newPullParser();
        Version version = new Version();
        try {
            xp.setInput(is, "utf-8");
            //对节点的事件类型进行判断，就可以知道当前节点是什么节点
            int type = xp.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                       if ("code".equals(xp.getName())) {
                            String code = xp.nextText();
                            version.setCode(Integer.valueOf(code));
                            Log.e("version", "code=" + code);
                            break;
                        } else if ("name".equals(xp.getName())) {
                            String name = xp.nextText();
                            version.setName(name);
                            Log.e("version", "versionName=" + name);
                            break;
                        } else if ("url".equals(xp.getName())) {
                            String apkUrl = xp.nextText();
                            version.setUrl(apkUrl);
                            Log.e("version", "versionAPK=" + apkUrl);
                            break;
                        }
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                //解析完当前节点后，把指针移动至下一个节点，直至节点完毕，并返回它的事件类型
                type = xp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

}
