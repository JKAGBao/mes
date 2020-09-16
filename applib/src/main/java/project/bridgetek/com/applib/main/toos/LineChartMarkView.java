package project.bridgetek.com.applib.main.toos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import project.bridgetek.com.applib.R;

/**
 * Created by czz on 19-5-9.
 */

public class LineChartMarkView extends MarkerView {


    private TextView tvDate;
    private TextView tvValue0;
    private IAxisValueFormatter xAxisValueFormatter;
    List<String> dataXList = new LinkedList<>();
    protected DecimalFormat mFormat;
    protected int mDecimalDigits;

    public LineChartMarkView(Context context, IAxisValueFormatter xAxisValueFormatter, List<String> dataXList, int digits) {
        super(context, R.layout.markview);
        this.xAxisValueFormatter = xAxisValueFormatter;
        this.dataXList = dataXList;
        this.mDecimalDigits = digits;
        setup(mDecimalDigits);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvValue0 = (TextView) findViewById(R.id.tv_value0);
    }

    public void setup(int digits) {
        this.mDecimalDigits = digits;
        StringBuffer b = new StringBuffer();

        for (int i = 0; i < digits; ++i) {
            if (i == 0) {
                b.append(".");
            }

            b.append("0");
        }

        this.mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        Chart chart = getChartView();
        if (chart instanceof LineChart) {
            LineData lineData = ((LineChart) chart).getLineData();
            //获取到图表中的所有曲线
            List<ILineDataSet> dataSetList = lineData.getDataSets();
            for (int i = 0; i < dataSetList.size(); i++) {
                LineDataSet dataSet = (LineDataSet) dataSetList.get(i);
                //获取到曲线的所有在Y轴的数据集合，根据当前X轴的位置 来获取对应的Y轴值
                float y = dataSet.getValues().get((int) e.getX()).getY();
                if (i == 0) {
                    tvValue0.setText(dataSet.getLabel() + "：" + mFormat.format(y));
                }
            }
//            String temp = xAxisValueFormatter.getFormattedValue(e.getX(), null);

            //获取全部刻度，包括不显示的刻度
            String temp = dataXList.get((int) e.getX());
            if (!TextUtils.isEmpty(temp)) {
                tvDate.setText(temp);

            }

        }

    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
