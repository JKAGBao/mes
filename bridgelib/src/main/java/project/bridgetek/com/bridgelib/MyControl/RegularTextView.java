package project.bridgetek.com.bridgelib.MyControl;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import project.bridgetek.com.bridgelib.toos.HiApplication;

public class RegularTextView extends AppCompatTextView {
    public RegularTextView(Context context) {
        super(context);
    }

    public RegularTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeface(HiApplication.REGULAR);
        setIncludeFontPadding(false);
    }

    public RegularTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
