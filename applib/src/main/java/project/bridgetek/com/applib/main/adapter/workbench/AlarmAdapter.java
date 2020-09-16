package project.bridgetek.com.applib.main.adapter.workbench;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.OfflineReason;
import project.bridgetek.com.applib.main.bean.workbench.Devdetail;
import project.bridgetek.com.applib.main.toos.DropEditText;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * Created by czz on 19-4-23.
 */

public class AlarmAdapter extends BaseAdapter {
    private Activity mContext;
    private List<Devdetail.CurWarningEntitysBean> mList;
    private int mMark;
    private ProgressDialog mDialog;

    public AlarmAdapter(Activity mContent, List<Devdetail.CurWarningEntitysBean> mList, int mark) {
        this.mContext = mContent;
        this.mList = mList;
        this.mMark = mark;
        mDialog = Storage.getPro(mContent, mContent.getString(R.string.upcoming_builder_title_text));
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Devdetail.CurWarningEntitysBean getItem(int position) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_alarm, null);
            viewHolder = new ViewHolder();
            viewHolder.tvMessage = view.findViewById(R.id.tv_message);
            viewHolder.tvChange = view.findViewById(R.id.tv_change);
            viewHolder.tvHappen = view.findViewById(R.id.tv_happen);
            viewHolder.tvReason = view.findViewById(R.id.tv_reason);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final Devdetail.CurWarningEntitysBean item = getItem(position);
        viewHolder.tvMessage.setText(mContext.getString(R.string.device_details_alarm_tv_message_text) + mContext.getString(R.string.semicolon) + item.getWarningContent());
        viewHolder.tvHappen.setText(mContext.getString(R.string.device_details_alarm_tv_happen_text) + mContext.getString(R.string.semicolon) + item.getWarningBegTime());
        viewHolder.tvReason.setText(mContext.getString(R.string.device_details_alarm_tv_noneed_text) + mContext.getString(R.string.semicolon) + item.getReasonContent());
        if (mMark == 1) {
            viewHolder.tvChange.setVisibility(View.GONE);
        } else {
            viewHolder.tvChange.setVisibility(View.VISIBLE);
        }
        viewHolder.tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDia(item, mContext);
            }
        });
        return view;
    }

    public void getOffReasonItem(final DropEditText tvTaskName, final Activity context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("EnCode", "OfflineReasons");
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.OFFREASONITEM, json);
                    final String data = loadDataFromWeb.getResult();
                    final List<String> list = JSONArray.parseArray(data, String.class);
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!list.isEmpty()) {
                                String[] strings = new String[list.size()];
                                for (int i = 0; i < list.size(); i++) {
                                    strings[i] = list.get(i);
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, strings);
                                tvTaskName.setAdapter(adapter);
                            }
                        }
                    });
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
        }).start();
    }

    public void setDia(final Devdetail.CurWarningEntitysBean curWarningEntitysBean, final Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_alarm, null, false);
        final Dialog builder = new Dialog(context, R.style.update_dialog);
        builder.setCancelable(false);
        builder.setCanceledOnTouchOutside(false);
        final DropEditText tvTaskName = view.findViewById(R.id.tv_taskName);
        getOffReasonItem(tvTaskName, context);
        Button btPreservation = view.findViewById(R.id.bt_preservation);
        Button btComplete = view.findViewById(R.id.bt_complete);
        btComplete.setTypeface(HiApplication.MEDIUM);
        btPreservation.setTypeface(HiApplication.MEDIUM);
        btPreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOfflineReason(curWarningEntitysBean, tvTaskName.getText().toString(), context, builder);
            }
        });
        btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.setContentView(view);
        builder.show();
    }

    public void setOfflineReason(final Devdetail.CurWarningEntitysBean curWarningEntitysBean, final String OfflineReason, final Activity context, final Dialog builder) {
        mDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Id", curWarningEntitysBean.getId());
                    jsonObject.put("OfflineReason", OfflineReason);
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.OFFLINEREASON, json);
                    final String data = loadDataFromWeb.getResult();
                    final OfflineReason offlineReason = JSONObject.parseObject(data, OfflineReason.class);
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            if ("success".equals(offlineReason.getResult())) {
                                curWarningEntitysBean.setReasonContent(OfflineReason);
                                notifyDataSetChanged();
                            }
                            builder.dismiss();
                        }
                    });
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
        }).start();
    }

    class ViewHolder {
        TextView tvMessage, tvChange, tvHappen, tvReason;
    }
}
