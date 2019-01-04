package com.allenxuan.xuanyihuang.customwidgetpack.quicksidebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.allenxuan.xuanyihuang.customwidgetpack.R;
import com.allenxuan.xuanyihuang.customwidgetpack.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.allenxuan.xuanyihuang.customwidgetpack.utils.DimensUtils;

import java.util.ArrayList;
import java.util.List;

public class QuickSideBarView extends View {

    private OnQuickSideBarTouchListener listener;
    private List<String> mLetters;
    private int mChoose = -1;
    private Paint mPaint = new Paint();
    private float mTextSize;
    private int mTextColor;
    private int mTextColorChoose;
    private int mBgColorChoose;
    private int mDrawWidth;
    private int mHeight;
    private float mItemHeight = 0;
    private String mFontPath;
    private Rect textRect = new Rect();

    public QuickSideBarView(Context context) {
        this(context, null);
    }

    public QuickSideBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickSideBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mLetters = new ArrayList<>();

        mTextColor = context.getResources().getColor(android.R.color.black);
        mTextColorChoose = context.getResources().getColor(android.R.color.black);
        mTextSize = context.getResources().getDimensionPixelSize(R.dimen.textSize_quicksidebar);
//        mItemHeight = context.getResources().getDimension(R.dimen.height_quicksidebaritem);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.QuickSideBarView);

            mTextColor = a.getColor(R.styleable.QuickSideBarView_sidebarTextColor, mTextColor);
            mTextColorChoose = a.getColor(R.styleable.QuickSideBarView_sidebarTextColorChoose, mTextColorChoose);
            mTextSize = a.getDimension(R.styleable.QuickSideBarView_sidebarTextSize, mTextSize);
            mBgColorChoose = a.getColor(R.styleable.QuickSideBarView_sidebarBgColorChoose, mTextColorChoose);
            mFontPath = a.getString(R.styleable.QuickSideBarView_sidebar_font_path);
            a.recycle();
        }
        mPaint.setAntiAlias(true);
        setFont(mFontPath);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float textSize = mTextSize;
        if (!mLetters.isEmpty()) {
            mItemHeight = (mHeight - getPaddingTop() - getPaddingBottom()) / mLetters.size();
            if (mItemHeight / mTextSize > 2.5) {
                textSize = mTextSize + DimensUtils.dip2pixel(getContext(), 2);
            } else if (mItemHeight / mTextSize < 1.2) {
                textSize = mTextSize - DimensUtils.dip2pixel(getContext(), 2);
            }
        }

        mPaint.setTextSize(textSize);
        mPaint.setColor(mTextColor);
        mPaint.setFakeBoldText(true);

        int selectRectHeight = (int) Math.min(mItemHeight, mDrawWidth);
        for (int i = 0; i < mLetters.size(); i++) {
            if (i == mChoose) {
                mPaint.setColor(mBgColorChoose);

                textRect.top = (int) (mItemHeight * i + (mItemHeight - selectRectHeight) / 2) + getPaddingTop();
                textRect.bottom = (int) (textRect.top + selectRectHeight);
                textRect.left = 0;
                textRect.right = mDrawWidth;
                canvas.drawRect(textRect, mPaint);
                mPaint.setColor(mTextColorChoose);
            } else {
                mPaint.setColor(mTextColor);
            }

            mPaint.getTextBounds(mLetters.get(i), 0, mLetters.get(i).length(), textRect);
            float xPos = (int) ((mDrawWidth - textRect.width()) * 0.5);
            float yPos = mItemHeight * i + (int) ((mItemHeight * 0.5 + textRect.height() / 2)) + getPaddingTop();
            canvas.drawText(mLetters.get(i), xPos, yPos, mPaint);
        }

    }

    public void setFont(String assetFontPath) {
        if (TextUtils.isEmpty(assetFontPath)) {
            return;
        }
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), assetFontPath);
        if (font != null) {
            mPaint.setTypeface(font);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mDrawWidth = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mItemHeight == 0) {
            return false;
        }
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = mChoose;
        final int newChoose = (int) ((y - getPaddingTop()) / mItemHeight);
        switch (action) {
            case MotionEvent.ACTION_UP:
                if (listener != null) {
                    listener.onLetterTouching(false);
                }
                invalidate();
                break;
            default:
                if (oldChoose != newChoose) {
                    if (newChoose >= 0 && newChoose < mLetters.size()) {
                        mChoose = newChoose;
                        if (listener != null) {
                            //计算位置
                            Rect rect = new Rect();
                            mPaint.getTextBounds(mLetters.get(mChoose), 0, mLetters.get(mChoose).length(), rect);
                            float yPos = mItemHeight * mChoose + (int) ((mItemHeight + rect.height()) * 0.5) + getPaddingTop();
                            listener.onLetterChanged(mLetters.get(newChoose), mChoose, yPos);
                        }
                    }
                    invalidate();
                }
                //如果是cancel也要调用onLetterUpListener 通知
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    if (listener != null) {
                        listener.onLetterTouching(false);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下调用 onLetterDownListener
                    if (listener != null) {
                        listener.onLetterTouching(true);
                    }
                }

                break;
        }
        return true;
    }

    public OnQuickSideBarTouchListener getListener() {
        return listener;
    }

    public void setOnQuickSideBarTouchListener(OnQuickSideBarTouchListener listener) {
        this.listener = listener;
    }

    public List<String> getLetters() {
        return mLetters;
    }

    /**
     * 设置字母表
     *
     * @param letters
     */
    public void setLetters(List<String> letters) {
        this.mLetters = letters;
        requestLayout();
    }

    public void setCurLetter(String letter) {
        for (int i = 0; i < mLetters.size(); ++i) {
            if (mLetters.get(i).equalsIgnoreCase(letter)) {
                mChoose = i;
                postInvalidate();
                break;
            }
        }

    }
}

