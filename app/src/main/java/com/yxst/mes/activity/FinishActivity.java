package com.yxst.mes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yxst.mes.R;
import com.yxst.mes.activity.adapter.FinishRvAdapter;
import com.yxst.mes.database.model.InspectDevice;
import com.yxst.mes.database.model.InspectImage;
import com.yxst.mes.database.Manager.InspectDevicQueryUtil;
import com.yxst.mes.database.Manager.InspectImageQueryUtil;
import com.yxst.mes.rx.manager.InspectImageManager;
import com.yxst.mes.rx.manager.RecordManager;
import com.yxst.mes.rx.manager.VisitManager;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FinishActivity extends BaseActivity implements FinishRvAdapter.OnItemClickListener{

    @BindView(R.id.rv_all) RecyclerView rv;
    private List<InspectDevice> deviceData;
    private FinishRvAdapter adapter;
    public static final int INSPECTED = 2;
    public static final int UNUPLOAD_STATUS = 0;//未上传


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countFinishPlace();

        setContentView(R.layout.activity_finish);
        setTitle("已检设备");
        ButterKnife.bind(this);
        //页面访问记录
        VisitManager.pageRecord(this,getTitle().toString());
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(layoutManager);
        //正在巡检状态的，未上传的显示出来
        deviceData = InspectDevicQueryUtil.getInpectDevicesByStatus(this,INSPECTED);
//        deviceData = InspectDevicQueryUtil.getInpectDeviceListByStatus(INSPECTING,UNUPLOAD_STATUS,this);
        adapter = new FinishRvAdapter(this,deviceData);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "asf"+(adapter==null?1:2), Toast.LENGTH_SHORT).show();
        if (adapter!=null){
    //        deviceData = InspectDevicQueryUtil.getInpectDeviceListByStatus(FINISH_STATUS,this);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null){
           // deviceData = InspectDevicQueryUtil.getInpectDeviceListByStatus(FINISH_STATUS,this);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
      //      Toast.makeText(this,requestCode+","+resultCode ,Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(final View view , final Long deviceId,final Long lineId, int position) {
        if(view.getId() == R.id.ll_device){
            List<InspectDevice> devices = InspectDevicQueryUtil.getByInspectStatus(this,deviceId,lineId,2);
            if(devices.size()!=0){
                Intent intent = new Intent(this,PlaceLookActivity.class);
                intent.putExtra("deviceId",deviceId);
                intent.putExtra("lineId",lineId);
                startActivity(intent);
            }else{
                Toast.makeText(this, "巡检完成，才可查看修改！", Toast.LENGTH_SHORT).show();

            }
        }
        else if(view.getId() == R.id.btn_upload){
            final Button btn = (Button) view;
     //       Toast.makeText(this, ""+deviceId, Toast.LENGTH_SHORT).show();
            RecordManager.postInpectRecord(this,deviceId,lineId,btn);
            List<InspectImage> serverImageList = InspectImageQueryUtil.getImageByServerStatus(this,deviceId,lineId);
            List<InspectImage> uploadImageList = InspectImageQueryUtil.getImageByUploadStatus(this,deviceId,lineId);
            if(serverImageList.size()!=0){
                InspectImageManager.postInpsectImageToServer(this,serverImageList);
            }
            if(uploadImageList.size()!=0){
                InspectImageManager.upLoadImageByStatus(this,uploadImageList);
            }

        }
       }

    private void countFinishPlace(){
//        InspectDevicQueryUtil.updateInspectDeviceStatus(this);
        InspectDevicQueryUtil.updateInspectDeviceFinishStatus(this);
    }


    }



