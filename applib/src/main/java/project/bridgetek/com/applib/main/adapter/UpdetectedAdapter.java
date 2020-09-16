package project.bridgetek.com.applib.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;

/**
 * Created by Cong Zhizhong on 18-7-3.
 */

public class UpdetectedAdapter extends BaseAdapter {
    private List<CheckItemInfo> mList;
    private Context mContext;
    private BlackDao mBlackDao;
    private String mResultId;

    public UpdetectedAdapter(List<CheckItemInfo> list, Context context, BlackDao blackDao, String ID) {
        this.mList = list;
        this.mContext = context;
        this.mBlackDao = blackDao;
        this.mResultId = ID;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CheckItemInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_watch, null);
            viewHolder = new ViewHolder();
            viewHolder.mLlCount = view.findViewById(R.id.ll_count);
            viewHolder.mTvCheckItemDesc = view.findViewById(R.id.tv_CheckItemDesc);
            viewHolder.mTvESTStandard = view.findViewById(R.id.tv_ESTStandard);
            viewHolder.mTvResult = view.findViewById(R.id.tv_result);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CheckItemInfo item = getItem(position);
        List<CheckItem> accurate = mBlackDao.getCheckAccurate(item.getCheckItemID(), mResultId);
        if (accurate.size() > 0) {
            if (accurate.get(0).getException_YN().equals("1")) {
                viewHolder.mTvResult.setText(mContext.getResources().getString(R.string.upcom_watch_adapter_tv_result_text));
                viewHolder.mTvResult.setTextColor(mContext.getResources().getColor(R.color.test_history_shape));
            } else {
                viewHolder.mTvResult.setText(mContext.getResources().getString(R.string.upcom_watch_tvresult_text));
                viewHolder.mTvResult.setTextColor(mContext.getResources().getColor(R.color.work_title));
            }
        } else {
            viewHolder.mTvResult.setText(mContext.getResources().getString(R.string.upcom_watch_tvresult_text));
            viewHolder.mTvResult.setTextColor(mContext.getResources().getColor(R.color.work_title));
        }
        viewHolder.mLlCount.setVisibility(View.GONE);
        viewHolder.mTvCheckItemDesc.setText(mContext.getString(R.string.task_over_abnoradapter_tvcheckitemdesc_text) + item.getCheckItemDesc());
        viewHolder.mTvESTStandard.setText(mContext.getString(R.string.task_over_abnoradapter_tveststandard_text) + item.getESTStandard());
        return view;
    }

    class ViewHolder {
        LinearLayout mLlCount;
        TextView mTvCheckItemDesc, mTvESTStandard;
        TextView mTvResult;
    }
}
