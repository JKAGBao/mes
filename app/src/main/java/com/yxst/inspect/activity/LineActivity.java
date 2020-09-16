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
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yxst.inspect.R;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.InspectLineDao;
import com.yxst.inspect.database.model.InspectLine;
import com.yxst.inspect.fragment.adapter.LineRvAdapter;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.util.SharedPreferenceUtil;
import com.yxst.inspect.util.ThrowableUtil;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/*
线路管理
 */
public class LineActivity extends BaseActivity implements LineRvAdapter.OnClickListener{

    @BindView(R.id.rv_bind)
    RecyclerView rvBind;
    private static final int BIND_REQUEST_CONDE = 1;
    private LineRvAdapter adapter = null;
    private PopupWindow popupWindow;//
    private long lineId;
    private Button button;
    private LinearLayout llBind;
    private View view;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bind);
        ButterKnife.bind(this);
        setTitle("线路管理");
        //RecyclerView 的线性布局设置
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rvBind.setLayoutManager(layoutManager);
    //  rvBind.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        //从数据库中获取所有线路
        Long userId = SharedPreferenceUtil.getId(LineActivity.this,"User");
        List<InspectLine> lines = DatabaseManager.getInstance().getInspectLineDao().queryBuilder().where(
                InspectLineDao.Properties.BeginTime.le(new Date())
                , InspectLineDao.Properties.EndTime.ge(new Date())
                ,InspectLineDao.Properties.UserID.eq(userId))
                .list();

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
            //加载数据
            adapter =  new LineRvAdapter(lines);
            rvBind.setAdapter(adapter);
            adapter.setOnClickListener(this);

        }
        int  mScreenWidth= getWindowManager().getDefaultDisplay().getWidth();
        view = getLayoutInflater().inflate(R.layout.popupwindow_line_stop,null);
        popupWindow = new PopupWindow(view,mScreenWidth,200);//参数为1.View 2.宽度 3.高度
        popupWindow.setOutsideTouchable(true);//设置点击外部区域可以取消popupWindow


    }

    @Override
    public void onClick(View view, View btn,Long lineId,String name, int Position) {
        llBind = (LinearLayout)view;
//        Intent intent = new Intent(this,NFCBindLineActivity.class);
        Intent intent = new Intent(this,AnInspectActivity.class);
        intent.putExtra("LineId",lineId);
        intent.putExtra("LineName",name);
         startActivityForResult(intent,BIND_REQUEST_CONDE);

    }
    long lineID;
    @Override
    public void onItemClick(View view, View btn, final Long lineId, int Position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                LineActivity.this)
                .setMessage("确定要报备线路停用吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InspectLine line = DatabaseManager.getInstance().getInspectLineDao().queryBuilder().where(
                                InspectLineDao.Properties.LineID.eq(lineId)
                                ,InspectLineDao.Properties.BeginTime.le(new Date())
                                ,InspectLineDao.Properties.EndTime.ge(new Date())
                        ).list().get(0);
                        //  Log.e("saf","line"+line.getLineID());
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put("LineID",line.getLineID());
                        params.put("BeginTime",line.getBeginTime());
                        params.put("EndTime",line.getEndTime());
                        params.put("RunStates","1");

                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), new Gson().toJson(params));
                        Observable statusValue = RestCreator.getRxRestService().stopLineStatus(requestBody);
                        statusValue.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer() {
                                    @Override
                                    public void accept(Object o) throws Exception {
                                        if(Integer.valueOf(o.toString())>0){
                                            Toast.makeText(LineActivity.this, "停用已报备！" +
                                                    "", Toast.LENGTH_SHORT).show();
                                        //    finish();
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        ThrowableUtil.exceptionManager(throwable,LineActivity.this);
                                    //    Toast.makeText(LineActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();

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
