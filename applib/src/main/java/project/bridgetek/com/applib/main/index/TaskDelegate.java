package project.bridgetek.com.applib.main.index;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.index.child.ViewPagerFragment;
import project.bridgetek.com.bridgelib.delegates.bottom.BottomItemDelegate;

// 主页
public class TaskDelegate extends BottomItemDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_task;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (findChildFragment(ViewPagerFragment.class) == null) {
            loadRootFragment(R.id.fl_index_container, ViewPagerFragment.newInstance());
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        // 这里可以不用懒加载,因为Adapter的场景下,Adapter内的子Fragment只有在父Fragment是show状态时,才会被Attach,Create
    }
}
