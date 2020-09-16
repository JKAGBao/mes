package project.bridgetek.com.applib.main.adapter.workbench;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.workbench.TreeNode;

/**
 * Created by czz on 19-5-6.
 */

public class ChoiceDeviceAdapter extends BaseAdapter {
    private List<TreeNode.ChildNodesBean> mList;
    private Context mContext;
    private String mIndicate;

    public ChoiceDeviceAdapter(List<TreeNode.ChildNodesBean> mList, Context mContext, String indicate) {
        this.mList = mList;
        this.mContext = mContext;
        this.mIndicate = indicate;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TreeNode.ChildNodesBean getItem(int position) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_choice_device, null);
            viewHolder = new ViewHolder();
            viewHolder.tvDeviceCode = view.findViewById(R.id.tv_device_code);
            viewHolder.cbDevice = view.findViewById(R.id.cb_device);
            viewHolder.imgDevice = view.findViewById(R.id.img_device);
            viewHolder.llDevice = view.findViewById(R.id.ll_device);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        TreeNode.ChildNodesBean item = getItem(position);
        viewHolder.llDevice.setVisibility(View.GONE);
        if (item.getChildNodeType().equals("MObject")) {
            viewHolder.imgDevice.setVisibility(View.GONE);
            if (mIndicate.equals("1")) {
                viewHolder.cbDevice.setVisibility(View.VISIBLE);
            } else {
                viewHolder.cbDevice.setVisibility(View.GONE);
            }
        } else {
            viewHolder.imgDevice.setVisibility(View.VISIBLE);
            viewHolder.cbDevice.setVisibility(View.GONE);
        }
        viewHolder.tvDeviceCode.setText(item.getChildNodeName());
        if (item.isSelection()) {
            viewHolder.cbDevice.setChecked(true);
        } else {
            viewHolder.cbDevice.setChecked(false);
        }
        return view;
    }

    class ViewHolder {
        TextView tvDeviceCode;
        LinearLayout llDevice;
        CheckBox cbDevice;
        ImageView imgDevice;
    }
}
