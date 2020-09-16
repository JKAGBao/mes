package project.bridgetek.com.bridgelib.toos;

import android.app.Activity;
import android.content.Intent;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

/**
 * Created by Cong Zhizhong on 18-6-13.
 */

public class Scaveng {
    private Activity mactivity;
    private int mcode;

    public Scaveng(Activity mactivity, int mcode) {
        this.mactivity = mactivity;
        this.mcode = mcode;
    }

    public void getScav(){
        Intent intent = new Intent(mactivity, CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
        config.setShowAlbum(false);//是否显示相册
        config.setShowFlashLight(true);//是否显示闪光灯
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        mactivity.startActivityForResult(intent, mcode);
    }
}
