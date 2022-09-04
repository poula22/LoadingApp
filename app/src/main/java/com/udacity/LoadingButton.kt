package com.udacity

import android.R
import android.animation.*
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt
import kotlin.properties.Delegates

@SuppressLint("ObjectAnimatorBinding")
class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonText: String
    private var progress: Float = 0f
    private var valueAnimator = ValueAnimator()

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        when(new){
            ButtonState.Loading ->{
                setText("We are loading")
                setBackgroundColor(Color.CYAN)
                invalidate()
                requestLayout()
                valueAnimator= ValueAnimator.ofFloat(0f, 1f).apply {
                    addUpdateListener {
                        progress = animatedValue as Float
                        invalidate()
                    }
                    duration = 3000
                    start()
                }
                isEnabled=false

            }
            ButtonState.Completed ->{
                setText("Download")
                setBackgroundColor(Color.GREEN)
                invalidate()
                requestLayout()
                valueAnimator.cancel()
                progress=0f
                isEnabled=true
            }
            ButtonState.Clicked->{

            }
        }
        invalidate()

    }

    private fun setText(text: String) {
        buttonText=text
        invalidate()
        requestLayout()
    }


    init {
        isClickable = true
        buttonText="Download"

    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 100f
        color = Color.WHITE
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
    }

    private val inProgressBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.CYAN
    }

    private val inProgressArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
    }

//    override fun performClick(): Boolean {
//        if(super.performClick()) return true
//        val startColor = Color.GREEN
//        val endColor= Color.RED
//        valueAnimator = ValueAnimator.ofObject(ArgbEvaluator(),startColor,endColor)
//        valueAnimator.duration=1000
//        valueAnimator.addUpdateListener {
//            buttonState=ButtonState.Loading
//            setBackgroundColor(it.animatedValue as Int)
//            setText("loading")
//        }
//        valueAnimator.start()
//        return true
//    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val backgroundWidth = measuredWidth.toFloat()
        val backgroundHeight = measuredHeight.toFloat()
        canvas.drawColor(Color.GREEN)
        canvas.drawRoundRect(0f, 0f, backgroundWidth, backgroundHeight, 15f, 15f, backgroundPaint)
        val centerX = measuredWidth.toFloat() / 2
        val centerY = measuredHeight.toFloat() / 2

        if (buttonState == ButtonState.Loading) {
            var progressVal = progress * measuredWidth.toFloat()
            canvas.drawRoundRect(0f, 0f, progressVal, backgroundHeight, 10f, 10f, inProgressBackgroundPaint)

            val r = 20f
            val arcRectSize = measuredHeight.toFloat() - paddingBottom.toFloat() - r

            progressVal = progress * 360f
            canvas.drawArc(measuredWidth-(8*r),
                paddingTop.toFloat() + r,
                measuredWidth-r,
                arcRectSize,
                0f,
                progressVal,
                true,
                inProgressArcPaint)
        }
        canvas.drawText(buttonText,centerX, centerY, textPaint)


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

}