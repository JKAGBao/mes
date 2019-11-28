package com.yxst.mes.rx;

import android.content.Context;

import com.yxst.mes.net.HttpMethod;
import com.yxst.mes.net.RestCreator;
import com.yxst.mes.net.RestService;
import com.yxst.mes.net.callback.IError;
import com.yxst.mes.net.callback.IFailture;
import com.yxst.mes.net.callback.IRequest;
import com.yxst.mes.net.callback.ISuccess;
import com.yxst.mes.net.callback.RequestCallbacks;
import com.yxst.mes.ui.Loader;
import com.yxst.mes.ui.LoaderStyle;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RxRestClient {
    private  final String URL;
    private  final WeakHashMap<String,Object> PARAMS = RestCreator.getPramas();
    private  final RequestBody BODY;
    private  final Context context;
    private final LoaderStyle LOADER_STYLE;

    private RxRestClient(String url, WeakHashMap<String,Object> pramas,
                          RequestBody body, Context context, LoaderStyle loaderStyle){
        this.URL = url;
        this.PARAMS.putAll(pramas);
        this.BODY = body;
        this.context = context;
        this.LOADER_STYLE = loaderStyle;
    }
    public static final Builder Builder(){
        return new Builder();
    }

    private Observable request(HttpMethod method){
        final RxRestService service = RestCreator.getRxRestService();
        //Call<String> call = null;
//           Call<List<InspectDevice>> call = null;
        Observable observable = null;

        if(LOADER_STYLE!=null){
            Loader.showLoading(context,LOADER_STYLE);
        }
        switch (method){
            case GET :
                //   call = service.get(URL,PARAMS);
                observable = service.getLine();
                break;
            case POST:
                // call = service.post(URL,PARAMS);
                break;
            default:
                break;

        }
//        if(call!=null){
//            call.enqueue(getRequestCallback());
//        }
        return observable;
    }

    public final Observable get(){

        return request(HttpMethod.GET);
    }
    public final Observable post(){

        return request(HttpMethod.POST);
    }

    /*
    Builder内部类
     */
    public final static class Builder{
        private  String mUrl;
        private  WeakHashMap<String,Object> PARAMS = RestCreator.getPramas();
        private  RequestBody mBody;
        private  Context mContext;
        private  LoaderStyle mLoaderStyle;
        public Builder(){

        }
        public final Builder url(String url){
            this.mUrl = url;
            return this;
        }
        public final Builder params(String key,Object value) {

            PARAMS.put(key,value);
            return this;
        }
        public final Builder body(String raw){
            mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
            return this;
        }

        public final Builder loader(Context context,LoaderStyle loaderStyle){
            mContext = context;
            mLoaderStyle = loaderStyle;
            return this;
        }
        public final Builder loader(Context context){
            mContext = context;
            mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
            return this;
        }

        public final RxRestClient build(){
            return new RxRestClient(mUrl,PARAMS,mBody,mContext,mLoaderStyle);
        }

    }
}
