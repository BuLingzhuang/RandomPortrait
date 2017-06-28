package com.bulingzhuang.randomportrait

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
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
    var colorList = listOf(R.color.red500, R.color.pink500, R.color.purple500, R.color.deeppurple500,
            R.color.indigo500, R.color.blue500, R.color.lightblue500, R.color.cyan500,
            R.color.teal500, R.color.green500, R.color.lightgreen500, R.color.lime500,
            R.color.yellow500, R.color.amber500, R.color.orange500, R.color.deeporange500,
            R.color.brown500, R.color.grey500, R.color.bluegrey500, R.color.red400, R.color.pink400, R.color.purple400, R.color.deeppurple400,
            R.color.indigo400, R.color.blue400, R.color.lightblue400, R.color.cyan400,
            R.color.teal400, R.color.green400, R.color.lightgreen400, R.color.lime400,
            R.color.yellow400, R.color.amber400, R.color.orange400, R.color.deeporange400,
            R.color.brown400, R.color.grey400, R.color.bluegrey400, R.color.red300, R.color.pink300, R.color.purple300, R.color.deeppurple300,
            R.color.indigo300, R.color.blue300, R.color.lightblue300, R.color.cyan300,
            R.color.teal300, R.color.green300, R.color.lightgreen300, R.color.lime300,
            R.color.yellow300, R.color.amber300, R.color.orange300, R.color.deeporange300,
            R.color.brown300, R.color.grey300, R.color.bluegrey300, R.color.red200, R.color.pink200, R.color.purple200, R.color.deeppurple200,
            R.color.indigo200, R.color.blue200, R.color.lightblue200, R.color.cyan200,
            R.color.teal200, R.color.green200, R.color.lightgreen200, R.color.lime200,
            R.color.yellow200, R.color.amber200, R.color.orange200, R.color.deeporange200,
            R.color.brown200, R.color.grey200, R.color.bluegrey200, R.color.red800, R.color.pink800, R.color.purple800, R.color.deeppurple800,
            R.color.indigo800, R.color.blue800, R.color.lightblue800, R.color.cyan800,
            R.color.teal800, R.color.green800, R.color.lightgreen800, R.color.lime800,
            R.color.yellow800, R.color.amber800, R.color.orange800, R.color.deeporange800,
            R.color.brown800, R.color.grey800, R.color.bluegrey800, R.color.red600, R.color.pink600, R.color.purple600, R.color.deeppurple600,
            R.color.indigo600, R.color.blue600, R.color.lightblue600, R.color.cyan600,
            R.color.teal600, R.color.green600, R.color.lightgreen600, R.color.lime600,
            R.color.yellow600, R.color.amber600, R.color.orange600, R.color.deeporange600,
            R.color.brown600, R.color.grey600, R.color.bluegrey600, R.color.red700, R.color.pink700, R.color.purple700, R.color.deeppurple700,
            R.color.indigo700, R.color.blue700, R.color.lightblue700, R.color.cyan700,
            R.color.teal700, R.color.green700, R.color.lightgreen700, R.color.lime700,
            R.color.yellow700, R.color.amber700, R.color.orange700, R.color.deeporange700,
            R.color.brown700, R.color.grey700, R.color.bluegrey700)
    var color: Int = 0

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
        if (color == 0) {
            mBorderPaint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        } else {
            mBorderPaint.color = ContextCompat.getColor(context, color)
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
        if (color == 0) {
            piecePaint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        } else {
            piecePaint.color = ContextCompat.getColor(context, color)
        }
        if (color == 0) {
            mBorderPaint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        } else {
            mBorderPaint.color = ContextCompat.getColor(context, color)
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

        val width = setMeasureSize(widthMeasureSpec, 233 / 2f)
        val height = setMeasureSize(heightMeasureSpec, 233 / 2f)
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
                Log.e("blz","111")
                if (mShowCircle) {
                    it.drawCircle(width / 2f, height / 2f, (minOf(width, height) - mBorderPaint.strokeWidth) / 2 - 1f, mBorderPaint)
                    it.drawCircle(width / 2f, height / 2f, minOf(width, height) / 2f, mPortraitPaint)
                } else {
                    it.drawRect(mBorderPaint.strokeWidth / 2, mBorderPaint.strokeWidth / 2, width - mBorderPaint.strokeWidth / 2 - 0.5f, height - mBorderPaint.strokeWidth / 2 - 0.5f, mBorderPaint)
                    it.drawRect(mBorderPaint.strokeWidth, mBorderPaint.strokeWidth, width - mBorderPaint.strokeWidth, height - mBorderPaint.strokeWidth, mPortraitPaint)
                }
            } else {
                Log.e("blz","222")
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