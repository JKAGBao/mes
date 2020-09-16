package com.yxst.inspect.nfc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxst.inspect.R;
import com.yxst.inspect.database.model.Item;

import java.util.List;

public class NFCItemAdapter extends BaseAdapter {
    private List<Item> items;
    private Context context;
    private boolean isGone;
    public NFCItemAdapter(Context context,List<Item> items){
        this.context = context;
        this.items = items;

    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Item item = items.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_lv_nfcinspect,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //1.待巡检 2 漏检 3 已巡检
        holder.tvItemName.setText(item.getCheckContent());
        holder.tvCheckType.setText(item.getCheckType()+"");
        holder.tvItemId.setText(item.getItemID()+"");
        holder.tvLineId.setText(item.getLineID()+"");
        holder.tvCheckValue.setText(item.getCheckValue());
//        holder.tvCheckValue.setText(item.getCheckValue()==null?"":item.getCheckValue());

        return convertView;
    }
    private class ViewHolder{
        TextView tvItemName;
        TextView tvItemId;
        TextView tvLineId;
        TextView tvCheckType;
        TextView tvCheckValue;
        LinearLayout linearLayout;
        ViewHolder(View view){
            linearLayout = view.findViewById(R.id.ll_adapter);
            tvItemName = view.findViewById(R.id.tv_item_name);
            tvItemId = view.findViewById(R.id.tv_item_id);
            tvLineId = view.findViewById(R.id.tv_line);
            tvCheckType = view.findViewById(R.id.tv_item_checktype);
            tvCheckValue = view.findViewById(R.id.tv_itme_value);
        }
    }
}
