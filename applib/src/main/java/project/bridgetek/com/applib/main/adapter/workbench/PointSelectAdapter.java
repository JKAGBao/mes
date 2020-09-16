package project.bridgetek.com.applib.main.adapter.workbench;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.workbench.Devs;

/**
 * Created by czz on 19-5-8.
 */

public class PointSelectAdapter extends BaseAdapter {
    Context mContext;
    List<Devs.Dev.PointsBean> mList;

    public PointSelectAdapter(Context mContext, List<Devs.Dev.PointsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Devs.Dev.PointsBean getItem(int position) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_point_select, null);
            viewHolder = new ViewHolder();
            viewHolder.tvPointName = view.findViewById(R.id.tv_point_name);
            viewHolder.tvPointValue = view.findViewById(R.id.tv_point_value);
            viewHolder.cbPoint = view.findViewById(R.id.cb_point);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Devs.Dev.PointsBean item = getItem(position);
        viewHolder.tvPointName.setText(item.getPointName());
        viewHolder.tvPointValue.setText(item.getGetDate());
        viewHolder.cbPoint.setChecked(item.isSelect());
        return view;
    }

    class ViewHolder {
        TextView tvPointValue, tvPointName;
        CheckBox cbPoint;
    }
}
