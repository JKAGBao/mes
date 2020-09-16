package project.bridgetek.com.applib.main.toos;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Cong Zhizhong on 18-9-5.
 */

public class NoScrollListView extends ListView {
    boolean isStart = true;

    public NoScrollListView(Context context) {
        super(context);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public NoScrollListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }

    //        @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            // 当手指触摸listview时，让父控件交出ontouch权限,不能滚动
//            case MotionEvent.ACTION_DOWN:
//                setParentScrollAble(false);
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                // 当手指松开时，让父控件重新获取onTouch权限
//                setParentScrollAble(true);
//                break;
//
//        }
//        return super.onInterceptTouchEvent(ev);
//
//    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() == 0) {//到头部了
            getParent().requestDisallowInterceptTouchEvent(true);//放行
        } else {
            getParent().requestDisallowInterceptTouchEvent(!isStart);
            isStart = !isStart;
            //拦截
        }
        return super.onInterceptTouchEvent(ev);
    }

    // 设置父控件是否可以获取到触摸处理权限
    private void setParentScrollAble(boolean flag) {
        getParent().requestDisallowInterceptTouchEvent(!flag);
    }
}
