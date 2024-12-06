package com.mojise.library.chocolate.view.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate._internal.exts.getColorOrNull
import com.mojise.library.chocolate.ext.dp
import com.mojise.library.chocolate._internal.TAG
import com.mojise.library.chocolate._internal.chocolate_button_background_disabled_color
import com.mojise.library.chocolate._internal.chocolate_button_text_disabled_color
import com.mojise.library.chocolate._internal.chocolate_ripple_color_black
import com.mojise.library.chocolate.view.ChocolateView
import com.mojise.library.chocolate.view.model.Attributes
import com.mojise.library.chocolate.view.model.ChocolateAndroidAttribute
import com.mojise.library.chocolate.view.model.ChocolateAttribute
import com.mojise.library.chocolate.view.model.ChocolateColorState
import com.mojise.library.chocolate.view.model.DrawablePosition
import com.mojise.library.chocolate.view.model.PressEffectStrength
import com.mojise.library.chocolate.view.util.ChocolateViewUtil

@SuppressLint("ResourceType")
internal object ChocolateViewHelper {

    const val NONE = 0
    const val NO_COLOR = Int.MAX_VALUE

    const val CORNER_RADIUS_NORMAL_PIXEL = 16f

    fun initAttributes(
        view: View,
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
    ): Attributes {
        //Log.e(TAG, "initAttributes")

        // Android(android.R.attr.xxx) 속성을 가져오기 위한 TypedArray
        val androidTypedArray = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.background, android.R.attr.clickable), defStyleAttr, defStyleRes)
        val androidAttribute = try {
            ChocolateAndroidAttribute(
                isClickable = androidTypedArray.getBoolean(1, true),
                background = androidTypedArray.getDrawable(0),
            )
        } catch (e: Exception) {
            ChocolateAndroidAttribute(
                isClickable = true,
                background = null,
            )
        } finally {
            androidTypedArray.recycle()
        }

        // Chocolate(R.styleable.xxx) 속성을 가져오기 위한 TypedArray
        val chocolateTypedArray = context.obtainStyledAttributes(attrs, R.styleable.chocolateViewAttributes)
        val chocolateAttribute = try {
            // 눌림 효과 속성
            val pressEffectStrengthInt = chocolateTypedArray.getInt(R.styleable.chocolateViewAttributes_chocolate_PressEffectStrengthLevel, PressEffectStrength.Normal.level)
            val pressEffectStrength = PressEffectStrength.valueOf(pressEffectStrengthInt)
            val pressEffectScaleRatio = chocolateTypedArray.getFloat(R.styleable.chocolateViewAttributes_chocolate_PressEffectScaleRatio, -1f)
                .takeIf { it in 0f..1f }
                ?: pressEffectStrength.value

            // 테두리 속성 (width > 0 이고, 색상이 하나 이상 지정된 경우에만 적용)
            val strokeWidth = chocolateTypedArray.getDimension(R.styleable.chocolateViewAttributes_chocolate_StrokeWidth, 0f)
            val strokeEnabledColor = chocolateTypedArray.getColorOrNull(R.styleable.chocolateViewAttributes_chocolate_StrokeColor)
            val strokeSelectedColor = chocolateTypedArray.getColorOrNull(R.styleable.chocolateViewAttributes_chocolate_StrokeSelectedColor)
            val strokeDisabledColor = chocolateTypedArray.getColorOrNull(R.styleable.chocolateViewAttributes_chocolate_StrokeDisabledColor)
            val strokeColors = if (strokeWidth > 0f && (strokeSelectedColor != null || strokeEnabledColor != null || strokeDisabledColor != null)) {
                ChocolateColorState(
                    enabledColor = strokeEnabledColor ?: Color.TRANSPARENT,
                    selectedColor = strokeSelectedColor ?: strokeEnabledColor ?: Color.TRANSPARENT,
                    disabledColor = strokeDisabledColor ?: view.chocolate_button_text_disabled_color,
                )
            } else {
                null
            }

            val backgroundEnabledColor = chocolateTypedArray.getColorOrNull(R.styleable.chocolateViewAttributes_chocolate_BackgroundColor)
            val backgroundSelectedColor = chocolateTypedArray.getColorOrNull(R.styleable.chocolateViewAttributes_chocolate_BackgroundSelectedColor)
            val backgroundDisabledColor = chocolateTypedArray.getColorOrNull(R.styleable.chocolateViewAttributes_chocolate_BackgroundDisabledColor)
            val backgroundColors = if (backgroundSelectedColor != null || backgroundEnabledColor != null || backgroundDisabledColor != null) {
                ChocolateColorState(
                    enabledColor = backgroundEnabledColor ?: Color.TRANSPARENT,
                    selectedColor = backgroundSelectedColor ?: backgroundEnabledColor ?: Color.TRANSPARENT,
                    disabledColor = backgroundDisabledColor ?: view.chocolate_button_background_disabled_color,
                )
            } else {
                null
            }

            ChocolateAttribute(
                isPressEffectEnabled = chocolateTypedArray.getBoolean(R.styleable.chocolateViewAttributes_chocolate_PressEffectEnabled, true),
                pressEffectScaleRatio = pressEffectScaleRatio,
                cornerRadius = chocolateTypedArray.getDimension(R.styleable.chocolateViewAttributes_chocolate_CornerRadius, CORNER_RADIUS_NORMAL_PIXEL.dp),

                ripplePosition = chocolateTypedArray.getInt(R.styleable.chocolateViewAttributes_chocolate_RippleApplyTo, DrawablePosition.Foreground.value)
                    .let(DrawablePosition::valueOf),
                rippleColors = ChocolateColorState.Transparent.apply {
                    enabledColor = chocolateTypedArray.getColor(R.styleable.chocolateViewAttributes_chocolate_RippleColor, view.chocolate_ripple_color_black)
                    selectedColor = chocolateTypedArray.getColor(R.styleable.chocolateViewAttributes_chocolate_RippleSelectedColor, NO_COLOR)
                        .takeIf { it != NO_COLOR }
                        ?: enabledColor
                    disabledColor = Color.TRANSPARENT
                },

                strokeWidth = strokeWidth,
                strokeColors = strokeColors,
                backgroundColors = backgroundColors,
            )
        } catch (e: Exception) {
            Log.w(TAG, "ChocolateAttribute error", e)
            ChocolateAttribute.Default
        } finally {
            chocolateTypedArray.recycle()
        }

        return Attributes(
            android = androidAttribute,
            chocolate = chocolateAttribute,
        )
    }

    fun setRippleBackgroundOrForeground(
        view: View,
        attributes: Attributes,
    ) {
        when (attributes.chocolate.ripplePosition) {
            DrawablePosition.Foreground -> {
                view.foreground = ChocolateViewUtil.generateRippleDrawable(
                    cornerRadius = attributes.chocolate.cornerRadius,
                    rippleColors = attributes.chocolate.rippleColors,
                    strokeWidth = attributes.chocolate.strokeWidth.toInt(),
                    strokeColors = attributes.chocolate.strokeColors,
                    backgroundColors = null,
                )
                view.background = attributes.android.background
                    ?: ChocolateViewUtil.generateDrawable(
                        cornerRadius = attributes.chocolate.cornerRadius,
                        strokeWidth = attributes.chocolate.strokeWidth.toInt(),
                        strokeColors = attributes.chocolate.strokeColors,
                        backgroundColors = attributes.chocolate.backgroundColors,
                    )
            }
            DrawablePosition.Background -> {
                view.background = ChocolateViewUtil.generateRippleDrawable(
                    cornerRadius = attributes.chocolate.cornerRadius,
                    rippleColors = attributes.chocolate.rippleColors,
                    strokeWidth = attributes.chocolate.strokeWidth.toInt(),
                    strokeColors = attributes.chocolate.strokeColors,
                    backgroundColors = attributes.chocolate.backgroundColors,
                )
            }
        }
    }

    /**
     * [ChocolateView]에서 사용되는 뷰의 터치 이벤트 발생 시, 눌림 효과 애니메이션
     */
    fun showPressEffectOnTouch(
        view: View,
        event: MotionEvent?,
        pressEffectScaleRatio: Float,
    ) {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> animateScale(view, true, pressEffectScaleRatio)
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> animateScale(view, false, pressEffectScaleRatio)
        }
    }

    private fun animateScale(
        view: View,
        isDown: Boolean,
        scaleRatio: Float,
    ) {
        view.animate()
            //.setInterpolator(AccelerateDecelerateInterpolator())
            .setInterpolator(DecelerateInterpolator())
            .setDuration(100)
            .scaleX(if (isDown) scaleRatio else 1f)
            .scaleY(if (isDown) scaleRatio else 1f)
            .withEndAction {
                if (isDown) view.scaleX = scaleRatio else view.scaleX = 1f
                if (isDown) view.scaleY = scaleRatio else view.scaleY = 1f
            }
            .start()
    }
}