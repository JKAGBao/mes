package com.yxst.mes.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.yxst.mes.R;
import com.yxst.mes.activity.adapter.UndetectAdapter;
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.model.Undetect;
import com.yxst.mes.rx.manager.VisitManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By YuanCheng on 2019/6/5 16:36
 */
public class UndetectActivity extends BaseActivity  {
    @BindView(R.id.rv_undetect) RecyclerView rv;
    private UndetectAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undetect);
        ButterKnife.bind(this);
        setTitle("漏检设备");
        List<Undetect> undetects = DatabaseManager.getInstance().getUndetectDao().loadAll();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(layoutManager);
        if(undetects.size()!=0){
           // adapter.setOnItemClickListener(this);
        }
        adapter = new UndetectAdapter(undetects);
        rv.setAdapter(adapter);
        //页面访问记录
       VisitManager.pageRecord(this,getTitle().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
//        adapter.notifyDataSetChanged();
    }



    @Override
    public void onPause() {
        super.onPause();
     //   adapterData.clear();
    }

}
