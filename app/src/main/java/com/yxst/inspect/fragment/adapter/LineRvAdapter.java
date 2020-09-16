package com.yxst.inspect.fragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;
import com.yxst.inspect.database.model.InspectLine;
import com.yxst.inspect.util.TimeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * 排班安排界面数据的填充的Adapter
 */
public class LineRvAdapter extends RecyclerView.Adapter<LineRvAdapter.BindViewHolder>{
    private OnClickListener mOnClickListener;
    public void setOnClickListener(OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;

    }
    private final List<InspectLine> LINES ;
    public LineRvAdapter(List<InspectLine> lines){
        this.LINES = lines;
    }
    @NonNull
    @Override
    public BindViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_rv_line,viewGroup,false);
        return new BindViewHolder(view);
    }
    Map<Integer,Boolean> map = new HashMap<>();
    @Override
    public void onBindViewHolder(@NonNull final BindViewHolder sViewHolder, final int position) {
        final InspectLine line = LINES.get(position);
        sViewHolder.tvName.setText(line.getLineName());
        sViewHolder.tvModel.setText(line.getInspectionTypeName());
        sViewHolder.tvStart.setText(TimeUtil.dateNoYearFormat(line.getBeginTime()));
        sViewHolder.tvEnd.setText(TimeUtil.timeFormat(line.getEndTime()));
        final Long lineId = line.getLineID();
        sViewHolder.imMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onItemClick(sViewHolder.llBind,sViewHolder.imMsg,lineId,position);
            }
        });
        sViewHolder.llBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null){
                    int position = sViewHolder.getLayoutPosition();
                    mOnClickListener.onClick(sViewHolder.llBind,sViewHolder.tvName,lineId,line.getLineName(),position);
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
//        @BindView(R.id.btn_bind_obtain) Button btnObtain;
        @BindView((R.id.ll_bind)) LinearLayout llBind;
        @BindView(R.id.tv_model) TextView tvModel;
        @BindView(R.id.tv_start) TextView tvStart;
        @BindView(R.id.tv_end) TextView tvEnd;
        @BindView(R.id.iv_msg) ImageView imMsg;

        public BindViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnClickListener{
        public void onClick(View view, View btn, Long lineId,String name, int Position);
        public void onItemClick(View view, View btn, Long lineId, int Position);
    }
}

