package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.Card.CardManager;
import project.bridgetek.com.applib.main.toos.NfcDao;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;
import project.bridgetek.com.bridgelib.toos.Scaveng;

/**
 * @author DragoNar
 */
public class NfcActivity extends Activity {
    private Button mSetBtn, mbtnExit;
    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private Resources mRes;
    private TextView mBoard;
    private NfcDao mNfcDao;
    private ImageView mIcBack;
    private CheckItemInfo mCheckItemInfo = new CheckItemInfo();
    private String mTaskID;
    private LocalUserInfo mInstance;
    private BlackDao mBlackDao;
    boolean extra;
    LinearLayout btScaveng;
    private boolean usable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        Intent intent = getIntent();
        extra = intent.getBooleanExtra(Constants.SUBMIT, false);
        if (extra) {
            mCheckItemInfo = (CheckItemInfo) intent.getSerializableExtra(Constants.CHECKINFO);
        }
        usable = getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
        if (usable) {
            initData();
        }
        this.mBoard = (TextView) findViewById(R.id.nfcInfo);
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        mIcBack = findViewById(R.id.ic_back);
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextView = findViewById(R.id.nfcInfo);
        btScaveng = findViewById(R.id.bt_scaveng);
        if (extra) {
            btScaveng.setVisibility(View.VISIBLE);
        }
        btScaveng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scaveng scaveng = new Scaveng(NfcActivity.this, 0);
                scaveng.getScav();
            }
        });
    }

    public void initData() {
        //nfc初始化设置
        NfcDao dao = new NfcDao(this);
    }

    public void initUI() {
        mInstance = LocalUserInfo.getInstance(NfcActivity.this);
        mTaskID = mInstance.getUserInfo(Constants.TASKID);
        mBlackDao = BlackDao.getInstance(NfcActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开启前台调度系统
        if (usable) {
            NfcDao.mNfcAdapter.enableForegroundDispatch(this, NfcDao.mPendingIntent, NfcDao.mIntentFilter, NfcDao.mTechList);
        }
        Logger.d("OPEN NFC");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //关闭前台调度系统
        if (usable) {
            NfcDao.mNfcAdapter.disableForegroundDispatch(this);
        }
        Logger.d("CLOSE NFC");
    }

    /**
     * NFC读取显示
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.d(intent.getAction());
        byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        Toast.makeText(NfcActivity.this, R.string.app_login_toast_nfc_text, Toast.LENGTH_SHORT).show();
        final Parcelable i = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String data = null;
        data = CardManager.load(i, mRes);
        if (data == null || data.length() == 0) {
            data = ByteArrayToHexString(id);
            if (data == null || data.length() == 0) {
                mTextView.setText(R.string.setup_nfc_textview_text);
                return;
            }
            mTextView.setText(data);
            return;
        }
        if (extra) {
            setJump(data);
        }
        mTextView.setText(data);
        return;
    }

    //    private void showData(String data) {
//        if (data == null || data.length() == 0) {
//
//            mTextView.setText(R.string.setup_nfc_textview_text);
//            return;
//        }
//        mTextView.setText(data);
//    }
    public static String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra("codedContent");
                setJump(content);
            }
        }
    }

    public void setJump(String data) {
        if (data.equals(mCheckItemInfo.getLabelCode())) {
            Intent intent = null;
            if (mCheckItemInfo.getCheckType().equals(Constants.GC)) {
                intent = new Intent(NfcActivity.this, WatchActivity.class);
                List<CheckItem> item1 = mBlackDao.getCheckAbnorItem(mTaskID, mCheckItemInfo.getCheckItemID());
                if (item1.get(0).getException_YN().equals("0")) {
                    mCheckItemInfo.setSubmit(false);
                    mCheckItemInfo.setCheckOrderNo(4);
                } else {
                    mCheckItemInfo.setSubmit(true);
                }

            } else if (mCheckItemInfo.getCheckType().equals(Constants.CB)) {
                intent = new Intent(NfcActivity.this, MeterActivity.class);
            } else {
                intent = new Intent(NfcActivity.this, MeasureActivity.class);
            }
            intent.putExtra(Constants.SUBMIT, true);
            intent.putExtra(Constants.CHANGE, true);
            intent.putExtra(Constants.CHECKINFO, mCheckItemInfo);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.upcom_check_toast_data_text, Toast.LENGTH_SHORT).show();
        }
    }
}
