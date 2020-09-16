package project.bridgetek.com.applib.sign;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.R2;
import project.bridgetek.com.bridgelib.delegates.BlackDelegate;

// 选择注册方式
public class SelectedSignDelegate extends BlackDelegate {
    @BindView(R2.id.tv_account)
    TextView tv_account;

    @BindView(R2.id.tv_id)
    TextView tv_id;

    @Override
    public Object setLayout() {
        return R.layout.delegate_selectedsign;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @OnClick(R2.id.tv_account)
    void onAccountSign() {
        start(SignDelegate.newInstance("account"));
    }

    @OnClick(R2.id.tv_id)
    void onIdSign() {
        start(SignDelegate.newInstance("id"));
    }
}
