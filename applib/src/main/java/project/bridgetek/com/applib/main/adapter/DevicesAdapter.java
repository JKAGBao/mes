package project.bridgetek.com.applib.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.Devices;
import project.bridgetek.com.bridgelib.toos.HiApplication;

/**
 * Created by Cong Zhizhong on 18-7-6.
 */

public class DevicesAdapter extends BaseAdapter {
    private List<Devices> mList;
    private Context mContext;

    public DevicesAdapter(List<Devices> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Devices getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_devices, null);
            viewHolder = new ViewHolder();
            viewHolder.mTvDeviceLocation = view.findViewById(R.id.tv_DeviceLocation);
            viewHolder.mTvDevices = view.findViewById(R.id.tv_devices);
            viewHolder.mTvMobjectCode = view.findViewById(R.id.tv_MobjectCode);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Devices item = getItem(position);
        viewHolder.mTvDevices.setText(item.getDeviceName());
        viewHolder.mTvMobjectCode.setText(item.getDeviceCode());
        viewHolder.mTvDeviceLocation.setText(item.getDeviceLocation());
        viewHolder.mTvDevices.setTypeface(HiApplication.BOLD);
        viewHolder.mTvDeviceLocation.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvMobjectCode.setTypeface(HiApplication.REGULAR);
        return view;
    }

    class ViewHolder {
        TextView mTvDeviceLocation, mTvMobjectCode, mTvDevices;
    }
}
