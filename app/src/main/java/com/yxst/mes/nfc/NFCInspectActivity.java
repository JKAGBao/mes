package com.yxst.mes.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yxst.mes.R;
import com.yxst.mes.activity.BaseActivity;
import com.yxst.mes.activity.ReadMeterActivity;
import com.yxst.mes.database.dao.PlaceDao;
import com.yxst.mes.database.model.Device;
import com.yxst.mes.database.model.Item;
import com.yxst.mes.database.Manager.ItemQueryUtil;
import com.yxst.mes.fragment.ConfigInfo;
import com.yxst.mes.nfc.adapter.NFCItemAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 *
 * Create by YuanCheng on 2019/5/28
 */
public class NFCInspectActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_inspect);
        ButterKnife.bind(this);
        placeName = getIntent().getStringExtra("placeName");
        setTitle(placeName!=null?placeName:getTitle()); //设置标题
        Long placeId = getIntent().getLongExtra("placeid",0);
        lineId = getIntent().getLongExtra("lineid",0);
        // nfcIdentify();识别在 place部分界面执行
        if(placeId!=0){
            List<Item> items = ItemQueryUtil.getItemByPlaceId(this,placeId,lineId);
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
    TextView tvValue;
    Intent intent;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView tvItemId = (TextView) view.findViewById(R.id.tv_item_id);
        TextView tvLineId = (TextView) view.findViewById(R.id.tv_line);
        TextView tvCheckType = (TextView)view.findViewById(R.id.tv_item_checktype);
        Long itemId = Long.valueOf(tvItemId.getText().toString());
        Long lineId = Long.valueOf(tvLineId.getText().toString());
        int checkType = Integer.valueOf(tvCheckType.getText().toString());
        switch (checkType){
            case (ConfigInfo.ITME_TYPE_VIBRATE):
                intent = new Intent(this,MainActivity.class);
                //项id
                intent.putExtra("name",placeName);
                intent.putExtra("itemId",itemId);
                intent.putExtra("lineId",lineId);
                startActivityForResult(intent,ITME_OBSERVE_VALUE);
                break;
            case(ConfigInfo.ITME_TYPE_TEMPERATURE):
                intent = new Intent(this,MainActivity.class);
                //点击的位置
                intent.putExtra("name",placeName);
                intent.putExtra("itemId",itemId);
                intent.putExtra("lineId",lineId);
                startActivityForResult(intent,ITME_OBSERVE_VALUE);
                break;
            case (ConfigInfo.ITME_TYPE_OBSERVE):
                intent = new Intent(this,ObserveActivity.class);
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
        if (mNfcAdapter == null) {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
     //       Toast.makeText(getApplicationContext(),"\"看下这个支持nfc吗 或者 开了没\"", Toast.LENGTH_SHORT).show();
        }
        //一旦截获NFC消息，就会通过PendingIntent调用窗口
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()), 0);

    }
    /**
     * 获得焦点，按钮可以点击
     */
    @Override
    public void onResume() {
        super.onResume();
        if( itemadapter!=null)
             itemadapter.notifyDataSetChanged();
        //设置处理优于所有其他NFC的处理
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }
    /*
    byte数组转为16进制数字
     */
    private  String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        StringBuilder out = new StringBuilder();

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out.append(hex[i]);
            i = in & 0x0f;
            out.append(hex[i]);
        } return out.toString();
    }
    /*
    转为10进制数字
     */
    private String flipHexStr(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= s.length() - 2; i = i + 2) {
            result.append(new StringBuilder(s.substring(i, i + 2)).reverse());
        }
        return result.reverse().toString();
    }

    //暂时用不上
    private void nfcIdentify(Intent intent) {
        //1.获取Tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.e("nfclist","detectedTag.getTechList()"+detectedTag.getTechList().toString());
        //获取支持的类型
        for(int i=0;i<detectedTag.getTechList().length;i++){
            String tag = detectedTag.getTechList()[i];
            Log.e("nfclist",tag);
        }
        try {
            String hex = flipHexStr(ByteArrayToHexString(detectedTag.getId()));
            Long cardNo = Long.parseLong(flipHexStr(ByteArrayToHexString(detectedTag.getId())), 16);
            Toast.makeText(this,cardNo+"",Toast.LENGTH_LONG).show();
            //if cardNo不为空，查询是否已经绑定过，未绑定的话，进行绑定
            if(cardNo!=null){
                // Device device = dao.queryBuilder().where(DeviceDao.Properties.NfcCode.eq(cardNo)).unique();
                Log.e("inspect",device.getEquipmentName());
                if(device != null){//已绑定
//                    lineId = device.getLineID();//巡检线Id，存入检测表Record表中
//                    inspectName.setText(device.getEquipmentName());
//                    items = device.getItemList();
//                    Log.e("inspect",device.getItemList().size()+"");
//                    lvInspectItem.setAdapter(new NFCItemAdapter(this,items));
//                    lvInspectItem.setOnItemClickListener(this);

                }else{//未绑定

                }
                String usedCode = device.getRFID();
                if(usedCode != null&&usedCode.equals(cardNo)){
                    //Toast 请更换卡片

                }else {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
