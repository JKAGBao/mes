package project.bridgetek.com.bridgelib.delegates.bottom;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;
import project.bridgetek.com.bridgelib.R;
import project.bridgetek.com.bridgelib.R2;
import project.bridgetek.com.bridgelib.delegates.BlackDelegate;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

// 有底部导航栏的delegate基类
public abstract class BaseBottomDelegate extends BlackDelegate implements View.OnClickListener {
    // 存入底部导航的按钮和对应的Delegate页面
    private final ArrayList<BottomTabBean> TAB_BEANS = new ArrayList<>();
    public final ArrayList<BottomItemDelegate> ITEM_DELEGATES = new ArrayList<>();

    // 将底部的导航按钮和界面组装在一个Map集合中
    private final LinkedHashMap<BottomTabBean, BottomItemDelegate> ITEMS = new LinkedHashMap<>();
    LocalUserInfo mUserInfo;
    private String mAccountid;
    boolean mError = false;
    // 当前显示的delegate页面下标
    public int mCurrentDelegate = 0;

    // 第一次加载显示的主页下标
    private int mIndexDelegate = 0;

    // 点击之后按钮显示的颜色
    private int mClickColor = Color.RED;

    // 让子类传入布局所需要的按钮和布局
    public abstract LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder itemBuilder);

    // 让子类传入设置首次加载的主页
    public abstract int setIndexDelegate();

    // 让子类传入设置点击之后按钮的颜色
    public abstract int setClickColor();

    @Override
    public Object setLayout() {
        return R.layout.delegate_bottom;
    }

    @BindView(R2.id.bottom_bar)
    public LinearLayoutCompat mBottomBar = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndexDelegate = setIndexDelegate();
        if (setClickColor() != 0) {
            mClickColor = setClickColor();
        }

        final ItemBuilder builder = ItemBuilder.builder();
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = setItems(builder);

        ITEMS.putAll(items);
        // for 循环取出ITEMS中的键值对的值
        for (Map.Entry<BottomTabBean, BottomItemDelegate> item : ITEMS.entrySet()) {
            final BottomTabBean key = item.getKey();
            final BottomItemDelegate value = item.getValue();
            TAB_BEANS.add(key);
            ITEM_DELEGATES.add(value);
        }
        mUserInfo = LocalUserInfo.getInstance(getContext());
        mAccountid = mUserInfo.getUserInfo(Constants.ACCOUNTID);
        if (TextUtils.isEmpty(mAccountid) || mAccountid.equals("")) {
            mError = true;
            mCurrentDelegate = 1;
            mIndexDelegate = 1;
        }
    }

    @Override
    public void onBindView(Bundle savedInstanceState, View rootView) {
        final int size = ITEMS.size();
        // for循环填充布局
        for (int i = 0; i < size; i++) {
            LayoutInflater.from(getContext()).inflate(R.layout.bottom_item_icon_text_layout, mBottomBar);
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            //设置每个item的点击事件
            item.setTag(i);
            item.setOnClickListener(this);
            final ImageView itemIcon = (ImageView) item.getChildAt(0);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
            final BottomTabBean bean = TAB_BEANS.get(i);
            Resources resources = getActivity().getResources();
            Drawable task = resources.getDrawable(R.drawable.ic_task);
            Drawable test = resources.getDrawable(R.drawable.ic_test);
            Drawable abormal = resources.getDrawable(R.drawable.ic_abnormal);
            Drawable testgray = resources.getDrawable(R.drawable.ic_test_gray);
            Drawable abormalgray = resources.getDrawable(R.drawable.ic_abnormal_gray);
            itemTitle.setTypeface(HiApplication.MEDIUM);
            if (i == 0) {
                itemIcon.setBackground(task);
            } else if (i == 1) {
                itemIcon.setBackground(test);
            } else {
                itemIcon.setBackground(abormal);
            }

            itemTitle.setText(bean.getTITLE());
            if (i == mIndexDelegate) {
                itemIcon.setBackground(task);
                itemTitle.setTextColor(Constants.CLICKCOLOR);
            } else {
                if (i == 1) {
                    itemIcon.setBackground(testgray);
                } else {
                    itemIcon.setBackground(abormalgray);
                }
                itemTitle.setTextColor(Constants.COLOR);
            }
        }
        // 将delegates的ArrayList转化为SupportFragment的数组，框架需要
        final SupportFragment[] delegateArray = ITEM_DELEGATES.toArray(new SupportFragment[size]);
        loadMultipleRootFragment(R.id.bottom_bar_delegate_container, mIndexDelegate, delegateArray);
    }

    // 重置按钮的颜色
    public void resetColor() {
        final int count = mBottomBar.getChildCount();
        for (int i = 0; i < count; i++) {
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            final ImageView itemIcon = (ImageView) item.getChildAt(0);
            Resources resources = getActivity().getResources();
            Drawable taskgray = resources.getDrawable(R.drawable.ic_task_gray);
            Drawable testgray = resources.getDrawable(R.drawable.ic_test_gray);
            Drawable abormalgray = resources.getDrawable(R.drawable.ic_abnormal_gray);
            if (i == 0) {
                itemIcon.setBackground(taskgray);
            } else if (i == 1) {
                itemIcon.setBackground(testgray);
            } else {
                itemIcon.setBackground(abormalgray);
            }
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
            itemTitle.setTextColor(Constants.COLOR);
        }
    }

    // 点击之后重新导入delegate，设置按钮的颜色
    @Override
    public void onClick(View view) {
        final int tag = (int) view.getTag();
        resetColor();
        final RelativeLayout item = (RelativeLayout) view;
        final ImageView itemIcon = (ImageView) item.getChildAt(0);
        Resources resources = getActivity().getResources();
        Drawable task = resources.getDrawable(R.drawable.ic_task);
        Drawable test = resources.getDrawable(R.drawable.ic_test);
        Drawable abormal = resources.getDrawable(R.drawable.ic_abnormal);
        if (!mError){
            if (tag == 0) {
                itemIcon.setBackground(task);
            } else if (tag == 1) {
                itemIcon.setBackground(test);
            } else {
                itemIcon.setBackground(abormal);
            }
        }else {
            if (tag == 0) {
                Toast.makeText(getContext(), "未登录，功能不可用", Toast.LENGTH_SHORT).show();
                itemIcon.setBackground(task);
            } else if (tag == 1) {
                itemIcon.setBackground(test);
            }else {
                itemIcon.setBackground(abormal);
                Toast.makeText(getContext(), "未登录，功能不可用", Toast.LENGTH_SHORT).show();
            }
        }
        final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
        itemTitle.setTextColor(Constants.CLICKCOLOR);
        if (!mError) {
            showHideFragment(ITEM_DELEGATES.get(tag), ITEM_DELEGATES.get(mCurrentDelegate));
            mCurrentDelegate = tag;
        }
    }
}
