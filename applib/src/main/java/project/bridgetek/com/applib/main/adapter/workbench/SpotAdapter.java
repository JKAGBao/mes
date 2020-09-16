package project.bridgetek.com.applib.main.adapter.workbench;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.workbench.Trend;

/**
 * Created by czz on 19-7-8.
 */

public class SpotAdapter extends BaseAdapter {
    List<Trend.TrendDatasBean> mList;
    Context mContext;
    String unit;

    public SpotAdapter(List<Trend.TrendDatasBean> mList, Context mContext, String unit) {
        this.mList = mList;
        this.mContext = mContext;
        this.unit = unit;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Trend.TrendDatasBean getItem(int position) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_spot, null);
            viewHolder = new ViewHolder();
            viewHolder.tvKey = view.findViewById(R.id.tv_key);
            viewHolder.tvValue = view.findViewById(R.id.tv_value);
            viewHolder.tvUnit = view.findViewById(R.id.tv_unit);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Trend.TrendDatasBean item = getItem(position);
        viewHolder.tvKey.setText(item.getF_Value());
        viewHolder.tvValue.setText(item.getF_GetDate());
        viewHolder.tvUnit.setText(unit);
        return view;
    }

    class ViewHolder {
        TextView tvKey;
        TextView tvValue;
        TextView tvUnit;
    }
}
