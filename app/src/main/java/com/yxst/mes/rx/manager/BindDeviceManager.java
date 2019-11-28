package com.yxst.mes.rx.manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yxst.mes.activity.BindActivity;
import com.yxst.mes.activity.BindLineActivity;
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.model.Device;
import com.yxst.mes.database.model.Line;
import com.yxst.mes.net.RestCreator;
import com.yxst.mes.util.ThrowableUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created By YuanCheng on 2019/7/4 11:23
 */
public class BindDeviceManager {

    public static void bindDevice(final Context context){
        Observable line = RestCreator.getRxRestService().getLine();
        Observable device = RestCreator.getRxRestService().getDeviceList();
        Observable.merge(line,device).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(List list) {
                        //请求所有的任务线成功后，先清一下Line表数据
                        if (list != null && list.size() > 0) {
                            if (list.get(0) instanceof Device) {
                                //同步设备信息，先清一下之前数据
                                DatabaseManager.getInstance().getDeviceDao().deleteAll();
                                DatabaseManager.getInstance().getDeviceDao().insertOrReplaceInTx(list);
                                Log.e("schedule", "Schedule-url-geDevice（）：" + list.size());
                            }
                            else if (list.get(0) instanceof Line) {
                                //同步设备信息，先清一下之前数据
                                DatabaseManager.getInstance().getLineDao().deleteAll();
                                DatabaseManager.getInstance().getLineDao().insertOrReplaceInTx(list);
                                Log.e("schedule", "Schedule-url-geDevice（）：" + list.size());
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {

                        ThrowableUtil.exceptionManager(e,context);
                        Intent intent = new Intent(context,BindLineActivity.class);
//                        Intent intent = new Intent(context,BindActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onComplete() {
//                        Intent intent = new Intent(context,BindLineActivity.class);
                        Intent intent = new Intent(context,BindActivity.class);
                        context.startActivity(intent);               }
                });
    }
    public static void obtainline(final Context context){
        Observable line = RestCreator.getRxRestService().getLine();
     //   Observable device = RestCreator.getRxRestService().getDeviceList();
        line.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(List list) {
                        //请求所有的任务线成功后，先清一下Line表数据
                        if (list != null && list.size() > 0) {
                            if (list.get(0) instanceof Device) {
                                //同步设备信息，先清一下之前数据
                                DatabaseManager.getInstance().getDeviceDao().deleteAll();
                                DatabaseManager.getInstance().getDeviceDao().insertOrReplaceInTx(list);
                                Log.e("schedule", "Schedule-url-geDevice（）：" + list.size());
                            }
                            else if (list.get(0) instanceof Line) {
                                //同步设备信息，先清一下之前数据
                                DatabaseManager.getInstance().getLineDao().deleteAll();
                                DatabaseManager.getInstance().getLineDao().insertOrReplaceInTx(list);
                                Log.e("schedule", "Schedule-url-geDevice（）：" + list.size());
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        ThrowableUtil.exceptionManager(e,context);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
