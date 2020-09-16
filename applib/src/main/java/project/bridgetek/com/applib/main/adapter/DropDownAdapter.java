package project.bridgetek.com.applib.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.DropDown;

public class DropDownAdapter extends BaseAdapter {
    List<DropDown> mList;
    Context mContext;
    String value = "";
    String status = "";

    public DropDownAdapter(List<DropDown> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public DropDown getItem(int position) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_drop_down, null);
            viewHolder = new ViewHolder();
            viewHolder.tvDropDown = view.findViewById(R.id.tv_drop_down);
            viewHolder.llCount = view.findViewById(R.id.ll_count);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DropDown item = getItem(position);
        viewHolder.tvDropDown.setText(item.getOptionName());
        if (item.isSelect()) {
            viewHolder.llCount.setBackgroundResource(R.color.exception_company);
            value = item.getOptionValue();
            status = item.getOptionStatus();
        } else {
            viewHolder.llCount.setBackgroundResource(R.color.upback);
        }
        return view;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    class ViewHolder {
        TextView tvDropDown;
        LinearLayout llCount;
    }
}
