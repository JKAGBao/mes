package com.yxst.inspect.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;
import com.yxst.inspect.activity.PlaceLookActivity;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.InspectDeviceDao;
import com.yxst.inspect.database.model.InspectDevice;
import com.yxst.inspect.database.model.Place;
import com.yxst.inspect.fragment.adapter.InspectAdapter;

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

    @SuppressLint("WrongConstant")
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
