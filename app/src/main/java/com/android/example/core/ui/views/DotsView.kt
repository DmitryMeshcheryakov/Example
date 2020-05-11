package com.android.example.core.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.android.example.R

class DotsView : LinearLayout {

    @DrawableRes
    private var enabledDotResId: Int = R.drawable.bg_dot_enabled
    @DrawableRes
    private var disabledDotResId: Int = R.drawable.bg_dot_disabled

    private var dotMargin = 0 //dp
    private var dotSize = 50
    private var dots: List<ImageView> = emptyList()

    var dotCount: Int = 4
        set(value) {
            field = value
            createImages(value, dotSize, dotMargin)
        }


    constructor(context: Context): super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    fun setDots(count: Int) {
        for (i in 0 until count)
            dots[i].setImageResource(enabledDotResId)

        for (i in count until dotCount)
            dots[i].setImageResource(disabledDotResId)
    }

    private fun init(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DotsView,
            0, 0).apply {
            try {
                dotMargin = getDimensionPixelOffset(R.styleable.DotsView_dotMargin, 0)
                dotSize = getDimensionPixelSize(R.styleable.DotsView_dotHeight, 50)
                enabledDotResId = getResourceId(R.styleable.DotsView_enabledDotSrc,  R.drawable.bg_dot_enabled)
                disabledDotResId = getResourceId(R.styleable.DotsView_disabledDotSrc,  R.drawable.bg_dot_disabled)
                dotCount = getInteger(R.styleable.DotsView_dotCount, 4)
            } finally {
                recycle()
            }
        }
    }

    private fun createImages(dotCount: Int, sizePx: Int, marginPx: Int) {
        removeAllViews()
        dots = emptyList()
        for (i in 0 until dotCount)
            dots = dots.plus(createImageView(sizePx, marginPx))

        dots.forEach { addView(it) }
    }

    private fun createImageView(sizePx: Int, marginPx: Int) : ImageView {
        return ImageView(context).apply {
            setImageResource(disabledDotResId)
            layoutParams = LayoutParams(sizePx, sizePx).apply {
                gravity = Gravity.CENTER
                setMargins(marginPx, marginPx, marginPx, marginPx)
            }
        }
    }
}