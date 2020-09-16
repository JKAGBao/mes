package com.yxst.inspect.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;
import com.yxst.inspect.activity.ScheduleActivity;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.model.Device;
import com.yxst.inspect.fragment.adapter.BindRvAdapter;
import com.yxst.inspect.nfc.NFCBindActivity;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class BindFragment extends Fragment implements BindRvAdapter.OnClickListener{

    @BindView(R.id.rv_bind)
    RecyclerView rvBind;
    private static final int BIND_REQUEST_CONDE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_bind,container,false);
        ButterKnife.bind(this,view);
        //RecyclerView 的线性布局设置
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rvBind.setLayoutManager(layoutManager);
        //获取 所有设备
        List<Device> devices = DatabaseManager.getInstance().getDeviceDao().loadAll();

        //未获取到设备表示未同步数据,提示是否去同步
        if(devices.size()==0){
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setMessage("需要先同步数据，现在去同步？");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   Intent intent = new Intent(getActivity(),ScheduleActivity.class);
                   startActivity(intent);
                   getActivity().onBackPressed();
                   return;
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();

        }else{
            BindRvAdapter adapter =  new BindRvAdapter(devices);
            rvBind.setAdapter(adapter);
            adapter.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
      //  unbinder.unbind();
    }
     private Button button;
    @Override
    public void onClick(View view, View btn,Long deviceId,int Position) {
        button = (Button) view;
        Intent intent = new Intent(getActivity(),NFCBindActivity.class);
        intent.putExtra("DeviceId",deviceId);
         startActivityForResult(intent,BIND_REQUEST_CONDE);
//        BindFragment.this.

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(resultCode!= Activity.RESULT_OK){return;}
        Log.e("bind-result","------requestCode:"+requestCode+button);
            switch (requestCode){
                case BIND_REQUEST_CONDE:
                    button.setText("已绑定");
                    button.setEnabled(false);
                    break;
                default:
                    break;
            }
    }
}
