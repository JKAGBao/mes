package project.bridgetek.com.applib.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.bridgelib.toos.HiApplication;

public class DeviceStateAdapter extends BaseAdapter {
    private List<CheckItemInfo> mList;
    private Context mContext;
    private List<String> stringList;

    public DeviceStateAdapter(List<CheckItemInfo> mList, Context context, List<String> list) {
        this.mList = mList;
        this.mContext = context;
        this.stringList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CheckItemInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_device_state, null);
            viewHolder = new ViewHolder();
            viewHolder.tvDeviceName = view.findViewById(R.id.tv_device_name);
            viewHolder.spinnerSimple = view.findViewById(R.id.spinner_simple);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CheckItemInfo item = getItem(position);
        setSimple(viewHolder.spinnerSimple, mContext, item);
        viewHolder.tvDeviceName.setText(item.getMobjectName());
        viewHolder.tvDeviceName.setTypeface(HiApplication.MEDIUM);
        return view;
    }

    public void setSimple(Spinner spinnerSimple, Context context, final CheckItemInfo item) {
        String[] spinnerItems = null;
        if (!stringList.isEmpty()) {
            spinnerItems = new String[stringList.size()];
            for (int i = 0; i < stringList.size(); i++) {
                spinnerItems[i] = stringList.get(i);
            }
        }
        final String[] finalSpinnerItems = spinnerItems;
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, finalSpinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSimple.setAdapter(spinnerAdapter);
        //选择监听
        spinnerSimple.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                item.setState(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        spinnerSimple.setSelection(item.getState());
    }

    class ViewHolder {
        TextView tvDeviceName;
        Spinner spinnerSimple;
    }
}
