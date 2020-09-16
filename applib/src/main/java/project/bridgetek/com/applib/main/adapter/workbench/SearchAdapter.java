package project.bridgetek.com.applib.main.adapter.workbench;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.workbench.DeviceSearch;

/**
 * Created by czz on 19-5-21.
 */

public class SearchAdapter extends BaseAdapter {
    private List<DeviceSearch> mList;
    private Context mContext;

    public SearchAdapter(List<DeviceSearch> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public DeviceSearch getItem(int position) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_device_search, null);
            viewHolder = new ViewHolder();
            viewHolder.tvUnit = view.findViewById(R.id.tv_unit);
            viewHolder.tvDeviceName = view.findViewById(R.id.tv_device_name);
            viewHolder.tvDeviceCode = view.findViewById(R.id.tv_device_code);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DeviceSearch item = getItem(position);
        String string = mContext.getString(R.string.semicolon);
        viewHolder.tvDeviceName.setText(mContext.getString(R.string.check_label_tv_text) + string + item.getDevName());
        viewHolder.tvUnit.setText("作业线名称" + string + item.getProdLineSName());
        viewHolder.tvDeviceCode.setText("作业线父节点名称" + string + item.getFullName());
        return view;
    }

    class ViewHolder {
        TextView tvUnit;
        TextView tvDeviceName;
        TextView tvDeviceCode;
    }
}
