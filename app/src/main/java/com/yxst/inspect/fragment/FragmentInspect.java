package com.yxst.inspect.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.R;
import com.yxst.inspect.activity.PlaceLookActivity;
import com.yxst.inspect.database.model.InspectDevice;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.Manager.InspectDevicQueryUtil;
import com.yxst.inspect.database.Manager.ItemQueryUtil;
import com.yxst.inspect.database.Manager.PlaceQueryUtil;
import com.yxst.inspect.database.model.Place;
import com.yxst.inspect.fragment.adapter.InspectAdapter;
import com.yxst.inspect.net.RestCreator;
import com.yxst.inspect.util.SharedPreferenceUtil;
import com.yxst.inspect.util.ThrowableUtil;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentInspect extends Fragment implements InspectAdapter.OnItemClickListener {
    @BindView(R.id.rv_all)
    RecyclerView rv;
    @BindView(R.id.tv_load) TextView tvLoad;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.progressBarLarge) LinearLayout progressBar;
    private List<InspectDevice> adapterData;
    private  InspectAdapter adapter;
    private Long userid;
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
     //   WaitInspectManager.waitInpectInter(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countFinishPlace();//统计巡检完的部位个数

    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspect_all,container,false);
        ButterKnife.bind(this,view);
        userid = SharedPreferenceUtil.getId(getActivity(),"User");
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapterData = InspectDevicQueryUtil.getInpectDevicesByEQStatus(getActivity(), ConfigInfo.ITEM_UNCHECK_STATUS);;
        adapter = new InspectAdapter(getActivity(), adapterData);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(FragmentInspect.this);
        initPullRefresh();

        return view;
    }
    private Observer<List> observer  =   new Observer<List>() {
        @Override
        public void onSubscribe(Disposable d) {

        }
        @Override
        public void onNext(List list) {
      //      tvLoad.setText("已获取最新！");
            //         Toast.makeText(PrimaryActivity.this,list.size()+","+list,Toast.LENGTH_LONG).show();
            if (list == null || list.size() == 0) {
                return;
            } else if (list.get(0) instanceof InspectDevice) {
                    InspectDevicQueryUtil.deleteByUserID(userid);
                    InspectDevicQueryUtil.saveByList(list);

            } else if (list.get(0) instanceof Place) {
//                List<Place> placeList = PlaceQueryUtil.getPlaceByUserId(userid);
//                List<Place> placesbyTime = PlaceQueryUtil.getPlaceByTime(getActivity());
                    PlaceQueryUtil.deleteByUserId(userid);
                    PlaceQueryUtil.saveWithList(list);

            } else if (list.get(0) instanceof Item) {
//                List<Item> itemList = ItemQueryUtil.getItemByUserId(userid);
//                List<Item> items = ItemQueryUtil.getItemByTime(getActivity());
                ItemQueryUtil.deleteByUserId(userid);
                ItemQueryUtil.saveWithItemList(list);

            }
        }
        @Override
        public void onError(Throwable e) {
            ThrowableUtil.exceptionManager(e, getActivity());
            progressBar.setVisibility(View.GONE);

        }
        @SuppressLint("WrongConstant")
        @Override
        public void onComplete() {
        //    countFinishPlace();
            adapterData = InspectDevicQueryUtil.getInpectDevicesByEQStatus(getActivity(),ConfigInfo.ITEM_UNCHECK_STATUS);;
            if(adapterData.size()!=0){
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(OrientationHelper.VERTICAL);
                rv.setLayoutManager(layoutManager);

                adapter = new InspectAdapter(context, adapterData);
                rv.setAdapter(adapter);
                adapter.setOnItemClickListener(FragmentInspect.this);
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, "已获取最新！", Toast.LENGTH_SHORT).show();

               progressBar.setVisibility(View.GONE);
             }else{
                Toast.makeText(context, "同步未成功！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Observable inspectDevice = RestCreator.getRxRestService().getInspectDeviceByUserId(userid);
                Observable item = RestCreator.getRxRestService().getItemByUserId(userid);
                Observable place = RestCreator.getRxRestService().getPlaceByUserId(userid);
                Observable.merge(inspectDevice,place,item).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);

              }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        if(adapterData!=null){
            adapterData.clear();
        }

    }

    @Override
    public void onClick(Long deviceId, Long lineId,int position) {

        Intent intent = new Intent(getActivity(), PlaceLookActivity.class);
        intent.putExtra("deviceId",deviceId);
        intent.putExtra("lineId",lineId);
        startActivity(intent);

    }
    private void countFinishPlace(){
        InspectDevicQueryUtil.updateInspectDeviceFinishStatus(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

}
