package com.yxst.inspect.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yxst.inspect.R;
import com.yxst.inspect.activity.adapter.LubeItemAdapter;
import com.yxst.inspect.database.Manager.LubeDeviceQuery;
import com.yxst.inspect.database.Manager.LubeItemQuery;
import com.yxst.inspect.database.model.LubeDevice;
import com.yxst.inspect.database.model.LubeItem;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.util.SharedPreferenceUtil;
import com.yxst.inspect.util.TimeUtil;

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
润滑项界面
 */
public class LubeItemActivity extends BaseActivity implements LubeItemAdapter.OnItemClickListener {

    @BindView(R.id.rv_lubeItem)
    RecyclerView rv;
    @BindView(R.id.tv_nfc_pname) TextView deviceName;
    LubeItemAdapter adapter;
    private static final int INSPECT_RESULT = 0;
    private List<LubeItem> items;
    View llview;
    View rootview;
    private Long itemId;
    private Long zoneId;
    private Context mContext;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lubeitem);
        rootview = LayoutInflater.from(LubeItemActivity.this).inflate(R.layout.activity_lubeitem, null);
        llview = findViewById(R.id.ll_lube);
        ButterKnife.bind(this);
        this.mContext = this;
        //获取传过来的参数
        Long deviceId = getIntent().getLongExtra("deviceId",0);
        Long lineId = getIntent().getLongExtra("lineId",0);
        //设置标题名称
        LubeDevice device = LubeDeviceQuery.findByDeviceId(this,deviceId,lineId);
        if(device!=null){
            setTitle(device.getEquipmentName());
        }
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(layoutManager);
        //根据NFC卡绑定的设备，查询部位下
        items = LubeItemQuery.findByDeviceID(this,deviceId,lineId);
        Log.e("items", "items：" + "" + items.size());
        adapter = new LubeItemAdapter(this, items);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

    }
 /*
    巡检项的 检查类型
    1 测振型
    2 观察型
    3 测温型
    4 抄表型
    5 测温测振型
 */


    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
           adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(1,intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view, String fatName,String unit,Long zoneId,Long itemId, int position) {
//        initPopuptWindow(position);
//        mPopupWindow.showAsDropDown(rootview,0,mScreenWidth,Gravity.BOTTOM);

        Intent intent = new Intent(LubeItemActivity.this,LubeValueActivity.class);
        intent.putExtra("name",fatName);
        intent.putExtra("zoneId",zoneId);
        intent.putExtra("itemId",itemId);
        intent.putExtra("unit",unit);
        startActivity(intent);
    }
    int mScreenWidth;
    int mScreenHeight;
    PopupWindow mPopupWindow;
    int mPopupWindowWidth;
    int mPopupWindowHeight;
    View view;
    /**
     * 设置pop框，暂时未用
     */
    private void initPopuptWindow(int positon) {
        //加载pop框的视图布局view
        view = View.inflate(LubeItemActivity.this, R.layout.popupwindow_lube, null);
        mScreenWidth= getWindowManager().getDefaultDisplay().getWidth();
        mScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
        mPopupWindow = new PopupWindow(view, mScreenWidth, mScreenHeight-mScreenWidth+200);
        // 获取屏幕的width和height
        //获取pop框的宽和高
        mPopupWindowWidth = mPopupWindow.getWidth();
        mPopupWindowHeight = mPopupWindow.getHeight();

        // 需要设置一下此参数，点击外边可消失
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失    这两步用于点击手机的返回键的时候，不是直接关闭activity,而是关闭pop框
        mPopupWindow.setOutsideTouchable(true);

        // 设置此参数获得焦点，否则无法点击，即：事件拦截消费
        mPopupWindow.setFocusable(true);
        final LubeItem item = items.get(positon);
        TextView unit = view.findViewById(R.id.tv_unit);
        unit.setText(item.getUnit());
        Button save = view.findViewById(R.id.btn_save);
        final EditText remark = view.findViewById(R.id.et_remark);
        final EditText add = view.findViewById(R.id.et_add);
        add.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.NONE, true) {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_PHONE;
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = add.getText().toString();
                if(num!=null &&!("".equals(num))){
                    item.setRealNum(Integer.valueOf(num));
                    LubeItemQuery.update(item);
                }
                mPopupWindow.dismiss();
                if(add.getText().equals("")){
                    return;
                }
                adapter.notifyDataSetChanged();
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("AddValue",add.getText().toString());
                params.put("UpdateUser",SharedPreferenceUtil.getName(LubeItemActivity.this,"User"));
                params.put("Remark",remark.getText().toString());
                params.put("PlanID",item.getPlanID());
                params.put("ZoneID",item.getZoneID());
                params.put("LubricationItemID",item.getLubricationItemID());
                params.put("FatID",item.getFatID());
                params.put("BeginTime",TimeUtil.dateTimeFormat2(item.getBeginTime()));
                params.put("EndTime",TimeUtil.dateTimeFormat2(item.getEndTime()));
                Log.e("asf",new Gson().toJson(params).toString());
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), new Gson().toJson(params));
                Observable statusValue = RestCreator.getRxRestService().postLubRecord(requestBody);
                statusValue.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {
                                if(Integer.valueOf(o.toString())>0){

                                }
                                Toast.makeText(LubeItemActivity.this, "提交成功！" +
                                        o.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(LubeItemActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("asd",throwable.getMessage());
                            }
                        });
            }
        });
        //设置动画   采用属性动画
        mPopupWindow.setAnimationStyle(R.style.AnimTheme);

//        ImageView guanbi = view.findViewById(R.id.guanbi);
//
//        //点击“X”形图片的关闭pop框
//        guanbi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPopupWindow.dismiss();
//            }
//        });

        //点击pop框的其他地方也可以关闭pop框
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPopupWindow.dismiss();
//            }
//        });
    }

}
