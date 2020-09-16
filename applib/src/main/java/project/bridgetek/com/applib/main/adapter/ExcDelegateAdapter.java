package project.bridgetek.com.applib.main.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.ReException;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.NetworkUtil;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.DeleteFileUtil;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * Created by Cong Zhizhong on 18-7-5.
 */

public class ExcDelegateAdapter extends BaseAdapter {
    private List<ReException> mList;
    private Context mContext;
    private BlackDao mBlackDao;
    private int mInt;
    ProgressDialog mProgressDialog;
    List<ResultFileInfo> mResuList = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                delResultFile(mResuList, getItem(mInt));
                mList.remove(getItem(mInt));
                notifyDataSetChanged();
                mProgressDialog.dismiss();
            } else if (msg.what == 2) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, R.string.app_context_tost_result_text, Toast.LENGTH_SHORT).show();
            }
        }
    };

    public ExcDelegateAdapter(List<ReException> mList, Context mContext, BlackDao blackDao) {
        this.mList = mList;
        this.mContext = mContext;
        this.mBlackDao = blackDao;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ReException getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_rexception, null);
            viewHolder = new ViewHolder();
            viewHolder.mTvUser = view.findViewById(R.id.tv_User);
            viewHolder.mTvTime = view.findViewById(R.id.tv_time);
            viewHolder.mTvInspect = view.findViewById(R.id.tv_inspect);
            viewHolder.mTvMObjectStatus = view.findViewById(R.id.tv_MObjectStatus);
            viewHolder.mTvMessage = view.findViewById(R.id.tv_message);
            viewHolder.mImgRefer = view.findViewById(R.id.img_refer);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final ReException item = getItem(position);
//        Devices mDeviceName = mBlackDao.getDeviceName(item.getMobjectCode());
        viewHolder.mTvUser.setText(item.getMobjectName());
        viewHolder.mTvTime.setText(item.getFound_TM());
        viewHolder.mTvInspect.setText(item.getExceptionTitle());
        if (item.getStatus().equals(Constants.ONE)) {
            viewHolder.mTvMObjectStatus.setText(R.string.excdelegate_fragment_adapter_holder_one_text);
        } else if (item.getStatus().equals(Constants.TWO)) {
            viewHolder.mTvMObjectStatus.setText(R.string.excdelegate_fragment_adapter_holder_two_text);
        } else if (item.getStatus().equals(Constants.THREE)) {
            viewHolder.mTvMObjectStatus.setText(R.string.excdelegate_fragment_adapter_holder_three_text);
        } else {
            viewHolder.mTvMObjectStatus.setText(R.string.excdelegate_fragment_adapter_holder_four_textfour);
        }
        viewHolder.mTvUser.setTypeface(HiApplication.BOLD);
        viewHolder.mTvInspect.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvTime.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvMObjectStatus.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvMessage.setTypeface(HiApplication.REGULAR);
        viewHolder.mImgRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean netWorkConnected = NetworkUtil.isNetWorkConnected(mContext);
                if (netWorkConnected) {
                    mProgressDialog = Storage.getProgress(mContext);
                    mProgressDialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put(Constants.REEXCEPTION, item);
                                JSONObject target = jsonObject.getJSONObject(Constants.REEXCEPTION);
                                mResuList = mBlackDao.getAbnorFile(item.getException_ID());
                                List<ResultFileInfo> infos = Storage.setBase64(mResuList);
                                target.put(Constants.RESULTFILE, infos);
                                String string = target.toString();
                                LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.RESULT_EXCEPTION, string);
                                String result = loadDataFromWeb.getupload();
                                if (result.equals("true")) {
                                    mInt = position;
                                    handler.obtainMessage(0).sendToTarget();
                                } else {
                                    handler.obtainMessage(2).sendToTarget();
                                }
                            } catch (Exception e) {
                                Logger.e("onClick: " + e);
                                handler.obtainMessage(2).sendToTarget();
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(mContext, R.string.mactivity_excdelegate_toast_connect_text, Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    public void delResultFile(List<ResultFileInfo> list, ReException item) {
        if (list.size() > 0) {
            mBlackDao.delResultFile(list.get(0).getException_ID());
            for (int i = 0; i < list.size(); i++) {
                DeleteFileUtil.deleteFile(list.get(i).getCheckItem_ID(), mContext);
            }
        }
        mBlackDao.delReException(item.getException_ID());
    }


    class ViewHolder {
        TextView mTvUser, mTvInspect, mTvTime, mTvMObjectStatus;
        TextView mTvMessage;
        ImageView mImgRefer;
    }
}
