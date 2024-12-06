package com.mojise.library.chocolate.view.util

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import com.mojise.library.chocolate.view.model.ChocolateColorState

internal object ChocolateViewUtil {

    fun generateDrawable(
        cornerRadius: Float,
        strokeWidth: Int,
        strokeColors: ChocolateColorState?,
        backgroundColors: ChocolateColorState?,
    ) = GradientDrawable().also {
        it.shape = GradientDrawable.RECTANGLE
        it.cornerRadius = cornerRadius
        it.color = backgroundColors?.toColorStateList()

        if (strokeWidth >= 0 && strokeColors != null) {
            it.setStroke(strokeWidth, strokeColors.toColorStateList())
        }
    }

    fun generateRippleDrawable(
        cornerRadius: Float,
        strokeWidth: Int,
        rippleColors: ChocolateColorState,
        strokeColors: ChocolateColorState?,
        backgroundColors: ChocolateColorState?,
    ): RippleDrawable {
        val maskDrawable = GradientDrawable().also {
            it.shape = GradientDrawable.RECTANGLE
            it.color = rippleColors.toColorStateList()
            it.cornerRadius = cornerRadius

            if (strokeWidth >= 0 && strokeColors != null) {
                it.setStroke(strokeWidth, strokeColors.toColorStateList())
            }
        }
        val backgroundDrawable = if (backgroundColors != null) {
            GradientDrawable().also {
                it.shape = GradientDrawable.RECTANGLE
                it.color = backgroundColors.toColorStateList()
                it.cornerRadius = cornerRadius

                if (strokeWidth >= 0 && strokeColors != null) {
                    it.setStroke(strokeWidth, strokeColors.toColorStateList())
                }
            }
        } else {
            null
        }
        return RippleDrawable(
            rippleColors.toColorStateList(),
            backgroundDrawable,
            maskDrawable,
        )
    }
}