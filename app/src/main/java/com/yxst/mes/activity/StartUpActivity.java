package com.yxst.mes.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.yxst.mes.R;
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.model.Device;
import com.yxst.mes.database.model.Grade;
import com.yxst.mes.database.model.Line;
import com.yxst.mes.net.RestCreator;
import com.yxst.mes.util.ThrowableUtil;

import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        //让应用主题内容占用系统状态栏的空间,注意:下面两个参数必须一起使用 stable 牢固的
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        //设置状态栏颜色为透明
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_startup);
        //数据库无数据，表示未存储过,存储
        RxRetrofitLine();

    }
    private void RxRetrofitLine(){
        //1.如果网络正常情况下，正常返回值，清一下Line数据库值，获取最新。
        // 2、如果网络正常，服务器异常，未返回值，不清数据库，从数据库获取值
        // 3、如果网络异常，从数据库获取
        Observable grade = RestCreator.getRxRestService().getDangerGrade();
        Observable device = RestCreator.getRxRestService().getDeviceList();
       // Observable line = RestCreator.getRxRestService().getLine();
        Observable line = RestCreator.getRxRestService().getLine();
        Observable.merge(line,grade,device).subscribeOn(Schedulers.io())
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
                            else if (list.get(0) instanceof Line) {
                                //同步设备信息，先清一下之前数据
                                DatabaseManager.getInstance().getLineDao().deleteAll();
                                DatabaseManager.getInstance().getLineDao().insertInTx(list);
                                Log.e("schedule", "Schedule-url-geDevice（）：" + list.size());
                            }
                            else if (list.get(0) instanceof Device) {
                                //同步设备信息，先清一下之前数据
                                DatabaseManager.getInstance().getDeviceDao().deleteAll();
                                DatabaseManager.getInstance().getDeviceDao().insertOrReplaceInTx(list);
                                Log.e("schedule", "Schedule-url-geDevice（）：" + list.size());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ThrowableUtil.exceptionManager(e,StartUpActivity.this);
                        Intent intent = new Intent(StartUpActivity.this,SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(StartUpActivity.this,SignInActivity.class);
                        startActivity(intent);
                        finish();                    }
                });
    }

}
