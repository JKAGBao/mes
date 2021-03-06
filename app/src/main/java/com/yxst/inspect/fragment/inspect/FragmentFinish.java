package com.yxst.inspect.fragment.inspect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.yxst.inspect.database.model.Device;
import com.yxst.inspect.fragment.adapter.FinishAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentFinish extends Fragment implements FinishAdapter.OnItemClickListener {
    private RecyclerView rv;
    private List<Device> adapterData;
    private FinishAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapterData = new ArrayList<Device>();
//        List<Device> devices = DatabaseManager.getInstance().getDeviceDao().loadAll();
//        for(Device device:devices){
//            List<Place> places =  device.getPlaceList();
//            List<Place> finishSize = new ArrayList<>();
//            for(int p=0;p<places.size();p++){
//                Place place = places.get(p);
//                if(place.getInspectStatus()==1){
//                    finishSize.add(place);
//                }
//            }
//            if(finishSize.size() == places.size()){
//                device.setInspectStatus(1);
//                DatabaseManager.getInstance().getDeviceDao().update(device);
//            }
//            if(device.getInspectStatus() == 1){
//                adapterData.add(device);
//            }
//        }
//        Log.e("123456","LineData:"+adapterData.size());
        View view = inflater.inflate(R.layout.fragment_inspect_finish,container,false);
        rv =  (RecyclerView)view.findViewById(R.id.rv_finish);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapter = new FinishAdapter(adapterData);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onClick(Long deviceId, int position) {
        Intent intent = new Intent(getActivity(),PlaceLookActivity.class);
        intent.putExtra("deviceId",deviceId);
        startActivity(intent);
    }
}
