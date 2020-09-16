package project.bridgetek.com.bridgelib.MyControl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import project.bridgetek.com.bridgelib.R;

public class PullupListView extends ListView implements AbsListView.OnScrollListener {
    private View footview;
    private int totaItemCounts;
    private int lasstVisible;
    private int fistVisiable;
    private PullupListView.LoadListener loadListener;
    private int footViewHeight;
    private int headViewHeight;
    private int yload;
    boolean isLoading;
    private TextView updateTime;
    private boolean up = false;
    private boolean refresh = false;

    public PullupListView(Context context) {
        super(context);
        initView(context);
    }

    public PullupListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullupListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        //拿到头布局xml

        //updateTime.setText("更新于：" + new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(System.currentTimeMillis()));
        //拿到尾布局xml
        footview = LayoutInflater.from(context).inflate(R.layout.footer_layout, null);
        //测量footview的高度
        footview.measure(0, 0);
        //拿到高度
        footViewHeight = footview.getMeasuredHeight();
        //隐藏view
        footview.setPadding(0, -footViewHeight, 0, 0);
        //设置不可见
        // footview.findViewById(R.id.foot_load).setVisibility(View.GONE);
        // headview.findViewById(R.id.head).setVisibility(View.GONE);
        //添加到listview底部
        this.addFooterView(footview);
        //设置拉动监听
        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                yload = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                int i = moveY - yload;
                if (i > 0) {
                    up = false;
                    refresh = false;
                    if (fistVisiable == 0) {
                    }
                    break;
                } else {
                    up = true;
                }

//            case MotionEvent.ACTION_UP:
//                headview.setPadding(0,0,0,0);
//                updateInfo.setText("正在刷新。。。");
//                progressBar.setVisibility(View.VISIBLE);
//                loadListener.pullLoad();
//
//                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (up) {
            if (totaItemCounts == lasstVisible && scrollState == SCROLL_STATE_IDLE) {
                if (!isLoading) {
                    isLoading = true;
                    // footview.findViewById(R.id.foot_load).setVisibility(View.VISIBLE);
                    footview.setPadding(0, 0, 0, 0);
                    //加载数据
                    loadListener.onLoad();
                }
            }
        } else {
            if (refresh) {
                if (fistVisiable == 0) {
//                    loadListener.pullLoad();
                } else {
                    loadComplete();
                }
            } else {
                loadComplete();
            }
        }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.fistVisiable = firstVisibleItem;
        this.lasstVisible = firstVisibleItem + visibleItemCount;
        this.totaItemCounts = totalItemCount;
    }

    //加载完成
    public void loadComplete() {
        isLoading = false;
        //footview.findViewById(R.id.foot_load).setVisibility(View.GONE);
        footview.setPadding(0, -footViewHeight, 0, 0);
    }

    public void setInterface(PullupListView.LoadListener loadListener) {
        this.loadListener = loadListener;
    }

    //接口回调
    public interface LoadListener {
        void onLoad();

    }
}
