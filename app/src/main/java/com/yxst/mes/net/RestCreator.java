package com.yxst.mes.net;

import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.yxst.mes.rx.RxRestService;
import com.yxst.mes.util.TimeUtil;

import org.greenrobot.greendao.annotation.Convert;
import org.kobjects.base64.Base64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestCreator {
    public static String UPLOAD_PATH ;
    private static String WSDL_URI =  RestConfig.BASE_URL+"/FilesServer/WebService.asmx";//wsdl 的uri
    public static WeakHashMap<String,Object> getPramas(){
        return ParamsHolder.PRAMAS;
    }
    private static class ParamsHolder{
        private static final WeakHashMap<String,Object> PRAMAS = new WeakHashMap<String,Object>();
    }

    public static final RestService getRestService(){
        return RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);

    }


    private static final class RetrofitHolder{
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(RestConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                //        .client(OkhttpHolder.OK_HTTP_CLIENT)
                .build();
    }
    /*
    获取RxRestService
     */
    public static final RxRestService getRxRestService(){
        return RxRetrofitHolder.RETROFIT_CLIENT.create(RxRestService.class);

    }
   static Gson gson = new GsonBuilder()
           .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
           .setLenient()
            .create();


    private static final class RxRetrofitHolder{

        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(RestConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkhttpHolder.OK_HTTP_CLIENT)
                .build();
    }
    private static final class OkhttpHolder{
        private static final int TIME_OUT = 3;
//        readTimeout(60, TimeUnit.SECONDS).
//        writeTimeout(60, TimeUnit.SECONDS)
        private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT,TimeUnit.SECONDS)
                .build();
    }
    public static String postImage(String fileName,String filePath) throws IOException, XmlPullParserException {
        String namespace = "http://tempuri.org";
        String methodName = "SaveImgFile";
        SoapObject request = new SoapObject(namespace,methodName);
        File tFile = new File(fileName);
        String uploadBuffer = "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(tFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count = 0;
            while((count = fis.read(buffer)) >= 0){
                baos.write(buffer, 0, count);
            }
            uploadBuffer = new String(Base64.encode(baos.toByteArray()));  //进行Base64编码
            fis.close();
            }catch (Exception e){
                e.printStackTrace();
            }
//        File tempFile = new File(dir, "/DCIM/Camera/IMG_20190609_130548.jpg");

        request.addProperty("image",uploadBuffer);
        request.addProperty("fileName",filePath+tFile.getName());
        request.addProperty("vName", "lzl_YouXun100.cn");
        request.addProperty("validateKey", "2014lzlYX!@#");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        envelope.encodingStyle="UTF-8";
        envelope.setOutputSoapObject(request);

        HttpTransportSE se = new HttpTransportSE(WSDL_URI);
        se.call("http://tempuri.org/SaveImgFile",envelope);
        SoapObject in = (SoapObject) envelope.bodyIn;
        String result = in.getProperty(0).toString();
        return result;
    }
    /*
    Inspect图片路径
     */
    public static String getImagePath() throws Exception{
        String namespace = "http://tempuri.org/";//namespace
        String methodName = "CreateFile";//要调用的方法名称
        String filePath = "/files/device/inspection/"+ TimeUtil.dayFormat(new Date())+"/";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("FilePath", filePath);
        request.addProperty("vName", "lzl_YouXun100.cn");
        request.addProperty("validateKey", "2014lzlYX!@#");

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
        httpTransportSE.debug = true;
        httpTransportSE.call("http://tempuri.org/CreateFile", envelope);//调用
        //  Toast.makeText(this, "envelope.getResponse();"+envelope.getResponse(), Toast.LENGTH_SHORT).show();
        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的数据
//        if (envelope.getResponse()!=null){
//            SoapObject object = (SoapObject) envelope.getResponse();
//        }
        String result = object.getProperty(0).toString();
        // 获取返回的结果
        return result;

    }
    /*
    润滑图片路径
     */
    public static String getLubeImagePath() throws Exception{
        String namespace = "http://tempuri.org/";//namespace
        String methodName = "CreateFile";//要调用的方法名称
        String filePath = "/files/device/lube/"+ TimeUtil.dayFormat(new Date())+"/";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("FilePath", filePath);
        request.addProperty("vName", "lzl_YouXun100.cn");
        request.addProperty("validateKey", "2014lzlYX!@#");

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
        httpTransportSE.debug = true;
        httpTransportSE.call("http://tempuri.org/CreateFile", envelope);//调用
        //  Toast.makeText(this, "envelope.getResponse();"+envelope.getResponse(), Toast.LENGTH_SHORT).show();
        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        // 获取返回的结果
        return result;

    }
    public static String postImageUp(String fileName,String filePath) throws Exception{
        String namespace = "http://tempuri.org/";//namespace
        String methodName = "SaveImgFile";//要调用的方法名称
        SoapObject request = new SoapObject(namespace, methodName);
        //String fileName =  Environment.getExternalStorageDirectory()+"/DCIM/Camera/IMG_20190621_104137.jpg" ;
        File tFile = new File(fileName);
        Log.e("fileIs",""+tFile.exists());
        String uploadBuffer = "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(tFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }
            uploadBuffer = new String(Base64.encode(baos.toByteArray()));  //进行Base64编码
        }catch (Exception e){
            e.printStackTrace();
        }
//            uploadBuffer = android.util.Base64.decode(baos.toByteArray(), android.util.Base64.NO_WRAP).toString();
        fis.close();
        request.addProperty("image",uploadBuffer );
        Log.e("img",uploadBuffer);
        request.addProperty("fileName", filePath+tFile.getName());
        request.addProperty("vName", "lzl_YouXun100.cn");
        request.addProperty("validateKey", "2014lzlYX!@#");
        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true
        envelope.encodingStyle="UTF-8";
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
        httpTransportSE.call("http://tempuri.org/SaveImgFile", envelope);//调用
        SoapObject object = (SoapObject) envelope.bodyIn;

        String  result = object.getProperty(0).toString();
        return result;

    }
}
