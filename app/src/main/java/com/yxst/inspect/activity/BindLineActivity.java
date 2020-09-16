package com.yxst.inspect.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.DeviceDao;
import com.yxst.inspect.database.model.Device;
import com.yxst.inspect.database.model.Line;
import com.yxst.inspect.fragment.adapter.BindLineRvAdapter;
import com.yxst.inspect.nfc.NFCBindLineActivity;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class BindLineActivity extends BaseActivity implements BindLineRvAdapter.OnClickListener{

    @BindView(R.id.rv_bind)
    RecyclerView rvBind;
    private static final int BIND_REQUEST_CONDE = 1;
    private BindLineRvAdapter adapter = null;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bind);
        ButterKnife.bind(this);
        setTitle("绑定线路");

        //RecyclerView 的线性布局设置
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rvBind.setLayoutManager(layoutManager);
        rvBind.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        //获取 所有设备
        List<Device> devices = DatabaseManager.getInstance().getDeviceDao().loadAll();
        List<Line> lines = DatabaseManager.getInstance().getLineDao().loadAll();

        //未获取到设备表示未同步数据,提示是否去同步
        if(lines.size()==0){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("未同步到线路数据，检查网络是否正常!");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    return;
                }
            });
            builder.show();

        }else{
            adapter =  new BindLineRvAdapter(lines);
            rvBind.setAdapter(adapter);
            adapter.setOnClickListener(this);

        }

    }

    private Button button;
    private LinearLayout llBind;
    @Override
    public void onClick(View view, View btn,Long lineId, int Position) {
        button = (Button) btn;
        llBind = (LinearLayout)view;

        Intent intent = new Intent(this,NFCBindLineActivity.class);
        intent.putExtra("LineId",lineId);
         startActivityForResult(intent,BIND_REQUEST_CONDE);

    }

    @Override
    public void onItemClick(View view, View btn, Long lineId, int Position) {
        List<String> data = new ArrayList<>();

       if(lineId!=0){
           List<Device>  deviceList = DatabaseManager.getInstance().getDeviceDao().queryBuilder()
                   .where(DeviceDao.Properties.LineID.eq(lineId)).listLazy();
           Log.e("listsize","devicelist"+deviceList.size());
           for(Device device : deviceList){
               data.add(device.getEquipmentName());
           }
       }
        View dialog = getLayoutInflater().inflate(R.layout.dialog_listview, null);
        ListView listView=dialog.findViewById(R.id.listView);
        ArrayAdapter adapter=new ArrayAdapter<String>(BindLineActivity.this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("设备列表")
                .setView(dialog)
                .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface,
                                        int paramAnonymousInt) {

                    }
                }).create();

        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(resultCode!= Activity.RESULT_OK){return;}
        Log.e("bind-result","------requestCode:"+requestCode+button);
            switch (requestCode){
                case BIND_REQUEST_CONDE:
                    button.setText("更改NFC卡");
                    adapter.notifyDataSetChanged();
//                    button.setEnabled(false);
//                    llBind.setEnabled(false);
                    break;
                default:
                    break;
            }
    }
}
