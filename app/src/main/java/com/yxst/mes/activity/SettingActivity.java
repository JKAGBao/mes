package com.yxst.mes.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yxst.mes.R;
import com.yxst.mes.activity.adapter.LubeItemAdapter;
import com.yxst.mes.activity.adapter.SettingAdapter;
import com.yxst.mes.database.Manager.LubeDeviceQuery;
import com.yxst.mes.database.Manager.LubeItemQuery;
import com.yxst.mes.database.model.LubeDevice;
import com.yxst.mes.database.model.LubeItem;
import com.yxst.mes.net.RestCreator;
import com.yxst.mes.nfc.NFCBindLineActivity;
import com.yxst.mes.nfc.NFCSettingActivity;
import com.yxst.mes.util.SharedPreferenceUtil;
import com.yxst.mes.util.TimeUtil;
import com.yxst.mes.util.TitleBarUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SettingActivity extends BaseActivity implements SettingAdapter.OnItemClickListener {

    private SettingAdapter adapter;
    private static final int INSPECT_RESULT = 0;
    private List<LubeItem> items;
    @BindView(R.id.ll_identify)LinearLayout llIdentify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setTitle("配置");
    }
    @OnClick(R.id.ll_identify)
    public void onViewClicked(View view){
        Intent rfid = new Intent(SettingActivity.this,NFCSettingActivity.class);
        startActivity(rfid);
    }



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
    public void onClick(View view, Long lineId, int position) {

    }
}
