package com.yxst.inspect.rx.manager;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.yxst.inspect.model.Visit;
import com.yxst.inspect.net.RestCreator;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created By YuanCheng on 2019/8/6 15:30
 */
public class VisitManager {
    public static void pageRecord(final Context ctx,String pageName){
        Visit visit = new Visit(ctx,pageName);
        Gson gson = new Gson();
        String array =  gson.toJson(visit);
        Log.e("gson",array);
        Observable postRecord = RestCreator.getRxRestService().visitRecord(visit);
        postRecord.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //      Toast.makeText(FinishActivity.this, ""+o.toString(), Toast.LENGTH_SHORT).show();
                        if(Integer.valueOf(o.toString()) > 0){
                 //          Toast.makeText(ctx, "上传成功！"+o.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                     //   ThrowableUtil.exceptionManager(throwable,ctx);
                        //Toast.makeText(FinishActivity.this, "上传失败"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
