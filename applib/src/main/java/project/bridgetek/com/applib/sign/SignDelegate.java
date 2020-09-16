package project.bridgetek.com.applib.sign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import butterknife.Optional;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.R2;
import project.bridgetek.com.applib.main.MyBottomDelegate;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.bean.Login;
import project.bridgetek.com.applib.main.bean.TaskInfo;
import project.bridgetek.com.applib.main.toos.BackGroup;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.NetworkUtil;
import project.bridgetek.com.applib.main.toos.NfcDao;
import project.bridgetek.com.bridgelib.delegates.BlackDelegate;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

//
public class SignDelegate extends BlackDelegate {
    private boolean mIsvisual;
    private static final String LOGIN_MODE = "login_mode";
    private EditText mEtLoginAccount, mEtLoginPsw;
    public static EditText mEtLoginId;
    private String mStrLogid, mStrLoginAccount, mStrLoginPsw;
    private TextView mTvWorkcard, mTvNumber;
    private Login mLogin;
    private BlackDao mBlackDao;
    private List<CheckItemInfo> mList = new ArrayList<>();
    private List<TaskInfo> mTaskInfos = new ArrayList<>();
    private Button mBtnLoginId, mBtnLoginAccount;
    private ImageView mImgVisual;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                saveMyInfo(mLogin);
                if (getArguments().getString(LOGIN_MODE).equals(Constants.ACCOUNT)) {
                    mBtnLoginAccount.setText(R.string.app_login_set_sleep_text);
                } else {
                    mBtnLoginId.setText(R.string.app_login_set_sleep_text);
                }
                boolean netWorkConnected = NetworkUtil.isNetWorkConnected(_mActivity);
                if (netWorkConnected) {
                    _mActivity.startService(new Intent(_mActivity, BackGroup.class));
                }
                getSleep();
            } else if (msg.what == 1) {
                Toast.makeText(_mActivity, R.string.login_tost_error, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                Toast.makeText(_mActivity, R.string.login_toast_error1_text, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                Toast.makeText(_mActivity, R.string.login_toast_error2_text, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 5) {
                Toast.makeText(_mActivity, R.string.app_login_toast_error_text, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 4) {
                startWithPop(new MyBottomDelegate());
            }
        }
    };

    public static SignDelegate newInstance(String mode) {
        SignDelegate fragment = new SignDelegate();
        Bundle bundle = new Bundle();
        bundle.putString(LOGIN_MODE, mode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Object setLayout() {
        Bundle bundle = getArguments();
        if (bundle.getString(LOGIN_MODE).equals(Constants.ACCOUNT)) {
            return R.layout.delegate_sign_account;
        } else if (bundle.getString(LOGIN_MODE).equals(Constants.ID)) {
            NfcDao dao = new NfcDao(_mActivity);
            NfcDao.mNfcAdapter.enableForegroundDispatch(_mActivity, NfcDao.mPendingIntent, NfcDao.mIntentFilter, NfcDao.mTechList);
            return R.layout.delegate_sign_id;
        }
        return null;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        InitUI(rootView);
    }

    public void InitUI(View rootView) {
        mEtLoginId = rootView.findViewById(R.id.et_login_id);
        mEtLoginPsw = rootView.findViewById(R.id.et_login_psw);
        mEtLoginAccount = rootView.findViewById(R.id.et_login_account);
        mTvWorkcard = rootView.findViewById(R.id.tv_Workcard);
        mTvNumber = rootView.findViewById(R.id.tv_number);
        mBtnLoginAccount = rootView.findViewById(R.id.btn_login_account);
        mBtnLoginId = rootView.findViewById(R.id.btn_login_id);
        mImgVisual = rootView.findViewById(R.id.img_visual);
        if (getArguments().getString(LOGIN_MODE).equals(Constants.ACCOUNT)) {
            mTvWorkcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start(SignDelegate.newInstance(Constants.ID));
                }
            });
        } else {
            mTvNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start(SignDelegate.newInstance(Constants.ACCOUNT));
                    if (getArguments().getString(LOGIN_MODE).equals(Constants.ID)) {
                        NfcDao.mNfcAdapter.disableForegroundDispatch(_mActivity);
                    }
                }
            });
        }
        mImgVisual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVisual();
            }
        });
    }

    @Optional
    @OnClick(R2.id.btn_login_account)
    void onLoginAccount() {
        getAccountText();
        mBlackDao = BlackDao.getInstance(_mActivity);
        if (mStrLoginAccount.length() < 1 || mStrLoginPsw.length() < 1) {
            Toast.makeText(_mActivity, R.string.login_toast_nopsw_text, Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(Constants.ACCOUNT, mStrLoginAccount);
                        map.put(Constants.PASSWORD, mStrLoginPsw);
                        LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb("http://121.196.220.45:8090/api/Account/Login", map);
                        String data = loadDataFromWeb.getData();
                        mLogin = JSONObject.parseObject(data, Login.class);
                        handler.obtainMessage(mLogin.getRetcode()).sendToTarget();

                    } catch (Exception e) {
                        handler.obtainMessage(5).sendToTarget();
                    }
                }
            }).start();
        }
    }

    @Optional
    @OnClick(R2.id.btn_login_id)
    void onLoginId() {
        getIdText();
        mBlackDao = BlackDao.getInstance(_mActivity);
        if (mStrLogid.length() < 1 || mStrLoginPsw.length() < 1) {
            Toast.makeText(_mActivity, R.string.login_toast_nopsw_text, Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(Constants.BEDGEID, mStrLogid);
                        map.put(Constants.PASSWORD, mStrLoginPsw);
                        LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb("http://121.196.220.45:8090/api/Account/Login", map);
                        String data = loadDataFromWeb.getData();
                        mLogin = JSONObject.parseObject(data, Login.class);
                        handler.obtainMessage(mLogin.getRetcode()).sendToTarget();
                    } catch (Exception e) {
                        handler.obtainMessage(5).sendToTarget();
                    }
                }
            }).start();
        }
    }

    public void getAccountText() {
        mStrLoginAccount = mEtLoginAccount.getText().toString();
        mStrLoginPsw = mEtLoginPsw.getText().toString();
    }

    public void getIdText() {
        mStrLogid = mEtLoginId.getText().toString();
        mStrLoginPsw = mEtLoginPsw.getText().toString();
    }

    public void saveMyInfo(Login login) {
        LocalUserInfo.getInstance(_mActivity).setUserInfo(Constants.ACCOUNT, login.getAccount());
        LocalUserInfo.getInstance(_mActivity).setUserInfo(Constants.BEDGEID, login.getBadgeid());
        LocalUserInfo.getInstance(_mActivity).setUserInfo(Constants.ACCOUNTID, login.getAccountid());
        LocalUserInfo.getInstance(_mActivity).setUserInfo(Constants.USERNAME, login.getUsername());
        LocalUserInfo.getInstance(_mActivity).setUserInfo(Constants.GROUPID, login.getUsercode());
        LocalUserInfo.getInstance(_mActivity).setUserInfo(Constants.GROUPNAME, login.getUserpostid());
    }

    public void getSleep() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3500);
                } catch (Exception e) {

                }
                handler.obtainMessage(4).sendToTarget();
            }
        }).start();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("123455", "onStop: ");
    }

    public static final void showData(String data) {
        if (data == null || data.length() == 0) {
//            mTextView.setText("卡中无数据或数据类型不符！");
            return;
        }
        Log.i("qwe", "showData: " + data);
        mEtLoginId.setText(data);
    }

    public void getVisual() {
        if (!mIsvisual) {
            mEtLoginPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mImgVisual.setBackgroundResource(R.mipmap.btn_eyes);
        } else {
            mEtLoginPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mImgVisual.setBackgroundResource(R.mipmap.closedeyes);
        }
        mIsvisual = !mIsvisual;
        mEtLoginPsw.postInvalidate();
        CharSequence text = mEtLoginPsw.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }
}
