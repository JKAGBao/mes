package project.bridgetek.com.bridgelib.MyControl;

import android.content.Context;
import android.util.AttributeSet;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import androidx.appcompat.widget.AppCompatTextView;
/**
 * Created by czz on 19-4-15.
 */

public class BoldTextView extends AppCompatTextView {

    public BoldTextView(Context context) {
        super(context);
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(HiApplication.BOLD);
        setIncludeFontPadding(false);
    }

    public BoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
