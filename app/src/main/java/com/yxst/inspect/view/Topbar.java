package com.yxst.inspect.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxst.inspect.R;

/**
 * Created By YuanCheng on 2019/8/15 15:06
 */
public class Topbar extends RelativeLayout {
    public Button leftButton ,rightButton;
    public TextView tvTitle;

    private int leftTextColor;
    private String leftText;
    private float leftTextSize;
    private Drawable leftBackground;

    public int rightTextColor;
    public String rightText;
    public float rightTextSize;
    public Drawable rightBackground;

    private int titleTextColor;
    private String titleText;
    private float titleTextSize;
    private Drawable titleBackground;

    private LayoutParams leftParams,rightParams,titleParams;

    public interface TopbarOnclickListener{
        void onLeftClick();
        void onRightClick();
    }
    private TopbarOnclickListener lister;
    public void setOnClickListener(TopbarOnclickListener lister){
        this.lister = lister;
    }


    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor,0);
        leftText = ta.getString(R.styleable.TopBar_leftText);
        leftTextSize = ta.getDimension(R.styleable.TopBar_leftTextSize,0);
        leftBackground = ta.getDrawable(R.styleable.TopBar_leftBackground);

        rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor,0);
        rightText = ta.getString(R.styleable.TopBar_rightText);
        rightTextSize = ta.getDimension(R.styleable.TopBar_rightTextSize,0);
        rightBackground = ta.getDrawable(R.styleable.TopBar_rightBackground);

        titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor,0);
        titleText = ta.getString(R.styleable.TopBar_title);
        titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize,0);
        titleBackground = ta.getDrawable(R.styleable.TopBar_titleBackground);
        ta.recycle();

        leftButton = new Button(context);
        rightButton = new Button(context);
        tvTitle = new Button(context);

        leftButton.setText(leftText);
        leftButton.setTextColor(leftTextColor);
        leftButton.setTextSize(leftTextSize);
        leftButton.setBackground(leftBackground);

        rightButton.setText(rightText);
        rightButton.setTextColor(rightTextColor);
        rightButton.setTextSize(rightTextSize);
        rightButton.setBackground(rightBackground);

        tvTitle.setText(titleText);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setBackground(titleBackground);
        tvTitle.setGravity(Gravity.CENTER);

        setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(leftButton,leftParams);

        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        rightParams.topMargin=16;
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
   //   rightParams.addRule(RelativeLayout.ALIGN_BOTTOM,TRUE);
        addView(rightButton,rightParams);

        titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(tvTitle,titleParams);

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lister.onLeftClick();
            }
        });
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lister.onRightClick();
            }
        });

    }
}
