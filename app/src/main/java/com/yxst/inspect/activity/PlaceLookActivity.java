package com.yxst.inspect.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.R;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.model.Device;
import com.yxst.inspect.database.model.InspectDevice;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.Manager.InspectDevicQueryUtil;
import com.yxst.inspect.database.Manager.ItemQueryUtil;
import com.yxst.inspect.database.Manager.PlaceQueryUtil;
import com.yxst.inspect.database.model.Place;
import com.yxst.inspect.nfc.NFCInspectActivity;
import com.yxst.inspect.nfc.adapter.PlaceAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.DialogInterface.OnClickListener;
/*
部位界面
 */
public class PlaceLookActivity extends BaseActivity implements PlaceAdapter.OnItemClickListener {

    @BindView(R.id.lv_nfc_place)
    RecyclerView lvplaceItem;
    @BindView(R.id.tv_nfc_pname) TextView deviceName;
    PlaceAdapter adapter;
    private static final int INSPECT_RESULT = 0;
    private Long deviceId;
    private Device device;
    private Long lineId;//存入检测表中
    private List<Place> places;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_look);
        ButterKnife.bind(this);
        //页面访问记录
       // VisitManager.pageRecord(this,getTitle().toString());
        Long deviceId = getIntent().getLongExtra("deviceId",0);
        lineId = getIntent().getLongExtra("lineId",0);
        InspectDevice device = InspectDevicQueryUtil.getInpectDeviceByDeviceID(this,deviceId,lineId);
        if (device != null) {//已绑定
            Log.e("nfcplace", "根据nfc码查询设备：" + "" + device.getEquipmentName());
            setTitle(device.getEquipmentName());
            //根据NFC卡绑定的设备，查询部位下
            lineId = device.getLineID();
            places = PlaceQueryUtil.getPlaceByDeviceId(this,deviceId,lineId);
            Log.e("nfcplace", "places：" + "" + places.size());
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            lvplaceItem.setLayoutManager(layoutManager);
            adapter = new PlaceAdapter(this, places);
            adapter.setOnItemClickListener(this);
            lvplaceItem.setAdapter(adapter);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
           adapter.notifyDataSetChanged();
        if(places.size()!=0){
            for(Place place:places){
                List<Item> items = ItemQueryUtil.getItemByPlaceId(this,place.getPlaceID(),place.getLineID());
                //一个部位下 完成的Item。
                List<Item> finishItems = ItemQueryUtil.getItemByGEStatus(this,place.getPlaceID(),place.getLineID(), ConfigInfo.CHECKED_STATUS);
                if(items.size() == finishItems.size() && items.size()!=0){
                    //更新place状态
                    place.setCheckStatus(ConfigInfo.CHECKED_STATUS);
                    DatabaseManager.getInstance().getPlaceDao().update(place);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(1,intent);
    }

    @Override
    public void onClick(View view, String placeName, Long placeId, int position) {

        List<Item> items = ItemQueryUtil.getItemByPlaceId(this,placeId,lineId);
        if(items.size() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("该部位未配置巡检项，请确认！")
                    .setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            return;
                        }
                    });
            builder.show();
        }else{
            //         Toast.makeText(this, "placeId="+placeId, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,NFCInspectActivity.class);
            intent.putExtra("placeid",placeId);
            intent.putExtra("lineid",lineId);
            intent.putExtra("placeName",placeName);
            startActivity(intent);
            //startActivityForResult(intent,INSPECT_RESULT);
        }
    }
}
