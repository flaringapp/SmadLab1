package com.flaringapp.app.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.View.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import com.flaringapp.app.utils.ViewUtils.animateHide
import com.flaringapp.app.utils.ViewUtils.animateShow
import com.flaringapp.smadlab1.app.Constants.ANIM_DURATION


object ViewUtils {

    fun animateShow(view: View) {
        if (view.visibility == INVISIBLE || view.visibility == GONE) {
            view.visibility = VISIBLE
            view.alpha = 0f
        }

        animateAlpha(
            view,
            1f,
            object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    view.visibility = VISIBLE
                }
            }
        )
    }

    fun animateHide(view: View) {
        animateAlpha(
            view,
            0f,
            object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = GONE
                }
            },
            AccelerateInterpolator()
        )
    }

    fun animateAlpha(
        view: View,
        to: Float,
        listener: Animator.AnimatorListener? = null,
        interpolator: Interpolator? = null
    ) {
        val animator = ValueAnimator.ofFloat(view.alpha, to)
            .apply {
                duration = ANIM_DURATION
                this.interpolator = interpolator
                addListener(listener)
                addUpdateListener { view.alpha = it.animatedValue as Float }
                setTarget(view)
            }

        view.post {
            view.animate().cancel()
            animator.start()
        }
    }
}

fun View.gone(animate: Boolean = true) {
    if (animate) animateHide(this)
    else visibility = GONE
}

fun View.show(animate: Boolean = true) {
    if (animate) animateShow(this)
    else visibility = VISIBLE.also { alpha = 1f }
}

fun Context.dp(dp: Float): Float {
    return resources.dp(dp)
}

fun Context.dp(dp: Int): Float {
    return resources.dp(dp)
}

fun Resources.dp(dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)
}

fun Resources.dp(dp: Int): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics)
}