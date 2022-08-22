package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.withStyledAttributes
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var firstBtnColor = 0
    private var secondBtnColor = 0
    private var btnLabel: String
    private var textColor = 0
    private var isAnimationPlayed = false
    private var width = 0f
    private var startAngle = 0f
    private var endAngle = 0f




    private var valueAnimator = ValueAnimator()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = 55.0f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        when(new){

            ButtonState.Loading -> {
                animateRect()
                animateCircle()
            }

            ButtonState.Clicked->{

                btnLabel = resources.getString(R.string.loading)
                isAnimationPlayed = true
                invalidate()
                setLoadingState(ButtonState.Loading)

            }

            ButtonState.Completed->{
                btnLabel = resources.getString(R.string.download)
                isAnimationPlayed = false
                paint.color  = firstBtnColor
                invalidate()
            }



        }

    }


    init {

        context.withStyledAttributes(attrs,R.styleable.LoadingButton){
            firstBtnColor = getColor(R.styleable.LoadingButton_firstButtonColor,0)
            secondBtnColor = getColor(R.styleable.LoadingButton_secondButtonColor,0)
            textColor = getColor(R.styleable.LoadingButton_textColor,0)
        }
        btnLabel =resources.getString(R.string.download)

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = firstBtnColor
        paint.textAlign = Paint.Align.CENTER
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
        canvas.drawColor(paint.color)
        paint.color = textColor
        canvas.drawText(btnLabel, widthSize.toFloat() / 2, heightSize.toFloat() / 2, paint)
        if (isAnimationPlayed) {
            paint.color = secondBtnColor
            canvas.drawRect(0f, 0f, width, measuredHeight.toFloat(), paint)
            paint.color = textColor
            canvas.drawText(btnLabel, widthSize.toFloat() / 2, heightSize.toFloat() / 2, paint)
            paint.color = Color.YELLOW
            canvas.drawArc(
                (widthSize - 180f),
                (heightSize / 2) - 50f,
                (widthSize - 100f),
                (heightSize / 2) + 50f,
                startAngle, endAngle, true, paint
            )
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

    private fun animateRect(){
        valueAnimator = ValueAnimator.ofFloat(0F, measuredWidth.toFloat()).apply {
            duration = 1500
            addUpdateListener { valueAnimator ->
                width = valueAnimator.animatedValue as Float
                valueAnimator.repeatCount = ValueAnimator.INFINITE
                valueAnimator.repeatMode = ValueAnimator.REVERSE
                valueAnimator.interpolator = AccelerateInterpolator()
                invalidate()
            }
            start()
        }
    }
    private fun animateCircle(){
        valueAnimator = ValueAnimator.ofFloat(0f, 360f)
            .apply {
                duration = 1500
                addUpdateListener { animator ->
                    endAngle = animator.animatedValue as Float
                    animator.interpolator = AccelerateInterpolator()
                    invalidate()
                }
                start()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        custom_button.isEnabled = false
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        custom_button.isEnabled = true
                        setLoadingState(ButtonState.Completed)
                    }
                })
            }
    }

     fun setLoadingState(btnState: ButtonState){
        buttonState = btnState
    }


}