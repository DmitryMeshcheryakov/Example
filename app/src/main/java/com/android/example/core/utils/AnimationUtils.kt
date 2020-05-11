package com.android.example.core.utils

import android.animation.Animator
import android.content.Context
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.LayoutAnimationController
import android.view.animation.Transformation
import androidx.annotation.AnimRes
import kotlin.math.hypot

object AnimationUtils {

    private const val DEFAULT_ANIM_DURATION = 200L

    const val POS_MIDDLE = -1
    const val POS_START = -2
    const val POS_END = -3

    fun expand(
        view: View?,
        duration: Long = DEFAULT_ANIM_DURATION,
        callback: (() -> Unit)? = null
    ) {
        view?.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = view?.measuredHeight ?: 0

        view?.layoutParams?.height = 0
        view?.requestLayout()
        view?.visibility = View.VISIBLE
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                view?.layoutParams?.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                view?.requestLayout()
            }

            override fun willChangeBounds(): Boolean = true
        }.apply {
            this.interpolator = DecelerateInterpolator(2f)
            this.duration = duration
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) { callback?.invoke() }
            })
        }
        view?.startAnimation(a)
    }

    fun collapse(
        view: View?,
        duration: Long = DEFAULT_ANIM_DURATION,
        callback: (() -> Unit)? = null
    ) {
        val initialHeight = view?.measuredHeight ?: 0

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    view?.visibility = View.GONE
                } else {
                    view?.layoutParams?.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    view?.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean = true
        }.apply {
            this.interpolator = DecelerateInterpolator(2f)
            this.duration = duration
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) { callback?.invoke() }
            })
        }

        view?.startAnimation(a)
    }

    fun rotate(
        view: View?,
        degrees: Float = 0f,
        duration: Long = DEFAULT_ANIM_DURATION,
        callback: (() -> Unit)? = null
    ) {
        view?.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val viewRotation = view?.rotation ?: 0f
        val resultRotation = degrees - viewRotation
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                view?.rotation = viewRotation + (resultRotation * interpolatedTime)
                view?.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return false
            }
        }.apply {
            this.interpolator = DecelerateInterpolator(2f)
            this.duration = duration
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    callback?.invoke()
                }
            })
        }
        view?.clearAnimation()
        view?.startAnimation(a)
    }

    fun scale(
        view: View?,
        scaleX: Float = 1f,
        scaleY: Float = scaleX,
        duration: Long = DEFAULT_ANIM_DURATION,
        callback: (() -> Unit)? = null
    ) {
        view?.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val viewScaleX = (view?.scaleX ?: 1f)
        val viewScaleY = (view?.scaleY ?: 1f)

        val resultScaleX = scaleX - viewScaleX
        val resultScaleY = scaleY - viewScaleY
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                view?.scaleX = viewScaleX + (resultScaleX * interpolatedTime)
                view?.scaleY = viewScaleY + (resultScaleY * interpolatedTime)
                view?.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return false
            }

        }.apply {
            this.interpolator = DecelerateInterpolator(2f)
            this.duration = duration
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    callback?.invoke()
                }
            })
        }
        view?.clearAnimation()
        view?.startAnimation(a)
    }

    fun alpha(
        view: View?,
        alpha: Float = 1f,
        duration: Long = DEFAULT_ANIM_DURATION,
        callback: (() -> Unit)? = null
    ) {
        view?.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val viewAlpha = (view?.alpha ?: 1f)
        val resultAlpha = alpha - viewAlpha

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                view?.alpha = viewAlpha + (resultAlpha * interpolatedTime)
                view?.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return false
            }

        }.apply {
            this.interpolator = DecelerateInterpolator(2f)
            this.duration = duration
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    callback?.invoke()
                }
            })
        }
        view?.clearAnimation()
        view?.startAnimation(a)
    }

    fun translate(
        view: View?,
        translateX: Float = 0f,
        translateY: Float = 0f,
        duration: Long = DEFAULT_ANIM_DURATION,
        callback: (() -> Unit)? = null
    ) {
        view?.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val viewX = (view?.translationX ?: 0f)
        val viewY = (view?.translationY ?: 0f)
        val resultX = translateX - viewX
        val resultY = translateY - viewY

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                view?.translationX = viewX + (resultX * interpolatedTime)
                view?.translationY = viewY + (resultY * interpolatedTime)
                view?.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return false
            }

        }.apply {
            this.interpolator = DecelerateInterpolator(2f)
            this.duration = duration
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    callback?.invoke()
                }
            })
        }
        view?.clearAnimation()
        view?.startAnimation(a)
    }

    fun set(
        view: View?,
        rotation: Float = 0f,
        scaleX: Float = 1f,
        scaleY: Float = scaleX,
        translateX: Float = 0f,
        translateY: Float = 0f,
        alpha: Float = 1f,
        duration: Long = DEFAULT_ANIM_DURATION,
        callback: (() -> Unit)? = null
    ) {
        view?.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val viewRotation = view?.rotation ?: 0f
        val viewAlpha = (view?.alpha ?: 1f)
        val viewScaleX = (view?.scaleX ?: 1f)
        val viewScaleY = (view?.scaleY ?: 1f)
        val viewX = (view?.translationX ?: 0f)
        val viewY = (view?.translationY ?: 0f)

        val resultRotation = rotation - viewRotation
        val resultAlpha = alpha - viewAlpha
        val resultScaleX = scaleX - viewScaleX
        val resultScaleY = scaleY - viewScaleY
        val resultX = translateX - viewX
        val resultY = translateY - viewY

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                view?.alpha = viewAlpha + (resultAlpha * interpolatedTime)
                view?.scaleX = viewScaleX + (resultScaleX * interpolatedTime)
                view?.scaleY = viewScaleY + (resultScaleY * interpolatedTime)
                view?.rotation = viewRotation + (resultRotation * interpolatedTime)
                view?.translationX = viewX + (resultX * interpolatedTime)
                view?.translationY = viewY + (resultY * interpolatedTime)
                view?.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return false
            }

        }.apply {
            this.interpolator = DecelerateInterpolator(2f)
            this.duration = duration
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    callback?.invoke()
                }
            })
        }
        view?.clearAnimation()
        view?.startAnimation(a)
    }

    fun resetSet(view: View?, duration: Long = DEFAULT_ANIM_DURATION, callback: (() -> Unit)? = null) {
        set(view, duration = duration, callback = callback)
    }

    fun loadAnim(context: Context?, @AnimRes id: Int): LayoutAnimationController {
        return android.view.animation.AnimationUtils.loadLayoutAnimation(context, id)
    }

    fun expandCircular(
        view: View?,
        posX: Int = POS_MIDDLE,
        posY: Int = POS_MIDDLE,
        duration: Long = DEFAULT_ANIM_DURATION,
        callback: (() -> Unit)? = null
    ) {
        view?.let { v ->
            v.clearAnimation()
            val cx = when (posX) {
                POS_START -> 0
                POS_END -> v.width
                else -> v.width / 2
            }
            val cy = when (posY) {
                POS_START -> 0
                POS_END -> v.height
                else -> v.height / 2
            }

            val finalRadius = hypot(
                if (posX == POS_START) v.width.toDouble() else cx.toDouble(),
                if (posY == POS_START) v.height.toDouble() else cy.toDouble()
            ).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, finalRadius)
            anim.interpolator = DecelerateInterpolator(2f)
            anim.duration = duration
            v.visibility = View.VISIBLE

            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) { callback?.invoke() }
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationStart(animator: Animator) {}
            })
            anim.start()
        }
    }

    fun collapseCircular(
        view: View?,
        posX: Int = POS_MIDDLE,
        posY: Int = POS_MIDDLE,
        duration: Long = DEFAULT_ANIM_DURATION,
        callback: (() -> Unit)? = null
    ) {
        view?.let { v ->
            v.clearAnimation()
            val cx = when (posX) {
                POS_START -> 0
                POS_END -> v.width
                else -> v.width / 2
            }
            val cy = when (posY) {
                POS_START -> 0
                POS_END -> v.height
                else -> v.height / 2
            }

            val finalRadius = hypot(
                if (posX == POS_START) v.width.toDouble() else cx.toDouble(),
                if (posY == POS_START) v.height.toDouble() else cy.toDouble()
            ).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, finalRadius, 0f)
            anim.interpolator = DecelerateInterpolator(2f)
            anim.duration = duration
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    v.visibility = View.INVISIBLE
                    callback?.invoke()
                }
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationStart(animator: Animator) {}
            })
            anim.start()
        }
    }
}

val View.isCollapsedCircular: Boolean
    get() = this.visibility == View.INVISIBLE

val View.isCollapsed: Boolean
    get() = this.visibility == View.GONE

fun View.expandCircular(posX: Int = AnimationUtils.POS_MIDDLE,
                        posY: Int = posX,
                        callback: (() -> Unit)? = null) = AnimationUtils.expandCircular(this, posX, posY, callback = callback)

fun View.collapseCircular(posX: Int = AnimationUtils.POS_MIDDLE,
                          posY: Int = posX,
                          callback: (() -> Unit)? = null) = AnimationUtils.collapseCircular(this, posX, posY, callback = callback)

fun View.expand(callback: (() -> Unit)? = null) = AnimationUtils.expand(this, callback = callback)

fun View.collapse(callback: (() -> Unit)? = null) = AnimationUtils.collapse(this, callback = callback)

fun View.animateAlphaTo(alpha: Float, callback: (() -> Unit)? = null) =
    AnimationUtils.alpha(this, alpha, callback = callback)

fun View.animateTranslationTo(x: Float, y: Float, callback: (() -> Unit)? = null) =
    AnimationUtils.translate(this, x, y, callback = callback)

fun View.animateScaleTo(scale: Float, callback: (() -> Unit)? = null) =
    AnimationUtils.scale(this, scale, callback = callback)

fun View.animateRotationTo(rotation: Float, callback: (() -> Unit)? = null) =
    AnimationUtils.rotate(this, rotation, callback = callback)

fun View.resetAnimations(callback: (() -> Unit)? = null) = AnimationUtils.resetSet(this, callback = callback)