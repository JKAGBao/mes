package project.bridgetek.com.applib.main.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineDataSet.Mode;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.MessageEventWave;
import project.bridgetek.com.applib.main.toos.CurrentConfig;

public class OscFragment2
        extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LimitLine llfa = null;
    private float llfaVx = 0.0F;
    private float llfaVxs = 0.0F;
    private float llfaVy = 0.0F;
    private float llfaVys = 0.0F;
    private LimitLine llfao = null;
    private LimitLine llfb = null;
    private float llfbVx = 0.0F;
    private float llfbVxs = 0.0F;
    private float llfbVy = 0.0F;
    private float llfbVys = 0.0F;
    private LimitLine llfbo = null;
    private LimitLine llwa = null;
    private float llwaVx = 0.0F;
    private float llwaVxs = 0.0F;
    private float llwaVy = 0.0F;
    private float llwaVys = 0.0F;
    private LimitLine llwao = null;
    private LimitLine llwb = null;
    private float llwbVx = 0.0F;
    private float llwbVxs = 0.0F;
    private float llwbVy = 0.0F;
    private float llwbVys = 0.0F;
    private LimitLine llwbo = null;
    private LineChart mChartF;
    private LineChart mChartW;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    protected Typeface mTfLight;
    private CurrentConfig mUserConfig;
    private float mVmax = 0.0F;
    private float mVmin = 0.0F;
    private TextView mtvVunit;
    public static List<Float> mList = new ArrayList<>();

    private LineDataSet createSet() {
        LineDataSet localLineDataSet = new LineDataSet(null, null);
        localLineDataSet.setAxisDependency(AxisDependency.LEFT);
        localLineDataSet.setColor(-16711936);
        localLineDataSet.setCircleColor(-1);
        localLineDataSet.setLineWidth(2.0F);
        localLineDataSet.setCircleRadius(4.0F);
        localLineDataSet.setFillAlpha(65);
        localLineDataSet.setFillColor(-16711936);
        localLineDataSet.setHighLightColor(0);
        localLineDataSet.setValueTextColor(-1);
        localLineDataSet.setValueTextSize(9.0F);
        localLineDataSet.setDrawValues(false);
        return localLineDataSet;
    }

    private void initChart(LineChart paramLineChart, float paramFloat1, float paramFloat2) {
        paramLineChart.getDescription().setEnabled(false);
        paramLineChart.setTouchEnabled(true);
        paramLineChart.setDoubleTapToZoomEnabled(false);
        paramLineChart.setDragEnabled(true);
        paramLineChart.setScaleEnabled(false);
        paramLineChart.setScaleXEnabled(true);
        paramLineChart.setScaleYEnabled(false);
        paramLineChart.setPinchZoom(true);
        paramLineChart.setDrawGridBackground(false);
        paramLineChart.setBackgroundColor(getResources().getColor(R.color.white));
        paramLineChart.getDescription().setText("");
        LineData lineData = new LineData();
        lineData.setValueTextColor(getResources().getColor(R.color.darkblue));
        paramLineChart.setData(lineData);
        paramLineChart.getLegend().setEnabled(false);
        XAxis xAxis = paramLineChart.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setTextColor(getResources().getColor(R.color.darkblue));
        xAxis.setDrawGridLines(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        YAxis yAxis = paramLineChart.getAxisLeft();
        yAxis.setTypeface(mTfLight);
        yAxis.setTextColor(getResources().getColor(R.color.darkblue));
        yAxis.setAxisMaximum(paramFloat1);
        yAxis.setAxisMinimum(paramFloat2);
        yAxis.setDrawGridLines(true);
        paramLineChart.getAxisRight().setEnabled(false);
        if (paramLineChart == mChartW) {
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                public String getFormattedValue(float paramAnonymousFloat, AxisBase paramAnonymousAxisBase) {
                    return Float.toString(paramAnonymousFloat) + "ms";
                }
            });
        } else if (paramLineChart == mChartF) {
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                public String getFormattedValue(float paramAnonymousFloat, AxisBase paramAnonymousAxisBase) {
                    return Float.toString(paramAnonymousFloat) + "Hz";
                }
            });
        }
    }

    public static OscFragment2 newInstance(String paramString1, String paramString2) {
        OscFragment2 localOscFragment2 = new OscFragment2();
        Bundle localBundle = new Bundle();
        localBundle.putString("param1", paramString1);
        localBundle.putString("param2", paramString2);
        localOscFragment2.setArguments(localBundle);
        return localOscFragment2;
    }

    private void setData(LineChart paramLineChart, int paramInt, float paramFloat) {
        Object localObject = new ArrayList();
        int i = 0;
        while (i < paramInt) {
            float f1 = (float) (Math.random() * (paramFloat + 1.0F));
            float f2 = (float) Math.sin(1005.3096491487338D * i / 2560.0D);
            ((ArrayList) localObject).add(new Entry(i * 0.001F, f1 * f2 + 3.0F));
            i += 1;
        }
        LineDataSet lineDataSet = new LineDataSet((List) localObject, "");
        lineDataSet.setColor(ColorTemplate.getHoloBlue());
        lineDataSet.setHighLightColor(0);
        lineDataSet.setLineWidth(0.5F);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setMode(Mode.LINEAR);
        lineDataSet.setDrawFilled(false);
        paramLineChart.setData(new LineData(lineDataSet));
        paramLineChart.getLegend().setEnabled(false);
    }

    private void setData(LineChart paramLineChart, ArrayList<Entry> paramArrayList, float[] paramArrayOfFloat, float paramFloat1, float paramFloat2) {
        LineDataSet lineDataSet = new LineDataSet(paramArrayList, "");
        lineDataSet.setColor(getResources().getColor(R.color.darkblue));
        lineDataSet.setHighLightColor(0);
        lineDataSet.setLineWidth(1.5F);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setMode(Mode.LINEAR);
        lineDataSet.setDrawFilled(false);
        paramLineChart.setData(new LineData(new ILineDataSet[]{lineDataSet}));
        paramLineChart.getLegend().setEnabled(false);
        if (paramArrayOfFloat != null) {
            showMeasureValue(paramLineChart, paramArrayOfFloat[0]);
        }
        YAxis yAxis = paramLineChart.getAxisLeft();
        yAxis.setAxisMinimum(paramFloat1);
        yAxis.setAxisMaximum(paramFloat2);
        paramLineChart.invalidate();
    }

    private void showMeasureValue(LineChart LineChart, float value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        Description description = new Description();
        description.setText(String.valueOf(decimalFormat.format(value)));
        description.setXOffset(LineChart.getWidth() / 2);
        description.setYOffset(LineChart.getHeight() / 2);
        description.setTextSize(18.0F);
        Log.i("osc", "getHeight:" + LineChart.getHeight() + "getWidth: " + LineChart.getWidth());
        LineChart.setDescription(description);
    }

    @Override
    public void onAttach(Context paramContext) {
        super.onAttach(paramContext);
        if ((paramContext instanceof OnFragmentInteractionListener)) {
            mListener = ((OnFragmentInteractionListener) paramContext);
            Log.i("osc", "fragment2 attach");
            return;
        }
        throw new RuntimeException(paramContext.toString() + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("param1");
            mParam2 = getArguments().getString("param2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = inflater.inflate(R.layout.fragment_osc_layout2, paramViewGroup, false);
        mChartW = ((LineChart) view.findViewById(R.id.chartOscW));
        mChartF = ((LineChart) view.findViewById(R.id.chartOscF));
        mTfLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/NotoSansHans-Medium.otf");
        mtvVunit = ((TextView) view.findViewById(R.id.tv_Vunit));
        mUserConfig = ((CurrentConfig) mListener.onFragmentInteraction(0, 101, 0.0F, 0.0F, null));
        if (mUserConfig.sample_type.equals("ACC")) {
            mtvVunit.setText("m/s^2");
        } else if (mUserConfig.sample_type.equals("VEL")) {
            mtvVunit.setText("mm/s");
        }
        mChartW.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onNothingSelected() {
            }

            @Override
            public void onValueSelected(Entry paramAnonymousEntry, Highlight paramAnonymousHighlight) {
//        int i = 0;
//        if (mListener != null) {
//          i = ((Byte) mListener.onFragmentInteraction(1, 0, 0.0F, 0.0F, null)).byteValue();
//        }
//        if (!mChartF.getXAxis().getLimitLines().isEmpty())
//        {
//          llfao = llfa;
//          llfbo = llfb;
//          mListener.onFragmentInteraction(0, 3, 0.0F, 0.0F, null);
//          mChartF.getXAxis().removeAllLimitLines();
//          mChartF.invalidate();
//        }
//        if (llwao != null)
//        {
//          Log.i("osc", "osc1 re-lla");
//          mChartW.getXAxis().addLimitLine(llwao);
//          mListener.onFragmentInteraction(1, 1, llwaVxs, llwaVys, null);
//          llwao = null;
//        }
//        if (llwbo != null)
//        {
//          Log.i("osc", "osc1 re-llb");
//          mChartW.getXAxis().addLimitLine(llwbo);
//          mListener.onFragmentInteraction(1, 2, llwbVxs, llwbVys, null);
//          llwbo = null;
//        }
//        if ((i & 0x1) == 1)
//        {
//          Log.i("osc", "osc1 lla");
//          if (llwa != null) {
//            mChartW.getXAxis().removeLimitLine(llwa);
//          }
//          llwa = new LimitLine(paramAnonymousHighlight.getX(), "");
//          llwa.setLineWidth(1.0F);
//          llwa.setLineColor(-65536);
//          mChartW.getXAxis().addLimitLine(llwa);
//          mListener.onFragmentInteraction(1, 1, paramAnonymousEntry.getX(), paramAnonymousEntry.getY(), null);
//          llwaVxs = llwaVx = paramAnonymousEntry.getX();
//          llwaVys = llwaVy = paramAnonymousEntry.getY();
//        }
//        if ((i & 0x2) == 2)
//        {
//          Log.i("osc", "osc1 llb");
//          if (llwb != null) {
//            mChartW.getXAxis().removeLimitLine(llwb);
//          }
//          llwb = new LimitLine(paramAnonymousHighlight.getX(), "");
//          llwb.setLineWidth(1.0F);
//          llwb.setLineColor(-16776961);
//          mChartW.getXAxis().addLimitLine(llwb);
//          mListener.onFragmentInteraction(1, 2, paramAnonymousEntry.getX(), paramAnonymousEntry.getY(), null);
//          llwbVxs = llwbVx = paramAnonymousEntry.getX();
//          llwbVys = llwbVy = paramAnonymousEntry.getY();
//        }
//        mListener.onFragmentInteraction(1, 4, llwbVys, llwaVys, null);
//        mListener.onFragmentInteraction(1, 5, llwbVxs, llwaVxs, null);
            }
        });
        mChartF.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            public void onNothingSelected() {
            }

            public void onValueSelected(Entry paramAnonymousEntry, Highlight paramAnonymousHighlight) {
//        int i = 0;
//        if (mListener != null) {
//          i = ((Byte) mListener.onFragmentInteraction(1, 0, 0.0F, 0.0F, null)).byteValue();
//        }
//        if (!mChartW.getXAxis().getLimitLines().isEmpty()) {
//          llwao = llwa;
//          llwbo = llwb;
//          mListener.onFragmentInteraction(0, 3, 0.0F, 0.0F, null);
//          mChartW.getXAxis().removeAllLimitLines();
//          mChartW.invalidate();
//        }
//        if (llfao != null) {
//          Log.i("osc", "osc1 re-lla");
//          mChartF.getXAxis().addLimitLine(llfao);
//          mListener.onFragmentInteraction(2, 1, llfaVxs, llfaVys, null);
//          llfao = null;
//        }
//        if (llfbo != null) {
//          Log.i("osc", "osc1 re-llb");
//          mChartF.getXAxis().addLimitLine(llfbo);
//          mListener.onFragmentInteraction(2, 2, llfbVxs, llfbVys, null);
//          llfbo = null;
//        }
//        if ((i & 0x1) == 1) {
//          Log.i("osc", "osc1 lla");
//          if (llfa != null) {
//            mChartF.getXAxis().removeLimitLine(llfa);
//          }
//          llfa = new LimitLine(paramAnonymousHighlight.getX(), "");
//          llfa.setLineWidth(1.0F);
//          llfa.setLineColor(-65536);
//          mChartF.getXAxis().addLimitLine(llfa);
//          mListener.onFragmentInteraction(2, 1, paramAnonymousEntry.getX(), paramAnonymousEntry.getY(), null);
//          llfaVxs = llfaVx = paramAnonymousEntry.getX();
//          llfaVys = llfaVy = paramAnonymousEntry.getY();
//        }
//        if ((i & 0x2) == 2) {
//          Log.i("osc", "osc1 llb");
//          if (llfb != null) {
//            mChartF.getXAxis().removeLimitLine(llfb);
//          }
//          llfb = new LimitLine(paramAnonymousHighlight.getX(), "");
//          llfb.setLineWidth(1.0F);
//          llfb.setLineColor(-16776961);
//          mChartF.getXAxis().addLimitLine(llfb);
//          mListener.onFragmentInteraction(2, 2, paramAnonymousEntry.getX(), paramAnonymousEntry.getY(), null);
//          llfbVxs = llfbVx = paramAnonymousEntry.getX();
//          llfbVys = llfbVy = paramAnonymousEntry.getY();
//        }
//        mListener.onFragmentInteraction(2, 4, llfbVys, llfaVys, null);
//        mListener.onFragmentInteraction(2, 5, llfbVxs, llfaVxs, null);
            }
        });
        initChart(mChartW, 10.0F, -10.0F);
        initChart(mChartF, 10.0F, 0.0F);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.i("osc", "fragment2 detach");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventWave messageEventWave) {
        mList.clear();
        Log.i("osc", "raw msg:l=" + messageEventWave.getSampleNums());
        ArrayList waveArrayList = new ArrayList();
        ArrayList fftArrayList = new ArrayList();
        float[] arrayOfWave = new float[messageEventWave.getSampleNums()];
        float[] arrayOfFft = new float[messageEventWave.getSampleNums() / 2];
        float[] arrayOfValue = new float[12];
        if (messageEventWave.isWatch()) {
            messageEventWave.getFloat(arrayOfWave, arrayOfFft, arrayOfValue);
        } else {
            messageEventWave.getFloatWave(arrayOfWave, arrayOfFft, arrayOfValue);
        }
        mUserConfig = ((CurrentConfig) mListener.onFragmentInteraction(0, 101, 0.0F, 0.0F, null));
        if (mUserConfig.sample_type.equals("ACC")) {
            mtvVunit.setText("m/s^2");
        } else if (mUserConfig.sample_type.equals("VEL")) {
            mtvVunit.setText("mm/s");
        }
        float ms = 1000.0F / messageEventWave.getSampleRate();
        Log.i("osc", "ffts.length " + arrayOfWave.length + " ms:" + ms);
        for (int i = 0; i < arrayOfWave.length; i++) {
            mList.add(arrayOfWave[i]);
            waveArrayList.add(new Entry(i * ms, arrayOfWave[i]));
        }
        float hz = 1.0F * messageEventWave.getSampleRate() / messageEventWave.getSampleNums();
        Log.i("osc", "ffts.length " + arrayOfFft.length + " Hz:" + hz);
        int fftLengh = (int) (arrayOfFft.length / 1.28F + 1.0F);
        for (int i = 0; i < fftLengh; i++) {
            fftArrayList.add(new Entry(i * hz, arrayOfFft[i]));
        }
        float max = arrayOfValue[0];
        Log.i("osc", " " + Float.toString(max));
//        if (max <= 0.0001F) {
//            mVmax = 0.0001F;
//        } else if ((max > 0.0001F) && (max <= 0.001F)) {
//            mVmax = 0.001F;
//        } else if ((max > 0.0F) && (max <= 0.01F)) {
//            mVmax = 0.01F;
//        } else if ((max > 0.0F) && (max <= 0.1F)) {
//            mVmax = 0.1F;
//        } else if ((max > 0.0F) && (max <= 1.0F)) {
//            mVmax = 1.0F;
//        } else if ((max > 1.0F) && (max <= 5.0F)) {
//            mVmax = 5.0F;
//        } else if ((max > 5.0F) && (max <= 10.0F)) {
//            mVmax = 10.0F;
//        } else if ((max > 10.0F) && (max <= 20.0F)) {
//            mVmax = 20.0F;
//        } else if ((max > 20.0F) && (max <= 50.0F)) {
//            mVmax = 50.0F;
//        } else if ((max > 50.0F) && (max <= 100.0F)) {
//            mVmax = 100.0F;
//        } else {
//            mVmax = Math.round(max + 5);
//        }
        mVmax = (float) (max * 1.43);

        Log.i("WQWEWE", "onMessageEvent: " + waveArrayList);
        setData(mChartW, waveArrayList, arrayOfValue, -mVmax, mVmax);
        setData(mChartF, fftArrayList, null, 0.0F, (float) (arrayOfValue[1] * 1.43));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("osc", "fragment2 resume");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.i("osc", "fragment2 start");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.i("osc", "fragment2 stop");
    }

    public interface OnFragmentInteractionListener {
        Object onFragmentInteraction(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, Object paramObject);
    }
}
