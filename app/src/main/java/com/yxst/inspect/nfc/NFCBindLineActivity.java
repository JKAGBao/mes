package com.yxst.inspect.nfc;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.yxst.inspect.R;
import com.yxst.inspect.activity.BaseActivity;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.DeviceDao;
import com.yxst.inspect.database.dao.LineDao;
import com.yxst.inspect.database.model.Line;
import com.yxst.inspect.net.RestCreator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NFCBindLineActivity extends BaseActivity {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    @BindView(R.id.tv_nfctext) TextView mNfcText ;
    private Long lineId;
    private Line line;
    private DeviceDao deviceDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        setTitle("绑定RFID");
        ButterKnife.bind(this);
        lineId = getIntent().getLongExtra("LineId",0);
        if(lineId!=0){
            line = DatabaseManager.getInstance().getLineDao().queryBuilder().where(LineDao.Properties.ID.eq(lineId)).unique();
        }
      //  Toast.makeText(this, "deviceId:"+deviceId, Toast.LENGTH_SHORT).show();
    }
    /**
     * 启动Activity，界面可见时
     */
    @Override
    protected void onStart() {
        super.onStart();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(getApplicationContext(),
                    "\"看下这个支持nfc吗 或者 开了没\"", Toast.LENGTH_SHORT).show();
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
        //设置处理优于所有其他NFC的处理
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    private void readnfc(Intent intent){
        //1.获取Tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String[] obtain = detectedTag.getTechList();
        for(int i=0;i<obtain.length;i++){
            String o = obtain[i];
      //      Toast.makeText(this,o,Toast.LENGTH_LONG).show();
        }
        try {
            String hex = flipHexStr(ByteArrayToHexString(detectedTag.getId()));
            Long cardNo = Long.parseLong(flipHexStr(ByteArrayToHexString(detectedTag.getId())), 16);
            //if cardNo不为空，查询是否已经绑定过，未绑定的话，进行绑定
            if(cardNo!=null){

                Line undifyLine = DatabaseManager.getInstance().getLineDao().queryBuilder().where(LineDao.Properties.RFID.eq(cardNo)).unique();
                //不为空表示已经绑定，
                if(undifyLine!=null){
                    String code = line.getRFID();
                 //   Toast.makeText(this, "code："+code+","+value, Toast.LENGTH_SHORT).show();
                    if(undifyLine.getRFID().equals(code)){
                        //Toast 请更换卡片
                        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                .setTitle("该NFC卡已经绑定了该设备，请换一张！")
                                .setPositiveButton("确定",new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();
                    }else {

                        String lineName = undifyLine.getLineName();
                        //Toast 请更换卡片
                        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                .setTitle("已绑定了" + lineName + "，请更换新的卡片！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();
                    }

                } else {

                    updateNfcCode(cardNo);
                }
            }
       //     Toast.makeText(this,intent.getAction()+","+device.getNfcCode().toString(),Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onNewIntent(Intent intent) {
        Log.e("nfc",intent.getAction());
        readnfc(intent);

      //  mNfcText.setText(mTagText);
    }
    private void updateNfcCode(Long cardNo){
        line.setRFID(cardNo.toString());
        DatabaseManager.getInstance().getLineDao().update(line);
        Observable<String> rfid = RestCreator.getRxRestService().postLineCode(lineId.intValue(),cardNo.toString());
        rfid.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(String string) {
                        Toast.makeText(NFCBindLineActivity.this,"绑定成功！",Toast.LENGTH_LONG).show();

                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(NFCBindLineActivity.this,"请确认网络是否正常！",Toast.LENGTH_LONG).show();
                        setResult(RESULT_CANCELED);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        setResult(RESULT_OK);
                        finish();                  }
                });
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
}
