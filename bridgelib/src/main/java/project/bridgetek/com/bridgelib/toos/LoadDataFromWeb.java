package project.bridgetek.com.bridgelib.toos;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Cong Zhizhong on 18-6-4.
 */

public class LoadDataFromWeb {
    private String mUrl;
    private File mFile;
    private Map<String, Object> mMap = null;
    private String mPostParams;
    // 是否包含参数，默认是不包含
    private boolean mHasparams = false;

    public LoadDataFromWeb(String url) {
        this.mUrl = url;
        mHasparams = false;
    }

    public LoadDataFromWeb(String mUrl, String mPostParams) {
        this.mUrl = mUrl;
        this.mPostParams = mPostParams;
        mHasparams = true;
    }

    public LoadDataFromWeb(String url, Map<String, Object> map, File file) {
        this.mUrl = url;
        this.mMap = map;
        this.mFile = file;
        mHasparams = true;
    }

    public LoadDataFromWeb(String url,
                           Map<String, Object> map) {
        this.mUrl = url;
        this.mMap = map;
        mHasparams = true;
    }

    public String uploadFile() {
        String postParams = "";
        if (mHasparams) {
            Set maps = mMap.keySet();
            Iterator iterator = maps.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = (String) mMap.get(key);
                if (i == 0) {
                    postParams += key + "=" + value;
                } else {
                    postParams += "&" + key + "=" + value;
                }
                i++;
            }
        }
        OkHttpClient client = new OkHttpClient();
        Request request;
        if (mHasparams) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), mFile);
            /* form的分割线,自己定义 */
            String boundary = "--------WebKitFormBoundaryuwYcfA2AIgxqIxA0";
            RequestBody requestBody = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM).addFormDataPart("file", "sdf.txt", fileBody).build();
            RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"), postParams);
            request = new Request.Builder()
                    .url(mUrl)
                    .post(body)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(mUrl)
                    .build();
        }
        Response response = null;
        String result = "{\"code\":-2}";
        try {
            response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //封装的网络访问方法
    public String getData() {
        String postParams = "";
        if (mHasparams) {
            Set maps = mMap.keySet();
            Iterator iterator = maps.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Object value = (Object) mMap.get(key);
                if (i == 0) {
                    postParams += key + "=" + value;
                } else {
                    postParams += "&" + key + "=" + value;
                }
                i++;
            }
        }
        OkHttpClient client = new OkHttpClient();
        Request request;
        if (mHasparams) {
            RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"), postParams);
            request = new Request.Builder()
                    .url(mUrl)
                    .post(body)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(mUrl)
                    .build();
        }
        Response response = null;
        String result = "{\"retcode\":5}";
        try {
            response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //封装的网络访问方法
    public String getResult() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(36, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(36, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(36, TimeUnit.SECONDS)//设置连接超时时间
                .retryOnConnectionFailure(true)
                .build();
        Request request;
        if (mHasparams) {
            RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), mPostParams);
            request = new Request.Builder()
                    .url(mUrl)
                    .post(body)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(mUrl)
                    .build();
        }
        Response response = null;
        String result = "{\"code\":-2}";
        try {
            response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //上传
    public String getupload() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(6, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(6, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(6, TimeUnit.SECONDS)//设置连接超时时间
                .retryOnConnectionFailure(true)
                .build();
        Request request;
        if (mHasparams) {
            RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), mPostParams);
            request = new Request.Builder()
                    .url(mUrl)
                    .post(body)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(mUrl)
                    .build();
        }
        Response response = null;
        String result = "NO";
        try {
            response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getServer() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(5, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
                .retryOnConnectionFailure(true)
                .build();
        Request request;
        if (mHasparams) {
            RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), mPostParams);
            request = new Request.Builder()
                    .url(mUrl)
                    .post(body)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(mUrl)
                    .build();
        }
        Response response = null;
        String result = "NO";
        try {
            response = client.newCall(request).execute();
            result = response.message();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
