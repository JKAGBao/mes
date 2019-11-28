package com.yxst.mes.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxst.mes.R;
import com.yxst.mes.database.model.LubeItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LubeItemAdapter extends  RecyclerView.Adapter<LubeItemAdapter.ViewHolder> {
    private List<LubeItem> items;
    private Context mContext;
    Map<Integer,Boolean> map = new HashMap<>();
    private OnItemClickListener mOnItemClickListener;
    //声明接口
    public void setOnItemClickListener(LubeItemAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    public LubeItemAdapter(Context context, List<LubeItem> items){
        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_lv_lubeitem,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final LubeItem item = items.get(position);
        holder.tvLubeName.setText(item.getLubPlace());
        holder.tvFatName.setText(item.getFatName());
        holder.tvModel.setText(item.getFatModel());
        holder.tvType.setText(item.getLubTypeName());
        holder.tvReal.setText(item.getRealNum()+item.getUnit());
        holder.tvFirst.setText(item.getFirstfatNum()+item.getUnit());
        holder.tvAdd.setText(item.getPerAddNum()+item.getUnit());
//        holder.tvProgress2.setText(items.size()+"");
//        holder.tvProgress1.setText(finishItems.size()+"");
        holder.llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(holder.llAdd,item.getFatName(),item.getUnit(),item.getZoneID(),item.getLubricationItemID(),position);
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size()==0?1:items.size();
    }

    public interface OnItemClickListener {
        void onClick(View view, String fatName,String unit,Long zoneId, Long itemID,int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_lube_name) TextView tvLubeName;
     //   @BindView(R.id.tv_place_id) TextView tvPlaceId;
        @BindView(R.id.tv_fatname) TextView tvFatName;
        @BindView(R.id.tv_first) TextView tvFirst;
        @BindView(R.id.tv_model) TextView tvModel;
        @BindView(R.id.tv_type) TextView tvType;
        @BindView(R.id.tv_add) TextView tvAdd;
        @BindView(R.id.tv_real) TextView tvReal;
        @BindView(R.id.ll_add) LinearLayout llAdd;
        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
//            tvPlaceName = view.findViewById(R.id.tv_place_name);
//            tvFinish = view.findViewById(R.id.tv_finish);
//            tvProgress1 = view.findViewById(R.id.tv_progress1);
//            tvProgress2 = view.findViewById(R.id.tv_progress2);
//            tvPlaceId = view.findViewById(R.id.tv_place_id);

        }
    }
}
