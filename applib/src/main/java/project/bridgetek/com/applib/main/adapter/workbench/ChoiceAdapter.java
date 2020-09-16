package project.bridgetek.com.applib.main.adapter.workbench;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.workbench.TreeNode;

/**
 * Created by czz on 19-5-5.
 */

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ViewHolder> {
    private List<TreeNode.ChildNodesBean> mList;
    private Context mContext;
    //声明自定义的监听接口
    private OnRecyclerItemClickListener monItemClickListener;

    public ChoiceAdapter(List<TreeNode.ChildNodesBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_level, parent, false);
        ViewHolder simpleViewHolder = new ViewHolder(v);
        simpleViewHolder.setIsRecyclable(true);
        return simpleViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ChoiceAdapter.ViewHolder holder, int position) {
        TreeNode.ChildNodesBean childNodesBean = mList.get(position);
        holder.tvLevelName.setText(childNodesBean.getChildNodeName());
        if (childNodesBean.isSelection()) {
            holder.imgLevel.setVisibility(View.GONE);
            holder.tvLevelName.setTextColor(mContext.getColor(R.color.theme));
        } else {
            holder.imgLevel.setVisibility(View.VISIBLE);
            holder.tvLevelName.setTextColor(mContext.getColor(R.color.work_title));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLevelName;
        private ImageView imgLevel;

        public ViewHolder(View itemView) {
            super(itemView);
            tvLevelName = itemView.findViewById(R.id.tv_level_name);
            imgLevel = itemView.findViewById(R.id.img_level);
            //将监听传递给自定义接口
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (monItemClickListener != null) {
                        monItemClickListener.onItemClick(getAdapterPosition(), mList);
                    }
                }
            });
        }
    }

    public interface OnRecyclerItemClickListener {
        //RecyclerView的点击事件，将信息回调给view
        void onItemClick(int position, List<TreeNode.ChildNodesBean> dataBeanList);
    }

    //提供set方法供Activity或Fragment调用
    public void setRecyclerItemClickListener(OnRecyclerItemClickListener listener) {
        monItemClickListener = listener;
    }
}
