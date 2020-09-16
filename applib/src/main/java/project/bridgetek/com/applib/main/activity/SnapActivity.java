package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.R2;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

public class SnapActivity extends Activity {
    private BlackDao mBlackDao;
    private LocalUserInfo mInstance;
    private String mAccountID;
    private ProgressDialog mDialog;

    @OnClick(R2.id.ic_back)
    public void onBack() {
        finish();
    }

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
    }

    public void initUI() {
        mBlackDao = BlackDao.getInstance(SnapActivity.this);
        mInstance = LocalUserInfo.getInstance(SnapActivity.this);
        mAccountID = mInstance.getUserInfo(Constants.ACCOUNTID);
        mDialog = Storage.getPro(this, getString(R.string.delegate_exception_dialog_text));
        // NfcDao dao = new NfcDao(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //NfcDao.mNfcAdapter.enableForegroundDispatch(this, NfcDao.mPendingIntent, NfcDao.mIntentFilter, NfcDao.mTechList);
    }

}
