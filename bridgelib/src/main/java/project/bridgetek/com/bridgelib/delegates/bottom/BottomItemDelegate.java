package project.bridgetek.com.bridgelib.delegates.bottom;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import project.bridgetek.com.bridgelib.delegates.BlackDelegate;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BottomItemDelegate extends BlackDelegate {

    private long mExitTime = 0;
    private static final int EXIT_TIME = 2000;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getView();
        if (rootView != null) {
            rootView.setFocusableInTouchMode(true);
            rootView.requestFocus();
        }
    }
}
