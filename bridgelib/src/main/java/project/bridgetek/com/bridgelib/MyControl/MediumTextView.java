package project.bridgetek.com.bridgelib.MyControl;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import project.bridgetek.com.bridgelib.toos.HiApplication;

/**
 * Created by czz on 19-4-15.
 */

public class MediumTextView extends AppCompatTextView {
    public MediumTextView(Context context) {
        super(context);
    }

    public MediumTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeface(HiApplication.MEDIUM);
        setIncludeFontPadding(false);
    }

    public MediumTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
