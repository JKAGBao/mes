package project.bridgetek.com.applib.main.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.adapter.AbnorFraAdapter;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbnormalFragment extends Fragment {
    private String mTaskID, mLineID;
    private ListView mLvAbnonrmal;
    private BlackDao mBlackDao;
    private List<CheckItemInfo> mList;
    private AbnorFraAdapter mAbnorFraAdapter;
    private LocalUserInfo mLocalUserInfo;
    private ImageView mImgLoad;

    public static AbnormalFragment newInstance(String TaskID, String LineID, List<CheckItemInfo> list) {
        Bundle args = new Bundle();
        args.putString(Constants.TASKID, TaskID);
        args.putString(Constants.LINEID, LineID);
        args.putSerializable(Constants.WATCH, (Serializable) list);
        AbnormalFragment fragment = new AbnormalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskID = getArguments().getString(Constants.TASKID);
        mLineID = getArguments().getString(Constants.LINEID);
        mLocalUserInfo = LocalUserInfo.getInstance(getContext());
        mList = (List<CheckItemInfo>) getArguments().getSerializable(Constants.WATCH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_abnormal, container, false);
        initUI(view);
        return view;
    }

    public void initUI(View view) {
        mLvAbnonrmal = view.findViewById(R.id.lv_abnonrmal);
        mImgLoad = view.findViewById(R.id.img_load);
        mBlackDao = BlackDao.getInstance(getContext());
        //mList = mBlackDao.getCheckItemInfo(mLineID);
        if (mList.size() > 0) {
            mImgLoad.setVisibility(View.GONE);
        } else {
            mImgLoad.setVisibility(View.VISIBLE);
        }
        String info = mLocalUserInfo.getUserInfo(Constants.CYCLETIME);
        mAbnorFraAdapter = new AbnorFraAdapter(mList, getContext(), mBlackDao, info);
        mLvAbnonrmal.setAdapter(mAbnorFraAdapter);
    }
}
