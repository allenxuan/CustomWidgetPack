package com.allenxuan.xuanyihuang.customwidgetpack.imageview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.widget.ImageView
import android.graphics.drawable.Drawable
import com.allenxuan.xuanyihuang.customwidgetpack.R

class CircleImageView : ImageView {
    val paint: Paint = Paint()
    val outerCirclePaint = Paint()
    val path: Path = Path()
    var circleRectF = RectF(0.0f, 0.0f, 0.0f, 0.0f)
    var outerCircleRectF = RectF(0.0f, 0.0f, 0.0f, 0.0f)

    var withOuterCircle = false
    var outerCircleColor: Int = Color.parseColor("#ffffffff")
    var outCircleWidth: Int = 2

    var bitmap: Bitmap? = null
    var bitmapShader: BitmapShader? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init(context, attributeSet)
    }


    private fun init(context: Context, attributeSet: AttributeSet?) {
        val a: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CircleImageView)
        withOuterCircle = a.getBoolean(R.styleable.CircleImageView_with_outer_circle, false)
        outerCircleColor = a.getColor(R.styleable.CircleImageView_outer_circle_color, outerCircleColor)
        outCircleWidth = a.getInt(R.styleable.CircleImageView_outer_circle_width, outCircleWidth)
        a.recycle()

        paint.isAntiAlias = true

        outerCirclePaint.isAntiAlias = true
        outerCirclePaint.color = outerCircleColor
        outerCirclePaint.strokeWidth = (context.resources.displayMetrics.density * outCircleWidth + 0.5f).toInt().toFloat()
        outerCirclePaint.style = Paint.Style.STROKE
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)


        circleRectF.right = width.toFloat()
        circleRectF.bottom = height.toFloat()

        outerCircleRectF.left = circleRectF.left + outerCirclePaint.strokeWidth / 2
        outerCircleRectF.top = circleRectF.top + outerCirclePaint.strokeWidth / 2
        outerCircleRectF.right = circleRectF.right - outerCirclePaint.strokeWidth / 2
        outerCircleRectF.bottom = circleRectF.bottom - outerCirclePaint.strokeWidth / 2

        if (drawable != null && width > 0 && height > 0) {
            bitmap = drawable2Bitmap(drawable)
            if (bitmap != null) {
                bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                paint.shader = bitmapShader
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) {
            return  // couldn't resolve the URI
        }

        if (drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) {
            return      // nothing to draw (empty bounds)
        }

        if (imageMatrix == null && paddingTop == 0 && paddingLeft == 0) {
            drawable.draw(canvas)
        } else {
            val saveCount = canvas.saveCount
            canvas.save()

            if (cropToPadding) {
                val scrollX = scrollX
                val scrollY = scrollY
                canvas.clipRect(scrollX + paddingLeft, scrollY + paddingTop,
                        scrollX + right - left - paddingRight,
                        scrollY + bottom - top - paddingBottom)
            }

            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())

            if (bitmap != null && bitmapShader != null) {
                canvas.drawOval(circleRectF, paint)
                if (withOuterCircle) {
                    canvas.drawOval(outerCircleRectF, outerCirclePaint)
                }
            } else {
                if (imageMatrix != null) {
                    canvas.concat(imageMatrix)
                }
                drawable.draw(canvas)
            }

            canvas.restoreToCount(saveCount)
        }

    }


    /**
     * drawable转换成bitmap
     */
    private fun drawable2Bitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}