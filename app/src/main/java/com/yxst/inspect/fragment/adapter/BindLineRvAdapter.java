package com.yxst.inspect.fragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;
import com.yxst.inspect.database.model.Line;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * 排班安排界面数据的填充的Adapter
 */
public class BindLineRvAdapter extends RecyclerView.Adapter<BindLineRvAdapter.BindViewHolder>{
    private OnClickListener mOnClickListener;
    public void setOnClickListener(OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;

    }
    private final List<Line> LINES ;
    public BindLineRvAdapter(List<Line> lines){
        this.LINES = lines;
    }
    @NonNull
    @Override
    public BindViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_rv_bindline,viewGroup,false);
        return new BindViewHolder(view);
    }
    Map<Integer,Boolean> map = new HashMap<>();
    @Override
    public void onBindViewHolder(@NonNull final BindViewHolder sViewHolder, int position) {
        Line line = LINES.get(position);
        sViewHolder.tvName.setText(line.getLineName());
        sViewHolder.tvModel.setText(line.getInspectionTypeName());
        sViewHolder.tvRFID.setText(line.getRFID());
        final Long lineId = line.getID();
        if(line.getRFID()!=null && !"".equals(line.getRFID())){
            map.put(position,true);
        }
        if(map.containsKey(position)){
            sViewHolder.btnObtain.setText("更改RF卡");
        }else{
            sViewHolder.btnObtain.setText("绑定RF卡");
        }
//        if(line.getRFID()!=null && !"".equals(line.getRFID())){
//            sViewHolder.btnObtain.setText("更改NFC卡");
//
//        }else{
//            sViewHolder.btnObtain.setText("绑定NFC卡");
//        }

        sViewHolder.llBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null){
                    int position = sViewHolder.getLayoutPosition();
                    mOnClickListener.onItemClick(sViewHolder.llBind,sViewHolder.btnObtain,lineId,position);
                }
            }
        });
        sViewHolder.btnObtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null){
                    int position = sViewHolder.getLayoutPosition();
                    mOnClickListener.onClick(sViewHolder.llBind,sViewHolder.btnObtain,lineId,position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return LINES.size();
    }

    class BindViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_bind_name) TextView tvName;
        @BindView(R.id.btn_bind_obtain) Button btnObtain;
        @BindView((R.id.ll_bind)) LinearLayout llBind;
        @BindView(R.id.tv_model) TextView tvModel;
        @BindView(R.id.tv_rfid) TextView tvRFID;
        public BindViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnClickListener{
        public void onClick(View view, View btn, Long lineId, int Position);
        public void onItemClick(View view, View btn, Long lineId, int Position);
    }
}

