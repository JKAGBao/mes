package project.bridgetek.com.applib.main.Exception;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.AbnorDetailsActivity;
import project.bridgetek.com.applib.main.activity.ExcQueryActivity;
import project.bridgetek.com.applib.main.adapter.ExcDelegateAdapter;
import project.bridgetek.com.applib.main.bean.ReException;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.NetworkUtil;
import project.bridgetek.com.bridgelib.delegates.bottom.BottomItemDelegate;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

// 异常fragment
public class ExcDelegate extends BottomItemDelegate {
    private FloatingActionButton mFabRight;
    private ListView mLvAbnonrmal;
    private ExcDelegateAdapter mExcDelegateAdapter;
    private List<ReException> mList = new ArrayList<>();
    private List<ReException> mSearchList = new ArrayList<>();
    private BlackDao mBlackDao;
    private LocalUserInfo mLocalUserInfo;
    private String mAccountid;
    private ImageView mImgSearch;
    private TextView mEtSearch;
    private LinearLayout mLlConnect;
    private ImageView mImgLoad;

    @Override
    public Object setLayout() {
        return R.layout.delegate_exception;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initUI(rootView);
        setOnclick();
    }

    public void initUI(View view) {
        mFabRight = view.findViewById(R.id.fab_right);
        mLvAbnonrmal = view.findViewById(R.id.lv_abnonrmal);
        mImgSearch = view.findViewById(R.id.img_search);
        mEtSearch = view.findViewById(R.id.et_search);
        mLlConnect = view.findViewById(R.id.ll_connect);
        mImgLoad = view.findViewById(R.id.img_load);
        mImgLoad.setVisibility(View.GONE);
        mBlackDao = BlackDao.getInstance(_mActivity);
        mLocalUserInfo = LocalUserInfo.getInstance(_mActivity);
        mAccountid = mLocalUserInfo.getUserInfo(Constants.ACCOUNTID);
    }

    public void setOnclick() {
        mFabRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(_mActivity, AbnormalActivity.class);
//                intent.putExtra(Constants.ISFIRST, true);
//                startActivityForResult(intent, 1);
            }
        });
        mLlConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean netWorkConnected = NetworkUtil.isNetWorkConnected(_mActivity);
                if (netWorkConnected) {
                    startActivity(new Intent(_mActivity, ExcQueryActivity.class));
                } else {
                    Toast.makeText(_mActivity, R.string.mactivity_excdelegate_toast_connect_text, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mLvAbnonrmal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(_mActivity, AbnorDetailsActivity.class);
                intent.putExtra(Constants.REEXCEPTION, mExcDelegateAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        mList = mBlackDao.getReException(mAccountid);
        if (mList.size() < 1) {
            mLvAbnonrmal.setVisibility(View.GONE);
            mImgLoad.setVisibility(View.VISIBLE);
        } else {
            mLvAbnonrmal.setVisibility(View.VISIBLE);
            mImgLoad.setVisibility(View.GONE);
        }
        mExcDelegateAdapter = new ExcDelegateAdapter(mList, _mActivity, mBlackDao);
        mLvAbnonrmal.setAdapter(mExcDelegateAdapter);
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
//            mList = mBlackDao.getReException(mAccountid);
//            mExcDelegateAdapter = new ExcDelegateAdapter(mList, _mActivity, mBlackDao);
//            mLvAbnonrmal.setAdapter(mExcDelegateAdapter);
        }
    }
}
