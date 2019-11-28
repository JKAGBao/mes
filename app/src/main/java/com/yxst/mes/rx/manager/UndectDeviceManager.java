package com.yxst.mes.rx.manager;

import android.content.Context;
import android.content.Intent;

import com.yxst.mes.activity.UndetectActivity;
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.model.Undetect;
import com.yxst.mes.net.RestCreator;
import com.yxst.mes.util.SharedPreferenceUtil;
import com.yxst.mes.util.ThrowableUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created By YuanCheng on 2019/7/4 11:39
 */
public class UndectDeviceManager {
    public static void undectDeviceMutual(final Context ctx){
        Long userid = SharedPreferenceUtil.getId(ctx,"User");
        Observable<List<Undetect>> observable = RestCreator.getRxRestService().geUndectDeviceByUserId(userid);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Undetect>>() {
                    @Override
                    public void accept(List<Undetect> undetects) throws Exception {
                        if(undetects.size()!=0 && undetects!=null){
                            DatabaseManager.getInstance().getUndetectDao().deleteAll();
                            DatabaseManager.getInstance().getUndetectDao().insertOrReplaceInTx(undetects);
                            Intent intent = new Intent(ctx,UndetectActivity.class);
                            ctx.startActivity(intent);
                        }else{
                            DatabaseManager.getInstance().getUndetectDao().deleteAll();
                            Intent intent = new Intent(ctx,UndetectActivity.class);
                            ctx.startActivity(intent);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ThrowableUtil.exceptionManager(throwable,ctx);
                    }
                });
    }
}
