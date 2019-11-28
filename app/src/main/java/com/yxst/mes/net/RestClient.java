package com.yxst.mes.net;

import android.content.Context;
import com.yxst.mes.net.callback.IError;
import com.yxst.mes.net.callback.IFailture;
import com.yxst.mes.net.callback.IRequest;
import com.yxst.mes.net.callback.ISuccess;
import com.yxst.mes.net.callback.RequestCallbacks;
import com.yxst.mes.ui.Loader;
import com.yxst.mes.ui.LoaderStyle;

import java.util.WeakHashMap;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RestClient {
    private  final String URL;
    private  final WeakHashMap<String,Object> PARAMS = RestCreator.getPramas();
    private  final IRequest REQUEST;
    private  final ISuccess SUCESS;
    private  final IFailture FAILTURE;
    private  final IError ERROR;
    private  final RequestBody BODY;
    private  final Context context;
    private final LoaderStyle LOADER_STYLE;

    private RestClient(String url, WeakHashMap<String,Object> pramas,
                       IRequest request, ISuccess success, IFailture failture,
                       IError error, RequestBody body,Context context,LoaderStyle loaderStyle){
        this.URL = url;
        this.PARAMS.putAll(pramas);
        this.REQUEST = request;
        this.SUCESS =success;
        this.FAILTURE = failture;
        this.ERROR = error;
        this.BODY = body;
        this.context = context;
        this.LOADER_STYLE = loaderStyle;
    }
    public static final Builder Builder(){
        return new Builder();
    }
    public final static class Builder{
        private  String mUrl;
        private  WeakHashMap<String,Object> PARAMS = RestCreator.getPramas();
        private  IRequest mRequest;
        private  ISuccess mSuccess;
        private  IFailture mFailture;
        private  IError mError;
        private  RequestBody mBody;
        private  Context mContext;
        private  LoaderStyle mLoaderStyle;
        private Builder(){

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
        public final Builder success(ISuccess success){
            mSuccess = success;
            return this;
        }
        public final Builder error(IError error){
            mError = error;
            return this;
        }
        public final Builder failture(IFailture failture){
            mFailture = failture;
            return  this;
        }
        public final Builder onRequest(IRequest request){
            mRequest = request;
            return  this;
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


        public final RestClient build(){
            return new RestClient(mUrl,PARAMS,mRequest,mSuccess,mFailture,mError,mBody,mContext,mLoaderStyle);
        }

    }
       private void request(HttpMethod method){
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;
//           Call<List<InspectDevice>> call = null;
        if(REQUEST!=null){
            REQUEST.OnRequestStart();
        }
        if(LOADER_STYLE!=null){
            Loader.showLoading(context,LOADER_STYLE);
        }
        switch (method){
            case GET :
             //   call = service.get(URL,PARAMS);
                call = service.get(URL);
                break;
            case POST:
               // call = service.post(URL,PARAMS);
                break;
             default:
                 break;

        }
        if(call!=null){
           call.enqueue(getRequestCallback());
        }
    }
    RequestCallbacks getRequestCallback(){
        return new RequestCallbacks(REQUEST,SUCESS,FAILTURE,ERROR,LOADER_STYLE);
    }

    public final void get(){
        request(HttpMethod.GET);
    }
    public final void post(){
        request(HttpMethod.POST);
    }
}
