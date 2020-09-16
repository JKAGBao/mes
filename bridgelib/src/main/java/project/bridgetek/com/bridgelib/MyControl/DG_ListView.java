package project.bridgetek.com.bridgelib.MyControl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by czz on 19-4-30.
 */

public class DG_ListView extends ListView {
    public DG_ListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public DG_ListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public DG_ListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        return false;
//    }
}
