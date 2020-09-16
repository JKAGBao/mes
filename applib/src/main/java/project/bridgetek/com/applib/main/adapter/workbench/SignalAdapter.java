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

public class SignalAdapter extends BaseAdapter {
    private List<Devs.Dev.PointsBean.SignalsBean> mList;
    private Context mContext;

    public SignalAdapter(List<Devs.Dev.PointsBean.SignalsBean> mList, Context context) {
        this.mList = mList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Devs.Dev.PointsBean.SignalsBean getItem(int position) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_signal, null);
            viewHolder = new ViewHolder();
            viewHolder.cbSignal = view.findViewById(R.id.cb_signal);
            viewHolder.tvSignalText = view.findViewById(R.id.tv_signal_text);
            viewHolder.tvSignalValue = view.findViewById(R.id.tv_signal_value);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Devs.Dev.PointsBean.SignalsBean item = getItem(position);
        if (item.getSignalTypeName().equals("1")) {
            viewHolder.tvSignalText.setText(mContext.getString(R.string.test_vibration_rb_signal1_text));
        } else if (item.getSignalTypeName().equals("2")) {
            viewHolder.tvSignalText.setText(mContext.getString(R.string.test_vibration_rb_signal2_text));
        } else if (item.getSignalTypeName().equals("16")) {
            viewHolder.tvSignalText.setText(mContext.getString(R.string.test_vibration_rb_signal3_text));
        } else {
            viewHolder.tvSignalText.setText(mContext.getString(R.string.test_vibration_rb_signal4_text));
        }
        viewHolder.tvSignalValue.setText(item.getValue() + item.getUnit());
        viewHolder.cbSignal.setChecked(item.isSelect());
        return view;
    }

    class ViewHolder {
        CheckBox cbSignal;
        TextView tvSignalText, tvSignalValue;
    }
}
