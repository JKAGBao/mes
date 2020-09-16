package com.yxst.inspect.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.Manager.RecordQueryUtil;
import com.yxst.inspect.database.model.Record;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.rx.manager.RecordManager;

import java.util.List;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable()) {
            //上传Record
            List<Record> records = RecordQueryUtil.getRecordListByStatus(context, ConfigInfo.ITEM_UNCHECK_STATUS);
          //  Toast.makeText(context, record.size()+","+records.size(), Toast.LENGTH_SHORT).show();
            if(records.size()!=0){
                RecordManager.postRecords(context,records);
            }
            if("null".equals(RestCreator.UPLOAD_PATH)||"".equals(RestCreator.UPLOAD_PATH)){
                try {
                    RestCreator.UPLOAD_PATH = RestCreator.getImagePath();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
