package project.bridgetek.com.applib.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.Exception.StateDelegate;
import project.bridgetek.com.applib.main.Test.TestDelegate;
import project.bridgetek.com.applib.main.activity.AppActivity;
import project.bridgetek.com.applib.main.activity.MyActivity;
import project.bridgetek.com.applib.main.activity.SetupActivity;
import project.bridgetek.com.applib.main.index.TaskDelegate;
import project.bridgetek.com.applib.main.toos.ClientManager;
import project.bridgetek.com.bridgelib.delegates.bottom.BaseBottomDelegate;
import project.bridgetek.com.bridgelib.delegates.bottom.BottomItemDelegate;
import project.bridgetek.com.bridgelib.delegates.bottom.BottomTabBean;
import project.bridgetek.com.bridgelib.delegates.bottom.ItemBuilder;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

public class MyBottomDelegate extends BaseBottomDelegate implements View.OnKeyListener {
    private long mExitTime = 0;
    private static final int EXIT_TIME = 2000;
    private LinearLayout mIcSet, mIcMessage;
    private TextView mTvInspect;
    LocalUserInfo mUserInfo;
    private String mAccountid;
    boolean mError = false;
    private ImageView mImgTab;
    private List<String> permissions = new ArrayList<>();

    // fragment的bug，想要实现onKey的回调，需要这样做
    @Override
    public void onResume() {
        super.onResume();
        View rootView = getView();
        if (rootView != null) {
            rootView.setFocusableInTouchMode(true);
            rootView.requestFocus();
            rootView.setOnKeyListener(this);
        }
        InitUI(rootView);
        boolean opened = ClientManager.getClient().isBluetoothOpened();
        if (!opened) {
            ClientManager.getClient().openBluetooth();
        }
    }

    public void InitUI(View view) {
        mIcSet = view.findViewById(R.id.ll_set);
        mIcMessage = view.findViewById(R.id.ll_message);
        mTvInspect = view.findViewById(R.id.tv_inspect);
        mImgTab = view.findViewById(R.id.img_tab);
        mTvInspect.setTypeface(HiApplication.MEDIUM);
        mIcSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(_mActivity, SetupActivity.class), 1);
            }
        });
        mIcMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(_mActivity, MyActivity.class));
            }
        });
        String info = mUserInfo.getUserInfo(Constants.SENSORTIME);
        String info1 = mUserInfo.getUserInfo(Constants.SERVICETIME);
        if (TextUtils.isEmpty(info) || info.equals("")) {
            if (TextUtils.isEmpty(info1) || info1.equals("")) {
                mImgTab.setVisibility(View.GONE);
            } else {
                mImgTab.setVisibility(View.VISIBLE);
            }
        } else {
            mImgTab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mUserInfo = LocalUserInfo.getInstance(getContext());
        permissions = mUserInfo.getDataList(Constants.PERMISSIONS);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountid = mUserInfo.getUserInfo(Constants.ACCOUNTID);
        if (TextUtils.isEmpty(mAccountid) || mAccountid.equals("")) {
            mError = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 3) {
            boolean ceshi = data.getBooleanExtra(Constants.KEY_ID, false);
            if (ceshi) {
                startActivity(new Intent(_mActivity, AppActivity.class));
                _mActivity.finish();
            }
        }
    }

    @Override
    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder itemBuilder) {
        boolean isFreePatrol = false;
        for (int i = 0; i < permissions.size(); i++) {
            if (Constants.ONLINE.equals(permissions.get(i))) {
                isFreePatrol = true;
            }
        }
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = new LinkedHashMap<>();
        items.put(new BottomTabBean(CountString.ICON_TASK, getString(R.string.app_mybottom_item_task_text)), new TaskDelegate());
        items.put(new BottomTabBean(CountString.ICON_TEST, getString(R.string.app_mybottom_item_test_text)), new TestDelegate());
        //items.put(new BottomTabBean(CountString.ICON_ABNORMAL, getString(R.string.app_mybottom_item_abnormal_text)), new ExcDelegate());
        if (isFreePatrol) {
            items.put(new BottomTabBean(CountString.ICON_ABNORMAL, getString(R.string.app_mybottom_item_excdelegate_text)), new StateDelegate());
        }
        return itemBuilder.addItems(items).build();
    }

    @Override
    public int setIndexDelegate() {
        return 0;
    }

    @Override
    public int setClickColor() {
        return Color.parseColor(CountString.BOTTOMCOLOR);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mError) {
                if ((System.currentTimeMillis() - mExitTime) > EXIT_TIME) {
                    Toast.makeText(getContext(), R.string.app_mybottom_toast_signout_text, Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    _mActivity.finish();
                    if (mExitTime != 0) {
                        mExitTime = 0;
                    }
                }
                return true;
            } else {
                if (mCurrentDelegate != 0) {
                    resetColor();
                    RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(0);
                    final ImageView itemIcon = (ImageView) item.getChildAt(0);
                    Resources resources = getActivity().getResources();
                    Drawable task = resources.getDrawable(project.bridgetek.com.bridgelib.R.drawable.ic_task);
                    itemIcon.setBackground(task);
                    final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
                    itemTitle.setTextColor(Constants.CLICKCOLOR);
                    showHideFragment(ITEM_DELEGATES.get(0), ITEM_DELEGATES.get(mCurrentDelegate));
                    mCurrentDelegate = 0;
                    return true;
                } else {
                    if ((System.currentTimeMillis() - mExitTime) > EXIT_TIME) {
                        Toast.makeText(getContext(), R.string.app_mybottom_toast_signout_text, Toast.LENGTH_SHORT).show();
                        mExitTime = System.currentTimeMillis();
                    } else {
                        _mActivity.finish();
                        if (mExitTime != 0) {
                            mExitTime = 0;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
