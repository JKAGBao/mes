package com.yxst.inspect.nfc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.R;
import com.yxst.inspect.database.model.Item;
import com.yxst.inspect.database.Manager.ItemQueryUtil;
import com.yxst.inspect.database.model.Place;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NFCPlaceAdapter extends BaseAdapter {
    private List<Place> places;
    private Context mContext;
    int i=0;
    Map<Integer,Boolean> map = new HashMap<>();
    public NFCPlaceAdapter(Context context, List<Place> places){
        this.mContext = context;
        this.places = places;
    }
    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_lv_place,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Place place = places.get(position);
        List<Item> items = ItemQueryUtil.getItemByPlaceId(mContext,place.getPlaceID(),place.getLineID());
        List<Item> finishItems = ItemQueryUtil.getItemByGEStatus(mContext,place.getPlaceID(),place.getLineID(), ConfigInfo.CHECKED_STATUS);
    //    List<Item> finishItems = ItemQueryUtil.getItemByStatus(mContext,place.getPlaceID(),place.getLineID());
        //一个部位下 完成的Item。

        //1.待巡检 2 漏检 3 已巡检
        holder.tvPlaceName.setText(place.getPlaceName());
        holder.tvPlaceId.setText(place.getPlaceID()+"");
        holder.tvProgress2.setText(items.size()+"");
        holder.tvProgress1.setText(finishItems.size()+"");
        if(items.size()!=0 &&finishItems.size()!=0 && items.size() == finishItems.size() ){
            map.put(position,true);
        }
        if(map.containsKey(position)){
            holder.tvFinish.setVisibility(View.VISIBLE);
        }else{
            holder.tvFinish.setVisibility(View.GONE);
        }
        return convertView;
    }
    private class ViewHolder{
        TextView tvPlaceName;
        TextView tvPlaceId;
        TextView tvFinish;
        TextView tvProgress1;
        TextView tvProgress2;
        ViewHolder(View view){
            tvPlaceName = view.findViewById(R.id.tv_place_name);
            tvFinish = view.findViewById(R.id.tv_finish);
            tvProgress1 = view.findViewById(R.id.tv_progress1);
            tvProgress2 = view.findViewById(R.id.tv_progress2);
            tvPlaceId = view.findViewById(R.id.tv_place_id);

        }
    }
}
