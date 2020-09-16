package project.bridgetek.com.applib.main.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.inuker.bluetooth.library.search.SearchResult;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.BluetoothSetActivity;
import project.bridgetek.com.bridgelib.toos.Logger;


public class BluetoothAdapter extends BaseAdapter {
    int selectIndex = -1;
    private List<SearchResult> mBluelist;
    private LayoutInflater layoutInflater;
    BluetoothSetActivity mContext;

    public BluetoothAdapter(BluetoothSetActivity context, List<SearchResult> list) {
        this.mBluelist = list;
        this.layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mBluelist.size();
    }

    @Override
    public SearchResult getItem(int position) {
        return mBluelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.bluetooth_list, null);
            viewHolder.mDeviceName = view.findViewById(R.id.device_name);
            viewHolder.mDeviceAddress = view.findViewById(R.id.device_address);
            viewHolder.mRbNormal = view.findViewById(R.id.rb_normal);
            viewHolder.mLlConnect = view.findViewById(R.id.ll_connect);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final SearchResult result = mBluelist.get(position);
        Logger.i("getView: " + selectIndex);
        if (position == selectIndex) {//当前下标和选中位置一样时则radiobutton为选中状态，反之未选中
            viewHolder.mRbNormal.setChecked(true);
        } else {
            viewHolder.mRbNormal.setChecked(false);
        }
        final String deviceName = result.getName();
        viewHolder.mDeviceName.setText(TextUtils.isEmpty(deviceName) ? mContext.getString(R.string.setup_bluetooth_adapter_devicename_text) : deviceName);
        String deviceAddress = "[" + result.getAddress() + "]";
        viewHolder.mDeviceAddress.setText(deviceAddress);
        viewHolder.mLlConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndex(position, deviceName, result.getAddress());
            }
        });
        viewHolder.mRbNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndex(position, deviceName, result.getAddress());
            }
        });

        return view;
    }

    public void setIndex(int index, String deviceName, String deviceAddress) {//设置选中位置，并更新adapter
        this.selectIndex = index;
        mContext.setName(deviceName, deviceAddress);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView mDeviceName;
        TextView mDeviceAddress;
        RadioButton mRbNormal;
        LinearLayout mLlConnect;
    }
}
