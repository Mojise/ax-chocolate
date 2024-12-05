package com.mojise.library.chocolate.view.helper

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.app.getChocolateThemeColor
import com.mojise.library.chocolate.button.box.ChocolateBoxButton
import com.mojise.library.chocolate.ext.dp
import com.mojise.library.chocolate.util.TAG
import com.mojise.library.chocolate.view.ChocolateView
import com.mojise.library.chocolate.view.model.Attributes
import com.mojise.library.chocolate.view.model.ChocolateAndroidAttribute
import com.mojise.library.chocolate.view.model.ChocolateAttribute
import com.mojise.library.chocolate.view.model.ChocolateColorState
import com.mojise.library.chocolate.view.model.DrawablePosition
import com.mojise.library.chocolate.view.model.PressEffectStrength

@SuppressLint("ResourceType")
internal object ChocolateViewHelper {

    const val NONE = 0
    const val NO_COLOR = Int.MAX_VALUE

    const val CORNER_RADIUS_NORMAL_PIXEL = 16f

    fun initAttributes(
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
            val pressEffectStrengthInt = chocolateTypedArray.getInt(R.styleable.chocolateViewAttributes_chocolate_PressEffectStrengthLevel, -1)
            val pressEffectStrength = PressEffectStrength.valueOf(pressEffectStrengthInt)
            val pressEffectScaleRatio = pressEffectStrength?.value
                ?: chocolateTypedArray.getFloat(R.styleable.chocolateViewAttributes_chocolate_PressEffectScaleRatio, PressEffectStrength.Normal.value)

            ChocolateAttribute(
                isPressEffectEnabled = chocolateTypedArray.getBoolean(R.styleable.chocolateViewAttributes_chocolate_PressEffectEnabled, true),
                pressEffectScaleRatio = pressEffectScaleRatio,
                cornerRadius = chocolateTypedArray.getDimension(R.styleable.chocolateViewAttributes_chocolate_CornerRadius, CORNER_RADIUS_NORMAL_PIXEL.dp),

                ripplePosition = chocolateTypedArray.getInt(R.styleable.chocolateViewAttributes_chocolate_RippleApplyTo, DrawablePosition.Foreground.value)
                    .let(DrawablePosition::valueOf),
                rippleColors = ChocolateColorState().apply {
                    enabledColor = chocolateTypedArray.getColor(R.styleable.chocolateViewAttributes_chocolate_RippleColor, context.getChocolateThemeColor(R.color.chocolate_ripple_color_black))
                    selectedColor = chocolateTypedArray.getColor(R.styleable.chocolateViewAttributes_chocolate_RippleSelectedColor, NO_COLOR)
                        .takeIf { it != NO_COLOR }
                        ?: enabledColor
                    disabledColor = Color.TRANSPARENT
                },

                strokeWidth = chocolateTypedArray.getDimension(R.styleable.chocolateViewAttributes_chocolate_StrokeWidth, 0f),
                strokeColors = ChocolateColorState().apply {
                    enabledColor = chocolateTypedArray.getColor(R.styleable.chocolateViewAttributes_chocolate_StrokeColor, Color.TRANSPARENT)
                    selectedColor = chocolateTypedArray.getColor(R.styleable.chocolateViewAttributes_chocolate_StrokeSelectedColor, NO_COLOR)
                        .takeIf { it != NO_COLOR }
                        ?: enabledColor
                    disabledColor = chocolateTypedArray.getColor(R.styleable.chocolateViewAttributes_chocolate_StrokeDisabledColor, NO_COLOR)
                        .takeIf { it != NO_COLOR }
                        ?: enabledColor
                },

                backgroundColors = ChocolateColorState().apply {
                    enabledColor = chocolateTypedArray.getColor(R.styleable.chocolateViewAttributes_chocolate_BackgroundColor, Color.TRANSPARENT)
                    selectedColor = chocolateTypedArray.getColor(R.styleable.chocolateViewAttributes_chocolate_BackgroundSelectedColor, NO_COLOR)
                        .takeIf { it != NO_COLOR }
                        ?: enabledColor
                    disabledColor = chocolateTypedArray.getColor(R.styleable.chocolateViewAttributes_chocolate_BackgroundDisabledColor, context.getColor(R.color.chocolate_button_background_disabled))
                },
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

    /**
     * [ChocolateBoxButton]에서 사용되는 뷰의 상태 변경 애니메이션
     */
    fun animateButtonStateChange(
        attributes: ChocolateAttribute,
        isEnabled: Boolean,
        backgroundView: View,
    ) {
        // 현재 색상 가져오기
        val currentColor = (backgroundView.background as? ColorDrawable)?.color
            ?: attributes.backgroundColors.disabledColor
            ?: Color.TRANSPARENT

        // 목표 색상 결정
        val targetColor = if (isEnabled)
            attributes.backgroundColors.enabledColor ?: Color.TRANSPARENT
            else attributes.backgroundColors.disabledColor ?: Color.TRANSPARENT

        // 색상 변경 애니메이션
        val backgroundAnimator = ValueAnimator.ofArgb(currentColor, targetColor).also {
            it.duration = 300 // 애니메이션 지속 시간
            it.interpolator = DecelerateInterpolator()
            it.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                backgroundView.backgroundTintList = ColorStateList.valueOf(animatedValue)
            }
        }
    }
}
