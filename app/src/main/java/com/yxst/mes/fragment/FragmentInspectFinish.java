package com.yxst.mes.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxst.mes.R;
import com.yxst.mes.activity.PlaceLookActivity;
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.InspectDeviceDao;
import com.yxst.mes.database.model.InspectDevice;
import com.yxst.mes.database.model.Place;
import com.yxst.mes.fragment.adapter.InspectAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentInspectFinish extends Fragment implements InspectAdapter.OnItemClickListener {
    private RecyclerView rv;
    private List<InspectDevice> adapterData;
    private  InspectAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapterData = new ArrayList<InspectDevice>();
        List<InspectDevice> devices = DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder()
                .where(InspectDeviceDao.Properties.BeginTime.le(new Date()),
                        InspectDeviceDao.Properties.EndTime.ge(new Date())).list();
        Log.e("Inspect",devices.size()+"");
        for(InspectDevice device:devices){
            List<Place> places = DatabaseManager.getInstance().getPlaceDao()._queryInspectDevice_PlaceList(device.getEquipmentID());

        }

        Log.e("123456","LineData:"+adapterData.size());
        View view = inflater.inflate(R.layout.fragment_inspect_all,container,false);
        rv =  (RecyclerView)view.findViewById(R.id.rv_all);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapter = new InspectAdapter(adapterData);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        adapterData.clear();
    }

    @Override
    public void onClick(Long deviceId,Long lineId ,int position) {
        Intent intent = new Intent(getActivity(),PlaceLookActivity.class);
        intent.putExtra("deviceId",deviceId);
        startActivity(intent);
    }
}
