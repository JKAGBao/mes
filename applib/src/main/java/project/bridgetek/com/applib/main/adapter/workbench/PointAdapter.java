package project.bridgetek.com.applib.main.adapter.workbench;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.workbench.StateActivity;
import project.bridgetek.com.applib.main.bean.workbench.Devs;
import project.bridgetek.com.bridgelib.toos.Constants;

/**
 * Created by czz on 19-4-30.
 */

public class PointAdapter extends BaseAdapter {
    private List<Devs.Dev.PointsBean> mList;
    private Context mContext;
    private String devCode;

    public PointAdapter(List<Devs.Dev.PointsBean> mList, Context mContext, String devCode) {
        this.mList = mList;
        this.mContext = mContext;
        this.devCode = devCode;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_point, null);
            viewHolder = new ViewHolder();
            viewHolder.tvPointName = view.findViewById(R.id.tv_point_name);
            viewHolder.tvUpdateTime = view.findViewById(R.id.tv_update_time);
            viewHolder.tvAcceleration = view.findViewById(R.id.tv_acceleration);
            viewHolder.tvSpeed = view.findViewById(R.id.tv_speed);
            viewHolder.tvTemperature = view.findViewById(R.id.tv_temperature);
            viewHolder.rlPoint = view.findViewById(R.id.rl_point);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final Devs.Dev.PointsBean item = getItem(position);
        viewHolder.tvPointName.setText(item.getPointName());
        viewHolder.tvUpdateTime.setText(item.getGetDate());
        viewHolder.tvAcceleration.setVisibility(View.INVISIBLE);
        viewHolder.tvSpeed.setVisibility(View.INVISIBLE);
        viewHolder.tvTemperature.setVisibility(View.INVISIBLE);
        if (item.getSignals() != null && item.getSignals().size() > 0) {
            viewHolder.rlPoint.setVisibility(View.VISIBLE);
            for (int i = 0; i < item.getSignals().size(); i++) {
                if (i == 0) {
                    viewHolder.tvAcceleration.setVisibility(View.VISIBLE);
                    setName(item.getSignals().get(i), viewHolder.tvAcceleration);
                } else if (i == 1) {
                    viewHolder.tvSpeed.setVisibility(View.VISIBLE);
                    setName(item.getSignals().get(i), viewHolder.tvSpeed);
                } else {
                    viewHolder.tvTemperature.setVisibility(View.VISIBLE);
                    setName(item.getSignals().get(i), viewHolder.tvTemperature);
                }
            }
        } else {
            viewHolder.rlPoint.setVisibility(View.INVISIBLE);
        }
        final Intent intent = new Intent(mContext, StateActivity.class);
        intent.putExtra(Constants.MARK, 4);
        intent.putExtra(Constants.ONE, item.getPointCode());
        intent.putExtra(Constants.THREE, item.getPointName());
        intent.putExtra(Constants.DEVCODE, devCode);
        viewHolder.tvAcceleration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.TWO, "1");
                mContext.startActivity(intent);
            }
        });
        viewHolder.tvSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.TWO, "2");
                mContext.startActivity(intent);
            }
        });
        viewHolder.tvTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.TWO, "16");
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    public void setName(Devs.Dev.PointsBean.SignalsBean item, TextView textView) {
        String typeName = "";
        // if (!item.getValue().equals("")) {
        if (item.getSignalTypeName().equals("1")) {
            typeName = mContext.getString(R.string.upcom_measure_tv_acceleratiom_text);
        } else if (item.getSignalTypeName().equals("2")) {
            typeName = mContext.getString(R.string.upcom_measure_tv_speed_text);
        } else if (item.getSignalTypeName().equals("16")) {
            typeName = mContext.getString(R.string.upcom_measure_tv_temperatrue);
        } else {
            typeName = mContext.getString(R.string.test_vibration_rb_signal4_text) + mContext.getString(R.string.semicolon);
        }
        //textView.setVisibility(View.VISIBLE);
        textView.setText(typeName + item.getValue() + item.getUnit());
//        } else {
//            textView.setVisibility(View.GONE);
//        }
        if (item.getState() != null) {
            if (item.getState().equals("1")) {
                textView.setTextColor(mContext.getResources().getColor(R.color.percentage));
            } else if (item.getState().equals("2")) {
                textView.setTextColor(mContext.getResources().getColor(R.color.alarm));
            } else {
                textView.setTextColor(mContext.getResources().getColor(R.color.theme));
            }
        }
    }

    class ViewHolder {
        TextView tvPointName, tvUpdateTime, tvAcceleration, tvSpeed, tvTemperature;
        RelativeLayout rlPoint;
    }
}
