package com.yxst.inspect.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.R;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.DeviceDao;
import com.yxst.inspect.database.model.Device;
import com.yxst.inspect.database.model.Sample;
import com.yxst.inspect.model.Version;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.util.SharedPreferenceUtil;
import com.yxst.inspect.util.TimeUtil;
import com.yxst.inspect.util.VersionUtil;
import com.yxst.inspect.util.XmlUtil;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SampleActivity extends BaseActivity {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private Long deviceId;
    private Device device;
    private DeviceDao deviceDao;
    TimePicker timePicker;
    DatePicker datePicker;
    @BindView(R.id.et_date) EditText etDate;
    @BindView(R.id.et_user)EditText etUser;
    @BindView(R.id.btn_certain)TextView tvCertain;
    @BindView(R.id.btn_cancel)TextView tvCancel;

    @BindView(R.id.et_name)EditText etName;
    @BindView(R.id.tv_name)TextView tvName;
    @BindView(R.id.iv_date) ImageView ivDate;
    private Context mContext ;
    private long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 2000L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
        mContext = this;
        setTitle("质量采样");
        //版本更新查询
        new Thread(){
            @Override
            public void run() {
                super.run();
                int REQUEST_EXTERNAL_STORAGE = 1;
                String[] PERMISSIONS_STORAGE = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                int permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SampleActivity.this,
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                }
                Version version = XmlUtil.readXmlFromServer(ConfigInfo.VERSION_UPDATE_URL,mContext);
                // Version version = XmlUtil.loadLocalXml(PrimaryActivity.this);
                if(version!=null){
                    if(version.getCode()> VersionUtil.packageCode(mContext)){
                        VersionUtil.versionUpdate(version.getCode(),version.getName(),version.getUrl()
                                ,mContext);
                    }
                }
            }

        }.start();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDate.setText("");
                etName.setText("");
                etUser.setText("");
            }
        });
        tvCertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > TIME_INTERVAL) {
                    // do something
                    mLastClickTime = nowTime;
                    String date = etDate.getText().toString();
                    String sampleName = etName.getText().toString();
                    String userName = etUser.getText().toString();
            //    Toast.makeText(context, "s=", Toast.LENGTH_SHORT).show();
                    if(TextUtils.isEmpty(date) || TextUtils.isEmpty(sampleName) || TextUtils.isEmpty(userName)){
                        Toast.makeText(mContext, "不能提交空数据！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                String user = SharedPreferenceUtil.getName(SampleActivity.this,"User");
                Observable<String> submit = RestCreator.getRxRestService().submitSample(tvName.getText().toString(),user,etDate.getText().toString());
                submit.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                if(s.equals("1")){//表示数据提交正常
                                    Toast.makeText(mContext, "数据提交成功！", Toast.LENGTH_SHORT).show();
                                    etDate.setText("");
                                    etName.setText("");
                                    etUser.setText("");
                                }else{
                                    Toast.makeText(mContext, "数据提交异常，请联系管理员！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                } else {
                    Toast.makeText(SampleActivity.this, "不要重复点击！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                View dialogView = View.inflate(mContext, R.layout.dialog_date, null);
                timePicker = (TimePicker) dialogView.findViewById(R.id.tpPicker);
                datePicker = (DatePicker) dialogView.findViewById(R.id.dpPicker);
                AlertDialog.Builder builder = new AlertDialog.Builder(SampleActivity.this)
                .setTitle("检测日期")
                .setView(dialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {  //获取当前选择的时间
                            @Override
                            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                            }
                        });
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),
                                timePicker.getHour(),timePicker.getMinute());
                        etDate.setText(TimeUtil.pageDateFormat(calendar.getTime()));
                   Log.e("date:",","+TimeUtil.pageDateFormat(calendar.getTime()));
                  //      etDate.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                  //      dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();
                //初始化日期监听事件
                Resources systemResources = Resources.getSystem();
                int hourNumberPickerId = systemResources.getIdentifier("hour", "id", "android");
                int minuteNumberPickerId = systemResources.getIdentifier("minute", "id", "android");
                int dayNumberPickerId = systemResources.getIdentifier("day", "id", "android");
                int monthNumberPickerId = systemResources.getIdentifier("month", "id", "android");
                int yearNumberPickerId = systemResources.getIdentifier("year", "id", "android");
                NumberPicker hourNumberPicker = (NumberPicker) timePicker.findViewById(hourNumberPickerId);
                NumberPicker minuteNumberPicker = (NumberPicker) timePicker.findViewById(minuteNumberPickerId);
                NumberPicker dayNumberPicker = datePicker.findViewById(dayNumberPickerId);
                NumberPicker monthNumberPicker = datePicker.findViewById(monthNumberPickerId);
                NumberPicker yearNumberPicker = datePicker.findViewById(yearNumberPickerId);
                minuteNumberPicker.setMinValue(0);
                minuteNumberPicker.setMaxValue(0);
                setNumberPickerDivider(minuteNumberPicker);
                setNumberPickerDivider(hourNumberPicker);
                setNumberPickerDivider(dayNumberPicker);
                setNumberPickerDivider(monthNumberPicker);
                setNumberPickerDivider(yearNumberPicker);
                timePicker.setIs24HourView(true);
                Calendar calendar = Calendar.getInstance();
                timePicker.setCurrentHour(calendar.get(calendar.HOUR_OF_DAY));
                timePicker.setMinute(0);

                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {  //获取当前选择的时间
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                    }
                });
            }
        });

    }
    private void setNumberPickerDivider(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {  //设置颜色
                pf.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(
                        ContextCompat.getColor(this, R.color.colorPrimaryDark)); //选择自己喜欢的颜色
                try {
                    pf.set(numberPicker, colorDrawable);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (pf.getName().equals("mSelectionDividerHeight")) {   //设置高度
                pf.setAccessible(true);
                try {
                    int result = 3;  //要设置的高度
                    pf.set(picker, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            picker.invalidate();
        }
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


//        Observable devices = RestCreator.getRxRestService().getSample(1423433867);
//        devices.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List list) {
//                        Toast.makeText(SampleActivity.this, list.size() + "schedule", Toast.LENGTH_LONG).show();
//                        if (list.size() != 0) {
//                            if (list.get(0) instanceof Sample) {
////                                DatabaseManager.getInstance().getSampleDao().saveInTx(list);
//                                etName.setText(((Sample) list.get(0)).getSampleName());
//                                String user = SharedPreferenceUtil.getRealName(SampleActivity.this,"User");
//                                etUser.setText(user);
//                                etDate.setText(TimeUtil.NoMiniteDateFormat(new Date()));
//                                //                Toast.makeText(ScheduleActivity.this,list.size()+"Device",Toast.LENGTH_LONG).show();
////                                onCreate(null);
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }
    /**
     * 获得焦点，按钮可以点击
     */
    @Override
    public void onResume() {
        super.onResume();
    //    onCreate(null);
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
            String hex = flipHexStr(ByteArrayToHexString(detectedTag.getId()));
            Long cardNo = Long.parseLong(flipHexStr(ByteArrayToHexString(detectedTag.getId())), 16);
            //if cardNo不为空，查询是否已经绑定过，未绑定的话，进行绑定
            if(cardNo!=null) {

                Observable device = RestCreator.getRxRestService().getSample(cardNo);
                Observable devices = RestCreator.getRxRestService().getSample(1423433867);
                device.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List list) {
                               if(list.size()==0){
                                   Toast.makeText(SampleActivity.this, "该卡未绑定，请联系管理员！", Toast.LENGTH_LONG).show();

                               }else{
                                    if (list.get(0) instanceof Sample) {

                                    //    tvName.setText(((Sample) list.get(0)).getDataBaseName());
                                        tvName.setText(((Sample) list.get(0)).getID()+"");
                                        etName.setText(((Sample) list.get(0)).getSampleName());
                                        String user = SharedPreferenceUtil.getRealName(SampleActivity.this,"User");
                                        etUser.setText(user);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(new Date());
                                        cal.add(Calendar.HOUR_OF_DAY, 1);//24小时制
                                        //cal.add(Calendar.HOUR, x);12小时制
                                        etDate.setText(TimeUtil.NoMiniteDateFormat(cal.getTime()));
                                        //                Toast.makeText(ScheduleActivity.this,list.size()+"Device",Toast.LENGTH_LONG).show();
//                                onCreate(null);

                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                //     Toast.makeText(this,intent.getAction()+","+device.getNfcCode().toString(),Toast.LENGTH_LONG).show();
            }
    }
    @Override
    public void onNewIntent(Intent intent) {
        Log.e("nfc",intent.getAction());
        readnfc(intent);

      //  mNfcText.setText(mTagText);
    }
    private void updateNfcCode(Long cardNo){
        device.setRFID(cardNo.toString());
        DatabaseManager.getInstance().getDeviceDao().update(device);
        Observable<String> rfid = RestCreator.getRxRestService().postDeviceCode(deviceId.intValue(),cardNo.toString());
        rfid.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(String string) {
                        Toast.makeText(SampleActivity.this,"绑定成功！",Toast.LENGTH_LONG).show();

                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SampleActivity.this,"请确认网络是否正常！",Toast.LENGTH_LONG).show();
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
