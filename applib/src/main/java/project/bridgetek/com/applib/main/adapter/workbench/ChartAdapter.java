package project.bridgetek.com.applib.main.adapter.workbench;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.workbench.Trend;
import project.bridgetek.com.applib.main.toos.LineChartManager;
import project.bridgetek.com.bridgelib.toos.CountString;

/**
 * Created by czz on 19-5-8.
 */

public class ChartAdapter extends BaseAdapter {
    List<Trend> mList;
    Context mContext;

    public ChartAdapter(List<Trend> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Trend getItem(int position) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_chart, null);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
            viewHolder.lineChart = view.findViewById(R.id.chartOscW);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Trend item = getItem(position);
        String type = "";
        String unit = "";
        List<Float> list1 = new ArrayList<>();
        for (int i = 0; i < item.getTrendDatas().size(); i++) {
            list1.add(Float.valueOf(item.getTrendDatas().get(i).getF_Value()));
        }
        if (item.getF_SignalTypeId().equals("1")) {
            type = mContext.getString(R.string.test_vibration_rb_signal1_text);
            unit = CountString.ACCELERATION;
        } else if (item.getF_SignalTypeId().equals("2")) {
            type = mContext.getString(R.string.test_vibration_rb_signal2_text);
            unit = CountString.BAT;
        } else if (item.getF_SignalTypeId().equals("16")) {
            type = mContext.getString(R.string.test_vibration_rb_signal3_text);
            unit = CountString.TEMPERATURE;
        } else {
            type = mContext.getString(R.string.test_vibration_rb_signal4_text);
            unit = CountString.SHIFT;
        }
        if (list1.size() > 0) {
            LineChartManager lineChartManager = new LineChartManager(viewHolder.lineChart, unit, 3);
            lineChartManager.initLineDataSet(list1, type, mContext.getResources().getColor(R.color.theme), 3);
            lineChartManager.setXAxis(item.getTrendDatas());
            lineChartManager.setMarkerView(mContext);
        }
        return view;
    }

    class ViewHolder {
        LineChart lineChart;
    }
}
