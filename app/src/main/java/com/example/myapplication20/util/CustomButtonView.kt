package com.example.myapplication20.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.example.myapplication20.R
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class CustomButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultAttrs: Int = R.attr.googleButtonsStyle,
    defStyle: Int = 0
) : View(context, attrs, defaultAttrs, defStyle) {

    init {
        initializeAttributes(attrs, defaultAttrs, defStyle)
    }

    private var _logoSize = 0f
    private var _space = 0f
    private var _textSize = 0f
    private var _painterWidth = 0f
    private var _cornerRadius = 0f
    private var _font: Typeface? = null
    private val arcs = GoogleArcs().getArcs()

    private val backgroundPainter = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val painter = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = _painterWidth
    }

    private val textPainter = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = Color.BLACK
        textSize = _textSize
        typeface = _font
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Recycle", "CustomViewStyleable", "ResourceType")
    private fun initializeAttributes(
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {

        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomGoogleButtonView,
            defStyleAttr,
            defStyleRes
        )

        _logoSize =
            typedArray.getDimension(R.styleable.CustomGoogleButtonView_logoSize, DEF_LOGO_SIZE)
        _space =
            typedArray.getDimension(R.styleable.CustomGoogleButtonView_space, DEF_SPACE)
        _cornerRadius =
            typedArray.getDimension(
                R.styleable.CustomGoogleButtonView_cornerRadius,
                DEF_CORNER_RADIUS
            )
        _textSize =
            typedArray.getDimension(
                R.styleable.CustomGoogleButtonView_customViewTextSize,
                DEF_TEXT_SIZE
            )
        _font =
            typedArray.getFont(R.styleable.CustomGoogleButtonView_customViewTextFont)

        _painterWidth = _logoSize / 5

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            val startX = getStartXCoordinate(width)
            val centerY = (height / 2).toFloat()
            val oval = createOval(startX, centerY)
            val textBoundRect = Rect()
            val backgroundRect = RectF(0f, 0f, width.toFloat(), height.toFloat())

            // draw view background
            drawRoundRect(backgroundRect, _cornerRadius, _cornerRadius, backgroundPainter)

            //draw line of letter G
            painter.color = arcs[3].paintColor
            drawLine(
                startX + _logoSize / 2,
                centerY,
                startX + _logoSize - _painterWidth / 4,
                centerY,
                painter
            )


            // draw oval of letter G
            arcs.forEach {
                painter.color = it.paintColor
                drawArc(oval, it.startAnge, it.sweepAngle, false, painter)
            }

            // draw text
            textPainter.getTextBounds(DEF_TEXT, 0, DEF_TEXT.length, textBoundRect)
            val textHeight = textBoundRect.height()
            canvas.drawText(
                DEF_TEXT,
                startX - _painterWidth / 2 + _logoSize + _space,
                centerY + textHeight / 2,
                textPainter
            )
        }
    }

    private fun getStartXCoordinate(width: Int): Float {
        val totalLength = textPainter.measureText(DEF_TEXT) + _space + _logoSize
        return width / 2 - totalLength / 2
    }

    private fun createOval(startX: Float, centerY: Float): RectF {
        val ovalSize = _logoSize - _painterWidth
        val left = startX + _painterWidth / 2
        val top = centerY - ovalSize / 2
        val right = startX + ovalSize + _painterWidth / 2
        val bottom = centerY + ovalSize / 2

        return RectF(left,top, right, bottom)
    }

    private companion object {
        private const val DEF_TEXT = "GOOGLE"
        private const val DEF_LOGO_SIZE = 20f
        private const val DEF_TEXT_SIZE = 16f
        private const val DEF_SPACE = 16f
        private const val DEF_CORNER_RADIUS = 10f


    }
}