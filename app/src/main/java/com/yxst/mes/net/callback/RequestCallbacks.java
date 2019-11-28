package com.yxst.mes.net.callback;

import android.os.Handler;
import com.yxst.mes.ui.Loader;
import com.yxst.mes.ui.LoaderStyle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestCallbacks implements Callback<String> {
//    public class RequestCallbacks implements Callback<List<InspectDevice>> {
    private final IRequest REQUEST;
    private final ISuccess SUCESS;
    private final IFailture FAILTURE;
    private final IError ERROR;
    private final LoaderStyle LOADER_STYLE;
    private static final Handler HANDLER = new Handler();


    public RequestCallbacks(IRequest request, ISuccess success, IFailture failture, IError error,LoaderStyle loaderStyle) {
        this.REQUEST = request;
        this.SUCESS = success;
        this.FAILTURE = failture;
        this.ERROR = error;
        this.LOADER_STYLE = loaderStyle;
    }


    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if(response.isSuccessful()){
            if(call.isExecuted()){
                if(SUCESS!=null){
                    SUCESS.onSucess(response.body());
                }
            }
        }else {
            if (ERROR!=null){
                ERROR.onError(response.code(),response.message());
            }
        }
        if(LOADER_STYLE!=null){
            stoploading();
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if(FAILTURE!=null){
            FAILTURE.onFailture();
        }
        if(REQUEST!=null){
            REQUEST.OnRequestEnd();
        }
        if(LOADER_STYLE!=null){
            stoploading();
        }

    }
        private void stoploading(){
            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Loader.stopLoading();
                }
            },1000);
        }
}
