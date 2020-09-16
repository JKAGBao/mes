package project.bridgetek.com.applib.main.toos;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Parcelable;
import android.provider.Settings;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.toos.Card.Util;

import static project.bridgetek.com.bridgelib.app.Black.getApplicationContext;

public class NfcDao {
    public static final int SW1_OK = 0x00;
    //nfc
    public static NfcAdapter mNfcAdapter;
    public static IntentFilter[] mIntentFilter = null;
    public static PendingIntent mPendingIntent = null;
    public static String[][] mTechList = null;

    /**
     * 构造函数
     */
    public NfcDao(Activity activity) {
        mNfcAdapter = NfcCheck(activity);
        NfcInit(activity);
    }

    /**
     * 检查NFC是否打开
     */
    public static NfcAdapter NfcCheck(Activity activity) {
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        if (mNfcAdapter == null) {
            Toast.makeText(getApplicationContext(), R.string.setup_nfcdao_toast_nonsupport_text, Toast.LENGTH_SHORT).show();
            return null;
        } else {
            if (!mNfcAdapter.isEnabled()) {
                Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
                activity.startActivity(setNfc);
                Toast.makeText(getApplicationContext(), R.string.setup_nfcdao_toast_request_text, Toast.LENGTH_SHORT).show();
            }
        }
        return mNfcAdapter;
    }

    /**
     * 初始化nfc设置
     */
    public static void NfcInit(Activity activity) {
        mPendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter filter2 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        try {
            filter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        mIntentFilter = new IntentFilter[]{filter, filter2};
        mTechList = null;
    }

    /**
     * 读nfc数据
     */
    public static String readFromTag(Intent intent) throws UnsupportedEncodingException {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawArray != null) {
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            if (mNdefRecord != null) {
                String readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                return readResult;
            }
        }
        return "";
    }


//    /**
//     * 写nfc数据
//     */
//    public static void writeToTag(String data, Intent intent) throws IOException, FormatException {
//        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        Ndef ndef = Ndef.get(tag);
//        ndef.connect();
//        NdefRecord ndefRecord = NdefRecord.createTextRecord(null, data);
//        NdefRecord[] records = {ndefRecord};
//        NdefMessage ndefMessage = new NdefMessage(records);
//        ndef.writeNdefMessage(ndefMessage);
//    }

    /**
     * 读nfcID
     */
    public static String readIdFromTag(Intent intent) throws UnsupportedEncodingException {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        byte[] bytes = tag.getId();


        String id = toHex(tag.getId());
        return id;
    }

//    private static String ByteArrayToHexString(byte[] inarray) {
//        int i, j, in;
//        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
//        String str = "";
//
//        for (j = 0; j < inarray.length; j++) {
//            in = (int) inarray[j] & 0xff;
//            i = (in >> 4) & 0x0f;
//            str += hex[i];
//            i = in & 0x0f;
//            str += hex[i];
//        }
//        return str;
//    }

    private static String toHex(byte[] bytes) {
        StringBuilder str = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                str.append('0');
            str.append(Integer.toHexString(b));
            if (i > 0) {
                str.append(" ");
            }
        }
        return str.toString();
    }

    static String load(NfcV tech, Resources res) {
        String data = null;
        try {
            tech.connect();

            int pos, BLKSIZE, BLKCNT;
            byte cmd[], rsp[], ID[], RAW[], STA[], flag, DSFID;

            ID = tech.getTag().getId();
            if (ID == null || ID.length != 8)
                throw new Exception();

            /*--------------------------------------------------------------*/
            // get system information
            /*--------------------------------------------------------------*/
            cmd = new byte[10];
            cmd[0] = (byte) 0x22; // flag
            cmd[1] = (byte) 0x2B; // command
            System.arraycopy(ID, 0, cmd, 2, ID.length); // UID

            rsp = tech.transceive(cmd);
            if (rsp[0] != SW1_OK)
                throw new Exception();

            pos = 10;
            flag = rsp[1];

            DSFID = ((flag & 0x01) == 0x01) ? rsp[pos++] : 0;

            if ((flag & 0x02) == 0x02)
                pos++;

            if ((flag & 0x04) == 0x04) {
                BLKCNT = rsp[pos++] + 1;
                BLKSIZE = (rsp[pos++] & 0xF) + 1;
            } else {
                BLKCNT = BLKSIZE = 0;
            }

            /*--------------------------------------------------------------*/
            // read first 8 block
            /*--------------------------------------------------------------*/
            cmd = new byte[12];
            cmd[0] = (byte) 0x22; // flag
            cmd[1] = (byte) 0x23; // command
            System.arraycopy(ID, 0, cmd, 2, ID.length); // UID
            cmd[10] = (byte) 0x00; // index of first block to get
            cmd[11] = (byte) 0x07; // block count, one less! (see ISO15693-3)

            rsp = tech.transceive(cmd);
            if (rsp[0] != SW1_OK)
                throw new Exception();
            RAW = rsp;
            /*--------------------------------------------------------------*/
            // read last block
            /*--------------------------------------------------------------*/
            cmd[10] = (byte) (BLKCNT - 1); // index of first block to get
            cmd[11] = (byte) 0x00; // block count, one less! (see ISO15693-3)

            rsp = tech.transceive(cmd);
            if (rsp[0] != SW1_OK)
                throw new Exception();
            STA = rsp;
            data = Util.toHexString(rsp, 0, rsp.length);
            /*--------------------------------------------------------------*/
            // build result string
            /*--------------------------------------------------------------*/
//            final int type = parseType(DSFID, RAW, BLKSIZE);
//            final String name = parseName(type, res);
//            final String info = parseInfo(ID, res);
//            final String extra = parseData(type, RAW, STA, BLKSIZE, res);
//            data = CardManager.buildResult(name, info, extra, null);

        } catch (Exception e) {
            data = null;
            // data = e.getMessage();
        }
        try {
            tech.close();
        } catch (Exception e) {
        }
        return data;
    }
}
