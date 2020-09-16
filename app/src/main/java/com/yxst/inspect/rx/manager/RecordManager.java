package com.yxst.inspect.rx.manager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.database.model.InspectDevice;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.Manager.InspectDevicQueryUtil;
import com.yxst.inspect.database.Manager.InspectDevicValueQueryUtil;
import com.yxst.inspect.database.Manager.ItemQueryUtil;
import com.yxst.inspect.database.Manager.RecordQueryUtil;
import com.yxst.inspect.database.model.Record;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.util.ThrowableUtil;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created By YuanCheng on 2019/7/5 18:54
 */
public class RecordManager {
    @SuppressLint("CheckResult")
    public static void postInpectRecord(final Context cxt, final Long deviceId, final Long lineId, final Button btn ){
        List<Record> records = RecordQueryUtil.getRecordListByDeviceId(cxt,deviceId,lineId,1);
//        if(records.size()==0){
//            final AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
//            builder.setMessage("暂无最新巡检项提交！")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //showStatus变为1，表示已上传
//                            dialog.dismiss();
//                        }
//                    });
//            builder.show();
//        }else{
//
//        }
            Gson gson = new Gson();
            String array = gson.toJson(records);
            Log.e("gson",array+records.size());
            Observable postRecord = RestCreator.getRxRestService().postInspectItemRecord(array);
            postRecord.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer() {
                        @Override
                        public void accept(Object o) throws Exception {
                            Toast.makeText(cxt, "上传成功！"+o.toString(), Toast.LENGTH_SHORT).show();
                            if(Integer.valueOf(o.toString()) > 0){
                                //已经上传的Record记录，状态改为2，表示已上传
                                List<Record> records = RecordQueryUtil.getRecordListByDeviceId(cxt,deviceId,lineId,1);
                                for(Record record : records){
                                    record.setCheckStatus(2);//已上传
                                    RecordQueryUtil.updateRecord(record);
                                }
                                Toast.makeText(cxt, "上传成功！", Toast.LENGTH_SHORT).show();
                                //showStatus变为1，表示已上传
//                                InspectDevice inspectDevice = InspectDevicQueryUtil.getInpectDeviceByDeviceID(cxt, deviceId,lineId);
//                                InspectDevicValueQueryUtil.saveShowStatus(cxt,inspectDevice,1);
//                                //判断Device下Item是否都已上传，
//                                List<Record> recordList = RecordQueryUtil.getRecordListByDeviceId(cxt,deviceId,lineId,2);
//                                List<Item> itemList = ItemQueryUtil.getItemByDeviceId(cxt,deviceId,lineId);
//                                if(itemList.size() == recordList.size()){
//                                    //表示已上传完成
//                                    InspectDevice device = InspectDevicQueryUtil.getInpectDeviceByDeviceID(cxt, deviceId,lineId);
//                                    device.setUploadStatus(1);
//                                    InspectDevicQueryUtil.updateInpectDevice(device);
//                                    InspectDevicValueQueryUtil.saveInspectDeviceValue(cxt,device, ConfigInfo.ITEM_UPLOAD_STATUS);
//                                }
                                btn.setText("已上传");
                                btn.setEnabled(false);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ThrowableUtil.exceptionManager(throwable,cxt);
                            //Toast.makeText(FinishActivity.this, "上传失败"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


    }
    public static void realPostRecord(final Context cxt, final List<Record> records){

        Gson gson = new Gson();
        String array = gson.toJson(records);
        Log.e("gson",array+records.size());
        Observable postRecord = RestCreator.getRxRestService().postInspectItemRecord(array);
        postRecord.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                    //          Toast.makeText(cxt, ""+o.toString(), Toast.LENGTH_SHORT).show();
                        if(Integer.valueOf(o.toString()) > 0){
                            //已经上传的Record记录，状态改为2，表示已上传
                            for(Record record:records){
                                record.setCheckStatus(2);
                                RecordQueryUtil.updateRecord(record);
                            }

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ThrowableUtil.exceptionManager(throwable,cxt);
                        //Toast.makeText(FinishActivity.this, "上传失败"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void postRecords(final Context cxt, final List<Record> records){

            Gson gson = new Gson();
            String array = gson.toJson(records);
            Log.e("gson",array+records.size());
            Observable postRecord = RestCreator.getRxRestService().postInspectItemRecord(array);
            postRecord.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer() {
                        @Override
                        public void accept(Object o) throws Exception {
                 //           Toast.makeText(cxt, "上传"+o.toString(), Toast.LENGTH_SHORT).show();

                            if(Integer.valueOf(o.toString()) > 0){
                                //已经上传的Record记录，状态改为2，表示已上传
                                for(Record record:records){
                                    record.setCheckStatus(2);
                                    RecordQueryUtil.updateRecord(record);
                                    InspectDevice device = InspectDevicQueryUtil.getInpectDeviceBy(cxt, record.getEquipmentID(),record.getLineID(),0);
                                    if(device!=null){
                                        device.setUploadStatus(1);//表示都已上传
                                        InspectDevicQueryUtil.updateInpectDevice(device);
                                        InspectDevicValueQueryUtil.saveInspectDeviceValue(cxt,device,ConfigInfo.ITEM_UPLOAD_STATUS);
                                    }

                                }

                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ThrowableUtil.exceptionManager(throwable,cxt);
                            Toast.makeText(cxt, "上传失败"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
    }
}
