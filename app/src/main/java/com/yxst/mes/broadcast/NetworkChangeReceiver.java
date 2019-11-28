package com.yxst.mes.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.yxst.mes.PrimaryActivity;
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.Manager.RecordQueryUtil;
import com.yxst.mes.database.dao.RecordDao;
import com.yxst.mes.database.model.Record;
import com.yxst.mes.net.RestCreator;
import com.yxst.mes.rx.manager.RecordManager;

import java.util.List;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable()) {
            List<Record> record = DatabaseManager.getInstance().getRecordDao().loadAll();
            //上传Record
            List<Record> records = RecordQueryUtil.getRecordListByStatus(context,1);
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
