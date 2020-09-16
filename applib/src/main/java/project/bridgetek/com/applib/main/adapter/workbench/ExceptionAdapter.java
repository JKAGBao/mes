package project.bridgetek.com.applib.main.adapter.workbench;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.workbench.StateActivity;
import project.bridgetek.com.applib.main.bean.workbench.Devs;
import project.bridgetek.com.applib.main.toos.Mapping;
import project.bridgetek.com.bridgelib.MyControl.DG_ListView;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * Created by czz on 19-4-16.
 */

public class ExceptionAdapter extends BaseAdapter {
    private List<Devs.Dev> mList;
    private Context mContext;
    Mapping mapping;

    public ExceptionAdapter(List<Devs.Dev> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        mapping = new Mapping();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Devs.Dev getItem(int position) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_exception, null);
            viewHolder = new ViewHolder();
            viewHolder.tvDeviceName = view.findViewById(R.id.tv_device_name);
            viewHolder.tvDeviceCode = view.findViewById(R.id.tv_device_code);
            viewHolder.tvCompany = view.findViewById(R.id.tv_company);
            viewHolder.mImgCollect = view.findViewById(R.id.img_collect);
            viewHolder.mLvPoint = view.findViewById(R.id.lv_point);
            viewHolder.tvState = view.findViewById(R.id.tv_state);
            viewHolder.imgTrend = view.findViewById(R.id.img_trend);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final Devs.Dev item = getItem(position);

        viewHolder.mImgCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = Constants.API + Constants.ADDATTENTION;
                        if (item.getIsAttention().equals("1")) {
                            url = Constants.API + Constants.REMOVEATTENTION;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("accoundId", Constants.CURRENTUSER);
                            jsonObject.put("devCodes", new ArrayList<String>() {{
                                add(item.getDevCode());
                            }});
                            String json = jsonObject.toString();
                            LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(url, json);
                            final String data = loadDataFromWeb.getResult();
                            if (data.equals("true")) {
                                new Handler(mContext.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (item.getIsAttention().equals("1")) {
                                            item.setIsAttention("0");
                                            Toast.makeText(mContext, R.string.device_detail_no_attention_text, Toast.LENGTH_SHORT).show();
                                        } else {
                                            item.setIsAttention("1");
                                            Toast.makeText(mContext, R.string.device_detail_yes_attention_text, Toast.LENGTH_SHORT).show();
                                        }
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            Logger.e(e);
                        }
                    }
                }).start();
            }
        });
        viewHolder.imgTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StateActivity.class);
                intent.putExtra(Constants.MARK, 3);
                intent.putExtra(Constants.ONE, item.getDevCode());
                mContext.startActivity(intent);
            }
        });
        viewHolder.tvDeviceName.setText(mContext.getString(R.string.check_label_tv_text) + mContext.getString(R.string.semicolon) + mapping.getValue(item.getDevName()));
        viewHolder.tvDeviceCode.setText(mContext.getString(R.string.delegate_exception_tv_devicecode_text) + mContext.getString(R.string.semicolon) + mapping.getValue(item.getDevCode()));
        viewHolder.tvCompany.setText(mContext.getString(R.string.delegate_exception_tv_company_text) + mContext.getString(R.string.semicolon) + mapping.getValue(item.getOrgName()));
        if (item.getDevStatus().equals("1")) {
            viewHolder.tvState.setBackgroundResource(R.drawable.shape_round_textview_blue);
        } else if (item.getDevStatus().equals("0")) {
            viewHolder.tvState.setBackgroundResource(R.drawable.shape_round_textview_gray);
        } else {
            viewHolder.tvState.setBackgroundResource(R.drawable.shape_round_textview_red);
        }
        if (item.getIsAttention().equals("1")) {
            viewHolder.mImgCollect.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_collect));
        } else {
            viewHolder.mImgCollect.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_nocollect));
        }
        setPoint(viewHolder.mLvPoint, item.getPoints(), item.getDevCode());
        return view;
    }

    public void setPoint(ListView listView, List<Devs.Dev.PointsBean> list, String devCode) {
        PointAdapter pointAdapter = new PointAdapter(list, mContext, devCode);
        listView.setAdapter(pointAdapter);
    }

    class ViewHolder {
        TextView tvDeviceName, tvDeviceCode, tvCompany;
        TextView tvState;
        ImageView mImgCollect;
        DG_ListView mLvPoint;
        ImageView imgTrend;
    }
}
