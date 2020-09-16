package com.yxst.inspect;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.activity.FinishActivity;
import com.yxst.inspect.activity.InspectActivity;
import com.yxst.inspect.activity.LineActivity;
import com.yxst.inspect.activity.LubeActivity;
import com.yxst.inspect.activity.MonitorActivity;
import com.yxst.inspect.activity.ScheduleActivity;
import com.yxst.inspect.activity.SettingActivity;
import com.yxst.inspect.activity.SignInActivity;
import com.yxst.inspect.activity.TestVibActivity;
import com.yxst.inspect.activity.UnLubeActivity;
import com.yxst.inspect.activity.UndetectActivity;
import com.yxst.inspect.activity.adapter.DividerGridItemDecoration;
import com.yxst.inspect.activity.adapter.PrimaryRvAdapter;
import com.yxst.inspect.broadcast.NetworkChangeReceiver;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.Manager.InspectDevicQueryUtil;
import com.yxst.inspect.database.Manager.InspectDevicValueQueryUtil;
import com.yxst.inspect.database.Manager.ItemQueryUtil;
import com.yxst.inspect.database.Manager.PlaceQueryUtil;
import com.yxst.inspect.database.dao.InspectLineDao;
import com.yxst.inspect.database.dao.UnLubeDao;
import com.yxst.inspect.database.dao.UndetectDao;
import com.yxst.inspect.database.model.Grade;
import com.yxst.inspect.database.model.InspectDevice;
import com.yxst.inspect.database.model.InspectLine;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.model.Place;
import com.yxst.inspect.database.model.UnLube;
import com.yxst.inspect.database.model.Undetect;
import com.yxst.inspect.model.Version;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.nfc.MainActivity;
import com.yxst.inspect.rx.manager.BindDeviceManager;
import com.yxst.inspect.rx.manager.UndectDeviceManager;
import com.yxst.inspect.util.SharedPreferenceUtil;
import com.yxst.inspect.util.ThrowableUtil;
import com.yxst.inspect.util.TitleBarUtil;
import com.yxst.inspect.util.VersionUtil;
import com.yxst.inspect.util.XmlUtil;

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
import project.bridgetek.com.applib.main.activity.VibraTionActivity;

/*
主界面
 */
public class PrimaryActivity extends AppCompatActivity implements PrimaryRvAdapter.OnItemClickListener {

    @BindView(R.id.rv_item)
    RecyclerView rvItem;
    private Long userId;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
             TitleBarUtil.titlebarSetting(this);
            setContentView(R.layout.activity_primary);
            rvItem = findViewById(R.id.rv_item);
            ButterKnife.bind(this);
            userId = SharedPreferenceUtil.getId(this,"User");
            PrimaryRvAdapter mAdapter = new PrimaryRvAdapter(ConfigInfo.getInstance().ITEMS_PRIMARY,this);
            rvItem.setLayoutManager(new GridLayoutManager(this,2));
            rvItem.addItemDecoration(new DividerGridItemDecoration(this));
            rvItem.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);
            //版本更新查询
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    int REQUEST_EXTERNAL_STORAGE = 1;
                    String[] PERMISSIONS_STORAGE = {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.BLUETOOTH
                    };
                    int permission = ActivityCompat.checkSelfPermission(PrimaryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PrimaryActivity.this,
                                PERMISSIONS_STORAGE,
                                REQUEST_EXTERNAL_STORAGE
                        );

                    }
                    //读取服务器上的version.xml文件，查询版本是否需要更新。
                    Version version = XmlUtil.readXmlFromServer(ConfigInfo.VERSION_UPDATE_URL,PrimaryActivity.this);
                 // Version version = XmlUtil.loadLocalXml(PrimaryActivity.this);
                    if(version!=null){
                        if(version.getCode()>VersionUtil.packageCode(PrimaryActivity.this)){
                            VersionUtil.versionUpdate(version.getCode(),version.getName(),version.getUrl()
                                    ,PrimaryActivity.this);
                        }
                    }
                }

        }.start();

        if(userId == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(PrimaryActivity.this)
                    .setMessage("用户还未登录，请先登录")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(PrimaryActivity.this, SignInActivity.class);
                            startActivity(intent);
                        }
                    });
            builder.show();
        }

        //动态注册广播
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);



    }

    /*
       首页 9宫格
        */
    @Override
    public void onItemClick(View view, int position) {
        Long userId = SharedPreferenceUtil.getId(PrimaryActivity.this,"User");
        switch (position){
            case ConfigInfo.BIND_DEVICE:
                BindDeviceManager.bindDevice(PrimaryActivity.this);
                //  List<Line> lines = DatabaseManager.getInstance().getLineDao().loadAll();
//                if(lines.size()==0){
//                }else{
//                    Intent intent = new Intent(PrimaryActivity.this,BindLineActivity.class);
//                    startActivity(intent);
//                }
//                String roleCode = SharedPreferenceUtil.getRoleCode(this,"User");
//                if(roleCode.equals("system") || roleCode.equals("administrator")){
//                }else{
//                    Toast.makeText(this, "由管理员绑定！", Toast.LENGTH_SHORT).show();
//                }
                break;
            case ConfigInfo.LINE:
                //请求在巡检线路
                Observable line = RestCreator.getRxRestService().getLineByUser(userId);
                line.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }
                            @Override
                            public void onNext(List list) {
                                //请求所有的任务线成功后，先清一下Line表数据
                                if (list != null && list.size() > 0) {
                                    if (list.get(0) instanceof InspectLine) {
                                        //同步设备信息，先清一下之前数据
                                        long userId = SharedPreferenceUtil.getId(PrimaryActivity.this,"User");
                                        List<InspectLine> inspectDeviceList = DatabaseManager.getInstance().getInspectLineDao().queryBuilder().where(
                                                InspectLineDao.Properties.UserID.eq(userId)).list();
                                        for (InspectLine inspectDevice:inspectDeviceList){
                                            DatabaseManager.getInstance().getInspectLineDao().delete(inspectDevice);
                                        }
                                   //     DatabaseManager.getInstance().getInspectLineDao().deleteInTx(list);
                                        DatabaseManager.getInstance().getInspectLineDao().insertOrReplaceInTx(list);
                                        Log.e("schedule", "Schedule-url-geDevice（）：" + list.size());
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                ThrowableUtil.exceptionManager(e,PrimaryActivity.this);
                                Intent intent = new Intent(PrimaryActivity.this,LineActivity.class);
//                        Intent intent = new Intent(context,BindActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onComplete() {
//                        Intent intent = new Intent(context,BindLineActivity.class);
                                Intent line = new Intent(PrimaryActivity.this, LineActivity.class);
                                startActivity(line);
                            }
                        });

                break;

            case ConfigInfo.UN_INSPECT:
                List<Undetect> undetectList = DatabaseManager.getInstance().getUndetectDao().queryBuilder().where(
                        UndetectDao.Properties.BeginTime.le(new Date())
                        ,UndetectDao.Properties.EndTime.ge(new Date())).list();
                if(undetectList.size()==0){
                    UndectDeviceManager.undectDeviceMutual(PrimaryActivity.this);
                }else{
                    Intent intent = new Intent(this, UndetectActivity.class);
                    startActivity(intent);
                }
                break;
            case ConfigInfo.WAIT_INSPECT:
         //      Toast.makeText(PrimaryActivity.this,"id:"+userid,Toast.LENGTH_LONG).show();
                Intent intentw = new Intent(PrimaryActivity.this,InspectActivity.class);
                startActivity(intentw);
                break;

            case ConfigInfo.FINISH_INSPECT :
                Intent intent = new Intent(PrimaryActivity.this,FinishActivity.class);
                startActivity(intent);
                break;
            case ConfigInfo.WAIT_LUBE:
                Intent iLube = new Intent(PrimaryActivity.this,LubeActivity.class);
                startActivity(iLube);
                break;
            case ConfigInfo.TEST_VIB://
                Intent vib = new Intent(PrimaryActivity.this, TestVibActivity.class);
                startActivity(vib);
                break;
            case ConfigInfo.UN_LUBE://6
                List<UnLube> unLubeList = DatabaseManager.getInstance().getUnLubeDao().queryBuilder().where(
                        UnLubeDao.Properties.BeginTime.le(new Date())
                        ,UnLubeDao.Properties.EndTime.ge(new Date())
                ).list();
                if(unLubeList.size()==0){
                    Observable<List<UnLube>> unlube = RestCreator.getRxRestService().getMissLube(userId);
                    unlube.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<List<UnLube>>() {
                                @Override
                                public void accept(List<UnLube> unLubes) throws Exception {
                                    if(unLubes.size()>0){
                                        DatabaseManager.getInstance().getUnLubeDao().deleteAll();
                                        DatabaseManager.getInstance().getUnLubeDao().insertInTx(unLubes);
                                    }
                                    Intent unLube = new Intent(PrimaryActivity.this,UnLubeActivity.class);
                                    startActivity(unLube);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Intent unLube = new Intent(PrimaryActivity.this,UnLubeActivity.class);
                                    startActivity(unLube);
                                }
                            });
                }else{
                    Intent unLube = new Intent(PrimaryActivity.this,UnLubeActivity.class);
                    startActivity(unLube);
                }
                break;
            case ConfigInfo.MONITOR://设备监测

                Intent monitor = new Intent(PrimaryActivity.this, MonitorActivity.class);
                startActivity(monitor);
                break;
            case ConfigInfo.SETTING://设置

                Intent setting = new Intent(PrimaryActivity.this,SettingActivity.class);
                startActivity(setting);
                break;
            case ConfigInfo.BLUE://设置

                Intent blue = new Intent(PrimaryActivity.this, VibraTionActivity.class);
                startActivity(blue);
                break;
            case 20:
                Intent aintent = new Intent(PrimaryActivity.this,ScheduleActivity.class);
                startActivity(aintent);
                break;
            default:
                break;

        }
    }
    @Override
    public void onItemLongClick(View view, int position) {

    }


    private void RxRetrofitLine(){
        //1.如果网络正常情况下，正常返回值，清一下Line数据库值，获取最新。
        // 2、如果网络正常，服务器异常，未返回值，不清数据库，从数据库获取值
        // 3、如果网络异常，从数据库获取
        Observable grade = RestCreator.getRxRestService().getDangerGrade();
        Observable place = RestCreator.getRxRestService().getPlace();
        Observable.merge(place,grade).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(List list) {
                        //请求所有的任务线成功后，先清一下Line表数据
                        if(list!=null&&list.size()>0){
                            if(list.get(0) instanceof Grade){
                                DatabaseManager.getInstance().getGradeDao().deleteAll();
                                //存储
                                DatabaseManager.getInstance().getGradeDao().insertOrReplaceInTx(list);

                            }
                            else if(list.get(0) instanceof Place){
                                DatabaseManager.getInstance().getPlaceDao().deleteAll();
                                DatabaseManager.getInstance().getPlaceDao().insertOrReplaceInTx(list);

                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Intent intent = new Intent(PrimaryActivity.this,InspectActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(PrimaryActivity.this,InspectActivity.class);
                        startActivity(intent);
                        finish();                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("primary","onResume");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("primary","onRestart");
        List<InspectDevice> inspectDeviceList = InspectDevicQueryUtil.getInpectDeviceListByTime(PrimaryActivity.this);
        //              Toast.makeText(SignInActivity.this, ""+inspectDeviceList.size(), Toast.LENGTH_SHORT).show();
        //数据库无数据，表示未存储过,存储
        if (inspectDeviceList.size() == 0) {

            Observable inspectDevice = RestCreator.getRxRestService().getInspectDeviceByUserId(userId);
            Observable item = RestCreator.getRxRestService().getItemByUserId(userId);
            Observable place = RestCreator.getRxRestService().getPlaceByUserId(userId);
            Observable.merge(inspectDevice, place, item).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            InspectDevicValueQueryUtil.deleteByUserID(userId);
                            //Toast.makeText(SignInActivity.this, deviceList.size()+","+inspectDeviceList.size(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onNext(List list) {
                            if (list == null || list.size() == 0) {
                                return;
                            }
                            if (list.get(0) instanceof InspectDevice) {
                      //没有更新inspectdevice
                                    InspectDevicQueryUtil.deleteByUserID(userId);
                                    InspectDevicQueryUtil.saveByList(list);
                            } else if (list.get(0) instanceof Place) {

                                    PlaceQueryUtil.deleteByUserId(userId);
                                    PlaceQueryUtil.saveWithList(list);

                            } else if (list.get(0) instanceof Item) {

                                    ItemQueryUtil.deleteByUserId(userId);
                                    ItemQueryUtil.saveWithItemList(list);

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            ThrowableUtil.exceptionManager(e, PrimaryActivity.this);

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PrimaryActivity.this)
                .setMessage("确定要退出吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }
}
