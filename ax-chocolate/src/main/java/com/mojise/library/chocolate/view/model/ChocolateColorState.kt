package com.mojise.library.chocolate.view.model

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.ColorInt

data class ChocolateColorState(
    @ColorInt var selectedColor: Int = Color.TRANSPARENT,
    @ColorInt var enabledColor: Int = Color.TRANSPARENT,
    @ColorInt var disabledColor: Int = Color.TRANSPARENT,
) {

    val isAllTransparent: Boolean
        get() = selectedColor == Color.TRANSPARENT && enabledColor == Color.TRANSPARENT && disabledColor == Color.TRANSPARENT

    fun toColorStateList() = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled),
            intArrayOf(-android.R.attr.state_selected, android.R.attr.state_enabled),
            intArrayOf(-android.R.attr.state_enabled),
        ),
        intArrayOf(
            selectedColor,
            enabledColor,
            disabledColor,
        )
    )

    @Deprecated("아직 사용 안 함")
    fun toColorStateListCompat(): ColorStateList? {
        val states = mutableListOf<IntArray>()
        val colors = mutableListOf<Int>()

        if (selectedColor != Color.TRANSPARENT) {
            states.add(intArrayOf(android.R.attr.state_selected))
            colors.add(selectedColor)
        }
        if (enabledColor != Color.TRANSPARENT) {
            states.add(intArrayOf(android.R.attr.state_enabled))
            colors.add(enabledColor)
        }
        if (disabledColor != Color.TRANSPARENT) {
            states.add(intArrayOf(-android.R.attr.state_enabled))
            colors.add(disabledColor)
        }
        if (states.isEmpty() || colors.isEmpty()) {
            return null
        }
        return ColorStateList(states.toTypedArray(), colors.toIntArray())
    }

    override fun toString(): String = buildString {
        append("ChocolateColorState(")
        append("selectedColor=#${selectedColor.toString(16).uppercase()}, ")
        append("enabledColor=#${enabledColor.toString(16).uppercase()}, ")
        append("disabledColor=#${disabledColor.toString(16).uppercase()})")
    }

    companion object {
        fun singleColor(@ColorInt color: Int) = ChocolateColorState(color, color, color)
        fun transparent() = singleColor(Color.TRANSPARENT)
    }
}