package com.bulingzhuang.randomportrait

import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*

/**
 * Created by bulingzhuang
 * on 2017/6/27
 * E-mail:bulingzhuang@foxmail.com
 */
class RandomPortraitView : View {

    var mShowCircle: Boolean = false
    var mShowBorder: Boolean = false
    var mBorderWidth: Int = 0
    var mBorderPaint: Paint = Paint()
    var mPortraitPaint: Paint = Paint()
    var mHalfStr: String = ""
    var mCenterStr: String = ""
    var colorList = listOf("#f69988", "#f36c60", "#e84e40", "#e51c23", "#dd191d", "#d01716", "#c41411",
            "#f48fb1", "#f06292", "#ec407a", "#e91e63", "#d81b60", "#c2185b", "#ad1457",
            "#ce93d8", "#ba68c8", "#ab47bc", "#9c27b0", "#8e24aa", "#7b1fa2", "#6a1b9a",
            "#b39ddb", "#9575cd", "#7e57c2", "#673ab7", "#5e35b1", "#512da8", "#4527a0",
            "#9fa8da", "#7986cb", "#5c6bc0", "#3f51b5", "#3949ab", "#303f9f", "#283593",
            "#afbfff", "#91a7ff", "#738ffe", "#5677fc", "#4e6cef", "#455ede", "#3b50ce",
            "#81d4fa", "#4fc3f7", "#29b6f6", "#03a9f4", "#039be5", "#0288d1", "#0277bd",
            "#80deea", "#4dd0e1", "#26c6da", "#00bcd4", "#00acc1", "#0097a7", "#00838f",
            "#80cbc4", "#4db6ac", "#26a69a", "#009688", "#00897b", "#00796b", "#00695c",
            "#72d572", "#42bd41", "#2baf2b", "#259b24", "#0a8f08", "#0a7e07", "#056f00",
            "#c5e1a5", "#aed581", "#9ccc65", "#8bc34a", "#7cb342", "#689f38", "#558b2f",
            "#e6ee9c", "#dce775", "#d4e157", "#cddc39", "#c0ca33", "#afb42b", "#9e9d24",
            "#fff59d", "#fff176", "#ffee58", "#ffeb3b", "#fdd835", "#fbc02d", "#f9a825",
            "#ffe082", "#ffd54f", "#ffca28", "#ffc107", "#ffb300", "#ffa000", "#ff8f00",
            "#ffcc80", "#ffb74d", "#ffa726", "#ff9800", "#fb8c00", "#f57c00", "#ef6c00",
            "#ffab91", "#ff8a65", "#ff7043", "#ff5722", "#f4511e", "#e64a19", "#d84315",
            "#bcaaa4", "#a1887f", "#8d6e63", "#795548", "#6d4c41", "#5d4037", "#4e342e",
            "#eeeeee", "#e0e0e0", "#bdbdbd", "#9e9e9e", "#757575", "#616161", "#424242",
            "#b0bec5", "#90a4ae", "#78909c", "#607d8b", "#546e7a", "#455a64", "#37474f")
    var color: String = ""
    var mPortraitCode: String = ""
    var mCallback: CodeChangeCallback? = null

    interface CodeChangeCallback {
        fun notify(portraitCode: String)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttr(attrs)
        init()
    }

    private fun initAttr(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RandomPortraitView)
        mShowCircle = typedArray.getBoolean(R.styleable.RandomPortraitView_showCircle, false)
        mShowBorder = typedArray.getBoolean(R.styleable.RandomPortraitView_showBorder, false)
        mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.RandomPortraitView_borderWidth, 0)
        typedArray.recycle()
    }

    private fun init() {
        changeRandom()
        mBorderPaint.isAntiAlias = true
        mBorderPaint.style = Paint.Style.STROKE
        if (TextUtils.isEmpty(color)) {
            mBorderPaint.color = Color.parseColor("#009688")
        } else {
            mBorderPaint.color = Color.parseColor(color)
        }
        mBorderPaint.strokeWidth = mBorderWidth.toFloat()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        createShader()
    }

    private fun changeRandom() {
        val random = Random()
        val halfPieceNum = random.nextInt(1024)
        val centerPieceNum = random.nextInt(32)
        color = colorList[random.nextInt(133)]
        mHalfStr = Integer.toBinaryString(halfPieceNum)
        val lenH = 10 - mHalfStr.length
        for (position in 1..lenH) {
            mHalfStr = "0" + mHalfStr
        }
        Log.e("blz", "半区数组：" + mHalfStr)
        mCenterStr = Integer.toBinaryString(centerPieceNum)
        val lenC = 5 - mCenterStr.length
        for (position in 1..lenC) {
            mCenterStr = "0" + mCenterStr
        }
        Log.e("blz", "中间数组：" + mCenterStr)
        mPortraitCode = mHalfStr + mCenterStr + color
        Log.e("blz", "头像代码：$mPortraitCode")
        mCallback?.notify(mPortraitCode)
    }

    fun random() {
        changeRandom()
        createShader()
        invalidate()
    }

    private fun createShader() {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val piecePaint = Paint()
        piecePaint.isAntiAlias = true
        piecePaint.style = Paint.Style.FILL
        if (TextUtils.isEmpty(color)) {
            piecePaint.color = Color.parseColor("#009688")
        } else {
            piecePaint.color = Color.parseColor(color)
        }
        if (TextUtils.isEmpty(color)) {
            mBorderPaint.color = Color.parseColor("#009688")
        } else {
            mBorderPaint.color = Color.parseColor(color)
        }
        val pieceWidth = width / 5f
        val pieceHeight = height / 5f
        for (index in mHalfStr.indices) {
            when (mHalfStr[index]) {
                '1' -> {
                    val rows = index / 2
                    val columns = index % 2
                    Log.e("blz", "行：$rows，列$columns")
                    canvas.drawRect(pieceHeight * columns, pieceHeight * rows, pieceWidth * (columns + 1), pieceHeight * (rows + 1), piecePaint)
                    canvas.drawRect(pieceHeight * (4 - columns), pieceHeight * rows, pieceWidth * (4 - columns + 1), pieceHeight * (rows + 1), piecePaint)
                }
            }
        }

        for (index in mCenterStr.indices) {
            when (mCenterStr[index]) {
                '1' -> {
                    canvas.drawRect(pieceHeight * 2, pieceHeight * index, pieceWidth * 3, pieceHeight * (index + 1), piecePaint)
                }
            }
        }
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        mPortraitPaint.shader = bitmapShader
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = setMeasureSize(widthMeasureSpec, 77f)
        val height = setMeasureSize(heightMeasureSpec, 77f)
        Log.e("blz", "宽度：" + width)
        Log.e("blz", "高度：" + height)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBorder(canvas)
    }

    /**
     * 绘制边框
     */
    private fun drawBorder(canvas: Canvas?) {
        canvas?.let {
            it.save()
            if (mShowBorder) {
                if (mShowCircle) {
                    it.drawCircle(width / 2f, height / 2f, (minOf(width, height) - mBorderPaint.strokeWidth) / 2 - 1f, mBorderPaint)
                    it.drawCircle(width / 2f, height / 2f, minOf(width, height) / 2f, mPortraitPaint)
                } else {
                    it.drawRect(mBorderPaint.strokeWidth / 2, mBorderPaint.strokeWidth / 2, width - mBorderPaint.strokeWidth / 2 - 0.5f, height - mBorderPaint.strokeWidth / 2 - 0.5f, mBorderPaint)
                    it.drawRect(mBorderPaint.strokeWidth, mBorderPaint.strokeWidth, width - mBorderPaint.strokeWidth, height - mBorderPaint.strokeWidth, mPortraitPaint)
                }
            } else {
                if (mShowCircle) {
                    it.drawCircle(width / 2f, height / 2f, minOf(width, height) / 2f, mPortraitPaint)
                } else {
                    it.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mPortraitPaint)
                }
            }
            it.restore()
        }
    }


    fun dp2px(dpValue: Float): Int {
        return (context.resources.displayMetrics.density * dpValue + 0.5f).toInt()
    }

    fun setMeasureSize(measureSpec: Int, size: Float): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (mode) {
            MeasureSpec.EXACTLY -> return specSize
            MeasureSpec.AT_MOST -> return minOf(specSize, dp2px(size))
            MeasureSpec.UNSPECIFIED -> return dp2px(size)
            else -> return dp2px(size)
        }
    }
}