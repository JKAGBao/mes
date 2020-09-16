package project.bridgetek.com.bridgelib.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ContentFrameLayout;
import project.bridgetek.com.bridgelib.R;
import project.bridgetek.com.bridgelib.delegates.BlackDelegate;
import me.yokeyword.fragmentation.SupportActivity;

// 单例Activity的基类
public abstract class ProxyActivity extends SupportActivity{
    public abstract BlackDelegate setRootDelegate();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContainer(savedInstanceState);
    }

    private void initContainer(@Nullable Bundle saveInstanceState){
        @SuppressLint("RestrictedApi")
        final ContentFrameLayout container = new ContentFrameLayout(this);
        container.setId(R.id.delegate_container);
        setContentView(container);
        if (saveInstanceState==null){
            // Fragmentation提供的绑定Fragment到Activity的方法
            loadRootFragment(R.id.delegate_container, setRootDelegate());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        System.runFinalization();
    }
}
