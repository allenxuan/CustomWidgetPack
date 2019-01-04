package com.allenxuan.xuanyihuang.customwidgetpack.imageview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import com.allenxuan.xuanyihuang.customwidgetpack.R

class CornerImageView : ImageView {
    val paint: Paint = Paint()
    val path: Path = Path()

    var viewWidth = 0
    var viewHeight = 0

    var upperLeftRectF = RectF(0.0f, 0.0f, 0.0f, 0.0f)
    var upperRightRectF = RectF(0.0f, 0.0f, 0.0f, 0.0f)
    var lowerLeftRectF = RectF(0.0f, 0.0f, 0.0f, 0.0f)
    var lowerRightRectF = RectF(0.0f, 0.0f, 0.0f, 0.0f)

    var radius: Int = 0
    var widthEqualHeight = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init(context, attributeSet)
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {
        val a: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CornerImageView)
        radius = a.getInt(R.styleable.CornerImageView_corner_radius, 0)
        try {
            radius = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius.toFloat(), context.resources.displayMetrics)).toInt()
        }catch (e : Exception){
            Log.e("XuanCornerImageView","dp to px conversion error")
        }
        widthEqualHeight = a.getBoolean(R.styleable.CornerImageView_width_equal_height, false)
        a.recycle()

        paint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (widthEqualHeight) {
            setMeasuredDimension(measuredWidth, measuredWidth)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        viewWidth = width
        viewHeight = height

        upperLeftRectF.left = 0.0f
        upperLeftRectF.top = 0.0f
        upperLeftRectF.right = (radius * 2).toFloat()
        upperLeftRectF.bottom = (radius * 2).toFloat()

        upperRightRectF.left = (viewWidth - radius * 2).toFloat()
        upperRightRectF.top = 0.0f
        upperRightRectF.right = viewWidth.toFloat()
        upperRightRectF.bottom = (radius * 2).toFloat()

        lowerLeftRectF.left = 0.0f
        lowerLeftRectF.top = (viewHeight - radius * 2).toFloat()
        lowerLeftRectF.right = (radius * 2).toFloat()
        lowerLeftRectF.bottom = viewHeight.toFloat()

        lowerRightRectF.left = (viewWidth - radius * 2).toFloat()
        lowerRightRectF.top = (viewHeight - radius * 2).toFloat()
        lowerRightRectF.right = viewWidth.toFloat()
        lowerRightRectF.bottom = viewHeight.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()

        path.rewind()
        path.addArc(upperLeftRectF, -180.0f, 90.0f)
        path.arcTo(upperRightRectF, -90.0f, 90.0f, false)
        path.arcTo(lowerRightRectF, 0.0f, 90.0f, false)
        path.arcTo(lowerLeftRectF, 90.0f, 90.0f, false)
        path.close()
        canvas.clipPath(path)

        super.onDraw(canvas)

        canvas.restore()
    }
}