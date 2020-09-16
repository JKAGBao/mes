package project.bridgetek.com.applib.main.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.bean.TaskInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.NetworkUtil;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * Created by Cong Zhizhong on 18-7-2.
 */

public class OverAdapter extends BaseAdapter {
    private Context mContext;
    private List<TaskInfo> mList;
    private String mStarTime, mEndTime, mOverTime;
    private BlackDao mBlackDao;
    private boolean upload = true;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                notifyDataSetChanged();
            } else if (msg.what == 5) {
                mProgress.setProgress(progress);
                mTvSpeed.setText(progress + "%");
                mTvProgress.setText(allcount + "/" + count);
            } else if (msg.what == 4) {
                dialog.dismiss();
                if (upload) {
                    getDialog(mContext, true, "数据上传成功！", "提交了" + taskcount + mContext.getString(R.string.upcoming_num_text) + "点检结果,");
                } else {
                    getDialog(mContext, false, "数据上传失败！", "提交了" + count + mContext.getString(R.string.upcoming_num_text) + "点检结果," + "有" + (count - taskcount) + mContext.getString(R.string.upcoming_num_text) + "点检结果，" + "未完成提交！");
                }
                //    Toast.makeText(mContext, mContext.getString(R.string.upcoming_upload_text) + allcount + mContext.getString(R.string.upcoming_num_text) + mContext.getString(R.string.upcoming_notupload_text) + (count - allcount) + mContext.getString(R.string.upcoming_num_text), Toast.LENGTH_SHORT).show();
            }
        }
    };
    AlertDialog dialog;
    private ProgressBar mProgress;
    private TextView mTvSpeed, mTvProgress;
    int count, allcount, taskcount;
    private int progress;

    public OverAdapter(Context mContext, List<TaskInfo> mList, BlackDao mBlackDao) {
        this.mContext = mContext;
        this.mList = mList;
        this.mBlackDao = mBlackDao;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TaskInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_over, null);
            viewHolder = new ViewHolder();
            viewHolder.mTvOvertime = view.findViewById(R.id.tv_overtime);
            viewHolder.mTvPercentage = view.findViewById(R.id.tv_percentage);
            viewHolder.mTvPreservation = view.findViewById(R.id.tv_preservation);
            viewHolder.mTvWorkname = view.findViewById(R.id.tv_workname);
            viewHolder.mTvTime = view.findViewById(R.id.tv_time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final TaskInfo item = getItem(position);
        try {
            mStarTime = TimeType.getDate(item.getTaskPlanStartTime());
            mEndTime = TimeType.stringToDate(item.getTaskPlanEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int l0001 = mBlackDao.getLineNumCount(item.getTaskID());
        int t001 = mBlackDao.getTaskCheckCount(item.getTaskID());
        int overUpload = mBlackDao.getOverUpload(item.getTaskID());
        if (overUpload > 0) {
            viewHolder.mTvWorkname.setTextColor(mContext.getResources().getColor(R.color.over_tips));
            viewHolder.mTvPercentage.setTextColor(mContext.getResources().getColor(R.color.over_tips));
        } else {
            viewHolder.mTvWorkname.setTextColor(mContext.getResources().getColor(R.color.work_title));
            viewHolder.mTvPercentage.setTextColor(mContext.getResources().getColor(R.color.percentage));
        }
        final Resources resources = mContext.getResources();
        Drawable task = resources.getDrawable(R.drawable.ic_refer_min);
        Drawable test = resources.getDrawable(R.drawable.ic_refer_min_gray);
        if (item.isSubmit()) {
            viewHolder.mTvPreservation.setBackground(test);
        } else {
            viewHolder.mTvPreservation.setBackground(task);
        }
        final List<CheckItem> item1 = mBlackDao.getCheckItem(item.getTaskID());
        viewHolder.mTvPreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean netWorkConnected = NetworkUtil.isNetWorkConnected(mContext);
                upload = true;
                if (netWorkConnected) {
                    showProgressBar(item1.size(), mContext);
                    if (!item.isSubmit()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (item1.size() > 0) {
                                    try {
                                        boolean isSucceed = true;
                                        for (int i = 0; i < item1.size(); i++) {
                                            CheckItem checkItem = item1.get(i);
                                            if (!checkItem.isSubmit()) {
                                                JSONObject jsonObject = new JSONObject();
                                                jsonObject.put(Constants.CHECKINFO, checkItem);
                                                JSONObject target = jsonObject.getJSONObject(Constants.CHECKINFO);
                                                List<ResultFileInfo> mList = new ArrayList<>();
                                                mList = mBlackDao.getCheckFile(item1.get(i).getResult_ID());
                                                List<ResultFileInfo> infos = Storage.setBase64(mList);
                                                target.put(Constants.RESULTFILE, infos);
                                                if (checkItem.getExceptionID().equals("1")) {
                                                    List<Float> chart = mBlackDao.getChart(checkItem.getResult_ID());
                                                    target.put(Constants.WAVEDATA, chart);
                                                }
                                                String string = target.toString();
                                                LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.CHECKITEM, string);
                                                String result = loadDataFromWeb.getupload();
                                                if (!result.equals("true")) {
                                                    isSucceed = false;
                                                    upload = false;
                                                    allcount++;
                                                } else {
                                                    allcount++;
                                                    taskcount++;
                                                    progress = (int) (((float) allcount / count) * 100);
                                                    mBlackDao.delTastChart(checkItem.getResult_ID());
                                                    mBlackDao.setCheckSubmit(checkItem.getResult_ID());
                                                    mBlackDao.delTastResultFile(checkItem.getResult_ID());
                                                }
                                                handler.obtainMessage(5).sendToTarget();
                                            }
                                        }
                                        if (isSucceed) {
                                            mBlackDao.setTaskSubmit(item.getTaskID());
                                            item.setSubmit(true);
                                        }
                                        handler.obtainMessage(0).sendToTarget();
                                        handler.obtainMessage(4).sendToTarget();
                                    } catch (Exception e) {
                                        upload = false;
                                        Logger.e("run: " + e);
                                        handler.obtainMessage(4).sendToTarget();
                                    }
                                } else {
                                    mBlackDao.setTaskSubmit(item.getTaskID());
                                    item.setSubmit(true);
                                    handler.obtainMessage(4).sendToTarget();
                                }
                            }
                        }).start();
                    } else {
                        handler.obtainMessage(4).sendToTarget();
                    }
                } else {
                    Toast.makeText(mContext, R.string.mactivity_excdelegate_toast_connect_text, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.mTvWorkname.setText(item.getLineName());
        viewHolder.mTvTime.setText(mStarTime + CountString.CONNECTOR + mEndTime);
        if (item1.size() > 0) {
            try {
                mOverTime = TimeType.stringToDate(item1.get(0).getComplete_TM());
            } catch (Exception e) {

            }
            viewHolder.mTvOvertime.setText(mOverTime);
        } else {
            viewHolder.mTvOvertime.setText(CountString.UNKNOWN);
        }
        viewHolder.mTvPercentage.setText(CountString.LEFT_BRACKETS + t001 + CountString.SEMICOLON + l0001 + CountString.RIGHT_BRACKETS);
        viewHolder.mTvWorkname.setTypeface(HiApplication.BOLD);
        viewHolder.mTvTime.setTypeface(HiApplication.BOLD);
        viewHolder.mTvOvertime.setTypeface(HiApplication.BOLD);
        viewHolder.mTvPercentage.setTypeface(HiApplication.BOLD);
        return view;
    }

    /**
     * 上传进度对话框
     */
    public void showProgressBar(int count, Context context) {
        this.count = count;
        allcount = 0;
        taskcount = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(R.string.upcoming_builder_title_text);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.progressbar, null);
        mProgress = view.findViewById(R.id.progress);
        mTvSpeed = view.findViewById(R.id.tv_speed);
        mTvProgress = view.findViewById(R.id.tv_progress);
        progress = (int) (((float) allcount / count) * 100);
        mProgress.setProgress(progress);
        mTvSpeed.setText(progress + "%");
        mTvProgress.setText(allcount + "/" + count);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    class ViewHolder {
        TextView mTvWorkname, mTvPercentage, mTvTime, mTvOvertime;
        ImageView mTvPreservation;
    }

    public void getDialog(Context context, boolean message, String msg, String count) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_upload, null, false);
        final Dialog builder = new Dialog(context, R.style.update_dialog);
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(true);
        TextView tvTaskName = view.findViewById(R.id.tv_taskName);
        Button btPreservation = view.findViewById(R.id.bt_preservation);
        ImageView imgMessage = view.findViewById(R.id.img_message);
        TextView tvUpload = view.findViewById(R.id.tv_upload);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        if (message) {
            imgMessage.setBackgroundResource(R.drawable.ic_success);
            tvUpload.setText("成功");
        } else {
            imgMessage.setBackgroundResource(R.drawable.ic_fail);
            tvUpload.setText("失败");
        }
        btPreservation.setTypeface(HiApplication.MEDIUM);
        tvTaskName.setText(msg);
        tvMessage.setText(count);
        btPreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        builder.setContentView(view);
        builder.show();
    }
}
