package com.yxst.inspect.fragment.inspect;

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
import com.yxst.inspect.database.model.Device;
import com.yxst.inspect.fragment.adapter.WaitAdapter;
import java.util.ArrayList;
import java.util.List;

public class FragmentWait extends Fragment implements WaitAdapter.OnItemClickListener {
    private RecyclerView rv;
    private List<Device> adapterData;
    private  WaitAdapter adapter;

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
        adapterData = new ArrayList<Device>();
//        List<Device> devices = DatabaseManager.getInstance().getDeviceDao().loadAll();
//        for(Device device:devices){
//           List<Place> places =  device.getPlaceList();
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
//            if(device.getInspectStatus() == 0){
//                adapterData.add(device);
//            }
//        }

        Log.e("123456","LineData:"+adapterData.size());
        View view = inflater.inflate(R.layout.fragment_inspect_all,container,false);
        rv =  (RecyclerView)view.findViewById(R.id.rv_all);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapter = new WaitAdapter(adapterData);
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
    public void onClick(Long deviceId, int position) {
        Intent intent = new Intent(getActivity(),PlaceLookActivity.class);
        intent.putExtra("deviceId",deviceId);
        startActivity(intent);
    }
}
