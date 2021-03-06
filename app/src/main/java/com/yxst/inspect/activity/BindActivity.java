package com.yxst.inspect.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.model.Device;
import com.yxst.inspect.fragment.adapter.BindRvAdapter;
import com.yxst.inspect.nfc.NFCBindActivity;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class BindActivity extends AppCompatActivity implements BindRvAdapter.OnClickListener{

    @BindView(R.id.rv_bind)
    RecyclerView rvBind;
    private static final int BIND_REQUEST_CONDE = 1;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bind);
        ButterKnife.bind(this);
        setTitle("绑定设备");

        //RecyclerView 的线性布局设置
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rvBind.setLayoutManager(layoutManager);
        rvBind.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        //获取 所有设备
        List<Device> devices = DatabaseManager.getInstance().getDeviceDao().loadAll();

        //未获取到设备表示未同步数据,提示是否去同步
        if(devices.size()==0){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("未同步到设备数据，检查网络是否正常!");
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
            BindRvAdapter adapter =  new BindRvAdapter(devices);
            rvBind.setAdapter(adapter);
            adapter.setOnClickListener(this);
        }

    }

    private Button button;
    private LinearLayout llBind;
    @Override
    public void onClick(View view, View btn,Long deviceId, int Position) {
        button = (Button) btn;
        llBind = (LinearLayout)view;

        Intent intent = new Intent(this,NFCBindActivity.class);
        intent.putExtra("DeviceId",deviceId);
         startActivityForResult(intent,BIND_REQUEST_CONDE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Log.e("bind-result", "------requestCode:" + requestCode + button);
        switch (requestCode) {
            case BIND_REQUEST_CONDE:
                button.setText("更改RF卡");
//                    button.setEnabled(false);
//                    llBind.setEnabled(false);
                break;
            default:
                break;
        }
    }
}
