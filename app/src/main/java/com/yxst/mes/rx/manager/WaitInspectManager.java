package com.yxst.mes.rx.manager;

import android.content.Context;
import android.content.Intent;

import com.yxst.mes.MyApplication;
import com.yxst.mes.activity.InspectActivity;
import com.yxst.mes.database.model.InspectDevice;
import com.yxst.mes.database.model.Item;
import com.yxst.mes.database.Manager.InspectDevicQueryUtil;
import com.yxst.mes.database.Manager.ItemQueryUtil;
import com.yxst.mes.database.Manager.PlaceQueryUtil;
import com.yxst.mes.database.model.Place;
import com.yxst.mes.net.RestCreator;
import com.yxst.mes.util.SharedPreferenceUtil;
import com.yxst.mes.util.ThrowableUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created By YuanCheng on 2019/7/4 20:03
 */
public class WaitInspectManager {
    public static void matualWaitInpect(final Context ctx){
        final Long userid = SharedPreferenceUtil.getId(ctx,"User");
        Observable inspectDevice = RestCreator.getRxRestService().getInspectDeviceByUserId(userid);
        Observable item = RestCreator.getRxRestService().getItemByUserId(userid);
        Observable place = RestCreator.getRxRestService().getPlaceByUserId(userid);
        Observable.merge(inspectDevice,place,item).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(List list) {
                        //         Toast.makeText(PrimaryActivity.this,list.size()+","+list,Toast.LENGTH_LONG).show();
                        if(list==null||list.size()==0) {
                            return;
                        }
                        if (list.get(0) instanceof InspectDevice) {
                            List<InspectDevice> localDevice = InspectDevicQueryUtil.getInpectDeviceListByUserId(ctx);
                            List<InspectDevice> inspectDevices = InspectDevicQueryUtil.getInpectDeviceListByTime(ctx);
                            //    Toast.makeText(PrimaryActivity.this, inspectDevices.size()+","+localDevice.size()+","+list.size(), Toast.LENGTH_SHORT).show();
                            //        Toast.makeText(PrimaryActivity.this, "" + inspectDevices.size() + "," + localDevice.size(), Toast.LENGTH_SHORT).show();
                            if (list.size() != localDevice.size() || inspectDevices.size()==0) {//没有更新inspectdevice
                                InspectDevicQueryUtil.deleteByUserID(userid);
                                InspectDevicQueryUtil.saveByList(list);
                            }
                        }else if(list.get(0) instanceof Place){
                            List<Place> placeList = PlaceQueryUtil.getPlaceByUserId(userid);
                            List<Place> placesbyTime = PlaceQueryUtil.getPlaceByTime(ctx);
                            //         Toast.makeText(PrimaryActivity.this, ""+placesbyTime.size(), Toast.LENGTH_SHORT).show();
                            if(list.size()!=placeList.size() || placesbyTime.size()==0){
                                PlaceQueryUtil.deleteByUserId(userid);
                                PlaceQueryUtil.saveWithList(list);
                            }
                        }else if(list.get(0) instanceof Item){
                            List<Item> itemList = ItemQueryUtil.getItemByUserId(userid);
                            List<Item> items = ItemQueryUtil.getItemByTime(ctx);
                            //       Toast.makeText(PrimaryActivity.this,MyApplication.InInpectLine+","+list.size()+","+itemList.size(), Toast.LENGTH_SHORT).show();

                            if(list.size()!=itemList.size() || items.size()==0){
                                ItemQueryUtil.deleteByUserId(userid);
                                ItemQueryUtil.saveWithItemList(list);
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ThrowableUtil.exceptionManager(e,ctx);
                        Intent intent = new Intent(ctx,InspectActivity.class);
                        ctx.startActivity(intent);
                    }

                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(ctx,InspectActivity.class);
                        ctx.startActivity(intent);
                    }
                });
    }

    public static void waitInpectInter(final Context ctx){
        final List<InspectDevice> inspectDevices;
        final Long userid = SharedPreferenceUtil.getId(ctx,"User");
        final Observable inspectDevice = RestCreator.getRxRestService().getInspectDeviceByUserId(userid);
        Observable item = RestCreator.getRxRestService().getItemByUserId(userid);
        Observable place = RestCreator.getRxRestService().getPlaceByUserId(userid);
        Observable.merge(inspectDevice,place,item).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(List list) {
                        //         Toast.makeText(PrimaryActivity.this,list.size()+","+list,Toast.LENGTH_LONG).show();
                        if(list==null||list.size()==0) {
                            return;
                        }
                        if (list.get(0) instanceof InspectDevice) {
                            List<InspectDevice> localDevice = InspectDevicQueryUtil.getInpectDeviceListByUserId(ctx);
                            List<InspectDevice> inspectDevices = InspectDevicQueryUtil.getInpectDeviceListByTime(ctx);
                            //    Toast.makeText(PrimaryActivity.this, inspectDevices.size()+","+localDevice.size()+","+list.size(), Toast.LENGTH_SHORT).show();
                            //        Toast.makeText(PrimaryActivity.this, "" + inspectDevices.size() + "," + localDevice.size(), Toast.LENGTH_SHORT).show();
                            if (list.size() != localDevice.size() || inspectDevices.size()==0) {//没有更新inspectdevice
                                InspectDevicQueryUtil.deleteByUserID(userid);
                                InspectDevicQueryUtil.saveByList(list);
                            }
                        }else if(list.get(0) instanceof Place){
                            List<Place> placeList = PlaceQueryUtil.getPlaceByUserId(userid);
                            List<Place> placesbyTime = PlaceQueryUtil.getPlaceByTime(ctx);
                            //         Toast.makeText(PrimaryActivity.this, ""+placesbyTime.size(), Toast.LENGTH_SHORT).show();
                            if(list.size()!=placeList.size() || placesbyTime.size()==0){
                                PlaceQueryUtil.deleteByUserId(userid);
                                PlaceQueryUtil.saveWithList(list);
                            }
                        }else if(list.get(0) instanceof Item){
                            List<Item> itemList = ItemQueryUtil.getItemByUserId(userid);
                            List<Item> items = ItemQueryUtil.getItemByTime(ctx);
                            //       Toast.makeText(PrimaryActivity.this,MyApplication.InInpectLine+","+list.size()+","+itemList.size(), Toast.LENGTH_SHORT).show();

                            if(list.size()!=itemList.size() || items.size()==0){
                                ItemQueryUtil.deleteByUserId(userid);
                                ItemQueryUtil.saveWithItemList(list);
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ThrowableUtil.exceptionManager(e,ctx);
                    }

                    @Override
                    public void onComplete() {
//                        Intent intent = new Intent(ctx,InspectActivity.class);
//                        ctx.startActivity(intent);

                    }
                });
    }
    private static List<InspectDevice> deviceAdapterData(Context ctx) {
        List<InspectDevice> adapterData = new ArrayList<>();
        List<InspectDevice> devices = InspectDevicQueryUtil.getInpectDeviceListByTime(ctx);
        if(devices.size()!=0){
            MyApplication.InInpectLine = devices.get(0).getLineID();
            for(InspectDevice device:devices) {
                if (device.getInspectStatus() == 0) {
                    adapterData.add(device);
                }
            }
        }
        return  adapterData;
    }
}
