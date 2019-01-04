package com.allenxuan.xuanyihuang.customwidgetpack.quicksidebar.tipsview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.allenxuan.xuanyihuang.customwidgetpack.R;

@SuppressLint("AppCompatCustomView")
public class QuickSideBarTipsItemView extends TextView {
    private String mFontPath;


    public QuickSideBarTipsItemView(Context context) {
        this(context, null);
    }

    public QuickSideBarTipsItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickSideBarTipsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        int mTextColor = context.getResources().getColor(android.R.color.black);
        float mTextSize = context.getResources().getDimension(R.dimen.textSize_quicksidebartips);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.QuickSideBarView);

            mTextColor = a.getColor(R.styleable.QuickSideBarView_sidebarTextColor, mTextColor);
            mTextSize = a.getDimension(R.styleable.QuickSideBarView_sidebarTextSize, mTextSize);
            mFontPath = a.getString(R.styleable.QuickSideBarView_sidebar_font_path);
            Drawable bg = a.getDrawable(R.styleable.QuickSideBarView_sidebar_tips_background);
            setBackground(bg);
            a.recycle();
        }

        setFont(mFontPath);
        setTextColor(mTextColor);
        setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY));
    }

    public void setFont(String assetFontPath) {
        if (TextUtils.isEmpty(assetFontPath)) {
            return;
        }
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), assetFontPath);
        if (font != null) {
            setTypeface(font);
        }
    }



}
