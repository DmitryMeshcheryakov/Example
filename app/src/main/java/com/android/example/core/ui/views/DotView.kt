package com.android.example.core.ui.views

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.android.example.R

class DotView : LinearLayout {

    private val animationDuration = 200L
    private var isLocked = false

    @DrawableRes
    private var dotIcon: Int = R.drawable.bg_dot_enabled
    private var dotMargin = 8
    private var dotSize = 24
    private var currentDots: Int = 0

    var dotCount: Int = 4

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DotsView,
            0, 0
        ).apply {
            try {
                dotMargin = getDimensionPixelOffset(R.styleable.DotsView_dotMargin, 8)
                dotSize = getDimensionPixelSize(R.styleable.DotsView_dotHeight, 24)
                dotIcon = getResourceId(R.styleable.DotsView_enabledDotSrc, R.drawable.bg_dot_enabled)
                dotCount = getInteger(R.styleable.DotsView_dotCount, 4)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                recycle()
            }
        }
        this.gravity = Gravity.CENTER
    }

    fun setDots(count: Int) {
        if (count == 0) {
            currentDots = 0
            clear()
            return
        }
        val old = currentDots
        currentDots = count

        val c = count - old
        when {
            c > 0 -> for (i in 0 until c) addDot(true)
            c < 0 -> for (i in 0 until -c) removeDot(true)
        }
    }

    private fun addDot(withAnimation: Boolean = true) {

        if (!isLocked) {
            var view: View? = null

            for (i in childCount - 1 downTo 0) {
                val v = getChildAt(i)
                if (v?.isActivated == true) {
                    view = v
                    view.isActivated = false
                    break
                }
            }

            val count = if (view == null) childCount else childCount - 1
            if (count < dotCount) {
                if (view == null) {
                    view = createImageView(
                        if (withAnimation) 1 else dotSize,
                        if (withAnimation) 0 else dotMargin
                    )
                    addView(view)
                }

                if (withAnimation) {
                    val params = view.layoutParams as? LayoutParams?
                    val animation = object : Animation() {
                        override fun applyTransformation(
                            interpolatedTime: Float,
                            t: Transformation
                        ) {
                            val margin = (interpolatedTime * dotMargin).toInt()
                            val size = (interpolatedTime * dotSize).toInt()
                            params?.height = size
                            params?.width = size
                            params?.setMargins(margin, margin, margin, margin)
                            params?.let { view.layoutParams = it }
                        }

                        override fun willChangeBounds(): Boolean = true
                    }
                    animation.duration = animationDuration
                    animation.interpolator = DecelerateInterpolator(2f)
                    view.startAnimation(animation)
                }
            }
        }
    }

    private fun removeDot(withAnimation: Boolean = true) {
        if (!isLocked) {
            var view: View? = null

            for (i in childCount - 1 downTo 0) {
                view = getChildAt(i)
                if (view?.isActivated == false)
                    break
            }

            if (withAnimation) {
                view?.isActivated = true

                val params = view?.layoutParams as? LayoutParams?
                val currentSize = params?.height ?: dotSize
                val currentMargin = params?.marginStart ?: dotMargin
                val duration =
                    currentSize.toFloat() / dotSize.toFloat() * animationDuration.toFloat()

                val animation = object : Animation() {
                    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                        val margin = ((1 - interpolatedTime) * currentMargin).toInt()
                        val size = ((1 - interpolatedTime) * currentSize).toInt()
                        params?.height = size
                        params?.width = size
                        params?.setMargins(margin, margin, margin, margin)
                        params?.let { view?.layoutParams = it }
                    }

                    override fun willChangeBounds(): Boolean = true
                }
                animation.duration = duration.toLong()
                animation.interpolator = DecelerateInterpolator(2f)
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        removeView(view)
                    }
                })
                view?.startAnimation(animation)
            } else {
                removeView(view)
            }
        }
    }

    private fun clear() {
        if (!isLocked) {
            for (i in 0..childCount) {
                Handler().postDelayed(
                    { removeDot() },
                    (animationDuration.toFloat() / (dotCount * 2).toFloat()).toLong() * i
                )
            }
        }
    }

    private fun createImageView(sizePx: Int, marginPx: Int): ImageView {
        return ImageView(context).apply {
            layoutParams = LayoutParams(sizePx, sizePx).apply {
                gravity = Gravity.CENTER
                setMargins(marginPx, marginPx, marginPx, marginPx)
            }
            setImageResource(dotIcon)
            isActivated = false
        }
    }

}