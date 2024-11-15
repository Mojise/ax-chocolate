package com.mojise.library.chocolate.view.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.ext.dp
import com.mojise.library.chocolate.util.TAG
import com.mojise.library.chocolate.view.model.ChocolateColorState

@SuppressLint("ResourceType")
internal class ChocolateViewHelper @JvmOverloads constructor(
    view: View,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) {

    val attributes: ChocolateAttribute

    init {
        //Log.d(TAG, "ChocolateViewManager init")

        // Android(android.R.attr.xxx) 속성을 가져오기 위한 TypedArray
        val androidTypedArray = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.background, android.R.attr.clickable), defStyleAttr, defStyleRes)
        // Chocolate(R.styleable.xxx) 속성을 가져오기 위한 TypedArray
        val chocolateTypedArray = context.obtainStyledAttributes(attrs, R.styleable.chocolateCommonAttributes)

        // android:background 속성 (Drawable)
        val androidBackgroundDrawable: Drawable? = try {
            view.isClickable = androidTypedArray.getBoolean(1, true)
            androidTypedArray.getDrawable(0)
        } catch (e: Exception) {
            Log.w(TAG, "Android attributes error", e)
            null
        } finally {
            androidTypedArray.recycle()
        }

        // Chocolate 속성
        attributes = try {
            val pressEffectStrengthInt = chocolateTypedArray.getInt(R.styleable.chocolateCommonAttributes_chocolatePressEffectStrengthLevel, -1)
            val pressEffectStrength = PressEffectStrength.valueOf(pressEffectStrengthInt)
            val pressEffectScaleRatio = pressEffectStrength?.value
                ?: chocolateTypedArray.getFloat(R.styleable.chocolateCommonAttributes_chocolatePressEffectScaleRatio, PressEffectStrength.Normal.value)

            ChocolateAttribute(
                isPressEffectEnabled = chocolateTypedArray.getBoolean(R.styleable.chocolateCommonAttributes_chocolatePressEffectEnabled, true),
                pressEffectScaleRatio = pressEffectScaleRatio,
                cornerRadius = chocolateTypedArray.getDimension(R.styleable.chocolateCommonAttributes_chocolateCornerRadius, CORNER_RADIUS_NORMAL_PIXEL.dp),

                ripplePosition = chocolateTypedArray.getInt(R.styleable.chocolateCommonAttributes_chocolateRippleApplyTo, DrawablePosition.Foreground.value)
                    .let(DrawablePosition::valueOf),
                rippleColors = ChocolateColorState().apply {
                    enabledColor = chocolateTypedArray.getColor(R.styleable.chocolateCommonAttributes_chocolateRippleColor, context.getColor(R.color.chocolate_ripple_color_black))
                    selectedColor = chocolateTypedArray.getColor(R.styleable.chocolateCommonAttributes_chocolateRippleSelectedColor, NO_COLOR)
                        .takeIf { it != NO_COLOR }
                        ?: enabledColor
                    disabledColor = Color.TRANSPARENT
                },

                strokeWidth = chocolateTypedArray.getDimension(R.styleable.chocolateCommonAttributes_chocolateStrokeWidth, 0f),
                strokeColors = ChocolateColorState().apply {
                    enabledColor = chocolateTypedArray.getColor(R.styleable.chocolateCommonAttributes_chocolateStrokeColor, Color.TRANSPARENT)
                    selectedColor = chocolateTypedArray.getColor(R.styleable.chocolateCommonAttributes_chocolateStrokeSelectedColor, NO_COLOR)
                        .takeIf { it != NO_COLOR }
                        ?: enabledColor
                    disabledColor = chocolateTypedArray.getColor(R.styleable.chocolateCommonAttributes_chocolateStrokeDisabledColor, NO_COLOR)
                        .takeIf { it != NO_COLOR }
                        ?: enabledColor
                },

                backgroundColors = ChocolateColorState().apply {
                    enabledColor = chocolateTypedArray.getColor(R.styleable.chocolateCommonAttributes_chocolateBackgroundColor, Color.TRANSPARENT)
                    selectedColor = chocolateTypedArray.getColor(R.styleable.chocolateCommonAttributes_chocolateBackgroundSelectedColor, NO_COLOR)
                        .takeIf { it != NO_COLOR }
                        ?: enabledColor
                    disabledColor = chocolateTypedArray.getColor(R.styleable.chocolateCommonAttributes_chocolateBackgroundDisabledColor, context.getColor(R.color.chocolate_button_background_disabled))
                },
            )
        } catch (e: Exception) {
            Log.w(TAG, "ChocolateAttribute error", e)
            ChocolateAttribute.Default
        } finally {
            chocolateTypedArray.recycle()
        }

        //Log.e(TAG, "attributes: $attributes")

        when (attributes.ripplePosition) {
            DrawablePosition.Foreground -> {
                view.foreground = ChocolateViewUtil.generateRippleDrawable(
                    cornerRadius = attributes.cornerRadius,
                    rippleColors = attributes.rippleColors,
                    strokeWidth = attributes.strokeWidth.toInt(),
                    strokeColors = attributes.strokeColors,
                    backgroundColors = null,
                )
                view.background = androidBackgroundDrawable
                    ?: ChocolateViewUtil.generateDrawable(
                        cornerRadius = attributes.cornerRadius,
                        strokeWidth = attributes.strokeWidth.toInt(),
                        strokeColors = attributes.strokeColors,
                        backgroundColors = attributes.backgroundColors,
                    )
            }
            DrawablePosition.Background -> {
                view.background = ChocolateViewUtil.generateRippleDrawable(
                    cornerRadius = attributes.cornerRadius,
                    rippleColors = attributes.rippleColors,
                    strokeWidth = attributes.strokeWidth.toInt(),
                    strokeColors = attributes.strokeColors,
                    backgroundColors = attributes.backgroundColors,
                )
            }
        }
    }

    fun showPressEffectOnTouch(
        view: View,
        event: MotionEvent?,
    ) {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> animateScale(view, true, attributes.pressEffectScaleRatio)
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> animateScale(view, false, attributes.pressEffectScaleRatio)
        }
    }

    private fun animateScale(
        view: View,
        isDown: Boolean,
        scaleRatio: Float
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

    internal data class ChocolateAttribute constructor(
        var isPressEffectEnabled: Boolean,
        var pressEffectScaleRatio: Float,
        var cornerRadius: Float,
        var rippleColors: ChocolateColorState,
        var ripplePosition: DrawablePosition,
        var strokeWidth: Float,
        var strokeColors: ChocolateColorState,
        var backgroundColors: ChocolateColorState,
    ) {
        override fun toString(): String = buildString {
            appendLine("ChocolateAttribute(")
            appendLine("    scaleRatio=$pressEffectScaleRatio")
            appendLine("    cornerRadius=$cornerRadius")
            appendLine("    strokeWidth=$strokeWidth")
            appendLine("    strokeColors=$strokeColors")
            appendLine("    rippleColors=$rippleColors")
            appendLine("    backgroundColors=$backgroundColors")
            appendLine(")")
        }
        companion object {
            val Default = ChocolateAttribute(
                isPressEffectEnabled = true,
                pressEffectScaleRatio = PressEffectStrength.Normal.value,
                cornerRadius = CORNER_RADIUS_NORMAL_PIXEL.dp,
                rippleColors = ChocolateColorState.transparent(),
                ripplePosition = DrawablePosition.Background,
                strokeWidth = 0f,
                strokeColors = ChocolateColorState.transparent(),
                backgroundColors = ChocolateColorState.transparent(),
            )
        }
    }

    internal enum class DrawablePosition(val value: Int) {
        Foreground(value = 0),
        Background(value = 1);

        companion object {
            fun valueOf(value: Int): DrawablePosition = entries.firstOrNull { it.value == value } ?: Background
        }
    }

    internal enum class PressEffectStrength(val level: Int, val value: Float) {
        LightTiny    (level = 0, value = 0.98f),
        Light        (level = 1, value = 0.96f),
        Normal       (level = 2, value = 0.94f),
        Heavy        (level = 3, value = 0.92f),
        HeavyStrong  (level = 4, value = 0.90f),
        HeavyExtreme (level = 5, value = 0.85f);

        companion object {
            fun valueOf(level: Int): PressEffectStrength? = entries.firstOrNull { it.level == level }
        }
    }

    companion object {
        const val NONE = 0
        const val NO_COLOR = Int.MAX_VALUE

        const val CORNER_RADIUS_NORMAL_PIXEL = 16f
    }
}
