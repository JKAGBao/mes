package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.Login;
import project.bridgetek.com.applib.main.bean.body.LoginBody;
import project.bridgetek.com.applib.main.toos.ActivityCollectorUtil;
import project.bridgetek.com.applib.main.toos.Card.CardManager;
import project.bridgetek.com.applib.main.toos.DropEditText;
import project.bridgetek.com.applib.main.toos.NetworkUtil;
import project.bridgetek.com.applib.main.toos.NfcDao;
import project.bridgetek.com.applib.main.toos.RetrofitManager;
import project.bridgetek.com.applib.main.toos.UploadService;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import retrofit2.Call;

public class LoginActivity extends Activity {
    private boolean mIsvisual, mIsvisual1;
    private EditText mEtLoginPsw;
    private DropEditText mEtLoginAccount;
    private EditText mEtLoginId, mEtLoginPsw1;
    private String mStrLogid, mStrLoginAccount, mStrLoginPsw;
    private TextView mTvWorkcard, mTvNumber;
    private Login mLogin;
    private Button mBtnLoginId, mBtnLoginAccount;
    private ImageView mImgVisual, mImgVisual1;
    private LinearLayout mLlLoginfrom, mLlLoginfrom2;
    private Button mBtnSkip1, mBtnSkip;
    private ArrayAdapter<String> adapter;
    private List<String> mList = new ArrayList<>();
    LocalUserInfo mUserInfo;
    private boolean login = true;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                saveMyInfo(mLogin);
                mBtnLoginAccount.setText(R.string.app_login_set_sleep_text);
                mBtnLoginId.setText(R.string.app_login_set_sleep_text);
                boolean netWorkConnected = NetworkUtil.isNetWorkConnected(LoginActivity.this);
                if (netWorkConnected) {
                    //    startService(new Intent(LoginActivity.this, BackGroup.class));
                    startService(new Intent(LoginActivity.this, UploadService.class));
                }
                getSleep();
            } else if (msg.what == 1) {
                login = true;
                Toast.makeText(LoginActivity.this, R.string.login_tost_error, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                login = true;
                Toast.makeText(LoginActivity.this, R.string.login_toast_error1_text, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                login = true;
                Toast.makeText(LoginActivity.this, R.string.login_toast_error2_text, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 5) {
                login = true;
                Toast.makeText(LoginActivity.this, R.string.app_login_toast_error_text, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 4) {
                login = true;
                ActivityCollectorUtil.finishAllActivity();
                startActivity(new Intent(LoginActivity.this, AppActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initUI();
        setOnclick();
    }

    public void initUI() {
        mUserInfo = LocalUserInfo.getInstance(this);
        mEtLoginId = findViewById(R.id.et_login_id);
        mEtLoginPsw = findViewById(R.id.et_login_psw);
        mEtLoginAccount = findViewById(R.id.et_login_account);
        mTvWorkcard = findViewById(R.id.tv_Workcard);
        mTvNumber = findViewById(R.id.tv_number);
        mBtnLoginAccount = findViewById(R.id.btn_login_account);
        mBtnLoginId = findViewById(R.id.btn_login_id);
        mImgVisual = findViewById(R.id.img_visual);
        mEtLoginPsw1 = findViewById(R.id.et_login_psw1);
        mImgVisual1 = findViewById(R.id.img_visual1);
        mLlLoginfrom = findViewById(R.id.ll_loginfrom);
        mLlLoginfrom2 = findViewById(R.id.ll_loginfrom2);
        mBtnSkip1 = findViewById(R.id.btn_skip1);
        mBtnSkip = findViewById(R.id.btn_skip);
        mLlLoginfrom2.setVisibility(View.GONE);
        mList = mUserInfo.getDataList(Constants.LOGINACCOUTS);
        if (mList.size() > 0) {
            String[] strings = new String[mList.size()];
            for (int i = 0; i < mList.size(); i++) {
                strings[i] = mList.get(i);
            }
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings);
            mEtLoginAccount.setAdapter(adapter);
        }
        NfcDao dao = new NfcDao(LoginActivity.this);
    }

    public void setOnclick() {
        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollectorUtil.finishAllActivity();
                startActivity(new Intent(LoginActivity.this, AppActivity.class));
                finish();
            }
        });
        mBtnSkip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollectorUtil.finishAllActivity();
                startActivity(new Intent(LoginActivity.this, AppActivity.class));
                finish();
            }
        });
        mTvWorkcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlLoginfrom.setVisibility(View.GONE);
                mLlLoginfrom2.setVisibility(View.VISIBLE);
                NfcDao.mNfcAdapter.enableForegroundDispatch(LoginActivity.this, NfcDao.mPendingIntent, NfcDao.mIntentFilter, NfcDao.mTechList);
            }
        });
        mTvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlLoginfrom2.setVisibility(View.GONE);
                mLlLoginfrom.setVisibility(View.VISIBLE);
                NfcDao.mNfcAdapter.disableForegroundDispatch(LoginActivity.this);
            }
        });
        mImgVisual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVisual();
            }
        });
        mImgVisual1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVisual1();
            }
        });
        mBtnLoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccountText();
                if (mStrLoginAccount.length() < 1) {
                    Toast.makeText(LoginActivity.this, R.string.login_toast_nopsw_text, Toast.LENGTH_SHORT).show();
                } else {
                    if (login) {
                        login = false;
                        LoginBody loginBody = new LoginBody();
                        loginBody.setAccount(mStrLoginAccount);
                        loginBody.setPassword(mStrLoginPsw);
                        Call<Login> loginCall = RetrofitManager.instance(true).setLogin(loginBody);
                        loginCall.enqueue(new RetrofitManager.SimpleCallback<Login>() {
                            @Override
                            public void onResponseSuccess(Login result) {
                                if (result != null) {
                                    mLogin = result;
                                    handler.obtainMessage(result.getRetcode()).sendToTarget();
                                } else {
                                    handler.obtainMessage(5).sendToTarget();
                                }
                            }

                            @Override
                            public void onResponseFailure() {
                                super.onResponseFailure();
                                handler.obtainMessage(5).sendToTarget();
                            }
                        });
                    }
                }
            }
        });
        mBtnLoginId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIdText();
                if (mStrLogid.length() < 1 || mStrLoginPsw.length() < 1) {
                    Toast.makeText(LoginActivity.this, R.string.login_toast_nopsw_text, Toast.LENGTH_SHORT).show();
                } else {
                    LoginBody loginBody = new LoginBody();
                    loginBody.setBadgeid(mStrLogid);
                    loginBody.setPassword(mStrLoginPsw);
                    Call<Login> loginCall = RetrofitManager.instance(true).setLogin(loginBody);
                    loginCall.enqueue(new RetrofitManager.SimpleCallback<Login>() {
                        @Override
                        public void onResponseSuccess(Login result) {
                            if (result != null) {
                                mLogin = result;
                                handler.obtainMessage(result.getRetcode()).sendToTarget();
                            } else {
                                handler.obtainMessage(5).sendToTarget();
                            }
                        }

                        @Override
                        public void onResponseFailure() {
                            super.onResponseFailure();
                            handler.obtainMessage(5).sendToTarget();
                        }
                    });
                }
            }
        });
    }

    public void getAccountText() {
        mStrLoginAccount = mEtLoginAccount.getText().toString();
        mStrLoginPsw = mEtLoginPsw.getText().toString();
    }

    public void getIdText() {
        mStrLogid = mEtLoginId.getText().toString();
        mStrLoginPsw = mEtLoginPsw1.getText().toString();
    }

    public void saveMyInfo(Login login) {
        boolean isHave = false;
        for (int i = 0; i < mList.size(); i++) {
            if (mStrLoginAccount.equals(mList.get(i))) {
                isHave = true;
            }
        }
        if (!isHave) {
            mList.add(mStrLoginAccount);
        }
        mUserInfo.setDataList(Constants.LOGINACCOUTS, mList);
        mUserInfo.setUserInfo(Constants.ACCOUNT, login.getAccount());
        mUserInfo.setUserInfo(Constants.BEDGEID, login.getBadgeid());
        mUserInfo.setUserInfo(Constants.ACCOUNTID, login.getAccountid());
        mUserInfo.setUserInfo(Constants.USERNAME, login.getUsername());
        mUserInfo.setUserInfo(Constants.GROUPID, login.getUsercode());
        mUserInfo.setUserInfo(Constants.GROUPNAME, login.getUserpostid());
        mUserInfo.setUserInfo(Constants.DEPARTMENTNAME, login.getDepartmentname());
        mUserInfo.setUserInfo(Constants.COMPANYNAME, login.getCompanyname());
        if (!login.getPermissions().isEmpty()) {
            mUserInfo.setDataList(Constants.PERMISSIONS, login.getPermissions());
        }
        if (!login.getExceptionlevelnames().isEmpty()) {
            mUserInfo.setDataList(Constants.EXCEPTIONLEVE, login.getExceptionlevelnames());
        }
        if (!login.getEquipmentStatusNames().isEmpty()) {
            mUserInfo.setDataList(Constants.EQUIPMENTSTATUS, login.getEquipmentStatusNames());
        }
        Constants.CURRENTUSER = mStrLoginAccount;
    }

    public void getSleep() {
        handler.sendEmptyMessageDelayed(4, 1500);
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

    public void getVisual1() {
        if (!mIsvisual1) {
            mEtLoginPsw1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mImgVisual1.setBackgroundResource(R.mipmap.btn_eyes);
        } else {
            mEtLoginPsw1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mImgVisual1.setBackgroundResource(R.mipmap.closedeyes);
        }
        mIsvisual1 = !mIsvisual1;
        mEtLoginPsw1.postInvalidate();
        CharSequence text = mEtLoginPsw1.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    /**
     * NFC读取显示
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, R.string.app_login_toast_nfc_text, Toast.LENGTH_SHORT).show();
        final Parcelable i = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        showData(CardManager.load(i, null));
    }

    public void showData(String data) {
        if (data == null || data.length() == 0) {
            return;
        }
        mEtLoginId.setText(data);
    }
}
