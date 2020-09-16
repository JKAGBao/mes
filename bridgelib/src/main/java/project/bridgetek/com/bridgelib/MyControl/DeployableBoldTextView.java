package project.bridgetek.com.bridgelib.MyControl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import project.bridgetek.com.bridgelib.R;
import project.bridgetek.com.bridgelib.toos.HiApplication;


/**
 * Created by czz on 19-4-25.
 */

public class DeployableBoldTextView extends AppCompatTextView {
    private Drawable mDrawable; // 显示的图
    private int mDropDrawableResId; // 下拉图标
    private int mRiseDrawableResID; // 上拉图标
    private int mork = 0;

    public DeployableBoldTextView(Context context) {
        super(context);
        init(context);
    }

    public DeployableBoldTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeface(HiApplication.BOLD);
        setIncludeFontPadding(false);
        init(context);
    }

    public DeployableBoldTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mDropDrawableResId = R.drawable.btn_down_arrow; // 设置下拉图标
        mRiseDrawableResID = R.drawable.btn_upper_arrow; // 设置上拉图标
    }

    private void showDropDrawable() {
        mork = 0;
        mDrawable = getResources().getDrawable(mDropDrawableResId);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mDrawable, getCompoundDrawables()[3]);
        setCompoundDrawablePadding(10);
    }

    private void showRiseDrawable() {
        mork = 1;
        mDrawable = getResources().getDrawable(mRiseDrawableResID);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mDrawable, getCompoundDrawables()[3]);
        setCompoundDrawablePadding(10);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        post(new Runnable() {

            @Override
            public void run() {
                if (getLineCount() == 1) {
                    hideDropDrawable();
                } else {
                    showRiseDrawable();
                    setMaxLines(Integer.MAX_VALUE);
                }
            }
        });
    }

    private void hideDropDrawable() {
        setCompoundDrawables(null, null, null, null);
    }

    /**
     * 我们无法直接给EditText设置点击事件，只能通过按下的位置来模拟点击事件
     * 当我们按下的位置在图标包括图标到控件右边的间距范围内均算有效
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCompoundDrawables()[2] != null) {
//                int start = getWidth() - getTotalPaddingRight() + getPaddingRight()-40; // 起始位置
//                int end = getWidth(); // 结束位置
//                boolean available = (event.getX() > start) && (event.getX() < end);
//                if (available) {
                if (mork == 1) {
                    showDropDrawable();
                    setMaxLines(1);
                } else {
                    showRiseDrawable();
                    setMaxLines(Integer.MAX_VALUE);
                }
                return true;
            }
            // }
        }
        return super.onTouchEvent(event);
    }

}
