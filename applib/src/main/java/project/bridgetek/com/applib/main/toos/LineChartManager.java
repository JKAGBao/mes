package project.bridgetek.com.applib.main.toos;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import project.bridgetek.com.applib.main.bean.workbench.Trend;

public class LineChartManager {
    private LineChart lineChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private LineData lineData;
    private LineDataSet lineDataSet;
    List<String> dataXList = new LinkedList<>();
    protected int mDecimalDigits;
    protected DecimalFormat mFormat;

    //一条曲线
    public LineChartManager(LineChart mLineChart, String unit, int digits) {
        this.lineChart = mLineChart;
        leftAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();

        setup(digits);
        initLineChart(unit);
    }

    public void setup(int digits) {
        StringBuffer b = new StringBuffer();

        for (int i = 0; i < digits; ++i) {
            if (i == 0) {
                b.append(".");
            }

            b.append("0");
        }

        this.mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    /**
     * 初始化LineChar
     */
    private void initLineChart(final String unit) {
        lineChart.setDrawGridBackground(false);
        //显示边界
        lineChart.setDrawBorders(true);
        //折线图例 标签 设置
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        //设置图表距离上下左右的距离
        lineChart.setExtraOffsets(0, 0, 0, 10);

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGridLineWidth(2f);
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String strTemp = mFormat.format(value) + " " + unit;
                return strTemp;
            }
        });

        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        //将右边那条线隐藏
        lineChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // 让x轴在下面
        lineChart.getXAxis().setGridColor(0x00ffffff);

        // 设置右边的label不可用
        rightAxis.setDrawLabels(false);
        // 设置右边的线不可用
        rightAxis.setDrawGridLines(false);
        // 设置右边的线不可用
        rightAxis.setDrawAxisLine(false);
    }

    /**
     * 初始化折线(一条线)
     */
    public void initLineDataSet(final List<Float> dataList, String name, int color, int digits) {

        float value = Collections.max(dataList);

        this.mDecimalDigits = digits;
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            Entry entry = new Entry(i, dataList.get(i));
            entries.add(entry);
        }
        // 每一个LineDataSet代表一条线
        lineDataSet = new LineDataSet(entries, name);

        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setCircleRadius(1.5f);
        lineDataSet.setColor(color);
        //lineDataSet.setCircleColor(color);
        lineDataSet.setFillColor(color);
        lineDataSet.setHighLightColor(Color.BLACK);
        setDescription("");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //设置曲线填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        //设置参数
        lineData = new LineData(lineDataSet);
        //设置显示数据的多少位 很难找到这个方法
        lineData.setValueFormatter(new DefaultValueFormatter(digits));
        lineDataSet.setDrawCircles(false);
        lineChart.setBackgroundColor(Color.WHITE);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否显示值
        lineData.setDrawValues(false);
        lineChart.setData(lineData);
        xAxis.setLabelCount(4, true);
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        lineChart.zoom(1f, 1f, 0, 0);
        lineChart.setScaleMinima(1f, 1f);
        lineChart.setVisibleXRangeMaximum(dataList.size());
        //移到某个位置
        lineChart.moveViewToX(lineData.getEntryCount());
        lineChart.invalidate();
    }


    /**
     * 设置Y轴值
     */
    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, false);
        lineChart.invalidate();
    }

    /**
     * 设置X轴值
     */
    public void setXAxis(final List<Trend.TrendDatasBean> dataList) {

        for (Trend.TrendDatasBean data : dataList) {
            String tradeDate = data.getF_GetDate();
            dataXList.add(tradeDate);
        }

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String tradeDate = dataXList.get((int) value % dataXList.size());
                tradeDate = tradeDate.substring(5, tradeDate.length());
//                if (tradeDate.length() >= 10) {
//                    String temp = tradeDate.substring(tradeDate.length() - 2, tradeDate.length());
//                    if (temp.equals("00") || temp.equals("24")) {
//                        tradeDate = tradeDate.substring(tradeDate.length() - 6, tradeDate.length() - 4) + "-" +
//                                tradeDate.substring(tradeDate.length() - 4, tradeDate.length() - 2);
//
//                    } else {
//                        tradeDate = temp.replaceFirst("^0*", "");
//                        switch (tradeDate) {
//                            case "3":
//                            case "6":
//                            case "9":
//                            case "12":
//                            case "15":
//                            case "18":
//                            case "21":
//                                break;
//                            default:
//                                tradeDate = "";
//                                break;
//                        }
//                    }
//                }
                return tradeDate;
            }
        });

        lineChart.invalidate();
    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    public void setMarkerView(Context context) {
        LineChartMarkView mv = new LineChartMarkView(context, xAxis.getValueFormatter(), dataXList, mDecimalDigits);
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        lineChart.invalidate();
    }

    /**
     * 设置描述信息
     *
     * @param str
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        description.setTextSize(14);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }
}
