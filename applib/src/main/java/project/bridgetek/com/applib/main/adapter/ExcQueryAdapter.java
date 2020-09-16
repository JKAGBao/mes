package project.bridgetek.com.applib.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.ReException;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;

/**
 * Created by Cong Zhizhong on 18-7-30.
 */

public class ExcQueryAdapter extends BaseAdapter {
    Context mContext;
    List<ReException> mList;

    public ExcQueryAdapter(Context context, List<ReException> list) {
        mContext = context;
        mList = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_excquery, null);
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
        ReException item = getItem(position);
        String t = item.getFound_TM().substring(0, item.getFound_TM().indexOf("T"));
        viewHolder.mTvUser.setText(item.getMobjectName());
        viewHolder.mTvTime.setText(t);
        viewHolder.mTvInspect.setText(item.getExceptionTitle());
        if (item.getStatus() != null) {
            if (item.getStatus().equals(Constants.ONE)) {
                viewHolder.mTvMObjectStatus.setText(R.string.excdelegate_fragment_adapter_holder_one_text);
            } else if (item.getStatus().equals(Constants.TWO)) {
                viewHolder.mTvMObjectStatus.setText(R.string.excdelegate_fragment_adapter_holder_two_text);
            } else if (item.getStatus().equals(Constants.THREE)) {
                viewHolder.mTvMObjectStatus.setText(R.string.excdelegate_fragment_adapter_holder_three_text);
            } else {
                viewHolder.mTvMObjectStatus.setText(R.string.excdelegate_fragment_adapter_holder_four_textfour);
            }
        }
        viewHolder.mImgRefer.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvUser.setTypeface(HiApplication.BOLD);
        viewHolder.mTvInspect.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvTime.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvMObjectStatus.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvMessage.setTypeface(HiApplication.REGULAR);
        return view;
    }

    class ViewHolder {
        TextView mTvUser, mTvInspect, mTvTime, mTvMObjectStatus;
        TextView mTvMessage;
        TextView mImgRefer;
    }
}
