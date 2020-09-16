package project.bridgetek.com.applib.main.activity.workbench;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.fragment.workbench.DeviceDetailsFragment;
import project.bridgetek.com.applib.main.fragment.workbench.DeviceHistoryFragment;
import project.bridgetek.com.applib.main.fragment.workbench.DeviceSpotFragment;
import project.bridgetek.com.applib.main.fragment.workbench.MoreDeviceFragment;
import project.bridgetek.com.bridgelib.toos.Constants;

public class StateActivity extends AppCompatActivity {
    private DeviceDetailsFragment mDetailsFragment;
    private MoreDeviceFragment mMoreDeviceFragment;
    int mark;
    FragmentManager manager;
    FragmentTransaction transaction;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
    }

    public void initUI() {
        mark = getIntent().getIntExtra(Constants.MARK, 0);
        switch (mark) {
            case 1:
                String stringExtra = getIntent().getStringExtra(Constants.DEVCODE);
                mDetailsFragment = DeviceDetailsFragment.newInstance(stringExtra);
                setFragment(mDetailsFragment, "detail");
                break;
            case 2:
                mMoreDeviceFragment = MoreDeviceFragment.newInstance();
                setFragment(mMoreDeviceFragment, "more");
                break;
            case 3:
                String stringExtra1 = getIntent().getStringExtra(Constants.ONE);
                setFragment(DeviceHistoryFragment.newInstance(stringExtra1), "history");
                break;
            case 4:
                String stringExtra2 = getIntent().getStringExtra(Constants.ONE);
                String stringExtra3 = getIntent().getStringExtra(Constants.TWO);
                String stringExtra4 = getIntent().getStringExtra(Constants.DEVCODE);
                String pointName = getIntent().getStringExtra(Constants.THREE);
                setFragment(DeviceSpotFragment.newInstance(stringExtra2, stringExtra3, stringExtra4, pointName), "device_spot");
                break;
        }
    }

    public void setFragment(Fragment fragment, String fTag) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.state_count, fragment, fTag);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mDetailsFragment = null;
    }

    public void addFragment(String Tag, Fragment fragment, String fTag) {
        currentFragment = manager.findFragmentByTag(Tag);
        // 如果这个fragment不存于栈中
        //初始化Fragment事物
        transaction = manager.beginTransaction();
        //在添加之前先将上一个Fragment隐藏掉
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        transaction.add(R.id.state_count, fragment, fTag);
        transaction.addToBackStack(fTag);
        transaction.commit();
    }
}
