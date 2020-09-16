package project.bridgetek.com.applib.main.toos;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import java.util.List;

import project.bridgetek.com.applib.main.adapter.DropDownAdapter;
import project.bridgetek.com.applib.main.bean.DropDown;

public class DropListView extends ListView {
    private DropDownAdapter adapter;
    private Context mContext;

    public DropListView(Context context) {
        super(context);
    }

    public DropListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public DropListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initUI(List<DropDown> list) {
        adapter = new DropDownAdapter(list, mContext);
        this.setAdapter(adapter);
    }

    public void change() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public String getValue() {
        return adapter.getValue();
    }

    public void setValue(String value) {
        adapter.setValue(value);
    }

    public String getStatus() {
        return adapter.getStatus();
    }

    public void setStatus(String status) {
        adapter.setStatus(status);
    }
}
