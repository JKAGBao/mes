package project.bridgetek.com.applib.main.toos;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class RetrofitManager {
    private static String TAG = "RetrofitManager";
    private static Retrofit mRetrofit;
    private static OkHttpClient okHttpClient;
    private static final long HTTP_CONNECT_TIMEOUT = 10;
    private static final long HTTP_READ_TIMEOUT = 30;
    private static final long HTTP_WRITE_TIMEOUT = 30;
    public static String TOKEN = "token";

    private static ServiceApi API;
    private static boolean isReconnet = false;


    private RetrofitManager() {
    }

    //返回数据类型是否json，false时为xml
    public static ServiceApi instance(boolean json) {
        if (null == okHttpClient) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                    new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            //打印retrofit日志
                            if (!TextUtils.isEmpty(message) && message.length() > 10000) {
                                message = message.substring(0, 5000);
                            }
                            Log.d(TAG, message);
                        }
                    });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            //TODO 添加token以及检查登录
                            Request oldRequest = chain.request();
                            HttpUrl.Builder builder = oldRequest.url().newBuilder();
//                                    .setEncodedQueryParameter("token", TOKEN);
                            Request.Builder newBuilder = oldRequest.newBuilder()
                                    .method(oldRequest.method(), oldRequest.body())
                                    .url(builder.build());
                            /**
                             * Cookie:
                             * JSESSIONID=672977B08482DE9E0600253FAFE77BCA;
                             * _user_token=385C413168964D23AE0B0D9A8A6CF375;
                             * S-CSRF-TOKEN=a5ac2f336be125c26bec576233ec7717faefc38c8fbf2aba7780c947169747d5
                             */
                            Request newRequest = newBuilder.build();
                            Response response = chain.proceed(newRequest);
                            if (response.code() == 401) {
                                try {
                                    Field codeFiled = response.getClass().getDeclaredField("code");
                                    codeFiled.setAccessible(true);
                                    codeFiled.set(response, 200);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            return response;
                        }
                    })
                    .build();
            isReconnet = false;

        }
        if (null == mRetrofit || !TextUtils.equals(mRetrofit.baseUrl().host(), Constants.API)
                || isConverterChanged(json)) {
            Retrofit.Builder builder = new Retrofit
                    .Builder()
                    .baseUrl(getBaseUrl())
                    .addConverterFactory(json ? GsonConverterFactory.create() : SimpleXmlConverterFactory.create())
                    .client(okHttpClient);
            mRetrofit = builder.build();
            API = mRetrofit.create(ServiceApi.class);
        }
        return API;
    }

    public static abstract class SimpleCallback<T> implements Callback<T> {

        @Override
        public void onResponse(Call<T> call, retrofit2.Response<T> response) {
            if (response.isSuccessful() && response.body() != null) {
                onResponseSuccess(response.body());
            } else {
                Logger.e("response onFailed:" + response);
                onResponseFailure();
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            Logger.d("request onFailed:" + t.toString());
            onResponseFailure();
        }

        public void onResponseFailure() {
        }

        public abstract void onResponseSuccess(T result);
    }

    public static boolean isConverterChanged(boolean json) {
        List<Converter.Factory> factories = mRetrofit.newBuilder().converterFactories();
        ;
        if (factories != null && !factories.isEmpty()) {
            Converter.Factory factory = factories.get(0);
            if (factory instanceof SimpleXmlConverterFactory && json) {
                return true;
            } else if (factory instanceof GsonConverterFactory && !json) {
                return true;
            }
        }
        return false;
    }

    public static String getBaseUrl() {
//        if (Constant.APP_SERVER_IP.contains(":")) {
//            return "http://" + Constant.APP_SERVER_IP.split(":")[0] + ":18081" + "/nuc-dsan/";
//        }
//        return "http://" + Constant.APP_SERVER_IP + "/nuc-dsan/";
//        return "http://www.mocky.io/v2/";
        return Constants.API;
    }
}
