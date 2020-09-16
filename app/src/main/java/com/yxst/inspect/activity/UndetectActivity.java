package com.yxst.inspect.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;
import com.yxst.inspect.activity.adapter.UndetectAdapter;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.model.Undetect;
import com.yxst.inspect.rx.manager.VisitManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By YuanCheng on 2019/6/5 16:36
 */
public class UndetectActivity extends BaseActivity  {
    @BindView(R.id.rv_undetect)
    RecyclerView rv;
    private UndetectAdapter adapter;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undetect);
        ButterKnife.bind(this);
        setTitle("漏检设备");
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(layoutManager);
        //查询数据
        List<Undetect> undetects = DatabaseManager.getInstance().getUndetectDao().loadAll();
        adapter = new UndetectAdapter(undetects);
        rv.setAdapter(adapter);

        //页面访问记录
//       VisitManager.pageRecord(this,getTitle().toString());
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
