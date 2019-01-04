package com.allenxuan.xuanyihuang.customwidgetpack.quicksidebar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class QuickSideBarTipsView extends FrameLayout {
    private TextView mTipsView;

    public QuickSideBarTipsView(Context context) {
        this(context, null);
    }

    public QuickSideBarTipsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickSideBarTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            mTipsView = (TextView) getChildAt(0);
        } else {
            throw new IllegalStateException("Must has a TextView");
        }
    }

    public void setText(String text, int poistion, float y) {

        if (mTipsView != null) {
            setVisibility(View.VISIBLE);
            mTipsView.setText(text);
            LayoutParams layoutParams = (LayoutParams) mTipsView.getLayoutParams();
            layoutParams.topMargin = (int) (y - getWidth() / 2);
            mTipsView.setLayoutParams(layoutParams);
        }
    }


}
