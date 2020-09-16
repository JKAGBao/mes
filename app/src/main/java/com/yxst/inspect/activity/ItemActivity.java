package com.yxst.inspect.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.R;
import com.yxst.inspect.database.dao.PlaceDao;
import com.yxst.inspect.database.model.Device;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.Manager.ItemQueryUtil;
import com.yxst.inspect.nfc.MainActivity;
import com.yxst.inspect.nfc.ObserveActivity;
import com.yxst.inspect.nfc.adapter.NFCItemAdapter;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Create by YuanCheng on 2019/5/28
 */
public class ItemActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_nfc_inspect) ListView lvInspectItem;
    @BindView(R.id.tv_nfcinpect_name) TextView inspectName;
    private Long deviceId;
    private Device device;
    private PlaceDao placeDao;
    private Long lineId;//存入检测表中
    private List<Item> items;
    private String placeName;
    private static final int ITME_OBSERVE_VALUE = 0;
    private NFCItemAdapter itemadapter;
    Long placeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_inspect);
        ButterKnife.bind(this);
        placeName = getIntent().getStringExtra("placeName");
        setTitle(placeName!=null?placeName:getTitle()); //设置标题
        placeId = getIntent().getLongExtra("placeid",0);
        lineId = getIntent().getLongExtra("lineid",0);
        // nfcIdentify();识别在 place部分界面执行
        if(placeId!=0){
            items = ItemQueryUtil.getItemByPlaceId(this,placeId,lineId);
            itemadapter = new NFCItemAdapter(this,items);
            lvInspectItem.setAdapter(itemadapter);
            lvInspectItem.setOnItemClickListener(this);
        }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        TextView tvItemId = (TextView) view.findViewById(R.id.tv_item_id);
        TextView tvLineId = (TextView) view.findViewById(R.id.tv_line);
        TextView tvCheckType = (TextView)view.findViewById(R.id.tv_item_checktype);
        Long itemId = Long.valueOf(tvItemId.getText().toString());
        Long lineId = Long.valueOf(tvLineId.getText().toString());
        if(items.size()!=0){
            int checkType = Integer.valueOf(tvCheckType.getText().toString());
            switch (checkType){
                case (ConfigInfo.ITME_TYPE_VIBRATE):
                case(ConfigInfo.ITME_TYPE_TEMPERATURE):
                    intent = new Intent(this, MainActivity.class);
                    //点击的位置
                    intent.putExtra("name",placeName);
                    intent.putExtra("itemId",itemId);
                    intent.putExtra("lineId",lineId);
                    startActivityForResult(intent,ITME_OBSERVE_VALUE);
                    break;
                case (ConfigInfo.ITME_TYPE_OBSERVE):
                    intent = new Intent(this, ObserveActivity.class);
                    intent.putExtra("name",placeName);
                    intent.putExtra("itemId",itemId);
                    intent.putExtra("lineId",lineId);
                    startActivityForResult(intent,ITME_OBSERVE_VALUE);
                    break;
                case(ConfigInfo.ITME_TYPE_READMETER):
                    intent = new Intent(this,ReadMeterActivity.class);
                    intent.putExtra("name",placeName);
                    intent.putExtra("itemId",itemId);
                    intent.putExtra("lineId",lineId);
                    startActivityForResult(intent,ITME_OBSERVE_VALUE);
                    break;
                case(ConfigInfo.ITME_TYPE_VIBRATE_TEMP):
                    break;
                default:
                    break;
            }


        }
//        else{
//
//            Toast.makeText(this, "请贴RF卡，以完成巡检！", Toast.LENGTH_SHORT).show();
//        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=Activity.RESULT_OK){ return;}
        switch (requestCode){
            case ITME_OBSERVE_VALUE:
                itemadapter.notifyDataSetChanged();
                break;
        }
    }


    /**
     * 启动Activity，界面可见时
     */
    @Override
    protected void onStart() {
        super.onStart();

    }
    /**
     * 获得焦点，按钮可以点击
     */
    @Override
    public void onResume() {
        super.onResume();
        if( itemadapter!=null)
             itemadapter.notifyDataSetChanged();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
