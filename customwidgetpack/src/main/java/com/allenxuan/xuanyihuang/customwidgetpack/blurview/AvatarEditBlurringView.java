package com.allenxuan.xuanyihuang.customwidgetpack.blurview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

public class AvatarEditBlurringView extends BlurringView {
    private Paint mPaint;
    private Path mPath;
    private RectF innerRectFLarge;
    private RectF outterRectFLarge;
    private RectF innerRectFSmall;

    private int circleMargin = 0; //pixel

    int width;
    int height;

    private Bitmap mCenterCroppedTempBitmap, mCenterCroppedBitmap;
    private Canvas mCenterCroppedTempCanvas;

    public AvatarEditBlurringView(Context context) {
        this(context, null);
    }

    public AvatarEditBlurringView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarEditBlurringView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStrokeWidth(5);

        mPath = new Path();

        innerRectFLarge = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        outterRectFLarge = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        // innerRectFSmall = new RectF(0.0f, 0.0f, 0.0f, 0.0f);

        circleMargin = (int) (context.getResources().getDisplayMetrics().density * 16 + 0.5f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int width = getWidth();
        int height = getHeight();

        if (height >= width) {

            innerRectFLarge.left = circleMargin;
            innerRectFLarge.top = (height - width) / 2.0f + circleMargin;
            innerRectFLarge.right = width - circleMargin;
            innerRectFLarge.bottom = (height + width) / 2.0f - circleMargin;

            outterRectFLarge.left = circleMargin - 5;
            outterRectFLarge.top = (height - width) / 2.0f + circleMargin - 5;
            outterRectFLarge.right = width - circleMargin + 5;
            outterRectFLarge.bottom = (height + width) / 2.0f - circleMargin + 5;

            // innerRectFSmall.left = 0.9f * width;
            // innerRectFSmall.top = (height - width) / 2.0f;
            // innerRectFSmall.right = width;
            // innerRectFSmall.bottom = (height - width) / 2.0f + 0.1f * width;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (height >= width) {
            canvas.save();

            mPath.rewind();
            // mPath.addArc(innerRectFLarge, 0.0f, 270.0f);
            // mPath.arcTo(innerRectFSmall, -90.0f, 90.0f, false);
            // mPath.close();
            mPath.addOval(innerRectFLarge, Path.Direction.CW);
            canvas.clipPath(mPath, Region.Op.DIFFERENCE);
            super.onDraw(canvas);
            canvas.drawOval(outterRectFLarge, mPaint);

            canvas.restore();
        } else {
            super.onDraw(canvas);
        }
    }

    public Bitmap getCenterCroppedBitmap() {
        View blurredView = getBlurredView();

        //暂时不处理宽大于长的情况
        if (blurredView == null || getWidth() > getHeight()) {
            return null;
        }

        if (mCenterCroppedTempCanvas == null) {
            final int blurredViewWidth = blurredView.getWidth();
            final int blurredViewHeight = blurredView.getHeight();

            mCenterCroppedTempBitmap = Bitmap.createBitmap(blurredViewWidth, blurredViewHeight, Bitmap.Config.ARGB_8888);
            mCenterCroppedTempCanvas = new Canvas(mCenterCroppedTempBitmap);

        }

        getBlurredView().draw(mCenterCroppedTempCanvas);
        mCenterCroppedBitmap = Bitmap.createBitmap(mCenterCroppedTempBitmap, (int) (getX() - blurredView.getX() + innerRectFLarge.left), (int) (getY() - blurredView.getY() + innerRectFLarge.top), (int) innerRectFLarge.width(), (int) innerRectFLarge.height());

        return mCenterCroppedBitmap;

    }
}
