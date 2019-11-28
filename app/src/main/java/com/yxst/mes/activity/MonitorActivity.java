package com.yxst.mes.activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yxst.mes.R;
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.DeviceDao;
import com.yxst.mes.database.model.Device;
import com.yxst.mes.database.model.Monitor;
import com.yxst.mes.net.RestCreator;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MonitorActivity extends BaseActivity {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    @BindView(R.id.tv_nfctext) TextView mNfcText ;
    @BindView(R.id.ll_monitor)
    LinearLayout llMonitor;
    @BindView(R.id.webview)
    WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);

        setContentView(R.layout.activity_monitor);
        ButterKnife.bind(this);
        setTitle("设备监测");

        webView.getSettings().setJavaScriptEnabled(true);
        //扩大缩放
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //自适应
        //     webView.setInitialScale(100);//为25%，最小缩放等级
        // 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
        // 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN );
        webView.getSettings().setLoadWithOverviewMode(true);
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

        if(url!=null && !"".equals(url)){
            llMonitor.setVisibility(View.GONE);
            //    requestWindowFeature(Window.FEATURE_NO_TITLE);
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(url);
        }
    }

    private void readnfc(Intent intent){
        //1.获取Tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        try {
            String hex = flipHexStr(ByteArrayToHexString(detectedTag.getId()));
            Long cardNo = Long.parseLong(flipHexStr(ByteArrayToHexString(detectedTag.getId())), 16);
            //if cardNo不为空，查询是否已经绑定过，未绑定的话，进行绑定
            if(cardNo!=null){
                Device device = DatabaseManager.getInstance().getDeviceDao().queryBuilder().where(DeviceDao.Properties.RFID.eq(cardNo)).unique();
         //       Toast.makeText(MonitorActivity.this, ""+cardNo+",device="+device, Toast.LENGTH_SHORT).show();
                if(device!=null){
                    final Observable<List<Monitor>> monitor = RestCreator.getRxRestService().monitorByEquipmentID(device.getEquipmentID());
    //                    RestCreator.getRxRestService().monitorByEquipmentID(device.getEquipmentID());
                    monitor.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<List<Monitor>>() {
                                @Override
                                public void accept(List<Monitor> monitors) throws Exception {
                                    if(monitors.size()!=0){
                                        //      Toast.makeText(MonitorActivity.this, "" + monitors.get(0).getURL(), Toast.LENGTH_SHORT).show();
                                        llMonitor.setVisibility(View.GONE);
                                        webView.setVisibility(View.VISIBLE);
                                        setTitle(monitors.get(0).getMenuNameCN());
                                        url = monitors.get(0).getURL();
                                        webView.loadUrl(url);
    //                                  webView.setWebViewClient(new WebViewClient(){
    //                                            @Override
    //                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
    //                                                view.loadUrl(url);
    //                                                return true;
    //                                            }
    //                                        });
                                    }
                                    //设置Web视图
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Toast.makeText(MonitorActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setMessage("未查询到该设备的监测信息！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                    builder.show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
