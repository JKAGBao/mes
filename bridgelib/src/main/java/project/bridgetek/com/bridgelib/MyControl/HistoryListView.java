package project.bridgetek.com.bridgelib.MyControl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by czz on 19-5-31.
 */

public class HistoryListView extends ListView {
    public HistoryListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public HistoryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public HistoryListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
