package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0


    // Animation
    private val valueAnimator = ValueAnimator.ofFloat(0f,1f)
    private var progress = 0f
    private var downloadButtonText = resources.getString(R.string.button_name)


    // painting
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.GREEN
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)

    }


    // y-axis centered text
    private val textHeight = paint.descent() - paint.ascent()
    private val textOffset = textHeight / 2 - paint.descent()

    // circle properties
    private val circlePosLeft by lazy{0.7f*widthSize.toFloat()}
    private val circlePosTop by lazy{0.3f*heightSize.toFloat()}
    private val circleDiameter = 100.0f


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            //ButtonState.Clicked -> {downloadButtonText=resources.getString(R.string.button_name)}
            ButtonState.Loading -> {setProgress()}
            ButtonState.Completed -> {downloadButtonText=resources.getString(R.string.button_completed)}

        }

    }


    val rect by lazy {
        RectF(0f, 0f, widthSize.toFloat(), heightSize.toFloat())
    }

    val circle by lazy {
        RectF(circlePosLeft, circlePosTop, circlePosLeft+circleDiameter, circlePosTop+circleDiameter)
    }

    init {

    }

    private fun setProgress() {
        valueAnimator.apply{
            duration = 1000
            addUpdateListener{
                progress = animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun showRecLoadingState(canvas: Canvas) {
        paint.color = Color.parseColor("#0055FF")
        downloadButtonText = resources.getString(R.string.button_loading)
        canvas.drawRect(0f,0f, widthSize*progress, heightSize.toFloat(),paint)
    }

    private fun showCircleLoadingState(canvas: Canvas) {
        paint.color = Color.YELLOW
        canvas.drawArc(circle,0f,360f*progress,true,paint)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply{
            paint.color = Color.GREEN
            drawRect(rect, paint)
            if (buttonState == ButtonState.Loading) {
                showRecLoadingState(canvas)
                showCircleLoadingState(canvas)
            }
            paint.color = Color.RED
            drawText(downloadButtonText,widthSize.toFloat() / 2,heightSize.toFloat() / 2 + textOffset, paint)
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    fun updateButtonState(state: ButtonState) {
        buttonState = state
    }



}