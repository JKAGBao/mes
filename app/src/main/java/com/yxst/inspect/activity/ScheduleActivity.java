
package com.yxst.inspect.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;
import com.yxst.inspect.activity.adapter.ScheduleRvAdapter;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.model.Device;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.model.Line;
import com.yxst.inspect.net.RestCreator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ScheduleActivity extends AppCompatActivity implements ScheduleRvAdapter.OnClickListener {
    @BindView(R.id.rv_schedule)
    RecyclerView rvSchedule;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
        rvSchedule.setLayoutManager(new LinearLayoutManager(this));
        //获取 所有巡检线
        List<Line> lines = DatabaseManager.getInstance().getLineDao().loadAll();
        Log.e("schedule","dao：巡检线"+lines.size()+","+lines.get(0).getLineName());
        ScheduleRvAdapter adapter =  new ScheduleRvAdapter(lines);
        rvSchedule.setAdapter(adapter);
        adapter.setOnClickListener(this);

    }

    @Override
    public void onClick(View view,Long lineId,final int Position) {
          final Button button = (Button) view;
//        Toast.makeText(ScheduleActivity.this,Position+",",Toast.LENGTH_LONG).show();
          button.setText("更新中...");
        //请求设备列表、巡检项
        Observable device = RestCreator.getRxRestService().getDeviceByLineId(lineId.intValue());
        //巡检项请求
        Observable item = RestCreator.getRxRestService().getItemByLineId(lineId.intValue());
        //巡检部位
     // Observable place = RestCreator.getRxRestService().getPlace();
        Observable.merge(device,item).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(List list) {
              //          Toast.makeText(ScheduleActivity.this,list.size()+"schedule",Toast.LENGTH_LONG).show();
                        if (list.size() != 0) {
                            if (list.get(0) instanceof Device) {
                //                Toast.makeText(ScheduleActivity.this,list.size()+"Device",Toast.LENGTH_LONG).show();
                                //同步设备信息，先清一下之前数据
                                DatabaseManager.getInstance().getDeviceDao().deleteAll();
                                for (int i = 0; i < list.size(); i++) {
                                    Device device = (Device) list.get(i);
                                    DatabaseManager.getInstance().getDeviceDao().insert(device);
                                }
                                Log.e("schedule", "Schedule-url-geDevice（）：" + list.size());
                            } else if (list.get(0) instanceof Item) {
              //                  Toast.makeText(ScheduleActivity.this,list.size()+"Item",Toast.LENGTH_LONG).show();

                                DatabaseManager.getInstance().getItemDao().deleteAll();
                                for (int i = 0; i < list.size(); i++) {
                                    Item item = (Item) list.get(i);
                                    DatabaseManager.getInstance().getItemDao().insert(item);
                                }
                                Log.e("schedule", "url-PrimaryItem()：" + list.size());

                            }

                        }
                    }
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        button.setText("已同步");
                        button.setEnabled(false);
                    }
                });
    }

    /*
    ID	1
InspectionType	1
LineCode	"1001"
LineName	"专业巡检一"
     */

}
